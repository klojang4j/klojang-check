package nl.naturalis.check;

/**
 * Symbolic constants for logical quantifiers.
 */
public enum Quantifier {

  /**
   * Symbolic constant for the universal quantifier (∀).
   */
  ALL,
  /**
   * Symbolic constant for the existential quantifier (∃).
   */
  ANY,
  /**
   * Symbolic constant for the negation existential quantifier (¬∃).
   */
  NONE;

  /**
   * Returns {@link #ALL}.
   *
   * @return {@code Quantifier.ALL}
   */
  public static Quantifier allOf() {
    return ALL;
  }

  /**
   * Returns {@link #ANY}.
   *
   * @return {@code Quantifier.ANY}
   */
  public static Quantifier anyOf() {
    return ANY;
  }

  /**
   * Returns {@link #NONE}.
   *
   * @return {@code Quantifier.NONE}
   */
  public static Quantifier noneOf() {
    return NONE;
  }
}
