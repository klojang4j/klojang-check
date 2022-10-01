package nl.naturalis.common.collection;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.collection.ImmutableSet;

import java.util.*;

import static nl.naturalis.check.CommonChecks.sameAs;
import static nl.naturalis.check.CommonProperties.type;

abstract non-sealed class AbstractTypeSet extends ImmutableSet<Class<?>> implements
    TypeSet {

  private final TypeMap<Object> map;

  AbstractTypeSet() {
    this.map = createBackend();
  }

  abstract TypeMap<Object> createBackend();

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return Check.notNull(o).has(type(), sameAs(), Class.class).ok(map::containsKey);
  }

  @Override
  public Iterator<Class<?>> iterator() {
    return map.keySet().iterator();
  }

  @Override
  public Object[] toArray() {
    return map.keySet().toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    Check.notNull(a);
    return map.keySet().toArray(a);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    Check.notNull(c);
    return c.stream().filter(map::containsKey).count() == c.size();
  }

  @Override
  public int hashCode() {
    return map.keySet().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return map.keySet().equals(obj);
  }

  @Override
  public String toString() {
    return map.keySet().toString();
  }

}
