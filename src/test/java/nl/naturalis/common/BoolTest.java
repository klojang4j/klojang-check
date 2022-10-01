package nl.naturalis.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

public class BoolTest {

  public BoolTest() {}

  @Test
  public void isConvertible00() {
    assertTrue(Bool.isConvertible("0"));
    assertTrue(Bool.isConvertible("1"));
    assertTrue(Bool.isConvertible(null));
    assertFalse(Bool.isConvertible("2"));
    assertFalse(Bool.isConvertible("00"));
  }

  @Test
  public void fromNumber00() {
    assertTrue(Bool.from(Integer.valueOf(1)));
    assertTrue(Bool.from(Double.valueOf(1)));
    assertTrue(Bool.from(Float.valueOf(1)));
    assertTrue(Bool.from(Integer.valueOf(1)));
    assertTrue(Bool.from(Double.valueOf(1)));
    assertTrue(Bool.from(Byte.valueOf((byte) 1)));

    assertFalse(Bool.from((Integer) null));
    assertFalse(Bool.from(Integer.valueOf(0)));
    assertFalse(Bool.from(Double.valueOf(0)));
    assertFalse(Bool.from(Float.valueOf(0)));
    assertFalse(Bool.from(Integer.valueOf(0)));
    assertFalse(Bool.from(Double.valueOf(0)));
    assertFalse(Bool.from(Byte.valueOf((byte) 0)));
  }

  @Test(expected = TypeConversionException.class)
  public void fromNumber01() {
    Bool.from(Double.valueOf(1.234D));
  }

  @Test
  public void fromString00() {
    assertTrue(Bool.from("True"));
    assertTrue(Bool.from("ON"));
    assertTrue(Bool.from("enabled"));
    assertTrue(Bool.from("1"));
    assertTrue(Bool.from("yEs"));
  }

  @Test(expected = TypeConversionException.class)
  public void fromString01() {
    Bool.from("01");
  }

  @Test
  public void fromString02() {
    assertFalse(Bool.from("FALSE"));
    assertFalse(Bool.from("off"));
    assertFalse(Bool.from("Disabled"));
    assertFalse(Bool.from("0"));
    assertFalse(Bool.from("NO"));
  }

  @Test
  public void from00() {
    assertTrue(Bool.from(1));
    assertTrue(Bool.from(1F));
    assertTrue(Bool.from(1L));
    assertTrue(Bool.from(1.0));
    assertTrue(Bool.from('1'));
    assertTrue(Bool.from((short) 1));
    assertTrue(Bool.from((byte) 1));
  }

  @Test
  public void from01() {
    assertFalse(Bool.from(0));
    assertFalse(Bool.from(0F));
    assertFalse(Bool.from(0L));
    assertFalse(Bool.from(0.0D));
    assertFalse(Bool.from('0'));
    assertFalse(Bool.from((short) 0));
    assertFalse(Bool.from((byte) 0));
  }

  @Test(expected = TypeConversionException.class)
  public void from02() {
    Bool.from(42);
  }

  @Test(expected = TypeConversionException.class)
  public void from03() {
    Bool.from(42L);
  }

  @Test(expected = TypeConversionException.class)
  public void from04() {
    Bool.from(42D);
  }

  @Test(expected = TypeConversionException.class)
  public void from05() {
    Bool.from(42F);
  }

  @Test(expected = TypeConversionException.class)
  public void from06() {
    Bool.from((short) 42);
  }

  @Test(expected = TypeConversionException.class)
  public void from07() {
    Bool.from((byte) 42);
  }

  @Test(expected = TypeConversionException.class)
  public void test06() {
    Bool.from(0.23);
  }

  @Test(expected = TypeConversionException.class)
  public void test07() {
    Bool.from(new ByteArrayOutputStream());
  }

  @Test
  public void test08() {
    assertTrue(Bool.from(true));
    assertFalse(Bool.from(false));
    assertFalse(Bool.from((Object) null));
    assertFalse(Bool.from((Boolean) null));
    assertFalse(Bool.from((String) null));
    assertFalse(Bool.from(Character.valueOf('0')));
  }

  @Test(expected = TypeConversionException.class)
  public void test09() {
    Bool.from(new Object());
  }

  @Test(expected = TypeConversionException.class)
  public void test10() {
    Bool.from(new int[] {0});
  }

}
