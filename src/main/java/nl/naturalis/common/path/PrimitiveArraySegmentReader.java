package nl.naturalis.common.path;

import nl.naturalis.common.invoke.InvokeException;

import java.util.OptionalInt;

import static nl.naturalis.common.NumberMethods.toInt;
import static nl.naturalis.common.path.PathWalkerException.*;
import static nl.naturalis.common.x.invoke.InvokeUtils.*;

final class PrimitiveArraySegmentReader extends SegmentReader<Object> {

  PrimitiveArraySegmentReader(boolean suppressExceptions,
      KeyDeserializer keyDeserializer) {
    super(suppressExceptions, keyDeserializer);
  }

  @Override
  Object read(Object array, Path path, int segment) {
    OptionalInt opt = toInt(path.segment(segment));
    if (opt.isEmpty()) {
      return deadEnd(indexExpected(path, segment));
    }
    int idx = opt.getAsInt();
    int len = getArrayLength(array);
    if (idx >= 0 && idx < len) {
      Object val = getArrayElement(array, idx);
      return new ObjectReader(se, kd).read(val, path, ++segment);
    }
    return deadEnd(indexOutOfBounds(path, segment));
  }

}


