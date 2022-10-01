package nl.naturalis.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static nl.naturalis.common.NumberMethods.yes;
import static nl.naturalis.common.TypeConversionException.inputTypeNotSupported;

final class ToFloatConversion {

  static final BigDecimal BIG_MIN_FLOAT = new BigDecimal(Float.toString(Float.MIN_VALUE));
  static final BigDecimal BIG_MAX_FLOAT = new BigDecimal(Float.toString(Float.MAX_VALUE));

  private ToFloatConversion() {
    throw new UnsupportedOperationException();
  }

  private static final Map<Class<?>, Predicate<Number>> fitsIntoFloat = Map.of(
      BigDecimal.class, ToFloatConversion::testBigDecimal,
      BigInteger.class, ToFloatConversion::testBigInteger,
      Double.class, ToFloatConversion::testDouble,
      Float.class, yes(),
      Long.class, yes(),
      AtomicLong.class, yes(),
      Integer.class, yes(),
      AtomicInteger.class, yes(),
      Short.class, yes(),
      Byte.class, yes()
  );

  static boolean isLossless(Number number) {
    Predicate<Number> tester = fitsIntoFloat.get(number.getClass());
    if (tester != null) {
      return tester.test(number);
    }
    throw inputTypeNotSupported(number, Float.class);
  }

  static float exec(Number n) {
    if (isLossless(n)) {
      return n.floatValue();
    }
    throw new TypeConversionException(n, Float.class);
  }

  private static boolean testDouble(Number n) {
    double d = Math.abs(n.doubleValue());
    return d >= Float.MIN_VALUE && d <= Float.MAX_VALUE;
  }

  private static boolean testBigInteger(Number n) {
    return new BigDecimal(((BigInteger) n).abs()).compareTo(BIG_MAX_FLOAT) <= 0;
  }

  private static boolean testBigDecimal(Number n) {
    BigDecimal bd = ((BigDecimal) n).abs();
    return bd.compareTo(BIG_MIN_FLOAT) >= 0 && bd.compareTo(BIG_MAX_FLOAT) <= 0;
  }

}
