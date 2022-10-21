package org.klojang.check.relation;

import org.klojang.check.CommonChecks;
import org.klojang.check.IntCheck;
import org.klojang.check.ObjectCheck;

/**
 * Defines some kind of relationship between an object and an integer. For example,
 * if x is an {@code int[]} array containing 6, and y equals 6, then the
 * <b>contain-elements</b> relation exists between x and y. See the
 * {@linkplain nl.naturalis.common.function package description} for more
 * information.
 *
 * <p>This interface is included for completeness and because it represents the
 * {@linkplain #converse() converse} of the {@link IntObjRelation}.
 * {@link ObjectCheck} and {@link IntCheck} do not currently support checks in the
 * form of an {@code ObjIntRelation} and the {@link CommonChecks} does not define
 * any.
 *
 * @param <S> The type of the subject of the relation
 * @author Ayco Holleman
 * @see Relation
 */
@FunctionalInterface
public interface ObjIntRelation<S> {

  /**
   * Returns the converse of this relation, swapping subject and object in the
   * relationship. Thus, the converse of an {@code ObjIntRelation} is an
   * {@link IntObjRelation}.
   *
   * @return an {@code IntObjRelation} that is the converse of this
   *     {@code ObjIntRelation}.
   */
  default IntObjRelation<S> converse() {
    return (x, y) -> exists(y, x);
  }

  /**
   * Returns the negation of the this {@code ObjIntRelation}.
   *
   * @return the negation of the this {@code ObjIntRelation}
   */
  default ObjIntRelation<S> negate() {
    return (x, y) -> !exists(x, y);
  }

  /**
   * Whether this {@code ObjIntRelation} is found to exist between the provided
   * {@code subject} and {@code object}.
   *
   * @param subject The subject of the relation (the entity from which the
   *     relationship extends)
   * @param object The object of the relation (the entity to which the
   *     relationship extends)
   * @return {@code true} if the relation exists, {@code false} otherwise.
   */
  boolean exists(S subject, int object);

}
