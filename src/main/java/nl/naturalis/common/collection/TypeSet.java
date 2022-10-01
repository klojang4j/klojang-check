package nl.naturalis.common.collection;

import nl.naturalis.check.Check;
import nl.naturalis.common.CollectionMethods;
import nl.naturalis.common.x.collection.ArraySet;
import nl.naturalis.common.x.collection.PrettyTypeComparator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static nl.naturalis.check.CommonChecks.deepNotNull;

/**
 * <p>
 * An extension of the {@link Set} interface with behaviour analogous to the
 * {@link TypeMap} interface. That is, if the type passed to
 * {@link #contains(Object) contains} is not present, but one of its supertypes is,
 * then {@code contains} will return {@code true}. For more information about
 * features like <a href="TypeMap.html#autoboxing">autoboxing</a> and
 * <a href="TypeMap.html#auto-expansion">auto-expansion</a>, please read the
 * documentation for the {@link TypeMap} interface. You obtain an instance of a
 * {@code TypeSet} via the various static factory methods on the {@code TypeSet}
 * interface itself.
 */
public sealed interface TypeSet extends Set<Class<?>> permits AbstractTypeSet {

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#fixedTypeMap(Map) TypeMap.fixedTypeMap()}.
   *
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#fixedTypeMap(Map) TypeMap.fixedTypeMap()}
   */
  static TypeSet fixedTypeSet(Collection<Class<?>> types) {
    return fixedTypeSet(true, types);
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#fixedTypeMap(Map) TypeMap.fixedTypeMap()}.
   * {@code TypeMap}. See {@link TypeMap#fixedTypeMap(Map)}.
   *
   * @param autobox whether to enable "<a
   *     href="TypeMap.html#autoboxing">autoboxing</a>"
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#fixedTypeMap(Map) TypeMap.fixedTypeMap()}
   */
  static TypeSet fixedTypeSet(boolean autobox, Collection<Class<?>> types) {
    return fixedTypeSet(autobox, types.toArray(Class[]::new));
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#fixedTypeMap(Map) TypeMap.fixedTypeMap()}
   *
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#fixedTypeMap(Map) TypeMap.fixedTypeMap()}
   */
  static TypeSet fixedTypeSet(Class<?>... types) {
    return fixedTypeSet(true, types);
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#fixedTypeMap(Map) TypeMap.fixedTypeMap()}
   *
   * @param autobox whether to enable "<a
   *     href="TypeMap.html#autoboxing">autoboxing</a>"
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#fixedTypeMap(Map) TypeMap.fixedTypeMap()}
   */
  static TypeSet fixedTypeSet(boolean autobox, Class<?>... types) {
    Check.notNull(types);
    return new AbstractTypeSet() {
      @Override
      TypeMap<Object> createBackend() {
        // NB we associate each type with Boolean.TRUE, but that's completely
        // arbitrary. It could have been anything.
        return new FixedTypeMapBuilder<>()
            .autobox(autobox)
            .addMultiple(Boolean.TRUE, types)
            .freeze();
      }
    };
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#typeGraph(Map) TypeMap.typeGraph()}.
   *
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#typeGraph(Map)  TypeMap.typeGraph()}
   */
  static TypeSet typeGraphSet(Collection<Class<?>> types) {
    return typeGraphSet(true, types);
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#typeGraph(Map) TypeMap.typeGraph()}.
   *
   * @param autobox whether to enable "<a
   *     href="TypeMap.html#autoboxing">autoboxing</a>"
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#typeGraph(Map)  TypeMap.typeGraph()}
   */
  static TypeSet typeGraphSet(boolean autobox, Collection<Class<?>> types) {
    return typeGraphSet(autobox, types.toArray(Class[]::new));
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#typeGraph(Map) TypeMap.typeGraph()}.
   *
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#typeGraph(Map) TypeMap.typeGraph()}
   */
  static TypeSet typeGraphSet(Class<?>... types) {
    return typeGraphSet(true, types);
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#typeGraph(Map) TypeMap.typeGraph()}.
   *
   * @param autobox whether to enable "<a
   *     href="TypeMap.html#autoboxing">autoboxing</a>"
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#typeGraph(Map) TypeMap.typeGraph()}
   */
  static TypeSet typeGraphSet(boolean autobox, Class<?>... types) {
    Check.notNull(types);
    return new AbstractTypeSet() {
      @Override
      TypeMap<Object> createBackend() {
        return new TypeGraphBuilder<>()
            .autobox(autobox)
            .addMultiple(Boolean.TRUE, types)
            .freeze();
      }
    };
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#greedyTypeMap(Map) TypeMap.greedyTypeMap()}.
   *
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#greedyTypeMap(Map)}
   *     TypeMap.greedyTypeMap()}
   */
  static TypeSet greedyTypeSet(Collection<Class<?>> types) {
    return greedyTypeSet(true, types);
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#greedyTypeMap(Map) TypeMap.greedyTypeMap()}.
   *
   * @param autobox whether to enable "<a
   *     href="TypeMap.html#autoboxing">autoboxing</a>"
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#greedyTypeMap(Map)}
   *     TypeMap.greedyTypeMap()}
   */
  static TypeSet greedyTypeSet(boolean autobox,
      Collection<Class<?>> types) {
    return greedyTypeSet(autobox, types.toArray(Class[]::new));
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#greedyTypeMap(Map) TypeMap.greedyTypeMap()}.
   *
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#greedyTypeMap(Map)}
   *     TypeMap.greedyTypeMap()}
   */
  static TypeSet greedyTypeSet(Class<?>... types) {
    return greedyTypeSet(true, types);
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#greedyTypeMap(Map) TypeMap.greedyTypeMap()}.
   *
   * @param autobox whether to enable "<a
   *     href="TypeMap.html#autoboxing">autoboxing</a>"
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#greedyTypeMap(Map)}
   *     TypeMap.greedyTypeMap()}
   */
  static TypeSet greedyTypeSet(boolean autobox, Class<?>... types) {
    Check.notNull(types);
    return new AbstractTypeSet() {
      @Override
      TypeMap<Object> createBackend() {
        return new GreedyTypeMapBuilder<>()
            .autobox(autobox)
            .addMultiple(Boolean.TRUE, types)
            .freeze();
      }
    };
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#typeTree(Map) TypeMap.typeTree()}.
   *
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#typeTree(Map) TypeMap.typeTree()}
   */
  static TypeSet typeTreeSet(Collection<Class<?>> types) {
    return typeTreeSet(true, types);
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#typeTree(Map) TypeMap.typeTree()}.
   *
   * @param autobox whether to enable "<a
   *     href="TypeMap.html#autoboxing">autoboxing</a>"
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#typeTree(Map) TypeMap.typeTree()}
   */
  static TypeSet typeTreeSet(boolean autobox,
      Collection<Class<?>> types) {
    return typeTreeSet(autobox, types.toArray(Class[]::new));
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#typeTree(Map) TypeMap.typeTree()}.
   *
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#typeTree(Map) TypeMap.typeTree()}
   */
  static TypeSet typeTreeSet(Class<?>... types) {
    return typeTreeSet(true, types);
  }

  /**
   * Returns a {@code TypeSet} that is internally backed by a {@code TypeMap}
   * retrieved through {@link TypeMap#typeTree(Map) TypeMap.typeTree()}.
   *
   * @param autobox whether to enable "<a
   *     href="TypeMap.html#autoboxing">autoboxing</a>"
   * @param types the types to initialize the {@code TypeSet} with
   * @return a {@code TypeSet} that is internally backed by a {@code TypeMap}
   *     retrieved through {@link TypeMap#typeTree(Map) TypeMap.typeTree()}
   */
  static TypeSet typeTreeSet(boolean autobox, Class<?>... types) {
    Check.notNull(types);
    return new AbstractTypeSet() {
      @Override
      TypeMap<Object> createBackend() {
        return new TypeTreeMapBuilder<>()
            .autobox(autobox)
            .addMultiple(Boolean.TRUE, types)
            .freeze();
      }
    };
  }

  /**
   * <p>
   * Returns an unmodifiable {@code Set} in which the types in the provided
   * collection are sorted according to their distance from {@code Object.class}.
   * Note that this is a utility method, mainly meant for printing purposes. <b>The
   * returned set is not an instance of {@code TypeSet}</b>. Its {@code contains}
   * method performs poorly, but it can be iterated over quickly. The
   * {@link java.util.Comparator Comparator} used to sort the types is similar to the
   * one used for {@link #typeTreeSet(Class[]) typeTreeSet}, but much more
   * heavy-handed, applying a fully-deterministic ordering of the types in the
   * provided collection.
   * <p>
   * This is how the types in the returned set will be sorted:
   * <ul>
   *   <li>primitive types
   *   <li>primitive wrapper types
   *   <li>enums (excluding {@code Enum.class} itself)
   *   <li>other non-array types, according to their distance from {@code Object .class}
   *   <li>array types (recursively according to component type)
   *   <li>interfaces according to the number of other interfaces they extend
   *   <li>{@code Object.class}
   *   <li>by inverse fully-qualified class name (e.g. OutputStream.io.java)
   * </ul>
   *
   * @param src the collection to sort
   * @return an unmodifiable {@code Set} in which the types are sorted according to
   *     their distance from {@code Object.class}.
   */
  static Set<Class<?>> prettySort(Collection<Class<?>> src) {
    Check.notNull(src);
    if (!CollectionMethods.isNullRepellent(src)) {
      Check.that(src).is(deepNotNull());
    }
    Class<?>[] types = src.toArray(Class[]::new);
    Arrays.sort(types, new PrettyTypeComparator());
    return ArraySet.of(types, true);
  }

}
