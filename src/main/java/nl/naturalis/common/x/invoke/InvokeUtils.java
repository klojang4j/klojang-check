package nl.naturalis.common.x.invoke;

import nl.naturalis.common.ArrayMethods;
import nl.naturalis.common.ClassMethods;
import nl.naturalis.common.ExceptionMethods;
import nl.naturalis.common.invoke.InvokeException;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static java.lang.invoke.MethodHandles.*;
import static java.lang.invoke.MethodHandles.arrayElementSetter;
import static java.lang.invoke.MethodType.methodType;
import static java.lang.reflect.Modifier.isStatic;
import static nl.naturalis.common.ArrayMethods.pack;
import static nl.naturalis.common.ClassMethods.unbox;
import static nl.naturalis.check.Check.fail;

@SuppressWarnings({"unchecked", "rawtypes"})
public class InvokeUtils {

  private static final Map<Class<?>, MethodHandle> noArgConstructors = new HashMap<>();
  private static final Map<Class<?>, MethodHandle> intArgConstructors = new HashMap<>();
  private static final Set<String> NON_GETTERS = Set.of("getClass",
      "toString",
      "hashCode");

  private static final Class[] NARROW_TO_WIDE = pack(byte.class,
      short.class,
      int.class,
      long.class,
      float.class,
      double.class);

  public static boolean isDynamicallyAssignable(Object val, Class<?> type) {
    if (type.isInstance(val)) {
      return true;
    }
    if (!type.isPrimitive()) {
      return false;
    }
    Class clazz = unbox(val.getClass());
    if (!clazz.isPrimitive()) {
      return false;
    }
    type = type == char.class ? int.class : type;
    clazz = clazz == char.class ? int.class : clazz;
    for (Class c : NARROW_TO_WIDE) {
      if (clazz == c) {
        return true;
      }
      if (type == c) {
        return false;
      }
    }
    return fail("huh?");
  }

  public static <T> T newInstance(Class<T> clazz) {
    try {
      return (T) getNoArgConstructor(clazz).invoke();
    } catch (NoSuchMethodException e) {
      throw InvokeException.missingNoArgConstructor(clazz);
    } catch (Throwable t) {
      throw ExceptionMethods.uncheck(t);
    }
  }

  @SuppressWarnings({"unchecked"})
  public static <T> T newInstance(Class<T> clazz, int arg0) {
    try {
      return (T) getIntArgConstructor(clazz).invoke(arg0);
    } catch (NoSuchMethodException e) {
      throw InvokeException.noSuchConstructor(clazz, int.class);
    } catch (Throwable t) {
      throw ExceptionMethods.uncheck(t);
    }
  }

  @SuppressWarnings({"unchecked"})
  public static Object newArray(Class<?> clazz, int length) {
    MethodHandle mh = arrayConstructor(clazz);
    try {
      return mh.invoke(length);
    } catch (Throwable t) {
      throw ExceptionMethods.uncheck(t);
    }
  }

  public static int getArrayLength(Object array) {
    try {
      return (int) arrayLength(array.getClass()).invoke(array);
    } catch (Throwable t) {
      throw InvokeException.arrayOperationFailed(array, t);
    }
  }

  // Range check not included !!
  public static <T> T getArrayElement(Object array, int idx) {
    try {
      return (T) arrayElementGetter(array.getClass()).invoke(array, idx);
    } catch (Throwable t) {
      throw InvokeException.arrayOperationFailed(array, t);
    }
  }

  // Range check not included !!
  public static void setArrayElement(Object array, int idx, Object value) {
    try {
      arrayElementSetter(array.getClass()).invoke(array, idx, value);
    } catch (Throwable t) {
      throw InvokeException.arrayOperationFailed(array, t);
    }
  }

  public static void copyArrayElements(Object fromArray, Collection toCollection) {
    try {
      int len = (int) arrayLength(fromArray.getClass()).invoke(fromArray);
      if (len != 0) {
        MethodHandle mh = arrayElementGetter(fromArray.getClass());
        for (int i = 0; i < len; ++i) {
          toCollection.add(mh.invoke(fromArray, i));
        }
      }
    } catch (Throwable t) {
      throw InvokeException.arrayOperationFailed(fromArray, t);
    }
  }

  public static <T> MethodHandle getNoArgConstructor(Class<T> clazz)
      throws NoSuchMethodException, IllegalAccessException {
    MethodHandle mh = noArgConstructors.get(clazz);
    if (mh == null) {
      mh = publicLookup().findConstructor(clazz, methodType(void.class));
      noArgConstructors.put(clazz, mh);
    }
    return mh;
  }

