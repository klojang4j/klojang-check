package nl.naturalis.check;

import nl.naturalis.check.types.*;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

import static java.lang.invoke.MethodHandles.arrayLength;
import static nl.naturalis.check.MsgIntObjRelation.*;
import static nl.naturalis.check.MsgIntPredicate.*;
import static nl.naturalis.check.MsgIntRelation.*;
import static nl.naturalis.check.MsgObjIntRelation.*;
import static nl.naturalis.check.MsgPredicate.*;
import static nl.naturalis.check.MsgRelation.*;

/**
 * Defines various common checks on arguments, variables, object state, program
 * input, etc. The checks have short, informative error messages associated with
 * them, so you don't have to invent them yourself. It is important to realize that
 * these checks only test what they are documented to be testing. Many of them do
 * nothing but return a method reference. <b>They will not do a preliminary null
 * check</b>. If the argument might be {@code null}, always start with the
 * {@link #notNull()} check. Otherwise, a raw, unprocessed
 * {@link NullPointerException} will be thrown from the check <i>itself</i>, rather
 * than the application code.
 *
 * <blockquote>
 * <pre>{@code
 * Check.notNull(file).is(readable());
 * }</pre>
 * </blockquote>
 * <p>
 * For ease of reading, the documentation for the checks will often use the term
 * "argument" for the value being tested, but it might just as well be a local
 * variable, a field, a program argument, a system property, etc.
 *
 * @author Ayco Holleman
 */
public final class CommonChecks {

  static final Map<Object, PrefabMsgFormatter> MESSAGE_PATTERNS;
  static final Map<Object, String> NAMES;

  private static Map<Object, PrefabMsgFormatter> tmp0 = new HashMap<>(128);
  private static Map<Object, String> tmp1 = new HashMap<>(128);

  private CommonChecks() {
    throw new UnsupportedOperationException();
  }

  //////////////////////////////////////////////////////////////////////////////////
  // Predicate
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Verifies that the argument is null. Equivalent to
   * {@link Objects#isNull(Object) Objects::isNull}.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> NULL() {
    return Objects::isNull;
  }

  static {
    setMetadata(NULL(), msgNull(), "NULL");
  }

