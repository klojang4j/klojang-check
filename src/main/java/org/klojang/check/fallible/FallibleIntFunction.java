package org.klojang.check.fallible;

import java.util.function.IntFunction;

/**
 * An alternative to the {@link IntFunction} interface that allows the functional
 * method to throw a checked exception.
 *
 * @param <R> the type of the return value
 * @param <X> the type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface FallibleIntFunction<R, X extends Throwable> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   * @throws X if the operation fails
   */
  R apply(int value) throws X;

}
