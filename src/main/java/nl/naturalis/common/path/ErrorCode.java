package nl.naturalis.common.path;

/**
 * Symbolic constants for read/write failures.
 */
public enum ErrorCode {

  /**
   * Indicates that the {@code PathWalker} had arrived on a list or array, so
   * expected the next segment in the {@code Path} to be an array index, but found
   * something other than an integer.
   */
  INDEX_EXPECTED,
  /**
   * Indicates that the {@code PathWalker} encountered an array index in the
   * {@code Path}, but the value of the preceding path segment was something other
   * than a list or array.
   */
  INDEX_NOT_ALLOWED,
  /**
   * Indicates that the {@code PathWalker} encountered an array index in the
   * {@code Path} that was out of bounds for the list or array it was processing.
   */
  INDEX_OUT_OF_BOUNDS,
  /**
   * Indicates that the {@code PathWalker} encountered a segment that did not
   * correspond to any (accessible) property of the JavaBean it was processing.
   */
  NO_SUCH_PROPERTY,
  /**
   * Indicates that the {@code PathWalker} encountered a segment that did not
   * correspond to any key of the {@code Map} it was processing. This code is only
   * returned by the {@code read} methods of the {@code PathWalker} class. The
   * {@code write} methods just add the key to the map.
   */
  NO_SUCH_KEY,
  /**
   * Indicates that a {@link KeyDeserializer} failed to deserialize a path segment
   * into a map key.
   */
  KEY_DESERIALIZATION_FAILED,
  /**
   * Indicates that the {@code PathWalker} encountered a
   * {@link Path#NULL_SEGMENT null segment} or an empty segment while not processing
   * a {@code Map}. ({@code null} and the empty string can only possibly be valid as
   * map keys.)
   */
  EMPTY_SEGMENT,
  /**
   * Indicates that the {@code Path} continued after having reached a terminal value
   * (like {@code null} or a primitive value) or an opaque value (like a
   * {@code String}).
   */
  TERMINAL_VALUE,
  /**
   * Indicates that the {@code PathWalker} encountered a value that it doesn't know
   * how to read or write.
   */
  TYPE_NOT_SUPPORTED,
  /**
   * Indicates that the {@code PathWalker} could not write a value because its type
   * was incompatible with the target property or array.
   */
  TYPE_MISMATCH,
  /**
   * Indicates that the {@code PathWalker} attempted to modify a {@code List} or
   * {@code Map} and the {@code List} or {@code Map} responded by throwing an
   * {@code UnsupportedOperationException}.
   */
  NOT_MODIFIABLE,
  /**
   * Indicates that the {@code PathWalker} trapped an exception from underlying
   * code.
   */
  EXCEPTION,

}
