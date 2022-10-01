package nl.naturalis.common.collection;

import nl.naturalis.common.CollectionMethods;
import nl.naturalis.common.Emptyable;
import nl.naturalis.check.Check;
import nl.naturalis.common.x.collection.ArraySet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static nl.naturalis.common.ObjectMethods.replaceIf;
import static nl.naturalis.check.Check.fail;
import static nl.naturalis.check.CommonChecks.*;

/**
 * A fast enum-to-int map. The map is backed by a simple int array with the same
 * length as the number of constants in the {@code enum} class. One integer must be
 * designated to signify the <b>absence</b> of a key. By default, this is
 * {@link Integer#MIN_VALUE} and, by default, all elements in the {@code int} array
 * are set to this value (meaning the map is empty). It is not allowed to add a key
 * with this value to the map, as is would in effect amount to
 * <i>removing</i> that key from the map. It is also not allowed to pass this value
 * to {@link #containsValue(int) containsValue}. In both cases an
 * {@code IllegalArgumentException} is thrown. Empty enum classes (i.e. enum classes
 * without enum constants) are not supported.
 *
 * @param <K> The type of the enum class
 * @author Ayco Holleman
 */
public final class EnumToIntMap<K extends Enum<K>> implements Emptyable {

  private final K[] keys;
  private final int[] data;
  private final int kav; // the key-absent-value

  /**
   * Creates a new empty {@code EnumToIntMap} for the specified enum class using
   * {@code Integer.MIN_VALUE} as the <i>key-absent-value</i> value. All elements in
   * the backing array will be initialized to this value (meaning that the map is
   * empty).
   *
   * @param enumClass The type of the enum class
   */
  public EnumToIntMap(Class<K> enumClass) {
    this(enumClass, Integer.MIN_VALUE);
  }

  /**
   * Creates a new {@code EnumToIntMap} for the specified enum class with the
   * specified integer as the <i>key-absent-value</i> value. All elements in the
   * backing array will be initialized to this value (meaning that the map is
   * empty).
   *
   * @param enumClass The type of the enum class
   * @param keyAbsentValue The value used to signify the absence of a key
   */
  public EnumToIntMap(Class<K> enumClass, int keyAbsentValue) {
    this(enumClass, keyAbsentValue, k -> keyAbsentValue);
  }

  /**
   * Creates a new {@code EnumToIntMap} using {@code Integer.MIN_VALUE} as the
   * <i>key-absent-value</i> value and with its keys initialized using the specified
   * initializer function. For example:
   *
   * <blockquote><pre>{@code
   * EnumToIntMap<DayOfWeek> map = new EnumToIntMap<>(DayOfWeek.class, k -> k.ordinal() + 1);
   * }</pre></blockquote>
   *
   * @param enumClass The type of the enum class
   * @param initializer A function called to initialize the array elements
   */
  public EnumToIntMap(Class<K> enumClass, ToIntFunction<K> initializer) {
    this(enumClass, Integer.MIN_VALUE, initializer);
  }

  /**
   * Creates a new {@code EnumToIntMap} with the specified <i>key-absent-value</i>
   * value and the specified initializer function.
   *
   * @param enumClass The type of the enum class
   * @param keyAbsentValue The value used to signify the absence of a key
   * @param initializer A function called to initialize the array elements
   */
  public EnumToIntMap(Class<K> enumClass,
      int keyAbsentValue,
      ToIntFunction<K> initializer) {
    Check.notNull(enumClass, "enumClass");
    Check.notNull(initializer, "initializer");
    this.keys = Check.that(enumClass.getEnumConstants())
        .isNot(empty(), "enum class without enum constants").ok();
    this.data = new int[keys.length];
    this.kav = keyAbsentValue;
    Arrays.stream(keys).forEach(k -> assign(k, initializer.applyAsInt(k)));
  }

  /**
   * Instantiates a new {@code EnumToIntMap} with the same key-value mappings as the
   * specified {@code EnumToIntMap} and with the same <i>key-absent-value</i> value.
   *
   * @param other The {@code EnumToIntMap} whose key-value mappings to copy
   */
  public EnumToIntMap(EnumToIntMap<K> other) {
    Check.notNull(other, "other");
    this.keys = other.keys;
    this.data = new int[keys.length];
    this.kav = other.kav;
    System.arraycopy(other.data, 0, this.data, 0, keys.length);
  }

