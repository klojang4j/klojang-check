package nl.naturalis.common.invoke;

import nl.naturalis.check.Check;
import nl.naturalis.common.ClassMethods;

import static nl.naturalis.common.ArrayMethods.pack;
import static nl.naturalis.common.ObjectMethods.isEmpty;

/**
 * Thrown when attempting to read or write a non-existent or inaccessible bean
 * property.
 *
 * @author Ayco Holleman
 */
public final class NoSuchPropertyException extends InvokeException {

  /**
   * Returns a {@code NoSuchPropertyException} for the specified name.
   *
   * @param bean the JavaBean that supposedly was to have a property with the
   *     specified name
   * @param property the name of the non-existent or inaccessible property
   * @return a {@code NoSuchPropertyException}
   */
  public static NoSuchPropertyException noSuchProperty(Object bean,
      String property) {
    return new NoSuchPropertyException(
        "No such property in class %s: \"%s\"",
        pack(ClassMethods.className(bean.getClass()), property));
  }

  private NoSuchPropertyException(String message, Object[] msgArgs) {
    super(createMessage(message, msgArgs));
  }

  private static String createMessage(String message, Object[] msgArgs) {
    Check.notNull(message, "message");
    if (isEmpty(msgArgs)) {
      return message;
    }
    return String.format(message, msgArgs);
  }

}
