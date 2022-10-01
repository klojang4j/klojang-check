package nl.naturalis.common.path;

import java.util.function.Supplier;

import static nl.naturalis.common.ClassMethods.className;
import static nl.naturalis.common.ClassMethods.simpleClassName;
import static nl.naturalis.common.path.ErrorCode.*;

/**
 * Thrown by a {@link PathWalker} if a path-read or path-write error occurs.
 */
public final class PathWalkerException extends RuntimeException {

  interface Factory extends Supplier<PathWalkerException> {}

  private static final String INVALID_PATH = "invalid path: \"%s\" (segment %d)";
  private static final String PATH_SEGMENT = "path %s, segment %s: ";

  static Factory noSuchProperty(Path path, int segment, Class<?> clazz) {
    String fmt = INVALID_PATH
        + " *** no accessible property named \"%s\" in %s.class";
    String className = className(clazz);
    String msg = String.format(fmt,
        path,
        segment + 1,
        path.segment(segment),
        className);
    return () -> new PathWalkerException(NO_SUCH_PROPERTY, msg);
  }

  static Factory noSuchKey(Path path, int segment, Object key) {
    String fmt = INVALID_PATH + " *** no such key: \"%s\"";
    String msg = String.format(fmt, path, segment + 1, path.segment(segment));
    return () -> new PathWalkerException(NO_SUCH_KEY, msg);
  }

  static Factory indexExpected(Path path, int segment) {
    String fmt = INVALID_PATH + " *** array index expected; found: \"%s\"";
    String msg = String.format(fmt, path, segment + 1, path.segment(segment));
    return () -> new PathWalkerException(INDEX_EXPECTED, msg);
  }

  static Factory indexOutOfBounds(Path path, int segment) {
    String fmt = INVALID_PATH + " *** index out of bounds: %s";
    String msg = String.format(fmt, path, segment + 1, path.segment(segment));
    return () -> new PathWalkerException(INDEX_OUT_OF_BOUNDS, msg);
  }

  static Factory nullValue(Path path, int segment) {
    String fmt = INVALID_PATH
        + " *** terminal value encountered at segment \"%s\": null";
    String msg = String.format(fmt, path, segment + 1, path.segment(segment));
    return () -> new PathWalkerException(TERMINAL_VALUE, msg);
  }

  static Factory terminalValue(Path path, int segment, Object value) {
    String fmt = INVALID_PATH
        + " *** terminal value encountered at segment \"%s\": (%s) %s";
    String className = simpleClassName(value.getClass());
    String msg = String.format(fmt,
        path,
        segment + 1,
        path.segment(segment),
        className,
        value);
    return () -> new PathWalkerException(TERMINAL_VALUE, msg);
  }

  static Factory emptySegment(Path path, int segment) {
    String fmt = INVALID_PATH + " *** segment must not be null or empty";
    String msg = String.format(fmt, path, segment + 1);
    return () -> new PathWalkerException(EMPTY_SEGMENT, msg);
  }

  static Factory typeMismatch(Path path, int segment, String message) {
    String fmt = PATH_SEGMENT + "%s";
    String msg = String.format(fmt, path, segment + 1, message);
    return () -> new PathWalkerException(TYPE_MISMATCH, msg);
  }

  static Factory typeMismatch(Path path, int segment, Class expected, Class actual) {
    String fmt = PATH_SEGMENT + "cannot assign %s to %s";
    String scn0 = simpleClassName(expected);
    String scn1 = simpleClassName(actual);
    String msg = String.format(fmt, path, segment + 1, scn0, scn1);
    return () -> new PathWalkerException(TYPE_MISMATCH, msg);
  }

  static Factory notModifiable(Path path,
      int segment,
      Class<?> type) {
    String fmt = PATH_SEGMENT
        + "%s implementation encountered at segment \"%s\" appears to be unmodifiable";
    String msg = String.format(fmt,
        path,
        segment + 1,
        simpleClassName(type),
        path.segment(segment));
    return () -> new PathWalkerException(NOT_MODIFIABLE, msg);
  }

  static Factory keyDeserializationFailed(Path path,
      int segment,
      KeyDeserializationException exc) {
    String msg;
    if (exc.getMessage() == null) {
      String fmt = INVALID_PATH + " *** failed to deserialize \"%s\" into map key";
      msg = String.format(fmt, path, segment + 1, path.segment(segment));
      return () -> new PathWalkerException(KEY_DESERIALIZATION_FAILED, msg);
    } else {
      String fmt = INVALID_PATH + " *** %s";
      msg = String.format(fmt, path, segment + 1, exc.getMessage());
    }
    return () -> new PathWalkerException(KEY_DESERIALIZATION_FAILED, msg);
  }

  static Factory unexpectedError(Path path, int segment, Throwable t) {
    String fmt = PATH_SEGMENT + "unexpected error *** %s";
    String msg = String.format(fmt, path, segment + 1, t);
    return () -> new PathWalkerException(EXCEPTION, msg);
  }

  private final ErrorCode errorCode;

  private PathWalkerException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  /**
   * Return a symbolic constant for the error encountered by the {@link PathWalker}
   *
   * @return A symbolic constant for the error encountered by the {@link PathWalker}.
   */
  public ErrorCode getErrorCode() {
    return errorCode;
  }

}
