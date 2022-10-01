package nl.naturalis.common.collection;

import static nl.naturalis.common.ArrayMethods.ints;
import static nl.naturalis.common.util.ResizeMethod.ADD;
import static nl.naturalis.common.util.ResizeMethod.PERCENTAGE;
import static org.junit.Assert.*;

import nl.naturalis.common.util.MutableInt;
import org.junit.Test;

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

public class IntArrayListTest {

  @Test
  public void constructor00() {
    IntList il = IntList.of(0, 1, 2, 3, 4);
    IntArrayList ial = new IntArrayList(il);
    assertEquals(il, ial);
    assertEquals(List.of(0, 1, 2, 3, 4), ial.toGenericList());
  }

  @Test
  public void size00() {
    IntArrayList list = new IntArrayList(2);
    assertTrue("01", list.isEmpty());
    list.add(1);
    assertEquals("02", 1, list.size());
    list.addAll(ints(2, 3));
    assertEquals("03", 3, list.size());
    assertEquals("04", 4, list.capacity());
  }

  @Test
  public void get00() {
    IntArrayList list = new IntArrayList(2);
    list.addAll(ints(42, 42, 7, 8, 13));
    assertEquals("01", 5, list.size());
    assertEquals("02", 5, list.capacity());
    list.add(12);
    assertEquals("03", 10, list.capacity());
    assertEquals("04", 42, list.get(1));
    assertEquals("05", 13, list.get(4));
    assertEquals("06", 12, list.get(5));
  }

  @Test
  public void add00() {
    // append
    IntArrayList list = new IntArrayList(2, ADD, 2);
    list.add(0);
    assertEquals(IntList.of(0), list);
    list.add(1);
    assertEquals(IntList.of(0, 1), list);
    list.add(2);
    assertEquals(IntList.of(0, 1, 2), list);
    list.add(4);
    assertEquals(IntList.of(0, 1, 2, 4), list);
    // insert
    list.add(3, 3);
    assertEquals(IntList.of(0, 1, 2, 3, 4), list);
    list.add(0, -1);
    assertEquals(IntList.of(-1, 0, 1, 2, 3, 4), list);
    list.add(6, 5);
    assertEquals(IntList.of(-1, 0, 1, 2, 3, 4, 5), list);
    list.add(0, -2);
    assertEquals(IntList.of(-2, -1, 0, 1, 2, 3, 4, 5), list);
    list.add(list.size(), 6);
    assertEquals(IntList.of(-2, -1, 0, 1, 2, 3, 4, 5, 6), list);
  }

  @Test
  public void equals00() {
    IntArrayList list0 = new IntArrayList();
    list0.addAll(ints(0, 1, 2, 3, 4, 5));
    IntArrayList list1 = new IntArrayList();
    list1.addAll(ints(0, 1, 2, 3, 4, 5));
    IntArrayList list2 = new IntArrayList();
    list2.addAll(ints(1, 2, 3, 4, 5));
    IntList list3 = IntList.of(0, 1, 2, 3, 4, 5);
    IntList list4 = IntList.of(0, 1, 2, 3, 4, 5);
    IntList list5 = IntList.of(1, 2, 3, 4, 5);
    assertTrue(list0.equals(list0));
    assertFalse(list0.equals(null));
    assertFalse(list0.equals("hello"));
    assertTrue(list0.equals(list1));
    assertFalse(list0.equals(list2));
    assertTrue(list0.equals(list4));
    assertFalse(list0.equals(list5));
  }

  @Test
  public void hashCode00() {
    IntArrayList list0 = new IntArrayList();
    list0.addAll(ints(0, 1, 2, 3, 4, 5));
    assertEquals(986115, list0.hashCode());
  }

