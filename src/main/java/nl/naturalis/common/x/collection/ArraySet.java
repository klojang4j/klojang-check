package nl.naturalis.common.x.collection;

import nl.naturalis.common.ArrayMethods;
import nl.naturalis.check.Check;
import nl.naturalis.common.collection.DuplicateValueException;
import nl.naturalis.common.x.invoke.InvokeUtils;

import java.util.*;

import static nl.naturalis.common.ArrayMethods.EMPTY_OBJECT_ARRAY;
import static nl.naturalis.common.ArrayMethods.implode;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.common.collection.DuplicateValueException.ValueType.ELEMENT;

public final class ArraySet<E> extends ImmutableSet<E> {

  private static final String NULL_ELEMENTS_NOT_ALLOWED = "null elements not allowed";

  @SuppressWarnings({"rawtypes"})
  private static final ArraySet EMPTY = new ArraySet(EMPTY_OBJECT_ARRAY);

  @SuppressWarnings({"unchecked"})
  private static <E> ArraySet<E> empty() {
    return (ArraySet<E>) EMPTY;
  }

  /*
   * If trusted, the provided array is supposed to be internally generated, going out
   * of scope immediately, and known to contain unique values only. All bets are off
   * if this is not the case!!!! In that case the array will be swallowed rather than
   * copied into the ArraySet.
   */
  public static <E> ArraySet<E> of(E[] values, boolean trust) {
    if (values.length == 0) {
      return empty();
    } else if (trust) {
      return new ArraySet<>(values);
    }
    Set<E> set = new HashSet<>();
    for (E e : values) {
      Check.that(e).is(notNull(), NULL_ELEMENTS_NOT_ALLOWED);
      if (!set.add(e)) {
        throw new DuplicateValueException(ELEMENT, e);
      }
    }
    Object[] copy = new Object[values.length];
    System.arraycopy(values, 0, copy, 0, values.length);
    return new ArraySet<>(copy);
  }

  public static <E> ArraySet<E> copyOf(List<E> values, boolean trust) {
    if (values.size() == 0) {
      return empty();
    } else if (trust) {
      return new ArraySet<>(values.toArray());
    }
    Set<E> set = new HashSet<>();
    for (E e : values) {
      Check.that(e).is(notNull(), NULL_ELEMENTS_NOT_ALLOWED);
      if (!set.add(e)) {
        throw new DuplicateValueException(ELEMENT, e);
      }
    }
    return new ArraySet<>(values.toArray());
  }

  public static <E> ArraySet<E> copyOf(Set<E> set, boolean trust) {
    if (!trust) {
      Check.that(set, "set").is(deepNotNull());
    }
    return set instanceof ArraySet<E> arraySet
        ? arraySet
        : set.isEmpty() ? empty() : new ArraySet<>(set.toArray());
  }

  private final Object[] elems;

  private ArraySet(Object[] elems) {
    this.elems = elems;
  }

  @Override
  public int size() {
    return elems.length;
  }

  @Override
  public boolean isEmpty() {
    return elems.length == 0;
  }

  @Override
  public boolean contains(Object o) {
    return ArrayMethods.isElementOf(o, elems);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return new HashSet<>(this).containsAll(c);
  }

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {
      private int i = 0;

      @Override
      public boolean hasNext() {
        return i < elems.length;
      }

      @Override
      @SuppressWarnings({"unchecked"})
      public E next() {
        Check.that(i).is(lt(), size(), NoSuchElementException::new);
        return (E) elems[i++];
      }
    };
  }

  @Override
  public Object[] toArray() {
    if (this == EMPTY) {
      return EMPTY_OBJECT_ARRAY;
    }
    Object[] objs = new Object[elems.length];
    System.arraycopy(elems, 0, objs, 0, elems.length);
    return objs;
  }

  @Override
  public <T> T[] toArray(T[] a) {
    Check.notNull(a);
    int sz = elems.length;
    if (a.length < sz) {
      a = (T[]) InvokeUtils.newArray(a.getClass(), sz);
    }
    int i = 0;
    Object[] result = a;
    for (E val : this) {
      result[i++] = val;
    }
    if (a.length > sz) {
      a[sz] = null;
    }
    return a;
  }

  private int hash;
  private String str;

  @Override
  public int hashCode() {
    if (hash == 0) {
      hash = Arrays.hashCode(elems);
    }
    return hash;
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ArraySet other) {
      int len = other.elems.length;
      if (elems.length == len) {
        for (int i = 0; i < len; ++i) {
          if (!elems[i].equals(other.elems[i])) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    if (o instanceof Set s) {
      Iterator<E> itr = s.iterator();
      for (Object e : elems) {
        if (!itr.hasNext() || !e.equals(itr.next())) {
          return false;
        }
      }
      return !itr.hasNext();
    }
    return false;
  }

  @Override
  public String toString() {
    if (str == null) {
      str = '[' + implode(elems) + ']';
    }
    return str;
  }

}
