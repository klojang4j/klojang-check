package nl.naturalis.common;

import nl.naturalis.check.CommonChecks;

/**
 * Marks objects that can meaningfully be said to be empty. For example a POJO will
 * all fields set to their default values. The
 * {@link ObjectMethods#isEmpty(Object) isEmpty} and
 * {@link ObjectMethods#isDeepNotEmpty(Object) isDeepNotEmpty} methods will test
 * whether an object implements this interface in order to decide if it is an empty
 * object.
 *
 * @author Ayco Holleman
 * @see CommonChecks#empty()
 * @see CommonChecks#deepNotEmpty()
 * @see ObjectMethods#isEmpty(Emptyable)
 * @see ObjectMethods#isDeepNotEmpty(Object)
 */
@FunctionalInterface
public interface Emptyable {

  /**
   * A universally empty {@code Emptyable}.
   */
  static final Emptyable NOTHINGNESS = () -> true;

  /**
   * A universally non-empty {@code Emptyable}.
   */
  static final Emptyable POPULATED = () -> false;

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
