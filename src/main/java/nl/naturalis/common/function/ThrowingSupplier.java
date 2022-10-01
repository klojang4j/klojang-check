package nl.naturalis.common.function;

import java.util.function.Supplier;

/**
 * An alternative to Java's {@link Supplier} interface where the {@code get} method
 * is allowed to throw a checked exception.
 *
 * @param <T> The type of the return value
 * @param <X> The type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface ThrowingSupplier<T, X extends Throwable> {

  /**
   * Gets a result.
   *
   * @return a result
   * @throws X if the operation fails
   */
  T get() throws X;

}
