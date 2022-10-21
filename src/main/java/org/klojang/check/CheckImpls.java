package org.klojang.check;

import static org.klojang.check.x.Misc.getArrayLength;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import org.klojang.check.aux.Emptyable;

/*
 * Implementations of checks in the CommonChecks class that require more than 1 or 2
 * lines of code.
 */
final class CheckImpls {

  private static final Set<Class<?>> NULL_REPELLERS =
      // Actually, List.of(1) and List.of(1, 2) currently have the same type, but
      // better safe than sorry. They will anyhow be de-duplicated when entering
      // the HashSet
      Set.copyOf(new HashSet<>(
          Arrays.asList(
              Collections.emptyList().getClass(),
              Collections.emptySet().getClass(),
              List.of().getClass(),
              List.of(1).getClass(),
              List.of(1, 2).getClass(),
              List.of(1, 2, 3).getClass(),
              Set.of().getClass(),
              Set.of(1).getClass(),
              Set.of(1, 2).getClass(),
              Set.of(1, 2, 3).getClass())));

  static <T> boolean isEmpty(T arg) {
    return arg == null
        || (arg instanceof CharSequence cs && cs.length() == 0)
        || (arg instanceof Collection<?> c && c.size() == 0)
        || (isArray(arg) && getArrayLength(arg) == 0)
        || (arg instanceof Map<?, ?> m && m.size() == 0)
        || (arg instanceof Object[] x && x.length == 0)
        || (arg instanceof Optional<?> o && (o.isEmpty() || isEmpty(o.get())))
        || (arg instanceof Emptyable e && e.isEmpty())
        || (arg instanceof File f && fileSize(f) == 0)
        ;
  }

  static <T> boolean isNotEmpty(T arg) {
    return arg != null
        && (!(arg instanceof CharSequence cs) || cs.length() != 0)
        && (!(arg instanceof Collection<?> c) || c.size() != 0)
        && (!isArray(arg) || getArrayLength(arg) != 0)
        && (!(arg instanceof Map<?, ?> m) || m.size() != 0)
        && (!(arg instanceof Object[] x) || x.length != 0)
        && (!(arg instanceof Optional<?> o)
                || (o.isPresent() && isNotEmpty(o.get())))
        && (!(arg instanceof Emptyable e) || !e.isEmpty())
        && (!(arg instanceof File f) || fileSize(f) != 0)
        ;
  }

  static boolean isDeepNotEmpty(Object arg) {
    return arg != null
        && (!(arg instanceof CharSequence cs) || cs.length() > 0)
        && (!(arg instanceof Collection<?> c) || dne(c))
        && (!isArray(arg) || getArrayLength(arg) != 0)
        && (!(arg instanceof Map<?, ?> m) || dne(m))
        && (!(arg instanceof Object[] x) || dne(x))
        && (!(arg instanceof Optional<?> o) || dne(o))
        && (!(arg instanceof Emptyable e) || e.isDeepNotEmpty())
        && (!(arg instanceof File f) || isBlankFile(f))
        ;
  }

  static boolean isDeepNotNull(Object arg) {
    if (arg == null) {
      return false;
    } else if (arg instanceof Object[] o) {
      return Arrays.stream(o).allMatch(Objects::nonNull);
    } else if (arg instanceof Collection<?> c) {
      if (isNullRepellent(c)) {
        return true;
      }
      return c.stream().allMatch(Objects::nonNull);
    } else if (arg instanceof Map<?, ?> m) {
      return m.entrySet().stream()
          .allMatch(e -> e.getKey() != null && e.getValue() != null);
    }
    return true;
  }

  static <T, U extends T> boolean inArray(U elem, T[] array) {
    if (elem == null) {
      for (T e : array) {
        if (e == null) {
          return true;
        }
      }
      return false;
    } else {
      for (T e : array) {
        if (elem.equals(e)) {
          return true;
        }
      }
      return false;
    }
  }

  static <T> boolean isIndexOf(int idx, T obj) {
    if (idx < 0) {
      return false;
    } else if (obj instanceof String s) {
      return idx < s.length();
    } else if (obj instanceof List<?> l) {
      return idx < l.size();
    } else if (obj.getClass().isArray()) {
      try {
        return idx < getArrayLength(obj);
      } catch (Throwable t) {
        throw new InvalidCheckException(t.toString());
      }
    }
    throw new InvalidCheckException(
        "indexOf() not applicable to " + obj.getClass());
  }

  static <T> boolean isIndexInclusiveOf(int idx, T obj) {
    if (idx < 0) {
      return false;
    } else if (obj instanceof String s) {
      return idx <= s.length();
    } else if (obj instanceof List<?> l) {
      return idx <= l.size();
    } else if (obj.getClass().isArray()) {
      try {
        return idx <= getArrayLength(obj);
      } catch (Throwable t) {
        throw new InvalidCheckException(t.toString());
      }
    }
    throw new InvalidCheckException(
        "indexInclusiveOf() not applicable to " + obj.getClass());
  }

  private static boolean dne(Collection<?> coll) {
    if (coll.isEmpty()) {
      return false;
    }
    return coll.stream().allMatch(CheckImpls::isDeepNotEmpty);
  }

  private static boolean dne(Map<?, ?> map) {
    if (map.isEmpty()) {
      return false;
    }
    return map.entrySet().stream().allMatch(CheckImpls::entryDeepNotEmpty);
  }

  private static boolean entryDeepNotEmpty(Object obj) {
    var e = (Map.Entry<?, ?>) obj;
    return isDeepNotEmpty(e.getKey()) && isDeepNotEmpty(e.getValue());
  }

  private static boolean dne(Object[] arr) {
    if (arr.length == 0) {
      return false;
    }
    return Arrays.stream(arr).allMatch(CheckImpls::isDeepNotEmpty);
  }

  private static boolean dne(Optional<?> opt) {
    return opt.isPresent() && isDeepNotEmpty(opt.get());
  }

  private static boolean isNullRepellent(Collection<?> c) {
    return NULL_REPELLERS.contains(c.getClass());
  }

  private static boolean isArray(Object obj) {
    return obj.getClass().isArray();
  }

  private static long fileSize(File f) {
    try {
      return Files.size(f.toPath());
    } catch (IOException e) {
      throw new InvalidCheckException(e.toString());
    }
  }

  private static boolean isBlankFile(File f) {
    if (fileSize(f) != 0) {
      int BUF_SIZE = 128;
      try (InputStream in = new FileInputStream(f)) {
        try (var isr = new InputStreamReader(in, StandardCharsets.UTF_8)) {
          var buf = new char[BUF_SIZE];
          int x = isr.read(buf, 0, BUF_SIZE);
          while (x != -1) {
            for (int y = 0; y < x; ++y) {
              if (!Character.isWhitespace(buf[y])) {
                return false;
              }
            }
            x = isr.read(buf, 0, BUF_SIZE);
          }
        }
      } catch (IOException e) {
        throw new InvalidCheckException(e.toString());
      }
    }
    return true;
  }

}
