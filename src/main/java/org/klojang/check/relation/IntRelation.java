package org.klojang.check.relation;

/**
 * Defines a relationship between two integers. For example, if x equals 5 and y
 * equals 3, then the <b>is-greater-than</b> relation exists between x and y. See the
 * {@linkplain org.klojang.check.relation package description} for more
 * information.
 *
 * @author Ayco Holleman
 * @see Relation
 */
@FunctionalInterface
public interface IntRelation {

  /**
   * Returns the converse of this relation, swapping subject and object in the
   * relationship. For example, the converse of <b>x &gt; y</b> is <b>y &gt; x</b>
   * (or <b>x &lt;= y</b>).
   *
   * @return the converse of this {@code IntRelation}
   */
  default IntRelation converse() {
    return (x, y) -> exists(y, x);
  }

  /**
   * Returns the negation of this {@code IntRelation}. For example, the negation of
   * <b>x &gt; y</b> is <b>x &lt;= y</b>.
   *
   * @return the negation of this {@code IntRelation}
   */
  default IntRelation negate() {
    return (x, y) -> !exists(x, y);
  }

  /**
   * Determines whether {@code subject} relates to {@code object} in the manner
   * defined by this {@code IntRelation}.
   *
   * @param subject The value to test
   * @param object The value to test it against
   * @return {@code true} if the relation exists, {@code false} otherwise.
   */
  boolean exists(int subject, int object);

}
