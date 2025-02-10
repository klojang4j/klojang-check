package org.klojang.check.types;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjIntRelationTest {

  @Test
  public void converse00() {
    ObjIntRelation<String> r1 = (s, i) -> s.length() < i;
    IntObjRelation<String> r2 = r1.converse();
    assertTrue(r2.exists(10, "hello"));
  }

  @Test
  public void negate00() {
    ObjIntRelation<String> r1 = (s, i) -> s.length() < i;
    ObjIntRelation<String> r2 = r1.negate();
    assertFalse(r2.exists("hello", 10));
  }

}
