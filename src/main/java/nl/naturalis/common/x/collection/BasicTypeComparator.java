package nl.naturalis.common.x.collection;

import nl.naturalis.common.ClassMethods;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import static nl.naturalis.common.ClassMethods.countAncestors;
import static nl.naturalis.common.ClassMethods.getAllInterfaces;

public final class BasicTypeComparator implements Comparator<Class<?>> {

  @Override
  public int compare(Class<?> c1, Class<?> c2) {
    if (c1 == c2) {
      return 0;
    }
    if (c1 == Object.class) {
      return 1;
    }
    if (c2 == Object.class) {
      return -1;
    }
    int x;
    if (c1.isInterface()) {
      if (c2.isInterface()) {
        if ((x = countInterfaces(c2) - countInterfaces(c1)) != 0) {
          return x;
        }
      }
      return 1;
    }
    if (c2.isInterface()) {
      return -1;
    }
    if ((x = countAncestors(c2) - countAncestors(c1)) != 0) {
      return x;
    }
    if ((x = countInterfaces(c2) - countInterfaces(c1)) != 0) {
      return x;
    }
    if (c1.isArray() && c2.isArray()) {
      return compare(c1.getComponentType(), c2.getComponentType());
    }
    return c1.hashCode() - c2.hashCode();
  }



  private static int countInterfaces(Class<?> c) {
    return getAllInterfaces(c).size();
  }

}
