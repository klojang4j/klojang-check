package org.klojang.check;

/**
 * Thrown if a check was incorrectly specified. This is a "bad" exception because it
 * does not imply that the tested value is invalid, but that the check testing the
 * value is itself broken. Note that, by and large, Klojang Checks abides by the
 * motto: we don't check your check. This is to make sure that you can use Klojang
 * Check even in the most performance-critical operations. Thus, generally, you will
 * not be confronted with a {@code CorruptCheckException} if you specify a wrong
 * argument to the {@code is()}, {@code notIs()}, {@code has()} and {@code notHas()}
 * methods. Instead you will simply get, for example, a raw
 * {@code NullPointerException} (e.g. when specifying {@code null} for the test to be
 * executed).
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
