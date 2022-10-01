package nl.naturalis.common.path;

import java.util.List;
import java.util.OptionalInt;

import static nl.naturalis.common.NumberMethods.toInt;
import static nl.naturalis.common.path.PathWalkerException.indexExpected;
import static nl.naturalis.common.path.PathWalkerException.*;

@SuppressWarnings({"rawtypes", "unchecked"})
final class ListSegmentWriter extends SegmentWriter<List> {

  ListSegmentWriter(boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    super(suppressExceptions, keyDeserializer);
  }

  @Override
  boolean write(List list, Path path, Object value) {
    int segment = path.size() - 1;
    OptionalInt opt = toInt(path.segment(segment));
    if (opt.isEmpty()) {
      return deadEnd(indexExpected(path, segment));
    }
    int idx = opt.getAsInt();
    if (idx < list.size()) {
      try {
        list.set(opt.getAsInt(), value);
      } catch (UnsupportedOperationException e) {
        return deadEnd(notModifiable(path.parent(), segment, List.class));
      }
      return true;
    }
    return deadEnd(indexOutOfBounds(path, segment));
  }

}
