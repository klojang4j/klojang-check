package nl.naturalis.check;

import org.junit.Test;

import static nl.naturalis.common.ArrayMethods.ints;
import static nl.naturalis.check.CommonChecks.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static nl.naturalis.check.CommonProperties.*;

public class IntCheckTest {

  @Test
  public void vanilla00() {
    Check.that(42).is(even());
    Check.that(42).is(even(), "custom message");
    Check.that(42).is(even(), () -> new UnsupportedOperationException());
    Check.that(42).is(lt(), 43);
    Check.that(42).is(lt(), 43, "custom message");
    Check.that(42).is(lt(), 43, () -> new UnsupportedOperationException());
    Check.that(42).is(inIntArray(), ints(40, 42, 44));
    Check.that(42).is(inIntArray(), ints(40, 42, 44), "custom message");
    Check.that(42).is(inIntArray(),
        ints(40, 42, 44),
        () -> new UnsupportedOperationException());
    Check.that(42).has(box(), deepNotEmpty());
    Check.that(42).has(box(), "box", deepNotEmpty());
    Check.that(42).has(box(), deepNotEmpty(), "custom message");
    Check.that(42).has(box(),
        deepNotEmpty(),
        () -> new UnsupportedOperationException());
    Check.that(42).has(i -> i / 7, eq(), 6);
    Check.that(42).has(i -> i / 7, eq(), 6, "myprop");
    Check.that(42).has(i -> i / 7, eq(), 6, "custom message");
    Check.that(42).has(i -> i / 7,
        eq(),
        6,
        () -> new UnsupportedOperationException());
  }

  @Test
  public void vanilla01() {
    Check.that(42).isNot(odd());
    Check.that(42).isNot(odd(), "custom message");
    Check.that(42).isNot(odd(), () -> new UnsupportedOperationException());
    Check.that(42).isNot(gt(), 43);
    Check.that(42).isNot(gt(), 43, "custom message");
    Check.that(42).isNot(gt(), 43, () -> new UnsupportedOperationException());
    Check.that(42).isNot(indexOf(), "42");
    Check.that(42).isNot(indexOf(), "42", "custom message");
    Check.that(42).isNot(indexOf(),
        "42",
        () -> new UnsupportedOperationException());
    Check.that(42).notHas(box(), NULL());
    Check.that(42).notHas(box(), NULL(), "custom message");
    Check.that(42).notHas(box(), NULL(), () -> new UnsupportedOperationException());
    Check.that(42).notHas(box(), "box", NULL());
    Check.that(42).notHas(i -> i / 7, eq(), 9);
    Check.that(42).notHas(i -> i / 7, eq(), 9, "custom message");
    Check.that(42).notHas(i -> i / 7,
        eq(),
        9,
        () -> new UnsupportedOperationException());
    Check.that(42).notHas(i -> i / 7, eq(), 9, "my prop");
  }

  @Test
  public void is_IntPredicate00() {
    try {
      Check.that(7, "lion").is(even());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("lion must be even (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_IntPredicate00() {
    try {
      Check.that(7, "lion").isNot(odd());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("lion must not be odd (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void is_IntPredicate_CustomMsg00() {
    try {
      Check.that(7, "lion").is(even(), "Not a lucky number: ${arg}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Not a lucky number: 7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_IntPredicate_CustomMsg00() {
    try {
      Check.that(7, "lion").isNot(odd(), "That's ${test}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("That's odd", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void is_IntPredicate_CustomExc00() {
    Check.that(7).is(even(), () -> new IndexOutOfBoundsException());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void isNot_IntPredicate_CustomExc00() {
    Check.that(7).isNot(odd(), () -> new IndexOutOfBoundsException());
  }

  @Test
  public void is_IntRelation00() {
    try {
      Check.that(7, "elephant").is(multipleOf(), 4);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("elephant must be multiple of 4 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_IntRelation00() {
    try {
      Check.that(7, "elephant").isNot(multipleOf(), 1);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("elephant must not be multiple of 1 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void is_IntRelation_CustomMsg00() {
    try {
      Check.that(7, "lion").is(gt(), 17, "Not a lucky number: ${obj}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Not a lucky number: 17", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_IntRelation_CustomMsg00() {
    try {
      Check.that(7, "lion").isNot(gte(), 2, "That's ${test}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("That's gte", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void is_IntRelation_CustomExc00() {
    Check.that(7).is(lt(), 3, () -> new IndexOutOfBoundsException());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void isNot_IntRelation_CustomExc00() {
    Check.that(7).isNot(gt(), 5, () -> new IndexOutOfBoundsException());
  }

  @Test
  public void is_IntObjRelation00() {
    try {
      Check.that(7, "cat").is(inIntArray(), ints(2, 4, 6, 8, 10));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("cat must be element of int[5] of [2, 4, 6, 8, 10] (was 7)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_IntObjRelation00() {
    try {
      Check.that(2, "cat").isNot(indexOf(), "1234567");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("cat must be < 0 or >= 7 (was 2)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void is_IntObjRelation_CustomMsg00() {
    try {
      Check.that(7).is(inIntArray(), ints(2, 4, 6, 8, 10), "${test}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("inIntArray", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void isNot_IntObjRelation_CustomMsg00() {
    try {
      Check.that(2).isNot(indexOf(), "${0}", "BAR");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("BAR", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void is_IntObjRelation_CustomExc00() {
    Check.that(7).is(indexOf(),
        ints(2, 4, 6, 8, 10),
        () -> new IndexOutOfBoundsException());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void isNot_IntObjRelation_CustomExc00() {
    Check.that(2).isNot(indexOf(),
        "1234567",
        () -> new IndexOutOfBoundsException());
  }

  @Test
  public void has_Predicate00() {
    try {
      Check.that(100).has(box(), x -> x.getClass().equals(String.class));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for Integer.valueOf(argument): 100",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Predicate00() {
    try {
      Check.that(100).notHas(box(), x -> x.getClass().equals(Integer.class));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for Integer.valueOf(argument): 100",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_Predicate00() {
    try {
      Check.that(100, "my").has(box(),
          "box",
          x -> x.getClass().equals(String.class));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for my.box: 100", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_Predicate00() {
    try {
      Check.that(100, "my").notHas(box(),
          "box",
          x -> x.getClass().equals(Integer.class));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for my.box: 100", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Predicate_CustomMsg00() {
    try {
      Check.that(100).has(box(), x -> x.getClass().equals(String.class), "BIM");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("BIM", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Predicate_CustomMsg00() {
    try {
      Check.that(100).notHas(box(), x -> x.getClass().equals(Integer.class), "BAM");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("BAM", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void has_Predicate_CustomExc00() {
    Check.that(100)
        .has(box(),
            x -> x.getClass().equals(String.class),
            () -> new IndexOutOfBoundsException());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void notHas_Predicate_CustomExc00() {
    Check.that(100)
        .notHas(
            box(),
            x -> x.getClass().equals(Integer.class),
            () -> new IndexOutOfBoundsException());
  }

}
