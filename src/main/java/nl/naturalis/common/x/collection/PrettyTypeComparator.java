package nl.naturalis.common.x.collection;

import nl.naturalis.common.path.Path;

import java.util.Comparator;

import static nl.naturalis.common.ClassMethods.*;

public final class PrettyTypeComparator implements Comparator<Class<?>> {

  @Override
  public int compare(Class<?> c1, Class<?> c2) {
    if (c1 == c2) {
      return 0;
    }
    if (c1.isPrimitive()) {
      return -1;
    }
    if (c2.isPrimitive()) {
      return 1;
    }
    if (isWrapper(c1)) {
      return -1;
    }
    if (isWrapper(c2)) {
      return 1;
    }
    if (c1.isEnum()) {
      return -1;
    }
    if (c2.isEnum()) {
      return 1;
    }
    if (c1 == Object.class) {
      return 1;
    }
    if (c2 == Object.class) {
      return -1;
    }
    if (c1.isArray()) {
      if (c2.isArray()) {
        return compare(c1.getComponentType(), c2.getComponentType());
      }
      return 1;
    }
    if (c2.isArray()) {
      return -1;
    }
    if (c1.isInterface()) {
      if (c2.isInterface()) {
        if (getAllInterfaces(c1).size() < getAllInterfaces(c2).size()) {
          return 1;
        }
        if (getAllInterfaces(c1).size() > getAllInterfaces(c2).size()) {
          return -1;
        }
      }
      return 1;
    }
    if (c2.isInterface()) {
      return -1;
    }
    if (countAncestors(c1) < countAncestors(c2)) {
      return 1;
    }
    if (countAncestors(c1) > countAncestors(c2)) {
      return -1;
    }
    // Compare the number of implemented interfaces for regular
    // classes. Thus, classes not implementing any interface are
    // regarded as more primitive and should come first
    if (getAllInterfaces(c1).size() < getAllInterfaces(c2).size()) {
      return 1;
    }
    if (getAllInterfaces(c1).size() > getAllInterfaces(c2).size()) {
      return -1;
    }
    Path p0 = Path.from(c1.getName()).reverse();
    Path p1 = Path.from(c2.getName()).reverse();
    return p0.compareTo(p1);
  }

}
