package nl.naturalis.check.types;

import nl.naturalis.check.Check;
import org.junit.Test;

import java.util.List;

import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.util.Quantifier.*;
import static nl.naturalis.check.types.ComposableIntPredicate.*;

public class ComposableIntPredicateTest {

  @Test
  public void validIntIf00() {
    Check.that(27).is(validIntIf(lt(), 30));
  }

  @Test(expected = IllegalArgumentException.class)
  public void validIntIf01() {
    Check.that(27).is(validIntIf(lt(), 20));
  }

  @Test
  public void validIntIf02() {
    Check.that(1).is(validIntIf(indexOf(), List.of(1, 2, 3)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void validIntIf03() {
    Check.that(27).is(validIntIf(indexOf(), List.of(1, 2, 3)));
  }

  @Test
  public void negated00() {
    Check.that(27).is(even().negated());
  }

  @Test(expected = IllegalArgumentException.class)
  public void negated01() {
    Check.that(27).is(odd().negated());
  }

  @Test
  public void orElsePredicate00() {
    Check.that(23).is(even().orElse(odd()));
    Check.that(23).is(odd().orElse(even()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orElsePredicate01() {
    Check.that(43).is(even().orElse(i -> i == 23));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orElsePredicate02() {
    Check.that(42).is(ComposableIntPredicate.validIntIf(i -> i == 20)
        .orElse(i -> i % 20 == 0));
  }

  @Test
  public void orElseRelation00() {
    Check.that(23).is(even().orElse(lt(), 50));
  }

  @Test
  public void orElseRelation01() {
    Check.that(23).is(odd().orElse(gt(), 50));
  }

  @Test
  public void orElseRelation02() {
    Check.that(23).is(even().orElse(gt(), 30).orElse(lt(), 50));
  }

  @Test
  public void orThatBoolean00() {
    Check.that(9).is(validIntIf(lt(), 10).or(10 > 11));
  }

  @Test
  public void orThatBoolean01() {
    Check.that(9).is(validIntIf(gt(), 8).or(10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orThatBoolean02() {
    Check.that(9).is(validIntIf(lt(), 8).or(12 < 11));
  }

  @Test
  public void orEval00() {
    Check.that(9).is(validIntIf(lt(), 10).orEval(() -> 10 > 11));
  }

  @Test
  public void orEval01() {
    Check.that(9).is(validIntIf(lt(), 8).orEval(() -> 10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orEval02() {
    Check.that(9).is(validIntIf(lt(), 8).orEval(() -> 10 > 11));
  }

  @Test
  public void orNotPredicate01() {
    Check.that(23).is(even().orNot(zero()));
  }

  @Test
  public void orNotPredicate00() {
    Check.that(8).is(validIntIf(lt(), 10).orNot(x -> (int) x % 2 == 0));
    Check.that(8).is(validIntIf(gt(), 10).orNot(x -> (int) x % 3 == 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate02() {
    Check.that(42).is(zero().orNot(even()));
  }

  @Test
  public void orNotRelation00() {
    Check.that(23).is(odd().orNot(gt(), 7));
    Check.that(23).is(even().orNot(eq(), 7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotRelation02() {
    Check.that(42).is(validIntIf(i -> i > 50).orNot(i -> i % 2 == 0));
  }

  @Test
  public void orAny00() {
    Check.that(77).is(even().or(lt(), anyOf(), 1, 2, 3, 4, 88));
  }

  @Test
  public void orAny01() {
    Check.that(7).is(odd().or(lt(), anyOf(), 1, 2, 3, 4, 88));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny02() {
    Check.that(7).is(even().or(lt(), anyOf(), 1, 2, 3, 4));
  }

  @Test
  public void orAll00() {
    Check.that(18).is(invalidInt().or(gt(), allOf(), 1, 2, 3, 4));
  }

  @Test
  public void orAll01() {
    Check.that(18).is(even().or(lt(), allOf(), 1, 2, 3, 4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAll02() {
    Check.that(18).is(odd().or(lt(), allOf(), 1, 2, 3, 4));
  }

  @Test
  public void orNone00() {
    Check.that(18).is(even().or(lt(), noneOf(), 1, 2, 3, 100));
  }

  @Test
  public void orNone01() {
    Check.that(18).is(odd().or(lt(), noneOf(), 1, 2, 3, 4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNone02() {
    Check.that(18).is(odd().or(lt(), noneOf(), 1, 2, 3, 200));
  }

  @Test
  public void orThatPredicate100() {
    Check.that(28).is(odd().orThat("bar", s -> s.length() < 100));
    Check.that(27).is(odd().orThat("bar", s -> s.length() > 100));
  }

  @Test
  public void orThatPredicate101() {
    Check.that(28).is(odd().orThat(2, i -> i < 3));
    Check.that(27).is(odd().orThat(2, i -> i > 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orThatPredicate102() {
    Check.that(28).is(odd().orThat(3, even()));
  }

  @Test
  public void orThatRelation100() {
    Check.that(28).is(odd().orThat("aaa", LTE(), "bbb"));
    Check.that(28).is(even().orThat("aaa", GTE(), "bbb"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orThatRelation101() {
    Check.that(28).is(odd().orThat("aaa", GTE(), "bbb"));
  }

  @Test
  public void orThatIntRelation100() {
    Check.that(28).is(odd().orThat(1, ne(), 2));
    Check.that(28).is(even().orThat(1, eq(), 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orThatIntRelation101() {
    Check.that(28).is(odd().orThat(1, eq(), 2));
  }

  @Test
  public void orNotPredicate100() {
    Check.that(23).is(even().orNot(40F, f -> f > 50F));
    Check.that(23).is(odd().orNot(17F, EQ(), 17F));
  }

  @Test
  public void orNotPredicate101() {
    Check.that(23).is(even().orNot(40F, f -> f > 50F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate102() {
    Check.that(23).is(even().orNot(40F, f -> f < 50F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate103() {
    Check.that(42).is(invalidInt().orNot("", empty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate104() {
    Check.that(42).is(validIntIf(odd()).orNot("", empty()));
  }

  @Test
  public void orNotIntPredicate100() {
    Check.that(23).is(odd().orNot(1, even()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotIntPredicate101() {
    Check.that(23).is(even().orNot(1, odd()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotIntPredicate102() {
    Check.that(23).is(even().orNot(1, odd()));
  }

  @Test
  public void orNotRelation100() {
    Check.that(23).is(odd().orNot(17F, EQ(), 17F));
  }

  @Test
  public void orNotRelation101() {
    Check.that(23).is(even().orNot(17F, EQ(), 18F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotRelation102() {
    Check.that(23).is(even().orNot(17F, EQ(), 17F));
  }

  @Test
  public void orAllIntRelation100() {
    Check.that(23).is(even().or(10, gt(), allOf(), 1, 2, 3, 4, 5));
  }

  @Test
  public void orAllIntRelation101() {
    Check.that(23).is(odd().or(10, gt(), allOf(), 1, 2, 3, 4, 100));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAllIntRelation102() {
    Check.that(23).is(even().or(10, gt(), allOf(), 1, 2, 3, 4, 100));
  }

  @Test
  public void orAnyIntRelation100() {
    Check.that(23).is(odd().or(10, lt(), anyOf(), 1, 2, 3, 4, 5));
  }

  @Test
  public void orAnyIntRelation101() {
    Check.that(23).is(even().or(10, gt(), anyOf(), 1, 2, 3, 4, 100));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAnyIntRelation102() {
    Check.that(23).is(even().or(10, lt(), anyOf(), 1, 2, 3, 4));
  }

  @Test
  public void orNoneIntRelation100() {
    Check.that(23).is(odd().or(10, lt(), noneOf(), 1, 2, 3, 4, 5));
  }

  @Test
  public void orNoneIntRelation101() {
    Check.that(23).is(even().or(10, lt(), noneOf(), 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNoneIntRelation102() {
    Check.that(23).is(even().or(10, gt(), noneOf(), 1, 2, 3, 4, 5));
  }

  @Test
  public void orAllRelation100() {
    Check.that(23).is(even().or(10F, GT(), allOf(), 1F, 2F, 3F, 4F, 5F));
  }

  @Test
  public void orAllRelation101() {
    Check.that(23).is(odd().or(10F, GT(), allOf(), 1F, 2F, 3F, 4F, 100F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAllRelation102() {
    Check.that(23).is(even().or(10F, GT(), allOf(), 1F, 2F, 3F, 4F, 100F));
  }

  @Test
  public void orAnyRelation100() {
    Check.that(23).is(odd().or(10F, LT(), anyOf(), 1F, 2F, 3F, 4F, 5F));
  }

  @Test
  public void orAnyRelation101() {
    Check.that(23).is(even().or(10F, GT(), anyOf(), 1F, 2F, 3F, 4F, 100F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAnyRelation102() {
    Check.that(23).is(even().or(10F, LT(), anyOf(), 1F, 2F, 3F, 4F));
  }

  @Test
  public void orNoneRelation100() {
    Check.that(23).is(odd().or(10F, LT(), noneOf(), 1F, 2F, 3F, 4F, 5F));
  }

  @Test
  public void orNoneRelation101() {
    Check.that(23).is(even().or(10F, LT(), noneOf(), 1F, 2F, 3F, 4F, 5F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNoneRelation102() {
    Check.that(23).is(even().or(10F, GT(), noneOf(), 1F, 2F, 3F, 4F, 5F));
  }

  @Test
  public void andAlsoIntPredicate00() {
    Check.that(23).is(odd().andAlso(i -> i > 20));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAlsoIntPredicate01() {
    Check.that(43).is(even().andAlso(i -> i == 43));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAlsoIntPredicate02() {
    Check.that(43).is(odd().andAlso(i -> i == 42));
  }

  @Test
  public void andAlsoIntRelation00() {
    Check.that(23).is(odd().andAlso(lt(), 50));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAlsoIntRelation01() {
    Check.that(23).is(odd().andAlso(gt(), 50));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAlsoIntRelation02() {
    Check.that(23).is(invalidInt().andAlso(lt(), 50));
  }

  @Test
  public void andIntPredicate00() {
    Check.that(9).is(odd().and(i -> i % 3 == 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andIntPredicate01() {
    Check.that(9).is(even().and(i -> i % 3 == 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andIntPredicate02() {
    Check.that(9).is(odd().and(i -> i % 2 == 0));
  }

  @Test
  public void andThatBoolean00() {
    Check.that(9).is(odd().and(10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatBoolean01() {
    Check.that(9).is(even().and(10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatBoolean02() {
    Check.that(9).is(odd().and(12 < 11));
  }

  @Test
  public void andEval00() {
    Check.that(9).is(validIntIf(lt(), 10).andEval(() -> 10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andEval01() {
    Check.that(9).is(validIntIf(lt(), 10).andEval(() -> 12 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andEval02() {
    Check.that(9).is(validIntIf(lt(), 8).andEval(() -> 10 < 11));
  }

  @Test
  public void andNotPredicate00() {
    Check.that(2).is(even().andNot(zero()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotPredicate01() {
    Check.that(42).is(zero().andNot(even()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotPredicate02() {
    Check.that(0).is(validInt().andNot(zero()));
  }

  @Test
  public void andNotRelation00() {
    Check.that(23).is(odd().andNot(lt(), 7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation01() {
    Check.that(42).is(odd().andNot(multipleOf(), 87));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation02() {
    Check.that(42).is(validIntIf(i -> i > 50).andNot(i -> i % 2 == 0));
  }

  @Test
  public void andAny00() {
    Check.that(77).is(odd().and(lt(), anyOf(), 1, 2, 3, 4, 88));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny01() {
    Check.that(77).is(even().and(lt(), anyOf(), 1, 2, 3, 4, 88));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny02() {
    Check.that(77).is(odd().and(lt(), anyOf(), 1, 2, 3, 4));
  }

  @Test
  public void andAll00() {
    Check.that(18).is(validInt().and(gt(), allOf(), 1, 2, 3, 4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll01() {
    Check.that(18).is(invalidInt().and(gt(), allOf(), 1, 2, 3, 4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll02() {
    Check.that(18).is(even().and(gt(), allOf(), 1, 2, 3, 100));
  }

  @Test
  public void andNone00() {
    Check.that(18).is(even().and(multipleOf(), noneOf(), 7, 11, 13));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone01() {
    Check.that(18).is(odd().and(multipleOf(), noneOf(), 7, 11, 13));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone02() {
    Check.that(18).is(even().and(multipleOf(), noneOf(), 3, 6, 10));
  }

  @Test
  public void andThatPredicate100() {
    Check.that(28).is(even().andThat("bar", s -> s.length() < 100));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatPredicate101() {
    Check.that(28).is(odd().andThat("bar", deepNotEmpty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatPredicate102() {
    Check.that(28).is(even().andThat("bar", blank()));
  }

  @Test
  public void andThatIntPredicate100() {
    Check.that(28).is(even().andThat(2, even()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatIntPredicate101() {
    Check.that(28).is(odd().andThat(2, even()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatIntPredicate102() {
    Check.that(28).is(even().andThat(2, odd()));
  }

  @Test
  public void andThatRelation100() {
    Check.that(28).is(even().andThat("aaa", LTE(), "bbb"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatRelation101() {
    Check.that(28).is(odd().andThat("aaa", LTE(), "bbb"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatRelation102() {
    Check.that(28).is(even().andThat("aaa", GTE(), "bbb"));
  }

  @Test
  public void andThatIntRelation100() {
    Check.that(28).is(even().andThat(1, lt(), 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatIntRelation101() {
    Check.that(28).is(odd().andThat(1, lt(), 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatIntRelation102() {
    Check.that(28).is(even().andThat(2, lt(), 1));
  }

  @Test
  public void andNotIntPredicate100() {
    Check.that(28).is(even().andNot(1, i -> i == 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotIntPredicate101() {
    Check.that(28).is(even().andNot(1, i -> i == 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotIntPredicate102() {
    Check.that(28).is(even().negated().andNot(1, i -> i == 0));
  }

  @Test
  public void andNotPredicate100() {
    Check.that(28).is(even().andNot(3F, f -> f == 4F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotPredicate101() {
    Check.that(28).is(even().andNot(3F, f -> f == 3F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotPredicate102() {
    Check.that(28).is(odd().andNot(3F, f -> f == 4F));
  }

  @Test
  public void andNotRelation100() {
    Check.that(23).is(odd().andNot(17F, EQ(), 18F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation101() {
    Check.that(23).is(odd().andNot(17F, EQ(), 17F));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation102() {
    Check.that(23).is(even().andNot(17F, EQ(), 18F));
  }

  @Test
  public void andNotIntRelation100() {
    Check.that(23).is(odd().andNot(17, eq(), 18));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotIntRelation101() {
    Check.that(23).is(even().andNot(17, eq(), 17));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotIntRelation102() {
    Check.that(23).is(odd().andNot(17, eq(), 17));
  }

  @Test
  public void andAllIntRelation100() {
    Check.that(23).is(odd().and(10, gt(), allOf(), 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAllIntRelation101() {
    Check.that(23).is(even().and(10, gt(), allOf(), 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAllIntRelation102() {
    Check.that(23).is(odd().and(10, gt(), allOf(), 1, 2, 3, 4, 10));
  }

  @Test
  public void andAnyIntRelation100() {
    Check.that(23).is(odd().and(10, lt(), anyOf(), 1, 2, 3, 4, 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAnyIntRelation101() {
    Check.that(23).is(zero().and(10, lt(), anyOf(), 1, 2, 3, 4, 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAnyIntRelation102() {
    Check.that(23).is(odd().and(10, lt(), anyOf(), 1, 2, 3, 4, 5));
  }

  @Test
  public void andNoneIntRelation100() {
    Check.that(23).is(odd().and(10, lt(), noneOf(), 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNoneIntRelation101() {
    Check.that(23).is(even().and(10, lt(), noneOf(), 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNoneIntRelation102() {
    Check.that(23).is(odd().and(10, lt(), noneOf(), 1, 2, 3, 4, 100));
  }

  @Test
  public void andAllRelation100() {
    Check.that(23).is(odd().and(List.of(1, 2, 3, 4), contains(), allOf(), 1, 2, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAllRelation101() {
    Check.that(23).is(even().and(List.of(1, 2, 3, 4), contains(), allOf(), 1, 2, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAllRelation102() {
    Check.that(23).is(odd().and(List.of(1, 2, 3, 4), contains(), allOf(), 1, 2, 7));
  }

  @Test
  public void andAnyRelation100() {
    Check.that(23).is(odd()
        .and(List.of(1, 2, 3, 4), contains(), anyOf(), 1, 10, 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAnyRelation101() {
    Check.that(23).is(even()
        .and(List.of(1, 2, 3, 4), contains(), anyOf(), 1, 10, 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAnyRelation102() {
    Check.that(23).is(odd()
        .and(List.of(1, 2, 3, 4), contains(), anyOf(), 9, 10, 11));
  }

}
