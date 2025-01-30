package org.klojang.check.extra;

import org.klojang.check.CommonChecks;

/**
 * Marks objects that can meaningfully be said to be empty. An example could be a POJO with all fields set to
 * their default values. Classes that implement this interface get a free ride on the
 * {@link CommonChecks#empty() empty()}, {@link CommonChecks#notEmpty() notEmpty()} and
 * {@link CommonChecks#deepNotEmpty() deepNotEmpty()} checks from the {@code CommonChecks} class. That is,
 * instances of {@code Emptyable} can be tested using:
 *
 * <blockquote><pre>{@code
 * Check.that(myPojo).isNot(empty());
 * }</pre></blockquote>
 */
@FunctionalInterface
public interface Emptyable {

  /**
   * Returns {@code true} if this is an empty instance.
   *
   * @return {@code true} if this is an empty instance
   */
  boolean isEmpty();

  /**
   * Returns {@code true} if this is a recursively non-empty instance. The default implementation returns the
   * negation of {@link #isEmpty()}.
   *
   * @return {@code true} if this is a recursively non-empty instance
   */
  default boolean isDeepNotEmpty() {
    return !isEmpty();
  }

}
