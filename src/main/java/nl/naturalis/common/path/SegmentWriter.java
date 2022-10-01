package nl.naturalis.common.path;

abstract sealed class SegmentWriter<T> permits ArraySegmentWriter, BeanSegmentWriter,
    ListSegmentWriter, MapSegmentWriter, PrimitiveArraySegmentWriter {

  final boolean se;
  final KeyDeserializer kd;

  SegmentWriter(boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    this.se = suppressExceptions;
    this.kd = keyDeserializer;
  }

  abstract boolean write(T obj, Path path, Object value);

  boolean deadEnd(PathWalkerException.Factory excFactory) {
    if (se) {
      return false;
    }
    throw excFactory.get();
  }

}
