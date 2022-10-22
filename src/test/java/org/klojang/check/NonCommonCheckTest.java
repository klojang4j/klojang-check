package org.klojang.check;

import org.junit.Test;
import org.klojang.check.relation.Comparison;
import org.klojang.check.relation.IntObjRelation;
import org.klojang.check.relation.IntRelation;

import java.util.function.ToIntFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.relation.ComposeMethods.*;
import static org.klojang.check.x.msg.MsgUtil.defaultPredicateMessage;
import static org.klojang.check.x.msg.MsgUtil.defaultRelationMessage;

/*
 * Covers situations where the client provided a "property" that was not from the
 * CommonProperties class, or a check that was not from the CommonChecks class, or
 * both, or neither. This is a high-quality test class testing all permutations. It
 * probably makes quite a few tests scattered across other test classes redundant.
 */
public class NonCommonCheckTest {

  private static Comparison<Double> NO_RELATION = (s, o) -> false;
  private static IntRelation NO_INT_RELATION = (s, o) -> false;

  private static IntObjRelation<Double> NO_INT_OBJ_RELATION = (s, o) -> false;

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonCheckNoArgName00() {
    try {
      Check.that(23.5).is(invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage(null, 23.5));
      assertEquals(defaultPredicateMessage(null, 23.5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntCheckNoArgName00() {
    try {
      Check.that(2).is(invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage(null, 2));
      assertEquals(defaultPredicateMessage(null, 2), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonCheckNoArgName01() {
    try {
      Check.that(23.5).is(NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 23.5, 22.7));
      assertEquals(defaultRelationMessage(null, 23.5, 22.7), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntCheckNoArgName01() {
    try {
      Check.that(2).is(NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 2, 5));
      assertEquals(defaultRelationMessage(null, 2, 5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntCheckNoArgName02() {
    try {
      Check.that(2).is(NO_INT_OBJ_RELATION, 23.5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 2, 23.5));
      assertEquals(defaultRelationMessage(null, 2, 23.5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonCheckWithArgName00() {
    try {
      Check.that(23.5, "foo").is(invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo", 23.5));
      assertEquals(defaultPredicateMessage("foo", 23.5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntCheckWithArgName00() {
    try {
      Check.that(2, "foo").is(invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo", 2));
      assertEquals(defaultPredicateMessage("foo", 2), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonCheckWithArgName01() {
    try {
      Check.that(23.5, "foo").is(NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 23.5, 22.7));
      assertEquals(defaultRelationMessage("foo", 23.5, 22.7), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntCheckWithArgName01() {
    try {
      Check.that(2, "foo").is(NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 5));
      assertEquals(defaultRelationMessage("foo", 2, 5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntCheckWithArgName02() {
    try {
      Check.that(2, "foo").is(NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 5));
      assertEquals(defaultRelationMessage("foo", 2, 5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntCheckWithArgName03() {
    try {
      Check.that(2, "foo").is(NO_INT_OBJ_RELATION, 23.5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 23.5));
      assertEquals(defaultRelationMessage("foo", 2, 23.5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckNoArgName00() {
    try {
      Check.that(23.5).has(x -> 23.5, invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("Function.apply(23.5)", 23.5));
      assertEquals(defaultPredicateMessage("Function.apply(23.5)", 23.5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckNoArgNameWithPropName00() {
    try {
      Check.that(23.5).has(x -> 23.5, "bar", invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("bar", 23.5));
      assertEquals(
          defaultPredicateMessage("bar", 23.5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckNoArgName00() {
    try {
      Check.that(2).has(x -> 2, invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2));
      assertEquals(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckNoArgNameWithPropName00() {
    try {
      Check.that(2).has(x -> 2, "bar", invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultPredicateMessage("bar", 2));
      assertEquals(
          defaultPredicateMessage("bar", 2),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckNoArgName01() {
    try {
      Check.that(23.5).has(x -> 23.5, NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("Function.apply(23.5)", 23.5, 22.7));
      assertEquals(
          defaultRelationMessage("Function.apply(23.5)", 23.5, 22.7),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckNoArgName02() {
    try {
      Check.that(55.24).has(x -> 2, NO_INT_OBJ_RELATION, 67.345);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("Function.apply(55.24)", 2, 67.345));
      assertEquals(
          defaultRelationMessage("Function.apply(55.24)", 2, 67.345),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckNoArgNameWithPropName02() {
    try {
      Check.that(55.24).has(x -> 2, "bar", NO_INT_OBJ_RELATION, 67.345);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("bar", 2, 67.345));
      assertEquals(
          defaultRelationMessage("bar", 2, 67.345),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckNoArgNameWithPropName01() {
    try {
      Check.that(23.5).has(x -> 23.5, "bar", NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("bar", 23.5, 22.7));
      assertEquals(
          defaultRelationMessage("bar", 23.5, 22.7),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckNoArgName01() {
    try {
      Check.that(2).has(x -> 2, NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5));
      assertEquals(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckNoArgNameWithPropName01() {
    try {
      Check.that(2).has(x -> 2, "bar", NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("bar", 2, 5));
      assertEquals(
          defaultRelationMessage("bar", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckWithArgName00() {
    try {
      Check.that(23.5, "foo").has(x -> 23.5, invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("Function.apply(23.5)", 23.5));
      assertEquals(
          defaultPredicateMessage("Function.apply(23.5)", 23.5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckWithArgNameWithPropName00() {
    try {
      Check.that(23.5, "foo").has(x -> 23.5, "bar", invalid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo.bar", 23.5));
      assertEquals(
          defaultPredicateMessage("foo.bar", 23.5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckWithArgName00() {
    try {
      Check.that(2, "foo").has(x -> 2, invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2));
      assertEquals(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckWithArgNameWithPropName00() {
    try {
      Check.that(2, "foo").has(x -> 2, "bar", invalidInt());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo.bar", 2));
      assertEquals(
          defaultPredicateMessage("foo.bar", 2),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckWithArgName01() {
    try {
      Check.that(23.5, "foo").has(x -> 23.5, NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("Function.apply(23.5)", 23.5, 22.7));
      assertEquals(
          defaultRelationMessage("Function.apply(23.5)", 23.5, 22.7),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonHasCheckWithArgNameWithPropName01() {
    try {
      Check.that(23.5, "foo").has(x -> 23.5, "bar", NO_RELATION, 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo.bar", 23.5, 22.7));
      assertEquals(
          defaultRelationMessage("foo.bar", 23.5, 22.7),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckWithArgName01() {
    try {
      Check.that(2, "foo").has(x -> 2, NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5));
      assertEquals(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckWithArgNameWithPropName01() {
    try {
      Check.that(2, "foo").has(x -> 2, "bar", NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo.bar", 2, 5));
      assertEquals(
          defaultRelationMessage("foo.bar", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckWithArgName02() {
    try {
      Check.that(2, "foo").has(x -> 2, NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5));
      assertEquals(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntHasCheckWithArgNameWithPropName02() {
    try {
      Check.that(2, "foo").has(x -> 2, "bar", NO_INT_RELATION, 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo.bar", 2, 5));
      assertEquals(
          defaultRelationMessage("foo.bar", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotCheckNoArgName00() {
    try {
      Check.that(23.5).isNot(valid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage(null, 23.5));
      assertEquals(defaultPredicateMessage(null, 23.5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotIntCheckNoArgName00() {
    try {
      Check.that(2).isNot(validInt());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage(null, 2));
      assertEquals(defaultPredicateMessage(null, 2), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotCheckNoArgName01() {
    try {
      Check.that(23.5).isNot(NO_RELATION.negate(), 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 23.5, 22.7));
      assertEquals(defaultRelationMessage(null, 23.5, 22.7), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotIntCheckNoArgName01() {
    try {
      Check.that(2).isNot(NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 2, 5));
      assertEquals(defaultRelationMessage(null, 2, 5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotIntCheckNoArgName02() {
    try {
      Check.that(2).isNot(NO_INT_OBJ_RELATION.negate(), 23.5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage(null, 2, 23.5));
      assertEquals(defaultRelationMessage(null, 2, 23.5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotCheckWithArgName00() {
    try {
      Check.that(23.5, "foo").isNot(valid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo", 23.5));
      assertEquals(defaultPredicateMessage("foo", 23.5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotIntCheckWithArgName00() {
    try {
      Check.that(2, "foo").isNot(validInt());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo", 2));
      assertEquals(defaultPredicateMessage("foo", 2), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotCheckWithArgName01() {
    try {
      Check.that(23.5, "foo").isNot(NO_RELATION.negate(), 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 23.5, 22.7));
      assertEquals(defaultRelationMessage("foo", 23.5, 22.7), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotIntCheckWithArgName01() {
    try {
      Check.that(2, "foo").isNot(NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 5));
      assertEquals(defaultRelationMessage("foo", 2, 5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotIntCheckWithArgName02() {
    try {
      Check.that(2, "foo").isNot(NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 5));
      assertEquals(defaultRelationMessage("foo", 2, 5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIsNotIntCheckWithArgName03() {
    try {
      Check.that(2, "foo").isNot(NO_INT_OBJ_RELATION.negate(), 23.5);
    } catch (IllegalArgumentException e) {
      System.out.println(defaultRelationMessage("foo", 2, 23.5));
      assertEquals(defaultRelationMessage("foo", 2, 23.5), e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckNoArgName00() {
    try {
      Check.that(23.5).notHas(x -> 23.5, valid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("Function.apply(23.5)", 23.5));
      assertEquals(
          defaultPredicateMessage("Function.apply(23.5)", 23.5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckNoArgNameWithPropName00() {
    try {
      Check.that(23.5).notHas(x -> 23.5, "bar", valid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("bar", 23.5));
      assertEquals(
          defaultPredicateMessage("bar", 23.5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckNoArgName00() {
    try {
      Check.that(2).notHas(x -> 2, validInt());
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2));
      assertEquals(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckNoArgNameWithPropName00() {
    try {
      Check.that(2).notHas(x -> 2, "bar", validInt());
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultPredicateMessage("bar", 2));
      assertEquals(
          defaultPredicateMessage("bar", 2),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckNoArgName01() {
    try {
      Check.that(23.5).notHas(x -> 23.5, NO_RELATION.negate(), 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("Function.apply(23.5)", 23.5, 22.7));
      assertEquals(
          defaultRelationMessage("Function.apply(23.5)", 23.5, 22.7),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckNoArgNameWithPropName01() {
    try {
      Check.that(23.5).notHas(x -> 23.5, "bar", NO_RELATION.negate(), 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("bar", 23.5, 22.7));
      assertEquals(
          defaultRelationMessage("bar", 23.5, 22.7),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckNoArgName01() {
    try {
      Check.that(2).notHas(x -> 2, NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5));
      assertEquals(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckNoArgNameWithPropName01() {
    try {
      Check.that(2).notHas(x -> 2, "bar", NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("bar", 2, 5));
      assertEquals(
          defaultRelationMessage("bar", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckWithArgName00() {
    try {
      Check.that(23.5, "foo").notHas(x -> 23.5, valid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("Function.apply(23.5)", 23.5));
      assertEquals(
          defaultPredicateMessage("Function.apply(23.5)", 23.5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckWithArgNameWithPropName00() {
    try {
      Check.that(23.5, "foo").notHas(x -> 23.5, "bar", valid());
    } catch (IllegalArgumentException e) {
      System.out.println(defaultPredicateMessage("foo.bar", 23.5));
      assertEquals(
          defaultPredicateMessage("foo.bar", 23.5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckWithArgName00() {
    try {
      Check.that(2, "foo").notHas(x -> 2, validInt());
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2));
      assertEquals(
          defaultPredicateMessage("IntUnaryOperator.applyAsInt(2)", 2),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckWithArgNameWithPropName00() {
    try {
      Check.that(2, "foo").notHas(x -> 2, "bar", validInt());
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultPredicateMessage("foo.bar", 2));
      assertEquals(
          defaultPredicateMessage("foo.bar", 2),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckWithArgName01() {
    try {
      Check.that(23.5, "foo").notHas(x -> 23.5, NO_RELATION.negate(), 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("Function.apply(23.5)", 23.5, 22.7));
      assertEquals(
          defaultRelationMessage("Function.apply(23.5)", 23.5, 22.7),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckWithArgNameWithPropName01() {
    try {
      Check.that(23.5, "foo").notHas(x -> 23.5, "bar", NO_RELATION.negate(), 22.7);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("foo.bar", 23.5, 22.7));
      assertEquals(
          defaultRelationMessage("foo.bar", 23.5, 22.7),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckWithArgName01() {
    try {
      Check.that(2, "foo").notHas(x -> 2, NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5));
      assertEquals(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckWithArgNameWithPropName01() {
    try {
      Check.that(2, "foo").notHas(x -> 2, "bar", NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("foo.bar", 2, 5));
      assertEquals(
          defaultRelationMessage("foo.bar", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckWithArgName02() {
    try {
      Check.that(2, "foo").notHas(x -> 2, NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5));
      assertEquals(
          defaultRelationMessage("IntUnaryOperator.applyAsInt(2)", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckWithArgNameWithPropName02() {
    try {
      Check.that(2, "foo").notHas(x -> 2, "bar", NO_INT_RELATION.negate(), 5);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("foo.bar", 2, 5));
      assertEquals(
          defaultRelationMessage("foo.bar", 2, 5),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckWithArgName03() {
    try {
      Check.that(23.5, "foo").notHas(x -> 2, NO_INT_OBJ_RELATION.negate(), 42.56);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("Function.apply(23.5)", 2, 42.56));
      assertEquals(
          defaultRelationMessage("Function.apply(23.5)", 2, 42.56),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonIntNotHasCheckWithArgNameWithPropName03() {
    try {
      Check.that(23.5, "foo").notHas(x -> 2,
          "bar",
          NO_INT_OBJ_RELATION.negate(),
          42.56);
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("foo.bar", 2, 42.56));
      assertEquals(
          defaultRelationMessage("foo.bar", 2, 42.56),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckNoArgName02() {
    try {
      Check.that(23.5).has(
          (ToIntFunction<Double>) x -> 2,
          (IntObjRelation<? super String>) (s, o) -> false,
          "sparrow");
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("Function.apply(23.5)", 2, "sparrow"));
      assertEquals(
          defaultRelationMessage("Function.apply(23.5)", 2, "sparrow"),
          e.getMessage());
      throw e;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonCommonNotHasCheckNoArgNameWithPropName02() {
    try {
      Check.that(23.5).has(
          (ToIntFunction<Double>) x -> 2,
          "bar",
          (IntObjRelation<? super String>) (s, o) -> false,
          "sparrow");
    } catch (IllegalArgumentException e) {
      System.out.println(
          defaultRelationMessage("bar", 2, "sparrow"));
      assertEquals(
          defaultRelationMessage("bar", 2, "sparrow"),
          e.getMessage());
      throw e;
    }
    fail();
  }

}
