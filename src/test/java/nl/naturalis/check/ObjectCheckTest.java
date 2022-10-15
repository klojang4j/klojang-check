package nl.naturalis.check;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonProperties.strlen;
import static nl.naturalis.check.CommonProperties.unbox;
import static nl.naturalis.check.TestUtil.ints;
import static org.junit.Assert.*;

public class ObjectCheckTest {

  @Test
  public void vanilla00() throws Exception {
    Check.that(new int[2][2]).is(notNull());
    Check.that(new int[2][2]).is(notNull(), "custom message");
    Check.that(new int[2][2]).is(notNull(), () -> new Exception());
    Check.that("abc").has(strlen(), lt(), 10);
    Check.that("abc").has(strlen(), lt(), 10, "custom message");
    Check.that("abc").has(strlen(), lt(), 10, () -> new Exception());
    Check.that("abc").has(s -> s.substring(1), EQ(), "bc");
    Check.that("abc").has(s -> s.substring(1), "myprop", EQ(), "bc");
    Check.that("abc").has(s -> s.substring(1), EQ(), "bc", "custom message");
    Check.that("abc").has(s -> s.substring(1), EQ(), "bc", () -> new Exception());
    Check.that((Integer) 2).has(unbox(), CommonChecks.indexOf(), ints(2, 4, 6));
    Check.that((Integer) 2)
        .has(unbox(), CommonChecks.indexOf(), ints(2, 4, 6), "custom message");
    Check.that((Integer) 2)
        .has(unbox(),
            CommonChecks.indexOf(),
            ints(2, 4, 6),
            () -> new Exception());
  }

  @Test
  public void vanilla01() throws Exception {
    Check.that("abc").isNot(empty());
    Check.that("abc").isNot(empty(), "custom message");
    Check.that("abc").isNot(empty(), () -> new Exception());
    Check.that("abc").notHas(strlen(), gt(), 10);
    Check.that("abc").notHas(strlen(), gt(), 10, "custom message");
    Check.that("abc").notHas(strlen(), gt(), 10, () -> new Exception());
    Check.that("abc").notHas(s -> s.substring(1), EQ(), "ab");
    Check.that("abc").notHas(s -> s.substring(1), "myprop", EQ(), "ab");
    Check.that("abc").notHas(s -> s.substring(1), EQ(), "ab", "custom message");
    Check.that("abc").notHas(s -> s.substring(1), EQ(), "ab", () -> new Exception());
    Check.that((Integer) 2).notHas(unbox(), inIntArray(), ints(1, 3, 5));
    Check.that((Integer) 2).notHas(unbox(),
        inIntArray(),
        ints(1, 3, 5),
        "custom message");
    Check.that((Integer) 2).notHas(unbox(),
        inIntArray(),
        ints(1, 3, 5),
        () -> new Exception());
  }

  @Test
  public void is_Predicate00() {
    try {
      Check.that(new int[2][2], "lolita").is(NULL());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "lolita must be null (was int[2][] of [[0, 0], [0, 0]])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_Predicate00() {
    try {
      Check.that(new String[0], "lolita").isNot(empty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("lolita must not be null or empty (was String[0])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void is_Predicate_CustomMsg00() {
    try {
      Check.that(new int[2][0]).is(NULL(), "Almost 2D: ${arg}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Almost 2D: [[], []]", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_Predicate_CustomMsg00() {
    try {
      Check.that(new float[1][1][1]).isNot(deepNotEmpty(), "Definitely 3D: ${arg}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Definitely 3D: [[[0.0]]]", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void is_Predicate_CustomExc00() {
    Check.that(new int[2][0]).is(NULL(), () -> new UnsupportedOperationException());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void isNot_Predicate_CustomExc00() {
    Check.that(new float[1][1][1]).isNot(deepNotEmpty(),
        () -> new UnsupportedOperationException());
  }

  @Test
  public void is_Relation00() {
    try {
      Check.that("AAA", "pedro").is(GT(), "BBB");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("pedro must be > BBB (was AAA)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_Relation00() {
    try {
      Check.that(3.14, "pedro").isNot(GT(), 1.41);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("pedro must not be > 1.41 (was 3.14)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void is_Relation_CustomMsg00() {
    try {
      Check.that("AAA").is(GT(), "BBB", "I say: ${0}${1}${2}", 1, 2, 3);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("I say: 123", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void is_Relation_CustomMsg01() {
    Check.that("aaa").is(LT(), "bbb", "invalid value");
  }

  @Test
  public void is_Relation_CustomMsg02() {
    Check.that("aaa").is(LT(), "bbb", "invalid value: ${arg}");
  }

  @Test
  public void isNot_Relation_CustomMsg00() {
    try {
      Check.that(3.14).isNot(GT(), 1.41, "You say: ${0}=${arg}", "PI");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("You say: PI=3.14", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_Relation_CustomMsg01() {
    String s = Check.that("aaa")
        .is(LT(), "bbb", "invalid value").ok(String::toUpperCase);
    assertEquals("AAA", s);
  }

  @Test
  public void isNot_Relation_CustomMsg02() {
    String s = Check.that("aaa").is(LT(), "bbb", "invalid value: ${0}", "foo").ok();
    assertEquals("aaa", s);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void isNot_Relation_CustomExc00() {
    double d = Check.that(8.22)
        .isNot(GT(), 3.5, () -> new UnsupportedOperationException()).ok();
    assertEquals(8.22, d);
  }

  public void isNot_Relation_CustomExc01() {
    Check.that("foo")
        .isNot(EQ(), 3.5, () -> new UnsupportedOperationException());
  }

  @Test
  public void ok00() {
    int i = Check.that("9").is(valueOf(), int.class).ok(Integer::valueOf);
    assertEquals(9, i);
  }

  @Test
  public void then00() {
    AtomicInteger ai = new AtomicInteger();
    Check.that("-9").is(valueOf(), byte.class).then(s -> ai.set(Integer.valueOf(s)));
    assertEquals(-9, ai.get());
  }

  @Test
  public void and00() {
    assertTrue(
        Check.that("1").is(EQ(), "1").and("2").is(EQ(), "2").getClass()
            == ObjectCheck.class);
  }

  @Test
  public void and01() {
    assertTrue(
        Check.that("1").is(EQ(), "1").and("2", "foo").is(EQ(), "2").getClass()
            == ObjectCheck.class);
  }

  @Test
  public void and02() {
    assertTrue(
        Check.that("1").is(EQ(), "1").and(2).is(eq(), 2).getClass()
            == IntCheck.class);
  }

  @Test
  public void and03() {
    assertTrue(
        Check.that("1").is(EQ(), "1").and(2, "foo").is(eq(), 2).getClass()
            == IntCheck.class);
  }

}
