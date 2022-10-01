package nl.naturalis.common.collection;

import java.util.List;

import static nl.naturalis.common.CollectionMethods.implode;

/**
 * Indicates that insertion into a {@linkplain java.util.Map Map},
 * {@linkplain java.util.Set Set} or some other uniqueness-enforcing data structure
 * failed because the value to be inserted turned out to be a duplicate.
 */
public class DuplicateValueException extends RuntimeException {

  /**
   * Symbolic constants the type of value that was found to be a duplicate
   */
  public enum ValueType {
    /**
     * The value was a duplicate of a {@code Map} key, or possibly some record-like
     * structure.
     */
    KEY,
    /**
     * The value was a duplicate of a {@code Set} element.
     */
    ELEMENT,
    /**
     * The value was a duplicate due to some other uniqueness-enforcing mechanism or
     * data structure.
     */
    VALUE;

    private String description() {
      return name().toLowerCase();
    }
  }

  /**
   * Default constructor.
   */
  public DuplicateValueException() {
    super("duplicate value");
  }

  /**
   * Creates a new {@code DuplicateValueException} with the specified message.
   *
   * @param message the message
   */
  public DuplicateValueException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code DuplicateValueException}.
   *
   * @param valueType the type of the duplicates
   */
  public DuplicateValueException(ValueType valueType) {
    super("duplicate " + valueType.description());
  }

  /**
   * Creates a new {@code DuplicateValueException} for the specified value.
   *
   * @param valueType the type of the duplicates
   * @param duplicate the duplicate value.
   */
  public DuplicateValueException(ValueType valueType, Object duplicate) {
    super("duplicate " + valueType.description() + ": " + duplicate);
  }

  /**
   * Creates a new {@code DuplicateValueException} for the specified values.
   *
   * @param valueType the type of the duplicates
   * @param duplicates the duplicate values.
   */
  public DuplicateValueException(ValueType valueType, List<Object> duplicates) {
    super("duplicate " + valueType.description() + "(s): " + implode(duplicates));
  }

}