  /**
   * Verifies that the argument is not null. Equivalent to
   * {@link Objects#nonNull(Object) Objects::nonNull}.
   *
   * <p>Note that, mostly for convenience, the {@link #NULL()}, {@link #yes()} and
   * {@link #empty()} checks are the only ones that come with their negation:
   * {@code notNull()}, {@link #no()} and {@link #notEmpty()}. The other checks need
   * to be inverted using the {@code isNot(...)} and {@code notHas(...)} methods of
   * {@link ObjectCheck} and {@link IntCheck}.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> notNull() {
    return Objects::nonNull;
  }

  static {
    setMetadata(notNull(), msgNotNull(), "notNull");
  }

  /**
   * Verifies that a condition evaluates to {@code true}. Useful for validating
   * {@code boolean} properties:
   *
   * <blockquote><pre>{@code
   * Check.that(connection.isOpen()).is(yes());
   * }</pre></blockquote>
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<Boolean> yes() {
    return x -> x;
  }

  static {
    setMetadata(yes(), msgYes(), "yes");
  }

  /**
   * Verifies that a condition evaluates to {@code false}.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<Boolean> no() {
    return x -> !x;
  }

  static {
    setMetadata(no(), msgNo(), "no");
  }

  /**
   * Verifies that the argument is empty.
   *
   * <blockquote><pre>{@code
   * Check.that(list).isNot(empty());
   * }</pre></blockquote>
   *
   * <p>
   * A value is defined to be empty if any of the following applies:
   *
   * <ul>
   *   <li>it is {@code null}
   *   <li>it is an empty {@link CharSequence}
   *   <li>it is an empty {@link Collection}
   *   <li>it is an empty {@link Map}
   *   <li>it is an empty {@link Emptyable}
   *   <li>it is a zero-length array
   *   <li>it is an empty {@link Optional} <b>or</b> an {@code Optional}
   *      containing an empty value
   * </ul>
   *
   * <p>This check performs an implicit null check, so can be safely executed
   * without (or instead of) executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> empty() {
    return CheckImpls::isEmpty;
  }

  static {
    setMetadata(empty(), msgEmpty(), "empty");
  }

  /**
   * Verifies that the argument is not empty. More precisely: it verifies the
   * negation of the {@link #empty()} test. If your intention is to verify
   * non-emptiness, using this test affirmatively - {@code is(notEmpty())} - might be
   * slightly more performant than using the {@code empty()} check negatively -
   * {@code isNot(empty())}, at least while the JVM is warming up.
   *
   * <p>This check performs an implicit null check, so can be safely executed
   * without (or instead of) executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> notEmpty() {
    return CheckImpls::isNotEmpty;
  }

  static {
    setMetadata(notEmpty(), msgNotEmpty(), "notEmpty");
  }

  /**
   * Verifies that the argument is not {@code null} and, if it is an array,
   * {@code Collection} or {@code Map}, that it does not contain any {@code null}
   * values. It may still be an <i>empty</i> array, {@code Collection} or
   * {@code Map}, however. For maps, both keys and values are tested for
   * {@code null}.
   *
   * <p>This check performs an implicit null check, so can be safely executed
   * without (or instead of) executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> deepNotNull() {
    return CheckImpls::isDeepNotNull;
  }

  static {
    setMetadata(deepNotNull(), msgDeepNotNull(), "deepNotNull");
  }

  /**
   * Verifies that the argument is recursively non-empty. A value is defined to be
   * deep-not-empty if any of the following applies:
   *
   * <ul>
   *   <li>it is a non-empty {@link CharSequence}
   *   <li>it is a non-empty {@link Collection} containing only <i>deep-not-empty</i>
   *       elements
   *   <li>it is a non-empty {@link Map} containing only <i>deep-not-empty</i> keys and
   *       values
   *   <li>it is a <i>deep-not-empty</i> {@link Emptyable}
   *   <li>it is a non-zero-length {@code Object[]} containing only <i>deep-not-empty</i>
   *       elements
   *   <li>it is a non-zero-length array of primitive values
   *   <li>it is a non-empty {@link Optional} containing a <i>deep-not-empty</i> value
   *   <li>it is a non-null object of any other type
   * </ul>
   *
   * <p>This check performs an implicit null check, so can be safely executed
   * without (or instead of) executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> deepNotEmpty() {
    return CheckImpls::isDeepNotEmpty;
  }

  static {
    setMetadata(deepNotEmpty(), msgDeepNotEmpty(), "deepNotEmpty");
  }

  /**
   * Verifies that the argument is {@code null} or contains whitespace only. Probably
   * more useful when called from an {@code isNot} method.
   *
   * <p>This check performs an implicit null check, so can be safely executed
   * without (or instead of) executing the {@link #notNull()} check first.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<String> blank() {
    return s -> s == null || s.isBlank();
  }

  static {
    setMetadata(blank(), msgBlank(), "blank");
  }

  /**
   * Verifies that a string consists of digits only, by implication contains no '+'
   * or '-' sign, contains no leading zeros, and can be parsed into an {@code int}
   * (by implication non-negative).
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<String> plainInt() {
    return StringCheckImpls::isPlainInt;
  }

  /**
   * Verifies that a string consists of digits only, by implication contains no '+'
   * or '-' sign, contains no leading zeros, and can be parsed into a {@code short}
   * (by implication non-negative). Useful, for example, for parsing TCP port
   * numbers.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<String> plainShort() {
    return StringCheckImpls::isPlainShort;
  }

  /**
   * Verifies that the argument is an array or, if it is a {@code Class} object, that
   * it is an array type.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> array() {
    return x -> x instanceof Class<?> c ? c.isArray() : x.getClass().isArray();
  }

  static {
    setMetadata(array(), msgArray(), "array");
  }

  /**
   * Verifies that a file is an existing, regular file. Equivalent to
   * {@link File#isFile() File::isFile}.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> file() {
    return File::isFile;
  }

  static {
    setMetadata(file(), msgFile(), "file");
  }

  /**
   * Verifies a file is an existing directory. Equivalent to
   * {@link File#isDirectory() File::isDirectory}.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> directory() {
    return File::isDirectory;
  }

  static {
    setMetadata(directory(), msgDirectory(), "directory");
  }

  /**
   * Verifies that a file is present on the file system. Equivalent to
   * {@link File#exists() File::exists}.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> fileExists() {
    return File::exists;
  }

  static {
    setMetadata(fileExists(), msgFileExists(), "fileExists");
  }

  /**
   * Verifies that a file is readable. Implies that the file exists. Equivalent to
   * {@link File#canRead() File::canRead}.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> readable() {
    return File::canRead;
  }

  static {
    setMetadata(readable(), msgReadable(), "readable");
  }

  /**
   * Verifies that a file is writable. Implies that the file exists. Equivalent to
   * {@link File#canWrite() File::canWrite}.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> writable() {
    return File::canWrite;
  }

  static {
    setMetadata(writable(), msgWritable(), "writable");
  }

  /**
   * Verifies that an {@code Optional} contains a value. Note that this check differs
   * from the {@link #empty()} check in that it only verifies that {@code Optional}
   * is not empty. The {@code empty()} check (in its negation) additionally requires
   * that the value it contains is itself non-empty.
   *
   * @param <T> the type of the value contained in the {@code Optional}
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<Optional<T>> present() {
    return Optional::isPresent;
  }

  static {
    setMetadata(present(), msgPresent(), "present");
  }

  /**
   * Verifies that a {@linkplain Result result} is available. Note that this check
   * differs from the {@link #empty()} check in that it only verifies that
   * {@code Result} contains a value. The {@code empty()} check (in its negation)
   * additionally requires that the value it contains is itself non-empty.
   *
   * @param <T> the type of the value contained in the {@code Result}
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<Result<T>> available() {
    return Result::isAvailable;
  }

  static {
    setMetadata(available(), msgAvailable(), "available");
  }

  //////////////////////////////////////////////////////////////////////////////////
  // IntPredicate
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Verifies that the argument is an even integer.
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate even() {
    return x -> x % 2 == 0;
  }

  static {
    setMetadata(even(), msgEven(), "even");
  }

  /**
   * Verifies that the argument is an odd integer.
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate odd() {
    return x -> x % 2 == 1;
  }

  static {
    setMetadata(odd(), msgOdd(), "odd");
  }

  /**
   * Verifies that the argument is a positive integer.
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate positive() {
    return x -> x > 0;
  }

  static {
    setMetadata(positive(), msgPositive(), "positive");
  }

  /**
   * Verifies that the argument is a negative integer.
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate negative() {
    return x -> x < 0;
  }

  static {
    setMetadata(negative(), msgNegative(), "negative");
  }

  /**
   * Verifies that the argument is zero (0).
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate zero() {
    return x -> x == 0;
  }

  static {
    setMetadata(zero(), msgZero(), "zero");
  }

  //////////////////////////////////////////////////////////////////////////////////
  // IntRelation
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Verifies that the argument equals the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation eq() {
    return (x, y) -> x == y;
  }

  static {
    setMetadata(eq(), msgEq(), "eq");
  }

  /**
   * Verifies that the argument does not equal the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation ne() {
    return (x, y) -> x != y;
  }

  static {
    setMetadata(ne(), msgNe(), "ne");
  }

  /**
   * Verifies that the argument is greater than the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation gt() {
    return (x, y) -> x > y;
  }

  static {
    setMetadata(gt(), msgGt(), "gt");
  }

  /**
   * Verifies that the argument is greater than or equal to the specified {@code int}
   * value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation gte() {
    return (x, y) -> x >= y;
  }

  static {
    setMetadata(gte(), msgGte(), "gte");
  }

  /**
   * Verifies that the argument is less than the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation lt() {
    return (x, y) -> x < y;
  }

  static {
    setMetadata(lt(), msgLt(), "lt");
  }

  /**
   * Verifies that the argument is less than or equal to the specified {@code int}
   * value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation lte() {
    return (x, y) -> x <= y;
  }

  static {
    setMetadata(lte(), msgLte(), "lte");
  }

  /**
   * Verifies that the argument is a multiple of the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation multipleOf() {
    return (x, y) -> x % y == 0;
  }

  static {
    setMetadata(multipleOf(), msgMultipleOf(), "multipleOf");
  }

  //////////////////////////////////////////////////////////////////////////////////
  // Relation
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Verifies that a value equals another value. Equivalent to
   * {@link Objects#equals(Object) Objects::equals}.
   *
   * @param <S> the type of the subject of the relationship (which is the value
   *     being tested)
   * @param <O> the type of the object of the relationship
   * @return a function implementing the test described above
   */
  public static <S, O> Relation<S, O> EQ() {
    return Objects::equals;
  }

