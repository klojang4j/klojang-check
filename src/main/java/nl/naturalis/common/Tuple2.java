package nl.naturalis.common;

import nl.naturalis.check.Check;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Generic 2-tuple of arbitrarily typed, non-null objects.
 *
 * @param first The first component of the 2-tuple
 * @param second The second component of the 2-tuple
 * @param <T> The type of the first component
 * @param <U> The type of the second component
 * @see LaxTuple2
 */
public record Tuple2<T, U>(T first, U second) {

  /**
   * Returns a {@code Tuple2} consisting of the specified components.
   *
   * @param first The first component of the 2-tuple
   * @param second The second component of the 2-tuple
   * @param <ONE> The type of the first component
   * @param <TWO> The type of the second component
   * @return A {@code Tuple2} instance containing the specified values
   */
  public static <ONE, TWO> Tuple2<ONE, TWO> of(ONE first, TWO second) {
    return new Tuple2(first, second);
  }

  /**
   * Instantiates a new {@code Tuple2}.
   *
   * @param first The first component of the 2-tuple
   * @param second The second component of the 2-tuple
   */
  public Tuple2(T first, U second) {
    this.first = Check.notNull(first, "first component").ok();
    this.second = Check.notNull(second, "second component").ok();
  }

  /**
   * Converts this instance to an immutable {@code Map} entry.
   *
   * @return an immutable {@code Map} entry
   */
  public Map.Entry<T, U> toEntry() {
    return new AbstractMap.SimpleImmutableEntry<>(first, second);
  }

}
