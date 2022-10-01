package nl.naturalis.common.path;

import java.util.List;

/**
 * Defines a mechanism for converting a path segment to a map key. A
 * {@code KeyDeserializer} can optionally be passed to the
 * {@link PathWalker#PathWalker(List, boolean, KeyDeserializer) constructor} of the
 * {@link PathWalker} class. It is needed when reading or writing to objects that
 * are, or contain maps with a non-{@code String} key type.
 */
@FunctionalInterface
public interface KeyDeserializer {

  /**
   * Converts the path segment at the specified index to a map key. If
   * deserialization fails in a non-fatal way, a {@link KeyDeserializationException}
   * should be thrown. With
   * {@link PathWalker#PathWalker(List, boolean) exception suppression} disabled,
   * this exception will be converted to a {@link PathWalkerException} with error
   * code {@link ErrorCode#KEY_DESERIALIZATION_FAILED KEY_DESERIALIZATION_FAILED}.
   * With exception suppression enabled, however, the exception will be ignored, like
   * any of the anticipated errors in {@link ErrorCode}. The
   * {@link PathWalker#read(Object) read} methods will simply return {@code null} for
   * the path in question, while the {@link PathWalker#write(Object, Object) write}
   * methods will tacitly abort the attempt to set the path's value.
   *
   * @param path the path containing the segment
   * @param segmentIndex the index of the segment
   * @return the map key
   * @throws KeyDeserializationException If key deserialization fails in a
   *     non-fatal way
   */
  Object deserialize(Path path, int segmentIndex) throws KeyDeserializationException;

}
