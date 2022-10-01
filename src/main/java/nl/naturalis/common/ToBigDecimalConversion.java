package nl.naturalis.common;

import java.math.BigDecimal;

import static nl.naturalis.common.NumberMethods.toBigDecimal;

final class ToBigDecimalConversion {

  private ToBigDecimalConversion() {
    throw new UnsupportedOperationException();
  }

  @SuppressWarnings("unused")
  static boolean isLossless(Number n) {
    return true;
  }

  static BigDecimal exec(Number n) {
    return toBigDecimal(n);
  }

}
