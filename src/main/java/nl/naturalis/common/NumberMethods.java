package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.math.BigDecimal.ONE;
import static nl.naturalis.common.ClassMethods.*;
import static nl.naturalis.common.ObjectMethods.isEmpty;
import static nl.naturalis.common.ToFloatConversion.BIG_MAX_FLOAT;
import static nl.naturalis.common.ToFloatConversion.BIG_MIN_FLOAT;
import static nl.naturalis.common.TypeConversionException.inputTypeNotSupported;

/**
 * Methods for parsing, inspecting and converting {@code Number} instances.
 *
 * <p>NB For mathematical operations, see {@link MathMethods}.
 *
 * @author Ayco Holleman
 */
public final class NumberMethods {

  /**
   * {@code Double.MIN_VALUE} converted to a {@code BigDecimal}.
   */
  public static final BigDecimal MIN_DOUBLE_BD =
      new BigDecimal(Double.toString(Double.MIN_VALUE));

  /**
   * {@code Double.MAX_VALUE} converted to a {@code BigDecimal}.
   */
  public static final BigDecimal MAX_DOUBLE_BD =
      new BigDecimal(Double.toString(Double.MAX_VALUE));

  /**
   * {@code Long.MIN_VALUE} converted to a {@code BigDecimal}.
   */
  public static final BigDecimal MIN_LONG_BD =
      new BigDecimal(Long.MIN_VALUE);

  /**
   * {@code Long.MAX_VALUE} converted to a {@code BigDecimal}.
   */
  public static final BigDecimal MAX_LONG_BD =
      new BigDecimal(Long.MAX_VALUE);

  /**
   * {@code Long.MIN_VALUE} converted to a {@code BigInteger}.
   */
  public static final BigInteger MIN_LONG_BI =
      BigInteger.valueOf(Long.MIN_VALUE);

  /**
   * {@code Long.MAX_VALUE} converted to a {@code BigInteger}.
   */
  public static final BigInteger MAX_LONG_BI =
      BigInteger.valueOf(Long.MAX_VALUE);

  static Predicate<Number> yes() {return n -> true;}

  private static final Map<Class<? extends Number>, Function<Number, BigDecimal>>
      toBigDecimal =
      Map.of(
          BigDecimal.class, BigDecimal.class::cast,
          BigInteger.class, x -> new BigDecimal((BigInteger) x),
          Double.class, x -> new BigDecimal(Double.toString((double) x)),
          Float.class, x -> new BigDecimal(Float.toString((float) x)),
          Long.class, x -> new BigDecimal((Long) x),
          AtomicLong.class, x -> new BigDecimal(((AtomicLong) x).get()),
          Integer.class, x -> new BigDecimal((Integer) x),
          AtomicInteger.class, x -> new BigDecimal(((AtomicInteger) x).get()),
          Short.class, x -> new BigDecimal((Short) x),
          Byte.class, x -> new BigDecimal((Byte) x)
      );

  private static final Map<Class<?>, Function<String, Number>> parsers = Map.of(
      BigDecimal.class, NumberMethods::parseBigDecimal,
      BigInteger.class, NumberMethods::parseBigInteger,
      Double.class, NumberMethods::parseDouble,
      Float.class, NumberMethods::parseFloat,
      Long.class, NumberMethods::parseLong,
      AtomicLong.class, s -> new AtomicLong(parseLong(s)),
      Integer.class, NumberMethods::parseInt,
      AtomicInteger.class, s -> new AtomicInteger(parseInt(s)),
      Short.class, NumberMethods::parseShort,
      Byte.class, NumberMethods::parseByte);

  private static final Map<Class<?>, Predicate<String>> stringFitsInto = Map.of(
      BigDecimal.class, NumberMethods::isBigDecimal,
      BigInteger.class, NumberMethods::isBigInteger,
      Double.class, NumberMethods::isDouble,
      Float.class, NumberMethods::isFloat,
      Long.class, NumberMethods::isLong,
      AtomicLong.class, NumberMethods::isLong,
      Integer.class, NumberMethods::isInt,
      AtomicInteger.class, NumberMethods::isInt,
      Short.class, NumberMethods::isShort,
      Byte.class, NumberMethods::isByte);

