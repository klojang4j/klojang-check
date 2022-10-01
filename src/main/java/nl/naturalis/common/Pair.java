package nl.naturalis.common;

import nl.naturalis.check.Check;

/**
 * A 2-tuple of non-null objects of the same type.
 *
 * @param <T> The component type of the {@code Pair}
 * @author Ayco Holleman
 */
public record Pair<T>(T first, T second) {

  /**
   * Returns a new {@code Pair} consisting of the specified components.
   *
   * @param <U> The type of the component
   * @param one The first component of the {@code Pair}
   * @param two The second component of the {@code Pair}
   * @return A new {@code Pair} consisting of the specified components
   */
  public static <U> Pair<U> of(U one, U two) {
    return new Pair<>(one, two);
  }

  /**
   * Instantiate a new {@code Pair}.
   *
   * @param first The first component of the 2-tuple
   * @param second The second component of the 2-tuple
   */
  public Pair(T first, T second) {
    this.first = Check.notNull(first, "first component").ok();
    this.second = Check.notNull(second, "second component").ok();
  }

}
