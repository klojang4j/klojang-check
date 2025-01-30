package org.klojang.check;

import static org.klojang.check.extra.DuplicateValueException.Usage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.klojang.check.extra.DuplicateValueException;

/**
 * Provides factories for some commonly thrown exceptions. Typically (but not
 * always), for each type of exception, three exception factories are provided:
 *
 * <ol>
 *   <li>A {@code public static final} class constant of type
 *      {@code Function<String, Exception>}. This function can be used as the first
 *      argument to the {@code Check.on(...)} methods of the {@link Check} class. It
 *      sets the default exception, to be thrown if the value fails to pass any of
 *      the subsequent tests.
 *   <li>A method that takes a {@code String} (the error message) and returns a
 *      {@code Supplier<Exception>}. The {@code Supplier} will pass the string to
 *      the exception's constructor when requested to supply the exception. The
 *      {@code Supplier} can be used as the last argument to checks that allow you
 *      to specify an alternative exception.
 *   <li>A method that takes no arguments and returns a {@code Supplier<Exception>}.
 *      The {@code Supplier} will instantiate the exception using its no-arg
 *      constructor. This {@code Supplier}, too, can be used as the last argument to
 *      checks that allow you to specify an alternative exception.
 * </ol>
 *
 * <p>The following examples make this more concrete:
 *
 * <blockquote>
 *
 * <pre>{@code
 * Check.on(STATE, file, "file").is(writable());
 * // is shortcut for:
 * Check.on(IllegalStateException::new, file, "file").is(writable());
 *
 * Check.on(STATE, file).is(writable(), "file not writable");
 * // is shortcut for:
 * Check.on(IllegalStateException::new, file).is(writable(), "file not writable");
 *
 * Check.that(file).is(writable(), illegalState("file not writable"));
 * // is shortcut for:
 * Check.that(file).is(writable(), () -> new IllegalStateException("file not writable"));
 *
 * Check.that(file).is(writable(), illegalState());
 * // is shortcut for:
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
   * Shortcut for {@code IOException::new}.
   */
  public static final Function<String, IOException> IO = IOException::new;

  /**
   * Shortcut for {@code FileNotFoundException::new}.
   */
  public static final Function<String, FileNotFoundException> FILE = FileNotFoundException::new;

  /**
   * Shortcut for {@code NullPointerException::new}.
   */
  public static final Function<String, NullPointerException> NULL = NullPointerException::new;

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
   * default if a value fails to pass a test.
   */
  public static final Function<String, IllegalArgumentException> ARGUMENT =
      IllegalArgumentException::new;

  /**
   * Returns a {@code Supplier} of an {@link IllegalStateException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code IllegalStateException}
   */
  public static Supplier<IllegalStateException> illegalState(String message) {
    return () -> new IllegalStateException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@link IllegalStateException}. The supplier
   * will call the no-arg constructor of {@code IllegalStateException}.
   *
   * @return a {@code Supplier} of an {@code IllegalStateException}
   */
  public static Supplier<IllegalStateException> illegalState() {
    return IllegalStateException::new;
  }

  /**
   * Returns a {@code Supplier} of an {@link IllegalArgumentException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code IllegalArgumentException}
   */
  public static Supplier<IllegalArgumentException> illegalArgument(String message) {
    return () -> new IllegalArgumentException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@link IllegalArgumentException}. The supplier
   * will call the no-arg constructor of {@code IllegalArgumentException}.
   *
   * @return a {@code Supplier} of an {@code IllegalArgumentException}
   */
  public static Supplier<IllegalArgumentException> illegalArgument() {
    return IllegalArgumentException::new;
  }

  /**
   * Returns a {@code Supplier} of an {@link IndexOutOfBoundsException}.
   *
   * @param index the out-of-bounds index
   * @return a {@code Supplier} of an {@code IndexOutOfBoundsException}
   * @see IndexOutOfBoundsException#IndexOutOfBoundsException(int)
   */
  public static Supplier<IndexOutOfBoundsException> indexOutOfBounds(int index) {
    return () -> new IndexOutOfBoundsException(index);
  }

  /**
   * Returns a {@code Supplier} of an {@link IndexOutOfBoundsException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code IndexOutOfBoundsException}
   * @see IndexOutOfBoundsException#IndexOutOfBoundsException(int)
   */
  public static Supplier<IndexOutOfBoundsException> indexOutOfBounds(String message) {
    return () -> new IndexOutOfBoundsException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@link IOException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of an {@code IOException}
   */
  public static Supplier<IOException> ioException(String message) {
    return () -> new IOException(message);
  }

  /**
   * Returns a {@code Supplier} of an {@link IOException}. The supplier will call the
   * no-arg constructor of {@code IOException}.
   *
   * @return a {@code Supplier} of an {@code IOException}
   */
  public static Supplier<IOException> ioException() {
    return IOException::new;
  }

  /**
   * Returns a {@code Supplier} of a {@link FileNotFoundException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of a {@link FileNotFoundException}
   */
  public static Supplier<FileNotFoundException> fileNotFound(String message) {
    return () -> new FileNotFoundException(message);
  }

  /**
   * Returns a {@code Supplier} of a {@link FileNotFoundException}.
   *
   * @param f the {@link File} object corresponding to the non-existent file
   * @return a {@code Supplier} of a {@link FileNotFoundException}
   */
  public static Supplier<FileNotFoundException> fileNotFound(File f) {
    return () -> new FileNotFoundException("file not found: " + f);
  }

  /**
   * Returns a {@code Supplier} of a {@link NullPointerException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of a {@code NullPointerException}
   */
  public static Supplier<NullPointerException> npe(String message) {
    return () -> new NullPointerException(message);
  }

  /**
   * Returns a {@code Supplier} of a {@link NullPointerException}. The supplier will
   * call the no-arg constructor of {@code NullPointerException}.
   *
   * @return a {@code Supplier} of a {@code NullPointerException}
   */
  public static Supplier<NullPointerException> npe() {
    return NullPointerException::new;
  }

  /**
   * Returns a {@code Supplier} of a {@link NoSuchElementException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of a {@code NoSuchElementException}
   */
  public static Supplier<NoSuchElementException> noSuchElement(String message) {
    return () -> new NoSuchElementException(message);
  }

  /**
   * Returns a {@code Supplier} of a {@link NoSuchElementException}. The supplier
   * will call the no-arg constructor of {@code NoSuchElementException}.
   *
   * @return a {@code Supplier} of a {@code NoSuchElementException}
   */
  public static Supplier<NoSuchElementException> noSuchElement() {
    return NoSuchElementException::new;
  }

  /**
   * Returns a {@code Supplier} of a {@link DuplicateValueException}.
   *
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateKey() {
    return () -> new DuplicateValueException(Usage.KEY);
  }

  /**
   * Returns a {@code Supplier} of a {@link DuplicateValueException}.
   *
   * @param key the key found to be a duplicate
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateKey(Object key) {
    return () -> new DuplicateValueException(Usage.KEY, key);
  }

  /**
   * Returns a {@code Supplier} of a {@link DuplicateValueException}.
   *
   * @param element the element found to be a duplicate
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateElement(Object element) {
    return () -> new DuplicateValueException(Usage.ELEMENT, element);
  }

  /**
   * Returns a {@code Supplier} of a {@link DuplicateValueException}.
   *
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateElement() {
    return () -> new DuplicateValueException(Usage.ELEMENT);
  }

  /**
   * Returns a {@code Supplier} of a {@link DuplicateValueException}.
   *
   * @param message the exception message
   * @return a {@code Supplier} of a {@code DuplicateValueException}
   */
  public static Supplier<DuplicateValueException> duplicateValue(String message) {
    return () -> new DuplicateValueException(message);
  }

}
