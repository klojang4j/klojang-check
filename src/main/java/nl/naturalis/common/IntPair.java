package nl.naturalis.common;

/**
 * A 2-tuple of {@code int} values.
 */
public record IntPair(int one, int two) {

  /**
   * Static factory method.
   *
   * @param one The first {@code int}
   * @param two The second {@code int}
   * @return A new {@code IntPair}
   */
  public static IntPair of(int one, int two) {
    return new IntPair(one, two);
  }

}
