/**
 * Classes and interfaces related to functional programming. Two categories of
 * interfaces can be distinguished in this package:
 * <ol>
 * <li>The {@link nl.naturalis.common.function.Relation} interface and its sister interfaces</li>
 * <li>Variants of existing JDK interfaces that distinguish themselves from their JDK
 * counterparts by allowing a checked exception to be thrown from the functional method (for example
 * {@link java.util.function.Function Function} vs.
 * {@link nl.naturalis.common.function.ThrowingFunction}). These variants may not be
 * amenable to stream programming, let alone in concurrent fashion, but they are
 * nevertheless very useful and are used throughout in the other packages of
 * naturalis-common.
 * </ol>
 *
 * <h2>The Relation interface</h2>
 *
 * <p>The {@link nl.naturalis.common.function.Relation} interface
 * and its sister interfaces in this package assess the presence of a relationship
 * between two values. The values may or may not have the same type. The value
 * being assessed is called the <b>subject</b> of the relationship, and the value
 * that it forms the relationship with is called the <b>object</b>
 * of the relationship. If the subject does indeed have the sought-after relation to the
 * object, the relation is said to <b>exist</b>. For example, if x is a
 * {@code Collection} and y is an element of it, then the <b>contains</b> relation
 * ({@link java.util.Collection#contains(java.lang.Object) Collection::contains}) exists between x and y. The
 * {@link nl.naturalis.check.CommonChecks CommonChecks} class from
 * <b>{@linkplain nl.naturalis.check Naturalis Check}</b> defines (or aliases) many
 * small {@code Relation} implementations (like {@code Collection::contains}) that
 * can be used as checks on arguments
 *
 * <p>(NB following the JDK's nomenclature the {@code Relation} interface might also
 * have been termed a {@code BiPredicate}, because that is basically what it is. No
 * such interface exists, by the way, in the JDK.)
 */
package nl.naturalis.common.function;
