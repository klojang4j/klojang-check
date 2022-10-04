package nl.naturalis.check.function;

import java.util.function.IntConsumer;

/**
 * An alternative to Java's {@link IntConsumer} interface where the {@code accept}
 * method is allowed to throw a checked exception.
 *
 * @param <X> the type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface ThrowingIntConsumer<X extends Throwable> {

  /**
   * Performs this operation on the given argument.
   *
   * @param value the input argument
   * @throws X if the operation fails
   */
  void accept(int value) throws X;

}
