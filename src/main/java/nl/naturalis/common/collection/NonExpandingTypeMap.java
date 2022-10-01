package nl.naturalis.common.collection;

import nl.naturalis.check.Check;
import nl.naturalis.common.ArrayType;
import nl.naturalis.common.x.collection.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static nl.naturalis.check.CommonChecks.instanceOf;
import static nl.naturalis.common.ClassMethods.*;
import static nl.naturalis.common.ObjectMethods.ifNotNull;
import static nl.naturalis.common.ObjectMethods.ifNull;

/*
 * Currently only extended by TypeHashMap, but could be base class for any TypeMap
 * that relies on a regular map to do the lookups.
 */
abstract sealed class NonExpandingTypeMap<V> extends
    ImmutableMap<Class<?>, V> implements TypeMap<V> permits
    TypeTreeMap, FixedTypeMap {

  private final boolean autobox;

  NonExpandingTypeMap(boolean autobox) {
    this.autobox = autobox;
  }

  abstract Map<Class<?>, V> backend();

  @Override
  public V get(Object key) {
    Class<?> type = Check.notNull(key)
        .is(instanceOf(), Class.class)
        .ok(Class.class::cast);
    return find(type);
  }

  @Override
  public boolean containsKey(Object key) {
    Class<?> type = Check.notNull(key)
        .is(instanceOf(), Class.class)
        .ok(Class.class::cast);
    return find(type) != null;
  }

  private V find(Class<?> type) {
    V val;
    if ((val = backend().get(type)) != null) {
      return val;
    }
    if (type.isArray()) {
      val = findArrayType(type);
    } else if (type.isPrimitive()) {
      if (autobox) {
        val = find(box(type));
      }
    } else if (type.isInterface()) {
      val = findInterface(type);
    } else if ((val = findSuperClass(type)) == null) {
      val = findInterface(type);
    }
    if (val == null) {
      return getDefaultValue();
    }
    return val;
  }

  private V findSuperClass(Class<?> type) {
    List<Class<?>> supertypes = getAncestors(type);
    for (Class<?> c : supertypes) {
      if (c == Object.class) {
        break; // that's our last resort
      }
      V val = backend().get(c);
      if (val != null) {
        return val;
      }
    }
    return null;
  }

  private V findInterface(Class<?> type) {
    Set<Class<?>> supertypes = getAllInterfaces(type);
    for (Class<?> c : supertypes) {
      V val = backend().get(c);
      if (val != null) {
        return val;
      }
    }
    return null;
  }

  private V findArrayType(Class<?> type) {
    ArrayType arrayType = ArrayType.forClass(type);
    if (arrayType.baseType().isPrimitive()) {
      if (autobox) {
        return find(arrayType.box());
      }
    }
    V result = null;
    if (arrayType.baseType().isInterface()) {
      if ((result = findInterfaceArray(arrayType)) != null) {
        return result;
      }
    } else if ((result = findSuperClassArray(arrayType)) != null) {
      return result;
    } else if ((result = findInterfaceArray(arrayType)) != null) {
      return result;
    }
    return backend().get(Object[].class);

  }

  private V findSuperClassArray(ArrayType arrayType) {
    List<Class<?>> supertypes = getAncestors(arrayType.baseType());
    for (Class<?> c : supertypes) {
      if (c == Object.class) {
        break;
      }
      V val = backend().get(arrayType.toClass(c));
      if (val != null) {
        return val;
      }
    }
    return null;
  }

  private V findInterfaceArray(ArrayType arrayType) {
    Set<Class<?>> supertypes = getAllInterfaces(arrayType.baseType());
    for (Class<?> c : supertypes) {
      V val = backend().get(arrayType.toClass(c));
      if (val != null) {
        return val;
      }
    }
    return null;
  }

  @SuppressWarnings({"unchecked"})
  private final V NULL = (V) new Object();
  private V defVal;

  // The value associated with Object.class, or null if
  // the map does not contain key Object.class
  private V getDefaultValue() {
    if (defVal == null) {
      defVal = backend().get(Object.class);
    }
    return defVal;
  }

  @Override
  public int size() {
    return backend().size();
  }

  @Override
  public boolean isEmpty() {
    return backend().isEmpty();
  }

  @Override
  public boolean containsValue(Object value) {
    return backend().containsValue(value);
  }

  @Override
  public int hashCode() {
    return backend().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return backend().equals(obj);
  }

  @Override
  public String toString() {
    return backend().toString();
  }

}
