package nl.naturalis.common;

import org.junit.Test;

import java.time.Month;

import static org.junit.Assert.assertEquals;

public class MorphToNumberTest {

  @Test
  public void test00() {
    assertEquals(true, MorphToNumber.morph('1', boolean.class));
    assertEquals(false, MorphToNumber.morph('0', Boolean.class));
    assertEquals(false, MorphToNumber.morph("0", boolean.class));
    assertEquals(true, MorphToNumber.morph("true", Boolean.class));
    assertEquals(false, MorphToNumber.morph(0L, boolean.class));
    assertEquals(true, MorphToNumber.morph((byte) 1, Boolean.class));
  }

  @Test
  public void test01() {
    assertEquals('A', MorphToNumber.morph("A", char.class));
    assertEquals('8', MorphToNumber.morph(8, Character.class));
    assertEquals('1', MorphToNumber.morph(true, Character.class));
    assertEquals('0',
        MorphToNumber.morph(Boolean.FALSE, Character.class));
  }

  @Test(expected = TypeConversionException.class)
  public void test02() {
    MorphToNumber.morph("Hello, World", char.class);
  }

  @Test(expected = TypeConversionException.class)
  public void test03() {
    MorphToNumber.morph((short) 42, Character.class);
  }

  @Test
  public void test04() {
    assertEquals((short) 42, MorphToNumber.morph(42L, short.class));
    assertEquals(42L, MorphToNumber.morph("42", Long.class));
    assertEquals(7, MorphToNumber.morph('7', int.class));
    assertEquals(2, MorphToNumber.morph(Month.MARCH, int.class));
  }

  @Test(expected = TypeConversionException.class)
  public void test07() {
    MorphToNumber.morph("Z", byte.class);
  }

  @Test(expected = TypeConversionException.class)
  public void test08() {
    MorphToNumber.morph('Z', Double.class);
  }

}
