package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;
import nl.naturalis.common.x.invoke.InvokeUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;

import static java.lang.System.arraycopy;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static nl.naturalis.common.CollectionMethods.*;
import static nl.naturalis.common.ObjectMethods.ifNull;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.common.x.Constants.IMPLODE_SEPARATOR;
import static nl.naturalis.common.x.Param.*;

/**
 * Methods for working with arrays.
 */
public final class ArrayMethods {

  private ArrayMethods() {
    throw new UnsupportedOperationException();
  }

  /**
   * A zero-length Object array.
   */
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  /**
   * A zero-length String array.
   */
  public static final String[] EMPTY_STRING_ARRAY = new String[0];

  /**
   * A zero-length int array.
   */
  public static final int[] EMPTY_INT_ARRAY = new int[0];

  static final String START_INDEX = "Start index";
  static final String END_INDEX = "End index";

  private static final Long MAX_ARR_LEN = (long) Integer.MAX_VALUE;

  /**
   * Appends an element to an array.
   *
   * @param array the array to append the object to
   * @param obj the object to append
   * @param <T> the type of the array elements
   * @return a new array containing the original array plus the extra element
   */
  public static <T> T[] append(T[] array, T obj) {
    Check.notNull(array, ARRAY);
    T[] arr = fromTemplate(array, array.length + 1);
    arraycopy(array, 0, arr, 0, array.length);
    arr[array.length] = obj;
    return arr;
  }

  /**
   * Appends multiple elements to an array.
   *
   * @param array the array to append the objects to
   * @param obj0 the 1st object to append
   * @param obj1 the 2nd object to append
   * @param moreObjs more objects to append
   * @param <T> the type of the array elements
   * @return a new array containing the original array plus the extra elements
   */
  @SafeVarargs
  public static <T> T[] append(T[] array, T obj0, T obj1, T... moreObjs) {
    Check.notNull(array, ARRAY);
    Check.notNull(moreObjs, Param.MORE_OBJS);
    int sz = array.length + 2 + moreObjs.length;
    T[] arr = fromTemplate(array, sz);
    arraycopy(array, 0, arr, 0, array.length);
    arr[array.length] = obj0;
    arr[array.length + 1] = obj1;
    arraycopy(moreObjs, 0, arr, array.length + 2, moreObjs.length);
    return arr;
  }

  /**
   * Concatenates two arrays.
   *
   * @param arr0 The 1st array to go into the new array
   * @param arr1 The 2nd array to go into the new array
   * @param <T> the type of the array elements
   * @return a new array containing all elements of the specified arrays
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] concat(T[] arr0, T[] arr1) {
    return (T[]) concat(arr0, arr1, new Object[0][0]);
  }

  /**
   * Concatenates multiple arrays into a single array.
   *
   * @param arr0 The 1st array to go into the new array
   * @param arr1 The 2nd array to go into the new array
   * @param arr2 The 3rd array to go into the new array
   * @param moreArrays More arrays to concatenate
   * @param <T> the type of the array elements
   * @return a new array containing all elements of the specified arrays
   */
  @SafeVarargs
  public static <T> T[] concat(T[] arr0, T[] arr1, T[] arr2, T[]... moreArrays) {
    Check.notNull(arr0, "arr0");
    Check.notNull(arr1, "arr1");
    Check.notNull(arr2, "arr2");
    Check.notNull(moreArrays, "moreArrays");
    long x = Arrays.stream(moreArrays).flatMap(Arrays::stream).count();
    x += arr0.length + arr1.length + arr2.length;
    Check.that(x).is(LTE(), MAX_ARR_LEN, "too many elements: ${arg}");
    T[] all = fromTemplate(arr0, (int) x);
    int i = 0;
    arraycopy(arr0, 0, all, i, arr0.length);
    i += arr0.length;
    arraycopy(arr1, 0, all, i, arr1.length);
    i += arr1.length;
    arraycopy(arr2, 0, all, i, arr2.length);
    if (moreArrays.length != 0) {
      i += arr2.length;
      for (int j = 0; j < moreArrays.length; ++j) {
        T[] arr = moreArrays[j];
        Check.that(arr).is(notNull(), "array {0} must not be null", j + 4);
        arraycopy(arr, 0, all, i, arr.length);
        i += arr.length;
      }
    }
    return all;
  }

