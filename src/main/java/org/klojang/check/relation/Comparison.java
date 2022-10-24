package org.klojang.check.relation;

/**
 * Defines a relationship between two objects of the same type.
 *
 * @param <T> the type of the objects being compared
 */
@FunctionalInterface
public interface Comparison<T> extends Relation<T, T> {
}
