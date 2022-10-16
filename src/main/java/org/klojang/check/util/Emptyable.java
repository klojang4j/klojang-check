package org.klojang.check.util;

import java.lang.runtime.ObjectMethods;

import org.klojang.check.CommonChecks;

/**
 * Marks objects that can meaningfully be said to be empty. For example a POJO with
 * all fields set to their default values. Classes that implement this interface get
 * a free ride on the {@link CommonChecks#empty() empty()} and
 * {@link CommonChecks#deepNotEmpty() deepNotEmpty()} checks.
 */
@FunctionalInterface
public interface Emptyable {

  /**
   * Returns {@code true} if the object can be said to be empty.
   *
   * @return {@code true} if the object can be said to be empty
   */
  boolean isEmpty();

  /**
   * Returns {@code true} if the object is recursively non-empty. The default
   * implementation returns the negation of {@link #isEmpty()}.
   *
   * @return {@code true} if the object is recursively non-empty
   */
  default boolean isDeepNotEmpty() {
    return !isEmpty();
  }

}
