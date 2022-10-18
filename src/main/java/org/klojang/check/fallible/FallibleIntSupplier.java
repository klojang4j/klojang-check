package org.klojang.check.fallible;

import java.util.function.IntSupplier;

/**
 * An alternative to the {@link IntSupplier} interface that allows the functional
 * method to throw a checked exception.
 *
 * @param <X> the type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface FallibleIntSupplier<X extends Throwable> {

  /**
   * Gets a result.
   *
   * @return a result
   * @throws X if the operation fails
   */
  int getAsInt() throws X;

}
