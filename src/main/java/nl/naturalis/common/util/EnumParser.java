package nl.naturalis.common.util;

import nl.naturalis.check.Check;
import nl.naturalis.common.NumberMethods;
import nl.naturalis.common.TypeConversionException;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonExceptions.DUPLICATE;
import static nl.naturalis.common.NumberMethods.isIntegral;
import static nl.naturalis.common.util.EnumParser.ParseTarget.*;

/**
 * Parses strings into enum constants. Internally {@code EnumParser} maintains a
 * string-to-enum map containing normalized versions of {@link Enum#name()} and
 * {@link Enum#toString()} as keys. The strings to be parsed are normalized using the
 * same normalization function, and then looked up in the map. The normalizer
 * function is customizable. Note that the {@link #parse(Object) parse} method takes
 * an argument of type {@code Object} (rather than {@code String}). You can, in fact,
 * instruct the parser to be prepared for receiving the ordinal value of an enum
 * constant. You can even instruct it to be prepared for simply receiving an enum
 * constant itself. This is may be useful in dynamic contexts where it is not known
 * beforehand whether the incoming value perhaps already is (or has been converted
 * to) an enum constant. By default, the parser will be on the lookout for the name,
 * the ordinal value and the string representation of the enum constants.
 *
 * <blockquote><pre>{@code
 * enum TransportType {
 *  CAR, BIKE, TRAIN;
 *
 *  private static EnumParser<TransportType> parser = new EnumParser(TransportType.class);
 *
 *  @JsonCreator
 *  public static TransportType parse(String input) {
 *      return parser.parse(input);
 *  }
 * }
 * }</pre></blockquote>
 *
 * @param <T> The type of the {@code enum}
 * @author Ayco Holleman
 */
public final class EnumParser<T extends Enum<T>> {

  /**
   * Symbolic constants for what the value to be converted represents.
   */
  public enum ParseTarget {
    /**
     * Indicates that the value to be converted is supposed to be the
     * {@linkplain Enum#name() name} of an enum constant.
     */
    NAME,
    /**
     * Indicates that the value to be converted is supposed to be the
     * {@linkplain Enum#ordinal() ordinal value} of an enum constant.
     */
    ORDINAL,
    /**
     * Indicates that the value to be converted is supposed to be the string
     * representation of an enum constant.
     */
    TO_STRING,
    /**
     * Indicates that the value to be converted is supposed to be already an enum
     * constant and must be returned <i>as-is</i> by the parser. This may be useful
     * in dynamic contexts where it is not known beforehand whether the incoming
     * value perhaps already is (or has been converted to) an enum constant.
     */
    IDENTITY
  }

  private static final String BAD_KEY = "duplicate key: ${arg}";

  /**
   * The default normalization function. Removes spaces, hyphens and underscores and
   * returns an all-lowercase string.
   */
  public static final UnaryOperator<String> DEFAULT_NORMALIZER =
      s -> Check.notNull(s).ok().replaceAll("[-_ ]", "").toLowerCase();

  private final Class<T> enumClass;
  private final UnaryOperator<String> normalizer;
  private final Set<ParseTarget> targets;
  private final Map<String, T> lookups;

  /**
   * Creates an {@code EnumParser} for the specified enum class, using the
   * {@link #DEFAULT_NORMALIZER}.
   *
   * @param enumClass The enum class
   */
  public EnumParser(Class<T> enumClass) {
    this(enumClass, DEFAULT_NORMALIZER);
  }

  /**
   * Creates an {@code EnumParser} for the specified enum class, using the specified
   * {@code normalizer} to normalize the strings to be parsed.
   *
   * @param enumClass the enum class managed by this {@code EnumParser}
   * @param normalizer the normalization function
   */
  public EnumParser(Class<T> enumClass, UnaryOperator<String> normalizer) {
    this(enumClass, normalizer, EnumSet.of(NAME, ORDINAL, TO_STRING));
  }

  /**
   * Creates an {@code EnumParser} for the specified enum class, using the specified
   * {@code normalizer} to normalize the strings to be parsed.
   *
   * @param enumClass the enum class managed by this {@code EnumParser}
   * @param normalizer the normalization function
   * @param parseTargets the aspects of an enum constant that the values to be
   *     converted may represent (the constant's name, ordinal value, string
   *     representation, or the constant itself).
   */
  public EnumParser(Class<T> enumClass, UnaryOperator<String> normalizer,
      Set<ParseTarget> parseTargets) {
    this.enumClass = Check.notNull(enumClass, "enumClass").ok();
    this.normalizer = Check.notNull(normalizer, "normalizer").ok();
    this.targets = Check.that(parseTargets, "parseTargets").is(deepNotEmpty()).ok();
    HashMap<String, T> tmp = new HashMap<>();
    if (parseTargets.contains(NAME)) {
      if (parseTargets.contains(TO_STRING)) {
        for (T e : enumClass.getEnumConstants()) {
          if (e.name().equals(e.toString())) {
            addKey(e, tmp, Enum::toString);
          } else {
            addKey(e, tmp, Enum::name);
            addKey(e, tmp, Enum::toString);
          }
        }
      } else {
        addKeys(enumClass, tmp, Enum::name);
      }
    } else if (parseTargets.contains(ParseTarget.TO_STRING)) {
      addKeys(enumClass, tmp, Enum::toString);
    }
    this.lookups = Map.copyOf(tmp);
  }

  /**
   * Parses the specified value into an enum constant.
   *
   * @param value The value to be mapped an enum constant.
   * @return The enum constant
   * @throws TypeConversionException If the value was {@code null} or could not
   *     be mapped to one of the enum's constants.
   */
  public T parse(Object value) throws TypeConversionException {
    if (value != null) {
      if (targets.contains(ORDINAL) && isIntegral(value.getClass())) {
        int ordinal = NumberMethods.convert((Number) value, Integer.class);
        return Check.that(ordinal)
            .is(indexOf(), enumClass.getEnumConstants(), noSuchConstant(value))
            .ok(x -> enumClass.getEnumConstants()[x]);
      } else if (targets.contains(IDENTITY) && enumClass.isInstance(value)) {
        return enumClass.cast(value);
      }
    }
    String key = normalize(Objects.toString(value));
    return Check.that(lookups.get(key)).is(notNull(), noSuchConstant(value)).ok();
  }

  private String normalize(String s) {
    try {
      return normalizer.apply(s);
    } catch (TypeConversionException tce) {
      throw tce;
    } catch (Throwable t) {
      throw new TypeConversionException(s, enumClass, t.getMessage());
    }
  }

  private Supplier<TypeConversionException> noSuchConstant(Object value) {
    return () -> new TypeConversionException(value, enumClass);
  }

  private void addKey(T e,
      HashMap<String, T> map,
      Function<T, String> stringifier) {
    String key = normalize(stringifier.apply(e));
    Check.on(DUPLICATE, key).isNot(keyIn(), map, BAD_KEY);
    map.put(key, e);
  }

  private void addKeys(Class<T> enumClass,
      HashMap<String, T> map,
      Function<T, String> stringifier) {
    for (T e : enumClass.getEnumConstants()) {
      addKey(e, map, stringifier);
    }
  }

}
