package org.klojang.check.fallible;

import java.util.function.ToIntFunction;

/**
 * An alternative to the {@link ToIntFunction} interface that allows the functional
 * method to throw a checked exception.
 *
 * @param <T> The type of the input variable
 * @param <X> The type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface FallibleToIntFunction<T, X extends Throwable> {

  /**
   * Calculates a value for the provided argument.
   *
   * @param arg the input variable
   * @return an {@code int} value
   * @throws X the exception potentially being thrown
   */
  int apply(T arg) throws X;

}
