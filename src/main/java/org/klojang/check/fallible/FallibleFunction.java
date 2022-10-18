package org.klojang.check.fallible;

import java.util.function.Function;

/**
 * An alternative to the {@link Function} interface that allows the functional
 * method to throw a checked exception.
 *
 * @param <T> the type of the input variable
 * @param <R> the type of the return value
 * @param <X> the type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface FallibleFunction<T, R, X extends Throwable> {

  /**
   * Calculates a value for the provided argument.
   *
   * @param arg the input variable
   * @return a value of type {@code R}
   * @throws X if the operation fails
   */
  R apply(T arg) throws X;

}
