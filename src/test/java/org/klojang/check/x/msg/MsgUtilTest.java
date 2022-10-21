package org.klojang.check.x.msg;

import org.junit.Test;
import org.klojang.check.x.msg.MsgUtil;

import static org.junit.Assert.assertEquals;

public class MsgUtilTest {

  @Test
  public void defaultPredicateMessage00() {
    String s = MsgUtil.defaultPredicateMessage(null, 42);
    assertEquals("invalid value: 42", s);
  }

  @Test
  public void defaultPredicateMessage01() {
    String s = MsgUtil.defaultPredicateMessage("funky", 42);
    assertEquals("invalid value for funky: 42", s);
  }

  @Test
  public void defaultRelationMessage00() {
    String s = MsgUtil.defaultRelationMessage(null, 42, 43);
    assertEquals("no such relation between 42 and 43", s);
  }

  @Test
  public void defaultRelationMessage01() {
    String s = MsgUtil.defaultRelationMessage("funky", 42, 43);
    assertEquals("argument funky: no such relation between 42 and 43", s);
  }

}
