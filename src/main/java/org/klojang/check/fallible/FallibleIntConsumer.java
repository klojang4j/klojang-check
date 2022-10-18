package org.klojang.check.fallible;

import java.util.function.IntConsumer;

/**
 * An alternative to the {@link IntConsumer} interface that allows the functional
 * method to throw a checked exception.
 *
 * @param <X> the type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface FallibleIntConsumer<X extends Throwable> {

  /**
   * Performs this operation on the given argument.
   *
   * @param value the input argument
   * @throws X if the operation fails
   */
  void accept(int value) throws X;

}
