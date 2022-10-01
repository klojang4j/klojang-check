package nl.naturalis.common.invoke;

import nl.naturalis.common.ClassMethods;

import static nl.naturalis.common.ArrayMethods.implode;
import static nl.naturalis.common.ClassMethods.simpleClassName;
import static nl.naturalis.common.ExceptionMethods.getRootCause;

/**
 * A {@code RuntimeException} thrown in response to various dynamic invocation
 * errors.
 */
public sealed class InvokeException extends RuntimeException permits
    IllegalAssignmentException,
    NoPublicGettersException, NoPublicSettersException, NoSuchPropertyException {

  /**
   * Returns an {@code InvokeException} informing the client that a class could not
   * be instantiated because it has a no-arg constructor.
   *
   * @param clazz the class to be instantiated
   * @return an {@code InvokeException} informing the client that a class could not
   *     be instantiated because it has a no-arg constructor
   */
  public static InvokeException missingNoArgConstructor(Class<?> clazz) {
    return new InvokeException("missing no-arg constructor for " + clazz);
  }

  /**
   * Returns an {@code InvokeException} informing the client that a class could not
   * be instantiated because it has constructor with the provided parameter list.
   *
   * @param clazz the class to be instantiated
   * @param params the provided (and illegal) parameter list
   * @return an {@code InvokeException} informing the client that a class could not
   *     be instantiated because it has constructor with the provided parameter list
   */
  public static InvokeException noSuchConstructor(Class<?> clazz,
      Class<?>... params) {
    String msg = String.format("no such constructor: %s(%s)",
        simpleClassName(clazz),
        implode(params, ClassMethods::simpleClassName, ", ", 0, -1));
    return new InvokeException(msg);
  }

  /**
   * Returns an {@code InvokeException} informing the client that an operation failed
   * because of some {@code Throwable} thrown from the {@code java.lang.invoke}
   * package.
   *
   * @param array the array to be operated upon
   * @param throwable the throwable thrown from {@code java.lang.invoke}
   * @return an {@code InvokeException} informing the client that an operation failed
   *     because of some {@code Throwable} thrown from the {@code java.lang.invoke}
   */
  public static InvokeException arrayOperationFailed(Object array,
      Throwable throwable) {
    String msg = String.format("array operation failed for %s: %s",
        simpleClassName(array),
        throwable);
    return new InvokeException(msg);
  }

  static InvokeException wrap(Throwable t, Object bean, Getter getter) {
    String msg = String.format("Error while reading %s.%s: %s",
        simpleClassName(bean),
        getter.getProperty(),
        getRootCause(t));
    return new InvokeException(msg);
  }

  InvokeException(String message) {
    super(message);
  }

}
