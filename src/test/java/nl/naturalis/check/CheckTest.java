package nl.naturalis.check;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static nl.naturalis.check.Check.EOM;
import static org.junit.Assert.*;
import static nl.naturalis.check.TestUtil.*;

@SuppressWarnings({"rawtypes"})
public class CheckTest {

  @Test(expected = IllegalArgumentException.class)
  public void notNull00() {
    Check.notNull(null);
  }

  @Test
  public void notNull01() {
    Object obj = Check.notNull(new Object());
    assertTrue(obj instanceof ObjectCheck);
  }

  @Test
  public void fail00() {
    Long l = 42L;
    try {
      l = Check.failOn(IndexOutOfBoundsException::new, "Got that wrong ${0}", "bro");
    } catch (IndexOutOfBoundsException e) {
      assertEquals(42L, (long) l);
      assertEquals("Got that wrong bro", e.getMessage());
      return;
    }
    Assert.fail();
  }

  @Test
  public void fail01() {
    String s = "hello";
    try {
      s = Check.fail("Got that wrong ${0}", "bro");
    } catch (IllegalArgumentException e) {
      assertEquals("hello", s);
      assertEquals("Got that wrong bro", e.getMessage());
      return;
    }
    Assert.fail();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fail02() {
    Check.fail(IndexOutOfBoundsException::new);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fail03() {
    String s = "hello";
    try {
      s = Check.fail(null, null); // Allowed
    } catch (IllegalArgumentException e) {
      assertNull(e.getMessage());
      assertEquals("hello", s);
      throw e;
    }
    Assert.fail();
  }

  @Test(expected = IllegalStateException.class)
  public void fail04() {
    String s = Check.failOn(IllegalStateException::new, null, null);
  }

  @Test
  public void fail05() {
    try {
      Check.fail("Nothing left to say", null);
    } catch (IllegalArgumentException e) {
      assertEquals("Nothing left to say", e.getMessage());
      return;
    }
    Assert.fail();
  }

  @Test
  public void fail06() {
    Integer x = 7;
    try {
      x = Check.fail("Nothing left to say", EOM);
    } catch (IllegalArgumentException e) {
      assertEquals(7, (int) x);
      assertEquals("Nothing left to say", e.getMessage());
      return;
    }
    Assert.fail();
  }

  @Test
  public void fail07() {
    String s = "hello";
    try {
      s = Check.fail("Nothing left to say${0}${1}", EOM, " (except this)");
    } catch (IllegalArgumentException e) {
      assertEquals("hello", s);
      assertEquals("Nothing left to say" + EOM + " (except this)", e.getMessage());
      return;
    }
    Assert.fail();
  }

  @Test
  public void fail08() {
    Object o = null;
    try {
      o = Check.fail("Nothing left to say${0}${1}", "...", " (except this)");
    } catch (IllegalArgumentException e) {
      assertNull(o);
      assertEquals("Nothing left to say... (except this)", e.getMessage());
      return;
    }
    Assert.fail();
  }

  @Test
  public void offsetLength00() {
    Check.offsetLength(new byte[0], 0, 0);
    Check.offsetLength(new byte[1], 0, 0);
    // Allowed: new ByteArrayOutputStream().write(new byte[1], 1, 0) !!
    Check.offsetLength(new byte[1], 1, 0);
    Check.offsetLength(new byte[1], 0, 1);
    Check.offsetLength(new byte[2], 0, 2);
    Check.offsetLength(0, 0, 0);
    Check.offsetLength(1, 0, 0);
    Check.offsetLength(1, 0, 1);
    Check.offsetLength(1, 1, 0);
    Check.offsetLength(2, 0, 2);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength01() {
    Check.offsetLength(new byte[0], 0, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength02() {
    Check.offsetLength(new byte[0], 1, 1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength03() {
    Check.offsetLength(new byte[0], 1, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength04() {
    Check.offsetLength(new byte[4], 3, 3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength05() {
    Check.offsetLength(-10, 1, 3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength06() {
    Check.offsetLength(10, -1, 3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength07() {
    Check.offsetLength(10, 1, -3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength08() {
    Check.offsetLength(10, 1, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void offsetLength09() {
    Check.offsetLength(null, 1, 10);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength10() {
    Check.offsetLength(new byte[] {(byte) 0, (byte) 1, (byte) 2}, -1, 10);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength11() {
    Check.offsetLength(new byte[] {(byte) 0, (byte) 1, (byte) 2}, 1, -10);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void offsetLength12() {
    Check.offsetLength(new byte[] {(byte) 0, (byte) 1, (byte) 2}, 3, 7);
  }

  @Test
  public void fromTo00() {
    Check.fromTo("123", 0, 0);
    Check.fromTo("123", 3, 3);
    Check.fromTo("123", 2, 3);
    Check.fromTo(List.of(1, 2, 3), 0, 0);
    Check.fromTo(List.of(1, 2, 3), 3, 3);
    Check.fromTo(List.of(1, 2, 3), 2, 3);
    Check.fromTo(3, 0, 0);
    Check.fromTo(3, 3, 3);
    Check.fromTo(3, 2, 3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo01() {
    Check.fromTo("123", 3, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromTo02() {
    Check.fromTo((String) null, 3, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromTo03() {
    Check.fromTo((List) null, 3, 4);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo04() {
    Check.fromTo(-10, 3, 4);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo05() {
    Check.fromTo(10, -3, 4);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo06() {
    Check.fromTo(10, 3, -4);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo07List() {
    Check.fromTo(List.of(0, 1, 2, 3, 4), -4, 8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo08List() {
    Check.fromTo(List.of(0, 1, 2, 3, 4), 4, -8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo09List() {
    Check.fromTo(List.of(0, 1, 2, 3, 4), 4, 8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo10List() {
    Check.fromTo(List.of(0, 1, 2, 3, 4), 4, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromTo11List() {
    Check.fromTo((List) null, 4, 2);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo07Array() {
    Check.fromTo(pack(0, 1, 2, 3, 4), -4, 8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo08Array() {
    Check.fromTo(pack(0, 1, 2, 3, 4), 4, -8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo09Array() {
    Check.fromTo(pack(0, 1, 2, 3, 4), 4, 8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo10Array() {
    Check.fromTo(pack(0, 1, 2, 3, 4), 4, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromTo11Array() {
    Check.fromTo((Object[]) null, 4, 2);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo07String() {
    Check.fromTo("01234", -4, 8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo08String() {
    Check.fromTo("01234", 4, -8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo09String() {
    Check.fromTo("01234", 4, 8);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromTo10String() {
    Check.fromTo("01234", 4, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromTo11String() {
    Check.fromTo((String) null, 4, 2);
  }

}
