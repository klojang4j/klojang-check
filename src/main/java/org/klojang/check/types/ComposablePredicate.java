package org.klojang.check.types;

import org.klojang.check.Check;
import org.klojang.check.CommonChecks;

import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.klojang.check.types.Private.testAgainstArray;

/**
 * An extension of {@link Predicate} that acts as a bridge between {@code Predicate} and the relational
 * interfaces in this package. It enables the assembly of new tests from any number of instances of
 * {@link Predicate}, {@link IntPredicate}, {@link Relation}, {@link IntRelation} and {@link IntObjRelation}.
 *
 * <h2>AND vs. OR Compositions</h2>
 *
 * <p>Generally, you will have more use for compositions expressing a logical
 * disjunction (OR), because the chain of checks following {@link Check#that(Object) Check.that(...)} already
 * constitutes a logical conjunction (AND). For example, the following statement:
 *
 * <blockquote><pre>{@code
 * Check.that(numChairs).is(positive()).is(lt(), 4).is(even());
 * }</pre></blockquote>
 *
 * <p>requires the number of chairs to be positive <b>and</b> less than four  <b>and</b> even. If the number
 * of chairs needs to pass just one of these tests, write:
 *
 * <blockquote><pre>{@code
 * Check.that(numChairs).is(positive().orElse(lt(), 4).orElse(even()));
 * }</pre></blockquote>
 *
 * <p>Nevertheless, you might still want to use the {@code and()} methods for conciseness:
 *
 * <blockquote><pre>{@code
 * Check.that(string).is(notNull().and(hasSubstring(), allOf(), "to", "be", "or", "not"));
 * // is equivalent to:
 * Check.that(string).is(notNull())
 *    .is(hasSubstring(), "to")
 *    .is(hasSubstring(), "be")
 *    .is(hasSubstring(), "or")
 *    .is(hasSubstring(), "not")
 * }</pre></blockquote>
 *
 * <p>(See {@link Quantifier} for the {@code allOf()} argument.)
 *
 * <h2>Validating Interrelated Values</h2>
 *
 * <p>Sometimes, an argument, field or variable cannot be tested in isolation. Its
 * validity depends on the value of another argument, field or variable:
 *
 * <blockquote><pre>{@code
 * Check.that(engine.ready()).is(yes().or(buffer.size(), eq(), 0);
 * }</pre></blockquote>
 *
 * <p>In the above example, the engine only needs to be ready if there is more data
 * in the buffer.
 *
 * <p>Notice that the second check continues nicely in the idiom of Klojang Check,
 * even though it is now just syntactic sugar. Depending on your taste you can also just write:
 *
 * <blockquote><pre>{@code
 * Check.that(engine.ready()).is(yes().or(buffer.size() == 0);
 * }</pre></blockquote>
 *
 * <h2>Generics</h2>
 *
 * <p>Even though the type parameter for {@code ComposablePredicate} is {@code <T>},
 * the type parameter for the predicates and relations passed to the {@code and()} and {@code or()} methods is
 * simply {@code <?>}. This allows checks like {@link CommonChecks#notNull() notNull()} and
 * {@link CommonChecks#notEmpty() notEmpty()}, which can be applied to any non-primitive type, to be followed
 * by checks that can only be applied to a specific type. For example, the following code would <i>not</i>
 * compile if the argument to {@code andAlso()} were {@code Predicate<? super T>} instead of
 * {@code Predicate<?>}:
 *
 * <blockquote><pre>{@code
 * Check.that(file).is(empty().andAlso(writable()));
 * }</pre></blockquote>
 *
 * <p>The downside is that it is easier for a composition of tests to harbor a type
 * error without the compiler noticing it, resulting in a {@link ClassCastException} at runtime. For example,
 * the following nonsensical statement compiles just fine:
 *
 * <blockquote><pre>{@code
 * Check.that(list).is(empty().andAlso(writable()));
 * }</pre></blockquote>
 *
 * <p> In addition, when using lambdas, you will now have to specify the type of the
 * lambda parameter:
 *
 * <blockquote><pre>{@code
 * Check.that(file).is(empty().andAlso(f -> f.canWrite())); // WON'T COMPILE!!
 * Check.that(file).is(empty().andAlso((File f) -> f.canWrite())); // OK
 * }</pre></blockquote>
 *
 * <p>If you are not comfortable with this, you can instead use the
 * {@link #orThat(Object, Predicate) orThat()} method and repeat the argument for every call to
 * {@code orThat()}:
 *
 * <blockquote><pre>{@code
 * Check.that(list).is(empty().orThat(list, contains(), "foo"));
 * }</pre></blockquote>
 *
 * <p>Note, however, that the {@code orThat()} method is primarily meant to test <i>different</i>
 * (but interrelated) values.
 *
 * @param <T> the type of the value being tested
 * @see Relation
 * @see IntRelation
 * @see IntObjRelation
 * @see ObjIntRelation
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@FunctionalInterface
public interface ComposablePredicate<T> extends Predicate<T> {

  /**
   * Returns the negation of this predicate.
   *
   * @return the negation of this predicate
   * @see Predicate#negate()
   */
  default ComposablePredicate<T> negated() {
    return x -> !meFirst(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes <i>this</i> test or the specified test. Note that this method is meant to be "just another
   * overload" or the other {@code or()} methods in this interface, except that in this case we would be
   * overriding {@link Predicate#or(Predicate)}. To avoid confusion and unintended behavior, it is best to
   * keep the two methods separate.
   *
   * @param test the test to combine this test with
   * @param <V>  the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *             in actual fact, that really is the type of the value being tested by <i>this</i>
   *             {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orElse(Predicate<?> test) {
    return x -> meFirst(x) || ((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes <i>this</i> test or if it has the specified relation to the specified value.
   *
   * <blockquote><pre>{@code
   * Check.that("foo bar").is(empty().or(hasSubstring(), "foo"));
   * }</pre></blockquote>
   *
   * @param relation the relationship test to combine this test with
   * @param object   the object of the specified relation, with the value of this {@code ComposablePredicate}
   *                 now becoming the subject of that relation
   * @param <O>      the type of the object of the provided {@code Relation}
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> or(Relation<?, O> relation, O object) {
    return x -> meFirst(x) || ((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes <i>this</i> test or if it has a particular relation to the specified set of values.
   *
   * @param relation   the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects    the set of values to test the value against
   * @param <O>        the type of the object of the relation
   * @param <P>        the type of the values fed as "objects" into the relation
   * @param <V>        the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                   that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                   {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, P extends O, V> ComposablePredicate<V> or(Relation<V, O> relation,
      Quantifier quantifier,
      P... objects) {
    return x -> meFirst(x) || testAgainstArray(x, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test or if <i>another</i> value ({@code subject}) has a particular
   * relation to the specified set of values.
   *
   * @param subject    the subject of the specified {@code Relation}
   * @param relation   the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects    the set of values to test the subject against
   * @param <S>        the type of the subject of the relation
   * @param <O>        the type of the object of the relation
   * @param <P>        the type of the values fed as "objects" into the relation
   * @param <V>        the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                   that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                   {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, P extends O, V> ComposablePredicate<V> or(S subject,
      Relation<S, O> relation,
      Quantifier quantifier,
      P... objects) {
    return x -> meFirst(x) || testAgainstArray(subject, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test and if <i>another</i> value ({@code subject}) has a particular
   * relation to the specified set of values.
   *
   * @param subject    the subject of the relation
   * @param relation   the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects    the set of values to test the subject against
   * @param <V>        the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                   that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                   {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> or(int subject,
      IntRelation relation,
      Quantifier quantifier,
      int... objects) {
    return x -> meFirst(x) || testAgainstArray(subject, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A value will pass the new test
   * if it passes <i>this</i> test or if the provided expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass this test
   * @param <V>  the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *             in actual fact, that really is the type of the value being tested by <i>this</i>
   *             {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> or(boolean test) {
    return x -> meFirst(x) || test;
  }

  /**
   * Returns a new test combining this test with the free-form test supplied by the specified
   * {@code Supplier}. A value will pass the new test if it passes
   * <i>this</i> test or if the expression supplied by the {@code Supplier}
   * evaluates to {@code true}. The supplier's {@link Supplier#get() get()} method will only be called if the
   * value fails to pass this test. Useful if evaluating the expression could be expensive.
   *
   * @param test the supplier of a boolean expression
   * @param <V>  the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *             in actual fact, that really is the type of the value being tested by <i>this</i>
   *             {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> or(Supplier<Boolean> test) {
    return x -> meFirst(x) || test.get();
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes <i>this</i> test or the negation of the specified test.
   *
   * @param test the test to combine this test with
   * @param <V>  the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *             in actual fact, that really is the type of the value being tested by <i>this</i>
   *             {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orNot(Predicate<?> test) {
    return x -> meFirst(x) || !((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes <i>this</i> test or if it does not have the specified relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object   the object of the provided {@code Relation}, with the value of
   *                 <i>this</i> {@code ComposablePredicate} now becoming the <i>subject</i> of
   *                 that relation
   * @param <O>      the type of the object of the provided {@code Relation}
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> orNot(Relation<?, O> relation, O object) {
    return x -> meFirst(x) || !((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().orThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test  the test to combine this test with
   * @param <U>   the type of the value being tested by the specified predicate
   * @param <V>   the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *              in actual fact, that really is the type of the value being tested by <i>this</i>
   *              {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> orThat(U value, Predicate<U> test) {
    return x -> meFirst(x) || test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().orThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test  the test to combine this test with
   * @param <V>   the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *              in actual fact, that really is the type of the value being tested by <i>this</i>
   *              {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orThat(int value, IntPredicate test) {
    return x -> meFirst(x) || test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
   *
   * @param subject  the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object   the object of the specified relation
   * @param <S>      the type of the subject of the specified relation
   * @param <O>      the type of the object of the specified relation
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orThat(S subject,
      Relation<S, O> relation,
      O object) {
    return x -> meFirst(x) || relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the other test.
   *
   * @param subject  the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object   the object of the specified relation
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> orThat(int subject,
      IntRelation relation,
      int object) {
    return x -> meFirst(x) || relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the negation of the other
   * test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().orThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test  the test to combine this test with
   * @param <U>   the type of the value being tested by the specified predicate
   * @param <V>   the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *              in actual fact, that really is the type of the value being tested by <i>this</i>
   *              {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> orNot(U value, Predicate<U> test) {
    return x -> meFirst(x) || !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test or if another value manages to pass the negation of the other
   * test.
   *
   * @param subject  the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object   the object of the specified relation
   * @param <S>      the type of the subject of the specified relation
   * @param <O>      the type of the object of the specified relation
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> orNot(S subject,
      Relation<S, O> relation,
      O object) {
    return x -> meFirst(x) || !relation.exists(subject, object);
  }

  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////// [AND methods] ////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes both this test and the specified test.
   *
   * @param test the test to combine this test with
   * @param <V>  the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *             in actual fact, that really is the type of the value being tested by <i>this</i>
   *             {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andAlso(Predicate<?> test) {
    return x -> meFirst(x) && ((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes <i>this</i> test and if it has the specified relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object   the object of the relation, with the value being tested now becoming the subject of the
   *                 relation
   * @param <O>      the type of the object of the provided {@code Relation}
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> andAlso(Relation<?, O> relation, O object) {
    return x -> meFirst(x) && ((Relation) relation).exists(x, object);
  }

  /**
   * Returns a new test combining this test with the specified free-form test. A value will pass the new test
   * if it passes <i>this</i> test and if the provided expression evaluates to {@code true}.
   *
   * @param test the boolean expression to evaluate if the value fails to pass this test
   * @param <V>  the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *             in actual fact, that really is the type of the value being tested by <i>this</i>
   *             {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> and(boolean test) {
    return x -> meFirst(x) && test;
  }

  /**
   * Returns a new test combining this test with the free-form test supplied by the specified
   * {@code Supplier}. A value will pass the new test if it passes
   * <i>this</i> test and if the expression supplied by the {@code Supplier}
   * evaluates to {@code true}. The supplier's {@link Supplier#get() get()} method will only be called if the
   * value passes this test. Useful if evaluating the boolean expression could be expensive.
   *
   * @param test the supplier of a boolean expression
   * @param <V>  the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *             in actual fact, that really is the type of the value being tested by <i>this</i>
   *             {@code ComposablePredicate}.
   * @return a new test combining this test and the specified free-form test
   */
  default <V> ComposablePredicate<V> and(Supplier<Boolean> test) {
    return x -> meFirst(x) && test.get();
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes <i>this</i> test and if it has a particular relation to the specified set of values.
   *
   * @param relation   the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects    the set of values to test the value against
   * @param <O>        the type of the object of the relation
   * @param <P>        the type of the values fed as "objects" into the relation
   * @param <V>        the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                   that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                   {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, P extends O, V> ComposablePredicate<V> and(Relation<V, O> relation,
      Quantifier quantifier,
      P... objects) {
    return x -> meFirst(x) && testAgainstArray(x, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test and if <i>another</i> value ({@code subject}) has a particular
   * relation to the specified set of values.
   *
   * @param subject    the subject of the specified {@code Relation}
   * @param relation   the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects    the set of values to test the subject against
   * @param <S>        the type of the subject of the relation
   * @param <O>        the type of the object of the relation
   * @param <P>        the type of the values fed as "objects" into the relation
   * @param <V>        the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                   that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                   {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, P extends O, V> ComposablePredicate<V> and(S subject,
      Relation<S, O> relation, Quantifier quantifier, P... objects) {
    return x -> meFirst(x) && testAgainstArray(subject, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test and if <i>another</i> value ({@code subject}) has a particular
   * relation to the specified set of values.
   *
   * @param subject    the subject of the relation
   * @param relation   the relationship test to combine this test with
   * @param quantifier a logical quantifier modulating the relationship
   * @param objects    the set of values to test the subject against
   * @param <V>        the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                   that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                   {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> and(int subject,
      IntRelation relation, Quantifier quantifier, int... objects) {
    return x -> meFirst(x) && testAgainstArray(subject, relation, quantifier, objects);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes both this test and the negation of the specified test.
   *
   * @param test the test to combine this test with
   * @param <V>  the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *             in actual fact, that really is the type of the value being tested by <i>this</i>
   *             {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andNot(Predicate<?> test) {
    return x -> meFirst(x) && !((Predicate) test).test(x);
  }

  /**
   * Returns a new test combining this test and the specified test. A value will pass the new test if it
   * passes <i>this</i> test and if it does not have the specified relation to the specified value.
   *
   * @param relation the relationship test to combine this test with
   * @param object   the object of the relation, with the value being tested now becoming the subject of the
   *                 relation
   * @param <O>      the type of the object of the provided {@code Relation}
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <O, V> ComposablePredicate<V> andNot(Relation<?, O> relation, O object) {
    return x -> meFirst(x) && !((Relation) relation).exists(x, object);
  }


  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes <i>this</i> test and if another value pass another
   * test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().andThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test  the test to combine this test with
   * @param <U>   the type of the value being tested by the specified predicate
   * @param <V>   the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *              in actual fact, that really is the type of the value being tested by <i>this</i>
   *              {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> andThat(U value, Predicate<U> test) {
    return x -> meFirst(x) && test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes <i>this</i> test and if another value pass another
   * test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().andThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test  the test to combine this test with
   * @param <V>   the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *              in actual fact, that really is the type of the value being tested by <i>this</i>
   *              {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andThat(int value, IntPredicate test) {
    return x -> meFirst(x) && test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes <i>this</i> test and if another value pass another
   * test.
   *
   * @param subject  the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object   the object of the specified relation
   * @param <S>      the type of the subject of the specified relation
   * @param <O>      the type of the object of the specified relation
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andThat(S subject,
      Relation<S, O> relation,
      O object) {
    return x -> meFirst(x) && relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes <i>this</i> test and if another value pass another
   * test.
   *
   * @param subject  the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object   the object of the specified relation
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <V> ComposablePredicate<V> andThat(int subject,
      IntRelation relation,
      int object) {
    return x -> meFirst(x) && relation.exists(subject, object);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the negation of the other
   * test.
   *
   * <blockquote><pre>{@code
   * Check.that(file1).is(readable().andThat(file2, writable()));
   * }</pre></blockquote>
   *
   * @param value the value to be tested by the specified test
   * @param test  the test to combine this test with
   * @param <U>   the type of the value being tested by the specified predicate
   * @param <V>   the type of the value that is tested by the returned {@code ComposablePredicate}. Note that
   *              in actual fact, that really is the type of the value being tested by <i>this</i>
   *              {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <U, V> ComposablePredicate<V> andNot(U value, Predicate<U> test) {
    return x -> meFirst(x) && !test.test(value);
  }

  /**
   * Returns a new test combining this test and the specified test. It combines two checks on two different
   * values. A value will pass the new test if it passes
   * <i>this</i> test and if another value manages to pass the negation of the other
   * test.
   *
   * @param subject  the subject of the specified relation
   * @param relation the relationship test to combine this test with
   * @param object   the object of the specified relation
   * @param <S>      the type of the subject of the specified relation
   * @param <O>      the type of the object of the specified relation
   * @param <V>      the type of the value that is tested by the returned {@code ComposablePredicate}. Note
   *                 that in actual fact, that really is the type of the value being tested by <i>this</i>
   *                 {@code ComposablePredicate}.
   * @return a new test combining this test and the specified test
   */
  default <S, O, V> ComposablePredicate<V> andNot(S subject,
      Relation<S, O> relation,
      O object) {
    return x -> meFirst(x) && !relation.exists(subject, object);
  }

  private <V> boolean meFirst(V v) {
    return ComposablePredicate.this.test((T) v);
  }

}
