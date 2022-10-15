package nl.naturalis.check;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonChecks.parsableAs;
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

  @Test
  public void test22() {
    System.out.println(">>>>>>>>>>> " + Integer.MIN_VALUE);
    BigInteger bi = new BigInteger("-2147483649");
    System.out.println(">>>>>>>>>>> " + bi.bitLength());
    // assertEquals(32, bi.bitLength());
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

  @Test
  public void parsableAsInt00() {
    Check.that("0").is(parsableAs(), Integer.class);
  }

  @Test
  public void parsableAsInt01() {
    Check.that("-1").is(parsableAs(), Integer.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsInt02() {
    Check.that("   -1").is(parsableAs(), Integer.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsInt03() {
    Check.that("-   1").is(parsableAs(), Integer.class);
  }

  @Test
  public void parsableAsInt04() {
    Check.that("000001").is(parsableAs(), Integer.class);
  }

  @Test
  public void parsableAsInt05() {
    Check.that("3.0000000000000000000").is(parsableAs(), Integer.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsInt06() {
    Check.that("3.00000000000000000006").is(parsableAs(), Integer.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsInt07() {
    Check.that("99999999999999999999999999999999999999999999999999999999999")
        .is(parsableAs(), Integer.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsInt08() {
    Check.that("3.23").is(parsableAs(), Integer.class);
  }

  @Test
  public void parsableAsInt09() {
    Check.that("4.2E4").is(parsableAs(), Integer.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsInt10() {
    Check.that("4.23456E4").is(parsableAs(), Integer.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsInt11() {
    Check.that("12*6").is(parsableAs(), Integer.class);
  }

  @Test
  public void parsableAsShort00() {
    Check.that("0").is(parsableAs(), Short.class);
  }

  @Test
  public void parsableAsShort01() {
    Check.that("-1").is(parsableAs(), Short.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsShort02() {
    Check.that("   -1").is(parsableAs(), Short.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsShort03() {
    Check.that("-   1").is(parsableAs(), Short.class);
  }

  @Test
  public void parsableAsShort04() {
    Check.that("000001").is(parsableAs(), Short.class);
  }

  @Test
  public void parsableAsShort05() {
    Check.that("3.0000000000000000000").is(parsableAs(), Short.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsShort06() {
    Check.that("3.00000000000000000006").is(parsableAs(), Short.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsShort07() {
    Check.that("99999999999999999999999999999999999999999999999999999999999")
        .is(parsableAs(), Short.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsShort08() {
    Check.that("3.23").is(parsableAs(), Short.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsShort09() {
    Check.that("4.2E4").is(parsableAs(), Short.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsShort10() {
    Check.that("4.23456E4").is(parsableAs(), Short.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsShort11() {
    Check.that("12*6").is(parsableAs(), Short.class);
  }

  @Test
  public void parsableAsByte00() {
    Check.that("0").is(parsableAs(), Byte.class);
  }

  @Test
  public void parsableAsByte01() {
    Check.that("-1").is(parsableAs(), Byte.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsByte02() {
    Check.that("   -1").is(parsableAs(), Byte.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsByte03() {
    Check.that("-   1").is(parsableAs(), Byte.class);
  }

  @Test
  public void parsableAsByte04() {
    Check.that("000001").is(parsableAs(), Byte.class);
  }

  @Test
  public void parsableAsByte05() {
    Check.that("3.0000000000000000000").is(parsableAs(), Byte.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsByte06() {
    Check.that("3.00000000000000000006").is(parsableAs(), Byte.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsByte07() {
    Check.that("99999999999999999999999999999999999999999999999999999999999")
        .is(parsableAs(), Byte.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsByte08() {
    Check.that("3.23").is(parsableAs(), Byte.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsByte09() {
    Check.that("4.2E4").is(parsableAs(), Byte.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsByte10() {
    Check.that("4.23456E4").is(parsableAs(), Byte.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsByte11() {
    Check.that("12*6").is(parsableAs(), Byte.class);
  }

  @Test
  public void parsableAsLong00() {
    Check.that("0").is(parsableAs(), Long.class);
  }

  @Test
  public void parsableAsLong01() {
    Check.that("-1").is(parsableAs(), Long.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsLong02() {
    Check.that("   -1").is(parsableAs(), Long.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsLong03() {
    Check.that("-   1").is(parsableAs(), Long.class);
  }

  @Test
  public void parsableAsLong04() {
    Check.that("000001").is(parsableAs(), Long.class);
  }

  @Test
  public void parsableAsLong05() {
    Check.that("3.0000000000000000000").is(parsableAs(), Long.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsLong06() {
    Check.that("3.00000000000000000006").is(parsableAs(), Long.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsLong07() {
    Check.that("99999999999999999999999999999999999999999999999999999999999")
        .is(parsableAs(), Long.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsLong08() {
    Check.that("3.23").is(parsableAs(), Long.class);
  }

  @Test
  public void parsableAsLong09() {
    Check.that("4.2E4").is(parsableAs(), Long.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsLong10() {
    Check.that("4.23456E4").is(parsableAs(), Long.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsLong11() {
    Check.that("12*6").is(parsableAs(), Long.class);
  }

  @Test
  public void parsableAsDouble00() {
    Check.that("0").is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsDouble01() {
    Check.that("-1").is(parsableAs(), Double.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsDouble02() {
    Check.that("   -1").is(parsableAs(), Double.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsDouble03() {
    Check.that("-   1").is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsDouble04() {
    Check.that("000001").is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsDouble05() {
    Check.that("3.0000000000000000000").is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsDouble06() {
    Check.that("3.00000000000000000006").is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsDouble07() {
    Check.that("99999999999999999999999999999999999999999999999999999999999")
        .is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsDouble08() {
    Check.that("3.23").is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsDouble09() {
    Check.that("4.2E4").is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsDouble10() {
    Check.that("4.23456E4").is(parsableAs(), Double.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsDouble11() {
    Check.that("12*6").is(parsableAs(), Double.class);
  }

  @Test
  public void parsableAsFloat00() {
    Check.that("0").is(parsableAs(), Float.class);
  }

  @Test
  public void parsableAsFloat01() {
    Check.that("-1").is(parsableAs(), Float.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsFloat02() {
    Check.that("   -1").is(parsableAs(), Float.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsFloat03() {
    Check.that("-   1").is(parsableAs(), Float.class);
  }

  @Test
  public void parsableAsFloat04() {
    Check.that("000001").is(parsableAs(), Float.class);
  }

  @Test
  public void parsableAsFloat05() {
    Check.that("3.0000000000000000000").is(parsableAs(), Float.class);
  }

  @Test
  public void parsableAsFloat06() {
    Check.that("3.00000000000000000006").is(parsableAs(), Float.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsFloat07() {
    Check.that("99999999999999999999999999999999999999999999999999999999999")
        .is(parsableAs(), Float.class);
  }

  @Test
  public void parsableAsFloat08() {
    Check.that("3.23").is(parsableAs(), Float.class);
  }

  @Test
  public void parsableAsFloat09() {
    Check.that("4.2E4").is(parsableAs(), Float.class);
  }

  @Test
  public void parsableAsFloat10() {
    Check.that("4.23456E4").is(parsableAs(), Float.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void parsableAsFloat11() {
    Check.that("12*6").is(parsableAs(), Float.class);
  }

  @Test(expected = InvalidCheckException.class)
  public void parsableAs00() {
    Check.that("12*6").is(parsableAs(), AtomicLong.class);
  }

}
