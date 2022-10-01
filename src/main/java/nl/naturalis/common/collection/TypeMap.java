package nl.naturalis.common.collection;

import nl.naturalis.check.Check;

import java.util.Collection;
import java.util.Map;

import static nl.naturalis.common.x.Param.SOURCE_MAP;

/**
 * <p>
 * A specialisation of the {@code Map} interface, aimed at providing natural default
 * values for groups of Java types via a shared supertype.
 * <p>
 * A {@code TypeMap} behaves as follows: if a type, requested via
 * {@link #get(Object) get} or {@link #containsKey(Object) containsKey}, is not
 * present in the map, but one of its supertypes is, then it will return the value
 * associated with the supertype (or {@code true} in the case of
 * {@code containsKey}). For example: if the user requests the value associated with
 * key {@code Integer.class}, but the map only contains an entry for
 * {@code Number.class}, then the value associated with {@code Number.class} is
 * returned. If there is no entry for {@code Number.class} either, but there is one
 * for {@code Serializable.class}, then the value associated with that type is
 * returned. If the requested type is a regular class (rather than an interface), its
 * class hierarchy takes precedence over the interface hierarchy associated with it
 * (if any). Within the class hierarchy, the nearest supertype takes precedence over
 * supertypes further up the tree (closer to {@code Object.class}).
 * <p>
 * A {@code TypeMap} is not modifiable. All map-altering methods throw an
 * {@link UnsupportedOperationException}. The {@code getOrDefault} method also throws
 * an {@code UnsupportedOperationException} as it sidesteps the {@code TypeMap}
 * paradigm. Neither keys nor values are allowed to be {@code null}. If the map
 * contains key {@code Object.class}, it will <i>always</i> return a non-null value.
 * Note that this is, in fact, a deviation from Java's type hierarchy since primitive
 * types do not extend {@code Object.class}. However, the point of the
 * {@code TypeMap} interface is to provide natural default values for groups of
 * types, and {@code Object.class} is the obvious candidate for providing the
 * ultimate, last-resort, fall-back value.
 * <p>
 * You cannot instantiate a {@code TypeMap} directly, or even provide your own
 * implementation. You obtain an instance through the various static factory methods
 * on the {@code TypeMap} interface itself.
 *
 * <h2 id="autoboxing">Autoboxing</h2>
 * <p>
 * A {@code TypeMap} can be configured to "autobox" the types requested via
 * {@code get} and {@code containsKey}. For example, if the user makes a request for
 * {@code double.class}, but the map only contains an entry for {@code Double.class},
 * then the value associated with {@code Double.class} is returned. If there is no
 * entry for {@code Double.class} either, but there is one for {@code Number.class},
 * then the value associated with {@code Number.class} is returned. Thus, with
 * autoboxing enabled, you need (and should) only add the wrapper types to the map,
 * unless you want the primitive type to be associated with a different value than
 * the wrapper type. This applies not just to primitive types, but also to arrays of
 * a primitive type. Thus, with autoboxing enabled, {@code int[]} will be "autoboxed"
 * to {@code Integer[]}. Note that, irrespective of whether autoboxing is enabled or
 * not, the presence of {@code Object.class} in the map <i>always</i> guarantees that
 * a non-null value will be returned for whatever type is requested, even if it is a
 * primitive type. Autoboxing is <b>enabled</b> by default.
 *
 * <h2 id="auto-expansion">Auto-expansion</h2>
 *
 * <p>The "{@linkplain #greedyTypeMap(Map) greedy}" {@code TypeMap} will
 * automatically and tacitly absorb subtypes of the original types in the map, as and
 * when they are requested via {@code get} or {@code containsKey}. It will look up
 * the value for the nearest supertype and associate the subtype with that same
 * value. Thus, the next time the subtype is requested, it will result in a direct
 * hit. Note that an auto-expanding type map is still immutable to the outside world
 * and that the map will still only ever contain subtypes of the types with which the
 * map was seeded. No new branches of the Java type hierarchy will emerge - unless,
 * of course, the original map contained {@code Object.class}.
 *
 * <h2>Implementations</h2>
 *
 * <p>Through its static factory methods and {@code Builder} classes, the
 * {@code TypeMap} interface provides access to four different implementations. Which
 * implementation to choose strongly depends on the internal makeup of the map (the
 * interdependencies between the types within the map), the size of the map, and the
 * ratio between the size of the map and the total number of types it is going to be
 * queried for. For high ratios (a small map processing a large variety of types),
 * the "{@link #typeGraph(Map, boolean) type graph}" is most likely the best choice,
 * especially if the types in the map tend to be base types (like {@code Number} and
 * {@code CharSequence}) while the types requested from it are concrete types (like
 * {@code Integer} and {@code String}). Otherwise any of the other implementations
 * will do, and you will have to test which implementation performs best.
 *
 * @param <V> the type of the values in the map The type of the values in the
 *     {@code Map}
 * @author Ayco Holleman
 */
