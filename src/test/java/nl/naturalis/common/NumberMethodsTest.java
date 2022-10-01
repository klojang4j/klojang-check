package nl.naturalis.common;

import org.junit.Test;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import static nl.naturalis.common.NumberMethods.*;
import static nl.naturalis.common.ObjectMethods.bruteCast;
import static nl.naturalis.common.NumberMethods.MAX_DOUBLE_BD;
import static nl.naturalis.common.NumberMethods.MIN_DOUBLE_BD;
import static org.junit.Assert.*;

public class NumberMethodsTest {

  @Test
  public void fitsInto01() {
    assertTrue(fitsInto(Double.MAX_VALUE, Double.class));
    assertTrue(fitsInto(Double.MIN_VALUE, Double.class));
    assertTrue(fitsInto(Float.MAX_VALUE, Double.class));
    assertTrue(fitsInto(Float.MIN_VALUE, Double.class));
    assertTrue(fitsInto(Long.MAX_VALUE, Double.class));
    assertTrue(fitsInto(Long.MIN_VALUE, Double.class));
    assertTrue(fitsInto(Integer.MAX_VALUE, Double.class));
    assertTrue(fitsInto(Integer.MIN_VALUE, Double.class));
    assertTrue(fitsInto(Short.MAX_VALUE, Double.class));
    assertTrue(fitsInto(Short.MIN_VALUE, Double.class));
    assertTrue(fitsInto(Byte.MAX_VALUE, Double.class));
    assertTrue(fitsInto(Byte.MIN_VALUE, Double.class));
    assertTrue(fitsInto((short) 2, Double.class));
    assertTrue(fitsInto(3L, Double.class));
    assertTrue(fitsInto((Number) null, Double.class));
    assertTrue(fitsInto((Number) null, Float.class));
    assertTrue(fitsInto((Number) null, Long.class));
    assertTrue(fitsInto((Number) null, Integer.class));
    assertTrue(fitsInto((Number) null, Short.class));
    assertTrue(fitsInto((Number) null, Byte.class));
    assertTrue(fitsInto((Number) null, AtomicLong.class));
    assertTrue(fitsInto((Number) null, AtomicInteger.class));
    assertTrue(fitsInto((Number) null, BigDecimal.class));
    assertTrue(fitsInto((Number) null, BigInteger.class));
  }

  @Test
  public void fitsInto02() {
    assertFalse(fitsInto(Double.MAX_VALUE, Float.class));
    assertFalse(fitsInto(Double.MIN_VALUE, Float.class));
    assertTrue(fitsInto(Double.valueOf(Float.MAX_VALUE), Float.class));
    assertTrue(fitsInto(Double.valueOf(Float.MIN_VALUE), Float.class));
    assertFalse(fitsInto(Double.MIN_VALUE, Float.class));
    assertTrue(fitsInto(Float.MAX_VALUE, Float.class));
    assertTrue(fitsInto(Float.MIN_VALUE, Float.class));
    assertTrue(fitsInto(Long.MAX_VALUE, Float.class));
    assertTrue(fitsInto(Long.MIN_VALUE, Float.class));
    assertTrue(fitsInto(Integer.MAX_VALUE, Float.class));
    assertTrue(fitsInto(Integer.MIN_VALUE, Float.class));
    assertTrue(fitsInto(Short.MAX_VALUE, Float.class));
    assertTrue(fitsInto(Short.MIN_VALUE, Float.class));
    assertTrue(fitsInto(Byte.MAX_VALUE, Float.class));
    assertTrue(fitsInto(Byte.MIN_VALUE, Float.class));
    assertTrue(fitsInto(3.00000D, Float.class));
    assertTrue(fitsInto(3.00001D, Float.class));
    assertTrue(fitsInto(3.00001D, Float.class));
  }

  @Test
  public void fitsInto03() {
    assertFalse(fitsInto(Double.MAX_VALUE, Long.class));
    assertFalse(fitsInto(Double.MIN_VALUE, Long.class));
    assertFalse(fitsInto(Float.MAX_VALUE, Long.class));
    assertFalse(fitsInto(Float.MIN_VALUE, Long.class));
    assertTrue(fitsInto(Double.valueOf(Long.MAX_VALUE), Long.class));
    assertTrue(fitsInto(Double.valueOf(Long.MIN_VALUE), Long.class));
    assertTrue(fitsInto(Float.valueOf(Long.MAX_VALUE), Long.class));
    assertTrue(fitsInto(Float.valueOf(Long.MIN_VALUE), Long.class));
    assertTrue(fitsInto(Long.MAX_VALUE, Long.class));
    assertTrue(fitsInto(Long.MIN_VALUE, Long.class));
    assertTrue(fitsInto(Integer.MAX_VALUE, Long.class));
    assertTrue(fitsInto(Integer.MIN_VALUE, Long.class));
    assertTrue(fitsInto(Short.MAX_VALUE, Long.class));
    assertTrue(fitsInto(Short.MIN_VALUE, Long.class));
    assertTrue(fitsInto(Byte.MAX_VALUE, Long.class));
    assertTrue(fitsInto(Byte.MIN_VALUE, Long.class));
    assertTrue(fitsInto(3.0000000D, Long.class));
    assertFalse(fitsInto(3.0000001D, Long.class));
  }

