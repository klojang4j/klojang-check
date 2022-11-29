package org.klojang.check.relation;

import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.klojang.check.relation.Private.testAgainstArray;

/**
 * An extension of {@link IntPredicate} that acts as a bridge between
 * {@code IntPredicate} and the relational interfaces in this package. It enables the
 * composition of new tests from any number of instances of {@link Predicate},
 * {@link IntPredicate}, {@link Relation}, {@link IntRelation} and
 * {@link IntObjRelation}. {@code ComposableIntPredicate} extends
 * {@link IntPredicate} with a set of {@code default} methods that allow the
 * composition to take place. For more information, see {@link ComposablePredicate}.
 */
@SuppressWarnings({"unchecked"})
@FunctionalInterface
public interface ComposableIntPredicate extends IntPredicate {

  /**
   * Returns the negation of this predicate.
   *
   * @return the negation of this predicate
   */
  default ComposableIntPredicate negated() {
    return x -> !meFirst(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or the specified test.
   *
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orElse(IntPredicate test) {
    return x -> meFirst(x) || test.test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or if it has the specified relation
   * to the specified value.
   *
   * <blockquote><pre>{@code
   * Check.that("foo bar").is(empty().orElse(hasSubstring(), "foo"));
   * }</pre></blockquote>
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation, with the value of this
   *     {@code ComposablePredicate} now becoming the subject of that relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orElse(IntRelation relation, int object) {
    return x -> meFirst(x) || relation.exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or the negation of the specified
   * test.
   *
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orNot(IntPredicate test) {
    return x -> meFirst(x) || !test.test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or if it does not have the specified
   * relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation, with the value of this
   *     {@code ComposablePredicate} now becoming the subject of that relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orNot(IntRelation relation, int object) {
    return x -> meFirst(x) || !relation.exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or if it has a particular relation to
   * the specified set of values.
   *
   * @param relation the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects the set of values to test the value against
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate or(IntRelation relation,
      Quantifier quantifier,
      int... objects) {
    return x -> meFirst(x)
        || testAgainstArray(x, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <T> the type of the value tested by the predicate
   * @return a new test combining this test and the specified test
   */
  default <T> ComposableIntPredicate orThat(T value, Predicate<T> test) {
    return x -> meFirst(x) || test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orThat(int value, IntPredicate test) {
    return x -> meFirst(x) || test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
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
    return x -> meFirst(x) || relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orThat(int subject,
      IntRelation relation,
      int object) {
    return x -> meFirst(x) || relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the negation of the other
   * test.
   *
   * @param value the value to be tested by the specified predicate
   * @param test the test to combine this test with
   * @param <T> the type of values being tested by the specified predicate
   * @return a new test combining this test and the specified test
   */
  default <T> ComposableIntPredicate orNot(T value, Predicate<T> test) {
    return x -> meFirst(x) || !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the negation of the other
   * test.
   *
   * @param value the value to be tested by the specified predicate
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate orNot(int value, IntPredicate test) {
    return x -> meFirst(x) || !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the negation of the other
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
    return x -> meFirst(x) || !relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if <i>another</i> value ({@code subject}) has a particular
   * relation to the specified set of values.
   *
   * @param subject the subject of the specified {@code Relation}
   * @param relation the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects the set of values to test the subject against
   * @param <S> the type of the subject of the relation
   * @param <O> the type of the object of the relation
   * @param <P> the type of the values fed as "objects" into the relation
   * @return a new test combining this test and the specified test
   */
  default <S, O, P extends O> ComposableIntPredicate or(S subject,
      Relation<S, O> relation, Quantifier quantifier, P... objects) {
    return x -> meFirst(x)
        || testAgainstArray(subject, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if <i>another</i> value ({@code subject}) has a particular
   * relation to the specified set of values.
   *
   * @param subject the subject of the relation
   * @param relation the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects the set of values to test the subject against
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate or(int subject,
      IntRelation relation, Quantifier quantifier, int... objects) {
    return x -> meFirst(x)
        || testAgainstArray(subject, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A
   * value will pass the new test if it passes <i>this</i> test or if the provided
   * expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass
   *     this test
   * @return a new test combining this test and the specified free-form test
   */
  default ComposableIntPredicate or(boolean test) {
    return x -> meFirst(x) || test;
  }

  /**
   * Returns a new test combining this test with the free-form test supplied by the
   * specified {@code Supplier}. A value will pass the new test if it passes
   * <i>this</i> test or if the expression supplied by the {@code Supplier}
   * evaluates to {@code true}. The supplier's {@link Supplier#get() get()} method
   * will only be called if the value fails to pass this test. Useful if evaluating
   * the expression could be expensive.
   *
   * @param test the supplier of a boolean expression
   * @return a new test combining this test and the specified free-form test
   */
  default ComposableIntPredicate orEval(Supplier<Boolean> test) {
    return x -> meFirst(x) || test.get();
  }

  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////// [ AND methods ] ////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes both this test and the specified test.
   *
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andAlso(IntPredicate test) {
    return x -> meFirst(x) && test.test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test and if it has the specified relation
   * to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the relation, with the value being tested now
   *     becoming the subject of the relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andAlso(IntRelation relation, int object) {
    return x -> meFirst(x) && relation.exists(x, object);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A
   * value will pass the new test if it passes <i>this</i> test and if the provided
   * expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass
   *     this test
   * @return a new test combining this test and the specified free-form test
   */
  default ComposableIntPredicate and(boolean test) {
    return x -> meFirst(x) && test;
  }

  /**
   * Returns a new test combining this test with the free-form test supplied by the
   * specified {@code Supplier}. A value will pass the new test if it passes
   * <i>this</i> test and if the expression supplied by the {@code Supplier}
   * evaluates to {@code true}. The supplier's {@link Supplier#get() get()} method
   * will only be called if the value passes this test. Useful if evaluating the
   * boolean expression could be expensive.
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
    return x -> meFirst(x) && !test.test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test and if it does not have the specified
   * relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the relation, with the value being tested now
   *     becoming the subject of the relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andNot(IntRelation relation,
      int object) {
    return x -> meFirst(x) && !relation.exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test and if it has a particular relation
   * to the specified set of values.
   *
   * @param relation the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects the set of values to test the value against
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate and(IntRelation relation,
      Quantifier quantifier,
      int... objects) {
    return x -> meFirst(x)
        && testAgainstArray(x, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the other test.
   *
   * @param value the value to be tested by the specified predicate
   * @param test the test to combine this test with
   * @param <T> the type of values being tested by the specified predicate
   * @return a new test combining this test and the specified test
   */
  default <T> ComposableIntPredicate andThat(T value, Predicate<T> test) {
    return x -> meFirst(x) && test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the other test.
   *
   * @param value the value to be tested by the specified predicate
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andThat(int value, IntPredicate test) {
    return x -> meFirst(x) && test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the other test.
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
    return x -> meFirst(x) && relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the other test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andThat(int subject,
      IntRelation relation,
      int object) {
    return x -> meFirst(x) && relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the negation of the other
   * test.
   *
   * @param value the value to be tested by the specified predicate
   * @param test the test to combine this test with
   * @param <T> the type of values being tested by the specified predicate
   * @return a new test combining this test and the specified test
   */
  default <T> ComposableIntPredicate andNot(T value, Predicate<T> test) {
    return x -> meFirst(x) && !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the negation of the other
   * test.
   *
   * @param value the value to be tested by the specified predicate
   * @param test the test to combine this test with
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andNot(int value, IntPredicate test) {
    return x -> meFirst(x) && !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the negation of the other
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
    return x -> meFirst(x) && !relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the negation of the other
   * test.
   *
   * @param subject the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object the object of the specified relation
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate andNot(int subject,
      IntRelation relation,
      int object) {
    return x -> meFirst(x) && !relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if <i>another</i> value ({@code subject}) has a particular
   * relation to the specified set of values.
   *
   * @param subject the subject of the specified {@code Relation}
   * @param relation the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects the set of values to test the subject against
   * @param <S> the type of the subject of the relation
   * @param <O> the type of the object of the relation
   * @param <P> the type of the values fed as "objects" into the relation
   * @return a new test combining this test and the specified test
   */
  default <S, O, P extends O> ComposableIntPredicate and(S subject,
      Relation<S, O> relation, Quantifier quantifier, P... objects) {
    return x -> meFirst(x)
        && testAgainstArray(subject, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if <i>another</i> value ({@code subject}) has a particular
   * relation to the specified set of values.
   *
   * @param subject the subject of the relation
   * @param relation the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects the set of values to test the subject against
   * @return a new test combining this test and the specified test
   */
  default ComposableIntPredicate and(int subject,
      IntRelation relation, Quantifier quantifier, int... objects) {
    return x -> meFirst(x)
        && testAgainstArray(subject, relation, quantifier, objects);
  }

  private boolean meFirst(int i) {
    return ComposableIntPredicate.this.test(i);
  }

}
