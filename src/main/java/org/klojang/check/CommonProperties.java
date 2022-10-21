package org.klojang.check;

import static org.klojang.check.x.msg.MsgUtil.DEF_ARG_NAME;
import static org.klojang.check.x.Misc.typeNotSupported;
import static org.klojang.check.x.msg.MsgUtil.simpleClassName;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;

import org.klojang.check.relation.Relation;

/**
 * Defines various functions that can optionally be passed as the first argument to
 * the {@code has(...)} and {@code notHas(...) } methods of {@link IntCheck} and
 * {@link ObjectCheck}. These functions are associated with a description of the
 * property they expose, so generating an error message requires very little
 * hand-crafting. For example:
 *
 * <blockquote>
 *
 * <pre>{@code
 * Check.that(car, "car").has(strval(), equalTo(), "BMW");
 * // Error message: "car.toString() must be equal to BMW (was Toyota)"
 * }</pre>
 *
 * </blockquote>
 *
 * <p>Note that the word "property" is suggestive, but also misleading. The
 * functions defined here really are just that: functions that transform the argument
 * into some other value, which can then be subjected to further tests.
 *
 * <blockquote>
 *
 * <pre>{@code
 * Check.that(temperature, "temperature").has(abs(), lt(), 20);
 * // Error message: "abs(temperature) must be < 20 (was -39)"
 * }</pre>
 *
 * </blockquote>
 *
 * <p>As with the checks in the {@link CommonChecks} class, <i>none of the functions
 * defined here execute a preliminary null check</i>. Many of them simply return a
 * method reference. They rely upon being embedded in chain of checks, the first of
 * which should be the {@link CommonChecks#notNull() notNull} check (if necessary).
 *
 * @author Ayco Holleman
 */
@SuppressWarnings("rawtypes")
public class CommonProperties {

  private CommonProperties() {}

  private static final Map<Object, BiFunction<Object, String, String>> NAMES;

  private static Map<Object, BiFunction<Object, String, String>> tmp = new HashMap<>();

  /**
   * Returns the boxed version of the argument. Equivalent to
   * {@link Integer#valueOf(int) Integer::valueOf}. This "getter" is especially
   * useful to get access to the many {@link Relation} checks in the
   * {@link CommonChecks} class when validating an {@code int} argument:
   *
   * <blockquote>
   *
   * <pre>{@code
   * // WON'T COMPILE! IntCheck does not have method is(Relation, Object)
   * Check.that(42).is(keyIn(), map);
   *
   * // OK. ObjectCheck does have method is(Relation, Object)
   * Check.that((Integer) 42).is(keyIn(), map);
   *
   * // More idiomatic:
   * Check.that(42).has(box(), keyIn(), map);
   *
   * }</pre>
   *
   * </blockquote>
   *
   * @return The boxed version of the argument
   */
  public static IntFunction<Integer> box() {
    return Integer::valueOf;
  }

  static {
    tmp.put(box(),
        (arg, argName) ->
            "Integer.valueOf(" + ifNull(argName, DEF_ARG_NAME) + ")");
  }

  /**
   * Returns the unboxed version of the argument. Equivalent to
   * {@link Integer#intValue() Integer::intValue}.
   *
   * @return The unboxed version of the argument
   */
  public static ToIntFunction<Integer> unbox() {
    return Integer::intValue;
  }

  static {
    tmp.put(unbox(),
        (arg, argName) ->
            "Integer.intValue(" + ifNull(argName, DEF_ARG_NAME) + ")");
  }

  /**
   * Returns the result of calling {@code toString()} on the argument. Equivalent to
   * {@link Object#toString() Object::toString}.
   *
   * @param <T> The type of the object on which to call {@code toString{}}.
   * @return The result of calling {@code toString()} on the argument
   */
  public static <T> Function<T, String> strval() {
    return Object::toString;
  }

  static {
    tmp.put(strval(), (arg, argName) -> base(argName, arg) + ".toString()");
  }

  /**
   * Returns the length of a {@code CharSequence}. Equivalent to
   * {@code CharSequence::length}.
   *
   * @return The length of a {@code CharSequence}
   * @param <T> the type of the {@code CharSequence}
   */
  public static <T extends CharSequence> ToIntFunction<T> strlen() {
    return CharSequence::length;
  }

  static {
    tmp.put(strlen(), (arg, argName) -> base(argName, arg) + ".length()");
  }

  /**
   * Returns the upper case version of the argument. Equivalent to
   * {@link String#toUpperCase() String::toUpperCase}.
   *
   * @return The upper case version of the argument
   */
  public static Function<String, String> toUpperCase() {
    return String::toUpperCase;
  }

  static {
    tmp.put(toUpperCase(), (arg, argName) -> base(argName, arg) + ".toUpperCase()");
  }

