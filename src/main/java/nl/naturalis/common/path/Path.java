package nl.naturalis.common.path;

import nl.naturalis.common.ArrayMethods;
import nl.naturalis.common.Emptyable;
import nl.naturalis.common.NumberMethods;
import nl.naturalis.common.StringMethods;
import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;
import static java.util.function.Predicate.not;
import static nl.naturalis.common.ArrayMethods.EMPTY_STRING_ARRAY;
import static nl.naturalis.common.ArrayMethods.implode;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonExceptions.INDEX;
import static nl.naturalis.check.CommonExceptions.STATE;

/**
 * Specifies a path to a value within an object. For example:
 * {@code employee.address.street}. Path segments are separated by the dot character
 * ('.'). Array indices are specified as separate path segments. For example:
 * {@code employees.3.address.street}. Non-numeric segments can be either bean
 * properties or map keys. Therefore the {@code Path} class does not impose any
 * constraints on what constitutes a valid path segment. A map key, after all, can be
 * anything - including {@code null} and the empty string. Of course, if the path
 * segment denotes a JavaBean property, it should be a valid Java identifier.
 *
 * <h2>Escaping</h2>
 * <p>These are the escaping rules when specifying path strings:
 * <ul>
 *   <li>If a path segment represents a map key that happens to contain the
 *      segment separator ('.'), it must be escaped using the circumflex
 *      character ('^'). So a map key with the value {@code "my.awkward.map.key"}
 *      should be escaped like this: {@code "my^.awkward^.map^.key"}.
 *  <li>The escape character ('^') itself must not be escaped. Thus, if the
 *      escape character is followed by anything but a dot or the zero character
 *      (see next rule), it is just that character.
 *  <li>If a segment needs to denote a map key with value {@code null}, use this
 *      escape sequence: {@code "^0"}. So the path {@code "lookups.^0.name"}
 *      references the {@code name} field of an object stored under key
 *      {@code null} in the {@code lookups} map.
 *  <li>If a segment needs to denote a map key whose value is the empty string,
 *      simply make it a zero-length segment: {@code "lookups..name"}. This also
 *      implies that a path string that ends with a dot in fact ends with an
 *      empty (zero-length) segment.
 * </ul>
 *
 * <p>If a path segment represents a map key that happens to contain the segment
 * separator ('.'), it must be escaped using the circumflex character ('^'). So a map
 * key with the unfortunate value of {@code "my.awkward.map.key"} should be escaped
 * like this: {@code "my^.awkward^.map^.key"}. The escape character itself must not
 * be escaped. If a segment needs to denote a map key with value {@code null}, use
 * this escape sequence: {@code "^0"}. So the path {@code "lookups.^0.name"}
 * references the {@code name} field of an object stored under key {@code null} in
 * the {@code lookups} map. In case you want a segment to denote a map key whose
 * value is the empty string, simply make it a zero-length segment:
 * {@code "lookups..name"}.
 * <p>
 * You can let the {@link #escape(String) escape} method do the escaping for you.
 *
 * <p>Do not escape path segments when passing them individually (as a {@code
 * String} array) to the constructor. Only escape them when passing a complete path
 * string.
 *
 * @author Ayco Holleman
 */
public final class Path implements Comparable<Path>, Iterable<String>, Emptyable {

  private static final Path EMPTY_PATH = new Path();

  // segment separator
  private static final char SEP = '.';

  // escape character
  private static final char ESC = '^';

  // escape sequence to use for null keys
  private static final String NULL_SEGMENT = "^0";

  /**
   * Returns a new {@code Path} instance for the specified path string.
   *
   * @param path the path string from which to create a {@code Path}
   * @return a new {@code Path} instance for the specified path string
   */
  public static Path from(String path) {
    Check.notNull(path, Param.PATH);
    if (path.isEmpty()) {
      return EMPTY_PATH;
    }
    return new Path(path);
  }

  /**
   * Returns an empty {@code Path} instance, consisting of zero path segments.
   *
   * @return an empty {@code Path} instance, consisting of zero path segments
   */
  public static Path empty() {
    return EMPTY_PATH;
  }