  static {
    setMetadata(EQ(), msgEQ(), "EQ");
  }

  /**
   * Verifies that the argument is greater than another value.
   *
   * @param <T> the type of the values being compared
   * @return a function implementing the test described above
   * @see CommonProperties#unbox()
   * @see #gt()
   */
  public static <T extends Comparable<T>> Comparison<T> GT() {
    return (x, y) -> x.compareTo(y) > 0;
  }

  static {
    setMetadata(GT(), msgGT(), "GT");
  }

  /**
   * Verifies that the argument is less than another value.
   *
   * @param <T> the type of the values being compared
   * @return a function implementing the test described above
   * @see CommonProperties#unbox()
   * @see #lt()
   */
  public static <T extends Comparable<T>> Comparison<T> LT() {
    return (x, y) -> x.compareTo(y) < 0;
  }

  static {
    setMetadata(LT(), msgLT(), "LT");
  }

  /**
   * Verifies that the argument is greater than or equal to another value.
   *
   * @param <T> the type of the values being compared
   * @return a function implementing the test described above
   * @see CommonProperties#unbox()
   * @see #gte()
   */
  public static <T extends Comparable<T>> Comparison<T> GTE() {
    return (x, y) -> x.compareTo(y) >= 0;
  }

  static {
    setMetadata(GTE(), msgGTE(), "GTE");
  }

