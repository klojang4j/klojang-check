package nl.naturalis.check.types;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public interface ComposablePredicate<T> extends Predicate<T> {

  default ComposablePredicate<T> or(ComposablePredicate<T> test) {
    Objects.requireNonNull(test);
    return x -> callMe(x) || test.test(x);
  }

  default <U> ComposablePredicate<T> or(U value, ComposablePredicate<U> test) {
    Objects.requireNonNull(test, Util.TEST_MUST_NOT_BE_NULL);
    return x -> callMe(x) || test.test(value);
  }

  default ComposablePredicate<T> or(int value, ComposableIntPredicate test) {
    Objects.requireNonNull(test, Util.TEST_MUST_NOT_BE_NULL);
    return x -> callMe(x) || test.test(value);
  }

  default <U> ComposablePredicate<T> or(Relation<T, U> relation, U value) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    return x -> callMe(x) || relation.exists(x, value);
  }

  default <U> ComposablePredicate<T> or(Relation<T, U> relation,
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

  default ComposablePredicate<T> or(ObjIntRelation<T> relation, int value) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    return x -> callMe(x) || relation.exists(x, value);
  }

  default ComposablePredicate<T> or(ObjIntRelation<T> relation,
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

  default <U> ComposablePredicate<T> and(ComposablePredicate<T> test) {
    Objects.requireNonNull(test);
    return x -> callMe(x) && test.test(x);
  }

  default <U> ComposablePredicate<T> and(Relation<T, U> relation, U value) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    return x -> callMe(x) && relation.exists(x, value);
  }

  default <U> ComposablePredicate<T> and(Relation<T, U> relation,
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

  default ComposablePredicate<T> and(ObjIntRelation<T> relation, int value) {
    Objects.requireNonNull(relation, Util.RELATION_MUST_NOT_BE_NULL);
    return x -> callMe(x) && relation.exists(x, value);
  }

  default ComposablePredicate<T> and(ObjIntRelation<T> relation,
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

  private boolean callMe(T x) {
    return ComposablePredicate.this.test(x);
  }

}
