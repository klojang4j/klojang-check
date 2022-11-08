package org.klojang.check.x.msg;

import org.junit.Test;
import org.klojang.check.Check;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.klojang.check.CommonChecks.*;

public class MsgIntRelationTest {

  @Test(expected = IllegalArgumentException.class)
  public void lambdaAsIntRelation() {
    Check.that(7).is((x, y) -> x > y, 9);
  }

  @Test
  public void intRelation00() {
    Check.that(9).is(gt(), 7);
    Check.that(9).is(gte(), 9);
    Check.that(9).is(eq(), 9);
    Check.that(9).is(ne(), 11);
    Check.that(9).is(lt(), 11);
    Check.that(11).is(lte(), 11);
    Check.that(15).is(multipleOf(), 5);
    Check.that(16).isNot(multipleOf(), 5);
  }

  @Test
  public void intRelation01() {
    try {
      Check.that(7).is(gt(), 9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("argument must be > 9 (was 7)", e.getMessage());
    }
  }

  @Test
  public void intRelation02() {
    try {
      Check.that(9, "foo").is(lt(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo must be < 7 (was 9)", e.getMessage());
    }
  }

  @Test
  public void intRelation03() {
    try {
      Check.on(IOException::new, 9, "foo").is(lte(), 8);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      assertEquals("foo must be <= 8 (was 9)", e.getMessage());
    }
  }

  @Test
  public void intRelation04() {
    try {
      Check.on(IOException::new, 8, "foo").is(gte(),
          9,
          "${tag} incorrect: ${arg}. Required: ${obj}");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      assertEquals("foo incorrect: 8. Required: 9", e.getMessage());
    }
  }

  @Test
  public void intRelation05() {
    try {
      Check.that(7, "foo").is(ne(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo must not equal 7", e.getMessage());
    }
  }

  @Test
  public void intRelation06() {
    try {
      Check.that(7, "foo").isNot(eq(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo must not equal 7", e.getMessage());
    }
  }

  @Test
  public void intRelation07() {
    try {
      Check.that(7, "foo").is(multipleOf(), 3);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo must be multiple of 3 (was 7)", e.getMessage());
    }
  }

  @Test
  public void intRelation08() {
    try {
      Check.that(21, "foo").isNot(multipleOf(), 3);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo must not be multiple of 3 (was 21)", e.getMessage());
    }
  }

}
