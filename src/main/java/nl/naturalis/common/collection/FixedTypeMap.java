package nl.naturalis.common.collection;

import java.util.*;
import java.util.function.UnaryOperator;

final class FixedTypeMap<V> extends NonExpandingTypeMap<V> {

  private final Map<Class<?>, V> backend;

  FixedTypeMap(HashMap<Class<?>, V> src, boolean autobox) {
    super(autobox);
    this.backend = Map.copyOf(src);
  }

  @Override
  Map<Class<?>, V> backend() {
    return backend;
  }

  @Override
  public Set<Class<?>> keySet() {
    return backend.keySet();
  }

  @Override
  public Collection<V> values() {
    // Keep behaviour consistent across impls
    return Set.copyOf(backend.values());
  }

  @Override
  public Set<Entry<Class<?>, V>> entrySet() {
    return backend.entrySet();
  }

  private UnaryOperator<Entry<Class<?>, V>> makeImmutable() {
    return e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey(), e.getValue());
  }

}
