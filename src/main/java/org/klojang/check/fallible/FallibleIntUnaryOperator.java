package org.klojang.check.fallible;

/**
 * An alternative to the {@link java.util.function.IntUnaryOperator} interface that
 * allows the functional method to throw a checked exception.
 *
 * @param <X> the type of the exception potentially being thrown
 * @author Ayco Holleman
 */
@FunctionalInterface
public interface FallibleIntUnaryOperator<X extends Throwable> {

  /**
   * Applies this operator to the given operand.
   *
   * @param operand the operand
   * @return the operator result
   * @throws X if the operation fails
   */
  int applyAsInt(int operand) throws X;

}