  /**
   * Returns a {@code Path} consisting of a single segment. <i>Do not escape the
   * segment.</i>
   *
   * @param segment the one and only segment of the {@code Path}
   * @return a {@code Path} consisting of a single segment
   */
  public static Path of(String segment) {
    return new Path(new String[] {segment});
  }

  /**
   * Returns a {@code Path} consisting of the specified segments. <i>Do not escape
   * the segments.</i>
   *
   * @param segment0 the 1st segment
   * @param segment1 the 2nd segment
   * @return a {@code Path} consisting of the specified segments
   */
  public static Path of(String segment0, String segment1) {
    return new Path(new String[] {segment0, segment1});
  }

  /**
   * Returns a {@code Path} consisting of the specified segments. <i>Do not escape
   * the segments.</i>
   *
   * @param segment0 the 1st segment
   * @param segment1 the 2nd segment
   * @param segment2 the 3rd segment
   * @return a {@code Path} consisting of the specified segments
   */
  public static Path of(String segment0, String segment1, String segment2) {
    return new Path(new String[] {segment0, segment1, segment2});
  }

  /**
   * Returns a {@code Path} consisting of the specified segments. <i>Do not escape
   * the segments.</i>
   *
   * @param segment0 the 1st segment
   * @param segment1 the 2nd segment
   * @param segment2 the 3rd segment
   * @param segment3 the 4th segment
   * @return a {@code Path} consisting of the specified segments
   */
  public static Path of(String segment0,
      String segment1,
      String segment2,
      String segment3) {
    return new Path(new String[] {segment0, segment1, segment2, segment3});
  }

  /**
   * Returns a {@code Path} consisting of the specified segments. <i>Do not escape
   * the segments.</i>
   *
   * @param segment0 the 1st segment
   * @param segment1 the 2nd segment
   * @param segment2 the 3rd segment
   * @param segment3 the 4th segment
   * @param segment4 the 5th segment
   * @return a {@code Path} consisting of the specified segments
   */
  public static Path of(String segment0,
      String segment1,
      String segment2,
      String segment3,
      String segment4) {
    return new Path(new String[] {segment0, segment1, segment2, segment3, segment4});
  }

  /**
   * Returns a {@code Path} consisting of the specified segments. <i>Do not escape
   * the segments.</i>
   *
   * @param segments the path segments
   * @return a {@code Path} consisting of the specified segments
   */
  public static Path of(String... segments) {
    return ofSegments(segments);
  }

  /**
   * Returns a {@code Path} consisting of the specified segments. <i>Do not escape
   * the segments.</i>
   *
   * @param segments the path segments
   * @return a {@code Path} consisting of the specified segments
   */
  public static Path ofSegments(String[] segments) {
    Check.notNull(segments);
    return segments.length == 0 ? EMPTY_PATH : new Path(segments);
  }

  /**
   * Returns a copy of the specified path.
   *
   * @param other the {@code Path} to copy.
   * @return a copy of the specified path
   */
  public static Path copyOf(Path other) {
    return other == EMPTY_PATH ? other : Check.notNull(other).ok(Path::new);
  }

  /**
   * Escapes the provided path segment. Do not escape path segments when passing them
   * individually to one of the static factory methods. Only use this method to
   * construct complete path strings from individual path segments. Generally you
   * don't need this method when specifying path strings, unless one or more segments
   * contain a dot ('.') or the escape character ('^') itself.
   *
   * @param segment The path segment to escape
   * @return the escaped version of the segment
   */
  public static String escape(String segment) {
    if (segment == null) {
      return NULL_SEGMENT;
    }
    int x = segment.indexOf(SEP);
    if (x == -1) {
      return segment;
    }
    StringBuilder sb = new StringBuilder(segment.length() + 5);
    sb.append(segment, 0, x);
    for (int i = x; i < segment.length(); i++) {
      if (segment.charAt(i) == SEP) {
        sb.append(ESC).append(SEP);
      } else {
        sb.append(segment.charAt(i));
      }
    }
    return sb.toString();
  }

