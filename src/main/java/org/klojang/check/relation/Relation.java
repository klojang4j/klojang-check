package org.klojang.check.relation;

/**
 * Defines a relationship between two objects. For example, if x is a {@code Map} and
 * y is a {@code String} appearing as a key in that {@code Map}, then the
 * <b>contains-key</b> relation exists between x and y. See the
 * {@linkplain org.klojang.check.relation package description} for more information.
 *
 * @param <S> the type of the subject of the relation
 * @param <O> the type of the object of the relation
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface Relation<S, O> {

  /**
   * Returns the converse of this relation, swapping subject and object in the
   * relationship. For example, the converse of <b>x contains y</b> is <b>y contains
   * x</b> (or <b>x is-element-of y</b>).
   *
   * @return the converse of this {@code Relation}
   */
  default Relation<O, S> converse() {
    return (x, y) -> exists(y, x);
  }

  /**
   * Returns the negation of this {@code Relation}.
   *
   * @return the negation of this {@code Relation}
   */
  default Relation<S, O> negate() {
    return (x, y) -> !exists(x, y);
  }

  /**
   * Determines whether the two arguments have the relation that the implementation
   * attempts to establish.
   *
   * @param subject The value to test
   * @param object The value to test it against
   * @return {@code true} if the relation exists, {@code false} otherwise.
   */
  boolean exists(S subject, O object);

}
