package nl.naturalis.common.collection;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * A one-way-only iterator that, in practice, still provides the same functionality
 * as {@linkplain ListIterator}. A {@code WiredIterator} lets you flip the direction
 * of the iteration through the {@link #turn()} method. Where applicable, <b>all
 * operations are relative to the direction of the traversal.</b> This includes
 * operations like {@link #next() next()},
 * {@link #insertBefore(Object) insertBefore()} and
 * {@link #insertAfter(Object) insertAfter()}. So, for example, with the direction
 * reversed, a call to {@code next()} takes you closer to the beginning of the list
 * and further away from the end. A {@code WiredIterator} does not keep track of the
 * index of the current element, as does a {@code ListIterator}. This makes it less
 * vulnerable to concurrent modifications, as it only cares about the element
 * directly ahead of it.
 *
 * @param <E> The type of the elements being iterated over
 * @see WiredList#wiredIterator(boolean)
 */
public sealed interface WiredIterator<E> extends Iterator<E> permits
    WiredList.ForwardWiredIterator, WiredList.ReverseWiredIterator {

  /**
   * Sets the value of the current element. An {@link IllegalStateException} is
   * thrown if {@link #next()} has not been called yet.
   *
   * @param newVal The new value for the element.
   */
  void set(E newVal);

  /**
   * Returns the value of the current element.
   *
   * @return the value of the current element
   */
  E value();

  /**
   * Returns the value that would be returned by a call to {@link #next()} without
   * actually moving towards the next element. A
   * {@link java.util.NoSuchElementException} is thrown if the iterator has arrived
   * at the last element.
   *
   * @return The value that would be returned by a call to{@code next()}.
   */
  E peek();

  /**
   * Inserts a new element just before the current element. Note that the list must
   * contain at least one element to begin with.
   *
   * @param value the value to insert
   */
  void insertBefore(E value);

  /**
   * Inserts a new element just after the current element. A subsequent call to
   * {@link #next()} would make the new element the current element. Note that the
   * list must contain at least one element to begin with.
   *
   * @param value the value to insert
   */
  void insertAfter(E value);

  /**
   * Removes the current element from the underlying list. Contrary to the
   * specification for {@link Iterator}, this method multiple times in a row. Before
   * removing the element, the {@code WiredIterator} will move back to the preceding
   * element. Thus, after the removal, the preceding element is the current element
   * again, and a call to {@code next()} will make the element after the removed
   * element the current element.
   */
  @Override
  void remove();

  /**
   * Returns the index of the current element. You must have called {@code next()} at
   * least once before you can call this method. The index is calculated on demand by
   * starting from the first element of the list and advancing until the current
   * element is encountered. Thus, for large lists this method is very inefficient
   * compared to the equivalent methods in {@link ListIterator}.
   *
   * @return the index of the current element
   */
  int index();

  /**
   * Flips the direction of the iteration. The returned {@code Iterator} is
   * initialized to be at the same element as this {@code Iterator}. You must have
   * called {@link #next()} at least once before you can call {@code turn()}. An
   * {@link IllegalStateException} is thrown if {@link #next()} has not been called
   * yet.
   *
   * @return A {@code WiredIterator} that the traverses the list in the opposite
   *     direction.
   */
  WiredIterator<E> turn();

}
