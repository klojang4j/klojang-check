package nl.naturalis.common.collection;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

import static nl.naturalis.check.CommonChecks.keyIn;
import static nl.naturalis.common.collection.DuplicateValueException.ValueType.KEY;

final class FixedTypeMapBuilder<V> implements TypeMapBuilder<V> {

  static Supplier<DuplicateValueException> duplicateKey(Class<?> type) {
    return () -> new DuplicateValueException(KEY, type);
  }

  private final HashMap<Class<?>, V> tmp = new HashMap<>();

  private boolean autobox = true;

  FixedTypeMapBuilder() {}

  @Override
  public FixedTypeMapBuilder<V> autobox(boolean autobox) {
    this.autobox = autobox;
    return this;
  }

  @Override
  public FixedTypeMapBuilder<V> add(Class<?> type, V value) {
    Check.notNull(type, Param.TYPE).isNot(keyIn(), tmp, duplicateKey(type));
    Check.notNull(value, Param.VALUE);
    tmp.put(type, value);
    return this;
  }

  @Override
  public FixedTypeMapBuilder<V> addMultiple(V value, Class<?>... types) {
    Check.notNull(types, "types").ok(Arrays::stream).forEach(t -> add(t, value));
    return this;
  }

  @Override
  public FixedTypeMap<V> freeze() {
    return new FixedTypeMap<>(tmp, autobox);
  }

}
