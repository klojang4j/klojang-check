package org.klojang.check;

import org.junit.Test;
import org.klojang.check.Check;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.TestUtil.*;

public class MsgIntObjRelationTest {

  @Test(expected = IllegalArgumentException.class)
  public void lambdaAsIntObjRelation00() {
    Check.that(7).is((x, y) -> y.length() > x, "Foo");
  }

  @Test
  public void intObjRelation00() {
    Check.that(7).is(indexOf(), new float[10]);
    Check.that(7).is(indexOf(),
        IntStream.range(0, 10).mapToObj(String::valueOf).collect(toList()));
    Check.that(7).is(indexOf(), "Hello, Sam");
    Check.that(7).is(inIntArray(), ints(3, 5, 7, 9));
    Check.that(7).is(inRange(), ints(7, 8));
    Check.that(7).isNot(inRange(), ints(6, 7));
    Check.that(7).is(between(), ints(7, 7));
    Check.that(7).isNot(inRange(), ints(8, 10));
  }

  @Test
  public void indexOf00() {
    try {
      Check.that(7, "pepsi").is(indexOf(), new Object[5]);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("pepsi must be >= 0 and < 5 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void indexOf01() {
    try {
      Check.that(7, "pepsi").isNot(indexOf(), new Object[10]);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("pepsi must be < 0 or >= 10 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void indexInto00() {
    try {
      Check.that(7, "cola").is(indexOf(), IntStream.range(0, 5)
          .mapToObj(String::valueOf)
          .collect(toList()));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("cola must be >= 0 and < 5 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void indexInto01() {
    try {
      Check.that(7, "cola").isNot(indexOf(), IntStream.range(0, 10)
          .mapToObj(String::valueOf)
          .collect(toList()));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("cola must be < 0 or >= 10 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void strIndexOf00() {
    try {
      Check.that(7, "corona").is(indexOf(), "Hello");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("corona must be >= 0 and < 5 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void strIndexOf01() {
    try {
      Check.that(7, "corona").isNot(indexOf(), "Hello, Sam");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("corona must be < 0 or >= 10 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void intElementOf00() {
    try {
      Check.that(7, "tapioka").is(inIntArray(), ints(3, 5, 9));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "tapioka must be element of int[3] of [3, 5, 9] (was 7)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void intElementOf01() {
    try {
      Check.that(7, "tapioka").isNot(inIntArray(), ints(3, 5, 7));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "tapioka must not be element of int[3] of [3, 5, 7] (was 7)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void inRange00() {
    try {
      Check.that(7, "tapestry").is(inRange(), ints(100, 200));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("tapestry must be >= 100 and < 200 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void inRange01() {
    try {
      Check.that(7, "tapestry").isNot(inRange(), ints(6, 8));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("tapestry must be < 6 or >= 8 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void inRangeClosed00() {
    try {
      Check.that(7, "sunshine").is(between(), ints(100, 200));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("sunshine must be >= 100 and <= 200 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void inRangeClosed01() {
    try {
      Check.that(7, "sunshine").isNot(between(), ints(-7, 7));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("sunshine must be < -7 or > 7 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

}
