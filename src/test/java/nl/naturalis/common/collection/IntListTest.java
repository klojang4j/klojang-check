package nl.naturalis.common.collection;

import nl.naturalis.common.ClassMethods;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntListTest {

  @Test
  public void test00() {
    List<Integer> orig = List.of(3, 5, 7, 9, 11);
    IntList intList = IntList.copyOf(orig);
    assertEquals(IntList.of(3, 5, 7, 9, 11), intList);
  }

  @Test
  public void test01() {
    IntArrayList ial = new IntArrayList(3);
    ial.add(3);
    ial.add(5);
    ial.add(7);
    ial.add(9);
    ial.add(11);
    assertEquals(IntList.of(3, 5, 7, 9, 11), ial);
    assertEquals(ial, IntList.of(3, 5, 7, 9, 11));
  }

}