public sealed interface TypeMap<V> extends Map<Class<?>, V> permits TypeGraph,
    GreedyTypeMap, NonExpandingTypeMap {

  /**
   * Returns a {@code TypeMap} that is internally backed by a regular, unmodifiable
   * {@code Map}. It performs reliably well in many cases. Autoboxing is enabled in
   * the returned {@code TypeMap}.
   *
   * @param m the map to convert to a {@code TypeMap}
   * @param <V> the type of the values in the map
   * @return a {@code TypeMap} that performs reliably well in many cases
   */
  static <V> TypeMap<V> fixedTypeMap(Map<Class<?>, V> m) {
    return fixedTypeMap(m, true);
  }

  /**
   * Returns a {@code TypeMap} that is internally backed by a regular, unmodifiable
   * {@code Map}. It performs reliably well in many cases.
   *
   * @param m the map to convert to a {@code TypeMap}
   * @param autobox whether to "autobox" the types requested via {@code get} and
   *     {@code containsKey}
   * @param <V> the type of the values in the map
   * @return a {@code TypeMap} that performs reliably well in many cases
   */
  static <V> TypeMap<V> fixedTypeMap(Map<Class<?>, V> m, boolean autobox) {
    Check.notNull(m, SOURCE_MAP);
    var builder = new FixedTypeMapBuilder<V>().autobox(autobox);
    m.forEach(builder::add);
    return builder.freeze();
  }

  /**
   * Returns a {@code Builder} for "fixed" type maps.
   *
   * @param <V> the type of the values in the map
   * @return a {@code Builder} for "fixed" type maps
   */
  static <V> TypeMapBuilder<V> fixedTypeMapBuilder() {
    return new FixedTypeMapBuilder<>();
  }

  /**
   * Returns a {@code TypeMap} that relies on a data structure similar to the Java
   * type hierarchy itself. The {@code TypeMap} is sensitive to the insertion order
   * of the types. Thus, if you expect a lot of requests for, say,
   * {@code String.class}, it pays to pass a
   * {@link java.util.LinkedHashMap LinkedHashMap} where {@code String.class} was
   * inserted first. The keys of the returned {@code TypeMap} are sorted from more
   * abstract to less abstract. If present, {@code Object.class} will be the first
   * type in the key set. Autoboxing is enabled in the returned {@code TypeMap}.
   *
   * @param m the map to convert to a {@code TypeMap}
   * @param <V> the type of the values in the map
   * @return a {@code TypeMap} that relies on a data structure similar to the Java
   *     type hierarchy itself
   * @see TypeSet#greedyTypeSet(Class[])
   */
  static <V> TypeMap<V> typeGraph(Map<Class<?>, V> m) {
    return typeGraph(m, true);
  }

  /**
   * Returns a {@code TypeMap} that relies on a data structure similar to the Java
   * type hierarchy itself. The {@code TypeMap} is sensitive to the insertion order
   * of the types. Thus, if you expect a lot of requests for, say,
   * {@code String.class}, it pays to pass a
   * {@link java.util.LinkedHashMap LinkedHashMap} where {@code String.class} was
   * inserted first. The keys of the returned {@code TypeMap} are sorted from more
   * abstract to less abstract. If present, {@code Object.class} will be the first
   * type in the key set.
   *
   * @param m the map to convert to a {@code TypeMap}
   * @param autobox whether to "autobox" the types requested via {@code get} and
   *     {@code containsKey}
   * @param <V> the type of the values in the map
   * @return a {@code TypeMap} that relies on a data structure similar to the Java
   *     type hierarchy itself
   */
  static <V> TypeMap<V> typeGraph(Map<Class<?>, V> m, boolean autobox) {
    Check.notNull(m, SOURCE_MAP);
    var builder = new TypeGraphBuilder<V>().autobox(autobox);
    m.forEach(builder::add);
    return builder.freeze();
  }

  /**
   * Returns a {@code Builder} for type maps that rely on a data structure similar to
   * the Java type hierarchy itself. The {@code TypeMap} is sensitive to the
   * insertion order of the types. Thus, if you expect a lot of requests for key
   * {@code String.class}, it pays to make it the first class you
   * {@linkplain TypeMapBuilder#add(Class, Object) add} using the builder.
   *
   * @param <V> the type of the values in the map
   * @return a builder for type maps that rely on a data structure similar to the
   *     Java type hierarchy itself
   */
  static <V> TypeMapBuilder<V> typeGraphBuilder() {
    return new TypeGraphBuilder<>();
  }

  /**
   * Returns an <a href="#auto-expansion">auto-expanding</a> {@code TypeMap}.
   * Autoboxing is enabled in the returned {@code TypeMap}.
   *
   * @param m the map to convert to a {@code TypeMap}
   * @param <V> the type of the values in the map
   * @return an auto-expanding {@code TypeMap}
   */
  static <V> TypeMap<V> greedyTypeMap(Map<Class<?>, V> m) {
    return greedyTypeMap(m, true);
  }

  /**
   * Returns an <a href="#auto-expansion">auto-expanding</a> {@code TypeMap}.
   *
   * @param m the map to convert to a {@code TypeMap}
   * @param autobox whether to "autobox" the types requested via {@code get} and
   *     {@code containsKey}
   * @param <V> the type of the values in the map
   * @return an auto-expanding {@code TypeMap}
   */
  static <V> TypeMap<V> greedyTypeMap(Map<Class<?>, V> m, boolean autobox) {
    Check.notNull(m, SOURCE_MAP);
    var builder = new GreedyTypeMapBuilder<V>().autobox(autobox);
    m.forEach(builder::add);
    return builder.freeze();
  }

  /**
   * Returns a {@code Builder} for auto-expanding type maps.
   *
   * @param <V> the type of the values in the map
   * @return a {@code Builder} for auto-expanding type maps
   */
  static <V> TypeMapBuilder<V> greedyTypeMapBuilder() {
    return new GreedyTypeMapBuilder<>();
  }

  /**
   * Returns a {@code TypeMap} that is backed by a {@link java.util.TreeMap TreeMap}.
   * Its keys are sorted such that for any two types in the key set, the one that
   * comes first will never be a supertype of the one that comes second - and vice
   * versa: the one that comes second will never be a subtype of the one that comes
   * first. In other words, they are sorted from less abstract to more abstract. If
   * the map contains key {@code Object.class}, it will be the last element in the
   * key set. The {@link java.util.Comparator Comparator} used for the
   * {@code TreeMap} is similar to the one used for
   * {@link TypeSet#prettySort(Collection) CollectionMethods.prettyTypeSet}, but much
   * more light-weight, and therefore more performant. Autoboxing is enabled in the
   * returned {@code TypeMap}.
   *
   * @param <V> the type of the values in the map
   * @param m the map to convert to a {@code TypeMap}
   * @return a {@code TypeMap} that is itself backed by a {@code TreeMap}
   * @see TypeSet#typeTreeSet(Class[])
   */
  static <V> TypeMap<V> typeTree(Map<Class<?>, V> m) {
    return typeTree(m, true);
  }

  /**
   * Returns a {@code TypeMap} that is backed by a {@link java.util.TreeMap TreeMap}.
   * Its keys are sorted such that for any two types in the key set, the one that
   * comes first will never be a supertype of the one that comes second - and vice
   * versa: the one that comes second will never be a subtype of the one that comes
   * first. In other words, they are sorted from less abstract to more abstract. If
   * the map contains key {@code Object.class}, it will be the last element in the
   * key set. The {@link java.util.Comparator Comparator} used for the
   * {@code TreeMap} is similar to the one used for
   * {@link TypeSet#prettySort(Collection) TypeSet.prettySort()}, but more
   * light-weight, and therefore more performant.
   *
   * @param m the map to convert to a {@code TypeMap}
   * @param autobox whether to "autobox" the types requested via {@code get} and
   *     {@code containsKey}
   * @param <V> the type of the values in the map
   * @return a {@code TypeMap} that is itself backed by a {@code TreeMap}
   * @see TypeSet#typeTreeSet(Class[])
   */
  static <V> TypeMap<V> typeTree(Map<Class<?>, V> m, boolean autobox) {
    Check.notNull(m, SOURCE_MAP);
    var builder = new TypeTreeMapBuilder<V>().autobox(autobox);
    m.forEach(builder::add);
    return builder.freeze();
  }

  /**
   * Returns a {@code Builder} for type maps that are backed by a
   * {@linkplain java.util.TreeMap TreeMap}.
   *
   * @param <V> the type of the values in the map
   * @return a {@code Builder} for type maps that are backed by a {@code TreeMap}
   */
  static <V> TypeMapBuilder<V> typeTreeBuilder() {
    return new TypeTreeMapBuilder<>();
  }

  /**
   * Throws an {@linkplain UnsupportedOperationException}.
   */
  @Override
  default V getOrDefault(Object key, V defaultValue) {
    throw new UnsupportedOperationException();
  }

}