  /**
   * Returns the lower case version of the argument. Equivalent to
   * {@link String#toLowerCase() String::toLowerCase}.
   *
   * @return The lower case version of the argument
   */
  public static Function<String, String> toLowerCase() {
    return String::toLowerCase;
  }

  static {
    tmp.put(toLowerCase(), (arg, argName) -> base(argName, arg) + ".toLowerCase()");
  }

  /**
   * Returns the {@code Class} of the argument. Equivalent to
   * {@link Object#getClass() Object::getClass}.
   *
   * @param <T> The type of the object
   * @return The {@code Class} of the argument
   */
  public static <T> Function<T, Class<?>> type() {
    return Object::getClass;
  }

  static {
    tmp.put(type(), (arg, argName) -> base(argName, arg) + ".getClass()");
  }

  /**
   * Returns the constants of an enum class. Equivalent to
   * {@link Class#getEnumConstants() Class::getEnumConstants}.
   *
   * @param <T> The enum class
   * @return The constants of an enum class
   */
  public static <T extends Enum<T>> Function<Class<T>, T[]> constants() {
    return x -> (T[]) x.getEnumConstants();
  }

  static {
    tmp.put(constants(),
        (arg, argName) -> base(argName, arg) + ".getEnumConstants()");
  }

  /**
   * Returns the name of an enum constant. Equivalent to
   * {@link Enum#name() Enum::name}.
   *
   * @param <T> The type of the enum class
   * @return The name of the enum constant
   */
  public static <T extends Enum<?>> Function<T, String> name() {
    return Enum::name;
  }

  static {
    tmp.put(name(), (arg, argName) -> base(argName, arg) + ".name()");
  }

  /**
   * Returns the ordinal of an enum constant. Equivalent to
   * {@link Enum#ordinal() Enum::ordinal}.
   *
   * @param <T> The type of the enum class
   * @return The ordinal of the enum constant
   */
  public static <T extends Enum<?>> ToIntFunction<T> ordinal() {
    return Enum::ordinal;
  }

  static {
    tmp.put(ordinal(), (arg, argName) -> base(argName, arg) + ".ordinal()");
  }

  /**
   * A function that returns the length of an array argument.
   *
   * @param <T> The type of the array.
   * @return A {@code Function} that returns the length of an array
   */
  public static <T> ToIntFunction<T> length() {
    return Array::getLength;
  }

  static {
    tmp.put(length(), (arg, argName) -> base(argName, arg) + ".length");
  }

  /**
   * Returns the size of a {@code Collection} argument. Equivalent to
   * {@code Collection::size}.
   *
   * @param <C> The type of the {@code Collection}
   * @return The size of a {@code Collection} argument
   */
  public static <C extends Collection<?>> ToIntFunction<C> size() {
    return Collection::size;
  }

  static {
    tmp.put(size(), (arg, argName) -> base(argName, arg) + ".size()");
  }

  /**
   * Returns the size of a {@code List} argument. Equivalent to {@code List::size}.
   *
   * @param <L> The type of the {@code List}
   * @return Returns the size of a {@code List} argument
   */
  public static <L extends List<?>> ToIntFunction<L> listSize() {
    return List::size;
  }

  static {
    tmp.put(listSize(), tmp.get(size()));
  }

  /**
   * Returns the size of a {@code Set} argument. Equivalent to {@code Set::size}.
   *
   * @param <S> The type of the {@code Set}.
   * @return The size of a {@code Set} argument
   */
  public static <S extends Set<?>> ToIntFunction<S> setSize() {
    return Set::size;
  }

  static {
    tmp.put(setSize(), tmp.get(size()));
  }

  /**
   * Returns the size of a {@code Map} argument. Equivalent to {@code Map::size}.
   *
   * @param <M> The type of the {@code Map}
   * @return The size of a {@code Map} argument
   */
  public static <M extends Map<?, ?>> ToIntFunction<M> mapSize() {
    return Map::size;
  }

  static {
    tmp.put(mapSize(), tmp.get(size()));
  }

  /**
   * Returns the keys of a {@code Map} argument. Equivalent to
   * {@link Map#keySet() Map::keySet}.
   *
   * @param <K> The type of the keys in the map
   * @param <V> The type of the values in the map
   * @param <M> The type of the map
   * @return The keys of a {@code Map} argument
   */
  public static <K, V, M extends Map<K, V>> Function<M, Set<K>> keySet() {
    return Map::keySet;
  }

  static {
    tmp.put(keySet(),
        (arg, argName) -> base(argName, arg) + ".keySet()");
  }

  /**
   * Returns the keys of a {@code Map} argument. Equivalent to
   * {@link Map#values() Map::values}.
   *
   * @param <K> The type of the keys in the map
   * @param <V> The type of the values in the map
   * @param <M> The type of the map
   * @return The values of a {@code Map} argument
   */
  public static <K, V, M extends Map<K, V>> Function<M, Collection<V>> values() {
    return Map::values;
  }

