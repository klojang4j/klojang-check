package org.klojang.check.relation;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

final class Private {

  private static final String TEST_MUST_NOT_BE_NULL = "test must not be null";
  private static final String RELATION_MUST_NOT_BE_NULL = "relation must not be null";
  private static final String QUANTIFIER_MUST_NOT_BE_NULL = "quantifier must not be null";
  private static final String ERR_NO_OBJECT = "at least one object required";
  private static final String ARRAY_MUST_NOT_BE_NULL = "array must not be null";

  static <S, O, P extends O> boolean testAgainstArray(S subject,
      Relation<S, O> relation,
      Quantifier quantifier,
      P[] objects) {
    return switch (quantifier) {
      case ALL -> stream(objects).allMatch(o -> relation.exists(subject, o));
      case ANY -> stream(objects).anyMatch(o -> relation.exists(subject, o));
      case NONE -> stream(objects).noneMatch(o -> relation.exists(subject, o));
    };
  }

  static boolean testAgainstArray(int subject,
      IntRelation relation,
      Quantifier quantifier,
      int[] objects) {
    return switch (quantifier) {
      case ALL -> stream(objects).allMatch(o -> relation.exists(subject, o));
      case ANY -> stream(objects).anyMatch(o -> relation.exists(subject, o));
      case NONE -> stream(objects).noneMatch(o -> relation.exists(subject, o));
    };
  }

}
