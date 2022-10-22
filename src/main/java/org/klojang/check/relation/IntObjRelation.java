package org.klojang.check.relation;

/**
 * Defines a relationship between an integer and an object. For example, if x equals
 * 6, and y is an {@code int[]} array containing 6, then the <b>is-element-of</b>
 * relation exists between x and y. See the
 * {@linkplain org.klojang.check.relation package description} for more
 * information.
 *
 * @param <O> the type of the object of the relation
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface IntObjRelation<O> {

  /**
   * Returns the converse of this relation, swapping subject and object in the
   * relationship. Thus, the converse of an {@code IntObjRelation} is an
   * {@link ObjIntRelation}.
   *
   * @return an {@code ObjIntRelation} that is the converse of this
   *     {@code IntObjRelation}.
   */
  default ObjIntRelation<O> converse() {
    return (x, y) -> exists(y, x);
  }

  /**
   * Returns the negation of this {@code IntObjRelation}.
   *
   * @return the negation of this {@code IntObjRelation}
   */
  default IntObjRelation<O> negate() {
    return (x, y) -> !exists(x, y);
  }

  /**
   * Returns whether the relationship between {@code subject} and {@code object}
   * exists.
   *
   * @param subject The value to test
   * @param object The value to test it against
   * @return {@code true} if the relation exists, {@code false} otherwise
   */
  boolean exists(int subject, O object);

}
