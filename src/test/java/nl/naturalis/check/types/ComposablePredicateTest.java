package nl.naturalis.check.types;

import nl.naturalis.check.Check;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.types.ComposablePredicate.*;

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
    Check.that("hello world").is(eval((String s) -> s.charAt(1) == 'f')
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
  public void orNotPredicate00() {
    Check.that(List.of("foo", "bar")).is(empty().orNot(NULL()));
    Check.that(List.of("foo", "bar"))
        .is(notEmpty().orNot(blank())); // Yes, we can do this !!
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate01() {
    Check.that("hello world").is(eval((String s) -> s.length() > 100)
        .orNot((String s) -> s.charAt(1) == 'e'));
  }

  @Test
  public void orNotRelation00() {
    Check.that(List.of("foo", "bar")).is(empty().orNot(contains(), "bozo"));
    Check.that(List.of("foo", "bar")).is(notEmpty().orNot(contains(), "bozo"));
  }

  @Test(expected = ClassCastException.class)
  public void orNotRelation01() {
    Check.that(42F).is(eval((Float f) -> f > 50F).orNot(contains(), "bozo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotRelation02() {
    Check.that(42L).is(eval((Long l) -> l > 50).orNot((Long l) -> l % 2 == 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotRelation03() {
    Check.that(42L).is(eval(GTE(), 50L).orNot(60, LT(), 70));
  }

  @Test
  public void orAny00() {
    Check.that("hello, world")
        .is(invalid().orAny(of("foo", "world"), substringOf()));
  }

  @Test
  public void orAny01() {
    Check.that("hello, world")
        .is(notEmpty().orAny(of("foo", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny02() {
    Check.that("hello, world")
        .is(empty().orAny(of("foo", "bar"), substringOf()));
  }

  @Test
  public void orAll00() {
    Check.that("hello, world")
        .is(invalid().orAll(of("hello", "world"), substringOf()));
  }

  @Test
  public void orAll01() {
    Check.that("hello, world")
        .is(notEmpty().orAll(of("foo", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAll02() {
    Check.that("hello, world")
        .is(empty().orAll(of("hello", "bar"), substringOf()));
  }

  @Test
  public void orNone00() {
    Check.that("hello, world")
        .is(invalid().orNone(of("foo", "bar"), substringOf()));
  }

  @Test
  public void orNone01() {
    Check.that("hello, world")
        .is(notEmpty().orNone(of("hello", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNone02() {
    Check.that("hello, world")
        .is(empty().orNone(of("hello", "bar"), substringOf()));
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
    Check.that(42L).is(invalid().orNot("", empty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNotPredicate102() {
    Check.that(42L).is(eval(GTE(), 100L).orNot("", empty()));
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
    Check.that(Year.of(2001)).is(eval(GT(), Year.of(3000))
        .orNot("foo", substringOf(), "foo bar"));
  }

  @Test
  public void orAll100() {
    Check.that(LocalDate.now())
        .is(NULL().orAll(of("hello", "world"), substringOf(), "hello, world"));
    Check.that(LocalDate.now())
        .is(notNull().orAll(of("hello", "world"), substringOf(), "foo, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAll101() {
    Check.that(42F).is(eval(GTE(), 100F).orAll(of(1, 2, 3, 4, 5), GT(), 10));
  }

  @Test
  public void orAll102() {
    Check.that(LocalDate.now()).is(NULL().orAll(ints(1, 2, 3, 4), lt(), 10));
  }

  @Test
  public void orAll103() {
    Check.that(LocalDate.now()).is(notNull().orAll(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAll104() {
    Check.that(LocalDate.now()).is(NULL().orAll(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test
  public void orAny100() {
    Check.that(LocalDate.now())
        .is(NULL().orAny(of("hello", "foo"), substringOf(), "hello, world"));
    Check.that(LocalDate.now())
        .is(notNull().orAny(of("hello", "bar"), substringOf(), "foo, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny101() {
    Check.that(LocalDate.now())
        .is(invalid().orAny(of("hello", "bar"), substringOf(), "foo, world"));
  }

  @Test
  public void orAny102() {
    Check.that(LocalDate.now()).is(NULL().orAny(ints(1, 52, 53, 54), lt(), 10));
  }

  @Test
  public void orAny103() {
    Check.that(LocalDate.now()).is(notNull().orAny(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny104() {
    Check.that(LocalDate.now()).is(NULL().orAny(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test
  public void orNone100() {
    Check.that(LocalDate.now())
        .is(NULL().orNone(of("hello", "foo"), substringOf(), "bar, world"));
    Check.that(LocalDate.now())
        .is(notNull().orNone(of("hello", "bar"), substringOf(), "foo, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNone101() {
    Check.that(LocalDate.now())
        .is(invalid().orNone(of("hello", "bar"),
            substringOf(),
            "hello, world"));
  }

  @Test
  public void orNone102() {
    Check.that(LocalDate.now()).is(NULL().orNone(ints(51, 52, 53, 54), lt(), 10));
  }

  @Test
  public void orNone103() {
    Check.that(LocalDate.now()).is(notNull().orNone(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNone104() {
    Check.that(LocalDate.now()).is(NULL().orNone(ints(1, 52, 3, 4), gt(), 10));
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
        .is(ComposablePredicate.eval((List l) -> l.size() < 100)
            .andAlso(contains(), "foo"));
  }

  @Test
  public void andAlsoRelation01() {
    Check.that(Year.now()).is(ComposablePredicate.eval(GT(), Year.of(2000))
        .andAlso(LT(), Year.of(3000)));
  }

  @Test
  public void andAlsoRelation02() {
    Check.that(List.of("foo", "bar"))
        .is(ComposablePredicate.eval((List l) -> l.size() < 100)
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
        ComposablePredicate.eval((List<String> list) -> list.size() > 100)
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
        .is(valid().andAny(of("foo", "world"), substringOf()));
  }

  @Test
  public void andAny01() {
    Check.that("hello, world")
        .is(notEmpty().andAny(of("wor", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny02() {
    Check.that("hello, world")
        .is(empty().andAny(of("foo", "bar"), substringOf()));
  }

  @Test
  public void andAll00() {
    Check.that("hello, world")
        .is(valid().andAll(of("hello", "world"), substringOf()));
  }

  @Test
  public void andAll01() {
    Check.that("hello, world")
        .is(notEmpty().andAll(of("hel", "wor"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll02() {
    Check.that("hello, world")
        .is(empty().andAll(of("hello", "bar"), substringOf()));
  }

  @Test
  public void andNone00() {
    Check.that("hello, world")
        .is(valid().andNone(of("foo", "bar"), substringOf()));
  }

  @Test
  public void andNone01() {
    Check.that("hello, world")
        .is(notEmpty().andNone(of("foo", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone02() {
    Check.that("hello, world").is(ComposablePredicate.eval((String s) -> s.length()
            < 100)
        .andNone(of("hello", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone03() {
    Check.that("hello, world")
        .is(notEmpty().andNone(of("hell", "bar"), substringOf()));
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
    Check.that("foo").is(ComposablePredicate.eval((String s) -> s.length() < 100)
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
            .andAll(of("hello", "world"), substringOf(), "hello, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll101() {
    Check.that(LocalDate.now())
        .is(NULL().andAll(of("hello", "world"), substringOf(), "hello, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll102() {
    Check.that(LocalDate.now())
        .is(notNull()
            .andAll(of("hello", "world"), substringOf(), "foo, world"));
  }

  @Test
  public void andAll103() {
    Check.that(LocalDate.now()).is(notNull().andAll(ints(1, 2, 3, 4), lt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll104() {
    Check.that(LocalDate.now()).is(NULL().andAll(ints(1, 2, 3, 4), lt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAll105() {
    Check.that(LocalDate.now()).is(notNull().andAll(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test
  public void andAny100() {
    Check.that(LocalDate.now())
        .is(notNull()
            .andAlso(GT(), LocalDate.of(1800, 1, 1))
            .andAny(of("hello", "foo"), substringOf(), "hello, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny101() {
    Check.that(LocalDate.now())
        .is(valid().andAny(of("hello", "bar"), substringOf(), "foo, world"));
  }

  @Test
  public void andAny102() {
    Check.that(LocalDate.now()).is(notNull().andAny(ints(51, 2, 3, 4), gt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny103() {
    Check.that(LocalDate.now()).is(NULL().andAny(ints(51, 2, 3, 4), gt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny105() {
    Check.that(LocalDate.now()).is(notNull().andAny(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andAny106() {
    Check.that(LocalDate.now()).is(NULL().andAny(ints(1, 2, 3, 54), gt(), 10));
  }

  @Test
  public void andNone100() {
    Check.that(Year.now()).is(eval(GT(), Year.of(2000))
        .andNone(of("hello", "foo"), substringOf(), "bar, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone101() {
    Check.that(LocalDate.now())
        .is(invalid().andNone(of("hello", "bar"), substringOf(), "hello, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone102() {
    Check.that(LocalDate.now())
        .is(valid().andNone(of("hello", "bar"), substringOf(), "foo bar"));
  }

  @Test
  public void andNone103() {
    Check.that(LocalDate.now()).is(notNull().andNone(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone104() {
    Check.that(LocalDate.now()).is(NULL().andNone(ints(1, 2, 3, 4), gt(), 10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void andNone105() {
    Check.that(LocalDate.now()).is(notNull().andNone(ints(51, 2, 3, 4), gt(), 10));
  }

  private static int[] ints(int... ints) {
    return ints;
  }

}
