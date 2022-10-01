package nl.naturalis.common;

import org.junit.Test;

import java.math.BigInteger;

import static nl.naturalis.common.ToBigIntegerConversion.exec;
import static nl.naturalis.common.ToBigIntegerConversion.isLossless;
import static org.junit.Assert.*;

public class ToBigIntegerConversionTest {

  @Test
  public void isLossless00() {
    assertFalse(isLossless(0.89));
    assertFalse(isLossless(-.3F));
    assertTrue(isLossless(-.0F));
    assertTrue(isLossless(Double.valueOf("0.000")));
    assertTrue(isLossless(333));
  }

  @Test
  public void exec00() {
    assertEquals(BigInteger.valueOf(33L), exec((short) 33));
    assertEquals(BigInteger.valueOf(0), exec(Double.valueOf("0.000")));
  }

  @Test(expected = TypeConversionException.class)
  public void exec01() {
    exec(Double.valueOf("0.0002"));
  }

}