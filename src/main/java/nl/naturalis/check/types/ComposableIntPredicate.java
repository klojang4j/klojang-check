package nl.naturalis.check.types;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntPredicate;

public interface ComposableIntPredicate extends IntPredicate {

  default ComposableIntPredicate or(ComposableIntPredicate test) {
    Objects.requireNonNull(test);
    return x -> callMe(x) || test.test(x);
  }

  default ComposableIntPredicate or(IntRelation relation, int value) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    return x -> callMe(x) || relation.exists(x, value);
  }

  default ComposableIntPredicate or(IntRelation relation,
      int val0,
      int val1,
      int... moreValues) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    Objects.requireNonNull(moreValues, Util.VARARGS_MUST_NOT_BE_NULL);
    return x -> callMe(x)
        || relation.exists(x, val0)
        || relation.exists(x, val1)
        || Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default <U> ComposableIntPredicate or(IntObjRelation<U> relation, U value) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    return x -> callMe(x) || relation.exists(x, value);
  }

  default <U> ComposableIntPredicate or(IntObjRelation<U> relation,
      U val0,
      U val1,
      U... moreValues) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    Objects.requireNonNull(moreValues, Util.VARARGS_MUST_NOT_BE_NULL);
    return x -> callMe(x)
        || relation.exists(x, val0)
        || relation.exists(x, val1)
        || Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default ComposableIntPredicate and(ComposableIntPredicate test) {
    Objects.requireNonNull(test);
    return x -> callMe(x) && test.test(x);
  }

  default ComposableIntPredicate and(IntRelation relation, int value) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    return x -> callMe(x) && relation.exists(x, value);
  }

  default ComposableIntPredicate and(IntRelation relation,
      int val0,
      int val1,
      int... moreValues) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    Objects.requireNonNull(moreValues, Util.VARARGS_MUST_NOT_BE_NULL);
    return x -> callMe(x)
        && relation.exists(x, val0)
        && relation.exists(x, val1)
        && Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  default <U> ComposableIntPredicate and(IntObjRelation<U> relation, U value) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    return x -> callMe(x) && relation.exists(x, value);
  }

  default <U> ComposableIntPredicate and(IntObjRelation<U> relation,
      U val0,
      U val1,
      U... moreValues) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    Objects.requireNonNull(moreValues, Util.VARARGS_MUST_NOT_BE_NULL);
    return x -> callMe(x)
        && relation.exists(x, val0)
        && relation.exists(x, val1)
        && Arrays.stream(moreValues).anyMatch(u -> relation.exists(x, u));
  }

  private boolean callMe(int x) {
    return ComposableIntPredicate.this.test(x);
  }

}