  @Test
  public void fitsInto05() {
    assertFalse(fitsInto(Double.MAX_VALUE, Integer.class));
    assertFalse(fitsInto(Double.MIN_VALUE, Integer.class));
    assertFalse(fitsInto(Float.MAX_VALUE, Integer.class));
    assertFalse(fitsInto(Float.MIN_VALUE, Integer.class));
    assertFalse(fitsInto(Long.MAX_VALUE, Integer.class));
    assertFalse(fitsInto(Long.MIN_VALUE, Integer.class));
    assertTrue(fitsInto(Double.valueOf(Integer.MAX_VALUE), Integer.class));
    assertTrue(fitsInto(Double.valueOf(Integer.MIN_VALUE), Integer.class));
    assertFalse(fitsInto(Float.valueOf(Integer.MAX_VALUE), Integer.class));
    assertTrue(fitsInto(Float.valueOf(Integer.MIN_VALUE), Integer.class));
    assertTrue(fitsInto(Long.valueOf(Integer.MAX_VALUE), Integer.class));
    assertTrue(fitsInto(Long.valueOf(Integer.MIN_VALUE), Integer.class));
    assertTrue(fitsInto(Integer.MAX_VALUE, Integer.class));
    assertTrue(fitsInto(Integer.MIN_VALUE, Integer.class));
    assertTrue(fitsInto(Short.MAX_VALUE, Integer.class));
    assertTrue(fitsInto(Short.MIN_VALUE, Integer.class));
    assertTrue(fitsInto(Byte.MAX_VALUE, Integer.class));
    assertTrue(fitsInto(Byte.MIN_VALUE, Integer.class));
    assertTrue(fitsInto(3.0000000D, Integer.class));
    assertFalse(fitsInto(3.0000001D, Integer.class));
    assertTrue(fitsInto(3.00000D, Integer.class));
    assertFalse(fitsInto(3.00001F, Integer.class));
  }

  @Test
  public void fitsInto06() {
    assertFalse(fitsInto(Double.MAX_VALUE, Short.class));
    assertFalse(fitsInto(Double.MIN_VALUE, Short.class));
    assertFalse(fitsInto(Float.MAX_VALUE, Short.class));
    assertFalse(fitsInto(Float.MIN_VALUE, Short.class));
    assertFalse(fitsInto(Long.MAX_VALUE, Short.class));
    assertFalse(fitsInto(Long.MIN_VALUE, Short.class));
    assertFalse(fitsInto(Integer.MAX_VALUE, Short.class));
    assertFalse(fitsInto(Integer.MIN_VALUE, Short.class));
    assertTrue(fitsInto(Double.valueOf(Short.MAX_VALUE), Short.class));
    assertTrue(fitsInto(Double.valueOf(Short.MIN_VALUE), Short.class));
    assertTrue(fitsInto(Float.valueOf(Short.MAX_VALUE), Short.class));
    assertTrue(fitsInto(Float.valueOf(Short.MIN_VALUE), Short.class));
    assertTrue(fitsInto(Long.valueOf(Short.MAX_VALUE), Short.class));
    assertTrue(fitsInto(Long.valueOf(Short.MIN_VALUE), Short.class));
    assertTrue(fitsInto(Integer.valueOf(Short.MAX_VALUE), Short.class));
    assertTrue(fitsInto(Integer.valueOf(Short.MIN_VALUE), Short.class));
    assertTrue(fitsInto(Short.MAX_VALUE, Short.class));
    assertTrue(fitsInto(Short.MIN_VALUE, Short.class));
    assertTrue(fitsInto(Byte.MAX_VALUE, Short.class));
    assertTrue(fitsInto(Byte.MIN_VALUE, Short.class));
    assertTrue(fitsInto(3.0000000D, Short.class));
    assertFalse(fitsInto(3.0000001D, Short.class));
    assertTrue(fitsInto(3.00000D, Short.class));
    assertFalse(fitsInto(3.00001F, Short.class));
  }

