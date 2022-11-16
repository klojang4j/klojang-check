package org.klojang.check.relation;

import org.junit.Test;
import org.klojang.check.Check;

import java.time.Year;
import java.util.List;

import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.relation.Compose.*;

public class ComposeTest {

  @Test
  public void validIf00() {
    Check.that(Year.now()).is(validIf(GT(), Year.of(2000))
        .andAlso(LT(), Year.of(3000)));
  }

  @Test
  public void validIf01() {
    // 2nd validIf redundant but allowed
    Check.that("hello").is(validIf((String s) -> s.charAt(1) == 'e')
        .orElse(validIf((String s) -> s.charAt(1) == 'f'))
    );
  }

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
  public void validWhen00() {
    Check.that("foo").is(validWhen("foo"));
    Check.that(null).is(validWhen(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void validWhen01() {
    Check.that("foo").is(validWhen("bar"));
  }

  @Test
  public void validIntWhen00() {
    Check.that(42).is(validIntWhen(42));
  }

  @Test(expected = IllegalArgumentException.class)
  public void validIntWhen01() {
    Check.that(42).is(validIntWhen(43));
  }

  @Test
  public void invalidWhen00() {
    Check.that("foo").is(invalidWhen("bar"));
    Check.that(null).is(invalidWhen("bar"));
    Check.that("foo").is(invalidWhen(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidWhen01() {
    Check.that("foo").is(invalidWhen("foo"));
  }

  @Test
  public void invalidIntWhen00() {
    Check.that(42).is(invalidIntWhen(43));
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidIntWhen01() {
    Check.that(42).is(invalidIntWhen(42));
  }

}
