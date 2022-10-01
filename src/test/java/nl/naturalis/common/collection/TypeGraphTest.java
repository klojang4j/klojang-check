package nl.naturalis.common.collection;

import nl.naturalis.common.x.collection.ArraySet;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

import static nl.naturalis.common.CollectionMethods.implode;
import static org.junit.Assert.*;

public class TypeGraphTest {

  @Test
  public void noAutoExpandPlease() {
    Map<Class<?>, String> src = Map.of(Integer.class, "Integer");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertEquals(1, m.size());
    assertTrue(m.containsKey(int.class));
    assertEquals(1, m.size());
  }

  @Test
  public void keySet00() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(Byte.class, "Byte")
        .add(Integer.class, "Integer")
        .add(Double.class, "Double")
        .add(String.class, "String")
        .add(Object.class, "Object")
        .add(Short.class, "Short")
        .add(Number.class, "Number")
        .freeze();
    Set<Class<?>> keys = m.keySet();
    System.out.println("keySet00: " + implode(keys, Class::getSimpleName));
    List<Class<?>> expected = List.of(Object.class,
        String.class,
        Number.class,
        Byte.class,
        Integer.class,
        Double.class,
        Short.class);
    assertEquals(expected, new ArrayList<>(keys));
  }

  @Test
  public void keySet01() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(Integer.class, "Integer")
        .add(Byte.class, "Byte")
        .add(Double.class, "Double")
        .add(Number.class, "Number")
        .add(String.class, "String")
        .add(Object.class, "Object")
        .add(Short.class, "Short")
        .freeze();
    Set<Class<?>> keys = m.keySet();
    System.out.println("keySet01: " + implode(keys, Class::getSimpleName));
    List<Class<?>> expected = List.of(Object.class,
        Number.class,
        String.class,
        Integer.class,
        Byte.class,
        Double.class,
        Short.class);
    assertEquals(expected, new ArrayList<>(keys));
  }

  @Test
  public void keySet02() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(CharSequence.class, "CharSequence")
        .add(Integer.class, "Integer")
        .add(Number.class, "Number")
        .add(Byte.class, "Byte")
        .add(String.class, "String")
        .add(Short.class, "Short")
        .freeze();
    Set<Class<?>> keys = m.keySet();
    System.out.println("keySet02: " + implode(keys, Class::getSimpleName));
    List<Class<?>> expected = List.of(CharSequence.class,
        Number.class,
        String.class,
        Integer.class,
        Byte.class,
        Short.class);
    assertEquals(expected, new ArrayList<>(keys));
  }

  @Test
  public void entrySet00() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(Object.class, "Object")
        .add(Number.class, "Number")
        .add(Integer.class, "Integer")
        .add(Double.class, "Double")
        .add(Short.class, "Short")
        .add(Iterable.class, "Iterable")
        .add(Collection.class, "Collection")
        .add(List.class, "List")
        .add(ArrayList.class, "ArrayList")
        .add(LinkedList.class, "LinkedList")
        .add(Set.class, "Set")
        .add(HashSet.class, "HashSet")
        .add(LinkedHashSet.class, "LinkedHashSet")
        .freeze();
    Set entries = m.entrySet();
    assertEquals(13, entries.size());
  }

  @Test
  public void values00() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(Object.class, "Foo")
        .add(Number.class, "Bar")
        .add(Integer.class, "Integer")
        .add(Double.class, "Double")
        .add(Short.class, "Foo")
        .add(Iterable.class, "Bar")
        .add(Collection.class, "Collection")
        .add(List.class, "List")
        .add(ArrayList.class, "Foo")
        .add(LinkedList.class, "Bar")
        .add(Set.class, "Set")
        .add(HashSet.class, "HashSet")
        .add(LinkedHashSet.class, "Foo")
        .freeze();
    assertEquals(Set.of("Foo",
        "HashSet",
        "Bar",
        "Set",
        "Double",
        "List",
        "Collection",
        "Integer"), m.values());
    //System.out.println(m.values());
  }

  @Test
  public void copyOf00() {
    Map<Class<?>, String> src = Map.of(Number.class, "Number");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertFalse(m.isEmpty());
    assertTrue(m.containsKey(int.class));
    assertEquals("Number", m.get(int.class));
  }

  @Test
  public void copyOf01() {
    Map<Class<?>, String> src = Map.of(Number.class, "Number");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertTrue(m.containsKey(int.class));
    assertEquals("Number", m.get(int.class));
  }

  @Test
  public void copyOf02() {
    Map<Class<?>, String> src = Map.of(Number.class, "Number");
    TypeMap<String> m = TypeMap.typeGraph(src, false);
    assertFalse(m.containsKey(int.class));
    assertNull(m.get(int.class));
  }

  @Test
  public void size00() {
    TypeMap<String> m = TypeMap.typeGraph(Map.of());
    assertEquals(0, m.size());
    assertTrue(m.isEmpty());
    assertFalse(m.containsKey(Object.class));
  }

  @Test
  public void size01() {
    Map<Class<?>, String> src = Map.of(Serializable.class, "FOO");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertEquals(1, m.size());
    assertFalse(m.isEmpty());
    assertFalse(m.containsKey(Object.class));
    assertTrue(m.containsKey(Serializable.class));
    assertTrue(m.containsValue("FOO"));
  }

  @Test
  public void autobox00() {
    // When Object.class is present, anything goes. You don't even
    // need to have autoboxing turned on! See comments for TypeMap.
    Map<Class<?>, String> src = Map.of(Object.class,
        "Object",
        Serializable.class,
        "Serializable",
        Number.class,
        "Number",
        Integer.class,
        "Integer");
    TypeMap<String> m = TypeMap.typeGraph(src, false);
    assertTrue(m.containsKey(int.class));
    assertEquals("Object", m.get(int.class));
  }

  @Test // sanity check
  public void autobox01() {
    Map<Class<?>, String> src = Map.of(int.class,
        "int",
        Serializable.class,
        "Serializable",
        Number.class,
        "Number",
        Integer.class,
        "Integer");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertTrue(m.containsKey(int.class));
    assertEquals("int", m.get(int.class));
  }

  @Test
  public void autobox02() {
    Map<Class<?>, String> src = Map.of(Serializable.class,
        "Serializable",
        Number.class,
        "Number",
        Integer.class,
        "Integer");
    TypeMap<String> m = TypeMap.typeGraph(src, false);
    assertFalse(m.containsKey(int.class));
    assertNull(m.get(int.class));
  }

  @Test
  public void autobox03() {
    Map<Class<?>, String> src = Map.of(Object.class,
        "Object",
        Serializable.class,
        "Serializable",
        Number.class,
        "Number",
        Integer.class,
        "Integer");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertTrue(m.containsKey(int.class));
    assertEquals("Integer", m.get(int.class));
  }

  @Test
  public void autobox04() {
    Map<Class<?>, String> src = Map.of(Object.class,
        "Object",
        Serializable.class,
        "Serializable",
        Number.class,
        "Number");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertTrue(m.containsKey(int.class));
    assertEquals("Number", m.get(int.class));
  }

  @Test
  public void autobox05() {
    Map<Class<?>, String> src = Map.of(Object.class,
        "Object",
        Serializable.class,
        "Serializable");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertTrue(m.containsKey(int.class));
    assertEquals("Serializable", m.get(int.class));
  }

  @Test
  public void autobox06() {
    TypeMap<String> m = TypeMap.typeGraph(Map.of(Object.class, "Object"));
    assertTrue(m.containsKey(int.class));
    assertEquals("Object", m.get(int.class));
  }

  @Test
  public void autobox07() {
    TypeMap<String> m = TypeMap.typeGraph(Map.of());
    assertFalse(m.containsKey(int.class));
    assertNull(m.get(int.class));
  }

  @Test
  public void autobox08() {
    Map<Class<?>, String> src = Map.of(int[][][].class, "int[][][]");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertTrue(m.containsKey(int[][][].class));
    assertEquals("int[][][]", m.get(int[][][].class));
  }

  @Test
  public void autobox09() {
    Map<Class<?>, String> src = Map.of(Integer[][][].class, "Integer[][][]");
    TypeMap<String> m = TypeMap.typeGraph(src);
    assertTrue(m.containsKey(int[][][].class));
    assertEquals("Integer[][][]", m.get(int[][][].class));
  }

  @Test
  public void test00() {
    var m = TypeMap.<String>typeGraphBuilder().add(String.class,
        "String").add(Number.class, "Number").add(Short.class, "Short").freeze();
    assertEquals(3, m.size());
    assertTrue(m.containsValue("String"));
    assertTrue(m.containsValue("Number"));
    assertTrue(m.containsValue("Short"));
    assertFalse(m.containsValue("Integer"));
    String s = m.get(Short.class);
    assertEquals("Short", s);
    s = m.get(Integer.class);
    assertEquals("Number", s);
    assertEquals(3, m.size());
  }

  @Test
  public void test01() {
    var m = TypeMap.<String>typeGraphBuilder().add(Object.class,
        "Object").freeze();
    assertEquals(1, m.size());
    assertTrue(m.containsKey(Integer.class));
  }

  @Test
  public void test02() {
    var m = TypeMap.<String>typeGraphBuilder().add(Object.class,
        "Object").freeze();
    assertEquals(1, m.size());
    assertTrue(m.containsKey(Collection.class));
  }

  @Test
  public void test03() {
    var m = TypeMap.<String>typeGraphBuilder().add(Object.class,
        "Object").freeze();
    assertEquals(1, m.size());
    assertTrue(m.containsKey(Collection.class));
  }

  interface MyListInterface extends List<String> {}

  static class MyArrayList extends ArrayList<String> implements
      TypeTreeTest.MyListInterface {}

  @Test
  public void test04() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(ArrayList.class,
            "ArrayList")
        .add(List.class, "List")
        .add(Collection.class, "Collection")
        .freeze();
    assertEquals("ArrayList", m.get(TypeTreeTest.MyArrayList.class));
  }

  @Test
  public void test05() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(ArrayList.class,
            "ArrayList")
        .add(TypeTreeTest.MyListInterface.class, "MyListInterface")
        .freeze();
    assertEquals("ArrayList", m.get(TypeTreeTest.MyArrayList.class));
  }

  @Test
  public void test06() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(List.class, "List")
        .add(Object.class, "Object")
        .freeze();
    assertEquals("List", m.get(ArrayList.class));
  }

  @Test
  public void test07() {
    var m = TypeMap.<String>typeGraphBuilder().add(List[].class,
        "List[]").add(Object.class, "Object").freeze();
    assertEquals("List[]", m.get(ArrayList[].class));
  }

  @Test
  public void test08() {
    var m = TypeMap.<String>typeGraphBuilder().add(Object[].class,
        "Object[]").add(Object.class, "Object").freeze();
    assertEquals(2, m.size());
    assertEquals("Object[]", m.get(ArrayList[].class));
  }

  @Test
  public void test09() {
    var m = TypeMap.<String>typeGraphBuilder().add(Object[].class,
        "Object[]").add(Object.class, "Object").freeze();
    assertEquals("Object", m.get(Object.class));
  }

  @Test
  public void test10() {
    var m = TypeMap.<String>typeGraphBuilder()
        .autobox(true)
        .add(Object.class, "Object")
        .freeze();
    assertEquals("Object", m.get(int.class));
  }

  @Test
  public void test11() {
    var m = TypeMap.<String>typeGraphBuilder()
        .autobox(false)
        .add(Object.class, "Object")
        .freeze();
    assertEquals("Object", m.get(int.class));
  }

  @Test
  public void test12() {
    var m = TypeMap.<String>typeGraphBuilder()
        .autobox(false)
        .add(Object.class, "Object")
        .freeze();
    assertEquals("Object", m.get(int[].class));
  }

  @Test
  public void test13() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(Iterable.class,
            "Iterable")
        .add(Collection.class, "Collection")
        .add(Set.class, "Set")
        .add(SortedSet.class,
            "SortedSet")
        .add(String.class, "String")
        .add(Integer.class, "integer")
        .freeze();
    assertTrue(m.containsValue("Set"));
    assertFalse(m.containsValue("HashSet"));
    assertTrue(m.containsKey(HashSet.class));
    assertEquals("Set", m.get(HashSet.class));
    assertEquals("SortedSet", m.get(NavigableSet.class));
    assertEquals("Set", m.get(Set.class));
  }

  static class A0 {}

  static class A00 extends A0 {}

  static class A01 extends A0 {}

  static class A000 extends A00 {}

  static class A0000 extends A000 {}

  static class A0001 extends A0000 implements Serializable {}

  @Test
  public void test14() {
    var m = TypeMap.<String>typeGraphBuilder().add(A0.class, "A0").add(
        Serializable.class,
        "Serializable").freeze();
    assertTrue(m.containsKey(A000.class));
    assertTrue(m.containsKey(A0001.class));
    assertEquals("A0", m.get(A0001.class));
  }

  @Test
  public void test15() {
    var m = TypeMap.<String>typeGraphBuilder().add(A0.class, "A0").add(
        A000.class,
        "A000").add(Serializable.class, "Serializable").freeze();
    assertTrue(m.containsKey(A000.class));
    assertTrue(m.containsKey(A0001.class));
    assertEquals("A000", m.get(A0001.class));
  }

  @Test
  public void test16() {
    var m = TypeMap.<String>typeGraphBuilder().add(A0.class, "A0").add(
        A0000.class,
        "A0000").add(Serializable.class, "Serializable").freeze();
    assertFalse(m.containsKey(Object.class));
    assertTrue(m.containsKey(Serializable.class));
    assertEquals("A0000", m.get(A0000.class));
  }

  @Test(expected = DuplicateValueException.class)
  public void test17() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(A01.class, "A01")
        .add(A0.class, "A0")
        .add(A01.class, "FOO")
        .freeze();
  }

  @Test(expected = DuplicateValueException.class)
  public void test18() {
    var m = TypeMap.<String>typeGraphBuilder().add(A0.class, "A0").add(
        A01.class,
        "A01").add(A01.class, "A01").freeze();
  }

  @Test(expected = DuplicateValueException.class)
  public void test19() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(Object.class, "FOO")
        .add(A0.class, "A0")
        .add(Object.class, "BAR")
        .freeze();
  }

  @Test
  public void test20() {
    var m = TypeMap.<String>typeGraphBuilder()
        .add(Object[].class,
            "Object[]")
        .add(Collection[].class, "Collection[]")
        .add(Set[].class, "Set[]")
        .add(List[].class, "List[]")
        .add(AbstractList[].class, "AbstractList[]")
        .freeze();
    Class<?> c = SortedSet[].class;
    assertTrue(m.containsKey(c));
    assertEquals("Set[]", m.get(c));

    c = TreeSet[].class;
    assertTrue(m.containsKey(c));
    assertEquals("Set[]", m.get(c));

    c = List[].class;
    assertTrue(m.containsKey(c));
    assertEquals("List[]", m.get(c));

    c = ArrayList[].class;
    assertTrue(m.containsKey(c));
    assertEquals("AbstractList[]", m.get(c));

    c = WiredList[].class;
    assertTrue(m.containsKey(c));
    assertEquals("List[]", m.get(c));

    c = Serializable[].class;
    assertTrue(m.containsKey(c));
    assertEquals("Object[]", m.get(c));

    c = Iterable[].class;
    assertTrue(m.containsKey(c));
    assertEquals("Object[]", m.get(c));

    c = int[].class;
    assertTrue(m.containsKey(c));
    assertEquals("Object[]", m.get(c));

    c = Object[].class;
    assertTrue(m.containsKey(c));
    assertEquals("Object[]", m.get(c));

    c = Object.class;
    assertFalse(m.containsKey(c));
    assertNull(m.get(c));

    c = Set.class;
    assertFalse(m.containsKey(c));
    assertNull(m.get(c));

    c = AbstractList.class;
    assertFalse(m.containsKey(c));
    assertNull(m.get(c));
  }

}
