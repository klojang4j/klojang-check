package nl.naturalis.check;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;

/*
 * Implementations of string-to-number checks in the CommonChecks class. Separate
 * from the CheckImpl class because there are quite a few of them.
 */
final class StringCheckImpls {

  private static final BigDecimal MIN_DOUBLE_BD =
      new BigDecimal(Double.toString(Double.MIN_VALUE));

  private static final BigDecimal MAX_DOUBLE_BD =
      new BigDecimal(Double.toString(Double.MAX_VALUE));

  private static final BigDecimal BIG_MIN_FLOAT =
      new BigDecimal(Float.toString(Float.MIN_VALUE));
  private static final BigDecimal BIG_MAX_FLOAT =
      new BigDecimal(Float.toString(Float.MAX_VALUE));

  private static final BigDecimal MIN_LONG_BD = new BigDecimal(Long.MIN_VALUE);

  private static final BigDecimal MAX_LONG_BD = new BigDecimal(Long.MAX_VALUE);

  private StringCheckImpls() {
    throw new UnsupportedOperationException();
  }

  static boolean isInt(String s) {
    return isIntegral(s, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  static boolean isLong(String s) {
    if (notEmpty(s)) {
      try {
        return fitsIntoLong(new BigDecimal(s));
      } catch (NumberFormatException ignored) {
      }
    }
    return false;
  }

  static boolean isDouble(String s) {
    if (notEmpty(s)) {
      BigDecimal bd;
      try {
        bd = new BigDecimal(s);
      } catch (NumberFormatException e) {
        return false;
      }
      BigDecimal x = bd.abs();
      return x.compareTo(MIN_DOUBLE_BD) >= 0 && x.compareTo(MAX_DOUBLE_BD) <= 0;
    }
    return false;
  }

  static boolean isFloat(String s) {
    if (notEmpty(s)) {
      BigDecimal bd;
      try {
        bd = new BigDecimal(s);
      } catch (NumberFormatException e) {
        return false;
      }
      BigDecimal x = bd.abs();
      return x.compareTo(BIG_MIN_FLOAT) >= 0 && x.compareTo(BIG_MAX_FLOAT) <= 0;
    }
    return false;
  }

  static boolean isShort(String s) {
    return isIntegral(s, Short.MIN_VALUE, Short.MAX_VALUE);
  }

  static boolean isByte(String s) {
    return isIntegral(s, Byte.MIN_VALUE, Byte.MAX_VALUE);
  }

  static boolean isRound(double d) {
    return isRound(new BigDecimal(Double.toString(d)));
  }

  static boolean isRound(BigDecimal bd) {
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

  private static boolean isIntegral(String s, long minVal, long maxVal) {
    if (notEmpty(s)) {
      try {
        BigDecimal bd = new BigDecimal(s);
        long l;
        return isRound(bd) && (l = bd.longValue()) >= minVal && l <= maxVal;
      } catch (NumberFormatException ignored) {
      }
    }
    return false;
  }

  private static boolean fitsIntoLong(BigDecimal bd) {
    return isRound(bd)
        && bd.compareTo(MIN_LONG_BD) >= 0
        && bd.compareTo(MAX_LONG_BD) <= 0;
  }

  private static boolean notEmpty(String s) {
    return s != null && !s.isEmpty();
  }

}
