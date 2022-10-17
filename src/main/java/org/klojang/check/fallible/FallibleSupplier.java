package org.klojang.check.fallible;

import java.util.function.Supplier;

/**
 * An alternative to Java's {@link Supplier} interface that allows the functional
 * method to throw a checked exception.
 *
 * @param <T> The type of the return value
 * @param <X> The type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface FallibleSupplier<T, X extends Throwable> {

  /**
   * Gets a result.
   *
   * @return a result
   * @throws X if the operation fails
   */
  T get() throws X;

}
