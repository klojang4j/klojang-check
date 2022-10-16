package org.klojang.check;

import org.junit.Test;
import org.klojang.check.MsgUtil;

import static org.junit.Assert.assertEquals;

public class MsgUtilTest {

  @Test
  public void getPrefabMessage00() {
    String s = MsgUtil.getPrefabMessage("foo", true, null, 42, null, null);
    assertEquals("invalid value: 42", s);
  }

  @Test
  public void getPrefabMessage01() {
    String s = MsgUtil.getPrefabMessage("foo", true, "funky", 42, null, null);
    assertEquals("invalid value for funky: 42", s);
  }

}
