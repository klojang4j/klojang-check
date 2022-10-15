package nl.naturalis.check.types;

import nl.naturalis.check.util.Quantifier;

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

  static void checkArg(Predicate<?> test) {
    requireNonNull(test, TEST_MUST_NOT_BE_NULL);
  }

  static void checkArg(IntPredicate test) {
    requireNonNull(test, TEST_MUST_NOT_BE_NULL);
  }

  static void checkArg(Relation<?, ?> relation) {
    requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
  }

  static void checkArg(IntObjRelation<?> relation) {
    requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
  }

  static void checkArg(IntRelation relation) {
    requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
  }

  static void checkArgs(Relation<?, ?> relation,
      Quantifier quantifier,
      Object[] objects) {
    requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    requireNonNull(quantifier, QUANTIFIER_MUST_NOT_BE_NULL);
    if (requireNonNull(objects, ARRAY_MUST_NOT_BE_NULL).length == 0) {
      throw new IllegalArgumentException(ERR_NO_OBJECT);
    }
  }

  static void checkArgs(IntRelation relation,
      Quantifier quantifier,
      int[] objects) {
    requireNonNull(relation, RELATION_MUST_NOT_BE_NULL);
    requireNonNull(quantifier, QUANTIFIER_MUST_NOT_BE_NULL);
    if (requireNonNull(objects, ARRAY_MUST_NOT_BE_NULL).length == 0) {
      throw new IllegalArgumentException(ERR_NO_OBJECT);
    }
  }

  static void checkObjects(int[] values) {
    if (requireNonNull(values, ARRAY_MUST_NOT_BE_NULL).length == 0) {
      throw new IllegalArgumentException(ERR_NO_OBJECT);
    }
  }

  static void checkObjects(Object[] values) {
    if (requireNonNull(values, ARRAY_MUST_NOT_BE_NULL).length == 0) {
      throw new IllegalArgumentException(ERR_NO_OBJECT);
    }
  }

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
