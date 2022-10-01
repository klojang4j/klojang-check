package nl.naturalis.common.collection;

import nl.naturalis.check.Check;
import nl.naturalis.common.ArrayType;
import nl.naturalis.common.Tuple2;
import nl.naturalis.common.x.collection.ImmutableMap;

import java.util.*;
import java.util.function.UnaryOperator;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static nl.naturalis.check.CommonChecks.instanceOf;
import static nl.naturalis.common.ClassMethods.*;
import static nl.naturalis.common.ObjectMethods.ifNotNull;
import static nl.naturalis.common.ObjectMethods.ifNull;

final class GreedyTypeMap<V> extends ImmutableMap<Class<?>, V> implements
    TypeMap<V> {

  private final HashMap<Class<?>, V> backend;
  private final boolean autobox;

  GreedyTypeMap(HashMap<Class<?>, V> src, boolean autobox) {
    this.backend = src;
    this.autobox = autobox;
  }

  @Override
  public V get(Object key) {
    Class<?> type = Check.notNull(key)
        .is(instanceOf(), Class.class)
        .ok(Class.class::cast);
    Tuple2<Class<?>, V> entry = find(type);
    if (entry.second() == NULL) {
      return null;
    }
    if (type != entry.first()) {
      backend.put(type, entry.second());
    }
    return entry.second();
  }

  @Override
  public boolean containsKey(Object key) {
    Class<?> type = Check.notNull(key)
        .is(instanceOf(), Class.class)
        .ok(Class.class::cast);
    Tuple2<Class<?>, V> entry = find(type);
    if (entry.second() == NULL) {
      return false;
    }
    if (type != entry.first()) {
      backend.put(type, entry.second());
    }
    return true;
  }

  private Tuple2<Class<?>, V> find(Class<?> type) {
    V val;
    if ((val = backend.get(type)) != null) {
      return Tuple2.of(type, val);
    }
    Tuple2<Class<?>, V> result = null;
    if (type.isArray()) {
      result = findArrayType(type);
    } else if (type.isPrimitive()) {
      if (autobox) {
        result = find(box(type));
      }
    } else if (type.isInterface()) {
      result = findInterface(type);
    } else if ((result = findSuperClass(type)) == null) {
      result = findInterface(type);
    }
    if (result == null) {
      return getDefaultValue();
    }
    return result;
  }

  private Tuple2<Class<?>, V> findSuperClass(Class<?> type) {
    List<Class<?>> supertypes = getAncestors(type);
    for (Class<?> c : supertypes) {
      if (c == Object.class) {
        break; // that's our last resort
      }
      V val = backend.get(c);
      if (val != null) {
        return Tuple2.of(c, val);
      }
    }
    return null;
  }

  private Tuple2<Class<?>, V> findInterface(Class<?> type) {
    Set<Class<?>> supertypes = getAllInterfaces(type);
    for (Class<?> c : supertypes) {
      V val = backend.get(c);
      if (val != null) {
        return Tuple2.of(c, val);
      }
    }
    return null;
  }

  private Tuple2<Class<?>, V> findArrayType(Class<?> type) {
    ArrayType arrayType = ArrayType.forClass(type);
    if (arrayType.baseType().isPrimitive()) {
      if (autobox) {
        return find(arrayType.box());
      }
    }
    Tuple2<Class<?>, V> result;
    if (arrayType.baseType().isInterface()) {
      if ((result = findInterfaceArray(arrayType)) != null) {
        return result;
      }
    } else if ((result = findSuperClassArray(arrayType)) != null) {
      return result;
    } else if ((result = findInterfaceArray(arrayType)) != null) {
      return result;
    }
    return ifNotNull(backend.get(Object[].class), v -> Tuple2.of(Object[].class, v));
  }

  private Tuple2<Class<?>, V> findSuperClassArray(ArrayType arrayType) {
    List<Class<?>> supertypes = getAncestors(arrayType.baseType());
    for (Class<?> c : supertypes) {
      if (c == Object.class) {
        break;
      }
      Class<?> arrayClass = arrayType.toClass(c);
      V val = backend.get(arrayClass);
      if (val != null) {
        return Tuple2.of(arrayClass, val);
      }
    }
    return null;
  }

  private Tuple2<Class<?>, V> findInterfaceArray(ArrayType arrayType) {
    Set<Class<?>> supertypes = getAllInterfaces(arrayType.baseType());
    for (Class<?> c : supertypes) {
      Class<?> arrayClass = arrayType.toClass(c);
      V val = backend.get(arrayClass);
      if (val != null) {
        return Tuple2.of(arrayClass, val);
      }
    }
    return null;
  }

  private static final Object NULL = new Object();
  private Tuple2<Class<?>, V> defVal;

  // The value associated with Object.class, or null if
  // the map does not contain key Object.class
  @SuppressWarnings({"unchecked"})
  private Tuple2<Class<?>, V> getDefaultValue() {
    if (defVal == null) {
      V val = ifNull(backend.get(Object.class), (V) NULL);
      defVal = new Tuple2<>(Object.class, val);
    }
    return defVal;
  }

  @Override
  public int size() {
    return backend.size();
  }

  @Override
  public boolean isEmpty() {
    return backend.isEmpty();
  }

  @Override
  public boolean containsValue(Object value) {
    return backend.containsValue(value);
  }

  @Override
  public int hashCode() {
    return backend.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return backend.equals(obj);
  }

  @Override
  public String toString() {
    return backend.toString();
  }

  @Override
  public Set<Class<?>> keySet() {
    return Set.copyOf(backend.keySet());
  }

  @Override
  public Collection<V> values() {
    return Set.copyOf(backend.values());
  }

  @Override
  public Set<Entry<Class<?>, V>> entrySet() {
    return immutableEntrySet();
  }

  private Set<Entry<Class<?>, V>> immutableEntrySet() {
    return backend.entrySet()
        .stream()
        .map(makeImmutable())
        .collect(toUnmodifiableSet());
  }

  private UnaryOperator<Entry<Class<?>, V>> makeImmutable() {
    return e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey(), e.getValue());
  }

}
