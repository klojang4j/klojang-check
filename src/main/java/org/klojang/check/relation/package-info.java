/**
 * A collection of functional interfaces that together constitute one category of
 * checks that can be executed using Klojang Check. The other category consists of
 * the twin interfaces of {@link java.util.function.Predicate Predicate} and
 * {@link java.util.function.IntPredicate IntPredicate} from the JDK itself. This
 * package also contains extensions of these two interfaces:
 * {@link org.klojang.check.relation.ComposablePredicate} and
 * {@link org.klojang.check.relation.ComposableIntPredicate}. They function as a
 * bridge to the relational interfaces of this package. See
 * {@link org.klojang.check.relation.ComposablePredicate} for more information.
 *
 *
 * <p>The {@link org.klojang.check.relation.Relation} interface
 * and its sister interfaces in this package assess whether a certain type of
 * relation exists between one object and another. The object being assessed is
 * called the <b>subject</b> of the relationship, and the object that it is compared
 * against is called the <b>object</b> of the relationship. If the subject does
 * indeed have the specified relation to the object, the relation is said to
 * <b>exist</b>. For example, if x is a {@code Collection} and y is an element of
 * it, then the <b>contains</b> relation ({@code Collection::contains}) exists
 * between x and y.
 *
 */
package org.klojang.check.relation;