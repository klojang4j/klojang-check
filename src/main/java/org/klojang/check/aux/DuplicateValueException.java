package org.klojang.check.aux;

import java.util.Collection;

import org.klojang.check.Check;

import static java.util.stream.Collectors.joining;
import static org.klojang.check.aux.DuplicateValueException.Usage.VALUE;

/**
 * Indicates that insertion into a {@linkplain java.util.Map Map},
 * {@linkplain java.util.Set Set} or some other uniqueness-enforcing data structure
 * failed because the value to be inserted turned out to be a duplicate. Klojang
 * Check does not itself ever throw a {@code DuplicateValueException}, but the error
 * condition it represents is general and important enough to merit inclusion as a
 * common exception in the {@link CommonExceptions} class.
 */
public class DuplicateValueException extends RuntimeException {

  /**
   * Symbolic constants for the intended usage of the value.
   */
  public enum Usage {
    /**
     * The value was meant to be used as a map key, or possibly some record-like
     * structure.
     */
    KEY,
    /**
     * The value was meant to be inserted into a {@code Set}.
     */
    ELEMENT,
    /**
     * The value was meant to be used for some other uniqueness-enforcing purpose.
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
    this(VALUE);
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
   * @param usage the intended usage of the value
   */
  public DuplicateValueException(Usage usage) {
    super(createMessage(usage));
  }

  /**
   * Creates a new {@code DuplicateValueException} for the specified value.
   *
   * @param usage the intended usage of the value
   * @param duplicate the duplicate value.
   */
  public DuplicateValueException(Usage usage, Object duplicate) {
    super(createMessage(usage, duplicate));
  }

  /**
   * Creates a new {@code DuplicateValueException} for the specified values.
   *
   * @param usage the intended usage of the values
   * @param duplicates the duplicate values.
   */
  public DuplicateValueException(Usage usage, Collection<Object> duplicates) {
    super(createMessage(usage, duplicates));
  }

  private static String createMessage(Usage usage) {
    Check.notNull(usage, "usage");
    return "duplicate " + usage.description();
  }

  private static String createMessage(Usage usage, Object duplicate) {
    Check.notNull(usage, "usage");
    return "duplicate " + usage.description() + ": " + duplicate;
  }

  private static String createMessage(Usage usage, Collection<Object> duplicates) {
    Check.notNull(usage, "usage");
    Check.notNull(usage, "duplicates");
    String str = duplicates.stream().map(String::valueOf).collect(joining(", "));
    return "duplicate " + usage.description() + "s: " + str;
  }

}