  @Test
  public void fitsInto07() {
    assertFalse(fitsInto(Double.MAX_VALUE, Byte.class));
    assertFalse(fitsInto(Double.MIN_VALUE, Byte.class));
    assertFalse(fitsInto(Float.MAX_VALUE, Byte.class));
    assertFalse(fitsInto(Float.MIN_VALUE, Byte.class));
    assertFalse(fitsInto(Long.MAX_VALUE, Byte.class));
    assertFalse(fitsInto(Long.MIN_VALUE, Byte.class));
    assertFalse(fitsInto(Integer.MAX_VALUE, Byte.class));
    assertFalse(fitsInto(Integer.MIN_VALUE, Byte.class));
    assertFalse(fitsInto(Short.MAX_VALUE, Byte.class));
    assertFalse(fitsInto(Short.MIN_VALUE, Byte.class));
    assertTrue(fitsInto(Double.valueOf(Byte.MAX_VALUE), Byte.class));
    assertTrue(fitsInto(Double.valueOf(Byte.MIN_VALUE), Byte.class));
    assertTrue(fitsInto(Float.valueOf(Byte.MAX_VALUE), Byte.class));
    assertTrue(fitsInto(Float.valueOf(Byte.MIN_VALUE), Byte.class));
    assertTrue(fitsInto(Long.valueOf(Byte.MAX_VALUE), Byte.class));
    assertTrue(fitsInto(Long.valueOf(Byte.MIN_VALUE), Byte.class));
    assertTrue(fitsInto(Integer.valueOf(Byte.MAX_VALUE), Byte.class));
    assertTrue(fitsInto(Integer.valueOf(Byte.MIN_VALUE), Byte.class));
    assertTrue(fitsInto(Short.valueOf(Byte.MAX_VALUE), Byte.class));
    assertTrue(fitsInto(Short.valueOf(Byte.MIN_VALUE), Byte.class));
    assertTrue(fitsInto(Byte.MAX_VALUE, Byte.class));
    assertTrue(fitsInto(Byte.MIN_VALUE, Byte.class));
    assertTrue(fitsInto(3.0000000D, Byte.class));
    assertFalse(fitsInto(3.0000001D, Byte.class));
    assertTrue(fitsInto(3.00000D, Byte.class));
    assertTrue(fitsInto(3L, Byte.class));
    assertFalse(fitsInto(3.00001F, Byte.class));
    assertFalse(fitsInto(400, Byte.class));
    assertFalse(fitsInto(300L, Byte.class));
  }

  @Test
  public void fitsInto08() {
    assertFalse(fitsInto(Double.valueOf("3.000000000000001"), Integer.class));
  }

  @Test // Ouch - here we go past the precision of double.
  public void fitsInto09() {
    assertTrue(fitsInto(Double.valueOf("3.0000000000000001"), Integer.class));
    assertTrue(fitsInto(3.0000000000000001D, Integer.class));
  }

  @Test // Need to use BigDecimal to be safe
  public void fitsInto10() {
    assertFalse(fitsInto(new BigDecimal("3.0000000000000001"), Integer.class));
  }

  @Test(expected = TypeConversionException.class)
  public void fitsInto11() {
    fitsInto(300345, LongAdder.class);
  }

  @Test(expected = TypeConversionException.class)
  public void stringFitsInto00() {
    fitsInto("300345", LongAdder.class);
  }

  @Test
  public void stringFitsInto01() {
    assertTrue(fitsInto("3.0000000", Byte.class));
    assertFalse(fitsInto("3.0000001", Byte.class));
    assertTrue(fitsInto("3.0000001", BigDecimal.class));
    assertTrue(fitsInto("3.0000001", Double.class));
    assertTrue(fitsInto("3.000000000000000", BigInteger.class));
    assertTrue(fitsInto("3", AtomicLong.class));
    assertFalse(fitsInto("", AtomicLong.class));
    assertFalse(fitsInto((String) null, BigDecimal.class));
  }

  @Test(expected = TypeConversionException.class)
  public void convert00() {
    convert(300345, Byte.class);
  }

  @Test
  public void convert02() {
    byte b = convert((short) 123, Byte.class);
    assertEquals((byte) 123, b);
  }

  @Test
  public void convert03() {
    byte b = convert(123F, Byte.class);
    assertEquals((byte) 123, b);
  }

  @Test(expected = TypeConversionException.class)
  public void convert04() {
    convert(123.02F, Byte.class);
  }

  @Test
  public void convert05() {
    Float f0 = 9.0F;
    Float f1 = convert(f0, Float.class);
    assertSame(f0, f1);
  }

  @Test(expected = TypeConversionException.class)
  public void convert06() {
    convert(.3D, Short.class);
  }

  @Test
  public void convert07() {
    short s = convert(3D, Short.class);
    assertEquals((short) 3, s);
  }

  @Test(expected = TypeConversionException.class)
  public void convert08() {
    convert(Integer.MIN_VALUE, Short.class);
  }

  @Test
  public void convert09() {
    int i = convert(0, Integer.class);
    assertEquals(0, i);
  }

  @Test
  public void convert10() {
    byte b = convert(new AtomicLong(123L), Byte.class);
    assertEquals((byte) 123, b);
  }

  @Test
  public void convert11() {
    byte b = convert(new AtomicInteger(123), Byte.class);
    assertEquals((byte) 123, b);
  }

