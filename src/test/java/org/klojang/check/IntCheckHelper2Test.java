package org.klojang.check;

import org.junit.Test;
import org.klojang.check.relation.IntRelation;

import java.io.IOException;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.CommonProperties.abs;

public class IntCheckHelper2Test {

  @Test
  public void has_HappyPaths_00() throws IOException {
    Check.that(-7, "foo").has(abs(), odd());
    Check.that(-7, "foo").has(abs(), "bar", odd());
    Check.that(-7, "foo").has(abs(), odd(), "A custom message");
    Check.that(-7, "foo").has(abs(), odd(), () -> new IOException());
    Check.that(-7, "foo").has(abs(), lt(), 10);
    Check.that(-7, "foo").has(abs(), "foo", lt(), 10);
    Check.that(-7, "foo").has(abs(), lt(), 10, "A custom message");
    Check.that(-7, "foo").has(abs(), lt(), 10, () -> new IOException());
    Check.that(7).has(i -> i + 3, lt(), 11);
  }

  @Test
  public void notHas_HappyPaths_01() throws IOException {
    Check.that(-7, "foo").notHas(abs(), even());
    Check.that(-7, "foo").notHas(abs(), "bar", even());
    Check.that(-7, "foo").notHas(abs(), even(), "A custom message");
    Check.that(-7, "foo").notHas(abs(), even(), () -> new IOException());
    Check.that(-7, "foo").notHas(abs(), lt(), 5);
    Check.that(-7, "foo").notHas(abs(), "foo", lt(), 5);
    Check.that(-7, "foo").notHas(abs(), lt(), 5, "A custom message");
    Check.that(-7, "foo").notHas(abs(), lt(), 5, () -> new IOException());
  }

  @Test
  public void has_IntPredicate_CommonCheck_00() {
    try {
      Check.that(-7, "foo").has(abs(), even());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("abs(foo) must be even (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntPredicate_CustomCheck_00() {
    try {
      Check.that(-7, "foo").has(abs(), i -> i % 2 == 0);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for abs(foo): 7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntPredicate_CommonCheck_00() {
    try {
      Check.that(-7).notHas(abs(), odd());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("abs(argument) must not be odd (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntPredicate_CustomProperty_CommonCheck_00() {
    try {
      Check.that(-7).notHas(i -> abs().applyAsInt(i), odd());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("IntUnaryOperator.applyAsInt(-7) must not be odd (was 7)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntPredicate_CustomCheck_00() {
    try {
      Check.that(-7).notHas(abs(), i -> i % 2 == 1);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for abs(argument): -7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntPredicate_CustomProperty_CustomCheck_00() {
    try {
      Check.that(-7).notHas(i -> abs().applyAsInt(i), (int i) -> i % 2 == 1);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for IntUnaryOperator.applyAsInt(-7): -7",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntPredicate_CustomMsg_CommonCheck_00() {
    try {
      Check.that(-7).has(abs(),
          even(),
          "Test ${test} did not go as planned for ${type}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Test even did not go as planned for int", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntPredicate_CustomMsg_CustomCheck_00() {
    try {
      Check.that(-7).has(abs(),
          i -> i % 2 == 0,
          "${tag} did not go as planned for ${type}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("argument did not go as planned for int", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntPredicate_CustomMsg00() {
    try {
      Check.that(-7).notHas(abs(),
          odd(),
          "Test ${test} did not go as planned for ${arg}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Test odd did not go as planned for 7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_IntPredicate_CommonCheck_00() {
    try {
      Check.that(7, "foo").has(i -> i + 3, "bar", negative());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo.bar must be negative (was 10)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_IntPredicate_CustomCheck_00() {
    try {
      Check.that(7, "foo").has(i -> i + 3, "bar", (int i) -> i < 0);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for foo.bar: 7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_IntPredicate_CommonCheck_00() {
    try {
      Check.that(7, "foo").notHas(i -> i + 3, "bar", positive());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo.bar must not be positive (was 10)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_IntPredicate_CustomCheck_00() {
    try {
      Check.that(7, "foo").notHas(i -> i + 3, "bar", (int i) -> i > 0);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for foo.bar: 7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntRelation00() {
    Check.that(7).has(i -> i + 3, lt(), 100);
  }

  @Test
  public void notHas_IntRelation00() {
    Check.that(7).notHas(i -> i + 3, lt(), 5);
  }

  private static IntRelation myGt() {
    return (x, y) -> x > y;
  }

  @Test
  public void has_Name_IntRelation_CommonCheck_00() {
    try {
      Check.that(7, "foo").has(i -> i + 3, "bar", gt(), 100);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo.bar must be > 100 (was 10)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_IntRelation_CustomCheck_00() {
    try {
      Check.that(7, "foo").has(i -> i + 3, "bar", myGt(), 100);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for foo.bar: no such relation between 7 and 100",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_IntRelation_CommonCheck_00() {
    try {
      Check.that(7, "foo").notHas(i -> i + 3, "bar", gt(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo.bar must not be > 5 (was 10)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_IntRelation_CustomCheck_00() {
    try {
      Check.that(7, "foo").notHas(i -> i + 3, "bar", myGt(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for foo.bar: no such relation between 10 and 5",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntRelation_CustomMsg_CommonCheck_00() {
    try {
      Check.that(7).has(i -> i + 3, gt(), 100, "Oops: ${type} ${arg} was invalid");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Oops: int 10 was invalid", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntRelation_CustomMsg_Custom_Check_00() {
    try {
      Check.that(7).has(i -> i + 3, myGt(), 100, "Oops: ${type} ${arg} was invalid");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Oops: int 10 was invalid", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntRelation_CustomMsg_CommonCheck_01() {
    try {
      Check.that(7).notHas(i -> i + 3,
          gt(),
          5,
          "This number is fun: ${arg}${arg}${arg}${obj}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("This number is fun: 1010105", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntRelation_CustomMsg_CustomCheck_01() {
    try {
      Check.that(7).notHas(i -> i + 3,
          myGt(),
          5,
          "This number is fun: ${arg}${arg}${arg}${obj}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("This number is fun: 1010105", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IOException.class)
  public void has_IntRelation_CustomExc00() throws IOException {
    Check.that(7).has(i -> i + 3, gt(), 100, () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void notHas_IntRelation_CustomExc01() throws IOException {
    Check.that(7).notHas(i -> i + 3, gt(), 5, () -> new IOException());
  }

  @Test
  public void testLambdas() {
    int temperature = -7;
    Check.that(temperature).has(i -> Math.abs(i), (int i) -> i < 30);
    Check.that(temperature).has((IntUnaryOperator) i -> Math.abs(i), i -> i < 30);
    Check.that(temperature).has(i -> Math.abs(i), (Integer i) -> i % 2 == 1);
    Check.that(temperature).has(
        (IntFunction<Integer>) i -> Math.abs(i),
        i -> i < 30);
    Check.that(temperature).has(abs(), i -> i < 30);
    Check.that(temperature).has(i -> Math.abs(i), lt(), 30);
    Check.that(temperature).has(abs(), lt(), 30);
  }

}