  private static final Map<Class<?>, Predicate<Number>> numberFitsInto = Map.of(
      BigDecimal.class, ToBigDecimalConversion::isLossless,
      BigInteger.class, ToBigIntegerConversion::isLossless,
      Double.class, ToDoubleConversion::isLossless,
      Float.class, ToFloatConversion::isLossless,
      Long.class, ToLongConversion::isLossless,
      AtomicLong.class, ToLongConversion::isLossless,
      Integer.class, ToIntConversion::isLossless,
      AtomicInteger.class, ToIntConversion::isLossless,
      Short.class, ToShortConversion::isLossless,
      Byte.class, ToByteConversion::isLossless);

  private static final Map<Class<?>, UnaryOperator<Number>> converters = Map.of(
      BigDecimal.class, ToBigDecimalConversion::exec,
      BigInteger.class, ToBigIntegerConversion::exec,
      Double.class, ToDoubleConversion::exec,
      Float.class, ToFloatConversion::exec,
      Long.class, ToLongConversion::exec,
      AtomicLong.class, ToLongConversion::exec,
      Integer.class, ToIntConversion::exec,
      AtomicInteger.class, ToIntConversion::exec,
      Short.class, ToShortConversion::exec,
      Byte.class, ToByteConversion::exec);

  private static final Set<Class<? extends Number>> wrappers = Set.of(Byte.class,
      Short.class,
      Integer.class,
      Long.class,
      Float.class,
      Double.class);

  private static final Set<Class<? extends Number>> integrals = Set.of(Byte.class,
      Short.class,
      Integer.class,
      AtomicInteger.class,
      Long.class,
      AtomicLong.class,
      BigInteger.class);

  private static final Set<Class<? extends Number>> supported = Set.of(Byte.class,
      Short.class,
      Integer.class,
      AtomicInteger.class,
      Long.class,
      AtomicLong.class,
      Float.class,
      Double.class,
      BigInteger.class,
      BigDecimal.class);

  private NumberMethods() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns {@code true} if the specified class is one of the standard primitive
   * number wrappers: {@code Byte}, {@code Short}, {@code Integer}, {@code Long},
   * {@code Float}, {@code Double}.
   *
   * @param numberType the class to test
   * @return whether the class is a primitive number wrapper
   * @see ClassMethods#isPrimitiveNumber(Class)
   */
  public static boolean isWrapper(Class<?> numberType) {
    Check.notNull(numberType);
    return wrappers.contains(numberType);
  }

  /**
   * Returns {@code true} if  the specified number belongs to one of the primitive
   * number wrappers.
   *
   * @param number the number to test
   * @return whether the specified number belongs to one of the primitive number
   *     wrappers
   * @see #isWrapper(Class)
   */
  public static boolean isWrapper(Number number) {
    Check.notNull(number);
    return wrappers.contains(number.getClass());
  }

  /**
   * Returns {@code true} if the specified class is one of {@code Byte},
   * {@code Short}, {@code Integer}, {@code Long}, {@code BigInteger}.
   *
   * @param type the class to test
   * @return whether the class is an integral number type
   */
  public static boolean isIntegral(Class<?> type) {
    return Check.notNull(type).ok(integrals::contains);
  }

  /**
   * Returns {@code true} if the specified number is an integral number.
   *
   * @param number the number to test
   * @return whether the specified number is an integral number
   * @see #isIntegral(Class)
   */
  public static boolean isIntegral(Number number) {
    Check.notNull(number);
    return integrals.contains(number.getClass());
  }

