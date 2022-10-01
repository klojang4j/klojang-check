package nl.naturalis.common.path;

import java.util.OptionalInt;

import static nl.naturalis.common.NumberMethods.toInt;
import static nl.naturalis.common.ObjectMethods.isEmpty;
import static nl.naturalis.common.path.PathWalkerException.*;

final class ArraySegmentReader extends SegmentReader<Object[]> {

  ArraySegmentReader(boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    super(suppressExceptions, keyDeserializer);
  }

  @Override
  Object read(Object[] array, Path path, int segment) {
    OptionalInt opt = toInt(path.segment(segment));
    if (opt.isEmpty()) {
      return deadEnd(indexExpected(path, segment));
    }
    int idx = opt.getAsInt();
    if (idx < array.length) {
      return new ObjectReader(se, kd).read(array[idx], path, ++segment);
    }
    return deadEnd(indexOutOfBounds(path, segment));
  }

}