  @Test
  public void addAll00() {
    IntArrayList list0 = new IntArrayList(100);
    list0.addAll(ints(0, 1, 2, 3, 4, 5));
    assertEquals(6, list0.size());
    list0.addAll(ints(6, 7, 8, 9));
    assertEquals(10, list0.size());
    list0.addAll(IntList.of(10, 11));
    assertEquals(12, list0.size());
    IntArrayList list1 = new IntArrayList(IntList.of(12, 13, 14));
    list0.addAll(list1);
    assertEquals(15, list0.size());
    assertEquals(IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
        list0);
    assertEquals(list0,
        IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
  }

  @Test
  public void addAll01() {
    IntArrayList list0 = new IntArrayList(10, PERCENTAGE, 10);
    list0.addAll(ints(0, 1, 2, 3, 4, 5));
    assertEquals(6, list0.size());
    list0.addAll(ints(6, 7, 8, 9));
    assertEquals(10, list0.size());
    list0.addAll(IntList.of(10, 11));
    assertEquals(12, list0.size());
    IntArrayList list1 = new IntArrayList(IntList.of(12, 13, 14));
    list0.addAll(list1);
    assertEquals(15, list0.size());
    assertEquals(IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14),
        list0);
    assertEquals(list0,
        IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
  }

  @Test
  public void addAll02() {
    IntArrayList list0 = new IntArrayList(10, PERCENTAGE, 120);
    list0.addAll(ints(0, 1, 2, 3, 4, 5));
    assertEquals(6, list0.size());
    IntArrayList list1 = new IntArrayList(10, PERCENTAGE, 120);
    list1.addAll(ints(6, 7));
    assertEquals(2, list1.size());
    IntArrayList list2 = new IntArrayList(list1);
    assertEquals(2, list2.size());
    list0.addAll(list2);
    assertEquals(8, list0.size());
    assertEquals(IntList.of(0, 1, 2, 3, 4, 5, 6, 7), list0);
    list0.addAll(list0);
    assertEquals(16, list0.size());
  }

  @Test
  public void addAll03() {
    IntArrayList list = new IntArrayList();
    list.addAll(0, ints(0, 1, 2));
    list.addAll(3, list);
    assertEquals(IntList.of(0, 1, 2, 0, 1, 2), list);
    list.addAll(3, list);
    assertEquals(IntList.of(0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2), list);
    list.addAll(3, ints(3));
    assertEquals(IntList.of(0, 1, 2, 3, 0, 1, 2, 0, 1, 2, 0, 1, 2), list);
  }

  @Test
  public void set00() {
    IntArrayList list = new IntArrayList();
    list.addAll(ints(0, 1, 2, 3, 4, 5));
    list.set(0, 6);
    assertEquals(6, list.get(0));
    list.add(6);
    assertEquals(6, list.get(list.size() - 1));
  }

  @Test
  public void toArray00() {
    IntArrayList list = new IntArrayList(50);
    list.addAll(ints(0, 1, 2, 3, 4, 5));
    assertArrayEquals(ints(0, 1, 2, 3, 4, 5), list.toArray());
  }

  @Test
  public void stream00() {
    IntArrayList list = new IntArrayList(50);
    list.addAll(ints(0, 1, 2, 3, 4, 5));
    assertFalse(list.stream().anyMatch(i -> i > 100));
  }

  @Test
  public void toString00() {
    IntArrayList list = new IntArrayList(50);
    assertEquals("[]", list.toString());
    list.addAll(ints(0, 1, 2, 3, 4, 5));
    assertEquals("[0, 1, 2, 3, 4, 5]", list.toString());
  }

  @Test
  public void clear00() {
    IntArrayList list = new IntArrayList(3, 3);
    assertTrue(list.isEmpty());
    list.addAll(ints(0, 1, 2, 3, 4, 5));
    assertFalse(list.isEmpty());
    list.clear();
    assertTrue(list.isEmpty());
  }

  @Test
  public void forEach00() {
    IntArrayList list = new IntArrayList(4, 4);
    list.addAll(ints(0, 1, 2, 3, 4, 5));
    MutableInt mi = new MutableInt();
    list.forEach(i -> mi.plusIs(i));
    assertEquals(15, mi.get());
  }

  @Test
  public void forEachThrowing00() {
    IntArrayList list = new IntArrayList(50);
    list.addAll(ints(0, 1, 2, 3, 4, 5));
    MutableInt mi = new MutableInt();
    list.forEachThrowing(i -> mi.plusIs(i));
    assertEquals(15, mi.get());
  }

  @Test
  public void trimAndResize00() {
    IntArrayList list = new IntArrayList();
    list.addAll(IntList.ofElements(ints(0, 1, 2, 3, 4, 5)));
    assertEquals(6, list.size());
    list.trim(2);
    assertEquals(2, list.size());
    list.add(7);
    assertEquals(IntList.of(0, 1, 7), list);
    list.add(0, 3);
    assertEquals(IntList.of(3, 0, 1, 7), list);
    list.setCapacity(100);
    assertEquals(IntList.of(3, 0, 1, 7), list);
    list.setCapacity(2);
    assertEquals(2, list.capacity());
    assertEquals(IntList.of(3, 0), list);
    list.setCapacity(0);
    assertEquals(0, list.capacity());
    assertEquals(IntList.of(), list);
    list.addAll(IntList.ofElements(ints(0, 1, 2, 3, 4, 5)));
    assertEquals(IntList.of(0, 1, 2, 3, 4, 5), list);
    int cap = list.capacity();
    list.setCapacity(list.capacity());
    assertEquals(cap, list.capacity());
  }

  @Test
  public void removeByIndex00() {
    IntList list = new IntArrayList();
    list.addAll(ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    list.removeByIndex(9);
    assertEquals(IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8), list);
    list.removeByIndex(8);
    assertEquals(IntList.of(0, 1, 2, 3, 4, 5, 6, 7), list);
    list.removeByIndex(0);
    assertEquals(IntList.of(1, 2, 3, 4, 5, 6, 7), list);
    list.removeByIndex(2);
    assertEquals(IntList.of(1, 2, 4, 5, 6, 7), list);
    list.removeByIndex(4);
    assertEquals(IntList.of(1, 2, 4, 5, 7), list);
    list.removeByIndex(1);
    assertEquals(IntList.of(1, 4, 5, 7), list);
    list.removeByIndex(0);
    assertEquals(IntList.of(4, 5, 7), list);
    list.removeByIndex(0);
    assertEquals(IntList.of(5, 7), list);
    list.removeByIndex(0);
    assertEquals(IntList.of(7), list);
    list.removeByIndex(0);
    assertEquals(IntList.of(), list);
  }

  @Test
  public void removeByValue00() {
    IntList list = new IntArrayList();
    list.addAll(ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    assertTrue(list.removeByValue(1));
    assertTrue(list.removeByValue(9));
    assertTrue(list.removeByValue(0));
    assertTrue(list.removeByValue(5));
    assertTrue(list.removeByValue(8));
    assertFalse(list.removeByValue(8));
    assertTrue(list.removeByValue(7));
    assertTrue(list.removeByValue(2));
    assertEquals(IntList.of(3, 4, 6), list);
  }

  @Test
  public void indexOf00() {
    IntList list = new IntArrayList(IntList.of(0, 0, 2, 2, 4, 4));
    assertEquals(OptionalInt.of(0), list.indexOf(0));
    assertEquals(OptionalInt.of(1), list.lastIndexOf(0));
    assertEquals(OptionalInt.of(2), list.indexOf(2));
    assertEquals(OptionalInt.of(3), list.lastIndexOf(2));
    assertEquals(OptionalInt.of(4), list.indexOf(4));
    assertEquals(OptionalInt.of(5), list.lastIndexOf(4));
    assertEquals(OptionalInt.empty(), list.indexOf(1000));
    assertEquals(OptionalInt.empty(), list.lastIndexOf(1000));
  }

  @Test
  public void removeAll00() {
    IntList list = new IntArrayList();
    list.addAll(ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    assertTrue(list.removeAll(1, 3, 5, 7, 9, 1000));
    assertEquals(IntList.of(0, 2, 4, 6, 8), list);
    assertFalse(list.removeAll(1000, 1001, 1002));
    assertEquals(IntList.of(0, 2, 4, 6, 8), list);
    assertTrue(list.removeAll(IntList.of(0)));
    assertEquals(IntList.of(2, 4, 6, 8), list);
    assertFalse(list.removeAll(IntList.of()));
    assertFalse(list.removeAll(List.of()));
    assertTrue(list.removeAll(List.of(2, 6, 1000)));
    assertEquals(IntList.of(4, 8), list);
    assertTrue(list.removeAll(Set.of(4, 8, 1000)));
    assertEquals(IntList.of(), list);
  }

  @Test
  public void retainAll00() {
    IntList list = new IntArrayList();
    list.addAll(ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    assertTrue(list.retainAll(1, 3, 5, 7, 9, 1000));
    assertEquals(IntList.of(1, 3, 5, 7, 9), list);
    assertFalse(list.retainAll(1, 3, 5, 7, 9, 1000, 2000));
    assertTrue(list.retainAll(IntList.of(1, 9)));
    assertEquals(IntList.of(1, 9), list);
    assertFalse(list.retainAll(Set.of(1, 9)));
    assertTrue(list.retainAll(Set.of(1000, 2000)));
    assertTrue(list.isEmpty());
  }

  @Test
  public void sort00() {
    IntList list = new IntArrayList();
    list.addAll(ints(3, 0, 2, 5, 4, 1));
    list.sortDescending();
    assertEquals(IntList.of(5, 4, 3, 2, 1, 0), list);
    list.sort();
    assertEquals(IntList.of(0, 1, 2, 3, 4, 5), list);
  }

}