  static {
    tmp.put(values(),
        (arg, argName) -> base(argName, arg) + ".values()");
  }

  /**
   * Returns the key of a {@code Map} entry. Equivalent to
   * {@code Map.Entry::getKey}.
   *
   * @param <K> The type of the key of the entry
   * @param <V> The type of the value of the entry
   * @return The key of a {@code Map} entry
   */
  public static <K, V> Function<Map.Entry<K, V>, K> key() {
    return Map.Entry::getKey;
  }

  static {
    tmp.put(key(), (arg, argName) -> base(argName, arg) + ".getKey()");
  }

  /**
   * Returns the value of a {@code Map} entry. Equivalent to
   * {@code Map.Entry::getValue}.
   *
   * @param <K> The type of the key of the entry
   * @param <V> The type of the value of the entry
   * @return A {@code Function} that returns the value of a {@code Map} entry
   */
  public static <K, V> Function<Map.Entry<K, V>, V> value() {
    return Map.Entry::getValue;
  }

  static {
    tmp.put(value(), (arg, argName) -> base(argName, arg) + ".getValue()");
  }

  /**
   * Returns the absolute value of an {@code int} argument. Equivalent to
   * {@link Math#abs(int) Math::abs}.
   *
   * @return A {@code Function} that returns the absolute value of an integer
   */
  public static IntUnaryOperator abs() {
    return Math::abs;
  }

  static {
    tmp.put(abs(),
        (arg, argName) -> "abs(" + ifNull(argName, DEF_ARG_NAME) + ")");
  }

  //@formatter:off
  private static final Map<Class<?>, UnaryOperator<? extends Number>> absFunctions = Map.of(
      Integer.class,        n -> n.intValue() >= 0 ? n : Integer.valueOf(-n.intValue()),
      Double.class,         n -> n.doubleValue() >= 0 ? n : Double.valueOf(-n.doubleValue()),
      Long.class,           n -> n.longValue() >= 0 ? n : Long.valueOf(-n.longValue()),
      Float.class,          n -> n.floatValue() >= 0 ? n : Float.valueOf(-n.floatValue()),
      Short.class,          n -> n.shortValue() >= 0 ? n : Short.valueOf((short) -n.shortValue()),
      Byte.class,           n -> n.byteValue() >= 0 ? n : Byte.valueOf((byte) -n.byteValue()),
      BigInteger.class,     n -> ((BigInteger) n).abs(),
      BigDecimal.class,     n -> ((BigDecimal) n).abs());
  //@formatter:on

  /**
   * Returns the absolute value of a {@code Number}. The following {@code Number}
   * types are supported: {@code Integer}, {@code Double}, {@code Long},
   * {@code Float}, {@code Short}, {@code Byte}, {@code BigInteger},
   * {@code BigDecimal}.
   *
   * @param <T> The type of the {@code Number}
   * @return The absolute value of a {@code Number}
   */
  @SuppressWarnings("unchecked")
  public static <T extends Number> Function<T, T> ABS() {
    return n -> {
      UnaryOperator op = absFunctions.get(n.getClass());
      if (op != null) {
        return (T) op.apply(n);
      }
      throw typeNotSupported(n.getClass());
    };
  }

  static {
    tmp.put(ABS(), (arg, argName) -> "abs(" + base(argName, arg) + ")");
  }

  /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
  /*            End of getter definitions                    */
  /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

  static String formatProperty(Object arg,
      String argName,
      Object getter,
      Class getterClass) {
    BiFunction<Object, String, String> fmt = NAMES.get(getter);
    if (fmt == null) {
      String s0 = getterClass == ToIntFunction.class ? "applyAsInt" : "apply";
      String s1 = base(argName, arg);
      return simpleClassName(getterClass) + "." + s0 + "(" + s1 + ")";
    }
    return fmt.apply(arg, argName);
  }

  static String formatProperty(int arg,
      String argName,
      Object getter,
      Class getterClass) {
    BiFunction<Object, String, String> fmt = NAMES.get(getter);
    if (fmt == null) {
      String s0 = getterClass == IntUnaryOperator.class ? "applyAsInt" : "apply";
      String s1 = ifNull(argName, "int");
      return simpleClassName(getterClass) + "." + s0 + "(" + s1 + ")";
    }
    return fmt.apply(arg, argName);
  }

  static {
    NAMES = Map.copyOf(tmp);
    tmp = null;
  }

  private static String base(String argName, Object arg) {
    return ifNull(argName, simpleClassName(arg));
  }

  private static <T> T ifNull(T value, T defVal) {
    return value == null ? defVal : value;
  }

}
