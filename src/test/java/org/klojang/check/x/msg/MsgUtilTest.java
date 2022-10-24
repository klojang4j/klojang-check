package org.klojang.check.x.msg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MsgUtilTest {

  @Test
  public void defaultPredicateMessage00() {
    String s = MsgUtil.getDefaultPredicateMessage(null, 42);
    assertEquals("invalid value: 42", s);
  }

  @Test
  public void defaultPredicateMessage01() {
    String s = MsgUtil.getDefaultPredicateMessage("funky", 42);
    assertEquals("invalid value for funky: 42", s);
  }

  @Test
  public void defaultRelationMessage00() {
    String s = MsgUtil.getDefaultRelationMessage(null, 42, 43);
    assertEquals("no such relation between 42 and 43", s);
  }

  @Test
  public void defaultRelationMessage01() {
    String s = MsgUtil.getDefaultRelationMessage("funky", 42, 43);
    assertEquals("invalid value for funky: no such relation between 42 and 43", s);
  }

}
