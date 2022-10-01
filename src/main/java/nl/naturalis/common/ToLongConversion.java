package nl.naturalis.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static nl.naturalis.common.NumberMethods.*;
import static nl.naturalis.common.TypeConversionException.inputTypeNotSupported;

class ToLongConversion {

  private static final Map<Class<?>, Predicate<Number>> fitsIntoLong = Map.of(
      BigDecimal.class, ToLongConversion::testBigDecimal,
      BigInteger.class, ToLongConversion::testBigInteger,
      //Double.class, n -> n.doubleValue() == n.longValue(),
      Double.class, ToLongConversion::testDouble,
      //Float.class, n -> n.floatValue() == n.longValue(),
      Float.class, ToLongConversion::testFloat,
      Long.class, yes(),
      AtomicLong.class, yes(),
      Integer.class, yes(),
      AtomicInteger.class, yes(),
      Short.class, yes(),
      Byte.class, yes()
  );

  static boolean isLossless(Number n) {
    Predicate<Number> tester = fitsIntoLong.get(n.getClass());
    if (tester != null) {
      return tester.test(n);
    }
    throw inputTypeNotSupported(n, Long.class);
  }

  static Long exec(Number n) {
    if (isLossless(n)) {
      return n.longValue();
    }
    throw new TypeConversionException(n, Long.class);
  }

  private static boolean testBigDecimal(Number n) {
    BigDecimal bd = (BigDecimal) n;
    return isRound(bd)
        && bd.compareTo(MIN_LONG_BD) >= 0
        && bd.compareTo(MAX_LONG_BD) <= 0;
  }

  private static boolean testBigInteger(Number n) {
    BigInteger bi = (BigInteger) n;
    return bi.compareTo(MIN_LONG_BI) >= 0
        && bi.compareTo(MAX_LONG_BI) <= 0;
  }

  private static boolean testDouble(Number n) {
    Double d = (Double) n;
    return isRound(d)
        && d >= (double) Long.MIN_VALUE
        && d <= (double) Long.MAX_VALUE;
  }

  private static boolean testFloat(Number n) {
    Float f = (Float) n;
    return isRound(f)
        && f >= (float) Long.MIN_VALUE
        && f <= (float) Long.MAX_VALUE;
  }

}
