package nl.naturalis.common.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WiredIteratorTest {

  @Test
  public void test01() {
    var wl0 = WiredList.of(0, 1, 2, 3);
    var itr = wl0.wiredIterator();
    itr.next();
    itr.remove(); // 0
    itr.next();
    itr.remove(); // 1
    itr.next();
    itr.remove(); // 2
    itr.next();
    itr.remove(); // 3
    assertEquals(0, wl0.size());
  }

  @Test
  public void test02() {
    var wl0 = WiredList.of(0);
    var itr = wl0.wiredIterator();
    itr.next();
    itr.remove();
    assertEquals(0, wl0.size());
  }

  @Test
  public void test03() {
    var wl0 = WiredList.of(0, 1, 2, 3);
    var itr = wl0.wiredIterator();
    itr.next();
    itr.insertBefore(-1);
    assertEquals(List.of(-1, 0, 1, 2, 3), wl0);
  }

  @Test
  public void test04() {
    var wl0 = WiredList.of(0, 1, 2, 3);
    var itr = wl0.wiredIterator();
    itr.next();
    itr.insertBefore(-2);
    itr.insertBefore(-1);
    assertEquals(List.of(-2, -1, 0, 1, 2, 3), wl0);
  }

  @Test
  public void test05() {
    var wl0 = WiredList.of(0, 1, 2, 3);
    var itr = wl0.wiredIterator();
    itr.next();
    assertEquals(0, itr.index());
    itr.next();
    assertEquals(1, itr.index());
    itr.insertBefore(-2);
    assertEquals(2, itr.index());
    itr.insertBefore(-1);
    assertEquals(3, itr.index());
    assertEquals(List.of(0, -2, -1, 1, 2, 3), wl0);
  }

  @Test
  public void test06() {
    var wl0 = WiredList.of(0, 1, 2, 3);
    var itr = wl0.wiredIterator();
    itr.next();
    assertEquals(0, itr.index());
    itr.insertAfter(-1);
    assertEquals(0, itr.index());
    itr.insertAfter(-2);
    assertEquals(0, itr.index());
    assertEquals(List.of(0, -2, -1, 1, 2, 3), wl0);
  }

  @Test
  public void test07() {
    var wl0 = WiredList.of(0, 1, 2, 3);
    var itr = wl0.wiredIterator();
    itr.next();
    assertEquals(0, itr.index());
    itr.next();
    assertEquals(1, itr.index());
    itr.next();
    assertEquals(2, itr.index());
    itr.next();
    assertEquals(3, itr.index());
    itr.insertAfter(4);
    assertEquals(3, itr.index());
    itr.next();
    assertEquals(4, itr.index());
    itr.insertAfter(5);
    assertEquals(List.of(0, 1, 2, 3, 4, 5), wl0);
  }

  @Test
  public void test08() {
    List<Integer> bucket = new ArrayList<>();
    var wl0 = WiredList.of(0, 1, 2, 3, 4);
    var itr = wl0.wiredIterator();
    itr.next();
    itr.remove();
    itr.forEachRemaining(bucket::add);
    assertEquals(List.of(1, 2, 3, 4), bucket);
  }

  @Test
  public void test09() {
    List<Integer> bucket = new ArrayList<>();
    var wl0 = WiredList.of(0, 1, 2, 3, 4);
    var itr = wl0.wiredIterator();
    itr.next();
    itr.next();
    itr.remove();
    itr.remove();
    itr.forEachRemaining(bucket::add);
    assertEquals(List.of(2, 3, 4), bucket);
  }

  @Test
  public void set00() {
    var wl0 = WiredList.of(0, 1, 2);
    var itr = wl0.wiredIterator(true);
    itr.next();
    itr.set(7);
    itr.next();
    itr.set(6);
    itr.next();
    itr.set(5);
    assertFalse(itr.hasNext());
    assertEquals(List.of(5, 6, 7), wl0);
  }

  @Test
  public void insertBefore00() {
    var wl0 = WiredList.of(0, 1, 2);
    var itr = wl0.wiredIterator(true);
    itr.next();
    itr.insertBefore(3);
    assertEquals(4, wl0.size());
    assertEquals(List.of(0, 1, 2, 3), wl0);
  }

  @Test
  public void insertBefore01() {
    var wl0 = WiredList.of(0, 1, 2);
    var itr = wl0.wiredIterator(true);
    itr.next();
    itr.next();
    itr.next();
    itr.insertBefore(9);
    assertEquals(List.of(0, 9, 1, 2), wl0);
  }

  @Test
  public void insertAfter00() {
    var wl0 = WiredList.of(0, 1, 2);
    var itr = wl0.wiredIterator(true);
    itr.next();
    assertEquals(2, itr.index());
    itr.insertAfter(3); // current element now right-shifted!
    assertEquals(3, itr.index());
    itr.next();
    assertEquals(2, itr.index());
    assertEquals(List.of(0, 1, 3, 2), wl0);
  }

  @Test
  public void insertAfter01() {
    var wl0 = WiredList.of(0, 1, 2);
    var itr = wl0.wiredIterator(true);
    itr.next();
    assertEquals(2, itr.index());
    itr.next();
    assertEquals(1, itr.index());
    itr.next();
    assertEquals(0, itr.index());
    itr.insertAfter(9); // current element now right-shifted!
    assertEquals(1, itr.index());
    itr.next();
    assertEquals(0, itr.index());
    assertEquals(List.of(9, 0, 1, 2), wl0);
  }

  @Test
  public void insertBeforeAfter00() {
    var wl0 = WiredList.of(0, 1, 2);
    var itr = wl0.wiredIterator(true);
    itr.next();
    assertEquals(2, itr.index());
    itr.insertAfter(9); // current element now right-shifted!
    assertEquals(3, itr.index());
    itr.insertBefore(8); // current element _NOT_ right-shifted!
    assertEquals(3, itr.index());
    assertEquals(5, wl0.size());
    assertEquals(List.of(0, 1, 9, 2, 8), wl0);
  }

  @Test
  public void emptyList00() {
    var wl0 = WiredList.of();
    var itr = wl0.wiredIterator(false);
    assertFalse(itr.hasNext());
  }

  @Test
  public void emptyList01() {
    var wl0 = WiredList.of();
    var itr = wl0.wiredIterator(true);
    assertFalse(itr.hasNext());
  }

}
