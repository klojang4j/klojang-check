package org.klojang.check;

import org.junit.Test;
import org.klojang.check.Check;

import java.io.IOException;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.CommonProperties.abs;

public class HasIntIntTest {

  @Test
  public void vanilla00() throws IOException {
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
  public void vanilla01() throws IOException {
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
  public void has_IntPredicate00() {
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
  public void notHas_IntPredicate00() {
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
  public void has_IntPredicate_CustomMsg00() {
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
  public void has_Name_IntPredicate00() {
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
  public void notHas_Name_IntPredicate00() {
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
  public void has_IntRelation00() {
    Check.that(7).has(i -> i + 3, lt(), 100);
  }

  @Test
  public void notHas_IntRelation00() {
    Check.that(7).notHas(i -> i + 3, lt(), 5);
  }

  @Test
  public void has_Name_IntRelation00() {
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
  public void notHas_Name_IntRelation01() {
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
  public void has_IntRelation_CustomMsg00() {
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
  public void notHas_IntRelation_CustomMsg01() {
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
    Check.that(temperature).has((IntFunction<Integer>) i -> Math.abs(i),
        i -> i < 30);
    Check.that(temperature).has(abs(), i -> i < 30);
    Check.that(temperature).has(i -> Math.abs(i), lt(), 30);
    Check.that(temperature).has(abs(), lt(), 30);
  }

}
