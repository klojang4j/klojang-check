package nl.naturalis.check;

import nl.naturalis.common.collection.DuplicateValueException;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static nl.naturalis.common.collection.DuplicateValueException.ValueType;

/**
 * Provides factories for some commonly thrown exceptions. Typically (but not
 * always), for each type of exception, three exception factories are provided:
 *
 * <ol>
 *   <li>A {@code public static final} class constant of type {@code Function<String, Exception>}.
 *       This function can be used as the first argument to the {@linkplain Check#on(Function, int)
 *       Check.on(...)} static factory methods.
 *   <li>A method that takes a {@code String} (the error message) and returns a {@code
 *       Supplier<Exception>}. The {@code Supplier} passes the string to the exception's
 *       constructor. The {@code Supplier} can be used as the last argument to
 *       {@linkplain ObjectCheck#is(Predicate, String, Object...)}  checks} that allow you to supply
 *       your own exception.
 *   <li>A method that takes no arguments and returns a {@code Supplier<Exception>}. The {@code
 *       Supplier} instantiates the {@code Exception} using its no-arg constructor. This {@code
 *       Supplier}, too, can be used as the last argument to checks that allow you to supply your own
 *       exception.
 * </ol>
 *
 * <p>The following examples make this more concrete:
 *
 * <blockquote>
 *
 * <pre>{@code
 * Check.on(STATE, file, "file").is(writable());
 * // shortcut for:
 * Check.on(IllegalStateException::new, file, "file").is(writable());
 *
 * Check.on(STATE, file).is(writable(), "file not writable");
 * // shortcut for:
 * Check.on(IllegalStateException::new, file).is(writable(), "file not writable");
 *
 * Check.that(file).is(writable(), illegalState("file not writable"));
 * // shortcut for:
 * Check.that(file).is(writable(), () -> new IllegalStateException("file not writable"));
 *
 * Check.that(file).is(writable(), illegalState());
 * // shortcut for:
 * Check.that(file).is(writable(), () -> new IllegalStateException());
 * }</pre>
 *
 * </blockquote>
 *
 * @author Ayco Holleman
 */
public final class CommonExceptions {

  private CommonExceptions() {
    throw new UnsupportedOperationException();
  }

  /**
   * Shortcut for {@code IllegalStateException::new}.
   */
  public static final Function<String, IllegalStateException> STATE = IllegalStateException::new;

  /**
   * Shortcut for {@code IndexOutOfBoundsException::new}.
   */
  public static final Function<String, IndexOutOfBoundsException> INDEX =
      IndexOutOfBoundsException::new;

  /**
   * A {@code Function} that takes a string and returns an {@code IOException}.
   * Shortcut for {@code IOException::new}.
   */
  public static final Function<String, IOException> IO = IOException::new;

  /**
   * Shortcut for {@code NullPointerException::new}.
   */
  public static final Function<String, NullPointerException> NPE = NullPointerException::new;

  /**
   * Shortcut for {@code NoSuchElementException::new}.
   */
  public static final Function<String, NoSuchElementException> ELEMENT =
      NoSuchElementException::new;

  /**
   * Shortcut for {@code DuplicateValueException::new}.
   */
  public static final Function<String, DuplicateValueException> DUPLICATE =
      DuplicateValueException::new;

  /**
   * Shortcut for {@code IllegalArgumentException::new}. Included for completeness.
   * {@code IllegalArgumentException} already is the exception that is thrown by
   * default:
   *
   * <blockquote><pre>{@code
   * Check.on(ARGUMENT, foo, "foo").is(int32());
   * // is equivalent to:
   * Check.that(foo, "foo").is(int32());
   * }</pre></blockquote>
   */
  public static final Function<String, IllegalArgumentException> ARGUMENT =
      IllegalArgumentException::new;

  /**
   * Returns a {@code Supplier} of an {@code IllegalStateException}. The supplier
   * will pass the specified message to the constructor of
   * {@code IllegalStateException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code IllegalStateException}
   */
  public static Supplier<IllegalStateException> illegalState(String message) {
    return () -> new IllegalStateException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@code IllegalStateException}. The supplier
   * will call the no-arg constructor of {@code IllegalStateException}.
   *
   * @return a {@code Supplier} of an {@code IllegalStateException}
   */
  public static Supplier<IllegalStateException> illegalState() {
    return IllegalStateException::new;
  }