  /**
   * Verifies that the argument is less than or equal to another value.
   *
   * @param <T> the type of the values being compared
   * @return a function implementing the test described above
   * @see CommonProperties#unbox()
   * @see #lte()
   */
  public static <T extends Comparable<T>> Comparison<T> LTE() {
    return (x, y) -> x.compareTo(y) <= 0;
  }

  static {
    setMetadata(LTE(), msgLTE(), "LTE");
  }

  /**
   * Verifies that a value references the same object as another value.
   *
   * @param <S> the type of the subject of the relationship (which is the value
   *     being tested) (the subject of the {@code Relation})
   * @param <O> the type of the value to compare it with (the object of the
   *     {@code Relation})
   * @return a function implementing the test described above
   */
  public static <S, O> Relation<S, O> sameAs() {
    return (x, y) -> x == y;
  }

  static {
    setMetadata(sameAs(), msgSameAs(), "sameAs");
  }

  /**
   * Verifies that the argument is either null or equals a particular value.
   *
   * @param <T> the type of the subject of the relationship (which is the value
   *     being tested)
   * @return a function implementing the test described above
   */
  public static <T> Comparison<T> nullOr() {
    return (x, y) -> x == null || x.equals(y);
  }

  static {
    setMetadata(nullOr(), msgNullOr(), "nullOr");
  }

  /**
   * Verifies that the argument is an instance of a particular class or interface.
   *
   * @param <S> the type of the subject of the relation (which is the value being
   *     tested)
   * @param <O> the type of the object of the relation
   * @return a function implementing the test described above
   */
  public static <S, O extends Class<?>> Relation<S, O> instanceOf() {
    return (x, y) -> y.isInstance(x);
  }

  static {
    setMetadata(instanceOf(), msgInstanceOf(), "instanceOf");
  }

  /**
   * Verifies that the argument is a supertype of another type. In other words, that
   * the other type extends, implements, or is equal to the argument.
   *
   * @param <S> the type of the subject's class
   * @param <O> the type of the object's class
   * @return a function that implements the test described above
   */
  public static <S, O> Relation<Class<S>, Class<O>> supertypeOf() {
    return Class::isAssignableFrom;
  }

