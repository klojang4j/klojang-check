package org.klojang.check;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class FooList extends ArrayList<String> {

  @Test(expected = IllegalArgumentException.class)
  public void something() {
    // Is this Collection::contains the same as the one from CommonChecks? No.
    Check.that(this).is(Collection::contains, "foo");
  }

}