  /**
   * Returns a {@code Supplier} of an {@code IllegalArgumentException}. The supplier
   * will pass the specified message to the constructor of
   * {@code IllegalArgumentException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code IllegalArgumentException}
   */
  public static Supplier<IllegalStateException> illegalArgument(String message) {
    return () -> new IllegalStateException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@code IllegalStateException}. The supplier
   * will call the no-arg constructor of {@code IllegalStateException}.
   *
   * @return a {@code Supplier} of an {@code IllegalStateException}
   */
  public static Supplier<IllegalStateException> illegalArgument() {
    return IllegalStateException::new;
  }

  /**
   * A {@code Supplier} of an {@code IndexOutOfBoundsException} for the specified
   * index.
   *
   * @param index the out-of-bounds index
   * @return a {@code Supplier} of an {@code IndexOutOfBoundsException}
   * @see IndexOutOfBoundsException#IndexOutOfBoundsException(int)
   */
  public static Supplier<IndexOutOfBoundsException> indexOutOfBounds(int index) {
    return () -> new IndexOutOfBoundsException(index);
  }

  /**
   * Returns a {@code Supplier} of an {@code IOException}. The supplier will pass the
   * specified message to the constructor of {@code IOException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code IOException}
   */
  public static Supplier<IOException> ioException(String message) {
    return () -> new IOException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@code IOException}. The supplier will call the
   * no-arg constructor of {@code IOException}.
   *
   * @return a {@code Supplier} of an {@code IOException}
   */
  public static Supplier<IOException> ioException() {
    return IOException::new;
  }

  /**
   * Returns a {@code Supplier} of an {@code NullPointerException}. The supplier will
   * pass the specified message to the constructor of {@code NullPointerException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code NullPointerException}
   */
  public static Supplier<NullPointerException> NPE(String message) {
    return () -> new NullPointerException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@code NullPointerException}. The supplier will
   * call the no-arg constructor of {@code NullPointerException}.
   *
   * @return a {@code Supplier} of an {@code NullPointerException}
   */
  public static Supplier<NullPointerException> NPE() {
    return NullPointerException::new;
  }

  /**
   * Returns a {@code Supplier} of an {@code NoSuchElementException}. The supplier
   * will pass the specified message to the constructor of
   * {@code NoSuchElementException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code NoSuchElementException}
   */
  public static Supplier<NoSuchElementException> noSuchElement(String message) {
    return () -> new NoSuchElementException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@code NoSuchElementException}. The supplier
   * will call the no-arg constructor of {@code NoSuchElementException}.
   *
   * @return a {@code Supplier} of an {@code NoSuchElementException}
   */
  public static Supplier<NoSuchElementException> noSuchElement() {
    return NoSuchElementException::new;
  }

  /**
   * Returns a {@code Supplier} of a {@code DuplicateValueException}. To be used if
   * the duplicate value was meant to be used as a map key.
   *
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateKey() {
    return () -> new DuplicateValueException(ValueType.KEY);
  }

  /**
   * Returns a {@code Supplier} of a {@code DuplicateValueException}. To be used if
   * the duplicate value was meant to be used as a map key.
   *
   * @param key the key found to be a duplicate
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateKey(String key) {
    return () -> new DuplicateValueException(ValueType.KEY, key);
  }

  /**
   * Returns a {@code Supplier} of a {@code DuplicateValueException}. To be used if
   * the duplicate value was meant to be added to a {@code Set}.
   *
   * @param element the element found to be a duplicate
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateElement(String element) {
    return () -> new DuplicateValueException(ValueType.ELEMENT, element);
  }

  /**
   * Returns a {@code Supplier} of a {@code DuplicateValueException}. To be used if
   * the duplicate value was meant to be added to a {@code Set}.
   *
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateElement() {
    return () -> new DuplicateValueException(ValueType.ELEMENT);
  }

}