  @Test
  public void convert12() {
    byte b = convert(new AtomicInteger(123), Byte.class);
    assertEquals((byte) 123, b);
  }

  @Test
  public void convert13() {
    byte b = convert(new AtomicInteger(-123), Byte.class);
    assertEquals((byte) -123, b);
  }

  @Test(expected = TypeConversionException.class)
  public void convert14() {
    convert(new AtomicInteger(1000), Byte.class);
  }

  @Test(expected = TypeConversionException.class)
  public void convert15() {
    convert(new AtomicLong(1000), Byte.class);
  }

  @Test(expected = TypeConversionException.class)
  public void convert16() {
    convert(1000, Byte.class);
  }

  @Test(expected = TypeConversionException.class)
  public void convert17() {
    convert(-1000, Byte.class);
  }

  @Test(expected = TypeConversionException.class)
  public void convert18() {
    convert(Double.MAX_VALUE, Float.class);
  }

  @Test
  public void convert19() {
    Float f = convert(Float.MAX_VALUE, Float.class);
    assertTrue(f.equals(Float.MAX_VALUE));
  }

  @Test(expected = TypeConversionException.class)
  public void convert20() {
    convert(Double.MIN_VALUE, Float.class);
  }

  @Test
  public void convert21() {
    Float f = convert(Float.MIN_VALUE, Float.class);
    assertTrue(f.equals(Float.MIN_VALUE));
  }

  @Test(expected = TypeConversionException.class) // OUCH, FLOATING POINT STUFF
  public void convert22() {
    String s = Integer.toString(Integer.MAX_VALUE);
    Integer i = convert(Float.valueOf(s), Integer.class);
  }

  @Test(expected = TypeConversionException.class)
  public void convert23() {
    convert(33.78F, LongAdder.class);
  }

  @Test
  public void convert24() {
    assertEquals(Float.valueOf(33.78F), convert(33.78F, Float.class));
  }

  @Test
  public void convert25() {
    Short s = convert(null, Short.class);
    assertNull(s);
  }

  @Test(expected = TypeConversionException.class)
  public void convert26() { // not a Number
    Short s = convert(223D, (Class<Short>) bruteCast(OutputStream.class));
  }

  @Test(expected = TypeConversionException.class)
  public void convert27() { // not supported
    LongAdder la = convert(223D, LongAdder.class);
  }

  @Test(expected = TypeConversionException.class)
  public void parse01() {
    parse("300345", Byte.class);
  }

  @Test
  public void parse02() {
    byte b = parse("123", Byte.class);
    assertEquals((byte) 123, b);
  }

  @Test(expected = TypeConversionException.class)
  public void parse04() {
    parse("123.02", Byte.class);
  }

  @Test
  public void parse05() {
    Float f1 = parse("0.9", Float.class);
    assertEquals((Float) .9F, f1);
  }

  @Test(expected = TypeConversionException.class)
  public void parse06() {
    parse(".3", Short.class);
  }

  @Test
  public void parse07() {
    short s = parse("3", Short.class);
    assertEquals((short) 3, s);
  }

  @Test(expected = TypeConversionException.class)
  public void parse08() {
    parse(String.valueOf(Integer.MIN_VALUE), Short.class);
  }

  @Test
  public void parse09() {
    int i = parse("0", Integer.class);
    assertEquals(0, i);
  }

  @Test
  public void parse10() {
    BigInteger i = parse("42", BigInteger.class);
    assertEquals(42, i.intValueExact());
  }

  @Test
  public void parse11() {
    BigDecimal i = parse("42.337", BigDecimal.class);
    assertEquals(42.337F, i.floatValue(), 0F);
  }

  @Test
  public void parse12() {
    Integer i = parse("42.0000", Integer.class);
    assertEquals(42, (int) i);
  }

  @Test
  public void parse13() {
    Integer i = convert(42.000F, Integer.class);
    assertEquals(42, (int) i);
  }

  @Test(expected = TypeConversionException.class)
  public void parse14() { // not a Number
    parse("223", (Class<Short>) bruteCast(OutputStream.class));
  }

  @Test(expected = TypeConversionException.class)
  public void parse15() { // not supported
    parse("223", LongAdder.class);
  }

  @Test(expected = TypeConversionException.class)
  public void parseInt00() {
    parseInt("  22");
  }

