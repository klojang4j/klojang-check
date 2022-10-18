package org.klojang.check.relation;

/**
 * Symbolic constants for logical quantifiers. {@link ComposablePredicate} and
 * {@link ComposableIntPredicate} let you use logical quantifiers to define not just
 * a {@linkplain Relation relation} between one value and another value, but also a
 * relation between one value and a set of other values (a value domain).
 *
 * @see ComposablePredicate
 * @see ComposableIntPredicate
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
   * Symbolic constant for the negation of the existential quantifier (¬∃).
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
