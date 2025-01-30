package org.klojang.check;

import org.junit.Test;
import org.klojang.check.types.IntObjRelation;
import org.klojang.check.types.IntRelation;

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

  private static final DayOfWeek[] DAYS = DayOfWeek.class.getEnumConstants();

  private static IntRelation myNotEqual() {
    return (x, y) -> x != y;
  }

  private static IntRelation myLte() {
    return (x, y) -> x <= y;
  }

  private static IntRelation myMultipleOf() {
    return (x, y) -> multipleOf().exists(x, y);
  }

  private static IntObjRelation<List<Integer>> myFalseIntObj() {
    return (x, y) -> false;
  }

  private static IntObjRelation<List<Integer>> myTrueIntObj() {
    return (x, y) -> true;
  }

  @Test
  public void has_happyPaths_00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "c").has(size(), lt(), 10);
    Check.that(c, "c").has(size(), "size", lt(), 10);
    Check.that(c, "c").has(size(), lt(), 10, "Size must be ${test} ${0}", "ten");
    Check.that(c, "c").has(size(), lt(), 10, () -> new IOException());
    Check.that(c, "c").has(size(), positive());
    Check.that(c, "c").has(size(), "size", positive());
    Check.that(c, "c").has(size(), positive(), "Size must be ${test}");
    Check.that(c, "c").has(size(), positive(), () -> new IOException());
    Check.that(c, "c").has(size(), multipleOf(), 3, () -> new IOException());
    Check.that(DAYS).has(length(), eq(), 7);
    // IntObjRelation test - far-fetched but does the job
    Check.that(DAYS, "days").has(length(),
        "pays",
        indexOf(),
        List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
  }

  @Test
  public void notHas_happyPaths_00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "c").notHas(size(), gt(), 10);
    Check.that(c, "c").notHas(size(), "size", gt(), 10);
    Check.that(c, "c").notHas(size(), gt(), 10, "Size must be ${test} ${0}", "ten");
    Check.that(c, "c").notHas(size(), gt(), 10, () -> new IOException());
    Check.that(c, "c").notHas(size(), zero());
    Check.that(c, "c").notHas(size(), "size", zero());
    Check.that(c, "c").notHas(size(), zero(), "size must be ${test}");
    Check.that(c, "c").notHas(size(), zero(), () -> new IOException());
    Check.that(DAYS, "days").notHas(length(), "pays", indexOf(), List.of(1, 2));
  }

  @Test
  public void hasPredicate_CommonCheck_00() {
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
  public void hasPredicate_CustomCheck_00() {
    String s = "0123456789";
    try {
      Check.that(s, "foo").has(strlen(), i -> i < 0);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for foo.length(): 10", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHasPredicate_CommonCheck_00() {
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
  public void notHasPredicate_CustomCheck_00() {
    String s = "0123456789";
    try {
      Check.that(s, "foo").notHas(strlen(), i -> i > 0);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for foo.length(): 10", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_IntPredicate_CommonCheck_00() {
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
  public void has_Name_IntPredicate_CustomCheck_00() {
    float[] floats = floats(0F, 1F, 2F, 3F, 4F);
    try {
      Check.that(floats, "jimmie").has(length(), "bimmie", i -> i < 0);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for jimmie.bimmie: 5", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_IntPredicate_CommonCheck_00() {
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
  public void notHas_Name_IntPredicate_CustomCheck_00() {
    float[] floats = floats(0F, 1F, 2F, 3F, 4F);
    try {
      Check.that(floats, "jimmie").notHas(length(), "bimmie", i -> i > 0);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for jimmie.bimmie: 5", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntPredicate_CustomMsg_CommonCheck_00() {
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
  public void has_IntPredicate_CustomMsg_CustomCheck_00() {
    String s = "0123456789";
    try {
      Check.that(s, "foo").has(strlen(), i -> i == 0, "My name is ${tag}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("My name is foo", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntPredicate_CustomMsg_CommonCheck_00() {
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

  @Test
  public void notHas_IntPredicate_CustomMsg_CustomCheck_00() {
    String s = "0123456789";
    try {
      Check.that(s, "foo").notHas(strlen(), i -> i > 0, "Not in ${tag} mood today");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Not in foo mood today", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IOException.class)
  public void has_IntPredicate_CustomExc_CommonCheck_00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "collection").has(size(), zero(), () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void has_IntPredicate_CustomExc_CustomCheck_00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "collection").has(size(), i -> i == 0, () -> new IOException());
  }

  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////

  @Test
  public void has_IntRelation_CommonCheck_00() {
    try {
      Check.that(DAYS, "days").has(length(), ne(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("days.length must not equal 7", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntRelation_CustomCheck_00() {
    try {
      Check.that(DAYS, "days").has(length(), myNotEqual(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("invalid value for days.length: no such relation between 7 and 7",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntRelation_CommonCheck_00() {
    try {
      Check.that(DAYS, "days").notHas(length(), lte(), 10);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("days.length must not be <= 10 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntRelation_CustomCheck_00() {
    try {
      Check.that(DAYS, "days").notHas(length(), myLte(), 10);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for days.length: no such relation between 7 and 10",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_WithTag_IntRelation_CommonCheck_00() {
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
  public void has_WithTag_IntRelation_CustomCheck_00() {
    try {
      Check.that("1234567", "henkie").has(strlen(), "penkie", myNotEqual(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for henkie.penkie: no such relation between 7 and 7",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_IntRelation_CommonCheck_00() {
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
  public void notHas_WithTag_IntRelation_CustomCheck_00() {
    try {
      Check.that("1234567", "henkie").notHas(strlen(), "penkie", myLte(), 10);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for henkie.penkie: no such relation between 7 and 10",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntRelation_CustomMsg_CommonCheck_00() {
    try {
      Check.that("1234567").has(strlen(), ne(), 7, "Count to 10: ${arg}${0}", 8910);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Count to 10: 78910", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntRelation_CustomMsg_CustomCheck_00() {
    try {
      Check.that("1234567").has(strlen(),
          myNotEqual(),
          7,
          "Count to 10: ${arg}${0}",
          8910);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Count to 10: 78910", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntRelation_CustomMsg_CommonCheck_00() {
    try {
      Check.that("abc").notHas(strlen(), eq(), 3, "foo=bar");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo=bar", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntRelation_CustomMsg_CustomCheck_00() {
    try {
      Check.that("abc").notHas(strlen(),
          (int x, int y) -> x == y,
          3,
          "foo=bar");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo=bar", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IOException.class)
  public void has_IntRelation_CustomExc_CommonCheck_00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "c").has(size(), multipleOf(), 5, () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void has_IntRelation_CustomExc_CustomCheck_00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "c").has(size(), myMultipleOf(), 5, () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void notHas_IntRelation_CustomExc_CommonCheck_00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "c").notHas(size(), multipleOf(), 3, () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void notHas_IntRelation_CustomExc_CustomCheck_00() throws IOException {
    Collection<String> c = List.of("a", "b", "c", "d", "e", "f");
    Check.that(c, "c").notHas(size(), myMultipleOf(), 3, () -> new IOException());
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

  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////

  @Test
  public void has_IntObjRelation_CommonCheck_00() {
    try {
      Check.that(DAYS, "days").has(length(), indexOf(), List.of(1, 2, 3));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("days.length must be >= 0 and < 3 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntObjRelation_CustomCheck_00() {
    try {
      Check.that(DAYS, "days").has(length(), myFalseIntObj(), List.of(1, 2, 3));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for days.length: no such relation between 7 and ListN[3] of [1, 2, 3]",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntObjRelation_CommonCheck_00() {
    try {
      Check.that(DAYS, "days").notHas(length(),
          indexOf(),
          List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("days.length must be < 0 or >= 9 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntObjRelation_CustomCheck_00() {
    try {
      Check.that(DAYS, "days").notHas(length(), myTrueIntObj(), List.of(1, 2, 3));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for days.length: no such relation between 7 and ListN[3] of [1, 2, 3]",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_WithTag_IntObjRelation_CommonCheck_00() {
    try {
      Check.that(DAYS, "days").has(length(), "pays", indexOf(), List.of(1, 2, 3));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("days.pays must be >= 0 and < 3 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_WithTag_IntObjRelation_CustomCheck_00() {
    try {
      Check.that(DAYS, "days").has(length(),
          "pays",
          myFalseIntObj(),
          List.of(1, 2, 3));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for days.pays: no such relation between 7 and ListN[3] of [1, 2, 3]",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_IntObjRelation_CommonCheck_00() {
    try {
      Check.that(DAYS, "days").notHas(length(),
          "pays",
          indexOf(),
          List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("days.pays must be < 0 or >= 9 (was 7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_IntObjRelation_CustomCheck_00() {
    try {
      Check.that(DAYS, "days").notHas(length(),
          "pays",
          myTrueIntObj(),
          List.of(1, 2, 3));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for days.pays: no such relation between 7 and ListN[3] of [1, 2, 3]",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntObjRelation_CustomMessage_CommonCheck_00() {
    try {
      Check.that(DAYS, "days").has(length(),
          indexOf(),
          List.of(1, 2, 3),
          "not so bad");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("not so bad", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_IntObjRelation_CustomMessage_CustomCheck_00() {
    try {
      Check.that(DAYS, "days").has(length(),
          myFalseIntObj(),
          List.of(1, 2, 3),
          "not so bad");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("not so bad", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntObjRelation_CustomMessage_CommonCheck_00() {
    try {
      Check.that(DAYS, "days").notHas(length(),
          indexOf(),
          List.of(1, 2, 3, 4, 5, 6, 7, 8, 9),
          "not so bad");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("not so bad", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_IntObjRelation_CustomMessage_CustomCheck_00() {
    try {
      Check.that(DAYS, "days").notHas(length(),
          myTrueIntObj(),
          List.of(1, 2, 3),
          "not so bad");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("not so bad", e.getMessage());
      return;
    }
    fail();
  }

}
