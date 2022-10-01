package nl.naturalis.common.collection;

import nl.naturalis.common.util.MutableInt;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import static nl.naturalis.common.ArrayMethods.ints;
import static org.junit.Assert.*;

public class UnmodifiableIntListTest {

  @Test
  public void get00() {
    IntList il = IntList.of(0, 1, 2, 3, 4, 5);
    assertEquals(2, il.get(2));
  }

  @Test
  public void get01() {
    IntList il = IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    assertEquals(2, il.get(2));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void set() {
    IntList.of(0, 1, 2, 3, 4, 5).set(2, 4);
  }

  @Test
  public void indexOf00() {
    IntList il = IntList.of(0, 1, 2, 3, 4, 0, 1, 2, 3, 4);
    assertEquals(OptionalInt.of(1), il.indexOf(1));
  }

  @Test
  public void indexOf01() {
    IntList il = IntList.of(0, 1, 2, 3, 4, 0, 1, 2, 3, 4);
    assertEquals(OptionalInt.empty(), il.indexOf(45298));
  }

  @Test
  public void lastIndexOf00() {
    IntList il = IntList.of(0, 1, 2, 3, 4, 0, 1, 2, 3, 4);
    assertEquals(OptionalInt.of(6), il.lastIndexOf(1));
  }

  @Test
  public void lastIndexOf01() {
    IntList il = IntList.of(0, 1, 2, 3, 4, 0, 1, 2, 3, 4);
    assertEquals(OptionalInt.empty(), il.lastIndexOf(17713));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void add00() {
    IntList.of(0, 1, 2, 3, 4, 5).add(6);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void add01() {
    IntList.of(0, 1, 2, 3, 4, 5).add(8, 6);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void addAll00() {
    IntList.of(0, 1, 2, 3, 4, 5).addAll(IntList.of(6, 7));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void addAll01() {
    IntList.of(0, 1, 2, 3, 4, 5).addAll(ints(6, 7));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void addAll02() {
    IntList.of(0, 1, 2, 3, 4, 5).addAll(100, IntList.of(6, 7));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void addAll03() {
    IntList.of(0, 1, 2, 3, 4, 5).addAll(3, ints(6, 7));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void removeByIndex() {
    IntList.of(0, 1, 2, 3, 4, 5).removeByIndex(7000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void removeByValue() {
    IntList.of(0, 1, 2, 3, 4, 5).removeByValue(7000);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void removeAll00() {
    IntList.of(0, 1, 2, 3, 4, 5).removeAll(1, 2, 3);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void removeAll01() {
    IntList.of(0, 1, 2, 3, 4, 5).removeAll(IntList.of());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void removeAll02() {
    IntList.of(0, 1, 2, 3, 4, 5).removeAll(List.of(1, 2, 3));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void retainAll00() {
    IntList.of(0, 1, 2, 3, 4, 5).retainAll(1, 2, 3);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void retainAll01() {
    IntList.of(0, 1, 2, 3, 4, 5).retainAll(IntList.of(9, 8, 7));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void retainAll02() {
    IntList.of(0, 1, 2, 3, 4, 5).retainAll(List.of(1, 2, 3));
  }

  @Test
  public void size() {
    assertEquals(0, IntList.of().size());
    assertEquals(8, IntList.of(0, 1, 2, 3, 4, 5, 6, 7).size());
    assertEquals(5, IntList.ofElements(ints(1, 2, 3, 4, 5)).size());
  }

  @Test
  public void isEmpty() {
    assertTrue(IntList.of().isEmpty());
    assertFalse(IntList.of(0, 1, 2, 3, 4, 5, 6, 7).isEmpty());
    assertFalse(IntList.ofElements(ints(1, 2, 3, 4, 5)).isEmpty());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void clear() {
    IntList.ofElements(ints(1, 2, 3, 4, 5)).clear();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void trim() {
    IntList.ofElements(ints(1, 2, 3, 4, 5)).trim(9);
  }

  @Test
  public void capacity() {
    assertEquals(0, IntList.of().capacity());
    assertEquals(8, IntList.of(0, 1, 2, 3, 4, 5, 6, 7).capacity());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void setCapacity() {
    IntList.of(0, 1, 2, 3, 4, 5, 6, 7).setCapacity(3);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void sort() {
    IntList.of(45, 55, 12).sort();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void sortDescending() {
    IntList.of(45, 55, 12).sortDescending();
  }

  @Test
  public void toArray00() {
    assertArrayEquals(ints(45, 55, 12), IntList.of(45, 55, 12).toArray());
  }

  @Test
  public void toArray01() {
    assertArrayEquals(ints(), IntList.of().toArray());
  }

  @Test
  public void toGenericList() {
    assertEquals(List.of(45, 55, 12), IntList.of(45, 55, 12).toGenericList());
  }

  @Test
  public void stream() {
    assertEquals(20, IntList.of(3, 7, 10).stream().sum());
  }

  @Test
  public void forEach() {
    MutableInt mi = new MutableInt();
    IntList.of(3, 7, 10).forEach(mi::plusIs);
    assertEquals(20, mi.get());
  }

  @Test
  public void forEachThrowing() {
    MutableInt mi = new MutableInt();
    IntList.of(3, 7, 10).forEachThrowing(mi::plusIs);
    assertEquals(20, mi.get());
  }

  @Test
  public void equals00() {
    IntList il = IntList.copyOf(List.of(1, 2, 3, 4, 5));
    assertTrue(il.equals(il));
    assertTrue(il.equals(new IntArrayList(IntList.of(1, 2, 3, 4, 5))));
    assertFalse(il.equals(new IntArrayList(IntList.of(1, 2, 3, 4, 5, 6))));
    assertFalse(il.equals(new IntArrayList(IntList.of(1, 2, 3, 7, 8))));
    assertFalse(il.equals("Hello World"));
  }

  @Test
  public void hashCode00() {
    assertEquals(1, IntList.of().hashCode());
    assertEquals(73, IntList.of(42).hashCode());
    IntList il = IntList.of(1, 2, 3, 4);
    assertEquals(955331, il.hashCode());
    assertNotEquals(42, il.hashCode());
  }

  @Test
  public void toString00() {
    IntList il = IntList.of(1, 3, 4, -33);
    assertEquals("[1, 3, 4, -33]", il.toString());
    assertNotEquals("foo", il.toString());
  }

}