  static {
    setMetadata(supertypeOf(), msgSupertypeOf(), "supertypeOf");
  }

  /**
   * Verifies that the argument is a subtype of another type. In other words, that it
   * extends or implements the other type.
   *
   * @param <S> the type of the subject's class
   * @param <O> the type of the object's class
   * @return a function that implements the test described above
   */
  public static <S, O> Relation<Class<S>, Class<O>> subtypeOf() {
    return (x, y) -> y.isAssignableFrom(x);
  }

  static {
    setMetadata(subtypeOf(), msgSubtypeOf(), "subtypeOf");
  }

  /**
   * Verifies that a collection contains a particular value. Equivalent to
   * {@link Collection#contains(Object) Collection::contains}.
   *
   * @param <O> the type of the elements in the {@code Collection}
   * @param <S> the type of the collection
   * @return a function implementing the test described above
   */
  public static <O, S extends Collection<? super O>> Relation<S, O> contains() {
    return Collection::contains;
  }

  static {
    setMetadata(contains(), msgContains(), "contains");
  }

  /**
   * Verifies that a map contains a particular key. Equivalent to
   * {@link Map#containsKey(Object) Map::containsKey}.
   *
   * @param <O> the type of the keys within the map
   * @param <S> the Type of the {@code Map}
   * @return a function implementing the test described above
   */
  public static <O, S extends Map<? super O, ?>> Relation<S, O> containsKey() {
    return Map::containsKey;
  }

  static {
    setMetadata(containsKey(), msgContainsKey(), "containsKey");
  }

  /**
   * Verifies that a map contains a particular value. Equivalent to
   * {@link Map#containsValue(Object) Map::containsValue}.
   *
   * @param <O> the type of the values within the map
   * @param <S> the Type of the {@code Map}
   * @return a function implementing the test described above
   */
  public static <O, S extends Map<?, ? super O>> Relation<S, O> containsValue() {
    return Map::containsValue;
  }

  static {
    setMetadata(containsValue(), msgContainingValue(), "containsValue");
  }

  /**
   * Verifies that the argument is an element of a collection.
   *
   * @param <S> the type of the argument
   * @param <O> the type of the {@code Collection}
   * @return a function implementing the test described above
   */
  public static <S, O extends Collection<? super S>> Relation<S, O> in() {
    return (x, y) -> y.contains(x);
  }

  static {
    setMetadata(in(), msgIn(), "in");
  }

  /**
   * Verifies the presence of a key within a map.
   *
   * @param <S> the type of the keys within the map
   * @param <O> the Type of the {@code Map}
   * @return a function implementing the test described above
   */
  public static <S, O extends Map<? super S, ?>> Relation<S, O> keyIn() {
    return (x, y) -> y.containsKey(x);
  }

  static {
    setMetadata(keyIn(), msgKeyIn(), "keyIn");
  }

  /**
   * Verifies the presence of a value within a map.
   *
   * @param <S> the type of the keys within the map
   * @param <O> the Type of the {@code Map}
   * @return a function implementing the test described above
   */
  public static <S, O extends Map<?, ? super S>> Relation<S, O> valueIn() {
    return (x, y) -> y.containsValue(x);
  }

  static {
    setMetadata(valueIn(), msgValueIn(), "valueIn");
  }

  /**
   * Verifies that the argument is an element of an array.
   *
   * @param <S> the type of the subject of the relationship (which is the value
   *     being tested)
   * @param <O> the component type of the array
   * @return a function implementing the test described above
   */
  public static <O, S extends O> Relation<S, O[]> inArray() {
    return CheckImpls::inArray;
  }

  static {
    setMetadata(inArray(), msgIn(), "inArray"); // Recycle message
  }

