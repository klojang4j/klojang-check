package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.common.collection.IntList;
import nl.naturalis.common.x.Param;

import java.util.*;

import static java.lang.Character.toLowerCase;
import static nl.naturalis.check.CommonExceptions.indexOutOfBounds;
import static nl.naturalis.common.ArrayMethods.*;
import static nl.naturalis.common.MathMethods.divUp;
import static nl.naturalis.common.ObjectMethods.ifNull;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.common.ObjectMethods.isEmpty;
import static nl.naturalis.common.x.invoke.InvokeUtils.getArrayLength;

/**
 * Methods for working with strings. Quite a few methods in this class are geared
 * towards printing. They take an argument of type {@code Object}, rather than
 * {@code String}. If the argument is {@code null}, they will return an empty string,
 * else they will call {@code toString()} on the argument and then manipulate the
 * resulting {@code String}. They are null-safe and they will never return
 * {@code null} themselves. The parameter name for the {@code Object} argument will
 * be "input". For ease of reading the {@code input} parameter will still be referred
 * to as a {@code String}.
 */
public final class StringMethods {

  /**
   * The empty string.
   */
  public static final String EMPTY_STRING = "";

  private StringMethods() {}

  /**
   * Appends the specified value to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the value to
   * @param val The value to append
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(StringBuilder sb, Object val) {
    return Check.notNull(sb, "sb").ok().append(val);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(StringBuilder sb, Object val0, Object val1) {
    return Check.notNull(sb, "sb").ok().append(val0).append(val1);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @param val2 another value
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(StringBuilder sb,
      Object val0,
      Object val1,
      Object val2) {
    return Check.notNull(sb, "sb").ok().append(val0).append(val1).append(val2);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @param val2 another value
   * @param val3 another value
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(
      StringBuilder sb, Object val0, Object val1, Object val2, Object val3) {
    return Check.notNull(sb, "sb")
        .ok()
        .append(val0)
        .append(val1)
        .append(val2)
        .append(val3);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @param val2 another value
   * @param val3 another value
   * @param val4 another value
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(
      StringBuilder sb,
      Object val0,
      Object val1,
      Object val2,
      Object val3,
      Object val4) {

    return Check.notNull(sb, "sb")
        .ok()
        .append(val0)
        .append(val1)
        .append(val2)
        .append(val3)
        .append(val4);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @param val2 another value
   * @param val3 another value
   * @param val4 another value
   * @param val5 another value
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(
      StringBuilder sb,
      Object val0,
      Object val1,
      Object val2,
      Object val3,
      Object val4,
      Object val5) {

    return Check.notNull(sb, "sb")
        .ok()
        .append(val0)
        .append(val1)
        .append(val2)
        .append(val3)
        .append(val4)
        .append(val5);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @param val2 another value
   * @param val3 another value
   * @param val4 another value
   * @param val5 another value
   * @param val6 another value
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(
      StringBuilder sb,
      Object val0,
      Object val1,
      Object val2,
      Object val3,
      Object val4,
      Object val5,
      Object val6) {

    return Check.notNull(sb, "sb")
        .ok()
        .append(val0)
        .append(val1)
        .append(val2)
        .append(val3)
        .append(val4)
        .append(val5)
        .append(val6);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @param val2 another value
   * @param val3 another value
   * @param val4 another value
   * @param val5 another value
   * @param val6 another value
   * @param val7 another value
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(
      StringBuilder sb,
      Object val0,
      Object val1,
      Object val2,
      Object val3,
      Object val4,
      Object val5,
      Object val6,
      Object val7) {

    return Check.notNull(sb, "sb")
        .ok()
        .append(val0)
        .append(val1)
        .append(val2)
        .append(val3)
        .append(val4)
        .append(val5)
        .append(val6)
        .append(val7);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @param val2 another value
   * @param val3 another value
   * @param val4 another value
   * @param val5 another value
   * @param val6 another value
   * @param val7 another value
   * @param val8 another value
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(
      StringBuilder sb,
      Object val0,
      Object val1,
      Object val2,
      Object val3,
      Object val4,
      Object val5,
      Object val6,
      Object val7,
      Object val8) {

    return Check.notNull(sb, "sb")
        .ok()
        .append(val0)
        .append(val1)
        .append(val2)
        .append(val3)
        .append(val4)
        .append(val5)
        .append(val6)
        .append(val7)
        .append(val8);
  }

  /**
   * Appends the specified values to the specified {@code StringBuilder} and returns
   * the {@code StringBuilder}.
   *
   * @param sb the {@code StringBuilder} to append the values to
   * @param val0 a value
   * @param val1 another value
   * @param val2 another value
   * @param val3 another value
   * @param val4 another value
   * @param val5 another value
   * @param val6 another value
   * @param val7 another value
   * @param val8 another value
   * @param val9 another value
   * @param moreData more values
   * @return the {@code StringBuilder}
   */
  public static StringBuilder append(
      StringBuilder sb,
      Object val0,
      Object val1,
      Object val2,
      Object val3,
      Object val4,
      Object val5,
      Object val6,
      Object val7,
      Object val8,
      Object val9,
      Object... moreData) {

    Check.notNull(sb, "sb")
        .ok()
        .append(val0)
        .append(val1)
        .append(val2)
        .append(val3)
        .append(val4)
        .append(val5)
        .append(val6)
        .append(val7)
        .append(val8)
        .append(val9);
    Check.notNull(moreData, "val").ok(Arrays::stream).forEach(sb::append);
    return sb;
  }

