package nl.naturalis.common.collection;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.util.Arrays;
import java.util.HashMap;

import static nl.naturalis.check.CommonChecks.keyIn;
import static nl.naturalis.common.collection.FixedTypeMapBuilder.duplicateKey;

final class GreedyTypeMapBuilder<V> implements TypeMapBuilder<V> {

  private final HashMap<Class<?>, V> tmp = new HashMap<>();

  private boolean autobox = true;

  GreedyTypeMapBuilder() {}

  @Override
  public GreedyTypeMapBuilder<V> autobox(boolean autobox) {
    this.autobox = autobox;
    return this;
  }

  @Override
  public GreedyTypeMapBuilder<V> add(Class<?> type, V value) {
    Check.notNull(type, Param.TYPE).isNot(keyIn(), tmp, duplicateKey(type));
    Check.notNull(value, Param.VALUE);
    tmp.put(type, value);
    return this;
  }

  @Override
  public GreedyTypeMapBuilder<V> addMultiple(V value, Class<?>... types) {
    Check.notNull(types, "types").ok(Arrays::stream).forEach(t -> add(t, value));
    return this;
  }

  @Override
  public GreedyTypeMap<V> freeze() {
    return new GreedyTypeMap<>(tmp, autobox);
  }

}
