package org.klojang.check;

import org.junit.Test;
import org.klojang.check.relation.Comparison;
import org.klojang.check.relation.IntObjRelation;
import org.klojang.check.relation.IntRelation;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.relation.ComposeMethods.invalid;
import static org.klojang.check.relation.ComposeMethods.invalidInt;
import static org.klojang.check.x.msg.MsgUtil.defaultPredicateMessage;
import static org.klojang.check.x.msg.MsgUtil.defaultRelationMessage;

public class NonCommonCheckTest {

  private static Comparison<Double> NO_RELATION = (s, o) -> false;
  private static IntRelation NO_INT_RELATION = (s, o) -> false;

  private static IntObjRelation<Double> NO_INT_OBJ_RELATION = (s, o) -> false;

  @Test
  public void nonCommonCheckNoArgName00() {
    try {
      Check.that(23.5).is(invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage(null, 23.5));
      assertEquals(defaultPredicateMessage(null, 23.5), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonIntCheckNoArgName00() {
    try {
      Check.that(2).is(invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage(null, 2));
      assertEquals(defaultPredicateMessage(null, 2), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonCheckNoArgName01() {
    try {
      Check.that(23.5).is(NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 23.5, 22.7));
      assertEquals(defaultRelationMessage(null, 23.5, 22.7), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonIntCheckNoArgName01() {
    try {
      Check.that(2).is(NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 2, 5));
      assertEquals(defaultRelationMessage(null, 2, 5), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonIntCheckNoArgName02() {
    try {
      Check.that(2).is(NO_INT_OBJ_RELATION, 23.5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 2, 23.5));
      assertEquals(defaultRelationMessage(null, 2, 23.5), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonCheckWithArgName00() {
    try {
      Check.that(23.5, "foo").is(invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo", 23.5));
      assertEquals(defaultPredicateMessage("foo", 23.5), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonIntCheckWithArgName00() {
    try {
      Check.that(2, "foo").is(invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo", 2));
      assertEquals(defaultPredicateMessage("foo", 2), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonCheckWithArgName01() {
    try {
      Check.that(23.5, "foo").is(NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 23.5, 22.7));
      assertEquals(defaultRelationMessage("foo", 23.5, 22.7), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonIntCheckWithArgName01() {
    try {
      Check.that(2, "foo").is(NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 5));
      assertEquals(defaultRelationMessage("foo", 2, 5), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonIntCheckWithArgName02() {
    try {
      Check.that(2, "foo").is(NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 5));
      assertEquals(defaultRelationMessage("foo", 2, 5), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonIntCheckWithArgName03() {
    try {
      Check.that(2, "foo").is(NO_INT_OBJ_RELATION, 23.5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 23.5));
      assertEquals(defaultRelationMessage("foo", 2, 23.5), e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasCheckNoArgName00() {
    try {
      Check.that(23.5).has(x -> 23.5, invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("Function.apply(23.5)", 23.5));
      assertEquals(defaultPredicateMessage("Function.apply(23.5)", 23.5),
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasIntHasCheckNoArgName00() {
    try {
      Check.that(2).has(x -> 2, invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2));
      assertEquals(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2),
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasCheckNoArgName01() {
    try {
      Check.that(23.5).has(x -> 23.5, NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, "Function.apply(23.5)", 22.7));
      assertEquals(
          defaultRelationMessage(null, "Function.apply(23.5)", 22.7),
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasIntHasCheckNoArgName01() {
    try {
      Check.that(2).has(x -> 2, NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage(null, "IntUnaryOperator.applyAsInt(2)", 5));
      assertEquals(
          defaultRelationMessage(null, "IntUnaryOperator.applyAsInt(2)", 5),
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasCheckWithArgName00() {
    try {
      Check.that(23.5, "foo").has(x -> 23.5, invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("Function.apply(23.5)", 23.5));
      assertEquals(
          defaultPredicateMessage("Function.apply(23.5)", 23.5),
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasIntHasCheckWithArgName00() {
    try {
      Check.that(2, "foo").has(x -> 2, invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)",
          2));
      assertEquals(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2),
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasCheckWithArgName01() {
    try {
      Check.that(23.5, "foo").has(x -> 23.5, NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, "Function.apply(23.5)", 22.7));
      assertEquals(
          defaultRelationMessage(null, "Function.apply(23.5)", 22.7),
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasIntHasCheckWithArgName01() {
    try {
      Check.that(2, "foo").has(x -> 2, NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null,
          "IntUnaryOperator.applyAsInt(2)",
          5));
      assertEquals(
          defaultRelationMessage(null, "IntUnaryOperator.applyAsInt(2)", 5),
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nonCommonHasIntHasCheckWithArgName02() {
    try {
      Check.that(2, "foo").has(x -> 2, NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null,
          "IntUnaryOperator.applyAsInt(2)",
          5));
      assertEquals(
          defaultRelationMessage(null, "IntUnaryOperator.applyAsInt(2)", 5),
          e.getMessage());
      return;
    }
    fail();
  }

}