  // Return MethodHandle for constructor taking a single argument of type int
  public static <T> MethodHandle getIntArgConstructor(Class<T> clazz)
      throws NoSuchMethodException, IllegalAccessException {
    MethodHandle mh = intArgConstructors.get(clazz);
    if (mh == null) {
      mh = publicLookup().findConstructor(clazz, methodType(void.class, int.class));
      intArgConstructors.put(clazz, mh);
    }
    return mh;
  }

  public static List<Method> getGetters(Class<?> clazz, boolean strict) {
    Method[] methods = clazz.getMethods();
    List<Method> getters = new ArrayList<>();
    for (Method m : methods) {
      if (isStatic(m.getModifiers())) {
        continue;
      } else if (m.getParameterCount() != 0) {
        continue;
      } else if (m.getReturnType() == void.class) {
        continue;
      } else if (NON_GETTERS.contains(m.getName())) {
        continue;
      } else if (strict && !clazz.isRecord() && !validGetterName(m)) {
        continue;
      }
      getters.add(m);
    }
    return getters;
  }

  public static List<Method> getSetters(Class<?> beanClass) {
    Method[] methods = beanClass.getMethods();
    List<Method> setters = new ArrayList<>();
    for (Method m : methods) {
      if (isStatic(m.getModifiers())) {
        continue;
      } else if (m.getParameterCount() != 1) {
        continue;
      } else if (m.getReturnType() != void.class) {
        continue;
      } else if (!validSetterName(m)) {
        continue;
      }
      setters.add(m);
    }
    return setters;
  }

  // NB This method will only be called when we have already established that the
  // method has a zero-length parameter list and a non-void return type, so we
  // don't repeat that check here.
  public static String getPropertyNameFromGetter(Method m, boolean strict) {
    if (m.getDeclaringClass().isRecord()) {
      return m.getName();
    }
    String n = m.getName();
    if ((m.getReturnType() == boolean.class || m.getReturnType() == Boolean.class)
        && n.length() > 2
        && n.startsWith("is")
        && isUpperCase(n.charAt(2))) {
      return extractName(n, 2);
    } else if (n.length() > 3 && n.startsWith("get") && isUpperCase(n.charAt(3))) {
      return extractName(n, 3);
    }
    if (!strict) {
      return n;
    }
    throw notAProperty(m, true);
  }

  /**
   * Returns the property name corresponding to the specified method, which is
   * assumed to be a setter. If the method cannot be identified as a setter, an
   * {@link IllegalArgumentException} is thrown.
   *
   * @param m The method from which to extract a property name
   * @return The name of the property corresponding to the method
   */
  public static String getPropertyNameFromSetter(Method m) {
    String n = m.getName();
    if (n.startsWith("set") && isUpperCase(n.charAt(3))) {
      return extractName(n, 3);
    }
    throw notAProperty(m, false);
  }

  private static String extractName(String n, int from) {
    StringBuilder sb = new StringBuilder(n.length() - 3);
    sb.append(n.substring(from));
    sb.setCharAt(0, toLowerCase(sb.charAt(0)));
    return sb.toString();
  }

  private static IllegalArgumentException notAProperty(Method m, boolean asGetter) {
    String fmt = "Method %s %s(%s) in class %s is not a %s";
    String rt = ClassMethods.simpleClassName(m.getReturnType());
    String clazz = ClassMethods.className(m.getDeclaringClass());
    String params = ArrayMethods.implode(m.getParameterTypes(),
        ClassMethods::simpleClassName);
    String type = asGetter ? "getter" : "setter";
    String msg = String.format(fmt, rt, m.getName(), params, clazz, type);
    return new IllegalArgumentException(msg);
  }

  private static boolean validGetterName(Method m) {
    String n = m.getName();
    if (n.length() > 4 && n.startsWith("get") && isUpperCase(n.charAt(3))) {
      return true;
    }
    if (n.length() > 3 && n.startsWith("is") && isUpperCase(n.charAt(2))) {
      return m.getReturnType() == boolean.class
          || m.getReturnType() == Boolean.class;
    }
    return false;
  }

  private static boolean validSetterName(Method m) {
    String n = m.getName();
    return n.length() > 3 && n.startsWith("set") && isUpperCase(n.charAt(3));
  }

}
