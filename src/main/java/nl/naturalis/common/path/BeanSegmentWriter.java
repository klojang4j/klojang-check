package nl.naturalis.common.path;

import nl.naturalis.common.invoke.BeanWriter;
import nl.naturalis.common.invoke.IllegalAssignmentException;
import nl.naturalis.common.invoke.NoPublicSettersException;
import nl.naturalis.common.invoke.NoSuchPropertyException;

import static nl.naturalis.common.ObjectMethods.isEmpty;
import static nl.naturalis.common.path.PathWalkerException.noSuchProperty;
import static nl.naturalis.common.path.PathWalkerException.*;

@SuppressWarnings({"rawtypes", "unchecked"})
final class BeanSegmentWriter extends SegmentWriter<Object> {

  BeanSegmentWriter(boolean suppressExceptions, KeyDeserializer keyDeserializer) {
    super(suppressExceptions, keyDeserializer);
  }

  @Override
  boolean write(Object bean, Path path, Object value) {
    int segment = path.size() - 1;
    String property = path.segment(segment);
    if (isEmpty(property)) {
      return deadEnd(emptySegment(path, segment));
    }
    BeanWriter bw;
    try {
      bw = new BeanWriter(bean.getClass());
    } catch (NoPublicSettersException e) {
      return deadEnd(terminalValue(path, segment, bean.getClass()));
    }
    try {
      bw.write(bean, property, value);
      return true;
    } catch (NoSuchPropertyException e) {
      return deadEnd(noSuchProperty(path, segment, bean.getClass()));
    } catch (IllegalAssignmentException e) {
      return deadEnd(typeMismatch(path, segment, e.getMessage()));
    } catch (Throwable t) {
      return deadEnd(unexpectedError(path, segment, t));
    }
  }

}