  private final String[] elems;
  private String str; // Caches toString()
  private int hash; // Caches hashCode()

  // Reserved for EMPTY_PATH
  private Path() {
    elems = EMPTY_STRING_ARRAY;
  }

  private Path(String path) {
    elems = parse(str = path);
  }

  private Path(String[] segments) {
    elems = new String[segments.length];
    arraycopy(segments, 0, elems, 0, segments.length);
  }

  private Path(Path other) {
    // Since we are immutable we can happily share state
    this.elems = other.elems;
    this.str = other.str;
    this.hash = other.hash;
  }

  /**
   * Returns the path segment at the specified index. Specify a negative index to
   * retrieve a segment relative to end of the {@code Path} (-1 would return the last
   * path segment).
   *
   * @param index The array index of the path segment
   * @return the path segment at the specified index.
   */
  public String segment(int index) {
    if (index < 0) {
      return Check.that(elems.length + index)
          .is(indexOf(), elems)
          .ok(x -> elems[x]);
    }
    return Check.that(index).is(indexOf(), elems).ok(x -> elems[x]);
  }

  /**
   * Returns a new {@code Path} starting with the segment at the specified array
   * index. Specify a negative index to count back from the last segment of the
   * {@code Path} (-1 returns the last path segment).
   *
   * @param offset The index of the first segment of the new {@code Path}
   * @return a new {@code Path} starting with the segment at the specified array
   *     index
   */
  public Path subPath(int offset) {
    int from = offset < 0
        ? elems.length + offset
        : offset;
    Check.that(from).is(lt(), elems.length);
    return new Path(copyOfRange(elems, from, elems.length));
  }

  /**
   * Returns a new {@code Path} consisting of {@code length} segments starting with
   * segment {@code offset}. The {@code offset} argument may be negative to specify a
   * segment relative to the end of the {@code Path}. Thus, -1 specifies the last
   * segment of the {@code Path}.
   *
   * @param offset The index of the first segment to extract
   * @param length The number of segments to extract
   * @return a new {@code Path} consisting of {@code len} segments starting with
   *     segment {@code from}.
   */
  public Path subPath(int offset, int length) {
    if (offset < 0) {
      offset = elems.length + offset;
    }
    Check.offsetLength(elems.length, offset, length);
    return new Path(copyOfRange(elems, offset, offset + length));
  }

  /**
   * Return the parent of this {@code Path}. If the path is empty, this method
   * returns {@code null}. If it consists of a single segment, and empty {@code Path}
   * is returned.
   *
   * @return the parent of this {@code Path}
   */
  public Path parent() {
    if (elems.length == 0) {
      return null;
    } else if (elems.length == 1) {
      return EMPTY_PATH;
    }
    String[] segments = copyOfRange(elems, 0, elems.length - 1);
    return new Path(segments);
  }

  /**
   * Returns a new {@code Path} containing only the segments of this {@code Path}
   * that are not array indices.
   *
   * @return a new {@code Path} without any array indices
   */
  public Path getCanonicalPath() {
    String[] canonical = stream()
        .filter(not(NumberMethods::isInt))
        .toArray(String[]::new);
    return canonical.length == 0 ? EMPTY_PATH : new Path(canonical);
  }

  /**
   * Returns a new {@code Path} representing the concatenation of this {@code Path}
   * and the specified {@code Path}.
   *
   * @param path The path to append to this {@code Path}
   * @return a new {@code Path} representing the concatenation of this {@code Path}
   *     and the specified {@code Path}
   */
  public Path append(String path) {
    Check.notNull(path);
    return append(new Path(parse(path)));
  }

  /**
   * Returns a new {@code Path} consisting of the segments of this {@code Path} plus
   * the segments of the specified {@code Path}.
   *
   * @param other the {@code Path} to append to this {@code Path}.
   * @return a new {@code Path} consisting of the segments of this {@code Path} plus
   *     the segments of the specified {@code Path}
   */
  public Path append(Path other) {
    Check.notNull(other);
    return new Path(ArrayMethods.concat(elems, other.elems));
  }

