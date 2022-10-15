package nl.naturalis.check.util;

/**
 * Symbolic constants for logical quantifiers. Used by
 * {@link nl.naturalis.check.types.ComposablePredicate} to modulate the relation
 * between a value and a value domain.
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
