package nl.naturalis.common.x.collection;

import nl.naturalis.common.collection.DuplicateValueException;
import nl.naturalis.common.util.MutableInt;
import org.junit.Test;

import java.util.*;

import static nl.naturalis.common.ArrayMethods.pack;
import static org.junit.Assert.*;

public class ArraySetTest {

  @Test
  public void of00() {
    ArraySet<Integer> set = ArraySet.of(pack(0, 1, 2, 3, 4), true);
    assertEquals(Set.of(0, 1, 2, 3, 4), set);
    set = ArraySet.of(pack(0, 1, 2, 3, 4), false);
  }

  @Test(expected = DuplicateValueException.class)
  public void of01() {
    ArraySet.of(pack(0, 1, 2, 3, 4, 4), false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void of02() {
    ArraySet.of(pack(0, null, 1, 1, 2, 3, 4), false);
  }

  @Test
  public void copyOfList00() {
    ArraySet<Integer> set = ArraySet.copyOf(List.of(0, 1, 2, 3, 4), true);
    assertEquals(Set.of(0, 1, 2, 3, 4), set);
    set = ArraySet.copyOf(List.of(0, 1, 2, 3, 4), false);
    assertEquals(Set.of(0, 1, 2, 3, 4), set);
  }

  @Test(expected = DuplicateValueException.class)
  public void copyOfList01() {
    ArraySet.copyOf(Arrays.asList(0, 0, 1, 1, 2, 3, 4), false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void copyOfList02() {
    ArraySet.copyOf(Arrays.asList(0, null, 1, 1, 2, 3, 4), false);
  }

  @Test
  public void copyOfSet00() {
    ArraySet<Integer> set = ArraySet.copyOf(Set.of(0, 1, 2, 3, 4), true);
    assertEquals(Set.of(0, 1, 2, 3, 4), set);
    set = ArraySet.copyOf(Set.of(0, 1, 2, 3, 4), false);
    assertEquals(Set.of(0, 1, 2, 3, 4), set);
  }

  @Test(expected = IllegalArgumentException.class)
  public void copyOfSet02() {
    ArraySet.copyOf(new HashSet<>(Arrays.asList(0, null, 1, 2, 3, 4)), false);
  }

  @Test
  public void contains00() {
    ArraySet<Integer> set = ArraySet.copyOf(Set.of(0, 1, 2, 3, 4, 5), true);
    assertTrue(set.contains(3));
    assertFalse(set.contains(10));
    assertFalse(set.contains(null));
  }

  @Test
  public void containsAll00() {
    ArraySet<Integer> set = ArraySet.copyOf(Set.of(0, 1, 2, 3, 4, 5), true);
    assertTrue(set.containsAll(List.of()));
    assertTrue(set.containsAll(List.of(3)));
    assertTrue(set.containsAll(List.of(3, 5)));
    assertTrue(set.containsAll(List.of(3, 4, 5)));
    assertFalse(set.containsAll(List.of(3, 4, 5, 7)));
  }

  @Test
  public void iterator00() {
    ArraySet<Integer> set = ArraySet.copyOf(List.of(0, 1, 2, 3, 4, 5), true);
    MutableInt mi = new MutableInt();
    for (Iterator<Integer> itr = set.iterator(); itr.hasNext(); ) {
      assertEquals(mi.pp(), (int) itr.next());
    }
  }

  @Test
  public void toArray00() {
    ArraySet<Integer> set = ArraySet.copyOf(List.of(0, 1, 2, 3, 4, 5), true);
    assertEquals(pack(0, 1, 2, 3, 4, 5), set.toArray());
    assertEquals(pack(0, 1, 2, 3, 4, 5), set.toArray(Integer[]::new));
    assertEquals(pack(0, 1, 2, 3, 4, 5, null), set.toArray(new Integer[7]));
  }

}
