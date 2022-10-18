package org.klojang.check.fallible;

import java.util.function.BiFunction;

/**
 * An alternative to the {@link BiFunction} interface that allows the functional
 * method to throw a checked exception.
 *
 * @param <T> the type of the 1st argument
 * @param <U> the type of the 2nd argument
 * @param <R> the type of the return value
 * @param <X> the type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface FallibleBiFunction<T, U, R, X extends Throwable> {

  /**
   * Applies this function to the given arguments.
   *
   * @param arg0 the 1st argument
   * @param arg1 the 2nd argument
   * @return a value of type {@code R}
   * @throws X if the operation fails
   */
  R apply(T arg0, U arg1) throws X;

}
