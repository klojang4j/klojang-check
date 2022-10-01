package nl.naturalis.check;

import org.junit.Test;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonProperties.strlen;
import static nl.naturalis.check.CommonProperties.unbox;
import static nl.naturalis.common.ArrayMethods.ints;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

  @Test(expected = UnsupportedOperationException.class)
  public void is_Relation_CustomExc00() {
    Check.that(MONDAY).is(GTE(),
        THURSDAY,
        () -> new UnsupportedOperationException());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void isNot_Relation_CustomExc00() {
    Check.that(8.22).isNot(GT(), 3.5, () -> new UnsupportedOperationException());
  }

}
