package nl.naturalis.common.path;

import nl.naturalis.common.invoke.BeanReader;
import nl.naturalis.common.invoke.NoPublicGettersException;
import nl.naturalis.common.invoke.NoSuchPropertyException;

import static nl.naturalis.common.ObjectMethods.isEmpty;
import static nl.naturalis.common.path.PathWalkerException.*;

@SuppressWarnings({"rawtypes", "unchecked"})
final class BeanSegmentReader extends SegmentReader<Object> {

  BeanSegmentReader(boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    super(suppressExceptions, keyDeserializer);
  }

  @Override
  Object read(Object bean, Path path, int segment) {
    String property = path.segment(segment);
    if (isEmpty(property)) {
      return deadEnd(emptySegment(path, segment));
    }
    BeanReader reader;
    try {
      reader = new BeanReader(bean.getClass());
    } catch (NoPublicGettersException e) {
      return deadEnd(terminalValue(path, segment, bean.getClass()));
    }
    try {
      Object val = reader.read(bean, path.segment(segment));
      return new ObjectReader(se, kd).read(val, path, ++segment);
    } catch (NoSuchPropertyException e) {
      return deadEnd(noSuchProperty(path, segment, bean.getClass()));
    }
  }

}
