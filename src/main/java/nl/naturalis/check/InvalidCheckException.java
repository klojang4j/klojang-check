package nl.naturalis.check;

/**
 * Thrown if a check on an argument was not correctly specified. This is a "bad" exception because
 * it doesn't mean that the tested code is invalid, but that the check itself is flawed.
 *
 * @author Ayco Holleman
 */
public final class InvalidCheckException extends RuntimeException {

  InvalidCheckException(String message) {
    super(message);
  }
}
