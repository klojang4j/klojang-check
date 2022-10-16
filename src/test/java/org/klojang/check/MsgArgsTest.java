package org.klojang.check;

import org.junit.Test;
import org.klojang.check.MsgArgs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.klojang.check.CommonChecks.eq;
import static org.klojang.check.CommonChecks.notNull;

public class MsgArgsTest {

  @Test
  public void type00() {
    MsgArgs args = new MsgArgs(notNull(), false, "lastName", "foo", null, null);
    assertEquals(String.class, args.type());
  }

  @Test
  public void type01() {
    MsgArgs args = new MsgArgs(eq(), false, "counter", 2, int.class, 3);
    assertEquals(int.class, args.type());
  }

  @Test
  public void type02() {
    MsgArgs args = new MsgArgs(notNull(), false, "lastName", null, null, null);
    assertNull(args.type());
  }

  @Test
  public void typeAndName00() {
    MsgArgs args = new MsgArgs(notNull(), false, "lastName", "foo", null, null);
    assertEquals("String lastName", args.typeAndName());
  }

  @Test
  public void typeAndName01() {
    MsgArgs args = new MsgArgs(eq(), false, "counter", 2, int.class, 3);
    assertEquals("int counter", args.typeAndName());
  }

  @Test
  public void typeAndName02() {
    MsgArgs args = new MsgArgs(notNull(), false, "lastName", null, null, null);
    assertEquals("lastName", args.typeAndName());
  }

}
