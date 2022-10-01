package nl.naturalis.common.collection;

import nl.naturalis.common.ArrayMethods;
import nl.naturalis.check.Check;
import nl.naturalis.check.CommonChecks;
import nl.naturalis.common.function.ThrowingIntConsumer;
import nl.naturalis.common.util.ResizeMethod;

import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static java.lang.System.arraycopy;
import static nl.naturalis.common.ArrayMethods.*;
import static nl.naturalis.check.Check.fail;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonExceptions.indexOutOfBounds;
import static nl.naturalis.common.util.ResizeMethod.*;

/**
 * A mutable list of {@code int} values.
 *
 * @author Ayco Holleman
 */
public final class IntArrayList implements IntList {

  private final ResizeMethod resizeMethod;
  private final float resizeAmount;

  int[] buf;
  int size;

  /**
   * Creates an {@code IntList} with an initial capacity of 10.
   */
  public IntArrayList() {
    this(10);
  }

  /**
   * Creates an {@code IntList} with the specified initial capacity. Each time the
   * backing array reaches full capacity, it is resized to twice its length.
   * (However, see {@link ResizeMethod}.)
   *
   * @param initialCapacity The initial capacity of the list
   */
  public IntArrayList(int initialCapacity) {
    this(initialCapacity, MULTIPLY, 2);
  }

  /**
   * Creates an {@code IntList} with the specified initial capacity. Each time the
   * backing array reaches full capacity, it is enlarged by the specified amount.
   *
   * @param initialCapacity The initial capacity of the list
   * @param resizeAmount The (fixed) amount by which to enlarge it when it fills
   *     up
   */
  public IntArrayList(int initialCapacity, int resizeAmount) {
    this(initialCapacity, ADD, resizeAmount);
  }

  /**
   * Creates an {@code IntList} with the specified initial capacity. Each time the
   * backing array reaches full capacity, it is resized by applying the specified
   * {@link ResizeMethod} to the specified resize amount.
   *
   * @param initialCapacity The initial capacity of the list
   * @param resizeMethod The method to use for resizing the backing array
   * @param resizeAmount The resize amount
   */
  public IntArrayList(int initialCapacity,
      ResizeMethod resizeMethod,
      float resizeAmount) {
    Check.that(initialCapacity, "initialCapacity").is(gte(), 0);
    Check.notNull(resizeMethod, "resizeMethod");
    this.buf = new int[initialCapacity];
    this.resizeMethod = resizeMethod;
    this.resizeAmount = resizeAmount;
  }

  /**
   * Copy constructor. Creates a new {@code IntList} containing the same values as
   * the specified {@code IntList}.
   *
   * @param other The {@code IntList} to copy
   */
  public IntArrayList(IntList other) {
    Check.notNull(other, "IntList");
    if (other instanceof IntArrayList ial) {
      this.size = ial.size;
      this.resizeMethod = ial.resizeMethod;
      this.resizeAmount = ial.resizeAmount;
      this.buf = new int[Math.min(Integer.MAX_VALUE, size + 10)];
      arraycopy(ial.buf, 0, this.buf, 0, size);
    } else { // UnmodifiableIntList
      this.buf = other.toArray();
      this.size = other.size();
      this.resizeMethod = MULTIPLY;
      this.resizeAmount = 2F;
    }
  }

  @Override
  public int get(int index) {
    checkIndex(index);
    return buf[index];
  }

  @Override
  public void set(int index, int value) {
    checkIndex(index);
    buf[index] = value;
  }

  @Override
  public OptionalInt indexOf(int value) {
    for (int x = 0; x < size; ++x) {
      if (buf[x] == value) {
        return OptionalInt.of(x);
      }
    }
    return OptionalInt.empty();
  }

  @Override
  public OptionalInt lastIndexOf(int value) {
    for (int x = size - 1; x >= 0; --x) {
      if (buf[x] == value) {
        return OptionalInt.of(x);
      }
    }
    return OptionalInt.empty();
  }

  @Override
  public void add(int value) {
    add(size, value);
  }

  @Override
  public void add(int index, int value) {
    checkIndexInclusive(index);
    if (size == buf.length) {
      increaseCapacity(1);
    }
    if (index != size) {
      arraycopy(buf, index, buf, index + 1, size - index);
    }
    buf[index] = value;
    ++size;
  }

  @Override
  public void addAll(IntList other) {
    addAll(size, other);
  }

  @Override
  public void addAll(int[] values) {
    addAll(size, values);
  }

  @Override
  public void addAll(int index, IntList other) {
    checkIndexInclusive(index);
    Check.notNull(other);
    int minIncrease = getMinIncrease(buf.length, size, other.size());
    if (minIncrease > 0) {
      increaseCapacity(minIncrease);
    }
    if (index != size) {
      arraycopy(buf, index, buf, index + other.size(), size - index);
    }
    arraycopy(getBuffer(other), 0, buf, index, other.size());
    size += other.size();
  }

