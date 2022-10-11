package nl.naturalis.check.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static nl.naturalis.check.types.Private.*;

/**
 * An extension of {@link IntPredicate} that acts as a bridge between
 * {@code IntPredicate} and the other interfaces in this package in that it enables
 * the composition of new tests from any number of instances of {@code IntPredicate},
 * {@code Predicate}, {@code Relation}, {@code IntRelation}, {@code ObjIntRelation}.
 * It does not override any method of {@code Predicate}. Instead, it supplements it
 * with a comprehensive set of {@code default} methods that allow the composition to
 * take place.  For more information, see {@link ComposablePredicate}.
 *
 * @see ComposablePredicate
 * @see Relation
 * @see IntRelation
 * @see IntObjRelation
 * @see ObjIntRelation
 */
public interface ComposableIntPredicate extends IntPredicate {

  /**
   * Returns a {@code ComposableIntPredicate} that always evaluates to {@code true}.
   * Can be used as the first of a series of AND-joined checks.
   *
   * @return a {@code ComposableIntPredicate} that always evaluates to {@code true}
   */
  static ComposableIntPredicate valid() {
    return x -> true;
  }

  /**
   * Returns a {@code ComposableIntPredicate} that always evaluates to {@code false}.
   * Can be used as the first of a series of OR-joined checks.
   *
   * @return a {@code ComposableIntPredicate} that always evaluates to {@code false}
   */
  static ComposableIntPredicate invalid() {
    return x -> false;
  }

  /**
   * Converts a {@code Predicate} to the equivalent {@code ComposableIntPredicate},
   * so it can become part of a composition. This method can be used to convert a
   * predefined {@code Predicate} constant from outside Naturalis Check to a
   * {@code ComposableIntPredicate}, or to hard-cast a lambda or method reference to
   * a {@code ComposableIntPredicate}, so the compiler will treat it as such. Note
   * that this method is only needed if the {@code Predicate}, lambda or method
   * reference is to be the <i>first</i> test of the composition.
   *
   * <blockquote><pre>{@code
   * Check.that("hello").is(eval((String s) -> s.charAt(1) == 'e')
   *    .orElse((String s) -> s.charAt(1) == 'f'));
   * }</pre></blockquote>
   *
   * @param test the {@code Predicate}
   * @return the equivalent {@code ComposableIntPredicate}
   */
  static ComposableIntPredicate eval(IntPredicate test) {
    return test::test;
  }

