package nl.naturalis.common.function;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.util.Arrays;
import java.util.function.Predicate;

import static nl.naturalis.check.CommonChecks.notNull;

public interface ComposablePredicate<T> extends Predicate<T> {

  default ComposablePredicate<T> or(ComposablePredicate<T> test) {
    Check.that(test).is(notNull());
    return x -> callMe(x) || test.test(x);
  }

  default <U> ComposablePredicate<T> or(U value, ComposablePredicate<U> test) {
    Check.that(test).is(notNull());
    return x -> callMe(x) || test.test(value);
  }

  default ComposablePredicate<T> or(int value, ComposableIntPredicate test) {
    Check.that(test).is(notNull());
    return x -> callMe(x) || test.test(value);
  }

  default <U> ComposablePredicate<T> or(Relation<T, U> relation, U value) {
    Check.that(relation).is(notNull());
    return x -> callMe(x) || relation.exists(x, value);
  }

  default <U> ComposablePredicate<T> or(Relation<T, U> relation,
      U val0,
      U val1,
      U... moreValues) {
    Check.that(relation).is(notNull()).and(moreValues).is(notNull());
    return x -> callMe(x)
        || relation.exists(x, val0)
        || relation.exists(x, val1)
        || Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default ComposablePredicate<T> or(ObjIntRelation<T> relation, int value) {
    Check.that(relation).is(notNull());
    return x -> callMe(x) || relation.exists(x, value);
  }

  default ComposablePredicate<T> or(ObjIntRelation<T> relation,
      int val0,
      int val1,
      int... moreValues) {
    Check.that(relation).is(notNull()).and(moreValues).is(notNull());
    return x -> callMe(x)
        || relation.exists(x, val0)
        || relation.exists(x, val1)
        || Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default <U> ComposablePredicate<T> and(ComposablePredicate<T> test) {
    Check.that(test).is(notNull());
    return x -> callMe(x) && test.test(x);
  }

  default <U> ComposablePredicate<T> and(Relation<T, U> relation, U value) {
    Check.that(relation).is(notNull());
    return x -> callMe(x) && relation.exists(x, value);
  }

  default <U> ComposablePredicate<T> and(Relation<T, U> relation,
      U val0,
      U val1,
      U... moreValues) {
    Check.that(relation).is(notNull()).and(moreValues).is(notNull());
    return x -> callMe(x)
        && relation.exists(x, val0)
        && relation.exists(x, val1)
        && Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default ComposablePredicate<T> and(ObjIntRelation<T> relation, int value) {
    Check.that(relation).is(notNull());
    return x -> callMe(x) && relation.exists(x, value);
  }

  default ComposablePredicate<T> and(ObjIntRelation<T> relation,
      int val0,
      int val1,
      int... moreValues) {
    Check.that(relation).is(notNull()).and(moreValues).is(notNull());
    return x -> callMe(x)
        && relation.exists(x, val0)
        && relation.exists(x, val1)
        && Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  private boolean callMe(T x) {
    return ComposablePredicate.this.test(x);
  }

}
