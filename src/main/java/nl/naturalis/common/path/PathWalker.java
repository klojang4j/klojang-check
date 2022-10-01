package nl.naturalis.common.path;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonProperties.length;

/**
 * A {@code PathWalker} lets you read and write deeply nested values using {@link Path} objects. It
 * can read almost any type of object it encounters as it walks down the path towards the last path
 * segment: JavaBeans, maps, collections, arrays, scalars, and opaque types like {@code String}. It
 * can also write to most of them. A {@code PathWalker} can be useful when processing large batches
 * of sparsely populated maps or objects and/or it doesn't really matter whether a deeply nested
 * value is {@code null} or just not present at all. By default, the {@code PathWalker} will return
 * {@code null} in either case (although you can change this).
 *
 * @author Ayco Holleman
 */
@SuppressWarnings({"unchecked"})
public final class PathWalker {

  private static final String PATHS = "paths";

  private final Path[] paths;
  private final boolean se;
  private final KeyDeserializer kd;

  /**
   * Creates a {@code PathWalker} for the specified paths.
   *
   * @param paths One or more paths representing possibly deeply-nested properties
   */
  public PathWalker(Path... paths) {
    Check.that(paths, PATHS).isNot(empty()).is(deepNotNull());
    this.paths = Arrays.copyOf(paths, paths.length);
    this.se = true;
    this.kd = null;
  }

  /**
   * Creates a {@code PathWalker} for the specified paths.
   *
   * @param paths The paths to walk through the provided host objects
   */
  public PathWalker(String... paths) {
    Check.that(paths, PATHS).isNot(empty()).is(deepNotNull());
    this.paths = Arrays.stream(paths).map(Path::from).toArray(Path[]::new);
    this.se = true;
    this.kd = null;
  }

  /**
   * Creates a {@code PathWalker} for the specified paths.
   *
   * @param paths The paths to walk through the provided host objects
   */
  public PathWalker(List<Path> paths) {
    this(paths, true);
  }

  /**
   * Creates a {@code PathWalker} for the specified paths.
   *
   * @param paths The action to take if a path could not be read or written
   * @param suppressExceptions If {@code true}, the {@code read} methods will return {@code null}
   *     for paths that could not be read. The {@code write} methods will quietly return without
   *     having written the value. If {@code false}, a {@link PathWalkerException} will be thrown
   *     detailing the error.
   */
  public PathWalker(List<Path> paths, boolean suppressExceptions) {
    Check.that(paths, PATHS).isNot(empty()).is(deepNotNull());
    this.paths = paths.toArray(Path[]::new);
    this.se = suppressExceptions;
    this.kd = null;
  }

  /**
   * Creates a {@code PathWalker} for the specified paths.
   *
   * @param paths The paths to walk
   * @param suppressExceptions If {@code true}, the {@code read} methods will return {@code null}
   *     for paths that could not be read. The {@code write} methods will quietly return without
   *     having written the value. If {@code false}, a {@link PathWalkerException} will be thrown
   *     detailing the error.
   * @param keyDeserializer A function that converts path segments to map keys. You need to provide
   *     this if the host objects are, or contain, {@link Map} instances with non-string keys.
   */
  public PathWalker(List<Path> paths, boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    Check.that(paths, PATHS).is(deepNotEmpty());
    Check.notNull(keyDeserializer, "keyDeserializer");
    this.paths = paths.toArray(Path[]::new);
    this.se = suppressExceptions;
    this.kd = keyDeserializer;
  }

  // For internal use
  PathWalker(Path path, boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    this.paths = new Path[] {path};
    this.se = suppressExceptions;
    this.kd = keyDeserializer;
  }

  /**
   * Returns the values of all paths specified through the constructor.
   *
   * @param host the object to read the values from
   * @return the values of all paths specified through the constructor
   * @throws PathWalkerException If {@code suppressExceptions} is false and the {@code PathWalker}
   *     fails to retrieve the values of one or more paths.
   */
  public Object[] readValues(Object host) throws PathWalkerException {
    ObjectReader reader = new ObjectReader(se, kd);
    return Arrays.stream(paths).map(path -> reader.read(host, path, 0)).toArray();
  }

  /**
   * Reads the values of all paths specified through the constructor.
   *
   * @param host the object to read the path values from
   * @param output an array into which to place the values. The length of the output array must be
   *     equal to, or greater than the number of paths specified through the constructor.
   * @throws PathWalkerException If {@code suppressExceptions} is false and the {@code PathWalker}
   *     fails to retrieve the values of one or more paths.
   */
  public void readValues(Object host, Object[] output) throws PathWalkerException {
    Check.notNull(output, Param.OUTPUT).has(length(), gte(), paths.length);
    ObjectReader reader = new ObjectReader(se, kd);
    for (int i = 0; i < paths.length; ++i) {
      output[i] = reader.read(host, paths[i], 0);
    }
  }

  /**
   * Reads the values of all paths and inserts them into the provided path-to-value map.
   *
   * @param host the object from which to read the values
   * @param output The {@code Map} into which to put the values
   * @throws PathWalkerException If {@code suppressExceptions} is false and the {@code PathWalker}
   *     fails to retrieve the values of one or more paths.
   */
  public void readValues(Object host, Map<Path, Object> output) throws PathWalkerException {
    Check.notNull(output, Param.OUTPUT);
    ObjectReader reader = new ObjectReader(se, kd);
    Arrays.stream(paths).forEach(p -> output.put(p, reader.read(host, p, 0)));
  }

  /**
   * Reads the value of the first path specified through the constructor. Convenient if you
   * specified just one path.
   *
   * @param <T> The type of the value being returned
   * @param host the object from which to read the value
   * @return the value of the first path specified through the constructor
   * @throws PathWalkerException If {@code suppressExceptions} is false and the {@code PathWalker}
   *     fails to retrieve the value of the first path.
   */
  public <T> T read(Object host) {
    return (T) new ObjectReader(se, kd).read(host, paths[0], 0);
  }

  /**
   * Sets the values of the paths specified through the constructor. The provided array of values
   * must have the same length as the number of paths.
   *
   * @param host the object to which to write the values
   * @param values The values to write
   * @return the number of successfully written values
   */
  public int writeValues(Object host, Object... values) {
    Check.notNull(values, Param.VALUES).has(length(), eq(), paths.length);
    ObjectWriter writer = new ObjectWriter(se, kd);
    int x = 0;
    for (int i = 0; i < paths.length; ++i) {
      if (writer.write(host, paths[i], values[i])) {
        ++x;
      }
    }
    return x;
  }

  /**
   * Sets the value of the first path specified through the constructor. Convenient if you specified
   * just one path.
   *
   * @param host the object to write the value to
   * @param value The value to write
   * @return {@code true} if the value was successfully written
   */
  public boolean write(Object host, Object value) {
    return new ObjectWriter(se, kd).write(host, paths[0], value);
  }
}
