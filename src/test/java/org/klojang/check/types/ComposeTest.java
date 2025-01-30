package org.klojang.check.types;

import org.junit.Test;
import org.klojang.check.Check;

import java.time.Year;
import java.util.List;

import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.types.Compose.*;

public class ComposeTest {

  @Test
  public void validIf00() {
    Check.that(Year.now()).is(Compose.validWhen(GT(), Year.of(2000))
        .andAlso(LT(), Year.of(3000)));
  }

  @Test
  public void validIf01() {
    // 2nd validIf redundant but allowed
    Check.that("hello").is(validWhen((String s) -> s.charAt(1) == 'e')
        .orElse(validWhen((String s) -> s.charAt(1) == 'f'))
    );
  }

  @Test
  public void validIntIf00() {
    Check.that(27).is(Compose.validIntWhen(lt(), 30));
  }

  @Test(expected = IllegalArgumentException.class)
  public void validIntIf01() {
    Check.that(27).is(Compose.validIntWhen(lt(), 20));
  }

  @Test
  public void validIntIf02() {
    Check.that(1).is(Compose.validIntWhen(indexOf(), List.of(1, 2, 3)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void validIntIf03() {
    Check.that(27).is(Compose.validIntWhen(indexOf(), List.of(1, 2, 3)));
  }

  @Test
  public void validWhen00() {
    Check.that("foo").is(Compose.validIf("foo"));
    Check.that(null).is(Compose.validIf(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void validWhen01() {
    Check.that("foo").is(Compose.validIf("bar"));
  }

  @Test
  public void validIntWhen00() {
    Check.that(42).is(Compose.validIntIf(42));
  }

  @Test(expected = IllegalArgumentException.class)
  public void validIntWhen01() {
    Check.that(42).is(Compose.validIntIf(43));
  }

  @Test
  public void invalidWhen00() {
    Check.that("foo").is(invalidIf("bar"));
    Check.that(null).is(invalidIf("bar"));
    Check.that("foo").is(invalidIf(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidWhen01() {
    Check.that("foo").is(invalidIf("foo"));
  }

  @Test
  public void invalidIntWhen00() {
    Check.that(42).is(invalidIntIf(43));
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidIntWhen01() {
    Check.that(42).is(invalidIntIf(42));
  }

}
