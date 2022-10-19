package org.klojang.check.relation;

import org.junit.Test;
import org.klojang.check.Check;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.relation.ComposeMethods.validIf;
import static org.klojang.check.relation.Quantifier.*;

public class ComposablePredicateTest {

  @Test
  public void orElsePredicate00() {
    Check.that(List.of("foo", "bar")).is(empty().orElse(notEmpty()));
    Check.that(List.of("foo", "bar")).is(notEmpty().orElse(empty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orElsePredicate01() {
    Check.that("hello world").is(empty().orElse((String s) -> s.charAt(1) == 'f'));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orElsePredicate02() {
    Check.that("hello world")
        .is(validIf((String s) -> s.charAt(1) == 'f')
            .orElse((String s) -> s.charAt(1) == 'g'));
  }

  @Test(expected = ClassCastException.class)
  public void orElsePredicate03() {
    Check.that(List.of("foo", "bar")).is(file().orElse(notEmpty()));
  }

  @Test
  public void orElseRelation00() {
    Check.that(List.of("foo", "bar")).is(empty().orElse(contains(), "foo"));
  }

  @Test
  public void orElseRelation01() {
    Check.that(List.of()).is(empty().orElse(contains(), "foo"));
  }

  @Test
  public void orElseRelation02() {
    Check.that(List.of("foo", "bar")).is(empty().orElse(contains(), 30)
        .orElse(contains(), "foo"));
  }

  @Test(expected = ClassCastException.class)
  public void orElseRelation03() {
    Check.that(List.of("foo", "bar")).is(empty().orElse(hasSubstring(), "foo"));
  }

  @Test
  public void orThatBoolean00() {
    Check.that(9.3).is(validIf(LT(), 10D).or(10 > 11));
  }

  @Test
  public void orThatBoolean01() {
    Check.that(9.3).is(validIf(LT(), 8D).or(10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orThatBoolean02() {
    Check.that(9.3).is(validIf(LT(), 8D).or(10 > 11));
  }

  @Test
  public void orEval00() {
    Check.that(9.3).is(validIf(LT(), 10D).orEval(() -> 10 > 11));
  }

  @Test
  public void orEval01() {
    Check.that(9.3).is(validIf(LT(), 8D).orEval(() -> 10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orEval02() {
    Check.that(9.3).is(validIf(LT(), 8D).orEval(() -> 10 > 11));
  }

  @Test
  public void orNotPredicate01() {
    Check.that(List.of("foo", "bar")).is(empty().orNot(NULL()));
    Check.that(List.of("foo", "bar"))
        .is(notEmpty().orNot(blank())); // Yes, we can do this !!
  }

  @Test
  public void orNotPredicate00() {
    Check.that((Integer) 8).is(validIf(LT(), 10).orNot(x -> (int) x % 2 == 0));
    Check.that((Integer) 8).is(validIf(GT(), 10).orNot(x -> (int) x % 3 == 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate02() {
    Check.that("hello world").is(validIf((String s) -> s.length()
        > 100)
        .orNot((String s) -> s.charAt(1) == 'e'));
  }

  @Test
  public void orNotRelation00() {
    Check.that(List.of("foo", "bar")).is(empty().orNot(contains(), "bozo"));
    Check.that(List.of("foo", "bar")).is(notEmpty().orNot(contains(), "bozo"));
  }

  @Test(expected = ClassCastException.class)
  public void orNotRelation01() {
    Check.that(42F).is(validIf((Float f) -> f > 50F)
        .orNot(contains(), "bozo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotRelation02() {
    Check.that(42L).is(validIf((Long l) -> l > 50)
        .orNot((Long l) -> l % 2 == 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotRelation03() {
    Check.that(42L).is(validIf(GTE(), 50L).orNot(60, LT(), 70));
  }

  @Test
  public void orAny00() {
    Check.that("hello, world")
        .is(ComposeMethods.invalid().or(hasSubstring(), anyOf(), "foo", "world"));
  }

  @Test
  public void orAny01() {
    Check.that("hello, world")
        .is(notEmpty().or(hasSubstring(), anyOf(), "foo", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny02() {
    Check.that("hello, world")
        .is(empty().or(hasSubstring(), anyOf(), "foo", "bar"));
  }

  @Test
  public void orAll00() {
    Check.that("hello, world")
        .is(ComposeMethods.invalid().or(hasSubstring(), allOf(), "hello", "world"));
  }

  @Test
  public void orAll01() {
    Check.that("hello, world")
        .is(notEmpty().or(hasSubstring(), allOf(), "foo", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAll02() {
    Check.that("hello, world")
        .is(empty().or(hasSubstring(), allOf(), "hello", "bar"));
  }

  @Test
  public void orNone00() {
    Check.that("hello, world")
        .is(ComposeMethods.invalid().or(hasSubstring(), noneOf(), "foo", "bar"));
  }

  @Test
  public void orNone01() {
    Check.that("hello, world")
        .is(notEmpty().or(hasSubstring(), noneOf(), "hello", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNone02() {
    Check.that("hello, world")
        .is(empty().or(hasSubstring(), noneOf(), "hello", "bar"));
  }

  @Test
  public void orThatPredicate00() {
    Check.that("foo").is(blank().orThat("bar", s -> s.length() < 100));
    Check.that("   ").is(blank().orThat("bar", s -> s.length() < 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orThatPredicate01() {
    Check.that("foo").is(blank().orThat("bar", s -> s.length() > 100));
  }

  @Test
  public void orThatRelation00() {
    Check.that("foo").is(blank().orThat("bar", hasSubstring(), "ba"));
    Check.that("   ").is(blank().orThat("bar", hasSubstring(), "ba"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orThatRelation01() {
    Check.that("foo").is(blank().orThat("bar", hasSubstring(), "foo"));
  }

  @Test
  public void orNotPredicate100() {
    Check.that(List.of("foo", "bar"))
        .is(empty().orNot("hello", s -> s.length() > 100));
    Check.that(List.of("foo", "bar"))
        .is(notEmpty().orNot("hello", EQ(), "hello"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate101() {
    Check.that(42L).is(ComposeMethods.invalid().orNot("", empty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate102() {
    Check.that(42L).is(validIf(GTE(), 100L).orNot("", empty()));
  }

  @Test
  public void orNotRelation101() {
    Check.that(List.of("foo", "bar")).is(empty()
        .orNot("hello", hasSubstring(), "world"));
    Check.that(List.of("foo", "bar")).is(notEmpty()
        .orNot("hello", hasSubstring(), "hell"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotRelation102() {
    Check.that(Year.of(2001)).is(validIf(GT(), Year.of(3000))
        .orNot("foo", substringOf(), "foo bar"));
  }

  @Test
  public void orAll100() {
    Check.that(LocalDate.now()).is(NULL()
        .or("hello, world", hasSubstring(), allOf(), "hello", "world"));
    Check.that(LocalDate.now()).is(notNull()
        .or("foo, world", hasSubstring(), allOf(), "hello", "world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAll101() {
    Check.that(42F).is(validIf(GTE(), 100F).or(10, GT(), allOf(), 1, 2, 3, 200));
  }

  @Test
  public void orAll102() {
    Check.that(LocalDate.now()).is(NULL().or(10, gt(), allOf(), 1, 2, 3, 4));
  }

  @Test
  public void orAll103() {
    Check.that(LocalDate.now()).is(notNull().or(10, gt(), allOf(), 1, 2, 3, 200));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAll104() {
    Check.that(LocalDate.now()).is(NULL().or(10, gt(), allOf(), 1, 2, 3, 200));
  }

  @Test
  public void orAny100() {
    Check.that(LocalDate.now())
        .is(NULL().or("hello, world", hasSubstring(), anyOf(), "hello", "foo"));
    Check.that(LocalDate.now())
        .is(notNull().or("foo, world", hasSubstring(), anyOf(), "hello", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny101() {
    Check.that(LocalDate.now())
        .is(ComposeMethods.invalid()
            .or("foo, world", hasSubstring(), anyOf(), "hello", "bar"));
  }

  @Test
  public void orAny102() {
    Check.that(LocalDate.now()).is(NULL().or(10, gt(), anyOf(), 1, 52, 53, 54));
  }

  @Test
  public void orAny103() {
    Check.that(LocalDate.now()).is(notNull().or(10, lt(), anyOf(), 1, 2, 3, 4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny104() {
    Check.that(LocalDate.now()).is(NULL().or(10, lt(), anyOf(), 1, 2, 3, 4));
  }

  @Test
  public void orNone100() {
    Check.that(LocalDate.now())
        .is(NULL().or("bar, world", hasSubstring(), noneOf(), "hello", "foo"));
    Check.that(LocalDate.now())
        .is(notNull().or("foo, world", hasSubstring(), noneOf(), "hello", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNone101() {
    Check.that(LocalDate.now())
        .is(ComposeMethods.invalid()
            .or("hello, world", hasSubstring(), noneOf(), "hello", "bar"));
  }

  @Test
  public void orNone102() {
    Check.that(LocalDate.now()).is(NULL().or(10, gt(), noneOf(), 51, 52, 53, 54));
  }

  @Test
  public void orNone103() {
    Check.that(LocalDate.now()).is(notNull().or(10, gt(), noneOf(), 1, 2, 3, 4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNone104() {
    Check.that(LocalDate.now()).is(NULL().or(10, gt(), noneOf(), 1, 52, 3, 4));
  }

  ///////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////

  @Test
  public void andAlsoPredicate00() {
    Check.that(Optional.of(3.3F)).is(notEmpty().andAlso(notNull()));
  }

  @Test(expected = ClassCastException.class)
  public void andAlsoPredicate01() {
    Check.that(List.of("foo", "bar")).is(file().andAlso(notEmpty()));
  }

  @Test
  public void andAlsoRelation00() {
    Check.that(List.of("foo", "bar"))
        .is(validIf((List l) -> l.size() < 100)
            .andAlso(contains(), "foo"));
  }

  @Test
  public void andAlsoRelation01() {
    Check.that(Year.now()).is(validIf(GT(), Year.of(2000))
        .andAlso(LT(), Year.of(3000)));
  }

  @Test
  public void andAlsoRelation02() {
    Check.that(List.of("foo", "bar"))
        .is(validIf((List l) -> l.size() < 100)
            .andAlso((List l) -> l.size() > 1)
            .andAlso(contains(), "foo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAlsoRelation03() {
    Check.that(List.of()).is(empty().andAlso(contains(), "foo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAlsoRelation04() {
    Check.that(List.of()).is(notEmpty().andAlso(contains(), "foo"));
  }

  @Test
  public void andAlsoBoolean00() {
    Check.that(9.3).is(validIf(LT(), 10D).and(10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAlsoBoolean01() {
    Check.that(9.3).is(validIf(LT(), 8D).and(10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAlsoBoolean02() {
    Check.that(9.3).is(validIf(LT(), 10D).and(10 > 11));
  }

  @Test
  public void andEval00() {
    Check.that(9.3).is(validIf(LT(), 10D).andEval(() -> 10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andEval01() {
    Check.that(9.3).is(validIf(LT(), 8D).andEval(() -> 10 < 11));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andEval02() {
    Check.that(9.3).is(validIf(LT(), 10D).andEval(() -> 10 > 11));
  }

  @Test
  public void andNotPredicate00() {
    Check.that(List.of("foo", "bar")).is(notEmpty().andNot(NULL()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotPredicate01() {
    Check.that(List.of("foo", "bar")).is(
        notEmpty().andNot((List list) -> list.size() < 100));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotPredicate02() {
    Check.that(List.of("foo", "bar")).is(
        validIf((List<String> list) -> list.size() > 100)
            .andNot(empty()));
  }

  @Test
  public void andNotRelation00() {
    Check.that(List.of("foo", "bar")).is(notEmpty().andNot(contains(), "bozo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation01() {
    Check.that(List.of("foo", "bar")).is(empty().andNot(contains(), "bozo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation02() {
    Check.that(List.of("foo", "bar")).is(notEmpty().andNot(contains(), "foo"));
  }

  @Test
  public void andAny00() {
    Check.that("hello, world")
        .is(ComposeMethods.valid().and(hasSubstring(), anyOf(), "foo", "world"));
  }

  @Test
  public void andAny01() {
    Check.that("hello, world")
        .is(notEmpty().and(hasSubstring(), anyOf(), "wor", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny02() {
    Check.that("hello, world")
        .is(empty().and(hasSubstring(), anyOf(), "foo", "bar"));
  }

  @Test
  public void andAll00() {
    Check.that("hello, world")
        .is(ComposeMethods.valid().and(hasSubstring(), allOf(), "hello", "world"));
  }

  @Test
  public void andAll01() {
    Check.that("hello, world").is(
        notEmpty().and(hasSubstring(), allOf(), "hel", "wor"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll02() {
    Check.that("hello, world")
        .is(empty().and(hasSubstring(), allOf(), "hello", "bar"));
  }

  @Test
  public void andNone00() {
    Check.that("hello, world")
        .is(ComposeMethods.valid().and(hasSubstring(), noneOf(), "foo", "bar"));
  }

  @Test
  public void andNone01() {
    Check.that("hello, world")
        .is(notEmpty().and(hasSubstring(), noneOf(), "foo", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone02() {
    Check.that("hello, world").is(validIf((String s) -> s.length() < 100)
        .and(hasSubstring(), noneOf(), "hello", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone03() {
    Check.that("hello, world")
        .is(notEmpty().and(hasSubstring(), noneOf(), "hell", "bar"));
  }

  @Test
  public void andThatPredicate00() {
    Check.that("foo").is(notEmpty().andThat("bar", s -> s.length() < 100));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatPredicate01() {
    Check.that("foo").is(blank().andThat("bar", s -> s.length() > 100));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatPredicate02() {
    Check.that("   ").is(notNull().andThat("bar", s -> s.length() > 100));
  }

  @Test
  public void andThatRelation00() {
    Check.that("foo").is(validIf((String s) -> s.length() < 100)
        .andThat("bar", hasSubstring(), "ba"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andThatRelation01() {
    Check.that("foo").is(blank().andThat("bar", hasSubstring(), "foo"));
  }

  @Test
  public void andNotPredicate100() {
    Check.that(List.of("foo", "bar")).is(notEmpty().andNot("foo", blank()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotPredicate102() {
    Check.that(List.of("foo", "bar")).is(notEmpty().andNot("", empty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotPredicate103() {
    Check.that(List.of("foo", "bar")).is(empty().andNot("foo", empty()));
  }

  @Test
  public void andNotRelation101() {
    Check.that(List.of("foo", "bar")).is(notEmpty()
        .andNot("hello", hasSubstring(), "world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation102() {
    Check.that(List.of("foo", "bar")).is(notEmpty().andNot(9.12, LT(), 42.34));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation103() {
    Check.that(List.of("foo", "bar")).is(notEmpty()
        .andNot("hello", hasSubstring(), "hell"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNotRelation104() {
    Check.that(List.of("foo", "bar")).is(empty().andNot(9.12, GT(), 42.34));
  }

  @Test
  public void andAll100() {
    Check.that(LocalDate.now())
        .is(notNull()
            .and("hello, world", hasSubstring(), allOf(), "hello", "world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll101() {
    Check.that(LocalDate.now()).is(NULL()
        .and("hello, world", hasSubstring(), allOf(), "hello", "world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll102() {
    Check.that(LocalDate.now()).is(notNull()
        .and("foo, world", hasSubstring(), allOf(), "hello", "world"));
  }

  @Test
  public void andAll103() {
    Check.that(LocalDate.now()).is(notNull()
        .and(10, gt(), allOf(), 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll104() {
    Check.that(LocalDate.now()).is(NULL().and(10, gt(), allOf(), 1, 2, 3, 4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll105() {
    Check.that(LocalDate.now()).is(notNull().and(10, gt(), allOf(), 1, 2, 11, 3));
  }

  @Test
  public void andAny100() {
    Check.that(LocalDate.now())
        .is(notNull()
            .andAlso(GT(), LocalDate.of(1800, 1, 1))
            .and("hello, world", hasSubstring(), anyOf(), "hello", "foo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny101() {
    Check.that(LocalDate.now())
        .is(ComposeMethods.valid()
            .and("foo, world", hasSubstring(), anyOf(), "hello", "bar"));
  }

  @Test
  public void andAny102() {
    Check.that(LocalDate.now()).is(notNull().and(10, gt(), anyOf(), 1, 2, 13));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny103() {
    Check.that(LocalDate.now()).is(NULL().and(10, gt(), anyOf(), 1, 2, 13));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny105() {
    Check.that(LocalDate.now()).is(notNull().and(10, lt(), anyOf(), 1, 2, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny106() {
    Check.that(LocalDate.now()).is(NULL().and(10, lt(), anyOf(), 1, 2, 3));
  }

  @Test
  public void andNone100() {
    Check.that(Year.now()).is(validIf(GT(), Year.of(2000))
        .and("bar, world", hasSubstring(), noneOf(), "hello", "foo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone101() {
    Check.that(LocalDate.now())
        .is(ComposeMethods.invalid()
            .and("hello, world", hasSubstring(), noneOf(), "hello", "bar"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone102() {
    Check.that(LocalDate.now())
        .is(ComposeMethods.valid()
            .and("foo bar", hasSubstring(), noneOf(), "hello", "bar"));
  }

  @Test
  public void andNone103() {
    Check.that(LocalDate.now()).is(notNull().and(10, lt(), noneOf(), 1, 2, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone104() {
    Check.that(LocalDate.now()).is(NULL().and(10, lt(), noneOf(), 1, 2, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone105() {
    Check.that(LocalDate.now()).is(notNull().and(10, lt(), noneOf(), 1, 20, 3));
  }

  //  private static int[] ints(int... ints) {
  //    return ints;
  //  }

}