  /**
   * Returns {@code true} if the specified array contains the specified value.
   *
   * @param value the value to search for
   * @param array the array to search
   * @return whether the array contains the value
   * @see #indexOf(int[], int)
   */
  public static boolean isElementOf(int value, int[] array) {
    return indexOf(array, value).isPresent();
  }

  /**
   * Returns {@code true} if the specified array contains the specified value.
   *
   * @param value the value to search for
   * @param array the array to search
   * @param <T> the type of the array elements
   * @return whether the array contains the value
   * @see #indexOf(Object[], Object)
   */
  public static <T> boolean isElementOf(T value, T[] array) {
    return indexOf(array, value) != -1;
  }

  /**
   * Returns {@code true} if the specified array contains the specified reference.
   *
   * @param reference the reference to search for
   * @param references the array to search
   * @return whether the array contains the reference
   * @see #refIndexOf(Object[], Object)
   */
  public static boolean isOneOf(Object reference, Object... references) {
    return refIndexOf(references, reference) != -1;
  }

  /**
   * Returns an {@link OptionalInt} containing the array index of the first
   * occurrence of the specified value within the specified array. Returns an empty
   * {@link OptionalInt} if the array does not contain the value.
   *
   * @param array the array to search
   * @param value the value to search for
   * @return an {@link OptionalInt} containing the array index of the value
   */
  public static OptionalInt indexOf(int[] array, int value) {
    Check.notNull(array, ARRAY);
    for (int x = 0; x < array.length; ++x) {
      if (array[x] == value) {
        return OptionalInt.of(x);
      }
    }
    return OptionalInt.empty();
  }

  /**
   * Returns an {@link OptionalInt} containing the array index of the last occurrence
   * of the specified value within the specified array. Returns an empty
   * {@link OptionalInt} if the array does not contain the value.
   *
   * @param array the array to search
   * @param value the value to search for
   * @return an {@link OptionalInt} containing the array index of the value
   */
  public static OptionalInt lastIndexOf(int[] array, int value) {
    Check.notNull(array, ARRAY);
    for (int x = array.length - 1; x >= 0; --x) {
      if (array[x] == value) {
        return OptionalInt.of(x);
      }
    }
    return OptionalInt.empty();
  }

  /**
   * Returns the array index of the first occurrence of the specified value within
   * the specified array. Returns -1 if the array does not contain the value.
   * Searching for null is allowed.
   *
   * @param <T> the type of the elements within the array
   * @param array the array to search
   * @param value the value to search for (may be null)
   * @return the array index of the value
   */
  public static <T> int indexOf(T[] array, T value) {
    Check.notNull(array, ARRAY);
    if (value == null) {
      for (int x = 0; x < array.length; ++x) {
        if (array[x] == null) {
          return x;
        }
      }
    } else {
      for (int x = 0; x < array.length; ++x) {
        if (value.equals(array[x])) {
          return x;
        }
      }
    }
    return -1;
  }

  /**
   * Returns the array index of the last occurrence of the specified value within the
   * specified array. Returns -1 if the array does not contain the value. Searching
   * for null is allowed.
   *
   * @param <T> the type of the elements within the array
   * @param array the array to search
   * @param value the value to search for (may be null)
   * @return the array index of the value
   */
  public static <T> int lastIndexOf(T[] array, T value) {
    Check.notNull(array, ARRAY);
    if (value == null) {
      for (int x = array.length - 1; x >= 0; --x) {
        if (array[x] == null) {
          return x;
        }
      }
    } else {
      for (int x = array.length - 1; x >= 0; --x) {
        if (value.equals(array[x])) {
          return x;
        }
      }
    }
    return -1;
  }

