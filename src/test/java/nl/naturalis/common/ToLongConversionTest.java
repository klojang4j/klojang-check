package nl.naturalis.common;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.LongAdder;

import static java.math.BigInteger.ONE;
import static nl.naturalis.common.NumberMethods.MAX_DOUBLE_BD;
import static nl.naturalis.common.ToFloatConversion.BIG_MIN_FLOAT;
import static nl.naturalis.common.ToLongConversion.*;
import static org.junit.Assert.*;

public class ToLongConversionTest {

  @Test
  public void testDouble00() {
    assertFalse(isLossless(Double.MAX_VALUE));
    assertFalse(isLossless(Double.MIN_VALUE));
  }

  @Test
  public void testDouble01() {
    assertTrue(isLossless((double) Long.MAX_VALUE));
    assertTrue(isLossless((double) Long.MIN_VALUE));
  }

  @Test
  public void testBigInteger00() {
    assertTrue(isLossless(NumberMethods.MAX_LONG_BI));
  }

  @Test
  public void testBigInteger01() {
    assertTrue(isLossless(NumberMethods.MIN_LONG_BI));
  }

  @Test
  public void testBigDecimal00() {
    BigDecimal bd = new BigDecimal(NumberMethods.MAX_LONG_BI.add(ONE));
    assertFalse(isLossless(bd));
  }

  @Test
  public void testBigDecimal01() {
    BigDecimal bd = new BigDecimal(NumberMethods.MIN_LONG_BI.add(ONE));
    assertTrue(isLossless(bd));
  }

  @Test(expected = TypeConversionException.class)
  public void execDouble00() {
    exec(Double.MAX_VALUE);
  }

  @Test
  public void execDouble01() {
    assertEquals(Long.MIN_VALUE, (long) exec((double) (Long.MIN_VALUE + 1)));
  }

  @Test(expected = TypeConversionException.class)
  public void execBigInteger00() {
    exec(MAX_DOUBLE_BD.toBigInteger());
  }

  @Test
  public void execBigDecimal00() {
    BigDecimal two = BigDecimal.ONE.add(BigDecimal.ONE);
    BigDecimal overflow = two.multiply(BigDecimal.valueOf(Long.MAX_VALUE));
    BigDecimal bd = BIG_MIN_FLOAT.divide(two);
    assertFalse(isLossless(bd));
  }

  @Test
  public void execBigDecimal01() {
    assertEquals(100, (long) exec(BigDecimal.valueOf(100D)));
  }

  @Test(expected = TypeConversionException.class)
  public void execLongAdder00() {
    exec(new LongAdder());
  }

}
