package org.klojang.check.extra;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmptyableTest {

  @Test
  public void isDeepNotEmpty00() {
    Emptyable emptyable = () -> true;
    assertFalse(emptyable.isDeepNotEmpty());
  }

  @Test
  public void isDeepNotEmpty01() {
    Emptyable emptyable = () -> false;
    assertTrue(emptyable.isDeepNotEmpty());
  }
}
