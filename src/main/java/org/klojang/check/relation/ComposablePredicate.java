package org.klojang.check.relation;

import static org.klojang.check.relation.Private.*;

import java.util.function.Predicate;
import java.util.function.Supplier;

import org.klojang.check.CommonChecks;

/**
 * An extension of {@link Predicate} that acts as a bridge between {@code Predicate}
 * and the relational interfaces in this package. It enables the composition of new
 * tests from any number of instances of {@code Predicate}, {@code IntPredicate},
 * {@code Relation}, {@code IntRelation} and {@code IntObjRelation}.
 * {@code ComposablePredicate} does not override any method of {@code Predicate}.
 * Instead, it extends it with a comprehensive set of {@code default} methods that
 * allow the composition to take place. These methods can be divided along two axes:
 * <b>{@code and}</b> vs. <b>{@code or}</b> methods on the one hand and, on the
 * other, methods that execute two checks on a single value vs. methods that
 * effectively constitute a single check on two interdependent values.
 *
 * <h2>AND vs. OR Compositions</h2>
 *
 * <p>Generally, you will have more use for compositions expressing a logical
 * disjunction (OR), as the chain of checks following
 * {@link org.klojang.check.Check#that(Object) Check.that(...)} already constitutes a
 * logical conjunction (AND). For example, this statement:
 *
 * <blockquote><pre>{@code
 * Check.that(numChairs).is(positive()).is(lt(), 5).is(even());
 * }</pre></blockquote>
 *
 * <p>requires the number of chairs to be positive <b>and</b> less than 5
 * <b>and</b> even. If the number of chairs needs to pass just one of
 * these tests, write:
 *
 * <blockquote><pre>{@code
 * Check.that(numChairs).is(positive().orElse(lt(), 5).orElse(even()));
 * }</pre></blockquote>
 *
 * <p>Nevertheless, you might still want to use the {@code and()} methods for
 * conciseness:
 *
 * <blockquote><pre>{@code
 * Check.that(string).is(notNull().and(hasSubstring(), allOf(), "to", "be", "or", "not"));
 * }</pre></blockquote>
 *
 * <p>In the above example, the argument is tested against a domain of values.
 * Strictly speaking, the domain should be modeled as a {@link java.util.Set Set},
 * but for convenience it is a varargs array. See {@link Quantifier} for the
 * {@code allOf()} argument.
 *
 * <h2>Testing Interdependent Values</h2>
 *
 * <p>Sometimes, an argument, field or variable cannot be tested in isolation. Its
 * validity depends on the value of another argument, field or variable:
 *
 * <blockquote><pre>{@code
 * Check.that(electionRigged).is(yes().or(winner, EQ(), me);
 * }</pre></blockquote>
 *
 * <p>In the above example, the election only needs to be rigged if it turns out you
 * lost it. Note, however, that, in principle, the two checks could be completely
 * unrelated. There is no under-the-hood mechanism that enforces the interrelatedness
 * of the values. Thus it is up to the client whether to use this type of composition
 * as intended. Also note that the second check continues nicely in the idiom of
 * Klojang Check. Strictly speaking, however, that is now only syntactic sugar and,
 * depending on your taste, you can also just write:
 *
 * <blockquote><pre>{@code
 * Check.that(electionRigged).is(yes().or(winner.equals(me)));
 * }</pre></blockquote>
 *
 * <h2>Generics</h2>
 *
 * <p>Even though the type parameter for {@code ComposablePredicate} is {@code <T>},
 * the type parameter for the predicates and relations it strings together is not
 * {@code <? super T>}, as you would ordinarily expect. Instead, it is simply
 * {@code <?>}. This allows generic checks like
 * {@link CommonChecks#notNull() notNull()} and
 * {@link CommonChecks#notEmpty() notEmpty()}, which can be applied to any
 * non-primitive type, to be followed by checks that can only be applied to a
 * specific type of values. For example, the following code would <i>not</i> compile
 * if the type of the first argument to {@code orElse()} were
 * {@code Relation<? super T, O>} instead of simply {@code Relation<?, O>}:
 *
 * <blockquote><pre>{@code
 * Check.that(myArrayList).is(empty().orElse(contains(), "foo"));
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
 * Check.that(myArrayList).is(empty().orThat(myArrayList, contains(), "foo"));
 * }</pre></blockquote>
 *
 * <p>Note, however, that the {@code orThat()} method is primarily meant to test
 * interrelated values.
 *
 * <blockquote><pre>{@code
 * Check.that(list1).is(empty().orThat(list2, contains(), "foo"));
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
   * be used as the first of a series of AND-joined check, assuming you can do
   * without a {@link CommonChecks#notNull() notNull()}.
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
   * Check.that("hello").is(validIf((String s) -> s.charAt(1) == 'e')
   *    .orElse((String s) -> s.charAt(1) == 'f'));
   * }</pre></blockquote>
   *
   * @param test the {@code Predicate}
   * @param <T> the type of the value being tested
   * @return the equivalent {@code ComposablePredicate}
   */
  static <T> ComposablePredicate<T> validIf(Predicate<T> test) {
    checkArg(test);
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
   * Check.that(Year.now()).is(validIf(GT(), Year.of(2000)).andAlso(LT(), Year.of(3000)));
   * // import static java.util.List.of;
   * Check.that(42F).is(validIf(GT(), 100F).orAll(of(1, 2, 3, 4, 5), LT(), 10));
   * }</pre></blockquote>
   *
   * @param relation the relation
   * @param object the object of the relation
   * @param <S> the type of the subject of the relation
   * @param <O> the type of the object of the relation
   * @return a {@code ComposablePredicate} that evaluates to {@code true} if the
   *     value being tested has the specified relation to the specified value
   */
  static <S, O> ComposablePredicate<S> validIf(Relation<S, O> relation, O object) {
    checkArg(relation);
    return s -> relation.exists(s, object);
  }

  /**
   * Returns the negation of this predicate.
   *
   * @return the negation of this predicate
   */
  default ComposablePredicate<T> negated() {
    return x -> !meFirst(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or the specified test.
   *
   * @param test the test to combine this test with
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orElse(Predicate<?> test) {
    checkArg(test);
    return x -> meFirst(x) || ((Predicate) test).test(x);
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
   * @param <O> the type of the object of the provided {@code Relation}
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> orElse(Relation<?, O> relation,
      O object) {
    checkArg(relation);
    return x -> meFirst(x) || ((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or the negation of the specified
   * test.
   *
   * @param test the test to combine this test with
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orNot(Predicate<?> test) {
    checkArg(test);
    return x -> meFirst(x) || !((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or if it does not have the specified
   * relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the provided {@code Relation}, with the value
   *     of
   *     <i>this</i> {@code ComposablePredicate} now becoming the <i>subject</i> of
   *     that relation
   * @param <O> the type of the object of the provided {@code Relation}
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> orNot(Relation<?, O> relation,
      O object) {
    checkArg(relation);
    return x -> meFirst(x) || !((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test or if it has a particular relation to
   * the specified set of values.
   *
   * @param relation the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects the set of values to test the value against
   * @param <O> the type of the object of the relation
   * @param <P> the type of the values fed as "objects" into the relation
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, P extends O, V> ComposablePredicate<V> or(Relation<V, O> relation,
      Quantifier quantifier,
      P... objects) {
    checkArgs(relation, quantifier, objects);
    return x -> meFirst(x)
        || testAgainstArray(x, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().orThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value being tested by the specified predicate
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> orThat(U value, Predicate<U> test) {
    checkArg(test);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orThat(S subject,
      Relation<S, O> relation,
      O object) {
    checkArg(relation);
    return x -> meFirst(x) || relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the negation of the other
   * test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().orThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value being tested by the specified predicate
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> orNot(U value, Predicate<U> test) {
    checkArg(test);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orNot(S subject,
      Relation<S, O> relation,
      O object) {
    checkArg(relation);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, P extends O, V> ComposablePredicate<V> or(S subject,
      Relation<S, O> relation, Quantifier quantifier, P... objects) {
    checkArgs(relation, quantifier, objects);
    Private.checkObjects(objects);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> or(int subject,
      IntRelation relation, Quantifier quantifier, int... objects) {
    checkArgs(relation, quantifier, objects);
    checkObjects(objects);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> or(boolean test) {
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> orEval(Supplier<Boolean> test) {
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andAlso(Predicate<?> test) {
    checkArg(test);
    return x -> meFirst(x) && ((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test and if it has the specified relation
   * to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the relation, with the value being tested now
   *     becoming the subject of the relation
   * @param <O> the type of the object of the provided {@code Relation}
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> andAlso(Relation<?, O> relation, O object) {
    checkArg(relation);
    return x -> meFirst(x) && ((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A
   * value will pass the new test if it passes <i>this</i> test and if the provided
   * expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass
   *     this test
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> and(boolean test) {
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
   * @param <V> the type of the value that is tested by the returned
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andNot(Predicate<?> test) {
    checkArg(test);
    return x -> meFirst(x) && !((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test and if it does not have the specified
   * relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object the object of the relation, with the value being tested now
   *     becoming the subject of the relation
   * @param <O> the type of the object of the provided {@code Relation}
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> andNot(Relation<?, O> relation, O object) {
    checkArg(relation);
    return x -> meFirst(x) && !((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass
   * the new test if it passes <i>this</i> test and if it has a particular relation
   * to the specified set of values.
   *
   * @param relation the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects the set of values to test the value against
   * @param <O> the type of the object of the relation
   * @param <P> the type of the values fed as "objects" into the relation
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, P extends O, V> ComposablePredicate<V> and(Relation<V, O> relation,
      Quantifier quantifier,
      P... objects) {
    checkArgs(relation, quantifier, objects);
    return x -> meFirst(x)
        && testAgainstArray(x, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().andThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value being tested by the specified predicate
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> andThat(U value, Predicate<U> test) {
    checkArg(test);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andThat(S subject,
      Relation<S, O> relation,
      O object) {
    checkArg(relation);
    return x -> meFirst(x) && relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two
   * checks on two different values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the negation of the other
   * test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().andThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test the test to combine this test with
   * @param <U> the type of the value being tested by the specified predicate
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> andNot(U value, Predicate<U> test) {
    checkArg(test);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andNot(S subject,
      Relation<S, O> relation,
      O object) {
    checkArg(relation);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, P extends O, V> ComposablePredicate<V> and(S subject,
      Relation<S, O> relation, Quantifier quantifier, P... objects) {
    checkArgs(relation, quantifier, objects);
    return x -> meFirst(x) &&
        testAgainstArray(subject, relation, quantifier, objects);
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
   * @param <V> the type of the value that is tested by the returned
   *     {@code ComposablePredicate}. Note that in actual fact, that really is the
   *     type of the value being tested by <i>this</i> {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> and(int subject,
      IntRelation relation, Quantifier quantifier, int... objects) {
    checkArgs(relation, quantifier, objects);
    return x -> meFirst(x)
        && testAgainstArray(subject, relation, quantifier, objects);
  }

  private <V> boolean meFirst(V v) {
    return ComposablePredicate.this.test((T) v);
  }

}