  /**
   * Returns the array index of the first occurrence of the specified object, using
   * reference comparisons to identify the object. Returns -1 if the array does not
   * contain the specified reference. Searching for null is
   * <i>not</i> allowed.
   *
   * @param array the array to search
   * @param reference The reference to search for (must not be null)
   * @return the array index of the reference
   */
  public static int refIndexOf(Object[] array, Object reference) {
    Check.notNull(array, ARRAY);
    Check.notNull(reference, "reference");
    for (int i = 0; i < array.length; ++i) {
      if (array[i] == reference) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns the array index of the last occurrence of the specified object, using
   * reference comparisons to identify the object. Returns -1 if the array does not
   * contain the specified reference. Searching for null is <i>not</i> allowed.
   *
   * @param array the array to search
   * @param reference The reference to search for (must not be null)
   * @return the array index of the reference
   */
  public static int refLastIndexOf(Object[] array, Object reference) {
    Check.notNull(array, ARRAY);
    Check.notNull(reference, "reference");
    for (int i = array.length - 1; i >= 0; --i) {
      if (array[i] == reference) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns the first array element that passes the specified test, or
   * {@link Result#notAvailable()} if no element passed the test.
   *
   * @param array the array
   * @param test the test
   * @param <T> the type of the array elements
   * @return the first array element that passes the specified test, or
   *     {@link Result#notAvailable()} if no element passed the test.
   */
  public static <T> Result<T> find(T[] array, Predicate<T> test) {
    return find(array, test, identity());
  }

  /**
   * Returns a {@link Result} containing a property of the first array element that
   * passes the specified test, or {@link Result#notAvailable()} if no element passed
   * the test.
   *
   * <blockquote><pre>{@code
   * Person[] persons = getPersons();
   * find(persons, p -> p.age() < 15, Person::firstName).ifAvailable(
   *    name -> System.out.printf("And the winner is ..... %s!!!%n, name);
   * );
   * }</pre></blockquote>
   *
   * @param array the array
   * @param test the test
   * @param property a function that extracts some value from thr array element
   * @param <T> the type of the array elements
   * @param <R> the type of the value extracted from the array element
   * @return the {@code Result} containing the value extracted from the first array
   *     element that passed the specified test, or {@code null} if no element passed
   *     the test
   */
  public static <T, R> Result<R> find(T[] array,
      Predicate<T> test,
      Function<T, R> property) {
    Check.notNull(array, ARRAY);
    Check.notNull(test, Param.TEST);
    Check.notNull(property, Param.PROPERTY);
    return Arrays.stream(array)
        .filter(test)
        .map(property)
        .map(Result::of)
        .findFirst()
        .orElse(Result.notAvailable());
  }

  /**
   * Returns the first array element that passes the specified test, or an empty
   * {@code OptionalInt} if no element passed the test.
   *
   * @param array the array
   * @param test the test
   * @return the first array element that passes the specified test, or an empty
   *     {@code OptionalInt} if no element passed the test
   */
  public static OptionalInt find(int[] array, IntPredicate test) {
    Check.notNull(array, ARRAY);
    Check.notNull(test, TEST);
    return Arrays.stream(array)
        .filter(test)
        .mapToObj(OptionalInt::of)
        .findFirst()
        .orElse(OptionalInt.empty());
  }

  /**
   * Reverses the order of the elements in the specified array.
   *
   * @param array the array
   * @param <T> the type of the array elements
   * @return the input array
   */
  public static <T> T[] reverse(T[] array) {
    return doReverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the elements in the specified array segment.
   *
   * @param array the array
   * @param from the start index (inclusive) of the array segment
   * @param to the end index (exclusive) of the array segment
   * @param <T> the type of the array elements
   * @return the input array
   */
  public static <T> T[] reverse(T[] array, int from, int to) {
    Check.fromTo(array, from, to);
    return doReverse(array, from, to);
  }

  private static <T> T[] doReverse(T[] array, int from, int to) {
    for (int i = 0, j = to - 1; i < (to - from) / 2; ++i, --j) {
      T tmp = array[from + i];
      array[from + i] = array[j];
      array[j] = tmp;
    }
    return array;
  }

  /**
   * Reverses the order of the elements in the specified array.
   *
   * @param array the array
   * @return the input array
   */
  public static int[] reverse(int[] array) {
    return doReverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the elements in the specified array segment
   *
   * @param array the array
   * @param from the start index (inclusive) of the array segment
   * @param to the end index (exclusive) of the array segment
   * @return the input array
   */
  public static int[] reverse(int[] array, int from, int to) {
    Check.notNull(array, ARRAY);
    Check.fromTo(array.length, from, to);
    return doReverse(array, from, to);
  }

  private static int[] doReverse(int[] array, int from, int to) {
    for (int i = 0, j = to - 1; i < (to - from) / 2; ++i, --j) {
      int tmp = array[from + i];
      array[from + i] = array[j];
      array[j] = tmp;
    }
    return array;
  }

  private static final Map<Class<?>, ToIntFunction<Object>> hashCoders =
      Map.of(
          int[].class, obj -> Arrays.hashCode((int[]) obj),
          long[].class, obj -> Arrays.hashCode((long[]) obj),
          double[].class, obj -> Arrays.hashCode((double[]) obj),
          float[].class, obj -> Arrays.hashCode((float[]) obj),
          char[].class, obj -> Arrays.hashCode((char[]) obj),
          short[].class, obj -> Arrays.hashCode((short[]) obj),
          byte[].class, obj -> Arrays.hashCode((byte[]) obj));

  /**
   * Returns the hash code of an array. Allows you to retrieve the hash code of an
   * array object even if you don't know its exact type. An
   * {@link IllegalArgumentException} is thrown if the argument is not an array.
   *
   * @param array the array
   * @return Its hash code
   */
  public static int hashCode(Object array) {
    Check.notNull(array).is(array());
    if (array instanceof Object[] objs) {
      return Arrays.hashCode(objs);
    }
    return hashCoders.get(array.getClass()).applyAsInt(array);
  }

  /**
   * Returns the deep hash code of an array. Allow you to retrieve the deep hash code
   * of an array object even if you don't know its exact type. An
   * {@link IllegalArgumentException} is thrown if the argument is not an array.
   *
   * @param array the array
   * @return Its deep hash code
   */
  public static int deepHashCode(Object array) {
    Check.notNull(array).is(array());
    if (array instanceof Object[] objs) {
      return Arrays.deepHashCode(objs);
    }
    return hashCoders.get(array.getClass()).applyAsInt(array);
  }

  /**
   * PHP-style implode method, concatenating the array elements using ", "
   * (comma-space) as separator. Optimized for {@code int[]} arrays.
   *
   * @param array the array to implode
   * @return a concatenation of the elements in the array.
   */
  public static String implodeInts(int[] array) {
    return implodeInts(array, IMPLODE_SEPARATOR);
  }

  /**
   * PHP-style implode method, concatenating the array elements using the specified
   * separator. Optimized for {@code int[]} arrays.
   *
   * @param array the array to implode
   * @param separator the string used to separate the elements
   * @return a concatenation of the elements in the array.
   */
  public static String implodeInts(int[] array, String separator) {
    return implodeInts(array, separator, -1);
  }

  /**
   * PHP-style implode method, concatenating at most {@code limit} array elements
   * using ", " (comma-space) as separator.
   *
   * @param array the array to implode
   * @param limit The maximum number of elements to collect. The specified number
   *     will be clamped to {@code array.length} (i.e. it's OK to specify a number
   *     greater than {@code array.length}). You can specify -1 as a shorthand for
   *     {@code array.length}.
   * @return a concatenation of the elements in the array.
   */
  public static String implodeInts(int[] array, int limit) {
    return implodeInts(array, IMPLODE_SEPARATOR, limit);
  }

  /**
   * PHP-style implode method, concatenating at most {@code limit} array elements
   * using ", " (comma-space) as separator.
   *
   * @param array the array to implode
   * @param stringifier A {@code Function} that converts the array elements to
   *     strings
   * @return a concatenation of the elements in the array.
   */
  public static String implodeInts(int[] array, IntFunction<String> stringifier) {
    return implodeInts(array, stringifier, IMPLODE_SEPARATOR, 0, -1);
  }

  /**
   * PHP-style implode method, concatenating at most {@code limit} array elements
   * using the specified separator.
   *
   * @param array the array to implode
   * @param separator the string used to separate the elements
   * @param limit The maximum number of elements to collect. The specified number
   *     will be clamped to {@code array.length} (i.e. it's OK to specify a number
   *     greater than {@code array.length}). You can specify -1 as a shorthand for
   *     {@code array.length}.
   * @return a concatenation of the elements in the array.
   */
  public static String implodeInts(int[] array, String separator, int limit) {
    return implodeInts(array, String::valueOf, separator, 0, limit);
  }

  /**
   * PHP-style implode method, optimized for {@code int[]} arrays.
   *
   * @param array the array to implode
   * @param stringifier A {@code Function} that converts the array elements to
   *     strings
   * @param separator the string used to separate the elements
   * @param from The index of the element to begin the concatenation with
   *     (inclusive)
   * @param to The index of the element to end the concatenation with
   *     (exclusive). The specified number will be clamped to {@code array.length}
   *     (i.e. it's OK to specify a number greater than {@code array.length}). You
   *     can specify -1 as a shorthand for {@code array.length}.
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, Function, String, int, int)
   */
  public static String implodeInts(int[] array,
      IntFunction<String> stringifier,
      String separator,
      int from,
      int to) {
    Check.notNull(array, ARRAY);
    Check.notNull(separator, SEPARATOR);
    Check.that(from, FROM_INDEX).is(gte(), 0).is(lte(), array.length);
    int x = to == -1 ? array.length : Math.min(to, array.length);
    Check.that(x, TO_INDEX).is(gte(), from);
    return Arrays.stream(array, from, x)
        .mapToObj(stringifier)
        .collect(joining(separator));
  }

  /**
   * PHP-style implode method, concatenating the array elements using ", "
   * (comma-space) as separator. This method is primarily meant to implode primitive
   * arrays, but you <i>can</i> use it to implode any type of array. An
   * {@link IllegalArgumentException} is thrown if {@code array} is not an array.
   *
   * @param array the array to implode
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, String)
   */
  public static String implodeAny(Object array) {
    return implodeAny(array, Objects::toString, IMPLODE_SEPARATOR, 0, -1);
  }

  /**
   * PHP-style implode method, concatenating the array elements using the specified
   * separator. This method is primarily meant to implode primitive arrays, but you
   * <i>can</i> use it to implode any type of array. An {@link
   * IllegalArgumentException} is thrown if {@code array} is not an array.
   *
   * @param array the array to implode
   * @param separator the string used to separate the elements
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, String)
   */
  public static String implodeAny(Object array, String separator) {
    return implodeAny(array, separator, -1);
  }

  /**
   * PHP-style implode method, concatenating the array elements using ", "
   * (comma-space) as separator. This method is primarily meant to implode primitive
   * arrays, but you <i>can</i> use it to implode any type of array. An
   * {@link IllegalArgumentException} is thrown if {@code array} is not an array.
   *
   * @param array the array to implode
   * @param stringifier A {@code Function} that converts the array elements to
   *     strings
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, String)
   */
  public static String implodeAny(Object array,
      Function<Object, String> stringifier) {
    return implodeAny(array, stringifier, IMPLODE_SEPARATOR, 0, -1);
  }

  /**
   * PHP-style implode method, concatenating at most {@code limit} array elements
   * using ", " (comma-space) as separator. This method is primarily meant to implode
   * primitive arrays, but you <i>can</i> use it to implode any type of array. An
   * {@link IllegalArgumentException} is thrown if {@code array} is not an array.
   *
   * @param array the array to implode
   * @param limit The maximum number of elements to collect. The specified number
   *     will be clamped to {@code array.length} (i.e. it's OK to specify a number
   *     greater than {@code array.length}). You can specify -1 as a shorthand for
   *     {@code array.length}.
   * @return a concatenation of the elements in the array.
   */
  public static String implodeAny(Object array, int limit) {
    return implodeAny(array, IMPLODE_SEPARATOR, limit);
  }

  /**
   * PHP-style implode method, concatenating at most {@code limit} array elements
   * using the specified separator. This method is primarily meant to implode
   * primitive arrays, but you <i>can</i> use it to implode any type of array. An
   * {@link IllegalArgumentException} is thrown if {@code array} is not an array.
   *
   * @param array the array to implode
   * @param separator the string used to separate the elements
   * @param limit The maximum number of elements to collect. The specified number
   *     will be clamped to {@code array.length} (i.e. it's OK to specify a number
   *     greater than {@code array.length}). You can specify -1 as a shorthand for
   *     {@code array.length}.
   * @return a concatenation of the elements in the array.
   */
  public static String implodeAny(Object array, String separator, int limit) {
    return implodeAny(array, Objects::toString, separator, 0, limit);
  }

  /**
   * PHP-style implode method. This method is primarily meant to implode primitive
   * arrays, but you <i>can</i> use it to implode any type of array. An
   * {@link IllegalArgumentException} is thrown if {@code array} is not an array.
   *
   * @param array the array to implode
   * @param stringifier A {@code Function} that converts the array elements to
   *     strings
   * @param separator the string used to separate the elements
   * @param from The index of the element to begin the concatenation with
   *     (inclusive)
   * @param to The index of the element to end the concatenation with
   *     (exclusive). The specified number will be clamped to {@code array.length}
   *     (i.e. it's OK to specify a number greater than {@code array.length}). You
   *     can specify -1 as a shorthand for {@code array.length}.
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, Function, String, int, int)
   */
  public static String implodeAny(Object array,
      Function<Object, String> stringifier,
      String separator,
      int from,
      int to) {
    int len = Check.notNull(array, ARRAY)
        .is(array())
        .ok(InvokeUtils::getArrayLength);
    Check.notNull(stringifier, STRINGIFIER);
    Check.notNull(separator, SEPARATOR);
    Check.that(from, FROM_INDEX).is(gte(), 0).is(lte(), len);
    int x = to == -1 ? len : Math.min(to, len);
    Check.that(x, TO_INDEX).is(gte(), from);
    return IntStream.range(from, x)
        .mapToObj(i -> InvokeUtils.getArrayElement(array, i))
        .map(stringifier)
        .collect(joining(separator));
  }

  /**
   * PHP-style implode method, concatenating the array elements using ", "
   * (comma-space) as separator.
   *
   * @param array the collection to implode
   * @param <T> the type of the array elements
   * @return a concatenation of the elements in the collection.
   * @see CollectionMethods#implode(Collection)
   */
  public static <T> String implode(T[] array) {
    return implode(array, IMPLODE_SEPARATOR);
  }

  /**
   * PHP-style implode method, concatenating the array elements using the specified
   * separator.
   *
   * @param array the array to implode
   * @param separator the string used to separate the elements
   * @param <T> the type of the array elements
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, String)
   */
  public static <T> String implode(T[] array, String separator) {
    Check.notNull(array);
    return implode(array, Objects::toString, separator, 0, -1);
  }

  /**
   * PHP-style implode method, concatenating at most {@code limit} array elements
   * using ", " (comma+space) as separator.
   *
   * @param array the array to implode
   * @param limit The maximum number of elements to collect. The specified number
   *     will be clamped to {@code array.length} (i.e. it's OK to specify a number
   *     greater than {@code array.length}). You can specify -1 as a shorthand for
   *     {@code array.length}.
   * @param <T> the type of the array elements
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, int)
   */
  public static <T> String implode(T[] array, int limit) {
    return implode(array, IMPLODE_SEPARATOR, limit);
  }

  /**
   * PHP-style implode method, concatenating at most {@code limit} array elements
   * using ", " (comma+space) as separator.
   *
   * @param array the array to implode
   * @param stringifier A {@code Function} that converts the array elements to
   *     strings
   * @param <T> the type of the array elements
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, int)
   */
  public static <T> String implode(T[] array, Function<T, String> stringifier) {
    return implode(array, stringifier, IMPLODE_SEPARATOR, 0, -1);
  }

  /**
   * PHP-style implode method, concatenating at most {@code limit} array elements
   * using the specified separator.
   *
   * @param array the array to implode
   * @param separator the string used to separate the elements
   * @param limit The maximum number of elements to collect. The specified number
   *     will be clamped to {@code array.length} (i.e. it's OK to specify a number
   *     greater than {@code array.length}). You can specify -1 as a shorthand for
   *     {@code array.length}.
   * @param <T> the type of the array elements
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, String, int)
   */
  public static <T> String implode(T[] array, String separator, int limit) {
    return implode(array, Objects::toString, separator, 0, limit);
  }

  /**
   * PHP-style implode method.
   *
   * @param array the array to implode
   * @param stringifier A {@code Function} that converts the array elements to
   *     strings
   * @param separator the string used to separate the elements
   * @param from The index of the element to begin the concatenation with
   *     (inclusive)
   * @param to The index of the element to end the concatenation with
   *     (exclusive). The specified number will be clamped to {@code array.length}
   *     (i.e. it's OK to specify a number greater than {@code array.length}). You
   *     can specify -1 as a shorthand for {@code array.length}.
   * @param <T> the type of the array elements
   * @return a concatenation of the elements in the array.
   * @see CollectionMethods#implode(Collection, Function, String, int, int)
   */
  public static <T> String implode(T[] array,
      Function<T, String> stringifier,
      String separator,
      int from,
      int to) {
    Check.notNull(array, ARRAY);
    Check.notNull(stringifier, STRINGIFIER);
    Check.notNull(separator, SEPARATOR);
    Check.that(from, FROM_INDEX).is(gte(), 0).is(lte(), array.length);
    int x = to == -1 ? array.length : Math.min(to, array.length);
    Check.that(x, TO_INDEX).is(gte(), from);
    return Arrays.stream(array, from, x)
        .map(stringifier)
        .collect(joining(separator));
  }

  /**
   * Simply returns the specified array, but allows for leaner code when statically
   * imported.
   *
   * @param objs the array
   * @param <T> the type of the objects to pack
   * @return the same array
   */
  @SafeVarargs
  public static <T> T[] pack(T... objs) {
    return Check.notNull(objs).ok();
  }

  /**
   * Simply returns the specified array, but allows for leaner code when statically
   * imported.
   *
   * @param ints the array
   * @return the same array
   */
  public static int[] ints(int... ints) {
    return Check.notNull(ints).ok();
  }

  /**
   * Simply returns the specified array, but allows for leaner code when statically
   * imported.
   *
   * @param chars the array
   * @return the same array
   */
  public static char[] chars(char... chars) {
    return Check.notNull(chars).ok();
  }

  /**
   * Simply returns the specified array, but allows for leaner code when statically
   * imported.
   *
   * @param doubles the array
   * @return the same array
   */
  public static double[] doubles(double... doubles) {
    return Check.notNull(doubles).ok();
  }

  /**
   * Simply returns the specified array, but allows for leaner code when statically
   * imported.
   *
   * @param longs the array
   * @return the same array
   */
  public static long[] longs(long... longs) {
    return Check.notNull(longs).ok();
  }

  /**
   * Simply returns the specified array, but allows for leaner code when statically
   * imported.
   *
   * @param floats The array
   * @return the same array
   */
  public static float[] floats(float... floats) {
    return Check.notNull(floats).ok();
  }

  /**
   * Prefixes the specified object to the specified array.
   *
   * @param array the array to be prefixed
   * @param obj the object to prefix
   * @param <T> the type of the array elements and the object to be prefixed
   * @return a new array containing the specified object and the elements of the
   *     specified array
   */
  public static <T> T[] prefix(T[] array, T obj) {
    Check.notNull(array, ARRAY);
    T[] res = fromTemplate(array, array.length + 1);
    res[0] = obj;
    arraycopy(array, 0, res, 1, array.length);
    return res;
  }

  /**
   * Prefixes the specified object to the specified array.
   *
   * @param array the array to be prefixed
   * @param obj0 the 1st object to prefix
   * @param obj1 the 2nd object to prefix
   * @param moreObjs more objects to prefix
   * @param <T> the type of the array elements and the object to be prefixed
   * @return a new array containing the specified objects and the elements of the
   *     specified array
   */
  @SafeVarargs
  public static <T> T[] prefix(T[] array, T obj0, T obj1, T... moreObjs) {
    Check.notNull(array, ARRAY);
    Check.notNull(moreObjs, MORE_OBJS);
    int sz = array.length + 2 + moreObjs.length;
    T[] res = fromTemplate(array, sz);
    res[0] = obj0;
    res[1] = obj1;
    arraycopy(moreObjs, 0, res, 2, moreObjs.length);
    arraycopy(array, 0, res, 2 + moreObjs.length, array.length);
    return res;
  }

  /**
   * Converts an {@code int} array to a {@code List<Integer>}.
   *
   * @param values the array elements.
   * @return a {@code List} containing the same elements in the same order
   */
  public static List<Integer> asList(int[] values) {
    return Arrays.asList(box(values));
  }

  /**
   * Converts a {@code float} array to a {@code List<Float>}.
   *
   * @param values the array elements.
   * @return a {@code List} containing the same elements in the same order
   */
  public static List<Float> asList(float[] values) {
    return Arrays.asList(box(values));
  }

  /**
   * Converts a {@code double} array to a {@code List<Double>}.
   *
   * @param values the array elements.
   * @return a {@code List} containing the same elements in the same order
   */
  public static List<Double> asList(double[] values) {
    return Arrays.asList(box(values));
  }

  /**
   * Converts a {@code long} array to a {@code List<Long>}.
   *
   * @param values the array elements.
   * @return a {@code List} containing the same elements in the same order
   */
  public static List<Long> asList(long[] values) {
    return Arrays.asList(box(values));
  }

  /**
   * Converts a {@code short} array to a {@code List<Short>}.
   *
   * @param values the array elements.
   * @return a {@code List} containing the same elements in the same order
   */
  public static List<Short> asList(short[] values) {
    return Arrays.asList(box(values));
  }

  /**
   * Converts a {@code byte} array to a {@code List<Byte>}.
   *
   * @param values the array elements.
   * @return a {@code List} containing the same elements in the same order
   */
  public static List<Byte> asList(byte[] values) {
    return Arrays.asList(box(values));
  }

  /**
   * Converts a {@code char} array to a {@code List<Character>}.
   *
   * @param values the array elements.
   * @return a {@code List} containing the same elements in the same order
   */
  public static List<Character> asList(char[] values) {
    return Arrays.asList(box(values));
  }

  /**
   * Converts a {@code boolean} array to a {@code List<Boolean>}.
   *
   * @param values the array elements.
   * @return a {@code List} containing the same elements in the same order
   */
  public static List<Boolean> asList(boolean[] values) {
    return Arrays.asList(box(values));
  }

  /**
   * Converts an {@code Integer} array to an {@code int} array.
   *
   * @param values the {@code Integer} array
   * @param dfault The {@code int} value to convert {@code null} elements in the
   *     source array to
   * @return the {@code int} array
   */
  public static int[] unbox(Integer[] values, int dfault) {
    Check.notNull(values);
    return Arrays.stream(values).mapToInt(i -> ifNull(i, dfault)).toArray();
  }

  /**
   * Converts an {@code Integer} array to an {@code int} array. {@code null} elements
   * in the source array are converted to 0 (zero).
   *
   * @param values the {@code Integer} array
   * @return the {@code int} array
   */
  public static int[] unbox(Integer[] values) {
    return unbox(values, 0);
  }

  /**
   * Converts an {@code int} array to an {@code Integer} array.
   *
   * @param values the {@code int} array
   * @return the {@code Integer} array
   */
  public static Integer[] box(int[] values) {
    return Check.notNull(values).ok(Arrays::stream).boxed().toArray(Integer[]::new);
  }

  /**
   * Converts a {@code double} array to a {@code Double} array.
   *
   * @param values the {@code double} array
   * @return the {@code Double} array
   */
  public static Double[] box(double[] values) {
    return Check.notNull(values).ok(Arrays::stream).boxed().toArray(Double[]::new);
  }

  /**
   * Converts a {@code long} array to a {@code Long} array.
   *
   * @param values the {@code long} array
   * @return the {@code Long} array
   */
  public static Long[] box(long[] values) {
    return Check.notNull(values).ok(Arrays::stream).boxed().toArray(Long[]::new);
  }

  /**
   * Converts a {@code float} array to a {@code Float} array.
   *
   * @param values the {@code float} array
   * @return the {@code Float} array
   */
  public static Float[] box(float[] values) {
    var res = new Float[Check.notNull(values).ok().length];
    for (int i = 0; i < values.length; ++i) {
      res[i] = values[i];
    }
    return res;
  }

  /**
   * Converts a {@code short} array to a {@code Short} array.
   *
   * @param values the {@code short} array
   * @return the {@code Short} array
   */
  public static Short[] box(short[] values) {
    var res = new Short[Check.notNull(values).ok().length];
    for (int i = 0; i < values.length; ++i) {
      res[i] = values[i];
    }
    return res;
  }

  /**
   * Converts a {@code byte} array to a {@code Byte} array.
   *
   * @param values the {@code byte} array
   * @return the {@code Byte} array
   */
  public static Byte[] box(byte[] values) {
    var res = new Byte[Check.notNull(values).ok().length];
    for (int i = 0; i < values.length; ++i) {
      res[i] = values[i];
    }
    return res;
  }

  /**
   * Converts a {@code char} array to a {@code Character} array.
   *
   * @param values the {@code char} array
   * @return the {@code Character} array
   */
  public static Character[] box(char[] values) {
    var res = new Character[Check.notNull(values).ok().length];
    for (int i = 0; i < values.length; ++i) {
      res[i] = values[i];
    }
    return res;
  }

  /**
   * Converts a {@code boolean} array to a {@code Boolean} array.
   *
   * @param values the {@code boolean} array
   * @return the {@code Boolean} array
   */
  public static Boolean[] box(boolean[] values) {
    var res = new Boolean[Check.notNull(values).ok().length];
    for (int i = 0; i < values.length; ++i) {
      res[i] = values[i];
    }
    return res;
  }

  @SuppressWarnings("unchecked")
  private static <T> T[] fromTemplate(T[] template, int length) {
    return (T[]) InvokeUtils.newArray(template.getClass(), length);
  }

}
