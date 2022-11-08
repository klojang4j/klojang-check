package org.klojang.check;

import org.junit.Test;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.function.ToIntFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.CommonProperties.*;
import static org.klojang.check.TestUtil.floats;

public class ObjectCheckHelper2Test {

  @Test
  public void vanilla00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "collection").has(size(), lt(), 10);
    Check.that(c, "collection").has(size(), "size", lt(), 10);
    Check.that(c, "collection").has(size(),
        lt(),
        10,
        "Size must be ${test} ${0}",
        "ten");
    Check.that(c, "collection").has(size(), lt(), 10, () -> new IOException());
    Check.that(c, "collection").has(size(), positive());
    Check.that(c, "collection").has(size(), "size", positive());
    Check.that(c, "collection").has(size(), positive(), "Size must be ${test}");
    Check.that(c, "collection").has(size(), positive(), () -> new IOException());
    Check.that(c, "collection").has(size(),
        multipleOf(),
        3,
        () -> new IOException());
    Check.that(DayOfWeek.class.getEnumConstants()).has(length(), eq(), 7);
  }

  @Test
  public void vanilla01() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "collection").notHas(size(), gt(), 10);
    Check.that(c, "collection").notHas(size(), "size", gt(), 10);
    Check.that(c, "collection").notHas(size(),
        gt(),
        10,
        "Size must be ${test} ${0}",
        "ten");
    Check.that(c, "collection").notHas(size(), gt(), 10, () -> new IOException());
    Check.that(c, "collection").notHas(size(), zero());
    Check.that(c, "collection").notHas(size(), "size", zero());
    Check.that(c, "collection").notHas(size(), zero(), "size must be ${test}");
    Check.that(c, "collection").notHas(size(), zero(), () -> new IOException());
  }

  @Test
  public void hasPredicate00() {
    String s = "0123456789";
    try {
      Check.that(s, "foo").has(strlen(), negative());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo.length() must be negative (was 10)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHasPredicate00() {
    String s = "0123456789";
    try {
      Check.that(s, "foo").notHas(strlen(), positive());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo.length() must not be positive (was 10)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_IntPredicate00() {
    float[] floats = floats(0F, 1F, 2F, 3F, 4F);
    try {
      Check.that(floats, "jimmie").has(length(), "bimmie", negative());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("jimmie.bimmie must be negative (was 5)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_IntPredicate00() {
    float[] floats = floats(0F, 1F, 2F, 3F, 4F);
    try {
      Check.that(floats, "jimmie").notHas(length(), "bimmie", positive());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("jimmie.bimmie must not be positive (was 5)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntPredicate_CustomMsg00() {
    String s = "0123456789";
    try {
      Check.that(s, "foo").has(strlen(), zero(), "My name is ${tag}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("My name is foo", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntPredicate_CustomMsg00() {
    String s = "0123456789";
    try {
      Check.that(s, "foo").notHas(strlen(), positive(), "Not in ${test} mood today");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Not in positive mood today", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IOException.class)
  public void has_IntPredicate_CustomExc00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "collection").has(size(), zero(), () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void notHas_IntPredicate_CustomExc00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "collection").notHas(size(), x -> x > 2, () -> new IOException());
  }

  @Test
  public void has_IntRelation00() {
    try {
      Check.that(DayOfWeek.class.getEnumConstants(), "days").has(length(), ne(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("days.length must not equal 7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntRelation00() {
    try {
      Check.that(DayOfWeek.class.getEnumConstants(), "days").notHas(length(),
          lte(),
          10);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("days.length must not be <= 10 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_IntRelation00() {
    try {
      Check.that("1234567", "henkie").has(strlen(), "penkie", ne(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("henkie.penkie must not equal 7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_IntRelation00() {
    try {
      Check.that("1234567", "henkie").notHas(strlen(), "penkie", lte(), 10);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("henkie.penkie must not be <= 10 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntRelation_CustomMsg00() {
    try {
      Check.that("1234567").has(strlen(), ne(), 7, "Count to 10: ${arg}${0}", 8910);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Count to 10: 78910", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IOException.class)
  public void has_IntRelation_CustomExc00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "collection").has(size(),
        multipleOf(),
        5,
        () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void motHas_IntRelation_CustomExc00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "collection").notHas(size(),
        multipleOf(),
        3,
        () -> new IOException());
  }

  @Test
  public void lambdasTest() {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c).has((ToIntFunction<Collection<String>>) (Collection::size),
        (x, y) -> x < y,
        10);
    Check.that(c).has(Collection::size, (int x, int y) -> x < y, 10);
    Check.that(c).has(x -> x.size(), (int x, int y) -> x < y, 10);
    Check.that(c).has((Collection<String> x) -> x.size(), (int x) -> x < 10);
  }

}
