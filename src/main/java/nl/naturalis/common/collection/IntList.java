package nl.naturalis.common.collection;

import nl.naturalis.common.Emptyable;
import nl.naturalis.check.Check;
import nl.naturalis.common.function.ThrowingIntConsumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static nl.naturalis.check.CommonChecks.notNull;

/**
 * The ubiquitous list-of-int class, while we await Valhalla. Note that
 * {@code IntList} is a sealed interface with just two implementations: one
 * ({@link IntArrayList}) allows mutations on the list and the other is immutable.
 * Instances of the immutable variant can only be obtained through the static factory
 * methods on this interface.
 */
public sealed interface IntList extends Emptyable permits IntArrayList,
    UnmodifiableIntList {

  /**
   * Returns an unmodifiable {@code IntList} containing the integers in the specified
   * collection.
   *
   * @param c the {@code Integer} collection to extract the integers from
   * @return an unmodifiable {@code IntList}
   */
  static IntList copyOf(Collection<Integer> c) {
    Check.notNull(c);
    if (c.isEmpty()) {
      return UnmodifiableIntList.EMPTY;
    }
    int[] buf = new int[c.size()];
    int idx = 0;
    for (Integer i : c) {
      Check.that(i).is(notNull(), "collection must not contain null values", '\0');
      buf[idx++] = i;
    }
    return new UnmodifiableIntList(buf);
  }

  /**
   * Returns an unmodifiable copy of the specified {@code IntList}.
   *
   * @param other the {@code IntList} to extract the integers from
   * @return an unmodifiable {@code IntList}
   */
  static IntList copyOf(IntList other) {
    Check.notNull(other);
    if (other == UnmodifiableIntList.EMPTY || other.isEmpty()) {
      return UnmodifiableIntList.EMPTY;
    } else if (other.getClass() == UnmodifiableIntList.class) {
      return other;
    }
    return new UnmodifiableIntList(other.toArray());
  }

  /**
   * Returns an unmodifiable, empty {@code IntList}.
   *
   * @return an unmodifiable, empty {@code IntList}
   */
  static IntList of() {
    return UnmodifiableIntList.EMPTY;
  }

  /**
   * Returns an unmodifiable {@code IntList} containing the provided element.
   *
   * @param e0 the one and only element in the list
   * @return an unmodifiable {@code IntList} containing the provided element
   */
  static IntList of(int e0) {
    int[] buf = new int[] {e0};
    return new UnmodifiableIntList(buf);
  }

  /**
   * Returns an unmodifiable {@code IntList} containing the provided elements.
   *
   * @param e0 the 1st element
   * @param e1 the 2nd element
   * @return an unmodifiable {@code IntList} containing the provided elements
   */
  static IntList of(int e0, int e1) {
    int[] buf = new int[] {e0, e1};
    return new UnmodifiableIntList(buf);
  }

  /**
   * Returns an unmodifiable {@code IntList} containing the provided elements.
   *
   * @param e0 the 1st element
   * @param e1 the 2nd element
   * @param e2 the 3rd element
   * @return an unmodifiable {@code IntList} containing the provided elements
   */
  static IntList of(int e0, int e1, int e2) {
    int[] buf = new int[] {e0, e1, e2};
    return new UnmodifiableIntList(buf);
  }

  /**
   * Returns an unmodifiable {@code IntList} containing the provided elements.
   *
   * @param e0 the 1st element
   * @param e1 the 2nd element
   * @param e2 the 3rd element
   * @param e3 the 4th element
   * @return an unmodifiable {@code IntList} containing the provided elements
   */
  static IntList of(int e0, int e1, int e2, int e3) {
    int[] buf = new int[] {e0, e1, e2, e3};
    return new UnmodifiableIntList(buf);
  }

  /**
   * Returns an unmodifiable {@code IntList} containing the provided elements.
   *
   * @param e0 the 1st element
   * @param e1 the 2nd element
   * @param e2 the 3rd element
   * @param e3 the 4th element
   * @param e4 the 5th element
   * @return an unmodifiable {@code IntList} containing the provided elements
   */
  static IntList of(int e0, int e1, int e2, int e3, int e4) {
    int[] buf = new int[5];
    buf[0] = e0;
    buf[1] = e1;
    buf[2] = e2;
    buf[3] = e3;
    buf[4] = e4;
    return new UnmodifiableIntList(buf);
  }

  /**
   * Returns an unmodifiable {@code IntList} containing the provided elements.
   *
   * @param elements the elements with which to initialize the {@code IntList}
   * @return an unmodifiable {@code IntList} containing the specified elements
   */
  static IntList of(int... elements) {
    return ofElements(elements);
  }

  /**
   * Returns an unmodifiable {@code IntList} containing the specified elements.
   *
   * @param elements the elements with which to initialize the {@code IntList}
   * @return an unmodifiable {@code IntList} containing the specified elements
   */
  static IntList ofElements(int[] elements) {
    Check.notNull(elements, "array");
    if (elements.length == 0) {
      return UnmodifiableIntList.EMPTY;
    }
    int[] buf = Arrays.copyOf(elements, elements.length);
    return new UnmodifiableIntList(buf);
  }

  /**
   * Returns the value at the specified index.
   *
   * @param index the list index
   * @return the value at the specified index
   */
  int get(int index);

  /**
   * Sets the value at the specified index.
   *
   * @param index the list index
   * @param value the value
   */
  void set(int index, int value);

  /**
   * Returns an {@link OptionalInt} containing the array index of the first
   * occurrence of the specified value, or an empty {@link OptionalInt} if the value
   * is not present.
   *
   * @param value the value to search for
   * @return an {@link OptionalInt} containing the array index of the first
   *     occurrence of the specified value, or an empty {@link OptionalInt} if the
   *     value is not present
   */
  OptionalInt indexOf(int value);

  /**
   * Returns an {@link OptionalInt} containing the array index of the last occurrence
   * of the specified value, or an empty {@link OptionalInt} if the value is not
   * present.
   *
   * @param value the value to search for
   * @return an {@link OptionalInt} containing the array index of the first
   *     occurrence of the specified value, or an empty {@link OptionalInt} if the
   *     value is not present
   */
  OptionalInt lastIndexOf(int value);

  /**
   * Appends the specified integer.
   *
   * @param value the integer to append
   */
  void add(int value);

  /**
   * Inserts the specified value at the specified index, right-shifting all elements
   * at, and following that the index.
   *
   * @param index the index at which to insert the integer
   * @param value the integer to insert
   */
  void add(int index, int value);

  /**
   * Appends the values in the specified {@code IntList}  to this list.
   *
   * @param other an {@code IntList} containing the integers to append
   */
  void addAll(IntList other);

  /**
   * Appends the specified values.
   *
   * @param values the integers to append
   */
  void addAll(int[] values);

  /**
   * Inserts the values in the specified {@code IntList} into this list.
   *
   * @param index the index at which to insert the integers
   * @param other the integers to insert
   */
  void addAll(int index, IntList other);

  /**
   * Inserts the specified {@code int} array into this list at the specified index,
   * right-shifting the elements at, and following the index.
   *
   * @param index the index at which to insert the integers
   * @param values the integers to insert
   */
  void addAll(int index, int[] values);

  /**
   * Removes the element at the specified index
   *
   * @param index the index of the element to remove
   */
  void removeByIndex(int index);

  /**
   * Removes the first occurrence of the specified value in this {@code IntList}.
   *
   * @param value the value to remove
   * @return {@code true} if the value was present; {@code false} otherwise
   */
  boolean removeByValue(int value);

  /**
   * Removes the values contained in the specified {@code IntList} from this
   * {@code IntList}
   *
   * @param list the {@code IntList} containing the values to remove
   * @return whether the list changed
   */
  boolean removeAll(IntList list);

  /**
   * Removes the specified values from this {@code IntList}.
   *
   * @param values the values to remove
   * @return whether the list changed
   */
  boolean removeAll(int... values);

  /**
   * Removes the values contained in the specified {@code Collection} from this
   * {@code IntList}.
   *
   * @param c the {@code Collection IntList} containing the values to remove
   * @return whether the list changed
   */
  boolean removeAll(Collection<?> c);

  /**
   * Removes all values that are not in the specified {@code IntList}.
   *
   * @param list An {@code IntList} containing the values to keep
   * @return whether the list changed
   */
  boolean retainAll(IntList list);

  /**
   * Removes all values that are not in the specified {@code IntList}.
   *
   * @param values an array containing the values to keep
   * @return whether the list changed
   */
  boolean retainAll(int... values);

  /**
   * Removes all values that are not in the specified {@code IntList}.
   *
   * @param c A {@code Collection} containing the values to keep
   * @return whether the list changed
   */
  boolean retainAll(Collection<?> c);

  /**
   * Returns the current size of the list.
   *
   * @return the current size of the list.
   */
  int size();

  /**
   * Returns whether the list is empty.
   *
   * @return whether the list is empty.
   */
  boolean isEmpty();

  /**
   * Clears the list. Note that this leaves the backing array untouched. It just
   * resets the internal cursor.
   */
  void clear();

  /**
   * Trims the list to the specified size. Note that this leaves the backing array
   * untouched. It just moves the internal cursor backwards.
   *
   * @param newSize the desired new size of the list (must be less than or equal
   *     to its current size)
   * @see #setCapacity(int)
   */
  void trim(int newSize);

  /**
   * Returns the current capacity of the list (the length of the backing array).
   *
   * @return the current capacity of the list
   */
  int capacity();

  /**
   * Resizes the backing array. the new capacity is allowed to be less than the
   * current capacity, and even less than the size of the list. So this method can
   * also be used as a truncate or a trim-to-size method.
   *
   * @param newCapacity the desired length of the backing array
   * @see #trim(int)
   */
  void setCapacity(int newCapacity);

  /**
   * Sorts the elements in this list in ascending order.
   */
  void sort();

  /**
   * Sorts the elements in this list in descending order.
   */
  void sortDescending();

  /**
   * Converts this {@code IntList} to a fixed-size, mutable {@code List<Integer>}.
   *
   * @return a fixed-size, mutable {@code List<Integer>}
   */
  List<Integer> toGenericList();

  /**
   * Converts the list to an {@code int[]} array.
   *
   * @return an {@code int[]} array containing the values in this list
   */
  int[] toArray();

  /**
   * Converts the list to an {@code int[]} array.
   *
   * @param from the index (inclusive) of the first element
   * @param to the index (exclusive) of the last element
   * @return an {@code int[]} array containing the values in this list
   */
  int[] toArray(int from, int to);

  /**
   * Returns an {@code IntStream} of the elements in this list.
   *
   * @return an {@code IntStream} of the elements in this list
   */
  IntStream stream();

  /**
   * Carries out the specified action for each of the elements in the list.
   *
   * @param action the action to carry out for each of the elements
   */
  void forEach(IntConsumer action);

  /**
   * Carries out the specified action for each of the elements in the list.
   *
   * @param action the action to carry out for each of the elements
   * @param <E> the type of the exception that the consumer is allowed to throw.
   * @throws E a (possibly checked) exception thrown from within the consumer
   */
  <E extends Throwable> void forEachThrowing(ThrowingIntConsumer<E> action) throws E;

}
