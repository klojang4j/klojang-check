package nl.naturalis.check.types;

import nl.naturalis.check.CommonChecks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static nl.naturalis.check.types.Private.*;

/**
 * An extension of {@link Predicate} that acts as a bridge between {@code Predicate}
 * and the other interfaces in this package in that it enables the composition of new
 * tests from any number of instances of {@code Predicate}, {@code IntPredicate},
 * {@code Relation}, {@code IntRelation}, {@code ObjIntRelation}. Note that the newly
 * formed test is no longer associated with any predefined error message, even if it
 * was composed solely of checks from the {@link CommonChecks} class.
 *
 * <h2>Generics</h2>
 *
 * <p>Even though the type parameter for {@code ComposablePredicate} is {@code <T>},
 * the type parameter for the predicates and relations it strings together is not
 * {@code <? super T>}, as you would ordinarily expect. Instead, it is simply
 * {@code <?>}. This allows checks like {@link CommonChecks#notNull() notNull()},
 * which can be applied to any type of value, to be followed by checks that target
 * more specific types of values. For example, the following code would not compile
 * if the type of the first argument to {@code orElse()} were
 * {@code Relation<? super T, O>} instead of simply {@code Relation<?, O>}:
 *
 * <blockquote><pre>{@code
 * Check.that(List.of("foo", "bar")).is(NULL().orElse(contains(), "foo"));
 * }</pre></blockquote>
 *
 * <p>The downside is that it is easier for a composition of tests to harbor a type
 * error without the compiler noticing it, resulting in a {@link ClassCastException}
 * at runtime. For example, the following nonsensical statement compiles just as
 * well:
 *
 * <blockquote><pre>{@code
 * // or else is a readable file
 * Check.that(myArrayList).is(empty().orElse(readable()));
 * }</pre></blockquote>
 *
 * <p>If you are not comfortable with this, you can instead use the
 * {@link #orThat(Object, Predicate) orThat()} method and repeat the argument for
 * every call to {@code orThat()}:
 *
 * <blockquote><pre>{@code
 * Check.that("foo bar").is(empty().orThat("foo bar", hasSubstring(), "foo"));
 * }</pre></blockquote>
 *
 * <p>Note, however, that the {@code orThat()} methods are primarily meant to join
 * tests on two completely unrelated values:
 *
 * <blockquote><pre>{@code
 * Check.that(string1).is(empty().orThat(string2, hasSubstring(), "foo"));
 * }</pre></blockquote>
 *
 * @param <T> the type of the value being tested
 * @see Relation
 * @see IntRelation
 * @see IntObjRelation
 * @see ObjIntRelation
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface ComposablePredicate<T> extends Predicate<T> {

  /**
   * Returns a {@code ComposablePredicate} that always evaluates to {@code true}. Can
   * be used as the first of a series of AND-joined checks.
   *
   * @param <T> the type of the value being tested (which is ignored by the
   *     returned {@code ComposablePredicate})
   * @return a {@code ComposablePredicate} that always evaluates to {@code true}
   */
  static <T> ComposablePredicate<T> valid() {
    return x -> true;
  }

  /**
   * Returns a {@code ComposablePredicate} that always evaluates to {@code false}.
   * Can be used as the first of a series of OR-joined checks.
   *
   * @param <T> the type of the value being tested (which is ignored by the
   *     returned {@code ComposablePredicate})
   * @return a {@code ComposablePredicate} that always evaluates to {@code false}
   */
  static <T> ComposablePredicate<T> invalid() {
    return x -> false;
  }

  /**
   * Converts a {@code Predicate} to the equivalent {@code ComposablePredicate}, so
   * it can become part of a composition. This method can be used to convert a
   * predefined {@code Predicate} constant from outside Naturalis Check to a
   * {@code ComposablePredicate}, or to hard-cast a lambda or method reference to a
   * {@code ComposablePredicate}, so the compiler will treat it as such. Note that
   * this method is only needed if the {@code Predicate}, lambda or method reference
   * is to be the <i>first</i> test of the composition.
   *
   * <blockquote><pre>{@code
   * Check.that("hello").is(eval((String s) -> s.charAt(1) == 'e')
   *    .orElse((String s) -> s.charAt(1) == 'f'));
   * }</pre></blockquote>
   *
   * @param test the {@code Predicate}
   * @param <T> the type of the value being tested
   * @return the equivalent {@code ComposablePredicate}
   */
  static <T> ComposablePredicate<T> eval(Predicate<T> test) {
    return test::test;
  }

  /**
   * Converts a {@code Relation} to a {@code ComposablePredicate}. More precisely:
   * this method returns a {@code ComposablePredicate} that evaluates to {@code true}
   * if the value being tested has the specified relation to the specified value.
   * Note that this method is only needed if the {@code Relation} is to be the
   * <i>first</i> test of the composition.
   *
   * <blockquote><pre>{@code
   * Check.that(Year.now()).is(eval(GT(), Year.of(2000)).andAlso(LT(), Year.of(3000)));
   * // import static java.util.List.of;
   * Check.that(42F).is(eval(GT(), 100F).orAll(of(1, 2, 3, 4, 5), LT(), 10));
   * }</pre></blockquote>
   *
   * @param relation the relation
   * @param object the object of the relation
   * @param <S> the type of the subject of the relation
   * @param <O> the type of the object of the relation
   * @return a {@code ComposablePredicate} that evaluates to {@code true} if the
   *     value being tested has the specified relation to the specified value
   */
  static <S, O> ComposablePredicate<S> eval(Relation<S, O> relation, O object) {
    return s -> relation.exists(s, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or the specified test.
   *
   * @param test the test to combine this test with
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orElse(Predicate<?> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || ((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if it has the specified relation to the
   * specified value.
   *
   * <blockquote><pre>{@code
   * Check.that("foo bar").is(empty().orElse(hasSubstring(), "foo"));
   * }</pre></blockquote>
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the provided {@code Relation}, with the value
   *     of
   *     <i>this</i> {@code ComposablePredicate} now becoming the <i>subject</i> of
   *     that relation
   * @param <O> the type of the object of the provided {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> orElse(Relation<?, O> relation,
      O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || ((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A
   * value will pass the new test if it passes this test or if the provided
   * expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass
   *     this test
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> orElse(boolean test) {
    return x -> meFirst(x) || test;
  }

  /**
   * Returns a new test combining this test with the free-form test supplied by the
   * specified {@code Supplier}. A value will pass the new test if it passes this
   * test or if the expression supplied by the {@code Supplier} evaluates to
   * {@code true}. The supplier's {@link Supplier#get() get()} method will only be
   * called if the value fails to pass this test. Useful if evaluating the expression
   * is not necessarily trivial.
   *
   * @param test the supplier of a boolean expression
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> orEval(Supplier<Boolean> test) {
    return x -> meFirst(x) || test.get();
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or the negation of the specified test.
   *
   * @param test the test to combine this test with
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orNot(Predicate<?> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || !((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if it does not have the specified
   * relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the provided {@code Relation}, with the value
   *     of
   *     <i>this</i> {@code ComposablePredicate} now becoming the <i>subject</i> of
   *     that relation
   * @param <O> the type of the object of the provided {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> orNot(Relation<?, O> relation,
      O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || !((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if all values in the specified collection
   * have the specified relation to that value.
   *
   * <blockquote><pre>{@code
   * // import static java.util.List.of;
   * Check.that("foo bar").is(empty().orAll(of("foo", "bar"), substringOf());
   * }</pre></blockquote>
   *
   * @param subjects a collection of subjects for the specified {@code Relation},
   *     with the value of <i>this</i> {@code ComposablePredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @param <S> the type of the subject of the relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, V> ComposablePredicate<V> orAll(Collection<? extends S> subjects,
      Relation<S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().allMatch(y -> ((Relation) relation).exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if at least one value in the specified
   * collection has the specified relation to that value.
   *
   * @param subjects a collection of subjects for the specified {@code Relation},
   *     with the value of <i>this</i> {@code ComposablePredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @param <S> the type of the subject of the relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, V> ComposablePredicate<V> orAny(Collection<? extends S> subjects,
      Relation<S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().anyMatch(y -> ((Relation) relation).exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if none of the values in the specified
   * collection have the specified relation to that value.
   *
   * @param subjects a collection of subjects for the specified {@code Relation},
   *     with the value of <i>this</i> {@code ComposablePredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @param <S> the type of the subject of the relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, V> ComposablePredicate<V> orNone(Collection<? extends S> subjects,
      Relation<S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().noneMatch(y -> ((Relation) relation).exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if
   * another value manages to pass the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().orThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value to be tested by the other test
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> orThat(U value, Predicate<U> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if
   * another value manages to pass the other test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orThat(S subject,
      Relation<S, O> relation,
      O object) {
    Objects.requireNonNull(relation, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if
   * another value manages to pass the negation of the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().orThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value to be tested by the other test
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> orNot(U value, Predicate<U> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if
   * another value manages to pass the negation of the other test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orNot(S subject,
      Relation<S, O> relation,
      O object) {
    Objects.requireNonNull(relation, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || !relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if all
   * values in the specified collection have the specified relation to the specified
   * object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orAll(Collection<S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().allMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if all
   * values in the specified collection have the specified relation to the specified
   * object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orAll(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || Arrays.stream(subjects).allMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if at
   * least one value in the specified collection has the specified relation to the
   * specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orAny(Collection<S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().anyMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if at
   * least one value in the specified collection has the specified relation to the
   * specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orAny(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || Arrays.stream(subjects).anyMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if
   * none of the values in the specified collection have the specified relation to
   * the specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orNone(Collection<S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().noneMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the disjunction (OR) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test or if
   * none of the values in the specified collection have the specified relation to
   * the specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orNone(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || Arrays.stream(subjects).noneMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes both this test and the specified test.
   *
   * @param test the test to combine this test with
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andAlso(Predicate<?> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && ((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if it has the specified relation to the
   * specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the provided {@code Relation}, with the value
   *     of
   *     <i>this</i> {@code ComposablePredicate} now becoming the <i>subject</i> of
   *     that relation
   * @param <O> the type of the object of the provided {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> andAlso(Relation<?, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && ((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A
   * value will pass the new test if it passes this test and if the provided
   * expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass
   *     this test
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> andAlso(boolean test) {
    return x -> meFirst(x) && test;
  }

  /**
   * Returns a new test combining this test with the free-form test supplied by the
   * specified {@code Supplier}. A value will pass the new test if it passes this
   * test and if the expression supplied by the {@code Supplier} evaluates to
   * {@code true}. The supplier's {@link Supplier#get() get()} method will only be
   * called if the value passes this test. Useful if evaluating the boolean
   * expression is not necessarily trivial.
   *
   * @param test the supplier of a boolean expression
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> andEval(Supplier<Boolean> test) {
    return x -> meFirst(x) && test.get();
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes both this test and the negation of the specified
   * test.
   *
   * @param test the test to combine this test with
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andNot(Predicate<?> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && !((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if it does not have the specified
   * relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the provided {@code Relation}, with the value
   *     of
   *     <i>this</i> {@code ComposablePredicate} now becoming the <i>subject</i> of
   *     that relation
   * @param <O> the type of the object of the provided {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> andNot(Relation<?, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && !((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if all values in the specified
   * collection have the specified relation to that value.
   *
   * @param subjects a collection of subjects for the specified {@code Relation},
   *     with the value of <i>this</i> {@code ComposablePredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @param <S> the type of the subject of the relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, V> ComposablePredicate<V> andAll(Collection<? extends S> subjects,
      Relation<S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x) && subjects.stream()
        .allMatch(y -> ((Relation) relation).exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if at least one value in the specified
   * collection has the specified relation to that value.
   *
   * @param subjects a collection of subjects for the specified {@code Relation},
   *     with the value of <i>this</i> {@code ComposablePredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @param <S> the type of the subject of the relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, V> ComposablePredicate<V> andAny(Collection<? extends S> subjects,
      Relation<S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && subjects.stream().anyMatch(y -> ((Relation) relation).exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if none of the values in the specified
   * collection have the specified relation to that value.
   *
   * @param subjects a collection of subjects for the specified {@code Relation},
   *     with the value of <i>this</i> {@code ComposablePredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @param <S> the type of the subject of the relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, V> ComposablePredicate<V> andNone(Collection<? extends S> subjects,
      Relation<S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && subjects.stream().noneMatch(y -> ((Relation) relation).exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if
   * another value manages to pass the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().andThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value to be tested by the other test
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> andThat(U value, Predicate<U> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if
   * another value manages to pass the other test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andThat(S subject,
      Relation<S, O> relation,
      O object) {
    Objects.requireNonNull(relation, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if
   * another value manages to pass the negation of the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().andThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value to be tested by the other test
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> andNot(U value, Predicate<U> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if
   * another value manages to pass the negation of the other test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andNot(S subject,
      Relation<S, O> relation,
      O object) {
    Objects.requireNonNull(relation, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && !relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if
   * all values in the specified collection have the specified relation to the
   * specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andAll(Collection<? extends S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && subjects.stream().allMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if
   * all values in the specified collection have the specified relation to the
   * specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andAll(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && Arrays.stream(subjects).allMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if at
   * least one value in the specified collection has the specified relation to the
   * specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andAny(Collection<? extends S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && subjects.stream().anyMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if at
   * least one value in the specified collection has the specified relation to the
   * specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andAny(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && Arrays.stream(subjects).anyMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if
   * none of the values in the specified collection have the specified relation to
   * the specified object.
   *
   * <blockquote><pre>{@code
   * // import static java.util.List.of;
   * Check.that("foo bar").is(valid().andNone(of("hello", "world"), substringOf());
   * }</pre></blockquote>
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andNone(Collection<? extends S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && subjects.stream().noneMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It is, in fact,
   * the conjunction (AND) of two completely separate tests for two completely
   * separate values. A value will pass the new test if it passes this test and if
   * none of the values in the specified collection have the specified relation to
   * the specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <V> the type of the value tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that's the type of
   *     the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andNone(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && Arrays.stream(subjects).noneMatch(y -> relation.exists(y, object));
  }

  private <V> boolean meFirst(V v) {
    return ComposablePredicate.this.test((T) v);
  }

}