  /**
   * Concatenates the specified data.
   *
   * @param data the data to append (must not be null)
   * @return the concatenation of the data
   */
  public static String concat(Object... data) {
    Check.notNull(data);
    StringBuilder sb = new StringBuilder(10 * data.length);
    Arrays.stream(data).forEach(sb::append);
    return sb.toString();
  }

  /**
   * Counts the number of occurrences of {@code substr} within {@code input}. Returns
   * 0 (zero) if {@code input} is {@code null}.
   *
   * @param input the string to search
   * @param substr the substring to search for (must not be {@code null} or
   *     empty)
   * @return the number of occurrences of {@code substr} within {@code input}
   */
  public static int count(Object input, String substr) {
    return count(input, substr, false);
  }

  /**
   * Counts the number of occurrences of {@code substr} within {@code input}. Returns
   * 0 (zero) if {@code input} is {@code null}.
   *
   * @param input the string to search
   * @param substr the substring to search for (must not be {@code null} or
   *     empty)
   * @param ignoreCase whether to ignore case while comparing substrings
   * @return the number of occurrences of {@code substr} within {@code input}
   */
  public static int count(Object input, String substr, boolean ignoreCase) {
    return count(input, substr, ignoreCase, 0);
  }

  /**
   * Counts the number of occurrences of {@code substr} within {@code input}. Returns
   * 0 (zero) if {@code input} is {@code null}.
   *
   * @param input the string to search
   * @param substr the substring to search for (must not be {@code null} or
   *     empty)
   * @param ignoreCase whether to ignore case while comparing substrings
   * @param limit the maximum number of occurrences the count. You may specify 0
   *     (zero) for "no maximum".
   * @return the number of occurrences of {@code substr} within {@code input} (will
   *     not exceed {@code limit})
   */
  public static int count(Object input,
      String substr,
      boolean ignoreCase,
      int limit) {
    Check.that(substr, "substr").isNot(empty());
    Check.that(limit, "limit").is(gte(), 0);
    String str;
    if (input == null || (str = input.toString()).length() < substr.length()) {
      return 0;
    }
    if (substr.length() == 1) {
      return count(str, substr.charAt(0), ignoreCase, limit);
    }
    int count = 0;
    for (int i = 0; i <= str.length() - substr.length(); ++i) {
      if (str.regionMatches(ignoreCase, i, substr, 0, substr.length())) {
        if (++count == limit) {
          break;
        }
      }
    }
    return count;
  }

  /**
   * Counts the number of non-overlapping occurrences of {@code substr} within
   * {@code input}. The string to search for must not be null or empty and is not
   * treated as a regular expression. Returns 0 (zero) if {@code input} is
   * {@code null}.
   *
   * @param input the string to search
   * @param substr the substring to search for
   * @return the number of non-overlapping occurrences of {@code substr} within
   *     {@code input}
   */
  public static int countDiscrete(Object input, String substr) {
    return countDiscrete(input, substr, false, 0);
  }

  /**
   * Counts the number of non-overlapping occurrences of {@code substr} within
   * {@code input}. The string to search for must not be null or empty and is not
   * treated as a regular expression. Returns 0 (zero) if {@code input} is
   * {@code null}.
   *
   * @param input the string to search
   * @param substr the substring to search for
   * @param ignoreCase whether to ignore case while comparing substrings
   * @return the number of non-overlapping occurrences of {@code substr} within
   *     {@code input}
   */
  public static int countDiscrete(Object input,
      String substr,
      boolean ignoreCase) {
    return countDiscrete(input, substr, ignoreCase, 0);
  }

  /**
   * Counts the number of non-overlapping occurrences of {@code substr} within
   * {@code input}. Returns 0 (zero) if {@code input} is {@code null}.
   *
   * @param input the string to search
   * @param substr the substring to search for
   * @param ignoreCase whether to ignore case while comparing substrings
   * @param limit the maximum number of occurrences the count. You may specify 0
   *     (zero) for "no maximum".
   * @return the number of non-overlapping occurrences of {@code substr} within
   *     {@code input} (will not exceed {@code limit})
   */
  public static int countDiscrete(Object input,
      String substr,
      boolean ignoreCase,
      int limit) {
    Check.that(substr, "substr").isNot(empty());
    Check.that(limit, "limit").is(gte(), 0);
    String str;
    if (input == null || (str = input.toString()).length() < substr.length()) {
      return 0;
    }
    if (substr.length() == 1) {
      return count(str, substr.charAt(0), ignoreCase, limit);
    }
    int count = 0;
    int i = 0;
    do {
      if (str.regionMatches(ignoreCase, i, substr, 0, substr.length())) {
        if (++count == limit) {
          break;
        }
        i += substr.length();
      } else {
        i += 1;
      }
    } while (i <= str.length() - substr.length());
    return count;
  }

  private static int count(String str, char c, boolean ignoreCase, int limit) {
    int count = 0;
    if (ignoreCase) {
      char c0 = toLowerCase(c);
      for (int i = 0; i < str.length(); ++i) {
        if (toLowerCase(str.charAt(i)) == c0 && ++count == limit) {
          break;
        }
      }
    } else {
      for (int i = 0; i < str.length(); ++i) {
        if (str.charAt(i) == c && ++count == limit) {
          break;
        }
      }
    }
    return count;
  }

