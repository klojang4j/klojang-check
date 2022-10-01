package nl.naturalis.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static nl.naturalis.common.NumberMethods.isRound;
import static nl.naturalis.common.NumberMethods.yes;
import static nl.naturalis.common.TypeConversionException.inputTypeNotSupported;

final class ToIntConversion {

  private ToIntConversion() {
    throw new UnsupportedOperationException();
  }

  private static final Map<Class<?>, Predicate<Number>> fitsIntoInt = Map.of(
      BigDecimal.class,
      n -> isRound((BigDecimal) n) && n.longValue() == n.intValue(),
      BigInteger.class, n -> n.longValue() == n.intValue(),
      Double.class, n -> isRound((Double) n) && n.longValue() == n.intValue(),
      Float.class, n -> isRound((Float) n) && n.longValue() == n.intValue(),
      Long.class, n -> n.longValue() == n.intValue(),
      AtomicLong.class, n -> n.longValue() == n.intValue(),
      Integer.class, yes(),
      AtomicInteger.class, yes(),
      Short.class, yes(),
      Byte.class, yes()
  );

  static boolean isLossless(Number n) {
    Predicate<Number> tester = fitsIntoInt.get(n.getClass());
    if (tester != null) {
      return tester.test(n);
    }
    throw inputTypeNotSupported(n, Integer.class);
  }

  static int exec(Number n) {
    if (isLossless(n)) {
      return n.intValue();
    }
    throw new TypeConversionException(n, Integer.class);
  }

}
