package nl.naturalis.common.path;

/**
 * Thrown by a {@link KeyDeserializer} if it fails to deserialize a path segment into
 * a map key.
 */
public final class KeyDeserializationException extends Exception {

  /**
   * Instantiates a new {@code KeyDeserializationException}.
   */
  public KeyDeserializationException() {
    super();
  }

  /**
   * Instantiates a new {@code KeyDeserializationException}.
   *
   * @param reason the reason for the key deserialization failure
   */
  public KeyDeserializationException(String reason) {
    super(reason);
  }

}
