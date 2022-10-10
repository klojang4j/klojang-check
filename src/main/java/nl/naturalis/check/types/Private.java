package nl.naturalis.check.types;

import java.util.Collection;
import java.util.Objects;

final class Private {

  static final String TEST_MUST_NOT_BE_NULL = "test must not be null";
  static final String RELATION_MUST_NOT_BE_NULL = "relation must not be null";
  static final String VARARGS_MUST_NOT_BE_NULL = "array must not be null";

  private static final String AT_LEAST_ONE_SUBJECT_REQUIRED = "at least one subject required";

  static void checkSubjects(Collection<?> values) {
    if (Objects.requireNonNull(values, "collection must not be null").size() == 0) {
      throw new IllegalArgumentException(AT_LEAST_ONE_SUBJECT_REQUIRED);
    }
  }

  static void checkSubjects(int[] values) {
    if (Objects.requireNonNull(values, "array must not be null").length == 0) {
      throw new IllegalArgumentException(AT_LEAST_ONE_SUBJECT_REQUIRED);
    }
  }

}
