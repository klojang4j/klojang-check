package nl.naturalis.common;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.LongAdder;

import static java.math.BigDecimal.ONE;
import static nl.naturalis.common.ToFloatConversion.*;

import static nl.naturalis.common.NumberMethods.MAX_DOUBLE_BD;
import static org.junit.Assert.*;

public class ToFloatConversionTest {

  @Test
  public void testDouble00() {
    assertFalse(isLossless(Double.MAX_VALUE));
    assertFalse(isLossless(Double.MIN_VALUE));
  }

  @Test
  public void testDouble01() {
    assertTrue(isLossless((double) Float.MAX_VALUE));
    assertTrue(isLossless((double) Float.MIN_VALUE));
  }

  @Test
  public void testBigInteger00() {
    BigInteger bi = MAX_DOUBLE_BD.toBigInteger();
    assertFalse(isLossless(bi));
  }

  @Test
  public void testBigInteger01() {
    BigInteger bi = BIG_MAX_FLOAT.toBigInteger().subtract(BigInteger.ONE);
    assertTrue(isLossless(bi));
  }

  @Test
  public void testBigDecimal00() {
    BigDecimal bd = BIG_MAX_FLOAT.add(ONE);
    assertFalse(isLossless(bd));
  }

  @Test
  public void testBigDecimal01() {
    BigDecimal bd = BIG_MAX_FLOAT.subtract(ONE);
    assertTrue(isLossless(bd));
  }

  @Test
  public void testBigDecimal02() {
    BigDecimal two = ONE.add(ONE);
    BigDecimal bd = BIG_MIN_FLOAT.divide(two);
    assertFalse(isLossless(bd));
  }

  @Test
  public void testBigDecimal03() {
    BigDecimal two = ONE.add(ONE);
    BigDecimal bd = BIG_MIN_FLOAT.multiply(two);
    assertTrue(isLossless(bd));
  }

  @Test(expected = TypeConversionException.class)
  public void execDouble00() {
    exec(Double.MAX_VALUE);
  }

  @Test
  public void execDouble01() {
    assertEquals(Float.MIN_VALUE, exec((double) Float.MIN_VALUE), 0F);
  }

  @Test(expected = TypeConversionException.class)
  public void execBigInteger00() {
    exec(MAX_DOUBLE_BD.toBigInteger());
  }

  @Test
  public void execBigInteger01() {
    assertEquals(Float.MAX_VALUE - 1F,
        exec(BIG_MAX_FLOAT.toBigInteger().subtract(BigInteger.ONE)), 0F);
  }

  @Test
  public void execBigDecimal00() {
    BigDecimal two = ONE.add(ONE);
    BigDecimal bd = BIG_MIN_FLOAT.divide(two);
    assertFalse(isLossless(bd));
  }

  @Test
  public void execBigDecimal01() {
    BigDecimal two = ONE.add(ONE);
    BigDecimal bd = BIG_MIN_FLOAT.multiply(two);
    assertEquals(2F * Float.MIN_VALUE, exec(bd), 0F);
  }

  @Test(expected = TypeConversionException.class)
  public void execLongAdder00() {
    exec(new LongAdder());
  }

}
