package org.klojang.check.relation;

import org.junit.Test;
import org.klojang.check.Check;

import java.time.Year;
import java.util.List;

import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.relation.ComposeMethods.validIf;
import static org.klojang.check.relation.ComposeMethods.validIntIf;

public class ComposeMethodsTest {

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

}
