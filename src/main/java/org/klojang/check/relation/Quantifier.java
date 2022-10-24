package org.klojang.check.relation;

/**
 * Symbolic constants for logical quantifiers. {@link ComposablePredicate} and
 * {@link ComposableIntPredicate} let you use logical quantifiers to define not just
 * a {@linkplain Relation relation} between one value and another value, but also
 * between one value and an array of other values:
 *
 * <blockquote><pre>{@code
 * Check.that(list).is(notNull().and(contains(), noneOf(), "to", "be", "or", "not"));
 * }</pre></blockquote>
 *
 * <p>(Strictly speaking the value domain of a logical quantifier would better be
 * modeled as a {@link java.util.Set Set}. For convenience sake, however, it is
 * modeled as a varargs array within Klojang Check.)
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
