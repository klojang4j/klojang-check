package org.klojang.check;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.klojang.check.CommonChecks.numerical;

public class FooList extends ArrayList<String> {

  @Test(expected = IllegalArgumentException.class)
  public void something() {
    // Is this Collection::contains the same as the one from CommonChecks? No.
    Check.that(this).is(Collection::contains, "foo");
    Check.that("123").is(numerical(), int.class);
  }

}
