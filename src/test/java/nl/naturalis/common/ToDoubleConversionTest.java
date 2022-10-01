package nl.naturalis.common;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.LongAdder;

import static nl.naturalis.common.NumberMethods.*;
import static org.junit.Assert.*;

public class ToDoubleConversionTest {

  @Test
  public void isLossless00() {
    assertTrue(ToDoubleConversion.isLossless(Double.MAX_VALUE));
    assertTrue(ToDoubleConversion.isLossless(Double.MIN_VALUE));
  }

  @Test
  public void isLossless01() {
    BigDecimal bd = new BigDecimal(MAX_LONG_BI.add(BigInteger.ONE));
    assertTrue(ToDoubleConversion.isLossless(bd));
  }

  @Test
  public void isLossless02() {
    BigDecimal bd = MAX_DOUBLE_BD.add(BigDecimal.ONE);
    assertFalse(ToDoubleConversion.isLossless(bd));
  }

  @Test
  public void isLossless03() {
    BigInteger bi =
        MAX_DOUBLE_BD.toBigInteger().multiply(new BigInteger("2"));
    assertFalse(ToDoubleConversion.isLossless(bi));
  }

  @Test
  public void isLossless04() {
    assertTrue(ToDoubleConversion.isLossless(3.6F));
  }

  @Test
  public void exec00() {
    assertEquals(3.3356, ToDoubleConversion.exec(new BigDecimal("3.3356")), 0);
  }

  @Test
  public void exec01() {
    assertEquals(3D, ToDoubleConversion.exec((byte) 3), 0);
  }

  @Test
  public void exec02() {
    assertEquals(Double.MAX_VALUE,
        ToDoubleConversion.exec(MAX_DOUBLE_BD), 0);
  }

  @Test
  public void exec03() {
    assertEquals((double) Long.MAX_VALUE,
        ToDoubleConversion.exec(MAX_LONG_BD), 0);
  }

  @Test(expected = TypeConversionException.class)
  public void exec04() {
    BigDecimal bd = MAX_DOUBLE_BD.add(BigDecimal.ONE);
    ToDoubleConversion.exec(bd);
  }

  @Test(expected = TypeConversionException.class)
  public void exec05() {
    ToDoubleConversion.exec(new LongAdder());
  }

  @Test
  public void exec06() {
    assertEquals(10.0, ToDoubleConversion.exec(BigInteger.TEN), 0);
  }

  @Test(expected = TypeConversionException.class)
  public void exec07() {
    BigInteger bi =
        MAX_DOUBLE_BD.toBigInteger().multiply(new BigInteger("2"));
    ToDoubleConversion.exec(bi);
  }

}