  /**
   * Parses the specified string into a {@code int}. If the string does not represent
   * a number, or if it cannot be parsed into an {@code int} without loss of
   * information, a {@link TypeConversionException} is thrown.
   *
   * @param s the string to be parsed
   * @return the {@code int} value represented by the string
   */
  public static int parseInt(String s) throws TypeConversionException {
    return (int) parseIntegral(s, int.class, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Returns {@code true} if the specified string can be parsed into an {@code int}
   * without loss of information. The argument is allowed to be {@code null}, in
   * which case the return value will be {@code false}.
   *
   * @param s the string to be parsed
   * @return whether the specified string can be parsed into an {@code int} without
   *     causing integer overflow
   */
  public static boolean isInt(String s) {
    return isIntegral(s, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Returns an empty {@code OptionalInt} if the specified string cannot be parsed
   * into a 32-bit integer, else an {@code OptionalInt} containing the {@code int}
   * value parsed out of the string.
   *
   * @param s the string to be parsed
   * @return an {@code OptionalInt} containing the {@code int} value parsed out of
   *     the string
   */
  public static OptionalInt toInt(String s) {
    return toIntegral(s, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Parses the specified string into a {@code long}. If the string does not
   * represent a number, or if it cannot be parsed into a {@code long} without loss
   * of information, a {@link TypeConversionException} is thrown.
   *
   * @param s the string to be parsed
   * @return the {@code long} value represented by the string
   */
  public static long parseLong(String s) throws TypeConversionException {
    if (!isEmpty(s)) {
      BigDecimal bd;
      try {
        bd = new BigDecimal(s);
      } catch (NumberFormatException e) {
        throw new TypeConversionException(s, long.class, e.toString());
      }
      if (fitsIntoLong(bd)) {
        return bd.longValue();
      }
      throw TypeConversionException.targetTypeTooNarrow(s, long.class);
    }
    throw new TypeConversionException(s, long.class);
  }

  /**
   * Returns {@code true} if the specified string can be parsed into a {@code long}
   * without loss of information. The argument is allowed to be {@code null}, in
   * which case the return value will be {@code false}.
   *
   * @param s the string to be parsed
   * @return whether the specified string can be parsed into a {@code long} without
   *     causing integer overflow
   */
  public static boolean isLong(String s) {
    if (!isEmpty(s)) {
      try {
        return fitsIntoLong(new BigDecimal(s));
      } catch (NumberFormatException ignored) {
      }
    }
    return false;
  }

  /**
   * Returns an empty {@code OptionalLong} if the specified string cannot be parsed
   * into a 64-bit integer, else an {@code OptionalLong} containing the {@code long}
   * value parsed out of the string.
   *
   * @param s the string to be parsed
   * @return an {@code OptionalLong} containing the {@code long} value parsed out of
   *     the string
   */
  public static OptionalLong toLong(String s) {
    if (!isEmpty(s)) {
      try {
        BigDecimal bd = new BigDecimal(s);
        if (fitsIntoLong(bd)) {
          return OptionalLong.of(bd.longValue());
        }
      } catch (NumberFormatException ignored) {
      }
    }
    return OptionalLong.empty();
  }

  /**
   * Parses the specified string into a {@code double}. If the string does not
   * represent a number, or if it cannot be parsed into a {@code double} without loss
   * of information, a {@link TypeConversionException} is thrown.
   *
   * @param s the string to be parsed
   * @return the {@code double} value represented by the string
   */
  public static double parseDouble(String s) throws TypeConversionException {
    if (!isEmpty(s)) {
      BigDecimal bd;
      try {
        bd = new BigDecimal(s);
      } catch (NumberFormatException e) {
        throw new TypeConversionException(s, double.class, e.toString());
      }
      BigDecimal x = bd.abs();
      if (x.compareTo(MIN_DOUBLE_BD) >= 0 && x.compareTo(
          MAX_DOUBLE_BD) <= 0) {
        return bd.doubleValue();
      }
    }
    throw TypeConversionException.targetTypeTooNarrow(s, float.class);
  }

  /**
   * Returns {@code true} if the specified string can be parsed into a {@code double}
   * without loss of information. The argument is allowed to be {@code null}, in
   * which case the return value will be {@code false}.
   *
   * @param s the string to be parsed
   * @return whether he specified string can be parsed into a regular, finite
   *     {@code double}
   */
  public static boolean isDouble(String s) {
    if (!isEmpty(s)) {
      try {
        parseDouble(s);
        return true;
      } catch (TypeConversionException ignored) {
      }
    }
    return false;
  }

  /**
   * Returns an empty {@code OptionalDouble} if the specified string cannot be parsed
   * into a {@code double} value, else an {@code OptionalDouble} containing the
   * {@code double} value parsed out of the string.
   *
   * @param s the string to be parsed
   * @return an {@code OptionalDouble} containing the {@code double} value parsed out
   *     of the string
   */
  public static OptionalDouble toDouble(String s) {
    if (!isEmpty(s)) {
      try {
        return OptionalDouble.of(parseDouble(s));
      } catch (TypeConversionException ignored) {
      }
    }
    return OptionalDouble.empty();
  }

  /**
   * Parses the specified string into a {@code float}. If the string does not
   * represent a number, or if it cannot be parsed into a {@code float} without loss
   * of information, a {@link TypeConversionException} is thrown.
   *
   * @param s the string to be parsed
   * @return the {@code float} value represented by the string
   */
  public static float parseFloat(String s) throws TypeConversionException {
    if (!isEmpty(s)) {
      BigDecimal bd;
      try {
        bd = new BigDecimal(s);
      } catch (NumberFormatException e) {
        throw new TypeConversionException(s, double.class, e.toString());
      }
      BigDecimal x = bd.abs();
      if (x.compareTo(BIG_MIN_FLOAT) >= 0 && x.compareTo(BIG_MAX_FLOAT) <= 0) {
        return bd.floatValue();
      }
    }
    throw TypeConversionException.targetTypeTooNarrow(s, float.class);
  }

  /**
   * Returns {@code true} if the specified string can be parsed into a {@code float}
   * without loss of information. The argument is allowed to be {@code null}, in
   * which case the return value will be {@code false}.
   *
   * @param s the string to be parsed
   * @return whether he specified string can be parsed into a regular, finite
   *     {@code float}
   */
  public static boolean isFloat(String s) {
    if (!isEmpty(s)) {
      try {
        parseFloat(s);
        return true;
      } catch (TypeConversionException ignored) {
      }
    }
    return false;
  }

  /**
   * Returns an empty {@code OptionalDouble} if the specified string cannot be parsed
   * into a regular, finite {@code float} value, else an {@code OptionalDouble}
   * containing the {@code float} value parsed out of the string.
   *
   * @param s the string to be parsed
   * @return an {@code OptionalDouble} containing the {@code float} value parsed out
   *     of the string
   */
  public static OptionalDouble toFloat(String s) {
    if (!isEmpty(s)) {
      try {
        return OptionalDouble.of(parseFloat(s));
      } catch (TypeConversionException ignored) {
      }
    }
    return OptionalDouble.empty();
  }

  /**
   * Parses the specified string into a {@code short}. If the string does not
   * represent a number, or if it cannot be parsed into a {@code short} without loss
   * of information, a {@link TypeConversionException} is thrown.
   *
   * @param s the string to be parsed
   * @return the {@code short} value represented by the string
   */
  public static short parseShort(String s) throws TypeConversionException {
    return (short) parseIntegral(s, short.class, Short.MIN_VALUE, Short.MAX_VALUE);
  }

  /**
   * Returns {@code true} if the specified string can be parsed into a {@code short}
   * without loss of information. The argument is allowed to be {@code null}, in
   * which case the return value will be {@code false}.
   *
   * @param s the string to be parsed
   * @return whether he specified string can be parsed into a {@code short} without
   *     causing integer overflow
   */
  public static boolean isShort(String s) {
    return isIntegral(s, Short.MIN_VALUE, Short.MAX_VALUE);
  }

  /**
   * Returns an empty {@code OptionalInt} if the specified string cannot be parsed
   * into a 16-bit integer, else an {@code OptionalInt} containing the {@code short}
   * value parsed out of the string.
   *
   * @param s the string to be parsed
   * @return an {@code OptionalInt} containing the {@code short} value parsed out of
   *     the string
   */
  public static OptionalInt toShort(String s) {
    return toIntegral(s, Short.MIN_VALUE, Short.MAX_VALUE);
  }

  /**
   * Parses the specified string into a {@code byte}. If the string does not
   * represent a number, or if it cannot be parsed into a {@code byte} without loss
   * of information, a {@link TypeConversionException} is thrown.
   *
   * @param s the string to be parsed
   * @return the {@code byte} value represented by the string
   */
  public static byte parseByte(String s) throws TypeConversionException {
    return (byte) parseIntegral(s, byte.class, Byte.MIN_VALUE, Byte.MAX_VALUE);
  }

  /**
   * Returns {@code true} if the specified string can be parsed into a {@code byte}
   * without loss of information. The argument is allowed to be {@code null}, in
   * which case the return value will be {@code false}.
   *
   * @param s the string to be parsed
   * @return whether he specified string can be parsed into a {@code byte} without
   *     causing an integer overflow
   */
  public static boolean isByte(String s) {
    return isIntegral(s, Byte.MIN_VALUE, Byte.MAX_VALUE);
  }

  /**
   * Returns an empty {@code OptionalInt} if the specified string cannot be parsed
   * into an 8-bit integer, else an {@code OptionalInt} containing the {@code byte}
   * value parsed out of the string.
   *
   * @param s the string to be parsed
   * @return an {@code OptionalInt} containing the {@code byte} value parsed out of
   *     the string
   */
  public static OptionalInt toByte(String s) {
    return toIntegral(s, Byte.MIN_VALUE, Byte.MAX_VALUE);
  }

  /**
   * Parses the specified string into a {@code BigInteger}. If the string does not
   * represent a number, or if it cannot be parsed into a {@code BigInteger} without
   * loss of information, a {@link TypeConversionException} is thrown.
   *
   * @param s the string to be parsed
   * @return the {@code BigInteger} value represented by the string
   */
  public static BigInteger parseBigInteger(String s) {
    if (!isEmpty(s)) {
      try {
        BigDecimal bd = new BigDecimal(s);
        if (isRound(bd)) {
          return bd.toBigInteger();
        }
        throw TypeConversionException.targetTypeTooNarrow(s, BigInteger.class);
      } catch (NumberFormatException e) {
        throw new TypeConversionException(s, BigInteger.class, e.toString());
      }
    }
    throw new TypeConversionException(s, BigInteger.class);
  }

  /**
   * Returns {@code true} if the specified string can be parsed into a
   * {@code BigInteger} without loss of information. The argument is allowed to be
   * {@code null}, in which case the return value will be {@code false}.
   *
   * @param s the string to be parsed
   * @return whether he specified string can be parsed into a {@code BigInteger}
   */
  public static boolean isBigInteger(String s) {
    if (!isEmpty(s)) {
      try {
        BigDecimal bd = new BigDecimal(s);
        if (isRound(bd)) {
          return true;
        }
      } catch (NumberFormatException ignored) {
      }
    }
    return false;
  }

  /**
   * Returns an empty {@code Optional} if the specified string cannot be parsed into
   * BigInteger, else an {@code Optional} containing the {@code BigInteger} value
   * parsed out of the string.
   *
   * @param s the string to be parsed
   * @return an {@code Optional} containing the {@code BigInteger} value parsed out
   *     of the string
   */
  public static Optional<BigInteger> toBigInteger(String s) {
    if (!isEmpty(s)) {
      try {
        BigDecimal bd = new BigDecimal(s);
        if (isRound(bd)) {
          return Optional.of(bd.toBigInteger());
        }
      } catch (NumberFormatException ignored) {
      }
    }
    return Optional.empty();
  }

  /**
   * Parses the specified string into a {@code BigInteger}. If the string does not
   * represent a number, a {@link TypeConversionException} is thrown.
   *
   * @param s the string to be parsed
   * @return the {@code BigDecimal} value represented by the string
   */
  public static BigDecimal parseBigDecimal(String s) {
    try {
      return new BigDecimal(s);
    } catch (NumberFormatException e) {
      throw new TypeConversionException(s, BigDecimal.class, e.toString());
    }
  }

  /**
   * Returns {@code true} if the specified string can be parsed into a
   * {@code BigDecimal}. The argument is allowed to be {@code null}, in which case
   * the return value will be {@code false}.
   *
   * @param s the string to be parsed
   * @return whether he specified string can be parsed into a {@code BigDecimal}
   */
  public static boolean isBigDecimal(String s) {
    if (!isEmpty(s)) {
      try {
        parseBigDecimal(s);
        return true;
      } catch (TypeConversionException ignored) {
      }
    }
    return false;
  }

  /**
   * Returns an empty {@code Optional} if the specified string cannot be parsed into
   * BigDecimal, else an {@code Optional} containing the {@code BigDecimal} value
   * parsed out of the string.
   *
   * @param s the string to be parsed
   * @return an {@code Optional} containing the {@code BigDecimal} value parsed out
   *     of the string
   */
  public static Optional<BigDecimal> toBigDecimal(String s) {
    if (!isEmpty(s)) {
      try {
        return Optional.of(parseBigDecimal(s));
      } catch (TypeConversionException ignored) {
      }
    }
    return Optional.empty();
  }

  /**
   * Parses the specified string into a number of the specified type. Throws an
   * {@link TypeConversionException} if the string is not a number, or if the number
   * is too big to fit into the target type.
   *
   * @param <T> the type of {@code Number} to convert the string to
   * @param s the string to be parsed
   * @param targetType the class of the {@code Number} type
   * @return a {@code Number} of the specified type
   */
  @SuppressWarnings("unchecked")
  public static <T extends Number> T parse(String s, Class<T> targetType)
      throws TypeConversionException {
    Check.notNull(targetType, Param.TARGET_TYPE);
    Function<String, Number> parser = parsers.get(targetType);
    if (parser != null) {
      return (T) parser.apply(s);
    }
    throw notSupported(s, targetType);
  }

  /**
   * Tests whether the specified string can be parsed into a {@code Number} of the
   * specified type.
   *
   * @param <T> the type of {@code Number} to convert the string to
   * @param s the string to be parsed
   * @param targetType the class of the {@code Number} type
   * @return whether the specified string can be parsed into a {@code Number} of the
   *     specified type
   */
  public static <T extends Number> boolean fitsInto(String s, Class<T> targetType) {
    Check.notNull(targetType, Param.TARGET_TYPE);
    if (!isEmpty(s)) {
      Predicate<String> tester = stringFitsInto.get(targetType);
      if (tester != null) {
        return tester.test(s);
      }
      throw notSupported(s, targetType);
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////////
  //                           END OF PARSE METHODS                             //
  ////////////////////////////////////////////////////////////////////////////////

  /**
   * Converts a {@code Number} of an unspecified type to a {@code BigDecimal}.
   *
   * @param n the number
   * @return the {@code BigDecimal} representing the number
   */
  public static BigDecimal toBigDecimal(Number n) {
    Check.notNull(n);
    Function<Number, BigDecimal> fnc = toBigDecimal.get(n.getClass());
    if (fnc != null) {
      return fnc.apply(n);
    }
    throw inputTypeNotSupported(n, BigDecimal.class);
  }

  /**
   * Safely converts a number of an unspecified type to a number of a definite type.
   * Throws a {@link TypeConversionException} if the number cannot be converted to
   * the target type without loss of information. The number is allowed to be
   * {@code null}, in which case {@code null} will be returned.
   *
   * @param <T> the input type
   * @param <R> the output type
   * @param number the number to be converted
   * @param targetType the class of the target type
   * @return an instance of the target type
   */
  @SuppressWarnings({"unchecked"})
  public static <T extends Number, R extends Number> R convert(T number,
      Class<R> targetType) throws TypeConversionException {
    Check.notNull(targetType, Param.TARGET_TYPE);
    if (number == null || number.getClass() == targetType) {
      return (R) number;
    }
    UnaryOperator<Number> fnc = converters.get(targetType);
    if (fnc != null) {
      return (R) fnc.apply(number);
    }
    throw notSupported(number, targetType);
  }

  /**
   * Returns {@code true} if the specified number can be converted to the specified
   * target type without loss of information. The number is allowed to be
   * {@code null}, in which case {@code true} will be returned.
   *
   * @param <T> the type of {@code Number} to convert to
   * @param number the {@code Number} to convert
   * @param targetType the type of {@code Number} to convert to
   * @return whether conversion will be lossless
   */
  public static <T extends Number> boolean fitsInto(Number number,
      Class<T> targetType) {
    Check.notNull(targetType, Param.TARGET_TYPE);
    if (number == null) {
      return true;
    }
    Predicate<Number> tester = numberFitsInto.get(targetType);
    if (tester != null) {
      return tester.test(number);
    }
    throw inputTypeNotSupported(number, targetType);
  }

  /**
   * Determines whether the specified float's fractional part is zero or absent.
   *
   * @param f the {@code float} to inspect
   * @return whether the specified float's fractional part is zero or absent
   */
  public static boolean isRound(float f) {
    return isRound(new BigDecimal(Float.toString(f)));
  }

  /**
   * Determines whether the specified double's fractional part is zero or absent.
   *
   * @param d the {@code double} to inspect
   * @return whether the specified double's fractional part is zero or absent
   */
  public static boolean isRound(double d) {
    return isRound(new BigDecimal(Double.toString(d)));
  }

  /**
   * Determines whether the specified BigDecimal's fractional part is zero or
   * absent.
   *
   * @param bd the {@code BigDecimal} to inspect
   * @return whether the specified BigDecimal's fractional part is zero or absent
   */
  public static boolean isRound(BigDecimal bd) {
    // NB The 1st check is cheap and catches a lot of the cases.
    // The 2nd second is superfluous in the presence of the 3rd.
    // However, it still catches quite a few cases and seems
    // sufficiently cheap compared to the 3rd check that it seems
    // worth paying the price of wasted CPU cycles if we do fall
    // through to the 3rd check. The 3rd and 4th check are
    // equivalent, but the 3rd check seems more efficient. Needs
    // to be measured though.
    return bd.scale() <= 0
        || bd.stripTrailingZeros().scale() == 0
        || bd.divideToIntegralValue(ONE).compareTo(bd) == 0
        /*|| bd.remainder(ONE).signum() == 0 */;
  }

  private static long parseIntegral(String s,
      Class<?> type,
      long minVal,
      long maxVal) {
    if (!isEmpty(s)) {
      try {
        BigDecimal bd = new BigDecimal(s);
        long l;
        if (isRound(bd) && (l = bd.longValue()) >= minVal && l <= maxVal) {
          return l;
        }
        throw TypeConversionException.targetTypeTooNarrow(s, type);
      } catch (NumberFormatException e) {
        throw new TypeConversionException(s, int.class, e.toString());
      }
    }
    throw new TypeConversionException(s, type);
  }

  private static boolean isIntegral(String s, long minVal, long maxVal) {
    if (!isEmpty(s)) {
      try {
        BigDecimal bd = new BigDecimal(s);
        long l;
        return isRound(bd) && (l = bd.longValue()) >= minVal && l <= maxVal;
      } catch (NumberFormatException ignored) {
      }
    }
    return false;
  }

  private static OptionalInt toIntegral(String s, long minVal, long maxVal) {
    if (!isEmpty(s)) {
      try {
        BigDecimal bd = new BigDecimal(s);
        long l;
        if (isRound(bd) && (l = bd.longValue()) >= minVal && l <= maxVal) {
          return OptionalInt.of((int) l);
        }
      } catch (NumberFormatException ignored) {
      }
    }
    return OptionalInt.empty();
  }

  private static boolean fitsIntoLong(BigDecimal bd) {
    return isRound(bd)
        && bd.compareTo(MIN_LONG_BD) >= 0
        && bd.compareTo(MAX_LONG_BD) <= 0;
  }

  private static TypeConversionException notSupported(Object obj,
      Class<?> type) {
    if (isPrimitiveNumber(type)) {
      String fmt = "primitive types not supported *** call ClassMethods.box to convert %s to %s";
      String c0 = type.getName();
      String c1 = box(type).getSimpleName();
      return new TypeConversionException(obj, type, fmt, c0, c1);
    } else if (isSubtype(type, Number.class)) {
      String c0 = type.getSimpleName();
      return new TypeConversionException(obj, type, "%s not supported", c0);
    }
    String msg = "target type must be subclass of Number";
    return new TypeConversionException(obj, type, msg);
  }

}
