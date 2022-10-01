package nl.naturalis.common.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class MutableIntTest {

  @Test
  public void pp00() {
    MutableInt i = new MutableInt();
    int j = i.pp();
    assertEquals(0, j);
    assertEquals(1, i.get());
  }

  @Test
  public void ppi00() {
    MutableInt i = new MutableInt(7);
    int j = i.ppi();
    assertEquals(8, j);
    assertEquals(8, i.get());
  }

  @Test
  public void mm00() {
    MutableInt i = new MutableInt();
    int j = i.mm();
    assertEquals(0, j);
    assertEquals(-1, i.get());
  }

  @Test
  public void mmi00() {
    MutableInt i = new MutableInt(7);
    int j = i.mmi();
    assertEquals(6, j);
    assertEquals(6, i.get());
  }

  @Test
  public void plusIs00() {
    MutableInt i = new MutableInt(7);
    int j = i.plusIs(2);
    assertEquals(9, j);
    assertEquals(9, i.get());
    j = i.plusIs(i);
    assertEquals(18, j);
    assertEquals(18, i.get());
  }

  @Test
  public void minIs00() {
    MutableInt i = new MutableInt(7);
    int j = i.minIs(2);
    assertEquals(5, j);
    assertEquals(5, i.get());
    j = i.minIs(new MutableInt(i));
    assertEquals(0, j);
    assertEquals(0, i.get());
  }

  @Test
  public void set00() {
    MutableInt i = new MutableInt(7);
    int j = i.set(-2);
    assertEquals(-2, j);
    assertEquals(-2, i.get());
    j = i.set(i);
    assertEquals(-2, j);
    assertEquals(-2, i.get());
    j = i.set(new MutableInt(4));
    assertEquals(4, j);
    assertEquals(4, i.get());
  }

  @Test
  public void eq00() {
    MutableInt i = new MutableInt(7);
    assertTrue(i.eq(7));
    assertTrue(i.eq(new MutableInt(7)));
    assertFalse(i.eq(12));
    assertFalse(i.eq(new MutableInt(12)));
  }

  @Test
  public void ne00() {
    MutableInt i = new MutableInt(7);
    assertTrue(i.ne(9));
    assertTrue(i.ne(new MutableInt(9)));
    assertFalse(i.ne(7));
    assertFalse(i.ne(new MutableInt(7)));
  }

  @Test
  public void gt00() {
    MutableInt i = new MutableInt(7);
    assertTrue(i.gt(-7));
    assertTrue(i.gt(new MutableInt(-7)));
    assertFalse(i.gt(9));
    assertFalse(i.gt(new MutableInt(9)));
  }

  @Test
  public void lt00() {
    MutableInt i = new MutableInt(7);
    assertTrue(i.lt(10));
    assertTrue(i.lt(new MutableInt(10)));
    assertFalse(i.lt(7));
    assertFalse(i.lt(new MutableInt(7)));
  }

  @Test
  public void gte00() {
    MutableInt i = new MutableInt(7);
    assertTrue(i.gte(-7));
    assertTrue(i.gte(new MutableInt(-7)));
    assertFalse(i.gte(9));
    assertFalse(i.gte(new MutableInt(9)));
  }

  @Test
  public void lte00() {
    MutableInt i = new MutableInt(7);
    assertTrue(i.lte(7));
    assertTrue(i.lte(new MutableInt(7)));
    assertFalse(i.lte(4));
    assertFalse(i.lte(new MutableInt(4)));
  }

  @Test
  public void reset00() {
    MutableInt i = new MutableInt(7);
    int j = i.reset();
    assertEquals(0, j);
    assertEquals(0, i.get());
  }

  @Test
  public void hashCode00() {
    MutableInt i = new MutableInt(7);
    assertEquals(7, i.hashCode());
  }

  @Test
  public void equals00() {
    MutableInt i = new MutableInt(7);
    assertTrue(i.equals(i));
    assertTrue(i.equals(7));
    assertFalse(i.equals(13));
    assertTrue(i.equals((short) 7));
    assertFalse(i.equals((short) 13));
    assertTrue(i.equals((byte) 7));
    assertFalse(i.equals((byte) 13));
    assertTrue(i.equals(new MutableInt(7)));
    assertFalse(i.equals(new MutableInt(13)));
    assertFalse(i.equals(null));
    assertFalse(i.equals("Hello, World"));
  }

  @Test
  public void toString00() {
    MutableInt i = new MutableInt(7);
    assertEquals("7", i.toString());
  }

}
