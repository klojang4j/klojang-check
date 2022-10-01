package nl.naturalis.common.invoke;

/**
 * Used to indicate whether you want to allow or disallow properties from being read
 * by a {@link BeanReader} or written by a {@link BeanWriter}.
 */
public enum IncludeExclude {
  /**
   * Only allow the provided properties to be read/written
   */
  INCLUDE,
  /**
   * Exclude the provided properties from being read/written.
   */
  EXCLUDE;

  boolean isExclude() {
    return this == EXCLUDE;
  }
}
