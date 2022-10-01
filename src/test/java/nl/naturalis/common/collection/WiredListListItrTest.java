package nl.naturalis.common.collection;

import org.junit.Test;

import static org.junit.Assert.*;

public class WiredListListItrTest {

  @Test
  public void hasNext00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator();
    assertTrue(itr.hasNext());
  }

  @Test
  public void hasNext01() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator(4);
    assertTrue(itr.hasNext());
    assertEquals(4, (int) itr.next()); // Yes, that's right - see ListIterator specs
    assertEquals(5, (int) itr.next());
    assertFalse(itr.hasNext());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void test00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator(7);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void test01() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator(-1);
  }

  @Test
  public void hasPrevious00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator();
    assertFalse(itr.hasPrevious());
  }

  @Test
  public void hasPrevious01() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator(6);
    assertTrue(itr.hasPrevious());
    assertEquals(5, (int) itr.previous());
    assertFalse(itr.hasNext());
  }

  @Test
  public void hasPrevious02() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator(1);
    assertTrue(itr.hasPrevious());
    assertEquals(0, (int) itr.previous());
    assertFalse(itr.hasPrevious());
  }

  @Test
  public void hasPrevious03() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator(5);
    assertTrue(itr.hasPrevious());
  }

  @Test
  public void next00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator();
    assertEquals(0, (int) itr.next());
    assertEquals(1, (int) itr.next());
    assertEquals(2, (int) itr.next());
    assertEquals(3, (int) itr.next());
    assertEquals(4, (int) itr.next());
    assertEquals(5, (int) itr.next());
  }

  @Test
  public void previous00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3, 4, 5);
    var itr = wl.listIterator(6);
    assertEquals(5, (int) itr.previous());
    assertEquals(4, (int) itr.previous());
    assertEquals(3, (int) itr.previous());
    assertEquals(2, (int) itr.previous());
    assertEquals(1, (int) itr.previous());
    assertEquals(0, (int) itr.previous());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void add00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3);
    var itr = wl.listIterator();
    itr.next();
    itr.add(7);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void remove00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3);
    var itr = wl.listIterator();
    itr.next();
    itr.remove();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void set00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2, 3);
    var itr = wl.listIterator();
    itr.next();
    itr.set(7);
  }

  @Test
  public void nextIndex00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2);
    var itr = wl.listIterator();
    assertEquals(0, itr.nextIndex());
    itr.next();
    assertEquals(1, itr.nextIndex());
    itr.next();
    assertEquals(2, itr.nextIndex());
    itr.next();
    assertEquals(3, itr.nextIndex());
  }

  @Test
  public void previousIndex00() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2);
    var itr = wl.listIterator();
    assertEquals(-1, itr.previousIndex());
  }

  @Test
  public void previousIndex01() {
    WiredList<Integer> wl = WiredList.of(0, 1, 2);
    var itr = wl.listIterator(3);
    assertEquals(2, itr.previousIndex());
    itr.previous();
    assertEquals(1, itr.previousIndex());
    itr.previous();
    assertEquals(0, itr.previousIndex());
    itr.previous();
    assertEquals(0 - 1, itr.previousIndex());
  }

}
