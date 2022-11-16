package org.klojang.check;

/**
 * Thrown if a check was incorrectly specified. This is a "bad" exception because it
 * does not imply that the tested value is invalid, but that the check testing the
 * value is itself broken. Note that, by and large, Klojang Checks does not verify
 * the sanity of your checks. This is to make sure that you can use Klojang Check
 * even in the most performance-critical operations. Thus, generally, you will not be
 * confronted with a {@code CorruptCheckException}. Instead you will simply get raw
 * runtime exceptions, thrown from lower level layers of code, if not the JVM.
 *
 * @author Ayco Holleman
 */
public final class CorruptCheckException extends RuntimeException {

  /**
   * Instantiates a new {@code CorruptCheckException} with the specified error
   * message.
   *
   * @param message the error message.
   */
  public CorruptCheckException(String message) {
    super(message);
  }

}
