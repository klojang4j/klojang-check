package nl.naturalis.common;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.*;

public class ToShortConversionTest {

  @Test
  public void bigDecimalIsLossless00() {
    assertTrue(ToShortConversion.isLossless(BigDecimal.TEN));
    assertTrue(ToShortConversion.isLossless(BigDecimal.valueOf(Short.MIN_VALUE)));
    assertTrue(ToShortConversion.isLossless(BigDecimal.valueOf(Short.MAX_VALUE)));
    assertFalse(ToShortConversion.isLossless(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertFalse(ToShortConversion.isLossless(BigDecimal.valueOf(Integer.MAX_VALUE)));
    assertFalse(ToShortConversion.isLossless(new BigDecimal("1.2")));
  }

  @Test
  public void bigDecimalExec00() {
    assertEquals((short) 10, ToShortConversion.exec(BigDecimal.TEN));
    assertEquals(Short.MIN_VALUE,
        ToShortConversion.exec(BigDecimal.valueOf(Short.MIN_VALUE)));
  }

  @Test(expected = TypeConversionException.class)
  public void bigDecimalExec01() {
    ToShortConversion.exec(BigDecimal.valueOf(Integer.MIN_VALUE));
  }

  @Test
  public void bigIntegerIsLossless00() {
    assertTrue(ToShortConversion.isLossless(BigInteger.TEN));
    assertTrue(ToShortConversion.isLossless(new BigInteger(String.valueOf(Short.MIN_VALUE))));
    assertFalse(ToShortConversion.isLossless(new BigInteger(String.valueOf(Integer.MIN_VALUE))));
  }

  @Test
  public void bigIntegerExec00() {
    assertEquals((short) 10, ToShortConversion.exec(BigInteger.TEN));
    assertEquals(Short.MIN_VALUE,
        ToShortConversion.exec(new BigInteger(String.valueOf(Short.MIN_VALUE))));
  }

  @Test(expected = TypeConversionException.class)
  public void bigIntegerExec01() {
    ToShortConversion.exec(new BigInteger(String.valueOf(Integer.MIN_VALUE)));
  }

  @Test
  public void doubleIsLossless00() {
    assertTrue(ToShortConversion.isLossless(42.00));
    assertTrue(ToShortConversion.isLossless(-42.000));
    assertFalse(ToShortConversion.isLossless(42.0001));
  }

  @Test
  public void doubleExec00() {
    assertEquals((short) 42, ToShortConversion.exec(42.00D));
    assertEquals((short) -42, ToShortConversion.exec(-42D));
  }

  @Test(expected = TypeConversionException.class)
  public void doubleExec01() {
    ToShortConversion.exec(42.0001);
  }

  @Test
  public void floatIsLossless00() {
    assertTrue(ToShortConversion.isLossless(42.00F));
    assertTrue(ToShortConversion.isLossless(-42.000F));
    assertFalse(ToShortConversion.isLossless(42.0001F));
  }

  @Test
  public void floatExec00() {
    assertEquals((short) 42, ToShortConversion.exec(42.00F));
    assertEquals((short) -42, ToShortConversion.exec(-42F));
  }

  @Test(expected = TypeConversionException.class)
  public void floatExec01() {
    ToShortConversion.exec(42.0001F);
  }

  @Test
  public void longIsLossless00() {
    assertTrue(ToShortConversion.isLossless(42L));
    assertTrue(ToShortConversion.isLossless(-42L));
    assertTrue(ToShortConversion.isLossless(new AtomicLong(42L)));
    assertFalse(ToShortConversion.isLossless((long) Short.MAX_VALUE * 2L));
    assertFalse(ToShortConversion.isLossless(new AtomicLong(Short.MAX_VALUE * 2L)));
  }

  @Test
  public void longExec00() {
    assertEquals((short) 42, ToShortConversion.exec(42L));
    assertEquals((short) -42, ToShortConversion.exec(-42L));
  }

  @Test(expected = TypeConversionException.class)
  public void longExec01() {
    ToShortConversion.exec((long) Short.MAX_VALUE * 2L);
  }

  @Test
  public void integerIsLossless00() {
    assertTrue(ToShortConversion.isLossless(42));
    assertTrue(ToShortConversion.isLossless(-42L));
    assertTrue(ToShortConversion.isLossless(new AtomicInteger(42)));
    assertFalse(ToShortConversion.isLossless((int) Short.MAX_VALUE * 2));
    assertFalse(ToShortConversion.isLossless(
        new AtomicInteger(Short.MAX_VALUE * 2)));
  }

  @Test
  public void integerExec00() {
    assertEquals((short) 42, ToShortConversion.exec(42));
    assertEquals((short) -42, ToShortConversion.exec(-42));
  }

  @Test(expected = TypeConversionException.class)
  public void integerExec01() {
    ToShortConversion.exec((int) Short.MAX_VALUE * 2);
  }

  @Test(expected = TypeConversionException.class)
  public void isLossless00() {
    ToShortConversion.isLossless(new LongAdder());
  }

  @Test(expected = TypeConversionException.class)
  public void exec00() {
    ToShortConversion.exec(new LongAdder());
  }

}