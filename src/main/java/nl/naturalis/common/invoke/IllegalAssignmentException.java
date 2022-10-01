package nl.naturalis.common.invoke;

import nl.naturalis.common.ClassMethods;

import static nl.naturalis.common.ClassMethods.simpleClassName;
import static nl.naturalis.common.ObjectMethods.ifNotNull;

/**
 * Thrown by a {@link BeanWriter} if it failed to write a value to a property, for
 * example because of a type mismatch.
 */
public final class IllegalAssignmentException extends InvokeException {

  private final Class<?> beanClass;
  private final String propertyName;
  private final Class<?> propertyType;
  private final Object value;

  IllegalAssignmentException(Class<?> beanClass,
      String propertyName,
      Class<?> propertyType,
      Object value) {
    super(createMessage(beanClass, propertyName, propertyType, value));
    this.beanClass = beanClass;
    this.propertyName = propertyName;
    this.propertyType = propertyType;
    this.value = value;
  }

  private static String createMessage(Class<?> beanClass,
      String propertyName,
      Class<?> propertyType,
      Object value) {
    String valueType = ifNotNull(value, ClassMethods::simpleClassName, "null");
    return "illegal assignment for "
        + simpleClassName(beanClass)
        + "."
        + propertyName
        + ": cannot assign "
        + valueType
        + " to "
        + simpleClassName(propertyType);
  }

  /**
   * Returns the class containing the property.
   *
   * @return The class containing the property
   */
  public Class<?> getBeanClass() {
    return beanClass;
  }

  /**
   * Returns the name of the property.
   *
   * @return The name of the property
   */
  public String getPropertyName() {
    return propertyName;
  }

  /**
   * Returns the class of the property.
   *
   * @return The class of the property
   */
  public Class<?> getPropertyType() {
    return propertyType;
  }

  /**
   * Returns the value that was attempted to be assigned to the property
   *
   * @return The value that was attempted to be assigned to the property
   */
  public Object getValue() {
    return value;
  }

}