  /**
   * Instantiates a new {@code EnumToIntMap} with the same key-value mappings as the
   * specified {@code EnumToIntMap}, but (potentially) with a new
   * <i>key-absent-value</i>.
   *
   * @param other The {@code EnumToIntMap} whose key-value mappings to copy
   * @param keyAbsentValue The value used to signify the absence of a key
   */
  public EnumToIntMap(EnumToIntMap<K> other, int keyAbsentValue) {
    Check.notNull(other, "other");
    this.keys = other.keys;
    this.data = new int[keys.length];
    this.kav = keyAbsentValue;
    copyEntries(other);
  }

  /**
   * Returns {@code true} if this map contains a mapping for the specified key.
   *
   * @param key The enum constant
   * @return Whether the map contains an entry for the enum constant
   * @see Map#containsKey(Object)
   */
  public boolean containsKey(K key) {
    return Check.notNull(key).ok(k -> data[k.ordinal()] != kav);
  }

  /**
   * Returns {@code true} if this map maps one or more keys to the specified value.
   * It is not permitted to search for the <i>key-absent-value</i> value. An
   * {@code IllegalArgumentException} is thrown if you do.
   *
   * @param val The value
   * @return Whether the map contains the value
   * @see Map#containsValue(Object)
   */
  public boolean containsValue(int val) {
    Check.that(val).is(ne(), kav);
    return Arrays.stream(data).filter(x -> x == val).findFirst().isPresent();
  }

  /**
   * Returns the value to which the specified enum constant is mapped, or the
   * <i>key-absent-value</i> if this map contains no mapping for the key. (A regular
   * {@code Map} would return {@code null} in the latter case.)
   *
   * @param key The key whose associated value is to be returned
   * @return the value to which the specified key is mapped, or the
   *     <i>key-absent-value</i> if this map contains no mapping for the key
   * @see Map#get(Object)
   */
  public int get(K key) {
    return valueOf(Check.notNull(key).ok());
  }

  /**
   * Returns the value associated with the specified enum constant or {@code dfault}
   * if the map did not contain an entry for the specified enum constant.
   *
   * @param key The key to retrieve the value of.
   * @param dfault The integer to return if the map did not contain the key
   * @return the value associated with the key or {@code dfault}
   * @see Map#getOrDefault(Object, Object)
   */
  public int getOrDefault(K key, int dfault) {
    return containsKey(key) ? valueOf(key) : dfault;
  }

  /**
   * Associates the specified value with the specified key in this map.
   *
   * @param key The key
   * @param val The value
   * @return the previous value associated with the specified enum constant or the
   *     <i>key-absent-value</i> value if the map did not contain an entry for the
   *     enum constant yet.
   * @see Map#put(Object, Object)
   */
  public int put(K key, int val) {
    Check.notNull(key, "key");
    Check.that(val, "val").is(ne(), kav);
    int orig = valueOf(key);
    assign(key, val);
    return orig;
  }

  /**
   * Much like {@code put}, but provides a fluent API for adding entries to the map.
   *
   * @param key The key
   * @param val The value
   * @return This instance
   */
  public EnumToIntMap<K> set(K key, int val) {
    Check.notNull(key, "key");
    Check.that(val, "val").is(ne(), kav);
    assign(key, val);
    return this;
  }

  /**
   * Adds all entries of the specified map to this map, overwriting any previous
   * values. The source map must not contain the <i>key-absent-value</i> of this map.
   * An {@link IllegalArgumentException} is thrown if it does.
   *
   * @param other The {@code EnumToIntMap} whose key-value mappings to copy
   */
  public void putAll(EnumToIntMap<K> other) {
    Check.notNull(other, "other");
    copyEntries(other);
  }

  /**
   * Adds all entries of the specified map to this map. This method acts as a bridge
   * to fully-generic map implementations. The source map must not contain the
   * <i>key-absent-value</i> of this map. An {@link IllegalArgumentException} is
   * thrown if it does.
   *
   * @param other The {@code Map} whose key-value mappings to copy
   */
  public void putAll(Map<K, Integer> other) {
    Check.notNull(other, "other")
        .isNot(containingValue(), kav)
        .then(m -> m.forEach(this::assign));
  }

  /**
   * Returns an immutable, fully-generic version of this map.
   *
   * @return an immutable, fully-generic version of this map
   */
  public Map<K, Integer> toGenericMap() {
    return Map.ofEntries(entrySet().toArray(SimpleImmutableEntry[]::new));
  }

