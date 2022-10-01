package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.check.CommonChecks;
import nl.naturalis.common.collection.TypeMap;
import nl.naturalis.common.function.IntRelation;
import nl.naturalis.common.function.Relation;
import nl.naturalis.common.function.ThrowingSupplier;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;
import static nl.naturalis.check.CommonChecks.notNull;
import static nl.naturalis.common.ArrayMethods.isElementOf;
import static nl.naturalis.common.ClassMethods.isPrimitiveArray;
import static nl.naturalis.common.x.invoke.InvokeUtils.getArrayLength;

/**
 * General methods applicable to objects of any type.
 *
 * @author Ayco Holleman
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ObjectMethods {

  private static final String ERR_NULL_OPTIONAL = "Optional must not be null";

  private ObjectMethods() {
    throw new UnsupportedOperationException();
  }

  /**
   * Does a brute-force cast of one type to another. This method may come in handy,
   * especially in the form of a method reference, when bumping up against the limits
   * of Java's implementation of generics:
   *
   * <blockquote><pre>{@code
   * List<CharSequence> list1 = List.of("Hello", "world");
   * // WON'T COMPILE: List<String> list2 = list1;
   * List<String> list2 = bruteCast(list1);
   * }</pre></blockquote>
   *
   * <p>Handle with care, though, as it will just as easily brute-force its way to a
   * {@link ClassCastException} at runtime:
   *
   * <blockquote><pre>{@code
   * String s = bruteCast(new File("/tmp/foo.txt")); // compiles
   * }</pre></blockquote>
   *
   * @param input The value to be cast
   * @param <T> the input type
   * @param <R> The output type
   * @return the cast value
   */
  public static <T, R> R bruteCast(T input) {
    return (R) input;
  }

  /**
   * Returns whether the specified {@code CharSequence} is null or empty.
   *
   * @param arg the {@code CharSequence} to check
   * @return whether it is null or empty
   */
  public static boolean isEmpty(CharSequence arg) {
    return arg == null || arg.isEmpty();
  }

  /**
   * Returns whether the specified array is null or empty.
   *
   * @param arg the array to check
   * @return whether it is null or empty
   */
  public static boolean isEmpty(Object[] arg) {
    return arg == null || arg.length == 0;
  }

  /**
   * Returns whether the specified {@code Collection} is null or empty.
   *
   * @param arg the {@code Collection} to check
   * @return whether it is null or empty
   */
  public static boolean isEmpty(Collection<?> arg) {
    return arg == null || arg.isEmpty();
  }

  /**
   * Returns whether the specified {@code Map} is null or empty.
   *
   * @param arg the {@code Map} to check
   * @return whether it is null or empty
   */
  public static boolean isEmpty(Map<?, ?> arg) {
    return arg == null || arg.isEmpty();
  }

  /**
   * Returns whether the specified {@code Optional} is empty or contains an empty
   * object. This is the only {@code isEmpty} method that will throw an
   * {@code IllegalArgumentException} if the argument is null as {@code Optional}
   * objects should never be null.
   *
   * @param arg the {@code Optional} to check
   * @return whether it is empty or contains an empty object
   */
  public static boolean isEmpty(Optional<?> arg) {
    Check.that(arg).is(notNull(), ERR_NULL_OPTIONAL);
    return arg.isEmpty() || isEmpty(arg.get());
  }

  /**
   * Returns whether the specified array is null or empty.
   *
   * @param arg the array to check
   * @return whether it is null or empty
   */
  public static boolean isEmpty(Emptyable arg) {
    return arg == null || arg.isEmpty();
  }

  private static final TypeMap<Predicate> emptyChecks = TypeMap.<Predicate>typeGraphBuilder()
      .autobox(false)
      .add(CharSequence.class, x -> ((CharSequence) x).isEmpty())
      .add(Collection.class, x -> ((Collection) x).isEmpty())
      .add(Map.class, x -> ((Map) x).isEmpty())
      .add(Emptyable.class, x -> ((Emptyable) x).isEmpty())
      .add(Optional.class, ObjectMethods::isEmptyOptional)
      .add(Object.class, x -> x.getClass().isArray() && getArrayLength(x) == 0)
      .freeze();

  private static boolean isEmptyOptional(Object x) {
    Optional y = (Optional) x;
    return y.isEmpty() || isEmpty(y.get());
  }

  /**
   * Returns whether the specified argument is null or empty. This method is (and can
   * be) used for broad-stroke methods like {@link #ifEmpty(Object, Object)} and
   * {@link CommonChecks#empty()}, where you don't know the exact type of the
   * argument. More precisely, the method returns {@code true} if any of the
   * following applies:
   *
   * <p>
   *
   * <ul>
   *   <li>{@code arg} is {@code null}
   *   <li>{@code arg} is an empty {@link CharSequence}
   *   <li>{@code arg} is an empty {@link Collection}
   *   <li>{@code arg} is an empty {@link Map}
   *   <li>{@code arg} is an empty {@link Emptyable}
   *   <li>{@code arg} is a zero-length array
   *   <li>{@code arg} is an empty {@link Optional} <b>or</b> an {@code Optional} containing an
   *       empty value
   * </ul>
   *
   * <p>In any other case this method returns {@code false}.
   *
   * @param arg the argument to check
   * @return whether it is null or empty
   */
  public static boolean isEmpty(Object arg) {
    return arg == null || emptyChecks.get(arg.getClass()).test(arg);
  }

  /**
   * Verifies that the argument is recursively non-empty. More precisely, this method
   * returns {@code true} if the argument is <i>any</i> of the following:
   *
   * <p>
   *
   * <ul>
   *   <li>a non-empty {@link CharSequence}
   *   <li>a non-empty {@link Collection} containing only <i>deep-not-empty</i>
   *       elements
   *   <li>a non-empty {@link Map} containing only <i>deep-not-empty</i> keys and
   *       values
   *   <li>a <i>deep-not-empty</i> {@link Emptyable}
   *   <li>a non-zero-length {@code Object[]} containing only <i>deep-not-empty</i>
   *       elements
   *   <li>a non-zero-length array of primitives
   *   <li>a non-empty {@link Optional} containing a <i>deep-not-empty</i> value
   *   <li>a non-null object of any other type
   * </ul>
   *
   * @param arg The object to be tested
   * @return whether it is recursively non-empty
   * @see CommonChecks#deepNotEmpty()
   */
  public static boolean isDeepNotEmpty(Object arg) {
    return arg != null
        && (!(arg instanceof CharSequence cs) || cs.length() > 0)
        && (!(arg instanceof Collection c) || dne(c))
        && (!(arg instanceof Map m) || dne(m))
        && (!(arg instanceof Object[] x) || dne(x))
        && (!(arg instanceof Optional o) || dne(o))
        && (!(arg instanceof Emptyable e) || e.isDeepNotEmpty())
        && (!isPrimitiveArray(arg) || getArrayLength(arg) > 0);
  }

  private static boolean dne(Collection coll) {
    if (coll.isEmpty()) {
      return false;
    }
    return coll.stream().allMatch(ObjectMethods::isDeepNotEmpty);
  }

  private static boolean dne(Map map) {
    if (map.isEmpty()) {
      return false;
    }
    return map.entrySet().stream().allMatch(ObjectMethods::entryDeepNotEmpty);
  }

  private static boolean entryDeepNotEmpty(Object obj) {
    Map.Entry e = (Map.Entry) obj;
    return isDeepNotEmpty(e.getKey()) && isDeepNotEmpty(e.getValue());
  }

  private static boolean dne(Object[] arr) {
    if (arr.length == 0) {
      return false;
    }
    return Arrays.stream(arr).allMatch(ObjectMethods::isDeepNotEmpty);
  }

  private static boolean dne(Optional opt) {
    return opt.isPresent() && isDeepNotEmpty(opt.get());
  }

  /**
   * Verifies that the argument is not null and, if it is array, {@link Collection}
   * or {@link Map}, does not contain any null values. It may still be an empty
   * array, {@code Collection} or {@code Map}, however. For maps, both keys and
   * values are tested for {@code null}.
   *
   * @param arg the object to be tested
   * @return whether it is not null and does not contain any null values
   */
  public static boolean isDeepNotNull(Object arg) {
    if (arg == null) {
      return false;
    } else if (arg instanceof Object[] x) {
      return Arrays.stream(x).allMatch(notNull());
    } else if (arg instanceof Collection c) {
      if (CollectionMethods.isNullRepellent(c)) {
        return true;
      }
      return c.stream().allMatch(notNull());
    } else if (arg instanceof Map<?, ?> m) {
      return m.entrySet().stream()
          .allMatch(e -> e.getKey() != null && e.getValue() != null);
    }
    return true;
  }

  /**
   * Tests the provided arguments for equality using <i>empty-equals-null</i>
   * semantics. This is roughly equivalent to
   * {@code Objects.equals(e2n(arg0), e2n(arg1))}, except that {@code e2nEquals}
   * <i>does</i> take the types of the two arguments into account. So an empty
   * {@code String} is not equal to an empty {@code ArrayList} and an empty
   * {@code ArrayList} is not equal to an empty {@code HashSet}. (An empty
   * {@code HashSet} <i>is</i> equal to an empty {@code TreeSet}, but that is just
   * behaviour specified by the Collections Framework.)
   *
   * <p>
   *
   * <ol>
   *   <li>{@code null} equals an empty {@link CharSequence}
   *   <li>{@code null} equals an empty {@link Collection}
   *   <li>{@code null} equals an empty {@link Map}
   *   <li>{@code null} equals an empty array
   *   <li>{@code null} equals an empty {@link Optional} or an {@link Optional} containing an empty
   *       object
   *   <li>A empty instance of one type is not equal to a empty instance of another non-comparable
   *       type
   * </ol>
   *
   * @param arg0 The 1st of the pair of objects to compare
   * @param arg1 The 2nd of the pair of objects to compare
   * @return whether the provided arguments are equal using empty-equals-null
   *     semantics
   */
  public static boolean e2nEquals(Object arg0, Object arg1) {
    return (arg0 == arg1)
        || (arg0 == null && isEmpty(arg1))
        || (arg1 == null && isEmpty(arg0))
        || Objects.equals(arg0, arg1);
  }

  /**
   * Recursively tests the arguments for equality using <i>empty-equals-null</i>
   * semantics.
   *
   * @param arg0 The 1st of the pair of objects to compare
   * @param arg1 The 2nd of the pair of objects to compare
   * @return whether the provided arguments are deeply equal using empty-equals-null
   *     semantics
   */
  public static boolean e2nDeepEquals(Object arg0, Object arg1) {
    return (arg0 == arg1)
        || (arg0 == null && isEmpty(arg1))
        || (arg1 == null && isEmpty(arg0))
        || eq(arg0, arg1);
  }

  /**
   * Generates a hash code for the provided object using <i>empty-equals-null</i>
   * semantics. Empty objects (whatever their type and including {@code null}) all
   * have the same hash code: 0 (zero)! If the argument is any array, this method is
   * recursively applied to the array's elements. Therefore {@code e2nHashCode} in
   * effect generates a "deep" hash code.
   *
   * @param obj The object to generate a hash code for
   * @return the hash code
   */
  public static int e2nHashCode(Object obj) {
    if (isEmpty(obj)) {
      return 0;
    }
    int hash;
    if (obj instanceof Collection<?> c) {
      hash = 1;
      for (Object o : c) {
        hash = hash * 31 + e2nHashCode(o);
      }
    } else if (obj instanceof Map<?, ?> m) {
      hash = 1;
      for (Map.Entry e : m.entrySet()) {
        hash = hash * 31 + e2nHashCode(e.getKey());
        hash = hash * 31 + e2nHashCode(e.getValue());
      }
    } else if (obj instanceof Object[] a) {
      hash = 1;
      for (Object o : a) {
        hash = hash * 31 + e2nHashCode(o);
      }
    } else if (obj.getClass().isArray()) {
      hash = ArrayMethods.hashCode(obj);
    } else {
      hash = obj.hashCode();
    }
    return hash;
  }

  /**
   * <p>Generates a hash code for the provided object using <i>empty-equals-null</i>
   * semantics. This variant of {@code hashCode()} includes the type of the argument
   * in the computation of the hash code. As with {@link #e2nEquals(Object, Object)},
   * this ensures that an empty {@code String} will not have the same hash code as an
   * empty {@code ArrayList}, and an empty {@code ArrayList} will not have the same
   * hash code as an empty {@code HashSet}. An empty {@code HashSet} <i>will</i> have
   * the same hash code as an empty {@code TreeSet}, because for Collection Framework
   * classes it is hash code of the base type that is included in the computation of
   * the hash code (i.e. {@code List.class}, {@code Set.class} and
   * {@code Map.class}).
   *
   * <p>Thus, using {@code e2nTypedHashCode} will lead to fewer hash collisions
   * than {@code e2nHashcode}. However, it may, in some circumstances break the
   * contract for {@link Object#hashCode()}. A class could define an {@code equals}
   * method that allows instances of it to be equal to instances of its superclass.
   * However, {@code e2nTypedHashCode} precludes this as the hash code for the class
   * and the superclass will be different.
   *
   * <p>Also note that this anyhow only becomes relevant if the collection of
   * objects you work with is remarkably heterogeneous: strings, empty strings, sets,
   * empty sets, empty lists, empty arrays, etc.
   *
   * @param obj The object to generate a hash code for
   * @return the hash code
   */
  public static int e2nTypedHashCode(Object obj) {
    if (obj == null) {
      return 0;
    }
    int hash;
    if (obj instanceof String s) {
      hash = String.class.hashCode();
      if (!s.isEmpty()) {
        hash = hash * 31 + s.hashCode();
      }
    } else if (obj instanceof List x) {
      hash = List.class.hashCode();
      for (Object o : x) {
        hash = hash * 31 + e2nTypedHashCode(o);
      }
    } else if (obj instanceof Set x) {
      hash = Set.class.hashCode();
      for (Object o : x) {
        hash = hash * 31 + e2nTypedHashCode(o);
      }
    } else if (obj instanceof Map<?, ?> x) {
      hash = Map.class.hashCode();
      for (Map.Entry e : x.entrySet()) {
        hash = hash * 31 + e2nTypedHashCode(e.getKey());
        hash = hash * 31 + e2nTypedHashCode(e.getValue());
      }
    } else if (obj instanceof Object[] x) {
      hash = x.getClass().hashCode();
      for (Object o : x) {
        hash = hash * 31 + e2nTypedHashCode(o);
      }
    } else if (obj.getClass().isArray()) {
      hash = obj.getClass().hashCode();
      if (Array.getLength(obj) != 0) {
        hash = ArrayMethods.hashCode(obj);
      }
    } else if (obj instanceof Optional o) {
      hash = Optional.class.hashCode();
      if (o.isPresent() && !isEmpty(o.get())) {
        hash = hash * 31 + e2nTypedHashCode(o.get());
      }
    } else if (obj instanceof Result r) {
      hash = Result.class.hashCode();
      if (r.isAvailable() && !isEmpty(r.get())) {
        hash = hash * 31 + e2nTypedHashCode(r.get());
      }
    } else {
      hash = obj.hashCode();
    }
    return hash;
  }

  /**
   * Generates a hash code for the provided arguments using <i>empty-equals-null</i>
   * semantics. See {@link #hashCode()}.
   *
   * @param objs The objects to generate a hash code for
   * @return the hash code
   */
  public static int e2nHash(Object... objs) {
    if (objs == null) {
      return 0;
    }
    int hash = 1;
    for (Object obj : objs) {
      hash = hash * 31 + e2nHashCode(obj);
    }
    return hash;
  }

  /**
   * Returns the first argument if it is not null, else the second argument.
   *
   * @param value The value to return if not null
   * @param defVal The value to return if the first argument is {@code null}
   * @return A non-null value
   */
  public static <T> T ifNull(T value, T defVal) {
    return value == null ? defVal : value;
  }

  /**
   * Returns the first argument if it is not {@code null}, else the value produced by
   * the specified {@code Supplier}.
   *
   * @param <T> the type of the arguments and the return value
   * @param <E> The type of the exception that can potentially be thrown by the
   *     {@code Supplier}
   * @param value The value to return if it is not {@code null}
   * @param supplier The supplier of a default value
   * @return a non-null value
   */
  public static <T, E extends Exception> T ifNull(T value,
      ThrowingSupplier<? extends T, E> supplier) throws E {
    Check.notNull(supplier, "supplier");
    return value == null ? supplier.get() : value;
  }

  /**
   * Returns the first argument if it is not empty (as per {@link #isEmpty(Object)}),
   * else the second argument.
   *
   * @param <T> the type of the arguments and the return value
   * @param value The value to return if it is not empty
   * @param defVal The value to return if the first argument is empty
   * @return a non-empty value
   */
  public static <T> T ifEmpty(T value, T defVal) {
    return isEmpty(value) ? defVal : value;
  }

  /**
   * Returns the first argument if it is not empty (as per {@link #isEmpty(Object)}),
   * else the value produced by the specified {@code Supplier} (which may be empty as
   * well).
   *
   * @param value The value to return if not empty
   * @param supplier The supplier of a default value if {@code value} is null
   * @param <T> the input and return type
   * @param <E> The exception potentially being thrown by the supplier as it
   * @return a non-empty value
   */
  public static <T, E extends Exception> T ifEmpty(T value,
      ThrowingSupplier<? extends T, E> supplier) throws E {
    return isEmpty(value) ? Check.notNull(supplier, "supplier").ok().get() : value;
  }

  /**
   * Returns this first argument if it is among the allowed values, else
   * {@code null}.
   *
   * @param value The value to test
   * @param allowedValues The values it is allowed to have
   * @param <T> the type of the involved values
   * @return the first argument or {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <T> T nullUnless(T value, T... allowedValues) {
    Check.notNull(allowedValues, "allowedValue");
    return isElementOf(value, allowedValues) ? value : null;
  }

  /**
   * Returns {@code null} if the first argument is among the forbidden values, else
   * the first argument itself.
   *
   * @param <T> the type of the involved values
   * @param value The value to test
   * @param forbiddenValues The values it must <i>not</i> have
   * @return the first argument or {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <T> T nullIf(T value, T... forbiddenValues) {
    Check.notNull(forbiddenValues, "forbiddenValues");
    return isElementOf(value, forbiddenValues) ? null : value;
  }

  /**
   * Returns the result of passing the specified argument to the specified
   * {@code Function} if the argument is not {@code null}, else returns {@code null}.
   * For example:
   *
   * <pre>
   * String[] strs = ifNotNull("Hello World", s -> s.split(" "));
   * </pre>
   *
   * @param <T> the type of the first argument
   * @param <U> The return type
   * @param arg the value to test
   * @param then The transformation to apply to the value if it is not null
   * @return {@code value} or null
   */
  public static <T, U> U ifNotNull(T arg, Function<T, U> then) {
    return ifNotNull(arg, then, null);
  }

  /**
   * Returns the result of passing the specified argument to the specified
   * {@code Function} if the argument is not null, else a default value. For
   * example:
   *
   * <pre>
   * String[] strs = ifNotNull("Hello World", s -> s.split(" "), new String[0]);
   * </pre>
   *
   * @param <T> the type of the first value to transform
   * @param <U> The return type
   * @param arg the value to transform
   * @param then The transformation to apply to the value if it is not null
   * @param dfault A default value to return if the argument is null
   * @return the result produced by the {@code Function} or by the {@code Supplier}
   */
  public static <T, U> U ifNotNull(T arg, Function<T, U> then, U dfault) {
    return arg != null ? then.apply(arg) : dfault;
  }

  /**
   * Returns the result of passing the specified argument to the specified
   * {@code Function} if the argument is not {@link #isEmpty(Object) empty}, else
   * returns null.
   *
   * @param <T> the type of the value to transform
   * @param <U> The return type
   * @param arg the value to transform
   * @param then The function to apply to the value if it is not null
   * @return the result produced by the {@code Function} or a default value
   */
  public static <T, U> U ifNotEmpty(T arg, Function<T, U> then) {
    return ifNotEmpty(arg, then, null);
  }

  /**
   * Returns the result of passing the specified argument to the specified
   * {@code Function} if the argument is not {@link #isEmpty(Object) empty}, else a
   * default value.
   *
   * @param <T> the type of the value to transform
   * @param <U> The return type
   * @param arg the value to transform
   * @param then The function to apply to the value if it is not null
   * @param dfault A default value to return if the argument is empty
   * @return the result produced by the {@code Function} or a default value
   */
  public static <T, U> U ifNotEmpty(T arg, Function<T, U> then, U dfault) {
    return !isEmpty(arg) ? then.apply(arg) : dfault;
  }

  /**
   * Replaces a value with another value if it satisfies a certain criterion. Note
   * that the {@link CommonChecks} class defines various predicates that might be of
   * use.
   *
   * @param value the value to test and possibly return
   * @param criterion the criterion determining which value to return
   * @param replacement the replacement value
   * @param <T> the type of the values
   * @return the value determined by the criterion
   */
  public static <T> T replaceIf(T value,
      Predicate<? super T> criterion,
      T replacement) {
    return criterion.test(value) ? replacement : value;
  }

  /**
   * Replaces a value with another value if it has a certain relation <i>to</i> that
   * value. Note that the {@link CommonChecks} class defines various relations that
   * might be of use. This method can be used to apply some sort of clamping. For
   * example:
   *
   * <blockquote><pre>{@code
   * // import static nl.naturalis.check.CommonChecks.GT;
   *
   * // Prevent dates from lying in the future:
   * LocalDate myLocalDate = replaceIf(someLocalDate, GT(), LocalDate.now());
   * }</pre></blockquote>
   *
   * @param value the value to test and possibly return
   * @param relation the relation that needs to exist between the value and the
   *     replacement value in order for the replacement value to be returned
   * @param replacement the replacement value
   * @param <T> the type of the values
   * @return the value determined by the relation by the two values
   */
  public static <T> T clamp(T value, Relation<T, T> relation, T replacement) {
    return relation.exists(value, replacement) ? replacement : value;
  }

  /**
   * Replaces a value with another value if it has a certain relation to yet another
   * value. Note that the {@link CommonChecks} class defines various relations that
   * might be of use. For example:
   *
   * <blockquote><pre>{@code
   * // import static nl.naturalis.check.CommonChecks.EQ;
   *
   * // Replace green with blue:
   * Color color = replaceIf(someColor, EQ(), Color.GREEN, Color.BLUE);
   * }</pre></blockquote>
   *
   * @param value the value to test and possibly return
   * @param relation the relation that needs to exist between the value and the
   *     compare-to value in order for the replacement value to be returned
   * @param compareTo the compare-to value
   * @param replacement the value returned if the specified relation is found to
   *     exist between the value and the compare-to value
   * @param <T> the type of the values
   * @return the value determined by the relation by the two values
   */
  public static <T> T replaceIf(T value,
      Relation<T, T> relation,
      T compareTo,
      T replacement) {
    return relation.exists(value, compareTo) ? replacement : value;
  }

  /**
   * Replaces a value with another value if it satisfies a certain criterion. Note
   * that the {@link CommonChecks} class defines various predicates that might be of
   * use.
   *
   * @param value the value to test and possibly return
   * @param criterion the criterion determining which value to return
   * @param replacement the replacement value
   * @return the value determined by the criterion
   */
  public static int replaceIf(int value, IntPredicate criterion, int replacement) {
    return criterion.test(value) ? replacement : value;
  }

  /**
   * Retains a value if it has a certain relation to another value, else replaces it
   * <i>with</i> that value. Note that the {@link CommonChecks} class defines
   * various relations that might be of use. This method can be used to apply some
   * sort of clamping. For example:
   *
   * <blockquote><pre>{@code
   * // import static nl.naturalis.check.CommonChecks.gt;
   *
   * // Prevent dates from lying in the future:
   * int speed = replaceIf(someSpeed, gt(), 65);
   * }</pre></blockquote>
   *
   * @param value the value to test and possibly return
   * @param relation the relation that needs to exist between the value and the
   *     replacement value in order for the replacement value to be returned
   * @param replacement the replacement value
   * @return the value determined by the relation by the two values
   */
  public static int clamp(int value, IntRelation relation, int replacement) {
    return relation.exists(value, replacement) ? replacement : value;
  }

  /**
   * Replaces a value with another value if it has a certain relation to yet another
   * value. Note that the {@link CommonChecks} class defines various relations that
   * might be of use.
   *
   * @param value the value to test and possibly return
   * @param relation the relation that needs to exist between the value and the
   *     compare-to value in order for the replacement value to be returned
   * @param compareTo the compare-to value
   * @param replacement the value returned if the specified relation is found to
   *     exist between the value and the compare-to value
   * @return the value determined by the relation by the two values
   */
  public static int replaceIf(int value,
      IntRelation relation,
      int compareTo,
      int replacement) {
    return relation.exists(value, compareTo) ? replacement : value;
  }

  /**
   * Empty-to-null: returns {@code null} if the argument is empty (as per
   * {@link #isEmpty(Object)}), else the argument itself.
   *
   * @param <T> the type of the argument
   * @param arg the argument
   * @return the argument itself if not empty, else {@code null}
   */
  public static <T> T e2n(T arg) {
    return isEmpty(arg) ? null : arg;
  }

  /**
   * Null-to-empty: returns an empty {@code String} if the argument is null, else the
   * argument itself.
   *
   * @param arg an argument of type {@code String}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static String n2e(String arg) {
    return ifNull(arg, StringMethods.EMPTY_STRING);
  }

  /**
   * Null-to-empty: returns {@link Collections#emptyList()} if the argument is null,
   * else the argument itself.
   *
   * @param arg an argument of type {@code List}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static <T> List<T> n2e(List<T> arg) {
    return ifNull(arg, Collections.emptyList());
  }

  /**
   * Null-to-empty: returns {@link Collections#emptySet()} if the argument is null,
   * else the argument itself.
   *
   * @param arg an argument of type {@code List}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static <T> Set<T> n2e(Set<T> arg) {
    return ifNull(arg, Collections.emptySet());
  }

  /**
   * Null-to-empty: returns {@link Collections#emptyMap()} if the argument is null,
   * else the argument itself.
   *
   * @param arg an argument of type {@code List}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static <K, V> Map<K, V> n2e(Map<K, V> arg) {
    return ifNull(arg, Collections.emptyMap());
  }

  /**
   * Returns zero if the argument is null, else the argument itself.
   *
   * @param arg an argument of type {@code Integer}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static Integer n2e(Integer arg) {
    return ifNull(arg, 0);
  }

  /**
   * Returns zero if the argument is null, else the argument itself.
   *
   * @param arg an argument of type {@code Double}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static Double n2e(Double arg) {
    return ifNull(arg, 0D);
  }

  /**
   * Returns zero if the argument is null, else the argument itself.
   *
   * @param arg an argument of type {@code Long}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static Long n2e(Long arg) {
    return ifNull(arg, 0L);
  }

  /**
   * Returns zero if the argument is null, else the argument itself.
   *
   * @param arg an argument of type {@code Float}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static Float n2e(Float arg) {
    return ifNull(arg, 0F);
  }

  /**
   * Returns zero if the argument is null, else the argument itself.
   *
   * @param arg an argument of type {@code Short}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static Short n2e(Short arg) {
    return ifNull(arg, (short) 0);
  }

  /**
   * Returns zero if the argument is null, else the argument itself.
   *
   * @param arg an argument of type {@code Byte}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static Byte n2e(Byte arg) {
    return ifNull(arg, (byte) 0);
  }

  /**
   * Returns zero if the argument is null, else the argument itself.
   *
   * @param arg an argument of type {@code Byte}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static Character n2e(Character arg) {
    return ifNull(arg, '\0');
  }

  /**
   * Returns {@link Boolean#FALSE} if the argument is null, else the argument
   * itself.
   *
   * @param arg an argument of type {@code Byte}
   * @return the argument or the default value of the corresponding primitive type
   */
  public static Boolean n2e(Boolean arg) {
    return ifNull(arg, Boolean.FALSE);
  }

  private static boolean eq(Object arg0, Object arg1) {
    if (arg0 instanceof Object[] && arg1 instanceof Object[]) {
      return arraysEqual((Object[]) arg0, (Object[]) arg1);
    } else if (arg0 instanceof List && arg1 instanceof List) {
      return listsEqual((List) arg0, (List) arg1);
    } else if (arg0 instanceof Set && arg1 instanceof Set) {
      return setsEqual((Set) arg0, (Set) arg1);
    } else if (arg0 instanceof Map && arg1 instanceof Map) {
      return mapsEqual((Map) arg0, (Map) arg1);
    }
    return Objects.deepEquals(arg0, arg1);
  }

  private static boolean arraysEqual(Object[] arr0, Object[] arr1) {
    if (arr0.length == arr1.length) {
      for (int i = 0; i < arr0.length; ++i) {
        if (!e2nDeepEquals(arr0[i], arr1[i])) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private static boolean listsEqual(List list0, List list1) {
    if (list0.size() == list1.size()) {
      Iterator it0 = list0.iterator();
      Iterator it1 = list1.iterator();
      while (it0.hasNext()) {
        if (!it1.hasNext() || !e2nDeepEquals(it0.next(), it1.next())) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private static boolean setsEqual(Set set0, Set set1) {
    Set s0 = (Set) set0.stream().map(ObjectMethods::e2n).collect(toSet());
    Set s1 = (Set) set1.stream().map(ObjectMethods::e2n).collect(toSet());
    if (s0.size() != s1.size()) {
      return false;
    } else if (s0.equals(s1)) {
      return true;
    }
    for (Object obj0 : s0) {
      boolean found = false;
      for (Object obj1 : s1) {
        if (e2nDeepEquals(obj0, obj1)) {
          found = true;
          s1.remove(obj0);
          break;
        }
      }
      if (!found) {
        return false;
      }
    }
    return true;
  }

  private static boolean mapsEqual(Map map0, Map map1) {
    if (map0.size() == map1.size()) {
      for (Object k : map0.keySet()) {
        if (!map1.containsKey(k) || !e2nDeepEquals(map0.get(k), map1.get(k))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

}