  @Test
  public void parseInt01() {
    assertEquals(-22, parseInt("-00000000000022"));
    assertEquals(+22, parseInt("+00000000000022"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseInt02() {
    parseInt("-200000000000022");
  }

  @Test(expected = TypeConversionException.class)
  public void parseInt03() {
    parseInt(null);
  }

  @Test(expected = TypeConversionException.class)
  public void parseInt04() {
    parseInt("");
  }

  @Test(expected = TypeConversionException.class)
  public void parseInt05() {
    parseInt("42.6");
  }

  @Test(expected = TypeConversionException.class)
  public void parseInt06() {
    parseInt("12foo");
  }

  @Test
  public void parseInt07() {
    assertEquals(12, parseInt("12E0"));
  }

  @Test
  public void parseInt08() {
    assertEquals(12, parseInt("1.2E1"));
  }

  @Test
  public void parseInt09() {
    assertEquals(12, parseInt(".12E2"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseInt10() {
    assertEquals(12, parseInt("1.2E-2"));
  }

  @Test
  public void parseAny00() {
    assertEquals(34, parse("34", AtomicInteger.class).get());
    assertEquals(34L, parse("34", AtomicLong.class).get());
  }

  @Test(expected = TypeConversionException.class)
  public void parseAny01() {
    parse("34", LongAdder.class);
  }

  @Test
  public void toInt01() {
    assertEquals(OptionalInt.of(-22), toInt("-00000000000022"));
    assertEquals(OptionalInt.of(+22), toInt("+00000000000022"));
  }

  @Test
  public void toInt02() {
    assertEquals(OptionalInt.empty(), toInt("-200000000000022"));
  }

  @Test
  public void toInt03() {
    assertEquals(OptionalInt.empty(), toInt(null));
  }

  @Test
  public void toInt04() {
    assertEquals(OptionalInt.empty(), toInt(""));
  }

  @Test
  public void toInt05() {
    assertEquals(OptionalInt.empty(), toInt("42.6"));
  }

  @Test
  public void toInt06() {
    assertEquals(OptionalInt.empty(), toInt("12foo"));
  }

  @Test
  public void isInt01() {
    assertTrue(isInt("42"));
    assertTrue(isInt("-42"));
  }

  @Test
  public void isInt02() {
    assertFalse(isInt(null));
  }

  @Test
  public void isInt03() {
    assertFalse(isInt(""));
  }

  @Test
  public void isInt04() {
    assertFalse(isInt("1.3"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseDouble00() {
    parseDouble("  22");
  }

  @Test
  public void parseDouble01() {
    assertEquals(-22.3D, parseDouble("-00000000000022.3"), 0D);
    assertEquals(+22.3D, parseDouble("+00000000000022.3"), 0D);
    assertFalse(Double.isNaN(parseDouble("1.0E292")));
    assertFalse(Double.isInfinite(parseDouble("1.0E292")));
    assertFalse(Double.isInfinite(parseDouble("1.0E292")));
    assertFalse(Double.isInfinite(parseDouble("1.0E-44")));
  }

  @Test(expected = TypeConversionException.class)
  public void parseDouble02() {
    parseDouble("1"
        + ".67E299999999999999999999999999999999999999999999999999");
  }

  @Test(expected = TypeConversionException.class)
  public void parseDouble03() {
    parseDouble(null);
  }

  @Test(expected = TypeConversionException.class)
  public void parseDouble04() {
    parseDouble("");
  }

  @Test(expected = TypeConversionException.class)
  public void parseDouble05() {
    parseDouble("12foo");
  }

  @Test(expected = TypeConversionException.class)
  public void parseDouble06() {
    BigDecimal bd = MAX_DOUBLE_BD.add(BigDecimal.TEN);
    parseDouble(bd.toString());
  }

  @Test(expected = TypeConversionException.class)
  public void parseDouble07() {
    BigDecimal bd = MIN_DOUBLE_BD.divide(BigDecimal.TEN);
    parseDouble(bd.toString());
  }

  @Test
  public void toDouble00() {
    assertEquals(OptionalDouble.of(-22), toDouble("-00000000000022"));
    assertEquals(OptionalDouble.of(+22), toDouble("+00000000000022"));
  }

  @Test
  public void toDouble01() {
    assertEquals(OptionalDouble.empty(), toDouble("3.0D"));
  }

  @Test
  public void toDouble02() {
    assertEquals(OptionalDouble.empty(), toDouble(null));
  }

  @Test
  public void toDouble03() {
    assertEquals(OptionalDouble.empty(), toDouble(""));
  }

  @Test
  public void toDouble04() {
    assertEquals(OptionalDouble.empty(), toDouble("12foo"));
  }

  @Test
  public void isDouble00() {
    assertTrue(isDouble("42"));
    assertTrue(isDouble("-42.8989"));
  }

  @Test
  public void isDouble01() {
    assertFalse(isDouble(null));
  }

  @Test
  public void isDouble02() {
    assertFalse(isDouble(""));
  }

  @Test
  public void isDouble03() {
    assertFalse(isDouble("1.3D"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseFloat00() {
    parseFloat("  22");
  }

  @Test
  public void parseFloat01() {
    assertEquals(-22F, parseFloat("-00000000000022"), 0F);
    assertEquals(+22.5F, parseFloat("+00000000000022.5"), 0F);
  }

  @Test(expected = TypeConversionException.class)
  public void parseFloat02() {
    parseFloat("-3.6F");
  }

  @Test(expected = TypeConversionException.class)
  public void parseFloat03() {
    parseFloat(null);
  }

  @Test(expected = TypeConversionException.class)
  public void parseFloat04() {
    parseFloat("");
  }

  @Test(expected = TypeConversionException.class)
  public void parseFloat05() {
    parseFloat("12foo");
  }

  @Test(expected = TypeConversionException.class)
  public void parseFloat06() {
    parseFloat(MAX_DOUBLE_BD.toString());
  }

  @Test(expected = TypeConversionException.class)
  public void parseFloat07() {
    parseFloat(MIN_DOUBLE_BD.toString());
  }

  @Test
  public void toFloat01() {
    assertEquals(OptionalDouble.of(-1.3E1), toFloat("-1.3E1"));
  }

  @Test
  public void toFloat02() {
    assertEquals(OptionalDouble.empty(), toFloat(null));
  }

  @Test
  public void toFloat03() {
    assertEquals(OptionalDouble.empty(), toFloat(""));
  }

  @Test
  public void toFloat04() {
    assertEquals(OptionalDouble.empty(), toFloat("42.+6"));
  }

  @Test
  public void toFloat05() {
    assertEquals(OptionalDouble.empty(), toFloat("12foo"));
  }

  @Test
  public void isFloat01() {
    assertTrue(isFloat("42"));
    assertTrue(isFloat("-42"));
  }

  @Test
  public void isFloat02() {
    assertFalse(isFloat(null));
  }

  @Test
  public void isFloat03() {
    assertFalse(isFloat(""));
  }

  @Test
  public void isFloat04() {
    assertTrue(isFloat("1.3"));
  }

  @Test
  public void isFloat05() {
    assertFalse(isFloat(MAX_DOUBLE_BD.toString()));
  }

  @Test
  public void isFloat06() {
    assertFalse(isFloat(MIN_DOUBLE_BD.toString()));
  }

  @Test(expected = TypeConversionException.class)
  public void parseLong00() {
    parseLong("  22");
  }

  @Test
  public void parseLong01() {
    assertEquals(-22, parseLong("-00000000000022"));
    assertEquals(+22, parseLong("+00000000000022"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseLong02() {
    parseLong("-9999999999999999999999999999992");
  }

  @Test(expected = TypeConversionException.class)
  public void parseLong03() {
    parseLong(null);
  }

  @Test(expected = TypeConversionException.class)
  public void parseLong04() {
    parseLong("");
  }

  @Test(expected = TypeConversionException.class)
  public void parseLong05() {
    parseLong("42.6");
  }

  @Test(expected = TypeConversionException.class)
  public void parseLong06() {
    parseLong("12foo");
  }

  @Test
  public void toLong01() {
    assertEquals(OptionalLong.of(-22), toLong("-00000000000022"));
    assertEquals(OptionalLong.of(+22), toLong("+00000000000022"));
  }

  @Test
  public void toLong02() {
    assertEquals(OptionalLong.empty(), toLong(
        "99999999999999999999999999"));
  }

  @Test
  public void toLong03() {
    assertEquals(OptionalLong.empty(), toLong(null));
  }

  @Test
  public void toLong04() {
    assertEquals(OptionalLong.empty(), toLong(""));
  }

  @Test
  public void toLong05() {
    assertEquals(OptionalLong.empty(), toLong("42.6"));
  }

  @Test
  public void toLong06() {
    assertEquals(OptionalLong.empty(), toLong("12foo"));
  }

  @Test
  public void isLong01() {
    assertTrue(isLong("42"));
    assertTrue(isLong("-42"));
  }

  @Test
  public void isLong02() {
    assertFalse(isLong(null));
  }

  @Test
  public void isLong03() {
    assertFalse(isLong(""));
  }

  @Test
  public void isLong04() {
    assertFalse(isLong("1.3"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseShort00() {
    parseShort("  22");
  }

  @Test
  public void parseShort01() {
    assertEquals(-22, parseShort("-00000000000022"));
    assertEquals(+22, parseShort("+00000000000022"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseShort02() {
    parseShort("-200000000000022");
  }

  @Test(expected = TypeConversionException.class)
  public void parseShort03() {
    parseShort(null);
  }

  @Test(expected = TypeConversionException.class)
  public void parseShort04() {
    parseShort("");
  }

  @Test(expected = TypeConversionException.class)
  public void parseShort05() {
    parseShort("42.6");
  }

  @Test(expected = TypeConversionException.class)
  public void parseShort06() {
    parseShort("12foo");
  }

  @Test
  public void toShort01() {
    assertEquals(OptionalInt.of(-22), toShort("-00000000000022"));
    assertEquals(OptionalInt.of(+22), toShort("+00000000000022"));
  }

  @Test
  public void toShort02() {
    assertEquals(OptionalInt.empty(), toShort("-200000000000022"));
  }

  @Test
  public void toShort03() {
    assertEquals(OptionalInt.empty(), toShort(null));
  }

  @Test
  public void toShort04() {
    assertEquals(OptionalInt.empty(), toShort(""));
  }

  @Test
  public void toShort05() {
    assertEquals(OptionalInt.empty(), toShort("42.6"));
  }

  @Test
  public void toShort06() {
    assertEquals(OptionalInt.empty(), toShort("12foo"));
  }

  @Test
  public void isShort01() {
    assertTrue(isShort("42"));
    assertTrue(isShort("-42"));
  }

  @Test
  public void isShort02() {
    assertFalse(isShort(null));
  }

  @Test
  public void isShort03() {
    assertFalse(isShort(""));
  }

  @Test
  public void isShort04() {
    assertFalse(isShort("1.3"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseByte00() {
    parseByte("  22");
  }

  @Test
  public void parseByte01() {
    assertEquals(-22, parseByte("-00000000000022"));
    assertEquals(+22, parseByte("+00000000000022"));
  }

  @Test(expected = TypeConversionException.class)
  public void parseByte02() {
    parseByte("-200000000000022");
  }

  @Test(expected = TypeConversionException.class)
  public void parseByte03() {
    parseByte(null);
  }

  @Test(expected = TypeConversionException.class)
  public void parseByte04() {
    parseByte("");
  }

  @Test(expected = TypeConversionException.class)
  public void parseByte05() {
    parseByte("42.6");
  }

  @Test(expected = TypeConversionException.class)
  public void parseByte06() {
    parseByte("12foo");
  }

  @Test
  public void toByte01() {
    assertEquals(OptionalInt.of(-22), toByte("-00000000000022"));
    assertEquals(OptionalInt.of(+22), toByte("+00000000000022"));
  }

  @Test
  public void toByte02() {
    assertEquals(OptionalInt.empty(), toByte("-200000000000022"));
  }

  @Test
  public void toByte03() {
    assertEquals(OptionalInt.empty(), toByte(null));
  }

  @Test
  public void toByte04() {
    assertEquals(OptionalInt.empty(), toByte(""));
  }

  @Test
  public void toByte05() {
    assertEquals(OptionalInt.empty(), toByte("42.6"));
  }

  @Test
  public void toByte06() {
    assertEquals(OptionalInt.empty(), toByte("12foo"));
  }

  @Test
  public void isByte01() {
    assertTrue(isByte("42"));
    assertTrue(isByte("-42"));
  }

  @Test
  public void isByte02() {
    assertFalse(isByte(null));
  }

  @Test
  public void isByte03() {
    assertFalse(isByte(""));
  }

  @Test
  public void isByte04() {
    assertFalse(isByte("1.3"));
  }

  @Test
  public void parseBigInteger00() {
    assertEquals(new BigInteger("28"), parseBigInteger("28"));
  }

  @Test
  public void parseBigInteger01() {
    assertEquals(BigInteger.valueOf(Long.MIN_VALUE),
        parseBigInteger(Long.toString(Long.MIN_VALUE)));
  }

  @Test(expected = TypeConversionException.class)
  public void parseBigInteger02() {
    parseBigInteger("28.000000000000078");
  }

  @Test(expected = TypeConversionException.class)
  public void parseBigInteger03() {
    parseBigInteger("28E");
  }

  @Test
  public void parseBigInteger04() {
    assertEquals(2800, parseBigInteger("28E2").intValue());
  }

  @Test
  public void isBigInteger00() {
    assertTrue(isBigInteger("-28"));
  }

  @Test
  public void isBigInteger01() {
    assertFalse(isBigInteger("-28.000000000000078"));
    assertFalse(isBigInteger(""));
    assertFalse(isBigInteger(null));
  }

  @Test
  public void toBigInteger00() {
    assertFalse(toBigInteger("-28").isEmpty());
  }

  @Test
  public void toBigInteger01() {
    assertTrue(toBigInteger("-28.000000000000078").isEmpty());
    assertTrue(toBigInteger("").isEmpty());
    assertTrue(toBigInteger(null).isEmpty());
  }

  @Test
  public void parseBigDecimal00() {
    assertEquals(9.0, parse("9.0", BigDecimal.class).doubleValue(), 0D);
  }

  @Test
  public void parseBigDecimal01() {
    assertEquals(0.009, parseBigDecimal("9.0E-3").doubleValue(), 0D);
  }

  @Test(expected = TypeConversionException.class)
  public void parseBigDecimal02() {
    parseBigDecimal("28.0000000000.00078");
  }

  @Test
  public void isBigDecimal00() {
    assertTrue(isBigDecimal("9.0E-3"));
    assertTrue(isBigDecimal("-9.0E-3"));
    assertFalse(isBigDecimal("-9.xE-3"));
    assertFalse(isBigDecimal(null));
    assertFalse(isBigDecimal(""));
  }

  @Test
  public void toBigDecimal00() {
    assertTrue(toBigDecimal("9.0E-3").get().doubleValue() == .009);
    assertTrue(toBigDecimal("-9.0E-3").get().doubleValue() == -.009);
    assertTrue(toBigDecimal("-9.xE-3").isEmpty());
    assertTrue(toBigDecimal("").isEmpty());
    assertTrue(toBigDecimal((String) null).isEmpty());
  }

  @Test
  public void toBigDecimal01() {
    assertEquals((byte) 78, toBigDecimal((byte) 78).byteValueExact());
    assertEquals(78, toBigDecimal(78).intValueExact());
    assertEquals(78L, toBigDecimal(78L).longValueExact());
    assertEquals(78L, toBigDecimal(new AtomicLong(78L)).longValueExact());
    assertEquals(78, toBigDecimal(new AtomicInteger(78)).intValueExact());
    assertEquals(-78, toBigDecimal(new BigInteger("-78")).intValueExact());
    assertEquals(78.0, toBigDecimal(78D).doubleValue(), 0D);
    assertEquals(78.0F, toBigDecimal(78F).floatValue(), 0D);
    assertEquals(78.0, toBigDecimal(new BigDecimal("78")).doubleValue(), 0D);
    assertEquals((short) 78, toBigDecimal((short) 78).shortValueExact());
  }

  @Test(expected = TypeConversionException.class)
  public void toBigDecimal02() {
    toBigDecimal(new LongAdder());
  }

  @Test
  public void isIntegral00() {
    assertTrue(isIntegral(7));
    assertTrue(isIntegral(Integer.class));
    assertTrue(isIntegral(new BigInteger("56")));
    assertTrue(isIntegral(new AtomicLong(56)));
    assertFalse(isIntegral(int.class));
    assertFalse(isIntegral(7.0));
    assertFalse(isIntegral(OutputStream.class));
    assertFalse(isIntegral(new BigDecimal("6")));
  }

  @Test
  public void isWrapper00() {
    assertTrue(isWrapper(Byte.class));
    assertTrue(isWrapper(Short.class));
    assertTrue(isWrapper(Integer.class));
    assertTrue(isWrapper(Long.class));
    assertTrue(isWrapper(Float.class));
    assertTrue(isWrapper(Double.class));
    assertFalse(isWrapper(int.class));
    assertFalse(isWrapper(double.class));
    assertFalse(isWrapper(AtomicInteger.class));
    assertFalse(isWrapper(AtomicLong.class));
    assertFalse(isWrapper(BigInteger.class));
    assertFalse(isWrapper(BigDecimal.class));
    assertFalse(isWrapper(OutputStream.class));
    assertTrue(isWrapper(-11));
    assertFalse(isWrapper(new AtomicInteger(-11)));
  }

  @Test
  public void isRound00() {
    assertTrue(isRound(0));
    assertTrue(isRound(0.00000000));
    assertTrue(isRound(-0.00000000));
    assertTrue(isRound(-0.00000000F));
    assertTrue(isRound(7D));
    assertTrue(isRound(7F));
    assertTrue(isRound(7.000D));
    assertTrue(isRound(7.000F));
    assertTrue(isRound(7.000F));
    assertTrue(isRound(new BigDecimal("-7.0000000")));
    assertFalse(isRound(7.0009D));
    assertFalse(isRound(7.0009F));
    assertFalse(isRound(new BigDecimal("-7.0000008")));
    assertFalse(isRound(new BigDecimal("2E-2")));
    assertFalse(isRound(new BigDecimal(".2E-2")));
    assertTrue(isRound(new BigDecimal(".2E+1")));
    assertTrue(isRound(new BigDecimal(".2E+2")));
    assertTrue(isRound(new BigDecimal(".02E+2")));
  }

  @Test // pretty mundane value ("100.0") surfaced later as a bug
  public void isRound01() {
    assertTrue(isRound(new BigDecimal("100.0")));
  }

  @Test
  public void isRound02() {
    assertTrue(isRound(new BigDecimal("-100.0")));
  }

  @Test
  public void isRound03() {
    assertTrue(isRound(new BigDecimal("-107.0")));
  }

  @Test
  public void isRound04() {
    assertTrue(isRound(new BigDecimal("-100.000")));
  }

  @Test
  public void isRound05() {
    assertFalse(isRound(new BigDecimal("-100.70")));
  }

  @Test
  public void isRound06() {
    assertFalse(isRound(new BigDecimal("-100.700")));
  }

  @Test
  public void isRound07() {
    assertFalse(isRound(new BigDecimal("-100.7000")));
  }

  @Test
  public void isRound08() {
    assertFalse(isRound(new BigDecimal("-100.700006")));
  }

  @Test
  public void isRound09() {
    assertFalse(isRound(new BigDecimal(".700006")));
  }

}