  /**
   * Returns {@code input.toString()} if its length does not exceed {@code maxWidth},
   * else truncates the string and appends "...", such that the new string's length
   * does not exceed {@code maxWidth}. The lower bound for {@code maxWidth} will
   * tacitly be clamped to 4, so that at least one letter of the string is
   * displayed.
   *
   * <h4>Examples:</h4>
   *
   * <p>
   *
   * <pre>
   * String hello = "Hello World, how are you?";
   * assertEquals("Hello W...", ellipsis(hello, 10));
   * assertEquals("H...", ellipsis(hello, 4));
   * assertEquals(hello, ellipsis(hello, 100));
   * </pre>
   *
   * @param input the string to abbreviate, if necessary
   * @param maxWidth the maximum width of the string (must be greater than 3)
   * @return the string itself or an abbreviated version, suffixed with "..."
   */
  public static String ellipsis(Object input, int maxWidth) {
    if (input != null) {
      return ellipsis0(input.toString(), maxWidth);
    }
    return EMPTY_STRING;
  }

  /**
   * Returns a short string representation of an object. Equivalent to
   * {@link #toShortString(Object, int) toShortString(obj, 50)}.
   *
   * @param obj the object to stringify
   * @return a string consisting of no more than 50 characters
   */
  public static String toShortString(Object obj) {
    return toShortString(obj, 50);
  }

  /**
   * Returns a short string representation of an object. Roughly equivalent to
   * {@link #toShortString(Object, int, int, int) toShortString(obj, maxWidth,
   * maxWidth/8, maxWidth/16)}.
   *
   * @param obj the object to stringify
   * @param maxWidth the maximum width of the returned string
   * @return a string whose length will not exceed {@code maxWidth}
   */
  public static String toShortString(Object obj, int maxWidth) {
    int maxElements = divUp(maxWidth, 8);
    int maxEntries = divUp(maxWidth, 16);
    return toShortString(obj, maxWidth, maxElements, maxEntries);
  }

  /**
   * Returns a short string representation of an object. Broadly speaking, this
   * method behaves as follows:
   * <ul>
   *   <li>if {@code obj} is {@code null}, it is stringified to "null".
   *   <li>if {@code obj} is a {@code Class} object, it is stringified using
   *   {@link ClassMethods#simpleClassName(Class) ClassMethods.simpleClassName}.
   *   <li>if {@code obj} is a {@code Collection}, it is stringified as though by
   *   calling {@code toString()} on the collection after all but the first
   *   <b>{@code maxElements}</b> elements have been removed from it. (This prevents
   *   collections from blowing up into huge strings, only for them to be truncated
   *   to {@code maxWidth} again. So there is both a performance aspect and a
   *   security aspect to making the maximum number of elements user-definable.)
   *   <li>if {@code obj} is an array, it is stringified as though by calling
   *   {@link Arrays#deepToString(Object[]) Arrays.deepToString} on it after all but
   *   the first <b>{@code maxElements}</b> elements have been removed from it.
   *   <li>if {@code obj} is a {@code Map}, it is stringified as though by calling
   *   {@code toString()} on the collection after all but the first
   *   <b>{@code maxEntries}</b> entries have been removed from it.
   *   <li>Otherwise {@code obj} is stringified simply by calling {@code toString}
   *   on it.
   * </ul>
   * <p>
   * The resulting string is then truncated to <b>{@code maxWidth}</b> as though by
   * calling {@link #ellipsis(Object, int) ellipsis} on it.
   * <p>
   * <i>You <b>should not</b> rely on the exact appearance of the returned
   * string.</i> Future implementations may produce slightly different strings for
   * the same object. (For example, they might give special treatment to certain
   * other types of objects.) The only stated aim of this method is to provide a
   * <i>length-constrained</i> string representation of an object. The exact contents
   * of the returned string is unspecified.
   *
   * @param obj the object to stringify
   * @param maxWidth the maximum width of the returned string
   * @param maxElements the maximum number of elements to process if the argument
   *     is an array or {@code Collection}.
   * @param maxEntries the maximum number of entries to process if the argument
   *     is a {@code Map}.
   * @return a string whose length will not exceed {@code maxWidth}
   */
  public static String toShortString(Object obj,
      int maxWidth,
      int maxElements,
      int maxEntries) {
    // Don't use nl.naturalis.check here, as that package heavily relies on this
    // method again.
    if (maxWidth < 0 || maxElements < 0 || maxEntries < 0) {
      throw new IllegalArgumentException(
          "maxWidth, maxElements and maxEntries must all be positive");
    }
    if (obj == null) {
      return "null";
    } else if (obj.getClass() == String.class || obj instanceof Number) {
      // identify strings and numbers as quickly as possible, even though we will
      // end up calling obj.toString() again
      return ellipsis0(obj.toString(), maxWidth);
    }
    String s;
    if (obj instanceof Class<?> c) {
      s = ClassMethods.simpleClassName(c);
    } else if (obj instanceof Collection<?> c) {
      String x = c.size() > maxElements ? ", ...]" : "]";
      //@formatter:off
      s = '[' + CollectionMethods.implode(c, o -> toShortString(o, maxWidth, maxElements, maxEntries), ", ", 0, maxElements) + x;
      //@formatter:ons
    } else if (obj instanceof Map<?, ?> m) {
      String x = m.size() > maxElements ? ", ...}" : "}";
      //@formatter:off
      s = '{' + CollectionMethods.implode(m.entrySet(), o -> toShortString(o, maxWidth, maxElements, maxEntries), ", ", 0, maxEntries) + x;
      //@formatter:ons
    } else if (obj instanceof Map.Entry<?, ?> e) {
      //@formatter:off
      s = toShortString(e.getKey(), maxWidth, maxElements, maxEntries) + '=' + toShortString(e.getValue(), maxWidth, maxElements, maxEntries);
      //@formatter:ons
    } else if (obj instanceof int[] ints) {
      String x = ints.length > maxElements ? ", ...]" : "]";
      //@formatter:off
      s = '['+ ArrayMethods.implodeInts((int[])obj, String::valueOf, ", ", 0, maxElements) + x;
      //@formatter:on
    } else if (obj instanceof Object[] objs) {
      String x = objs.length > maxElements ? ", ...]" : "]";
      //@formatter:off
      s = '[' + ArrayMethods.implode(objs, o -> toShortString(o, maxWidth, maxElements, maxEntries), ", ", 0, maxElements) + x;
      //@formatter:on
    } else if (obj.getClass().isArray()) {
      String x = getArrayLength(obj) > maxElements ? ", ...]" : "]";
      //@formatter:off
      s = '[' + ArrayMethods.implodeAny(obj, String::valueOf, ", ", 0, maxElements) + x;
      //@formatter:on
    } else if (obj instanceof IntList il) {
      int y = Math.min(il.size(), maxElements + 1);
      //@formatter:off
      return toShortString(il.toArray(0, y), maxWidth, maxElements, maxEntries);
      //@formatter:on
    } else {
      s = obj.toString();
    }
    return ellipsis0(s, maxWidth);
  }

