package org.klojang.check.x;

import org.klojang.check.CorruptCheckException;

import java.util.*;
import java.util.function.Function;

import static java.lang.invoke.MethodHandles.arrayElementGetter;
import static java.lang.invoke.MethodHandles.arrayLength;
import static java.util.stream.Collectors.joining;

public final class Misc {

  private static final String SEP = ", ";

  private interface Stringifier extends Function<Object, String> {}

  private Misc() {
    throw new UnsupportedOperationException();
  }

  public static int getArrayLength(Object array) {
    try {
      return (int) arrayLength(array.getClass()).invoke(array);
    } catch (Throwable t) {
      throw new CorruptCheckException(t.toString());
    }
  }

  public static String describe(Object obj) {
    if (obj == null) {
      return "null";
    } else if (obj.getClass() == Class.class) {
      return simpleClassName((Class<?>) obj) + ".class";
    } else if (obj instanceof Collection<?> c) {
      return c.getClass().getSimpleName() + '[' + c.size() + ']';
    } else if (obj instanceof Map<?, ?> m) {
      return m.getClass().getSimpleName() + '[' + m.size() + ']';
    } else if (obj.getClass().isArray()) {
      return ArrayInfo.describe(obj);
    }
    return obj.getClass().getSimpleName();
  }

  public static String toShortString(Object obj, int maxWidth) {
    int maxElements = divUp(maxWidth, 8);
    int maxEntries = divUp(maxWidth, 16);
    return toShortString(obj, maxWidth, maxElements, maxEntries);
  }

  static String toShortString(Object obj,
      int maxLen,
      int maxElems,
      int maxEntries) {
    if (obj == null) {
      return "null";
    } else if (obj.getClass() == String.class || obj.getClass() == Integer.class) {
      // serve String & int as quickly as possible
      return ellipsis(obj.toString(), maxLen);
    }
    String s;
    if (obj.getClass() == Class.class) {
      s = simpleClassName((Class<?>) obj);
    } else if (obj instanceof Collection<?> c) {
      Stringifier stringifier = o -> toShortString(o, maxLen, maxElems, maxEntries);
      s = delimit0(implodeCollection(c, stringifier, maxElems), c.size(), maxElems);
    } else if (obj instanceof Map<?, ?> m) {
      Stringifier stringifier = o -> toShortString(o, maxLen, maxElems, maxEntries);
      s = delimit1(implodeCollection(m.entrySet(), stringifier, maxElems),
          m.size(),
          maxElems);
    } else if (obj instanceof Map.Entry<?, ?> e) {
      s = entryToString(e, maxLen, maxElems, maxEntries);
    } else if (obj.getClass() == int[].class) {
      int[] ints = (int[]) obj;
      s = delimit0(implodeInts(ints, maxElems), ints.length, maxElems);
    } else if (obj instanceof Object[] objs) {
      Stringifier stringifier = o -> toShortString(o, maxLen, maxElems, maxEntries);
      s = delimit0(implodeArray(objs, stringifier, maxElems), objs.length, maxElems);
    } else if (obj.getClass().isArray()) {
      Stringifier stringifier = o -> toShortString(o, maxLen, maxElems, maxEntries);
      s = delimit0(implodeAny(obj, stringifier, maxElems),
          getArrayLength(obj),
          maxElems);
    } else {
      s = obj.toString();
    }
    return ellipsis(s, maxLen);
  }

  private static String delimit0(String imploded, int len, int maxElems) {
    if (len == 0) {
      return "[]";
    } else {
      String suffix;
      if (len <= maxElems) {
        suffix = "]";
      } else {
        suffix = " (+" + (len - maxElems) + ")]";
      }
      return '[' + imploded + suffix;
    }
  }

  private static String delimit1(String imploded, int len, int maxElems) {
    if (len == 0) {
      return "{}";
    } else {
      String suffix;
      if (len <= maxElems) {
        suffix = "}";
      } else {
        suffix = " (+" + (len - maxElems) + ")}";
      }
      return '{' + imploded + suffix;
    }
  }

  public static String ellipsis(String str, int maxWidth) {
    if (str.length() <= maxWidth) {
      return str;
    }
    int newLen = Math.max(1, maxWidth - 3);
    return str.substring(0, newLen) + "...";
  }

  public static String simpleClassName(Class<?> clazz) {
    if (clazz.isArray()) {
      return ArrayInfo.create(clazz).simpleName();
    }
    return clazz.getSimpleName();
  }

  public static String className(Class<?> clazz) {
    if (clazz.isArray()) {
      return ArrayInfo.create(clazz).name();
    }
    return clazz.getName();
  }

  private static String entryToString(Map.Entry<?, ?> e,
      int maxLen,
      int maxElems,
      int maxEntries) {
    String k = toShortString(e.getKey(), maxLen, maxElems, maxEntries);
    String v = toShortString(e.getValue(), maxLen, maxElems, maxEntries);
    return k + '=' + v;
  }

  private static <T> String implodeCollection(Collection<T> collection,
      Stringifier stringifier,
      int maxElems) {
    int x = Math.min(collection.size(), maxElems);
    return collection.stream().limit(x).map(stringifier).collect(joining(SEP));
  }

  private static <T> String implodeArray(T[] array, Stringifier stringifier,
      int maxElems) {
    int x = Math.min(array.length, maxElems);
    return Arrays.stream(array, 0, x).map(stringifier).collect(joining(SEP));
  }

  private static String implodeInts(int[] array,
      int maxElems) {
    int x = Math.min(array.length, maxElems);
    return Arrays.stream(array, 0, x)
        .mapToObj(String::valueOf)
        .collect(joining(SEP));
  }

  public static String implodeAny(Object array, Stringifier stringifier, int to) {
    int len = getArrayLength(array);
    int x = Math.min(to, len);
    StringBuilder sb = new StringBuilder();
    try {
      for (int i = 0; i < x; ++i) {
        if (i != 0) {
          sb.append(SEP);
        }
        Object o = getArrayElement(array, i);
        sb.append(stringifier.apply(o));
      }
    } catch (Throwable t) {
      throw new CorruptCheckException(t.toString());
    }
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private static <T> T getArrayElement(Object array, int idx)
      throws Throwable {
    return (T) arrayElementGetter(array.getClass()).invoke(array, idx);
  }

  private static int divUp(int value, int divideBy) {
    return (int) Math.ceil((double) value / (double) divideBy);
  }

  public static CorruptCheckException typeNotSupported(Class<?> type) {
    return new CorruptCheckException("type not supported: " + type);
  }

  public static CorruptCheckException notApplicable(String check, Object arg) {
    String msg = String.format("[%s] check not applicable to %s", check, arg.getClass());
    return new CorruptCheckException(msg);
  }

}
