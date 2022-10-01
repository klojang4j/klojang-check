package nl.naturalis.common.path;

import java.util.Map;

import static nl.naturalis.common.path.ErrorCode.*;
import static nl.naturalis.common.path.PathWalkerException.*;

@SuppressWarnings({"rawtypes", "unchecked"})
final class MapSegmentWriter extends SegmentWriter<Map> {

  MapSegmentWriter(boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    super(suppressExceptions, keyDeserializer);
  }

  @Override
  boolean write(Map map, Path path, Object value) {
    int segment = path.size() - 1;
    Object key;
    if (kd == null) {
      key = path.segment(segment);
    } else {
      try {
        key = kd.deserialize(path, segment);
      } catch (KeyDeserializationException e) {
        return deadEnd(keyDeserializationFailed(path, segment, e));
      }
    }
    try {
      map.put(key, value);
    } catch (UnsupportedOperationException e) {
      return deadEnd(notModifiable(path, segment, Map.class));
    }
    return true;
  }

}