  private static String ellipsis0(String str, int maxWidth) {
    maxWidth = Math.max(4, maxWidth);
    if (str.length() > maxWidth) {
      return str.substring(0, maxWidth - 3) + "...";
    }
    return str;
  }

  /**
   * Determines whether {@code input} starts with any of the specified prefixes.
   * Returns an {@code Optional} containing the first prefix found to be equal to the
   * end of the string, or an empty {@code Optional} if the string does not end with
   * any of the specified prefixes.
   *
   * @param input the string to test
   * @param ignoreCase whether to ignore case
   * @param prefixes the prefixes to test
   * @return Returns an {@code Optional} containing the first prefix found to be
   *     equal to the end of the string, or an empty {@code Optional} if the string
   *     does not end with any of the specified prefixes.
   */
  public static Optional<String> startsWith(Object input,
      boolean ignoreCase,
      Collection<String> prefixes) {
    Check.notNull(prefixes, "prefixes");
    return startsWith(input, ignoreCase, prefixes.toArray(String[]::new));
  }

  /**
   * Determines whether {@code input} starts with any of the specified prefixes.
   * Returns an {@code Optional} containing the first prefix found to be equal to the
   * end of the string, or an empty {@code Optional} if the string does not end with
   * any of the specified prefixes.
   *
   * @param input the string to test
   * @param ignoreCase whether to ignore case
   * @param prefixes the prefixes to test
   * @return Returns an {@code Optional} containing the first prefix found to be
   *     equal to the end of the string, or an empty {@code Optional} if the string
   *     does not end with any of the specified prefixes.
   */
  public static Optional<String> startsWith(Object input,
      boolean ignoreCase,
      String... prefixes) {
    Check.that(prefixes, "prefixes").is(deepNotEmpty());
    if (input == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(startsWith0(input.toString(), ignoreCase, prefixes));
  }

  private static String startsWith0(String str,
      boolean ignoreCase,
      String[] prefixes) {
    if (!isEmpty(str)) {
      for (String prefix : prefixes) {
        if (str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length())) {
          return prefix;
        }
      }
    }
    return null;
  }

  /**
   * Determines whether {@code input} ends with any of the specified suffixes.
   * Returns an {@code Optional} containing the first suffix found to be equal to the
   * end of the string, or an empty {@code Optional} if the string does not end with
   * any of the specified suffixes.
   *
   * @param input the string to test
   * @param ignoreCase whether to ignore case
   * @param suffixes the suffixes to test
   * @return Returns an {@code Optional} containing the first suffix found to be
   *     equal to the end of the string, or an empty {@code Optional} if the string
   *     does not end with any of the specified suffixes.
   */
  public static Optional<String> endsWith(Object input,
      boolean ignoreCase,
      Collection<String> suffixes) {
    Check.notNull(suffixes, "suffixes");
    return endsWith(input, ignoreCase, suffixes.toArray(String[]::new));
  }

