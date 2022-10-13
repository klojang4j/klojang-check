package nl.naturalis.check.types;

import nl.naturalis.check.Check;
import org.junit.Test;

import static java.util.List.of;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.TestUtil.ints;
import static nl.naturalis.check.types.ComposableIntPredicate.*;

public class ComposableIntPredicateTest {

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

  @Test(expected = IllegalArgumentException.class)
  public void orNotRelation03() {
    Check.that(42).is(validIntIf(gte(), 50).orNot(60, lt(), 70));
  }

  @Test
  public void orAny00() {
    Check.that(77).is(even().orAny(ints(1, 2, 3, 4, 88), lt()));
  }

  @Test
  public void orAny01() {
    Check.that(7).is(odd().orAny(ints(1, 2, 3, 4, 88), lt()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void orAny02() {
    Check.that(7).is(even().orAny(ints(1, 2, 3, 4), gt()));
  }

  //  @Test
  //  public void orAll00() {
  //    Check.that("hello, world")
  //        .is(invalidInt().orAll(of(17, "world"), substringOf()));
  //  }
  //
  //  @Test
  //  public void orAll01() {
  //    Check.that("hello, world")
  //        .is(odd().orAll(of(28, "bar"), substringOf()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orAll02() {
  //    Check.that("hello, world")
  //        .is(even().orAll(of(17, "bar"), substringOf()));
  //  }
  //
  //  @Test
  //  public void orNone00() {
  //    Check.that("hello, world")
  //        .is(invalidInt().orNone(of(28, "bar"), substringOf()));
  //  }
  //
  //  @Test
  //  public void orNone01() {
  //    Check.that("hello, world")
  //        .is(odd().orNone(of(17, "bar"), substringOf()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orNone02() {
  //    Check.that("hello, world")
  //        .is(even().orNone(of(17, "bar"), substringOf()));
  //  }
  //
  //  @Test
  //  public void orThatPredicate00() {
  //    Check.that(28).is(blank().orThat("bar", s -> s.length() < 100));
  //    Check.that("   ").is(blank().orThat("bar", s -> s.length() < 1));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orThatPredicate01() {
  //    Check.that(28).is(blank().orThat("bar", s -> s.length() > 100));
  //  }
  //
  //  @Test
  //  public void orThatRelation00() {
  //    Check.that(28).is(blank().orThat("bar", lte(), "ba"));
  //    Check.that("   ").is(blank().orThat("bar", lte(), "ba"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orThatRelation01() {
  //    Check.that(28).is(blank().orThat("bar", lte(), 28));
  //  }
  //
  //  @Test
  //  public void orNotPredicate100() {
  //    Check.that(23)
  //        .is(even().orNot(17, s -> s.length() > 100));
  //    Check.that(23)
  //        .is(odd().orNot(17, EQ(), 17));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orNotPredicate101() {
  //    Check.that(42L).is(invalidInt().orNot("", even()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orNotPredicate102() {
  //    Check.that(42L).is(validIntIf(GTE(), 100L).orNot("", even()));
  //  }
  //
  //  @Test
  //  public void orNotRelation101() {
  //    Check.that(23).is(even()
  //        .orNot(17, lte(), "world"));
  //    Check.that(23).is(odd()
  //        .orNot(17, lte(), 97));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orNotRelation102() {
  //    Check.that(Year.of(2001)).is(validIntIf(gt(), Year.of(3000))
  //        .orNot(28, substringOf(), "foo bar"));
  //  }
  //
  //  @Test
  //  public void orAll100() {
  //    Check.that(LocalDate.now())
  //        .is(NULL().orAll(of(17, "world"), substringOf(), "hello, world"));
  //    Check.that(LocalDate.now())
  //        .is(notNull().orAll(of(17, "world"), substringOf(), "foo, world"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orAll101() {
  //    Check.that(42F).is(validIntIf(GTE(), 100F).orAll(of(1, 2, 3, 4, 5), gt(), 10));
  //  }
  //
  //  @Test
  //  public void orAll102() {
  //    Check.that(LocalDate.now()).is(NULL().orAll(ints(1, 2, 3, 4), lt(), 10));
  //  }
  //
  //  @Test
  //  public void orAll103() {
  //    Check.that(LocalDate.now()).is(notNull().orAll(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orAll104() {
  //    Check.that(LocalDate.now()).is(NULL().orAll(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test
  //  public void orAny100() {
  //    Check.that(LocalDate.now())
  //        .is(NULL().orAny(of(17, 28), substringOf(), "hello, world"));
  //    Check.that(LocalDate.now())
  //        .is(notNull().orAny(of(17, "bar"), substringOf(), "foo, world"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orAny101() {
  //    Check.that(LocalDate.now())
  //        .is(invalidInt().orAny(of(17, "bar"), substringOf(), "foo, world"));
  //  }
  //
  //  @Test
  //  public void orAny102() {
  //    Check.that(LocalDate.now()).is(NULL().orAny(ints(1, 52, 53, 54), lt(), 10));
  //  }
  //
  //  @Test
  //  public void orAny103() {
  //    Check.that(LocalDate.now()).is(notNull().orAny(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orAny104() {
  //    Check.that(LocalDate.now()).is(NULL().orAny(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test
  //  public void orNone100() {
  //    Check.that(LocalDate.now())
  //        .is(NULL().orNone(of(17, 28), substringOf(), "bar, world"));
  //    Check.that(LocalDate.now())
  //        .is(notNull().orNone(of(17, "bar"), substringOf(), "foo, world"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orNone101() {
  //    Check.that(LocalDate.now())
  //        .is(invalidInt().orNone(of(17, "bar"),
  //            substringOf(),
  //            "hello, world"));
  //  }
  //
  //  @Test
  //  public void orNone102() {
  //    Check.that(LocalDate.now()).is(NULL().orNone(ints(51, 52, 53, 54), lt(), 10));
  //  }
  //
  //  @Test
  //  public void orNone103() {
  //    Check.that(LocalDate.now()).is(notNull().orNone(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void orNone104() {
  //    Check.that(LocalDate.now()).is(NULL().orNone(ints(1, 52, 3, 4), gt(), 10));
  //  }
  //
  //  ///////////////////////////////////////////////////////////////////////
  //  ///////////////////////////////////////////////////////////////////////
  //  ///////////////////////////////////////////////////////////////////////
  //
  //  @Test
  //  public void andAlsoPredicate00() {
  //    Check.that(Optional.of(3.3F)).is(odd().andAlso(notNull()));
  //  }
  //
  //  @Test(expected = ClassCastException.class)
  //  public void andAlsoPredicate01() {
  //    Check.that(23).is(zero().andAlso(odd()));
  //  }
  //
  //  @Test
  //  public void andAlsoRelation00() {
  //    Check.that(23)
  //        .is(ComposableIntPredicate.validIntIf((List l) -> l.size() < 100)
  //            .andAlso(lt(), 50));
  //  }
  //
  //  @Test
  //  public void andAlsoRelation01() {
  //    Check.that(Year.now()).is(ComposableIntPredicate.validIntIf(gt(), Year.of(2000))
  //        .andAlso(lt(), Year.of(3000)));
  //  }
  //
  //  @Test
  //  public void andAlsoRelation02() {
  //    Check.that(23)
  //        .is(ComposableIntPredicate.validIntIf((List l) -> l.size() < 100)
  //            .andAlso((List l) -> l.size() > 1)
  //            .andAlso(lt(), 50));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAlsoRelation03() {
  //    Check.that(List.of()).is(even().andAlso(lt(), 50));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAlsoRelation04() {
  //    Check.that(List.of()).is(odd().andAlso(lt(), 50));
  //  }
  //
  //  @Test
  //  public void andAlsoBoolean00() {
  //    Check.that(9).is(validIntIf(lt(), 10).andAlso(10 < 11));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAlsoBoolean01() {
  //    Check.that(9).is(validIntIf(lt(), 8D).andAlso(10 < 11));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAlsoBoolean02() {
  //    Check.that(9).is(validIntIf(lt(), 10).andAlso(10 > 11));
  //  }
  //
  //  @Test
  //  public void andEval00() {
  //    Check.that(9).is(validIntIf(lt(), 10).andEval(() -> 10 < 11));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andEval01() {
  //    Check.that(9).is(validIntIf(lt(), 8D).andEval(() -> 10 < 11));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andEval02() {
  //    Check.that(9).is(validIntIf(lt(), 10).andEval(() -> 10 > 11));
  //  }
  //
  //  @Test
  //  public void andNotPredicate00() {
  //    Check.that(23).is(odd().andNot(NULL()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotPredicate01() {
  //    Check.that(23).is(
  //        odd().andNot((List list) -> list.size() < 100));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotPredicate02() {
  //    Check.that(23).is(
  //        ComposableIntPredicate.validIntIf((List<String> list) -> list.size() > 100)
  //            .andNot(even()));
  //  }
  //
  //  @Test
  //  public void andNotRelation00() {
  //    Check.that(23).is(odd().andNot(gt(), 7));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotRelation01() {
  //    Check.that(23).is(even().andNot(gt(), 7));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotRelation02() {
  //    Check.that(23).is(odd().andNot(lt(), 50));
  //  }
  //
  //  @Test
  //  public void andAny00() {
  //    Check.that("hello, world")
  //        .is(validInt().andAny(of(28, "world"), substringOf()));
  //  }
  //
  //  @Test
  //  public void andAny01() {
  //    Check.that("hello, world")
  //        .is(odd().andAny(of("wor", "bar"), substringOf()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAny02() {
  //    Check.that("hello, world")
  //        .is(even().andAny(of(28, "bar"), substringOf()));
  //  }
  //
  //  @Test
  //  public void andAll00() {
  //    Check.that("hello, world")
  //        .is(validInt().andAll(of(17, "world"), substringOf()));
  //  }
  //
  //  @Test
  //  public void andAll01() {
  //    Check.that("hello, world")
  //        .is(odd().andAll(of("hel", "wor"), substringOf()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAll02() {
  //    Check.that("hello, world")
  //        .is(even().andAll(of(17, "bar"), substringOf()));
  //  }
  //
  //  @Test
  //  public void andNone00() {
  //    Check.that("hello, world")
  //        .is(validInt().andNone(of(28, "bar"), substringOf()));
  //  }
  //
  //  @Test
  //  public void andNone01() {
  //    Check.that("hello, world")
  //        .is(odd().andNone(of(28, "bar"), substringOf()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNone02() {
  //    Check.that("hello, world")
  //        .is(ComposableIntPredicate.validIntIf((String s) -> s.length()
  //                < 100)
  //            .andNone(of(17, "bar"), substringOf()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNone03() {
  //    Check.that("hello, world")
  //        .is(odd().andNone(of(97, "bar"), substringOf()));
  //  }
  //
  //  @Test
  //  public void andThatPredicate00() {
  //    Check.that(28).is(odd().andThat("bar", s -> s.length() < 100));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andThatPredicate01() {
  //    Check.that(28).is(blank().andThat("bar", s -> s.length() > 100));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andThatPredicate02() {
  //    Check.that("   ").is(notNull().andThat("bar", s -> s.length() > 100));
  //  }
  //
  //  @Test
  //  public void andThatRelation00() {
  //    Check.that(28).is(ComposableIntPredicate.validIntIf((String s) -> s.length()
  //            < 100)
  //        .andThat("bar", lte(), "ba"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andThatRelation01() {
  //    Check.that("foo").is(blank().andThat("bar", hasSubstring(), "foo"));
  //  }
  //
  //  @Test
  //  public void andNotPredicate100() {
  //    Check.that(23).is(odd().andNot("", blank()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotPredicate102() {
  //    Check.that(23).is(odd().andNot(8, even()));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotPredicate103() {
  //    Check.that(23).is(even().andNot(28, even()));
  //  }
  //
  //  @Test
  //  public void andNotRelation101() {
  //    Check.that(23).is(odd().andNot("foo", substringOf(), "foo bar"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotRelation102() {
  //    Check.that(23).is(odd().andNot(9, lt(), 42));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotRelation103() {
  //    Check.that(23).is(odd().andNot(17, lte(), 97));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNotRelation104() {
  //    Check.that(23).is(even().andNot(9, gt(), 42));
  //  }
  //
  //  @Test
  //  public void andAll100() {
  //    Check.that(LocalDate.now())
  //        .is(notNull()
  //            .andAll(of(17, "world"), substringOf(), "hello, world"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAll101() {
  //    Check.that(LocalDate.now())
  //        .is(NULL().andAll(of(17, "world"), substringOf(), "hello, world"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAll102() {
  //    Check.that(LocalDate.now())
  //        .is(notNull()
  //            .andAll(of(17, "world"), substringOf(), "foo, world"));
  //  }
  //
  //  @Test
  //  public void andAll103() {
  //    Check.that(LocalDate.now()).is(notNull().andAll(ints(1, 2, 3, 4), lt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAll104() {
  //    Check.that(LocalDate.now()).is(NULL().andAll(ints(1, 2, 3, 4), lt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAll105() {
  //    Check.that(LocalDate.now()).is(notNull().andAll(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test
  //  public void andAny100() {
  //    Check.that(LocalDate.now())
  //        .is(notNull()
  //            .andAlso(gt(), LocalDate.of(1800, 1, 1))
  //            .andAny(of(17, 28), substringOf(), "hello, world"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAny101() {
  //    Check.that(LocalDate.now())
  //        .is(validInt().andAny(of(17, "bar"), substringOf(), "foo, world"));
  //  }
  //
  //  @Test
  //  public void andAny102() {
  //    Check.that(LocalDate.now()).is(notNull().andAny(ints(51, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAny103() {
  //    Check.that(LocalDate.now()).is(NULL().andAny(ints(51, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAny105() {
  //    Check.that(LocalDate.now()).is(notNull().andAny(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andAny106() {
  //    Check.that(LocalDate.now()).is(NULL().andAny(ints(1, 2, 3, 54), gt(), 10));
  //  }
  //
  //  @Test
  //  public void andNone100() {
  //    Check.that(Year.now()).is(validIntIf(gt(), Year.of(2000))
  //        .andNone(of(17, 28), substringOf(), "bar, world"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNone101() {
  //    Check.that(LocalDate.now())
  //        .is(invalidInt().andNone(of(17, "bar"), substringOf(), "hello, world"));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNone102() {
  //    Check.that(LocalDate.now())
  //        .is(validInt().andNone(of(17, "bar"), substringOf(), "foo bar"));
  //  }
  //
  //  @Test
  //  public void andNone103() {
  //    Check.that(LocalDate.now()).is(notNull().andNone(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNone104() {
  //    Check.that(LocalDate.now()).is(NULL().andNone(ints(1, 2, 3, 4), gt(), 10));
  //  }
  //
  //  @Test(expected = IllegalArgumentException.class)
  //  public void andNone105() {
  //    Check.that(LocalDate.now()).is(notNull().andNone(ints(51, 2, 3, 4), gt(), 10));
  //  }
  //
  //  private static int[] ints(int... ints) {
  //    return ints;
  //  }
  //
}