  /**
   * Verifies that a {@code Collection} argument contains all the elements of the
   * specified collection. Equivalent to
   * {@link Collection#containsAll(Collection) Collection::containsAll}.
   *
   * <blockquote><pre>{@code
   * Check.that(List.of(1,2,3)).is(enclosing(), Set.of(1,2); // true
   * Check.that(List.of(1,2)).is(enclosing(), Set.of(1,2,3); // false
   * }</pre></blockquote>
   *
   * @param <E> The type of the elements in the {@code Collection}
   * @param <C0> The type of the argument (the subject of the {@code Relation})
   * @param <C1> The type of the object of the {@code Relation}
   * @return a function implementing the test described above
   */
  public static <E, C0 extends Collection<? super E>, C1 extends Collection<E>>
  Relation<C0, C1> enclosing() {
    return Collection::containsAll;
  }

  static {
    setMetadata(enclosing(), msgEnclosing(), "enclosing");
  }

  /**
   * Verifies that a {@code Collection} argument is a subset or sublist of another
   * {@code Collection}.
   *
   * <blockquote>
   *
   * <pre>{@code
   * Check.that(List.of(1,2,3)).is(enclosedBy(), Set.of(1,2); // false
   * Check.that(List.of(1,2)).is(enclosedBy(), Set.of(1,2,3); // true
   * }</pre>
   *
   * </blockquote>
   *
   * @param <E> The type of the elements in the {@code Collection}
   * @param <C0> The type of the argument (the subject of the {@code Relation})
   * @param <C1> The type of the object of the {@code Relation}
   * @return a function implementing the test described above
   */
  public static <E, C0 extends Collection<E>, C1 extends Collection<? super E>>
  Relation<C0, C1> enclosedBy() {
    return (x, y) -> y.containsAll(x);
  }

  static {
    setMetadata(enclosedBy(), msgEnclosedBy(), "enclosedBy");
  }

  /**
   * Verifies that the argument contains the specified substring. Equivalent to
   * {@link String#contains(CharSequence) String::contains}.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, CharSequence> hasSubstring() {
    return String::contains;
  }

  static {
    setMetadata(hasSubstring(), msgHasSubstring(), "hasSubstring");
  }

  /**
   * Verifies that the argument is a substring of the specified string.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, String> substringOf() {
    return (x, y) -> y.contains(x);
  }

  static {
    setMetadata(substringOf(), msgSubstringOf(), "substringOf");
  }

  /**
   * Verifies that a value equals ignoring case the specified string.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, String> equalsIgnoreCase() {
    return String::equalsIgnoreCase;
  }

  static {
    setMetadata(equalsIgnoreCase(), msgEqualsIgnoreCase(), "equalsIgnoreCase");
  }

  /**
   * Verifies that the argument starts with the specified substring. Equivalent to
   * {@link String#startsWith(String) String::startsWith}.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, String> startsWith() {
    return String::startsWith;
  }

  static {
    setMetadata(startsWith(), msgStartsWith(), "startsWith");
  }

  /**
   * Verifies that the argument ends with the specified substring. Equivalent to
   * {@link String#endsWith(String) String::endsWith}.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, String> endsWith() {
    return String::endsWith;
  }

  static {
    setMetadata(endsWith(), msgEndsWith(), "endsWith");
  }

  private static final Map<Class<?>, Predicate<String>> parsables = Map.of(
      Integer.class, StringCheckImpls::isInt,
      Long.class, StringCheckImpls::isLong,
      Short.class, StringCheckImpls::isShort,
      Byte.class, StringCheckImpls::isByte,
      Double.class, StringCheckImpls::isDouble,
      Float.class, StringCheckImpls::isFloat
  );

  /**
   * Verifies that a string can be parsed into a {@code Number} of the specified
   * type, without loss of information. The provided type must be one of the "basic"
   * {@code Number} types: {@code Integer}, {@code Double}, {@code Float},
   * {@code Long}, {@code Short}, {@code Byte}. Any other type will result in an
   * {@link InvalidCheckException}. As for the integral types: the string to be
   * parsed may have a fractional part as long as it consists of zeros only;
   * scientific notation is allowed, too, as long as the fractional part effectively
   * consists of zeros only.
   *
   * @param <T> the type of the {@code Number} into which to parse the string
   * @return a function implementing the test described above
   * @see #plainInt()
   * @see #plainShort()
   */
  public static <T extends Number> Relation<String, Class<T>> parsableAs() {
    return (x, y) -> {
      Predicate<String> p = parsables.get(y);
      if (p != null) {
        return p.test(x);
      }
      throw new InvalidCheckException("unsupported number type: " + y);
    };
  }

