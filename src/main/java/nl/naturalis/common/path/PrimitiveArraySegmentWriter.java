package nl.naturalis.common.path;

import java.util.OptionalInt;

import static java.lang.invoke.MethodHandles.arrayElementSetter;
import static java.lang.invoke.MethodHandles.arrayLength;
import static nl.naturalis.common.NumberMethods.toInt;
import static nl.naturalis.common.path.PathWalkerException.*;
import static nl.naturalis.common.x.invoke.InvokeUtils.*;

final class PrimitiveArraySegmentWriter extends SegmentWriter<Object> {

  PrimitiveArraySegmentWriter(boolean suppressExceptions,
      KeyDeserializer keyDeserializer) {
    super(suppressExceptions, keyDeserializer);
  }

  @Override
  boolean write(Object array, Path path, Object value) {
    int segment = path.size() - 1;
    OptionalInt opt = toInt(path.segment(segment));
    if (opt.isEmpty()) {
      return deadEnd(indexExpected(path, segment));
    }
    int idx = opt.getAsInt();
    int len = getArrayLength(array);
    if (idx >= 0 && idx < len) {
      setArrayElement(array, idx, value);
      return true;
    }
    return deadEnd(indexOutOfBounds(path, segment));
  }

}