  /**
   * Returns a new {@code Path} with the path segment at the specified array index
   * set to the new value.
   *
   * @param index The array index of the segment to replace
   * @param newValue The new segment
   * @return a new {@code Path} with the path segment at the specified array index
   *     set to the new value
   */
  public Path replace(int index, String newValue) {
    Check.on(INDEX, index, Param.INDEX).is(indexOf(), elems);
    String[] copy = Arrays.copyOf(elems, elems.length);
    copy[index] = newValue;
    return new Path(copy);
  }

  /**
   * Returns a {@code Path} with all segments of this {@code Path} except the first
   * segment.
   *
   * @return a {@code Path} with all segments of this {@code Path} except the first
   *     segment
   */
  public Path shift() {
    String[] elems;
    Check.on(STATE, (elems = this.elems).length).is(ne(), 0, "empty path");
    if (elems.length == 1) {
      return EMPTY_PATH;
    }
    String[] shifted = new String[elems.length - 1];
    arraycopy(elems, 1, shifted, 0, elems.length - 1);
    return new Path(shifted);
  }

  /**
   * Returns a {@code Path} in which the order of the segments is reversed.
   *
   * @return a {@code Path} in which the order of the segments is reversed
   */
  public Path reverse() {
    String[] elems;
    if ((elems = this.elems).length > 1) {
      String[] segments = new String[elems.length];
      for (int i = 0; i < elems.length; ++i) {
        segments[i] = elems[elems.length - 1 - i];
      }
      return new Path(segments);
    }
    return this;
  }

  /**
   * Returns an {@code Iterator} over the path segments.
   *
   * @return an {@code Iterator} over the path segments
   */
  @Override
  public Iterator<String> iterator() {
    return new Iterator<>() {
      private int i;

      public boolean hasNext() {
        return i < elems.length;
      }

      public String next() {
        if (i < elems.length) {
          return elems[i++];
        }
        throw new IndexOutOfBoundsException(i);
      }
    };
  }

  /**
   * Returns a {@code Stream} of path segments.
   *
   * @return a {@code Stream} of path segments
   */
  public Stream<String> stream() {
    return Arrays.stream(elems);
  }

  /**
   * Returns the number of segments in this {@code Path}.
   *
   * @return the number of segments in this {@code Path}
   */
  public int size() {
    return elems.length;
  }

  /**
   * Returns {@code true} if this is an empty {@code Path}, consisting of zero
   * segments.
   *
   * @return {@code true} if this is an empty {@code Path}, consisting of zero
   *     segments
   */
  public boolean isEmpty() {
    return elems.length == 0;
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj
        || (obj instanceof Path p && Arrays.equals(elems, p.elems));
  }

  @Override
  public int hashCode() {
    if (hash == 0) {
      hash = Arrays.deepHashCode(elems);
    }
    return hash;
  }

  @Override
  public int compareTo(Path other) {
    Check.notNull(other);
    return Arrays.compare(elems, other.elems);
  }

  /**
   * Returns this {@code Path} as a string, properly escaped.
   *
   * @return this {@code Path} as a string, properly escaped
   */
  @Override
  public String toString() {
    if (str == null) {
      str = implode(elems, Path::escape, ".", 0, elems.length);
    }
    return str;
  }

  private static String[] parse(String path) {
    ArrayList<String> elems = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.length(); i++) {
      switch (path.charAt(i)) {
        case SEP:
          String s = sb.toString();
          elems.add(s.equals(NULL_SEGMENT) ? null : s);
          sb.setLength(0);
          break;
        case ESC:
          if (i < path.length() - 1 && path.charAt(i + 1) == SEP) {
            sb.append(SEP);
            ++i;
          } else {
            sb.append(ESC);
          }
          break;
        default:
          sb.append(path.charAt(i));
      }
    }
    if (sb.length() > 0) {
      String s = sb.toString();
      elems.add(s.equals(NULL_SEGMENT) ? null : s);
    } else if (path.endsWith(".")) {
      elems.add(StringMethods.EMPTY_STRING);
    }
    return elems.toArray(String[]::new);
  }

}