  @Override
  public void addAll(int index, int[] values) {
    checkIndexInclusive(index);
    Check.notNull(values);
    int minIncrease = getMinIncrease(buf.length, size, values.length);
    if (minIncrease > 0) {
      increaseCapacity(minIncrease);
    }
    if (index != size) {
      arraycopy(buf, index, buf, index + values.length, size - index);
    }
    arraycopy(values, 0, buf, index, values.length);
    size += values.length;
  }

  @Override
  public void removeByIndex(int index) {
    checkIndex(index);
    if (index != size - 1) {
      System.arraycopy(buf, index + 1, buf, index, size - 1 - index);
    }
    --size;
  }

  @Override
  public boolean removeByValue(int value) {
    OptionalInt index = indexOf(value);
    if (index.isPresent()) {
      removeByIndex(index.getAsInt());
      return true;
    }
    return false;
  }

  @Override
  public boolean removeAll(IntList list) {
    Check.notNull(list);
    return removeAll(list.toGenericList());
  }

  @Override
  public boolean removeAll(int... values) {
    Check.notNull(values);
    return removeAll(ArrayMethods.asList(values));
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    Check.notNull(c);
    Set<Integer> set = new LinkedHashSet<>(toGenericList());
    if (set.removeAll(c)) {
      buf = unbox(set.toArray(Integer[]::new));
      size = set.size();
      return true;
    }
    return false;
  }

  @Override
  public boolean retainAll(IntList list) {
    Check.notNull(list);
    return retainAll(list.toGenericList());
  }

  @Override
  public boolean retainAll(int... values) {
    Check.notNull(values);
    return retainAll(ArrayMethods.asList(values));
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    Check.notNull(c);
    Set<Integer> set = new LinkedHashSet<>(toGenericList());
    if (set.retainAll(c)) {
      buf = unbox(set.toArray(Integer[]::new));
      size = set.size();
      return true;
    }
    return false;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public int capacity() {
    return buf.length;
  }

  @Override
  public void setCapacity(int newCapacity) {
    if (newCapacity != buf.length) {
      Check.that(newCapacity, "new capacity")
          .is(gte(), 0)
          .is(lte(), Integer.MAX_VALUE);
      size = Math.min(size, newCapacity);
      int[] newBuf = new int[newCapacity];
      arraycopy(buf, 0, newBuf, 0, size);
      buf = newBuf;
    }
  }

  @Override
  public void sort() {
    Arrays.sort(buf, 0, size);
  }

  @Override
  public void sortDescending() {
    sort();
    // Not ideal, but OK for now
    ArrayMethods.reverse(buf, 0, size);
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void clear() {
    size = 0;
  }

  @Override
  public void trim(int newSize) {
    size = Check.that(newSize, "new size").is(gte(), 0).is(lte(), size).ok();
  }

  @Override
  public int[] toArray() {
    if (size == 0) {
      return EMPTY_INT_ARRAY;
    }
    int[] b = new int[size];
    arraycopy(buf, 0, b, 0, size);
    return b;
  }

  @Override
  public int[] toArray(int from, int to) {
    int len = Check.fromTo(size, from, to);
    if ((size | len) == 0) {
      return EMPTY_INT_ARRAY;
    }
    int[] b = new int[len];
    arraycopy(buf, 0, b, 0, len);
    return b;
  }

  @Override
  public List<Integer> toGenericList() {
    return List.of(box(buf));
  }

  @Override
  public IntStream stream() {
    return Arrays.stream(buf, 0, size);
  }

  @Override
  public void forEach(IntConsumer action) {
    stream().forEach(action);
  }

  @Override
  public <E extends Throwable> void forEachThrowing(ThrowingIntConsumer<E> action)
      throws E {
    for (int i : buf) {
      action.accept(i);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (obj instanceof IntList il) {
      return size == il.size()
          && Arrays.equals(buf, 0, size, getBuffer(il), 0, size);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = buf[0];
    for (int i = 1; i < size; ++i) {
      hash = hash * 31 + buf[i];
    }
    return hash;
  }

  @Override
  public String toString() {
    return '[' + implodeInts(buf, size) + ']';
  }

  private void increaseCapacity(int minIncrease) {
    int capacity = resizeMethod.resize(buf.length, resizeAmount, minIncrease);
    int[] newBuf = new int[capacity];
    arraycopy(buf, 0, newBuf, 0, size);
    buf = newBuf;
  }

  private static int[] getBuffer(IntList other) {
    // IntList is sealed, and as far as we know only permits
    // IntArrayList and UnmodifiableIntList
    return other instanceof IntArrayList ial
        ? ial.buf
        : other instanceof UnmodifiableIntList uil
            ? uil.buf
            : fail(AssertionError::new);
  }

  private void checkIndex(int index) {
    Check.that(index).is(CommonChecks.indexOf(), buf, indexOutOfBounds(index));
  }

  private void checkIndexInclusive(int index) {
    Check.that(index).is(indexInclusiveOf(), buf, indexOutOfBounds(index));
  }

}
