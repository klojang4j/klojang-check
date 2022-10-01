package nl.naturalis.common.path;

abstract sealed class SegmentReader<T> permits ArraySegmentReader, BeanSegmentReader,
    CollectionSegmentReader, MapSegmentReader, PrimitiveArraySegmentReader {

  final boolean se;
  final KeyDeserializer kd;

  SegmentReader(boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    this.se = suppressExceptions;
    this.kd = keyDeserializer;
  }

  abstract Object read(T obj, Path path, int segment);

  Object deadEnd(PathWalkerException.Factory excFactory) {
    if (se) {
      return null;
    }
    throw excFactory.get();
  }

}