  /**
   * Removes the mapping for a key from this map if it is present.
   *
   * @param key The key
   * @return the previous value associated with key, or the <i>key-absent-value</i>
   *     value if there was no mapping for key.
   * @see Map#remove(Object)
   */
  public int remove(K key) {
    Check.notNull(key, "key");
    int v = valueOf(key);
    assign(key, kav);
    return v;
  }

  /**
   * Returns a {@link Set} view of the keys contained in this map.
   *
   * @return a Set view of the keys contained in this map
   * @see Map#keySet()
   */
  public Set<K> keySet() {
    return streamKeys().collect(toSet());
  }

  /**
   * Returns a {@code Collection} view of the values contained in this map.
   *
   * @return a {@code Collection} view of the values contained in this map
   * @see Map#values()
   */
  public Collection<Integer> values() {
    return streamKeys().map(this::valueOf).collect(toList());
  }

  /**
   * Returns an {@code IntList} containing the values of this map.
   *
   * @return an {@code IntList} containing the values of this map
   */
  public IntList intValues() {
    IntArrayList ial = new IntArrayList(data.length);
    for (int i : data) {
      if (i != kav) {
        ial.add(i);
      }
    }
    return ial;
  }

  /**
   * Returns a Set view of the mappings contained in this map.
   *
   * @return a set view of the mappings contained in this map
   * @see Map#entrySet()
   */
  public Set<Map.Entry<K, Integer>> entrySet() {
    SimpleImmutableEntry[] entries = streamKeys()
        .map(k -> new SimpleImmutableEntry(k, valueOf(k)))
        .toArray(SimpleImmutableEntry[]::new);
    return ArraySet.of(entries, true);
  }

  /**
   * Returns {@code true} if this map contains no key-value mappings, {@code false}
   * otherwise.
   *
   * @return {@code true} if this map contains no key-value mappings, {@code false}
   *     otherwise
   * @see Map#isEmpty()
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Performs the given action for each entry in this map until all entries have been
   * processed or the action throws an exception.
   *
   * @param action The action to be performed for each entry
   * @see Map#forEach(BiConsumer)
   */
  public void forEach(ObjIntConsumer<K> action) {
    streamKeys().forEach(k -> action.accept(k, valueOf(k)));
  }

  /**
   * Removes all mappings from this map.
   *
   * @see Map#clear()
   */
  public void clear() {
    Arrays.fill(data, kav);
  }

  /**
   * Returns the number of key-value mappings in this map.
   *
   * @return the number of key-value mappings in this map
   * @see Map#size()
   */
  public int size() {
    return (int) streamKeys().count();
  }

  /**
   * Returns the type of the enum keys in this map.
   *
   * @return the type of the enum keys
   */
  @SuppressWarnings("unchecked")
  public Class<K> enumClass() {
    return (Class<K>) keys[0].getClass();
  }

  /**
   * Returns the integer used to signify the absence of a key within this map.
   *
   * @return the integer used to signify the absence of a key
   */
  public int keyAbsentValue() {
    return kav;
  }

  /**
   * Returns {@code true} if the argument is an {@code EnumToIntMap} for the same
   * enum class and if it contains the same key-value mappings. The two maps need not
   * have the same <i>key-absent-value</i> value.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof EnumToIntMap<?> that && enumClass() == that.enumClass()) {
      if (this.kav == that.kav) {
        return Arrays.equals(data, that.data);
      }
      for (int i = 0; i < keys.length; i++) {
        if (replaceIf(this.data[i], eq(), this.kav, that.kav) != that.data[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public int hashCode() {
    return entrySet().hashCode();
  }

  @Override
  public String toString() {
    return '[' + CollectionMethods.implode(entrySet()) + ']';
  }

  private void copyEntries(EnumToIntMap<K> other) {
    Check.that(other.enumClass()).is(sameAs(),
        enumClass(),
        "enum type mismatch: {arg} vs. {obj}");
    if (kav == other.kav) {
      System.arraycopy(other.data, 0, data, 0, data.length);
    } else if (other.containsValue(kav)) {
      fail("source map must not contain key-absent-value ({0})", kav);
    } else {
      for (int i = 0; i < data.length; ++i) {
        data[i] = replaceIf(other.data[i], eq(), other.kav, kav);
      }
    }
  }

  private Stream<K> streamKeys() {
    return Arrays.stream(keys).filter(k -> data[k.ordinal()] != kav);
  }

  private void assign(K key, int val) {
    data[key.ordinal()] = val;
  }

  private int valueOf(K key) {
    return data[key.ordinal()];
  }

}
