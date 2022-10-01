package nl.naturalis.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static nl.naturalis.common.NumberMethods.*;
import static nl.naturalis.common.TypeConversionException.inputTypeNotSupported;

final class ToBigIntegerConversion {

  private ToBigIntegerConversion() {
    throw new UnsupportedOperationException();
  }

  private static final Map<Class<?>, Predicate<Number>> fitsIntoBigInt = Map.of(
      BigDecimal.class, ToBigIntegerConversion::testBigDecimal,
      BigInteger.class, yes(),
      Double.class, n -> isRound((Double) n),
      Float.class, n -> isRound((Float) n),
      Long.class, yes(),
      AtomicLong.class, yes(),
      Integer.class, yes(),
      AtomicInteger.class, yes(),
      Short.class, yes(),
      Byte.class, yes()
  );

  static boolean isLossless(Number n) {
    Predicate<Number> tester = fitsIntoBigInt.get(n.getClass());
    if (tester != null) {
      return tester.test(n);
    }
    throw inputTypeNotSupported(n, BigInteger.class);
  }

  static BigInteger exec(Number n) {
    if (isLossless(n)) {
      return toBigDecimal(n).toBigInteger();
    }
    throw new TypeConversionException(n, BigInteger.class);
  }

  private static boolean testBigDecimal(Number n) {
    try {
      ((BigDecimal) n).toBigIntegerExact();
      return true;
    } catch (ArithmeticException e) {
      return false;
    }
  }

}
