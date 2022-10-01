package nl.naturalis.common.function;

import java.util.function.IntSupplier;

/**
 * An alternative to Java's {@link IntSupplier} interface where the {@code get}
 * method is allowed to throw a checked exception.
 *
 * @param <X> the type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface ThrowingIntSupplier<X extends Throwable> {

  /**
   * Gets a result.
   *
   * @return a result
   * @throws X if the operation fails
   */
  int getAsInt() throws X;

}
