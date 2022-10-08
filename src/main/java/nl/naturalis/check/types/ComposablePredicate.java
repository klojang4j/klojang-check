package nl.naturalis.check.types;

import nl.naturalis.check.CommonChecks;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import static nl.naturalis.check.types.Private.*;

/**
 * An extension of {@link Predicate} that acts as a bridge between {@code Predicate}
 * and the other interfaces in this package in that it enables the composition of new
 * tests from any number of instances of {@code Predicate}, {@code IntPredicate},
 * {@code Relation}, {@code IntRelation}, {@code ObjIntRelation}. Note that the newly
 * formed test is no longer associated with any predefined error message any longer,
 * even if it was composed solely of checks from the {@link CommonChecks} class.
 *
 * <h2>Generics</h2>
 *
 * <p>Even though the type parameter for this interface is {@code <T>}, the type
 * parameter for the functional interfaces passed to the {@code or} and {@code and}
 * methods is not {@code <? super T>}, as you would ordinarily expect (see for
 * example {@link Predicate#or(Predicate)}). Instead, it is just {@code <?>}. This
 * allows generic checks like {@link CommonChecks#notNull() notNull()}, which can be
 * applied to any type of value, to be followed by checks that target more specific
 * types of values. For example, the following code would not compile if the type of
 * the first argument to {@code orElse()} were {@code Relation<? super T, O>} (rather
 * than {@code Relation<?, O>}):
 *
 * <blockquote><pre>{@code
 * Check.that(List.of("foo", "bar")).is(empty().orElse(contains(), "foo"));
 * }</pre></blockquote>
 *
 * <p>The downside is that it is easier for a composition of tests to harbor a type
 * error without the compiler noticing it, thus causing a {@link ClassCastException}
 * at runtime. For example, the following statement would compile just as well:
 *
 * <blockquote><pre>{@code
 * Check.that(List.of("foo", "bar")).is(empty().orElse(hasSubstring(), "foo"));
 * }</pre></blockquote>
 *
 * <p>In addition, the type error may escape attention for quite some time. The
 * following statement compiles and will <i>not</i> throw a
 * {@code ClassCastException}:
 *
 * <blockquote><pre>{@code
 * Check.that(List.of("foo", "bar")).is(notEmpty().orElse(hasSubstring(), "foo"));
 * }</pre></blockquote>
 *
 * <p>If you are not comfortable with this, you can instead use the
 * {@link #orThat(Object, Predicate) orThat()} methods and repeat the argument for
 * every call to {@code orThat()}
 *
 * <blockquote><pre>{@code
 * Check.that("foo bar").is(empty().orThat("foo bar", hasSubstring(), "foo"));
 * }</pre></blockquote>
 *
 * <p>Note, however, that the {@code orThat()} methods are primarily meant to join
 * two tests on two completely unrelated values:
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
@SuppressWarnings({"unchecked", "rawtypes", "UnnecessaryLocalVariable"})
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
    Predicate self = this;
    return x -> self.test(x) || ((Predicate) test).test(x);
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
  default <O, V> ComposablePredicate<V> orElse(Relation<?, ? super O> relation,
      O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    Predicate self = this;
    return x -> self.test(x) || ((Relation) relation).exists(x, object);
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
    Predicate self = this;
    return x -> self.test(x) || !((Predicate) test).test(x);
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
  default <O, V> ComposablePredicate<V> orNot(Relation<?, ? super O> relation,
      O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    Predicate self = this;
    return x -> self.test(x) || !((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes this test or if all values in the specified collection
   * have the specified relation to that value.
   *
   * <blockquote><pre>{@code
   * Check.that(string).is(empty().orAll(List.of("foo", "bar"), substringOf());
   * }</pre></blockquote>
   *
   * <p>(Statically import the {@code List.of} method for an even stronger sugar
   * rush.)
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
  default <S, V> ComposablePredicate<V> orAll(Collection<S> subjects,
      Relation<? super S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    Predicate self = this;
    Relation r = relation;
    return x -> self.test(x) || subjects.stream().allMatch(y -> r.exists(y, x));
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
  default <S, V> ComposablePredicate<V> orAny(Collection<S> subjects,
      Relation<? super S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    Predicate self = this;
    Relation r = relation;
    return x -> self.test(x) || subjects.stream().anyMatch(y -> r.exists(y, x));
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
  default <S, V> ComposablePredicate<V> orNone(Collection<S> subjects,
      Relation<? super S, ?> relation) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    Predicate self = this;
    Relation r = relation;
    return x -> self.test(x) || subjects.stream().noneMatch(y -> r.exists(y, x));
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
    Predicate self = this;
    return x -> self.test(x) || test.test(value);
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
    Predicate self = this;
    return x -> self.test(x) || relation.exists(subject, object);
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
    Predicate self = this;
    return x -> self.test(x) || !test.test(value);
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
    Predicate self = this;
    return x -> self.test(x) || !relation.exists(subject, object);
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
      Relation<? super S, ?> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    Predicate self = this;
    Relation r = relation;
    return x -> self.test(x) || subjects.stream().allMatch(y -> r.exists(y, object));
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
      Relation<? super S, ?> relation, O object) {
    Objects.requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    checkSubjects(subjects);
    Predicate self = this;
    Relation r = relation;
    return x -> self.test(x) || subjects.stream().anyMatch(y -> r.exists(y, object));
  }

}
