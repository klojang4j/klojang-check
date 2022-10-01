package nl.naturalis.common.function;

import nl.naturalis.check.Check;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static nl.naturalis.check.CommonChecks.notNull;

public interface ComposableIntPredicate extends IntPredicate {

  default ComposableIntPredicate or(ComposableIntPredicate test) {
    Check.that(test).is(notNull());
    return x -> callMe(x) || test.test(x);
  }

  default ComposableIntPredicate or(IntRelation relation, int value) {
    Check.that(relation).is(notNull());
    return x -> callMe(x) || relation.exists(x, value);
  }

  default ComposableIntPredicate or(IntRelation relation,
      int val0,
      int val1,
      int... moreValues) {
    Check.that(relation).is(notNull()).and(moreValues).is(notNull());
    return x -> callMe(x)
        || relation.exists(x, val0)
        || relation.exists(x, val1)
        || Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default <U> ComposableIntPredicate or(IntObjRelation<U> relation, U value) {
    Check.that(relation).is(notNull());
    return x -> callMe(x) || relation.exists(x, value);
  }

  default <U> ComposableIntPredicate or(IntObjRelation<U> relation,
      U val0,
      U val1,
      U... moreValues) {
    Check.that(relation).is(notNull()).and(moreValues).is(notNull());
    return x -> callMe(x)
        || relation.exists(x, val0)
        || relation.exists(x, val1)
        || Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default ComposableIntPredicate and(ComposableIntPredicate test) {
    Check.that(test).is(notNull());
    return x -> callMe(x) && test.test(x);
  }

  default ComposableIntPredicate and(IntRelation relation, int value) {
    Check.that(relation).is(notNull());
    return x -> callMe(x) && relation.exists(x, value);
  }

  default ComposableIntPredicate and(IntRelation relation,
      int val0,
      int val1,
      int... moreValues) {
    Check.that(relation).is(notNull()).and(moreValues).is(notNull());
    return x -> callMe(x)
        && relation.exists(x, val0)
        && relation.exists(x, val1)
        && Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default <U> ComposableIntPredicate and(IntObjRelation<U> relation, U value) {
    Check.that(relation).is(notNull());
    return x -> callMe(x) && relation.exists(x, value);
  }

  default <U> ComposableIntPredicate and(IntObjRelation<U> relation,
      U val0,
      U val1,
      U... moreValues) {
    Check.that(relation).is(notNull()).and(moreValues).is(notNull());
    return x -> callMe(x)
        && relation.exists(x, val0)
        && relation.exists(x, val1)
        && Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  private boolean callMe(int x) {
    return ComposableIntPredicate.this.test(x);
  }

}
