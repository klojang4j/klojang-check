package nl.naturalis.common.collection;

import nl.naturalis.common.ArrayType;

import java.util.*;

import static nl.naturalis.common.ClassMethods.*;

final class TypeNode {

  static final TypeNode[] NO_SUBTYPES = new TypeNode[0];

  final Class<?> type;
  final Object value;
  final TypeNode[] subclasses;
  final TypeNode[] extensions;

  TypeNode(Class<?> type,
      Object value,
      TypeNode[] subclasses,
      TypeNode[] extensions) {
    this.type = type;
    this.value = value;
    this.subclasses = subclasses;
    this.extensions = extensions;
  }

  @SuppressWarnings({"unchecked"})
  <T> T value() {
    return (T) value;
  }

  <T> T get(Class<?> type) {
    return type.isInterface() ? findInterface(type) : findClass(type);
  }

  @SuppressWarnings({"unchecked"})
  <T> T getPrimitive(Class<?> type) {
    return (T) findClass(type, subclasses);
  }

  void collectTypes(List<Class<?>> bucket) {
    for (var node : extensions) {
      bucket.add((node.type));
    }
    for (var node : subclasses) {
      bucket.add(node.type);
    }
    for (var node : extensions) {
      node.collectTypes(bucket);
    }
    for (var node : subclasses) {
      node.collectTypes(bucket);
    }
  }

  <E> void collectValues(Set<E> bucket) {
    for (var node : extensions) {
      bucket.add(node.value());
      node.collectValues(bucket);
    }
    for (var node : subclasses) {
      bucket.add(node.value());
      node.collectValues(bucket);
    }
  }

  <E> void collectEntries(List<Map.Entry<Class<?>, E>> bucket) {
    for (var node : extensions) {
      bucket.add(new AbstractMap.SimpleImmutableEntry<>(node.type, node.value()));
      node.collectEntries(bucket);
    }
    for (var node : subclasses) {
      bucket.add(new AbstractMap.SimpleImmutableEntry<>(node.type, node.value()));
      node.collectEntries(bucket);
    }
  }

  @SuppressWarnings({"unchecked"})
  private <T> T findClass(Class<?> type) {
    if (!isSupertype(this.type, type)) {
      return null;
    }
    Object val;
    if ((val = findAsSubclass(type)) == null) {
      if ((val = findAsImpl(type)) == null) {
        val = this.value;
      }
    }
    return (T) val;
  }

  @SuppressWarnings({"unchecked"})
  private <T> T findInterface(Class<?> type) {
    if (!isSupertype(this.type, type)) {
      return null;
    }
    Object val;
    if ((val = findAsExtension(type)) == null) {
      val = this.value;
    }
    return (T) val;
  }

  private Object findAsSubclass(Class<?> type) {
    return findClass(type, subclasses);
  }

  private Object findAsImpl(Class<?> type) {
    return findClass(type, extensions);
  }

  private Object findAsExtension(Class<?> type) {
    for (TypeNode node : extensions) {
      Object val = node.findInterface(type);
      if (val != null) {
        return val;
      }
    }
    return null;
  }

  private static Object findClass(Class<?> type, TypeNode[] nodes) {
    for (TypeNode node : nodes) {
      Object val = node.findClass(type);
      if (val != null) {
        return val;
      }
    }
    return null;
  }

}
