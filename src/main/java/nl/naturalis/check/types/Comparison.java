package nl.naturalis.check.types;

/**
 * Defines a relationship between two objects of the same type.
 *
 * @param <T> the type of the objects being compared
 */
public interface Comparison<T> extends Relation<T, T> {
}
