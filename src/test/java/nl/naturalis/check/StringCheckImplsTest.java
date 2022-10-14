package nl.naturalis.check;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class StringCheckImplsTest {

  @Test(expected = NumberFormatException.class)
  public void test00() {
    BigInteger bi = new BigInteger("");
  }

  @Test(expected = NumberFormatException.class)
  public void test01() {
    BigInteger bi = new BigInteger(" ");
  }

  @Test(expected = NumberFormatException.class)
  public void test02() {
    BigInteger bi = new BigInteger(" 007");
  }

  @Test
  public void test03() {
    BigInteger bi = new BigInteger("007");
    assertEquals(7, bi.intValueExact());
  }

  @Test
  public void test04() {
    BigInteger bi = new BigInteger("-007");
    assertEquals(-7, bi.intValueExact());
  }

  @Test(expected = NumberFormatException.class)
  public void test05() {
    BigInteger bi = new BigInteger("-007  ");
  }

  @Test(expected = NumberFormatException.class)
  public void test06() {
    BigInteger bi = new BigInteger("- 007");
  }

  @Test(expected = NumberFormatException.class)
  public void test07() {
    BigInteger bi = new BigInteger(" -007");
  }

  @Test(expected = NumberFormatException.class)
  public void test09() {
    BigInteger bi = new BigInteger("4.2E+4");
  }

  @Test
  public void test10() {
    BigDecimal bi = new BigDecimal("4.2E+4");
    assertEquals(42000, bi.intValueExact());
  }

  @Test
  public void test20() {
    //System.out.println(">>>>>>>>>>> " + Integer.MAX_VALUE);
    // one past Integer.MAX_VALUE:
    BigInteger bi = new BigInteger("2147483648");
    //System.out.println(">>>>>>>>>>> " + bi.bitLength());
    assertEquals(32, bi.bitLength());
    // just to see what that does
    bi = new BigInteger(
        "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
    //System.out.println(">>>>>>>>>>> " + bi.bitLength());
  }

  @Test
  public void test21() {
    //System.out.println(">>>>>>>>>>> " + Short.MAX_VALUE);
    // one past Short.MAX_VALUE:
    BigInteger bi = new BigInteger("32768");
    //System.out.println(">>>>>>>>>>> " + bi.bitLength());
    assertEquals(16, bi.bitLength());
  }

}
