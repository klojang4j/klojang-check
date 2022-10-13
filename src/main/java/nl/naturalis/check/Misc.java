package nl.naturalis.check;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

import static java.lang.invoke.MethodHandles.arrayElementGetter;
import static java.lang.invoke.MethodHandles.arrayLength;
import static java.util.stream.Collectors.joining;

final class Misc {

  private static final String SEP = ", ";

  private interface Stringifier extends Function<Object, String> {}

  private Misc() {
    throw new UnsupportedOperationException();
  }

  static int getArrayLength(Object array) {
    try {
      return (int) arrayLength(array.getClass()).invoke(array);
    } catch (Throwable t) {
      throw new InvalidCheckException(t.toString());
    }
  }

  static String describe(Object obj) {
    if (obj == null) {
      return "null";
    } else if (obj instanceof Class clazz) {
      return simpleClassName(clazz) + ".class";
    } else if (obj instanceof Collection<?> c) {
      return c.getClass().getSimpleName() + '[' + c.size() + ']';
    } else if (obj instanceof Map<?, ?> m) {
      return m.getClass().getSimpleName() + '[' + m.size() + ']';
    } else if (obj.getClass().isArray()) {
      return ArrayInfo.describe(obj);
    }
    return obj.getClass().getSimpleName();
  }

  static String toShortString(Object obj, int maxWidth) {
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
    } else if ((obj instanceof String s && s.length() <= maxLen)
        || obj instanceof Number) {
      return obj.toString();
    }
    String s;
    if (obj instanceof Class<?> c) {
      s = simpleClassName(c);
    } else if (obj instanceof Collection<?> c) {
      Stringifier stringifier = o -> toShortString(o, maxLen, maxElems, maxEntries);
      String suffix = c.size() > maxElems ? ", ...]" : "]";
      s = '[' + implodeCollection(c, stringifier, maxElems) + suffix;
    } else if (obj instanceof Map<?, ?> m) {
      Stringifier stringifier = o -> toShortString(o, maxLen, maxElems, maxEntries);
      String suffix = m.size() > maxElems ? ", ...}" : "}";
      s = '{' + implodeCollection(m.entrySet(), stringifier, maxElems) + suffix;
    } else if (obj instanceof Map.Entry<?, ?> e) {
      s = entryToString(e, maxLen, maxElems, maxEntries);
    } else if (obj instanceof int[] ints) {
      s = imploded(implodeInts(ints, maxElems), ints.length, maxElems);
    } else if (obj instanceof Object[] objs) {
      Stringifier stringifier = o -> toShortString(o, maxLen, maxElems, maxEntries);
      s = imploded(implodeArray(objs, stringifier, maxElems), objs.length, maxElems);
    } else if (obj.getClass().isArray()) {
      Stringifier stringifier = o -> toShortString(o, maxLen, maxElems, maxEntries);
      s = imploded(
          implodeAny(obj, stringifier, maxElems), getArrayLength(obj), maxElems);
    } else {
      s = obj.toString();
    }
    return ellipsis(s, maxLen);
  }

  private static String imploded(String imploded, int len, int maxElems) {
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

  static String ellipsis(String str, int maxWidth) {
    if (str.length() <= maxWidth) {
      return str;
    }
    int newLen = Math.max(1, maxWidth - 3);
    return str.substring(0, newLen) + "...";
  }

  static String simpleClassName(Class<?> clazz) {
    if (clazz.isArray()) {
      return ArrayInfo.create(clazz).simpleName();
    }
    return clazz.getSimpleName();
  }

  static String className(Class<?> clazz) {
    if (clazz.isArray()) {
      return ArrayInfo.create(clazz).name();
    }
    return clazz.getName();
  }

  static String describe(int val) {
    return "int";
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

  static String implodeAny(Object array, Stringifier stringifier, int to) {
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
      throw new InvalidCheckException(t.toString());
    }
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private static <T> T getArrayElement(Object array, int idx) throws Throwable {
    return (T) arrayElementGetter(array.getClass()).invoke(array, idx);
  }

  private static int divUp(int value, int divideBy) {
    return (int) Math.ceil((double) value / (double) divideBy);
  }

}
