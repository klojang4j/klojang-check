package nl.naturalis.common;

import nl.naturalis.common.collection.IntList;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static nl.naturalis.common.Morph.convert;
import static nl.naturalis.common.x.invoke.InvokeUtils.*;
import static nl.naturalis.common.x.invoke.InvokeUtils.setArrayElement;

@SuppressWarnings({"rawtypes", "unchecked"})
final class MorphToArray {

  static <T> T morph(Object obj, Class targetType) {
    if (obj.getClass().isArray()) {
      return arrayToArray(obj, targetType);
    } else if (obj instanceof Collection c) {
      return collectionToArray(c, targetType);
    } else if (obj instanceof String s) {
      return stringToArray(s, targetType);
    } else if (obj instanceof IntList il) {
      return intListToArray(il, targetType);
    }
    return objToArray(obj, targetType);
  }

  private static <T> T arrayToArray(Object inputArray, Class targetType) {
    int sz = getArrayLength(inputArray);
    Object outputArray = newArray(targetType, sz);
    for (int i = 0; i < sz; ++i) {
      Object in = getArrayElement(inputArray, i);
      Object out = convert(in, targetType.getComponentType());
      setArrayElement(outputArray, i, out);
    }
    return (T) outputArray;
  }

  private static <E, T> T collectionToArray(Collection collection,
      Class targetType) {
    Object array = newArray(targetType, collection.size());
    int i = 0;
    for (Object in : collection) {
      Object elem = convert(in, targetType.getComponentType());
      setArrayElement(array, i++, elem);
    }
    return (T) array;
  }

  private static <T> T intListToArray(IntList il, Class targetType) {
    Class c = targetType;
    if (c == int[].class) {
      return (T) il.toArray();
    } else if (c == Integer[].class) {
      return (T) ArrayMethods.box(il.toArray());
    }
    Object array = newArray(targetType, il.size());
    for (int i = 0; i < il.size(); ++i) {
      Object elem = convert(il.get(i), targetType.getComponentType());
      setArrayElement(array, i, elem);
    }
    return (T) array;
  }

  private static <T> T stringToArray(String s, Class targetType) {
    if (targetType == char[].class) {
      return (T) s.toCharArray();
    } else if (targetType == byte[].class) {
      return (T) s.getBytes(StandardCharsets.UTF_8);
    }
    return objToArray(s, targetType);
  }

  static <T> T objToArray(Object obj, Class targetType) {
    Object array = newArray(targetType, 1);
    setArrayElement(array, 0, convert(obj, targetType.getComponentType()));
    return (T) array;
  }

}