  static {
    setMetadata(parsableAs(), msgParsableAs(), "parsableAs");
  }

  //////////////////////////////////////////////////////////////////////////////////
  // IntObjRelation
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Verifies that the argument is a valid index into the specified array,
   * {@code List} or {@code String}. No preliminary check is done to ensure the
   * provided object actually is an array, {@code List} or {@code String}. An
   * {@link InvalidCheckException} is thrown if it is not. Execute the
   * {@link #instanceOf()} or {@link #array()} check first, if necessary.
   *
   * @param <T> the type of the object of the {@code IntObjRelation} - must be a
   *     {@code String}, {@code List} or array
   * @return a function implementing the test described above
   */
  public static <T> IntObjRelation<T> indexOf() {
    return CheckImpls::isIndexOf;
  }

  static {
    setMetadata(indexOf(), msgIndexOf(), "indexOf");
  }

  /**
   * Verifies that a value can be used as a "from" or "to" index in operations like
   * (but not limited to)
   * {@link Arrays#copyOfRange(int[], int, int) Arrays.copyOfRange},
   * {@link String#substring(int, int) String.substring} and
   * {@link List#subList(int, int) List.subList}. These operations allow both the
   * "from" index and the "to" index to be equal to the length of the array, string
   * or list. No preliminary check is done to ensure the provided object actually is
   * an array, {@code List} or {@code String}. An {@link InvalidCheckException} is
   * thrown if it is not. Execute the {@link #instanceOf()} or {@link #array()} check
   * first, if necessary.
   *
   * @param <T> the type of the object of the {@code IntObjRelation} - must be a
   *     {@code String}, {@code List} or array
   * @return a function implementing the test described above
   * @see Check#fromTo(Object[], int, int)
   */
  public static <T> IntObjRelation<T> indexInclusiveOf() {
    return CheckImpls::isIndexInclusiveOf;
  }

  static {
    setMetadata(indexInclusiveOf(), msgIndexInclusiveInto(), "indexInclusiveInto");
  }

  /**
   * Verifies that the argument is greater than, or equal to the first integer in the
   * provided {@code int} array, and less than the second.
   *
   * @return a function implementing the test described above
   */
  public static IntObjRelation<int[]> inRange() {
    return (x, y) -> x >= y[0] && x < y[1];
  }

  static {
    setMetadata(inRange(), msgInRange(), "inRange");
  }

  /**
   * Verifies that the argument is greater than, or equal to the first integer in the
   * provided {@code int} array, and less than, or equal to the second (like the SQL
   * BETWEEN operator).
   *
   * @return a function implementing the test described above
   */
  public static IntObjRelation<int[]> between() {
    return (x, y) -> x >= y[0] && x <= y[1];
  }

  static {
    setMetadata(between(), msgBetween(), "between");
  }

  /**
   * Verifies that the argument is present in the specified {@code int} array.
   *
   * @return a function implementing the test described above
   */
  public static IntObjRelation<int[]> inIntArray() {
    return (x, y) -> {
      for (int i : y) {
        if (x == i) {
          return true;
        }
      }
      return false;
    };
  }

  static {
    setMetadata(inIntArray(), msgIn(), "inIntArray"); // Recycle message
  }

  /* ++++++++++++++ END OF CHECKS ++++++++++++++ */

  static {
    MESSAGE_PATTERNS = Map.copyOf(tmp0);
    NAMES = Map.copyOf(tmp1);
    tmp0 = null;
    tmp1 = null;
  }

  private static void setMetadata(Object test,
      PrefabMsgFormatter message,
      String name) {
    tmp0.put(test, message);
    tmp1.put(test, name);
  }

  private static <T> T notApplicable(String check, Object arg) {
    String msg = String.format("\"%s\" not applicable to %s", check, arg.getClass());
    throw new InvalidCheckException(msg);
  }

}