  /**
   * Converts an {@code IntRelation} to a {@code ComposableIntPredicate}. More
   * precisely: this method returns a {@code ComposableIntPredicate} that evaluates
   * to {@code true} if the value being tested has the specified relation to the
   * specified value. Note that this method is only needed if the {@code IntRelation}
   * is to be the <i>first</i> test of the composition.
   *
   * @param relation the relation
   * @param object the object of the relation
   * @return a {@code ComposableIntPredicate} that evaluates to {@code true} if the
   *     value being tested has the specified relation to the specified value
   */
  static ComposableIntPredicate eval(IntRelation relation, int object) {
    return s -> relation.exists(s, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or the specified test.
   *
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orElse(IntPredicate test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || test.test(x);
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
   * @param object the object of the provided {@code IntRelation}, with the value
   *     of
   *     <i>this</i> {@code ComposableIntPredicate} now becoming the <i>subject</i>
   *     of that relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orElse(IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || relation.exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or the negation of the specified test.
   *
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orNot(IntPredicate test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || !test.test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if it does not have the specified
   * relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the provided {@code IntRelation}, with the value
   *     of
   *     <i>this</i> {@code ComposableIntPredicate} now becoming the <i>subject</i>
   *     of that relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orNot(IntRelation relation,
      int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || !relation.exists(x, object);
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
   * @param subjects a collection of subjects for the specified
   *     {@code IntRelation}, with the value of <i>this</i>
   *     {@code ComposableIntPredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orAll(int[] subjects, IntRelation relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || Arrays.stream(subjects).allMatch(y -> relation.exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if at least one value in the specified
   * collection has the specified relation to that value.
   *
   * @param subjects a collection of subjects for the specified
   *     {@code IntRelation}, with the value of <i>this</i>
   *     {@code ComposableIntPredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orAny(int[] subjects,
      IntRelation relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || Arrays.stream(subjects).anyMatch(y -> relation.exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if none of the values in the specified
   * collection have the specified relation to that value.
   *
   * @param subjects a collection of subjects for the specified
   *     {@code IntRelation}, with the value of <i>this</i>
   *     {@code ComposableIntPredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orNone(int[] subjects, IntRelation relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || Arrays.stream(subjects).noneMatch(y -> relation.exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if another value manages to pass the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().orThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value to be tested by the other test
   * @return a new test combining this test and the specified test
   */
  default <U> ComposableIntPredicate orThat(U value, Predicate<U> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if another value manages to pass the other test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate orThat(S subject,
      Relation<S, O> relation,
      O object) {
    Objects.requireNonNull(relation, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A
   * value will pass the new test if it passes this test or if the provided
   * expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass
   *     this test
   * @return a new test combining this test and the specified free-form test
   */
  default ComposableIntPredicate orThat(boolean test) {
    return x -> meFirst(x) || test;
  }

  /**
   * Returns a new test combining this test with the free-form test supplied by the
   * specified {@code Supplier}. A value will pass the new test if it passes this
   * test or if the expression supplied by the {@code Supplier} evaluates to
   * {@code true}. The supplier's {@link Supplier#get() get()} method will only be
   * called if the value fails to pass this test. Useful if evaluating the expression
   * is not trivial.
   *
   * @param test the supplier of a boolean expression
   * @return a new test combining this test and the specified free-form test
   */
  default ComposableIntPredicate orEval(Supplier<Boolean> test) {
    return x -> meFirst(x) || test.get();
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if another value manages to pass the negation of the other
   * test.
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value to be tested by the other test
   * @return a new test combining this test and the specified test
   */
  default <U> ComposableIntPredicate orNot(U value, Predicate<U> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if another value manages to pass the negation of the other
   * test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate orNot(S subject,
      Relation<S, O> relation,
      O object) {
    Objects.requireNonNull(relation, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) || !relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if all values in the specified collection have the specified
   * relation to the specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate orAll(Collection<S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().allMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if all values in the specified collection have the specified
   * relation to the specified object.
   *
   * @param subjects a collection of subjects for the specified
   *     {@code IntRelation}. The array must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code IntRelation}
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orAll(int[] subjects,
      IntRelation relation,
      int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || Arrays.stream(subjects).allMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if at least one value in the specified collection has the
   * specified relation to the specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate orAny(Collection<? extends S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().anyMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if at least one value in the specified collection has the
   * specified relation to the specified object.
   *
   * @param subjects a collection of subjects for the specified
   *     {@code IntRelation}. The array must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code IntRelation}
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orAny(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || Arrays.stream(subjects).anyMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if none of the values in the specified collection have the
   * specified relation to the specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate orNone(Collection<? extends S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        || subjects.stream().noneMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test or if none of the values in the specified collection have the
   * specified relation to the specified object.
   *
   * @param subjects an array of int values used as subjects for the specified
   *     {@code IntRelation}. The array must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code IntRelation}
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orNone(int[] subjects,
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
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andAlso(IntPredicate test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && test.test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if it has the specified relation to the
   * specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the provided {@code IntRelation}, with the value
   *     of
   *     <i>this</i> {@code ComposableIntPredicate} now becoming the <i>subject</i>
   *     of that relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andAlso(IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && relation.exists(x, object);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A
   * value will pass the new test if it passes this test and if the provided
   * expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass
   *     this test
   * @return a new test combining this test and the specified free-form test
   */
  default ComposableIntPredicate andAlso(boolean test) {
    return x -> meFirst(x) && test;
  }

  /**
   * Returns a new test combining this test with the free-form test supplied by the
   * specified {@code Supplier}. A value will pass the new test if it passes this
   * test and if the expression supplied by the {@code Supplier} evaluates to
   * {@code true}. The supplier's {@link Supplier#get() get()} method will only be
   * called if the value passes this test. Useful if evaluating the boolean
   * expression is not trivial.
   *
   * @param test the supplier of a boolean expression
   * @return a new test combining this test and the specified free-form test
   */
  default ComposableIntPredicate andEval(Supplier<Boolean> test) {
    return x -> meFirst(x) && test.get();
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes both this test and the negation of the specified
   * test.
   *
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andNot(IntPredicate test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && !test.test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if it does not have the specified
   * relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the provided {@code IntRelation}, with the value
   *     of
   *     <i>this</i> {@code ComposableIntPredicate} now becoming the <i>subject</i>
   *     of that relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andNot(IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && !relation.exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if all values in the specified
   * collection have the specified relation to that value.
   *
   * @param subjects an array of int values used as subjects for the specified
   *     {@code IntRelation}, with the value of <i>this</i>
   *     {@code ComposableIntPredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andAll(int[] subjects,
      IntRelation relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x) && Arrays.stream(subjects)
        .allMatch(y -> relation.exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if at least one value in the specified
   * collection has the specified relation to that value.
   *
   * @param subjects an array of int values used as subjects for the specified
   *     {@code IntRelation}, with the value of <i>this</i>
   *     {@code ComposableIntPredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andAny(int[] subjects,
      IntRelation relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && Arrays.stream(subjects).anyMatch(y -> relation.exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test and if none of the values in the specified
   * collection have the specified relation to that value.
   *
   * @param subjects an array of int values used as subjects for the specified
   *     {@code IntRelation}, with the value of <i>this</i>
   *     {@code ComposableIntPredicate} now becoming the
   *     <i>object</i> of that relation. The collection must contain at least one
   *     element.
   * @param relation the relationship test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andNone(int[] subjects,
      IntRelation relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && Arrays.stream(subjects).noneMatch(y -> relation.exists(y, x));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if another value manages to pass the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().andThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value to be tested by the other test
   * @return a new test combining this test and the specified test
   */
  default <U> ComposableIntPredicate andThat(U value, Predicate<U> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if another value manages to pass the other test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate andThat(S subject,
      Relation<S, O> relation,
      O object) {
    Objects.requireNonNull(relation, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if another value manages to pass the negation of the other
   * test.
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value to be tested by the other test
   * @return a new test combining this test and the specified test
   */
  default <U> ComposableIntPredicate andNot(U value, Predicate<U> test) {
    Objects.requireNonNull(test, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if another value manages to pass the negation of the other
   * test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate andNot(S subject,
      Relation<S, O> relation,
      O object) {
    Objects.requireNonNull(relation, TEST_MUST_NOT_BE_NULL);
    return x -> meFirst(x) && !relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if all values in the specified collection have the
   * specified relation to the specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate andAll(Collection<? extends S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && subjects.stream().allMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if all values in the specified collection have the
   * specified relation to the specified object.
   *
   * @param subjects an array of int values used as subjects for the specified
   *     {@code IntRelation}. The array must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code IntRelation}
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andAll(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && Arrays.stream(subjects).allMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if at least one value in the specified collection has the
   * specified relation to the specified object.
   *
   * @param subjects a collection of subjects for the specified {@code Relation}.
   *     The collection must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code Relation}
   * @param <S> the type of the subject of the specified relation
   * @param <O> the type of the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate andAny(Collection<? extends S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && subjects.stream().anyMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if at least one value in the specified collection has the
   * specified relation to the specified object.
   *
   * @param subjects an array of int values used as subjects for the specified
   *     {@code IntRelation}. The array must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code IntRelation}
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andAny(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && Arrays.stream(subjects).anyMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if none of the values in the specified collection have the
   * specified relation to the specified object.
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
   * @return a new test combining this test and the specified test
   */
  default <S, O> ComposableIntPredicate andNone(Collection<? extends S> subjects,
      Relation<S, O> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && subjects.stream().noneMatch(y -> relation.exists(y, object));
  }

  /**
   * Returns a new test combining this test and the specified test. It combines, in
   * effect, two checks on two different values. A value will pass the new test if it
   * passes this test and if none of the values in the specified collection have the
   * specified relation to the specified object.
   *
   * @param subjects an array of int values used as subjects for the specified
   *     {@code IntRelation}. The array must contain at least one element.
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified {@code IntRelation}
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andNone(int[] subjects,
      IntRelation relation, int object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    return x -> meFirst(x)
        && Arrays.stream(subjects).noneMatch(y -> relation.exists(y, object));
  }

  private boolean meFirst(int i) {
    return ComposableIntPredicate.this.test(i);
  }

}