  /**
   * Determines whether {@code input} ends with any of the specified suffixes.
   * Returns an {@code Optional} containing the first suffix found to be equal to the
   * end of the string, or an empty {@code Optional} if the string does not end with
   * any of the specified suffixes.
   *
   * @param input the string to test
   * @param ignoreCase whether to ignore case
   * @param suffixes the suffixes to test
   * @return Returns an {@code Optional} containing the first suffix found to be
   *     equal to the end of the string, or an empty {@code Optional} if the string
   *     does not end with any of the specified suffixes.
   */
  public static Optional<String> endsWith(Object input,
      boolean ignoreCase,
      String... suffixes) {
    Check.that(suffixes, "suffixes").is(deepNotEmpty());
    if (input == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(endsWith0(input.toString(), ignoreCase, suffixes));
  }

  private static String endsWith0(String str,
      boolean ignoreCase,
      String[] suffixes) {
    if (!isEmpty(str)) {
      for (String suffix : suffixes) {
        int len = suffix.length();
        int off = str.length() - len;
        if (str.regionMatches(ignoreCase, off, suffix, 0, len)) {
          return suffix;
        }
      }
    }
    return null;
  }

  /**
   * Prefixes to specified prefix to {@code input} if it did not already start with
   * that prefix. Returns {@code prefix} if {@code input} is null,
   *
   * @param input the {@code String} to which to append the prefix
   * @param prefix the prefix (must not be {@code null})
   * @return a string that is guaranteed to start with {@code prefix}
   */
  public static String ensurePrefix(Object input, String prefix) {
    Check.notNull(prefix, "prefix");
    if (input == null) {
      return prefix;
    }
    String str = input.toString();
    return str.startsWith(prefix) ? str : prefix + str;
  }

  /**
   * Appends to specified suffix to {@code input} if it did not already have that
   * suffix. If {@code input} is null, {@code suffix} is returned.
   *
   * @param input the {@code String} to which to append the suffix
   * @param suffix the suffix (must not be {@code null})
   * @return a string that is guaranteed to end with {@code suffix}
   */
  public static String ensureSuffix(Object input, String suffix) {
    Check.notNull(suffix, "suffix");
    if (input == null) {
      return suffix;
    }
    String str = input.toString();
    return str.endsWith(suffix) ? str : str + suffix;
  }

  /**
   * Whether the specified string is null or blank.
   *
   * @param input the string
   * @return whether it is null or blank
   */
  public static boolean isBlank(Object input) {
    return input == null || input.toString().isBlank();
  }

  /**
   * Returns the 1st argument if it is not a whitespace-only string, else the 2nd
   * argument.
   *
   * @param input the string to return if not null
   * @param dfault the replacement string
   * @see ObjectMethods#ifNull(Object, Object)
   */
  public static String ifBlank(Object input, String dfault) {
    return isBlank(input) ? dfault : input.toString();
  }

  /**
   * Removes all occurrences of the specified prefixes from the start of a string.
   * The returned string will no longer start with any of the specified prefixes.
   *
   * @param input the string to remove the prefixes from
   * @param prefixes the prefixes to remove
   */
  public static String lchop(Object input, String... prefixes) {
    return lchop(input, false, prefixes);
  }

  /**
   * Removes all occurrences of the specified prefixes from the start of a string.
   * The returned string will no longer start with any of the specified prefixes.
   *
   * @param input the string to remove the prefixes from
   * @param ignoreCase whether to ignore case
   * @param prefixes the prefixes to remove
   */
  public static String lchop(Object input,
      boolean ignoreCase,
      String... prefixes) {
    Check.that(prefixes, "prefixes").is(deepNotEmpty());
    String str;
    if (input == null || (str = input.toString()).isEmpty()) {
      return EMPTY_STRING;
    }
    boolean found;
    int offset = 0;
    do {
      found = false;
      for (String prefix : prefixes) {
        if (str.regionMatches(ignoreCase, offset, prefix, 0, prefix.length())) {
          offset += prefix.length();
          found = true;
        }
      }
    } while (found);
    return str.substring(offset);
  }

  /**
   * Removes all occurrences of the specified suffixes from the end of a string. The
   * returned string will no longer end with any of the specified suffixes.
   *
   * @param input the string to manipulate
   * @param suffixes the suffixes to chop off the right of the string
   * @return a String that does not end with any of the specified suffixes
   */
  public static String rchop(Object input, String... suffixes) {
    return rchop(input, false, suffixes);
  }

  /**
   * Removes all occurrences of the specified suffixes from the end of a string. The
   * returned string will no longer end with any of the specified suffixes.
   *
   * @param input the string to manipulate
   * @param ignoreCase whether to ignore case while chopping off suffixes
   * @param suffixes a String that does not end with any of the specified
   *     suffixes
   */
  public static String rchop(Object input,
      boolean ignoreCase,
      String... suffixes) {
    Check.that(suffixes, "suffixes").is(deepNotEmpty());
    String str;
    if (input == null || (str = input.toString()).isEmpty()) {
      return EMPTY_STRING;
    }
    boolean found;
    int offset = str.length();
    do {
      found = false;
      for (String suffix : suffixes) {
        int sl = suffix.length();
        if (str.regionMatches(ignoreCase, offset - sl, suffix, 0, sl)) {
          offset -= sl;
          found = true;
        }
      }
    } while (found);
    return str.substring(0, offset);
  }

  /**
   * Ensures that the first character of the specified string is not a lowercase
   * character.
   *
   * @param input the string
   * @return the same string except that the first character is not a lowercase
   *     character
   */
  public static String firstToUpper(Object input) {
    String s;
    if (input == null || (s = input.toString()).isEmpty()) {
      return EMPTY_STRING;
    }
    if (Character.isLowerCase(s.charAt(0))) {
      return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    return s;
  }

  /**
   * Ensures that the first character of the specified string is not an uppercase
   * character.
   *
   * @param input the string
   * @return the same string except that the first character is not an uppercase
   *     character
   */
  public static String firstToLower(Object input) {
    String s;
    if (input == null || (s = input.toString()).isEmpty()) {
      return EMPTY_STRING;
    }
    if (Character.isUpperCase(s.charAt(0))) {
      return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
    return s;
  }

  /**
   * Left-pads a string to the specified width using the space character (' ').
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is returned without padding.
   * @return the left-padded string
   */
  public static String lpad(Object input, int width) {
    return lpad(input, width, ' ', EMPTY_STRING);
  }

  /**
   * Left-pads a string to the specified width using the specified padding
   * character.
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is returned without padding.
   * @param padChar the character used to left-pad the string
   * @return the left-padded string
   */
  public static String lpad(Object input, int width, char padChar) {
    return lpad(input, width, padChar, EMPTY_STRING);
  }

  /**
   * Left-pads a string to the specified width using the specified padding character
   * and then appends the specified terminator.
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is returned without padding.
   * @param padChar the character used to left-pad the string
   * @param delimiter a delimiter to append to the padded string. Specify null or
   *     an empty string to indicate that no delimiter should be appended.
   * @return the left-padded string
   * @throws IllegalArgumentException If {@code terminator} is null
   */
  public static String lpad(Object input,
      int width,
      char padChar,
      String delimiter) {
    Check.that(width, "width").is(gte(), 0);
    String s = input == null ? EMPTY_STRING : input.toString();
    String d = ifNull(delimiter, EMPTY_STRING);
    if (s.length() >= width) {
      return s + d;
    }
    return new StringBuilder(width + d.length())
        .append(String.valueOf(padChar).repeat(width - s.length()))
        .append(s)
        .append(d)
        .toString();
  }

  /**
   * Centers (left- and right-pads) a string within the specified width using the
   * space character.
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is printed without padding.
   * @return the left- and right-padded string plus the terminator
   */
  public static String pad(Object input, int width) {
    return pad(input, width, ' ', null);
  }

  /**
   * Centers (left- and right-pads) a string within the specified width using the
   * specified padding character.
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is printed without padding.
   * @param padChar the character used to left- and right-pad the string.
   * @return the left- and right-padded string plus the terminator
   */
  public static String pad(Object input, int width, char padChar) {
    return pad(input, width, padChar, null);
  }

  /**
   * Centers (left- and right-pads) a string within the specified width using the
   * specified padding character and then appends the specified delimiter.
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is printed without padding.
   * @param padChar the character used to left- and right-pad the string.
   * @param delimiter a delimiter to append to the padded string. Specify null or
   *     an empty string to indicate that no delimiter should be appended.
   * @return the left- and right-padded string plus the terminator
   */
  public static String pad(Object input,
      int width,
      char padChar,
      String delimiter) {
    Check.that(width, "width").is(gte(), 0);
    String s = input == null ? EMPTY_STRING : input.toString();
    String d = ifNull(delimiter, EMPTY_STRING);
    if (s.length() >= width) {
      return s + d;
    }
    StringBuilder sb = new StringBuilder(width + d.length());
    int left = (width - s.length()) / 2;
    int right = width - left - s.length();
    sb.append(String.valueOf(padChar).repeat(left));
    sb.append(s);
    sb.append(String.valueOf(padChar).repeat(Math.max(0, right)));
    sb.append(d);
    return sb.toString();
  }

  /**
   * Right-pads a string to the specified width using the space character (' ').
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is returned without padding.
   * @return the right-padded string
   */
  public static String rpad(Object input, int width) {
    return rpad(input, width, ' ', EMPTY_STRING);
  }

  /**
   * Right-pads a string to the specified width using the specified padding
   * character.
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is returned without padding.
   * @param padChar the character used to left-pad the string.
   * @return the right-padded string
   */
  public static String rpad(Object input, int width, char padChar) {
    return rpad(input, width, padChar, EMPTY_STRING);
  }

  /**
   * Right-pads a string to the specified width using the specified padding character
   * and appends the specified suffix.
   *
   * @param input an object whose {@code toString()} method produces the string
   *     to be padded. Null is treated as the empty string.
   * @param width the total length of the padded string. If the string itself is
   *     wider than the specified width, the string is printed without padding.
   * @param padChar the character used to right-pad the string.
   * @param suffix A suffix to append to the padded string.
   * @return the right-padded string
   */
  public static String rpad(Object input, int width, char padChar, String suffix) {
    Check.that(width, "width").is(gte(), 0);
    Check.notNull(suffix, "delimiter");
    String str = input == null ? EMPTY_STRING : input.toString();
    if (str.length() >= width) {
      return str + suffix;
    }
    StringBuilder sb = new StringBuilder(width + suffix.length());
    String padding = String.valueOf(padChar);
    return append(sb, str, padding.repeat(width - str.length()), suffix).toString();
  }

  /**
   * Left-trims all characters contained in {@code chars} from the specified string.
   * The resulting string will not start with any of the characters contained in
   * {@code chars}.
   *
   * @param input the {@code String} to trim
   * @param chars the character to trim off the {@code String}
   * @return the left-trimmed {@code String} or the input string if it did not start
   *     with any of the specified characters
   */
  public static String ltrim(Object input, String chars) {
    Check.that(chars, "chars").isNot(empty());
    if (input == null) {
      return EMPTY_STRING;
    }
    String str = input.toString();
    int i = 0;
    LOOP:
    for (; i < str.length(); ++i) {
      for (int j = 0; j < chars.length(); ++j) {
        if (str.charAt(i) == chars.charAt(j)) {
          continue LOOP;
        }
      }
      break;
    }
    return i == 0 ? str : str.substring(i);
  }

  /**
   * Right-trims all characters contained in {@code chars} from the specified string.
   * The resulting string will not end with any of the characters contained in
   * {@code chars}.
   *
   * @param input the {@code String} to trim
   * @param chars the character to trim off the {@code String} (must not be
   *     {@code null} or empty)
   * @return the right-trimmed {@code String} or the input string if it did not end
   *     with any of the specified characters
   */
  public static String rtrim(Object input, String chars) {
    Check.that(chars, "chars").isNot(empty());
    if (input == null) {
      return EMPTY_STRING;
    }
    String str = input.toString();
    int i = str.length() - 1;
    LOOP:
    for (; i >= 0; --i) {
      for (int j = 0; j < chars.length(); ++j) {
        if (str.charAt(i) == chars.charAt(j)) {
          continue LOOP;
        }
      }
      break;
    }
    return i == str.length() - 1 ? str : str.substring(0, i + 1);
  }

  /**
   * Left and right-trims the specified string. The resulting string will neither
   * start nor end with any of the specified characters.
   *
   * @param input the {@code String} to trim
   * @param chars the character to trim off the {@code String} (must not be
   *     {@code null} or empty)
   * @return the trimmed {@code String}.
   */
  public static String trim(Object input, String chars) {
    return rtrim(ltrim(input, chars), chars);
  }

  /**
   * Substring method that facilitates substring retrieval relative to the end of a
   * string. If {@code from} is negative, it is taken relative to the end of the
   * string ({@code -1} being equivalent to {@code str.length()-1}).
   *
   * @param str the {@code String} to extract a substring from
   * @param from the start index within {@code string} (may be negative)
   * @return the substring
   */
  public static String substr(String str, int from) {
    Check.notNull(str, Param.STR);
    int sz = str.length();
    if (from < 0) {
      from = sz + from;
    }
    Check.that(from, Param.FROM_INDEX).is(gte(), 0).is(lte(), sz);
    return str.substring(from);
  }

  /**
   * Substring method that facilitates substring retrieval relative to the end of a
   * string as well as substring retrieval in the opposite direction. If {@code from}
   * is negative, it is taken relative to the end of the string ({@code -1} being
   * equivalent to {@code str.length()-1}). If {@code length} is negative, the
   * substring is taken in the opposite direction. The character at {@code from} will
   * then be the <i>last</i> character of the substring.
   *
   * @param str the {@code String} to extract a substring from. <i>Must not be
   *     null.</i>
   * @param from the start index within {@code string} (may be negative)
   * @param length the desired length of the substring
   * @return the substring
   * @see CollectionMethods#sublist(List, int, int)
   */
  public static String substr(String str, int from, int length) {
    Check.notNull(str, Param.STR);
    int sz = str.length();
    int start;
    if (from < 0) {
      start = from + sz;
      Check.that(start, Param.FROM_INDEX).is(gte(), 0);
    } else {
      start = from;
      Check.that(start, Param.FROM_INDEX).is(lte(), sz);
    }
    int end;
    if (length >= 0) {
      end = start + length;
    } else {
      end = start + 1;
      start = end + length;
      Check.that(start, Param.FROM_INDEX).is(gte(), 0);
    }
    Check.that(end, Param.TO_INDEX).is(lte(), sz);
    return str.substring(start, end);
  }

  /**
   * Returns the index of the nth occurrence of the specified substring within
   * {@code input}. To find the first occurrence of the specified substring, specify
   * 1 for {@code occurrence}; to find the second occurrence, specify 2, etc. If
   * {@code input} is {@code null}, or if there is no nth occurrence of the specified
   * substring, the return value will be -1. You can specify a negative occurrence to
   * search backwards from the end of the string. Specify -1 for the last occurrence
   * of the specified substring; -2 for the last-but-one occurrence, etc.
   *
   * @param input the string to search
   * @param substr the substring to search for (must not be null or empty)
   * @param occurrence the occurrence number of the substring (1 means: get index
   *     of 1st occurrence; -1 means: get index of last occurrence)
   * @return the index of the nth occurrence of the specified substring
   */
  public static int indexOf(Object input, String substr, int occurrence) {
    Check.that(substr, "substr").isNot(empty());
    Check.that(occurrence, "occurrence").is(ne(), 0);
    String str;
    if (input == null || (str = input.toString()).length() < substr.length()) {
      return -1;
    }
    if (substr.length() == 1) {
      return occurrence > 0
          ? charPosIndexOf(str, substr.charAt(0), occurrence)
          : charNegIndexOf(str, substr.charAt(0), occurrence);
    }
    return occurrence > 0
        ? strPosIndexOf(str, substr, occurrence)
        : strNegIndexOf(str, substr, occurrence);
  }

  private static int charPosIndexOf(String str, char c, int occurrence) {
    for (int i = 0; i < str.length(); ++i) {
      if (str.charAt(i) == c && --occurrence == 0) {
        return i;
      }
    }
    return -1;
  }

  private static int charNegIndexOf(String str, char c, int occurrence) {
    for (int i = str.length() - 1; i >= 0; --i) {
      if (str.charAt(i) == c && ++occurrence == 0) {
        return i;
      }
    }
    return -1;
  }

  private static int strPosIndexOf(String str, String substr, int occurrence) {
    for (int i = 0; i <= str.length() - substr.length(); ++i) {
      if (str.regionMatches(i, substr, 0, substr.length()) && --occurrence == 0) {
        return i;
      }
    }
    return -1;
  }

  private static int strNegIndexOf(String str, String substr, int occurrence) {
    for (int i = str.length() - substr.length(); i >= 0; --i) {
      if (str.regionMatches(i, substr, 0, substr.length()) && ++occurrence == 0) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns the substring up to, but not including the nth occurrence of the
   * specified substring, or the entire string if there is no nth occurrence of the
   * substring. Returns an empty string if {@code input} is {@code null}. Specify 1
   * to find the first occurrence of the substring; 2 to find the second occurrence,
   * etc.  Specify -1 to find the first occurrence; -2 to find the last-but-one
   * occurrence, etc.
   *
   * @param input the string to search
   * @param substr the substring to search for
   * @param occurrence the occurrence number of the substring. Specify 1 for
   *     first occurrence; 2 for second occurrence, etc. Specify -1 for last
   *     occurrence, -2 for last-but-one occurrence, etc.
   * @return a substring up to (not including) the nth occurrence of the specified
   *     substring
   */
  public static String substringBefore(Object input, String substr, int occurrence) {
    int idx = indexOf(input, substr, occurrence);
    if (idx == -1) {
      return input == null ? EMPTY_STRING : input.toString();
    }
    return input.toString().substring(0, idx);
  }

  /**
   * Returns the substring up to, and including the nth occurrence of the specified
   * substring, or the entire string if there is no nth occurrence of the substring.
   * Returns an empty string if {@code input} is {@code null}. Specify 1 to find the
   * first occurrence of the substring; 2 to find the second occurrence, etc. Specify
   * -1 to find the first occurrence; -2 to find the last-but-one occurrence, etc.
   *
   * @param input the string to search
   * @param substr the substring to search for
   * @param occurrence the occurrence number of the substring. Specify 1 for
   *     first occurrence; 2 for second occurrence, etc. Specify -1 for last
   *     occurrence, -2 for last-but-one occurrence, etc.
   * @return a substring up to, and including the nth occurrence of the specified
   *     substring
   */
  public static String substringOnTo(Object input, String substr, int occurrence) {
    int idx = indexOf(input, substr, occurrence);
    if (idx == -1) {
      return input == null ? EMPTY_STRING : input.toString();
    }
    if (input.getClass() == String.class) {
      String s = input.toString();
      return idx + substr.length() == s.length()
          ? new String(s)
          : s.substring(0, idx + substr.length());
    }
    return input.toString().substring(0, idx + substr.length());
  }

  /**
   * Returns the substring from (and including) the nth occurrence of the specified
   * substring, or the entire string if there is no nth occurrence of the substring.
   * Returns an empty string if {@code input} is {@code null}. Specify 1 to find the
   * first occurrence of the substring; 2 to find the second occurrence, etc. Specify
   * -1 to find the first occurrence; -2 to find the last-but-one occurrence, etc.
   *
   * @param input the string to search
   * @param substr the substring to search for
   * @param occurrence the occurrence number of the substring. Specify 1 for
   *     first occurrence; 2 for second occurrence, etc. Specify -1 for last
   *     occurrence, -2 for last-but-one occurrence, etc.
   * @return a substring from (inclusive) the nth occurrence of the specified
   *     substring
   */
  public static String substrFrom(Object input, String substr, int occurrence) {
    int idx = indexOf(input, substr, occurrence);
    if (idx == -1) {
      return input == null ? EMPTY_STRING : input.toString();
    }
    if (input.getClass() == String.class) {
      String s = input.toString();
      return idx == 0 ? new String(s) : s.substring(idx);
    }
    return input.toString().substring(idx);
  }

  /**
   * Returns the substring after the nth occurrence of the specified substring, or
   * the entire string if there is no nth occurrence of the substring. Returns an
   * empty string if {@code input} is {@code null}. Specify 1 to find the first
   * occurrence of the substring; 2 to find the second occurrence, etc. Specify -1 to
   * find the first occurrence; -2 to find the last-but-one occurrence, etc.
   *
   * @param input the string to search
   * @param substr the substring to search for
   * @param occurrence the occurrence number of the substring. Specify 1 for
   *     first occurrence; 2 for second occurrence, etc. Specify -1 for last
   *     occurrence, -2 for last-but-one occurrence, etc.
   * @return a substring after (not including) the nth occurrence of the specified
   *     substring
   */
  public static String substrAfter(Object input, String substr, int occurrence) {
    int idx = indexOf(input, substr, occurrence);
    if (idx == -1) {
      return input == null ? EMPTY_STRING : input.toString();
    }
    return input.toString().substring(idx + substr.length());
  }

  /**
   * Returns the line number and column number of the character at the specified
   * index, given the system-defined line separator.
   *
   * @param str the string to search
   * @param index the string index to determine the line and column number of
   * @return a two-element array containing the line number and column number of the
   *     character at the specified index
   */
  public static int[] getLineAndColumn(String str, int index) {
    return getLineAndColumn(str, index, System.lineSeparator());
  }

  /**
   * Returns the line number and column number of the character at the specified
   * index, given the specified line separator.
   *
   * @param str the string to search
   * @param index the string index to determine the line and column number of
   * @param lineSep the line separator
   * @return a two-element array containing the line number and column number of the
   *     character at the specified index
   */
  public static int[] getLineAndColumn(String str, int index, String lineSep) {
    Check.that(str, Param.STR).is(notNull())
        .and(index).is(inRangeOf(), ints(0, str.length()), indexOutOfBounds(index))
        .and(lineSep, "lineSep").isNot(empty());
    if (index == 0) {
      return new int[] {0, 0};
    }
    int line = 0, pos = 0, i = str.indexOf(lineSep);
    while (i != -1 && i < index) {
      ++line;
      pos = i + lineSep.length();
      i = str.indexOf(lineSep, i + lineSep.length());
    }
    return new int[] {line, index - pos};
  }

}
