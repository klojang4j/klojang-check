package org.klojang.check;

import org.junit.Test;
import org.klojang.check.relation.Relation;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.function.IntPredicate;

import static java.time.DayOfWeek.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.CommonChecks.*;

public class IntCheckHelper1Test {

  private static final DayOfWeek[] days = DayOfWeek.class.getEnumConstants();

  private static <T extends Comparable<T>> Relation<T, T> myGTE() {
    return (x, y) -> x.compareTo(y) >= 0;
  }

  private static <T extends Comparable<T>> Relation<T, T> myLT() {
    return (x, y) -> x.compareTo(y) < 0;
  }

  @Test
  public void vanilla00() {
    Check.that(6).has((int i) -> days[i], d -> d.equals(SUNDAY));
    Check.that(7).has(i -> Math.abs(i), (int i) -> i < 30);
    Check.that(7).has(i -> Math.abs(i), (IntPredicate) i -> i < 30);
    Check.that(6).has(i -> days[i], (DayOfWeek d) -> d.equals(SUNDAY));
  }

  @Test
  public void vanilla01() {
    Check.that(6).notHas((int i) -> days[i], d -> d.equals(SATURDAY));
  }

  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////

  @Test
  public void has_Predicate_CommonCheck_00() {
    try {
      Check.that(6, "shoe").has(i -> "", notEmpty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "IntFunction.apply(6) must not be null or empty (was \"\")",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Predicate_CustomCheck_00() {
    try {
      Check.that(6, "shoe").has(i -> "", (String s) -> !s.isEmpty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for IntFunction.apply(6): \"\"",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Predicate_CommonCheck_00() {
    try {
      Check.that(6, "shoe").notHas(i -> "", empty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "IntFunction.apply(6) must not be null or empty (was \"\")",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Predicate_CustomCheck_00() {
    try {
      Check.that(6, "shoe").notHas(i -> "", (String s) -> s.isEmpty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for IntFunction.apply(6): \"\"",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_WithTag_Predicate_CommonCheck_00() {
    try {
      Check.that(6, "shoe").has(i -> "", "laces", notEmpty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "shoe.laces must not be null or empty (was \"\")",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_WithTag_Predicate_CustomCheck_00() {
    try {
      Check.that(6, "shoe").has(i -> "", "laces", (String s) -> !s.isEmpty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for shoe.laces: \"\"",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_Predicate_CommonCheck_00() {
    try {
      Check.that(6, "shoe").notHas(i -> "", "laces", empty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "shoe.laces must not be null or empty (was \"\")",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_Predicate_CustomCheck_00() {
    try {
      Check.that(6, "shoe").notHas(i -> "", "laces", (String s) -> s.isEmpty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for shoe.laces: \"\"",
          e.getMessage());
      return;
    }
    fail();
  }

  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////////

  @Test
  public void has_Predicate00() {
    try {
      Check.that(6, "shoe").has((int i) -> days[i], d -> d.equals(MONDAY));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for IntFunction.apply(6): SUNDAY",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Predicate01() {
    try {
      Check.that(6).has((int i) -> days[i], d -> d.equals(MONDAY));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for IntFunction.apply(6): SUNDAY",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Predicate00() {
    try {
      Check.that(6, "shoe").notHas((int i) -> days[i], d -> d.equals(SUNDAY));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for IntFunction.apply(6): SUNDAY",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_WithTag_Predicate00() {
    try {
      Check.that(6, "shoe").has((int i) -> days[i], "laces", d -> d.equals(MONDAY));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for shoe.laces: SUNDAY",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_Predicate00() {
    try {
      Check.that(6, "shoe").notHas((int i) -> days[i],
          "laces",
          d -> d.equals(SUNDAY));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for shoe.laces: SUNDAY",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_Predicate0() {
    try {
      Check.that(6).notHas((int i) -> days[i], "laces", d -> d.equals(SUNDAY));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for laces: SUNDAY",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Predicate_CustomMsg00() {
    try {
      Check.that(6).has((int i) -> days[i], d -> d.equals(MONDAY), "FOO");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("FOO", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Predicate_CustomMsg00() {
    try {
      Check.that(6).notHas((int i) -> days[i], d -> d.equals(SUNDAY), "BAR");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("BAR", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void has_Predicate_CustomExc00() {
    Check.that(6)
        .has((int i) -> days[i],
            d -> d.equals(MONDAY),
            () -> new UnsupportedOperationException());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void notHas_Predicate_CustomExc00() {
    Check.that(6)
        .notHas(
            (int i) -> days[i],
            d -> d.equals(SUNDAY),
            () -> new UnsupportedOperationException());
  }

  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////

  @Test
  public void happyPaths02() {
    Check.that(6, "shoe").has(i -> 4.5d, LT(), 9.9);
    Check.that(6).has(i -> 4.5d, LT(), 9.9);
    Check.that(6, "shoe").notHas(i -> 4.5d, GTE(), 9.9);
    Check.that(6, "shoe").has(i -> 4.5d, "laces", LT(), 9.9);
    Check.that(6, "shoe").notHas(i -> 4.5d, "laces", GTE(), 9.9);
    Check.that(6).has(i -> 4.5d, LT(), 9.9, "FOO");
    Check.that(6).notHas(i -> 4.5d, GTE(), 9.9, "BAR");
    Check.that(6).has(i -> 4.5d,
        LT(),
        9.9,
        () -> new UnsupportedOperationException());
    Check.that(6).notHas(i -> 4.5d,
        GTE(),
        9.9,
        () -> new UnsupportedOperationException());
  }

  @Test
  public void has_Relation_CommonCheck_00() {
    try {
      Check.that(6, "shoe").has(i -> 4.5d, GTE(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "IntFunction.apply(6) must be >= 9.9 (was 4.5)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Relation_CommonCheck_01() {
    try {
      Check.that(6).has(i -> 4.5d, GTE(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "IntFunction.apply(6) must be >= 9.9 (was 4.5)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Relation_CustomCheck_00() {
    try {
      Check.that(6, "shoe").has(i -> 4.5d, myGTE(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for IntFunction.apply(6): no such relation between 4.5 and 9.9",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Relation_CustomCheck_01() {
    try {
      Check.that(6).has(i -> 4.5d, myGTE(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for IntFunction.apply(6): no such relation between 4.5 and 9.9",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Relation_CommonCheck_00() {
    try {
      Check.that(6, "shoe").notHas(i -> 4.5d, LT(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "IntFunction.apply(6) must not be < 9.9 (was 4.5)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Relation_CustomCheck_00() {
    try {
      Check.that(6, "shoe").notHas(i -> 4.5d, myLT(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for IntFunction.apply(6): no such relation between 4.5 and 9.9",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_WithTag_Relation_CommonCheck_00() {
    try {
      Check.that(6, "shoe").has(i -> 4.5d, "laces", GTE(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "shoe.laces must be >= 9.9 (was 4.5)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_WithTag_Relation_CustomCheck_00() {
    try {
      Check.that(6, "shoe").has(i -> 4.5d, "laces", myGTE(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for shoe.laces: no such relation between 4.5 and 9.9",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_Relation_CommonCheck_00() {
    try {
      Check.that(6, "shoe").notHas(i -> 4.5d, "laces", LT(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("shoe.laces must be < 9.9 (was 4.5)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_Relation_CommonCheck_01() {
    try {
      Check.that(6).notHas(i -> 4.5d, "laces", LT(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("laces must be < 9.9 (was 4.5)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_Relation_CustomCheck_00() {
    try {
      Check.that(6, "shoe").notHas(i -> 4.5d, "laces", myLT(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for shoe.laces: no such relation between 4.5 and 9.9",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_WithTag_Relation_CustomCheck_01() {
    try {
      Check.that(6).notHas(i -> 4.5d, "laces", myLT(), 9.9);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "invalid value for laces: no such relation between 4.5 and 9.9",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Relation_CustomMsg_CommonCheck_00() {
    try {
      Check.that(6).has(i -> 4.5d, GTE(), 9.9, "FOO");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("FOO", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Relation_CustomMsg_CustomCheck_00() {
    try {
      Check.that(6).has(i -> 4.5d, myGTE(), 9.9, "FOO");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("FOO", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Relation_CustomMsg_CommonCheck_00() {
    try {
      Check.that(6).notHas(i -> 4.5d, LT(), 9.9, "BAR");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("BAR", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Relation_CustomMsg_CustomCheck_00() {
    try {
      Check.that(6).notHas(i -> 4.5d, myLT(), 9.9, "BAR");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("BAR", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void has_Relation_CustomExc_CommonCheck_00() {
    Check.that(6).has(i -> 4.5d,
        GTE(),
        9.9,
        () -> new UnsupportedOperationException());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void has_Relation_CustomExc_CustomCheck_00() {
    Check.that(6).has(i -> 4.5d,
        myGTE(),
        9.9,
        () -> new UnsupportedOperationException());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void notHas_Relation_CustomExc_CommonCheck_00() {
    Check.that(6).notHas(i -> 4.5d,
        LT(),
        9.9,
        () -> new UnsupportedOperationException());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void notHas_Relation_CustomExc_CustomCheck_00() {
    Check.that(6).notHas(i -> 4.5d,
        myLT(),
        9.9,
        () -> new UnsupportedOperationException());
  }

}
