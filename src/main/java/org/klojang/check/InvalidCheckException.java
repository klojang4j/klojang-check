package org.klojang.check;

/**
 * Thrown if a check on an argument was not correctly specified. This is a "bad"
 * exception because it doesn't mean that the tested code is invalid, but that the
 * check itself is flawed.
 *
 * @author Ayco Holleman
 */
public final class InvalidCheckException extends RuntimeException {

  static InvalidCheckException typeNotSupported(Class<?> type) {
    return new InvalidCheckException("type not supported: " + type);
  }

  static InvalidCheckException notApplicable(String check, Object arg) {
    String msg = String.format("\"%s\" not applicable to %s", check, arg.getClass());
    return new InvalidCheckException(msg);
  }

  InvalidCheckException(String message) {
    super(message);
  }

}
