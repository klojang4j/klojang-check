package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import static java.lang.String.format;
import static nl.naturalis.common.ClassMethods.*;
import static nl.naturalis.common.StringMethods.ellipsis;
import static nl.naturalis.common.x.Constants.DECENT_TO_STRING;

/**
 * Indicates that a value could not be converted to the desired type.
 *
 * @author Ayco Holleman
 */
public final class TypeConversionException extends RuntimeException {

  /**
   * Returns a {@code TypeConversionException} informing the user that a type
   * conversion failed because the conversion function does not support the type of
   * the input value.
   *
   * @param inputValue the input value (may be {@code null}, indicating that
   *     {@code null} could not be converted to the target type)
   * @param targetType the target type
   * @return a {@code TypeConversionException} informing the user that a type
   *     conversion failed because the conversion function does not support the type
   *     of the input value
   */
  public static TypeConversionException inputTypeNotSupported(Object inputValue,
      Class<?> targetType) {
    return new TypeConversionException(inputValue,
        targetType, "input type not supported");
  }

  /**
   * Returns a {@code TypeConversionException} informing the user that a type
   * conversion failed because the conversion function does not support the desired
   * target type.
   *
   * @param inputValue the input value (may be {@code null}, indicating that
   *     {@code null} could not be converted to the target type)
   * @param targetType the target type
   * @return a {@code TypeConversionException} informing the user that a type
   *     conversion failed because the conversion function does not support the
   *     desired target type
   */
  public static TypeConversionException targetTypeNotSupported(Object inputValue,
      Class<?> targetType) {
    return new TypeConversionException(inputValue,
        targetType, "target type not supported");
  }

  /**
   * Returns a {@code TypeConversionException} informing the user that a type
   * conversion failed because the input value did not "fit into" the target type
   *
   * @param inputValue the input value (may be {@code null}, indicating that
   *     {@code null} could not be converted to the target type)
   * @param targetType the target type
   * @return a {@code TypeConversionException} informing the user that a type
   *     conversion failed because the input value did not "fit into" the target
   *     type
   */
  public static final TypeConversionException targetTypeTooNarrow(Object inputValue,
      Class<?> targetType) {
    return new TypeConversionException(inputValue,
        targetType,
        "target type too narrow");
  }

  private final Object inputValue;
  private final Class<?> targetType;

  /**
   * Creates a new {@code TypeConversionException} for the specified input value and
   * target type. A standard message is generated from the two arguments
   *
   * @param inputValue the input value (may be {@code null}, indicating that
   *     {@code null} could not be converted to the target type)
   * @param targetType the target type
   */
  public TypeConversionException(Object inputValue, Class<?> targetType) {
    super(defaultMessage(inputValue, targetType));
    this.inputValue = inputValue;
    this.targetType = targetType;
  }

  /**
   * Creates a new {@code TypeConversionException} for the specified input value and
   * target type.
   *
   * @param inputValue the input value (may be {@code null}, indicating that
   *     {@code null} could not be converted to the target type)
   * @param targetType the target type
   * @param msg a custom message or {@code String.format} message pattern
   * @param msgArgs zero or more message arguments
   */
  public TypeConversionException(Object inputValue,
      Class<?> targetType,
      String msg,
      Object... msgArgs) {
    super(defaultMessage(inputValue, targetType) + " *** " + format(msg, msgArgs));
    this.inputValue = inputValue;
    this.targetType = targetType;
  }

  /**
   * Returns the value for which the type conversion failed.
   *
   * @return the value for which the type conversion failed
   */
  public Object getInputValue() {
    return inputValue;
  }

  /**
   * Returns the target type of the type conversion.
   *
   * @return the target type of the type conversion
   */
  public Class<?> getTargetType() {
    return targetType;
  }

  private static String defaultMessage(Object obj, Class<?> type) {
    Check.notNull(type, Param.TARGET_TYPE);
    if (obj == null) {
      return format("cannot convert null to %s", className(type));
    } else if (obj instanceof String s) {
      return format("cannot convert \"%s\" to %s",
          ellipsis(obj, 30),
          className(type));
    } else if (DECENT_TO_STRING.contains(obj.getClass())) {
      return format("cannot convert (%s) %s to %s",
          simpleClassName(obj),
          ellipsis(obj, 30), className(type));
    }
    return format("cannot convert (%s) to %s", className(obj), className(type));
  }

}
