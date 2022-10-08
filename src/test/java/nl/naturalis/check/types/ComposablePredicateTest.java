package nl.naturalis.check.types;

import nl.naturalis.check.Check;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.types.ComposablePredicate.*;

public class ComposablePredicateTest {

  @Test
  public void orElsePredicate00() {
    Check.that(List.of("foo", "bar")).is(empty().orElse(notEmpty()));
    Check.that(List.of("foo", "bar")).is(notEmpty().orElse(empty()));
  }

  @Test(expected = ClassCastException.class)
  public void orElsePredicate01() {
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

  @Test
  public void orNotRelation00() {
    Check.that(List.of("foo", "bar")).is(empty().orNot(contains(), "bozo"));
    Check.that(List.of("foo", "bar")).is(notEmpty().orNot(contains(), "bozo"));
  }

  @Test
  public void orAny00() {
    Check.that("hello, world")
        .is(invalid().orAny(List.of("foo", "world"), substringOf()));
  }

  @Test
  public void orAny01() {
    Check.that("hello, world")
        .is(notEmpty().orAny(List.of("foo", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny02() {
    Check.that("hello, world")
        .is(empty().orAny(List.of("foo", "bar"), substringOf()));
  }

  @Test
  public void orAll00() {
    Check.that("hello, world")
        .is(invalid().orAll(List.of("hello", "world"), substringOf()));
  }

  @Test
  public void orAll01() {
    Check.that("hello, world")
        .is(notEmpty().orAll(List.of("foo", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAll02() {
    Check.that("hello, world")
        .is(empty().orAll(List.of("hello", "bar"), substringOf()));
  }

  @Test
  public void orNone00() {
    Check.that("hello, world")
        .is(invalid().orNone(List.of("foo", "bar"), substringOf()));
  }

  @Test
  public void orNone01() {
    Check.that("hello, world")
        .is(notEmpty().orNone(List.of("hello", "bar"), substringOf()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orNone02() {
    Check.that("hello, world")
        .is(empty().orNone(List.of("hello", "bar"), substringOf()));
  }

  @Test
  public void orThatPredicate00() {
    Check.that("foo").is(blank().orThat("bar", s -> s.length() < 20));
    Check.that("   ").is(blank().orThat("bar", s -> s.length() < 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orThatPredicate01() {
    Check.that("foo").is(blank().orThat("bar", s -> s.length() > 20));
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

  @Test
  public void orNotRelation101() {
    Check.that(List.of("foo", "bar")).is(empty()
        .orNot("hello", hasSubstring(), "world"));
    Check.that(List.of("foo", "bar")).is(notEmpty()
        .orNot("hello", hasSubstring(), "hell"));
  }

  @Test
  public void orAll100() {
    Check.that(LocalDate.now())
        .is(NULL().orAll(List.of("hello", "world"), substringOf(), "hello, world"));
    Check.that(LocalDate.now())
        .is(notNull().orAll(List.of("hello", "world"), substringOf(), "foo, world"));
  }

  @Test
  public void orAny100() {
    Check.that(LocalDate.now())
        .is(NULL().orAny(List.of("hello", "foo"), substringOf(), "hello, world"));
    Check.that(LocalDate.now())
        .is(notNull().orAny(List.of("hello", "bar"), substringOf(), "foo, world"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny101() {
    Check.that(LocalDate.now())
        .is(invalid().orAny(List.of("hello", "bar"), substringOf(), "foo, world"));
  }

}
