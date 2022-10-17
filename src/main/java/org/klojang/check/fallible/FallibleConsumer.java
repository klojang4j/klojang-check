package org.klojang.check.fallible;

import java.util.function.Consumer;

/**
 * An alternative to Java's {@link Consumer} interface that allows the functional
 * method to throw a checked exception.
 *
 * @param <T> the type of the input to the operation
 * @param <X> the type of the exception potentially being thrown by the
 *     operation
 * @author Ayco Holleman
 */
public interface FallibleConsumer<T, X extends Throwable> {

  /**
   * Performs this operation on the given argument.
   *
   * @param t the input argument
   * @throws X if the operation fails
   */
  public void accept(T t) throws X;

}
