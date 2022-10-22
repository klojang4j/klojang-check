package org.klojang.check;

import org.klojang.check.aux.Emptyable;
import org.klojang.check.aux.Result;
import org.klojang.check.relation.*;
import org.klojang.check.x.CheckImpls;
import org.klojang.check.x.StringCheckImpls;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.klojang.check.x.Misc.typeNotSupported;
import static org.klojang.check.x.StringCheckImpls.NUMERICALS;
import static org.klojang.check.x.StringCheckImpls.PARSABLES;

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

  /**
   * Verifies that the argument is not null. Equivalent to
   * {@link Objects#nonNull(Object) Objects::nonNull}.
   *
   * <p>Note that, mostly for convenience, {@link #NULL()}, {@link #yes()} and
   * {@link #empty()} are the only checks that come with their negation:
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

  /**
   * Verifies that a condition evaluates to {@code false}.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<Boolean> no() {
    return x -> !x;
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
   *   <li>it is an empty {@link File}
   *   <li>it is a zero-length array
   *   <li>it is an empty {@link Optional} <b>or</b> an {@code Optional}
   *      containing an empty value
   * </ul>
   *
   * <p>This check (implicitly) performs a null check and can be safely executed
   * without or instead of executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> empty() {
    return CheckImpls::isEmpty;
  }

  /**
   * Verifies that the argument is not empty. More precisely: it verifies the
   * negation of the {@link #empty()} test.
   *
   * <p>This check (implicitly) performs a null check and can be safely executed
   * without or instead of executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> notEmpty() {
    return CheckImpls::isNotEmpty;
  }

  /**
   * Verifies that the argument is not {@code null} and, if it is an array,
   * collection or map, that it does not contain any {@code null} values. It could
   * still be a zero-length array, collection or map, however. For maps, both keys
   * and values are tested for {@code null}.
   *
   * <p>This check (implicitly) performs a null check and can be safely executed
   * without or instead of executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> deepNotNull() {
    return CheckImpls::isDeepNotNull;
  }

  /**
   * Verifies that the argument is recursively non-empty. A value is defined to be
   * deep-not-empty if any of the following applies:
   *
   * <ul>
   *   <li>it is a non-empty {@link CharSequence}
   *   <li>it is a non-empty {@link Collection} containing only
   *      <i>deep-not-empty</i> elements
   *   <li>it is a non-empty {@link Map} containing only <i>deep-not-empty</i> keys
   *      and values
   *   <li>it is a deep-not-empty {@link Emptyable}
   *   <li>it is a non-zero-length {@code Object[]} containing only
   *      <i>deep-not-empty</i> elements
   *   <li>it is a non-zero-length array of primitive values
   *   <li>it is a non-empty {@link Optional} containing a <i>deep-not-empty</i>
   *      value
   *   <li>it is a {@link File} containing at least one non-whitespace character.
   *      Consequently, this check could be expensive if the argument is a large
   *      {@code File}. Also note that this check will not verify that the file
   *      exists in the first place. If in doubt, execute the {@link #regularFile()} check
   *      first.
   *   <li>it is a non-null object of any other type
   * </ul>
   *
   * <p>This check (implicitly) performs a null check and can be safely executed
   * without or instead of executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> deepNotEmpty() {
    return CheckImpls::isDeepNotEmpty;
  }

  /**
   * Verifies that the argument is {@code null} or contains whitespace only. Probably
   * more useful when called from an {@code isNot} method.
   *
   * <p>This check (implicitly) performs a null check and can be safely executed
   * without or instead of executing the {@link #notNull()} check first.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<String> blank() {
    return s -> s == null || s.isBlank();
  }

  /**
   * Verifies that a string consists of digits only and (by implication) contains no
   * '+' or '-' sign, no leading zeros, and can be parsed into an integer (by
   * implication non-negative).
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<String> plainInt() {
    return StringCheckImpls::isPlainInt;
  }

  /**
   * Verifies that a string consists of digits only and (by implication) contains no
   * '+' or '-' sign, no leading zeros, and can be parsed into a half-precision
   * integer (by implication non-negative). Useful, for example, for parsing TCP port
   * numbers.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<String> plainShort() {
    return StringCheckImpls::isPlainShort;
  }

  /**
   * Returns {@code true} if the argument is an array or array <i>type</i>.
   *
   * <blockquote><pre>{@code
   * Object obj = new int[] {1, 2, 3, 4, 5};
   * Check.that(obj).is(array());             // OK
   * Check.that(obj.getClass()).is(array());  // OK
   * obj = "foo";
   * Check.that(obj).is(array());             // IllegalArgumentException
   * }</pre></blockquote>
   *
   * @param <T> the type of the argument
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<T> array() {
    return x -> x instanceof Class<?> c ? c.isArray() : x.getClass().isArray();
  }

  /**
   * Verifies that the argument is an existing, regular file.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> regularFile() {
    return f -> Files.isRegularFile(f.toPath());
  }

  /**
   * Verifies that the argument is an existing directory.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> directory() {
    return f -> Files.isDirectory(f.toPath());
  }

  /**
   * Verifies that the argument is a symbolic link.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> symlink() {
    return f -> Files.isSymbolicLink(f.toPath());
  }

  /**
   * Verifies that the specified file is present on the file system. Equivalent to
   * {@link File#exists() File::exists}.
   *
   * <blockquote><pre>{@code
   * // import static org.klojang.CommonChecks.fileExists;
   * // import static org.klojang.CommonExceptions.notFound;
   * Check.that(file).is(fileExists(), fileNotFound(file));
   * }</pre></blockquote>
   *
   * @return a function implementing the test described above
   * @see CommonExceptions#fileNotFound(File)
   */
  public static ComposablePredicate<File> fileExists() {
    return File::exists;
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

  /**
   * Verifies that a file is writable. Implies that the file exists. Equivalent to
   * {@link File#canWrite() File::canWrite}.
   *
   * @return a function implementing the test described above
   */
  public static ComposablePredicate<File> writable() {
    return File::canWrite;
  }

  /**
   * Verifies that an {@code Optional} contains a value. Note that this check differs
   * from the {@link #empty()} check in that it only verifies that the
   * {@code Optional} is not empty. The {@code empty()} check (in its negation)
   * additionally requires that the value it contains is itself non-empty.
   *
   * @param <T> the type of the value contained in the {@code Optional}
   * @return a function implementing the test described above
   */
  public static <T> ComposablePredicate<Optional<T>> present() {
    return Optional::isPresent;
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

  /**
   * Verifies that the argument is an odd integer.
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate odd() {
    return x -> x % 2 == 1;
  }

  /**
   * Verifies that the argument is a positive integer.
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate positive() {
    return x -> x > 0;
  }

  /**
   * Verifies that the argument is a negative integer.
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate negative() {
    return x -> x < 0;
  }

  /**
   * Verifies that the argument is zero (0).
   *
   * @return a function implementing the test described above
   */
  public static ComposableIntPredicate zero() {
    return x -> x == 0;
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

  /**
   * Verifies that the argument does not equal the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation ne() {
    return (x, y) -> x != y;
  }

  /**
   * Verifies that the argument is greater than the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation gt() {
    return (x, y) -> x > y;
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

  /**
   * Verifies that the argument is less than the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation lt() {
    return (x, y) -> x < y;
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

  /**
   * Verifies that the argument is a multiple of the specified {@code int} value.
   *
   * @return a function implementing the test described above
   */
  public static IntRelation multipleOf() {
    return (x, y) -> x % y == 0;
  }

  //////////////////////////////////////////////////////////////////////////////////
  // Relation
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Verifies that the argument equals some value. Equivalent to
   * {@link Object#equals(Object) Object::equals}. Note that this method is
   * <i>not</i> equivalent to {@link Objects#equals(Object, Object) Objects::equals}
   * and is therefore not null-safe. Execute a {@linkplain #notNull() null check}
   * first, if necessary.
   *
   * @param <S> the type of the subject of the relationship (which is the value
   *     being tested)
   * @param <O> the type of the object of the relationship
   * @return a function implementing the test described above
   */
  public static <S, O> Relation<S, O> EQ() {
    return Object::equals;
  }

  /**
   * Verifies that the argument equals some value. Equivalent to
   * {@link Object#equals(Object) Object::equals}. Use this check instead of
   * {@link #EQ()} if you need to be sure you are comparing objects of the same
   * type.
   *
   * @param <T> the type of the objects being compared
   * @return a function implementing the test described above
   */
  public static <T> Comparison<T> equalTo() {
    return Object::equals;
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

  /**
   * Verifies that the argument is either null or equals a particular value.
   *
   * <p>This check (implicitly) performs a null check and can be safely executed
   * without or instead of executing the {@link #notNull()} check first.
   *
   * @param <T> the type of the subject of the relationship (which is the value
   *     being tested)
   * @return a function implementing the test described above
   */
  public static <T> Comparison<T> nullOr() {
    return (x, y) -> x == null || x.equals(y);
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

  /**
   * Verifies that the argument contains the specified substring. Equivalent to
   * {@link String#contains(CharSequence) String::contains}.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, CharSequence> hasSubstring() {
    return String::contains;
  }

  /**
   * Verifies that the argument is a substring of the specified string.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, String> substringOf() {
    return (x, y) -> y.contains(x);
  }

  /**
   * Verifies that a value equals ignoring case the specified string.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, String> equalsIgnoreCase() {
    return String::equalsIgnoreCase;
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

  /**
   * Verifies that the argument ends with the specified substring. Equivalent to
   * {@link String#endsWith(String) String::endsWith}.
   *
   * @return a function implementing the test described above
   */
  public static Relation<String, String> endsWith() {
    return String::endsWith;
  }

  /**
   * Verifies that the argument matches the specified pattern (that is, the pattern
   * fully describes the string).
   *
   * @return a function implementing the test described above
   * @see #describedBy()
   */
  public static Relation<String, Pattern> hasPattern() {
    return (string, pattern) -> pattern.matcher(string).matches();
  }

  /**
   * Verifies that the argument contains the specified pattern (that is, the pattern
   * can be found somewhere in the string).
   *
   * @return a function implementing the test described above
   * @see #matching()
   */
  public static Relation<String, Pattern> containsPattern() {
    return (string, pattern) -> pattern.matcher(string).find();
  }

  /**
   * Verifies that the argument matches the specified pattern (that is, the pattern
   * fully describes the string). The subject (or "left hand side") of this
   * {@code Relation} is the string to match; the object (or "right hand side") of
   * the {@code Relation} is the regular expression to be compiled into a
   * {@link Pattern}.
   *
   * <blockquote><pre>{@code
   * Check.that("abcd123").is(matching(), "\\d{3}"); // yes
   * Check.that("abcd123").is(matching(), "\\d{4}"); // no
   * }</pre></blockquote>
   *
   * @return a function implementing the test described above
   */
  public static Comparison<String> describedBy() {
    return (string, pattern) ->
        hasPattern().exists(string, Pattern.compile(pattern));
  }

  /**
   * Verifies that the argument contains the specified pattern (that is, the pattern
   * can be found somewhere in the string). The subject (or "left hand side") of this
   * {@code Relation} is the string to match; the object (or "right hand side") of
   * the {@code Relation} is the regular expression to be compiled into a
   * {@link Pattern}.
   *
   * <blockquote><pre>{@code
   * Check.that("abcd123").is(describedBy(), "\\d{3}"); // yes
   * Check.that("abcd123").is(describedBy(), "\\d{4}"); // no
   * }</pre></blockquote>
   *
   * @return a function implementing the test described above
   */
  public static Comparison<String> matching() {
    return (string, pattern) ->
        containsPattern().exists(string, Pattern.compile(pattern));
  }

  /**
   * Verifies that a string can be parsed into a number of the specified type without
   * loss of information. The provided type must be one of the <i>primitive</i>
   * number types: {@code long}, {@code int}, {@code short}, {@code byte},
   * {@code double} or {@code float}. Specifying a wrapper type (e.g.
   * {@code Integer}) will result in an {@link CorruptCheckException}.
   *
   * @param <T> the type of the number into which to parse the string
   * @return a function implementing the test described above
   * @see #parsableAs
   * @see #plainInt()
   * @see #plainShort()
   */
  public static <T extends Number> Relation<String, Class<T>> numerical() {
    return (x, y) -> {
      Predicate<String> p = NUMERICALS.get(y);
      if (p != null) {
        return p.test(x);
      }
      throw typeNotSupported(y);
    };
  }

  /**
   * Verifies that a string can be parsed into a {@code Number} of the specified type
   * without loss of information. The provided type must be one of the
   * <i>primitive</i> number types: {@code long}, {@code int}, {@code short},
   * {@code byte}, {@code double} or {@code float}. Specifying a wrapper type (e.g.
   * {@code Integer}) will result in an {@link CorruptCheckException}. Contrary to
   * the {@link #numerical()}, this check allows the string to contain a fractional
   * part even if the target type is an integral type (like {@code int}), as long as
   * it consists of zeros only. Scientific notation is allowed, too, as long as the
   * effective fractional part consists of zeros only. For {@code Double} and
   * {@code Float} there is no difference between the two checks.
   *
   * @param <T> the type of the number into which to parse the string
   * @return a function implementing the test described above
   * @see #numerical()
   * @see #plainInt()
   * @see #plainShort()
   */
  public static <T extends Number> Relation<String, Class<T>> parsableAs() {
    return (x, y) -> {
      Predicate<String> p = PARSABLES.get(y);
      if (p != null) {
        return p.test(x);
      }
      throw typeNotSupported(y);
    };
  }

  //////////////////////////////////////////////////////////////////////////////////
  // IntObjRelation
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Verifies that the argument is a valid index into the specified array,
   * {@code List} or {@code String}. No preliminary check is done to ensure the
   * provided object actually is an array, {@code List} or {@code String}. An
   * {@link CorruptCheckException} is thrown if it is not. Execute the
   * {@link #instanceOf()} or {@link #array()} check first, if necessary.
   *
   * @param <T> the type of the object of the {@code IntObjRelation} - must be a
   *     {@code String}, {@code List} or array
   * @return a function implementing the test described above
   */
  public static <T> IntObjRelation<T> indexOf() {
    return CheckImpls::isIndexOf;
  }

  /**
   * Verifies that a value can be used as a "from" or "to" index in operations like
   * (but not limited to)
   * {@link Arrays#copyOfRange(int[], int, int) Arrays.copyOfRange},
   * {@link String#substring(int, int) String.substring} and
   * {@link List#subList(int, int) List.subList}. These operations allow both the
   * "from" index and the "to" index to be equal to the length of the array, string
   * or list. No preliminary check is done to ensure the provided object actually is
   * an array, {@code List} or {@code String}. An {@link CorruptCheckException} is
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

  /**
   * Verifies that the argument is greater than, or equal to the first integer in the
   * provided {@code int} array, and less than the second.
   *
   * @return a function implementing the test described above
   */
  public static IntObjRelation<int[]> inRange() {
    return (x, y) -> x >= y[0] && x < y[1];
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

}
