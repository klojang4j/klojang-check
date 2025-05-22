package org.klojang.check.x;

import org.junit.Test;
import org.klojang.check.Check;
import org.klojang.check.CorruptCheckException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.klojang.check.CommonChecks.*;

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
    // one __past__ Integer.MAX_VALUE:
    BigInteger bi = new BigInteger("2147483648");
    //System.out.println(">>>>>>>>>>> " + bi.bitLength());
    assertEquals(32, bi.bitLength());
  }

  @Test
  public void test21() {
    //System.out.println(">>>>>>>>>>> " + Short.MAX_VALUE);
    // one __past__ Short.MAX_VALUE:
    BigInteger bi = new BigInteger("32768");
    //System.out.println(">>>>>>>>>>> " + bi.bitLength());
    assertEquals(16, bi.bitLength());
  }

  @Test
  public void test22() {
    //System.out.println(">>>>>>>>>>> " + Integer.MIN_VALUE);
    // one __below__ Integer.MIN_VALUE
    BigInteger bi = new BigInteger("-2147483649");
    //System.out.println(">>>>>>>>>>> " + bi.bitLength());
    assertEquals(32, bi.bitLength());
  }

  @Test
  public void test23() {
    // just to see what that does
    BigInteger bi = new BigInteger(
        "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
    //System.out.println(">>>>>>>>>>> " + bi.bitLength());
  }

  @Test
  public void plainInt00() {
    Check.that("23").is(plainInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void plainInt01() {
    Check.that("+23").is(plainInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void plainInt02() {
    Check.that("-23").is(plainInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void plainInt03() {
    Check.that("99999999999999999999").is(plainInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void plainInt04() {
    long l = (int) Integer.MAX_VALUE + 1;
    String s = String.valueOf(l);
    Check.that(s).is(plainInt());
  }

  @Test
  public void plainShort00() {
    Check.that("23").is(plainShort());
  }

  @Test(expected = IllegalArgumentException.class)
  public void plainShort01() {
    Check.that("+23").is(plainShort());
  }

  @Test(expected = IllegalArgumentException.class)
  public void plainShort02() {
    Check.that("-23").is(plainShort());
  }

  @Test(expected = IllegalArgumentException.class)
  public void plainShort03() {
    Check.that("99999999999999999999").is(plainShort());
  }

  @Test(expected = IllegalArgumentException.class)
  public void plainShort04() {
    long l = (short) Short.MAX_VALUE + 1;
    String s = String.valueOf(l);
    Check.that(s).is(plainShort());
  }


}
