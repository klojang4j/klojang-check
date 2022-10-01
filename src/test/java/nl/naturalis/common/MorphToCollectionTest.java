package nl.naturalis.common;

import org.junit.Test;

import java.io.File;
import java.util.*;

import static nl.naturalis.common.ArrayMethods.pack;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class MorphToCollectionTest {

  @Test
  public void test00() {
    Collection<String> c = MorphToCollection.morph(pack("a", "b", "c"),
        Collection.class);
    assertSame(ArrayList.class, c.getClass());
    assertEquals(List.of("a", "b", "c"), c);
  }

  @Test
  public void test01() {
    Collection<String> c = MorphToCollection.morph("a", Collection.class);
    assertEquals(List.of("a"), c);
  }

  @Test
  public void test02() {
    Collection<String> c = MorphToCollection.morph(pack("a", "b", "c"),
        LinkedList.class);
    assertSame(LinkedList.class, c.getClass());
    assertEquals(List.of("a", "b", "c"), c);
  }

  @Test
  public void test03() {
    Collection<String> c = MorphToCollection.morph(1, LinkedList.class);
    assertSame(LinkedList.class, c.getClass());
    assertEquals(Arrays.asList(1), c);
    // Nice, that's type erasure for you
  }

  @Test
  public void test04() {
    Collection<Integer> c = MorphToCollection.morph(List.of(1, 2, 3),
        LinkedHashSet.class);
    assertSame(LinkedHashSet.class, c.getClass());
    assertEquals(Set.of(1, 2, 3), c);
  }

  @Test
  public void test05() {
    Collection<String> c = MorphToCollection.morph(pack("a", "b", "c"), Set.class);
    assertSame(HashSet.class, c.getClass());
    assertEquals(Set.of("a", "b", "c"), c);
  }

  @Test(expected = TypeConversionException.class)
  public void test06() {
    MorphToCollection.morph(List.of(1, 2, 3), Deque.class);
  }

  @Test
  public void test07() {
    Collection<String> c = MorphToCollection.morph(pack("a", "b", "c"),
        ArrayDeque.class);
    assertSame(ArrayDeque.class, c.getClass());
    assertEquals(List.of("a", "b", "c"), new ArrayList<>(c));
  }

  @Test
  public void test08() {
    Collection<String> c = MorphToCollection.morph("Hello, World", Collection.class);
    assertEquals(List.of("Hello, World"), c);
  }

  @Test
  public void test09() {
    Collection<Long> c = MorphToCollection.morph(33L, Set.class);
    assertEquals(Set.of(33L), c);
  }

  @Test(expected = TypeConversionException.class)
  public void test10() {
    // Not a public type
    MorphToCollection.morph("Hi there", List.of("foo").getClass());
  }

  @Test(expected = TypeConversionException.class)
  public void test11() {
    // Missing no-arg constructor
    MorphToCollection.morph("Hi there", File.class);
  }

}
