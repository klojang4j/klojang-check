package nl.naturalis.common;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;

import org.junit.Test;

import static nl.naturalis.common.ArrayMethods.doubles;
import static nl.naturalis.common.ClassMethods.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class ClassMethodsTest {

  @Test
  public void isA00() {
    assertTrue(ClassMethods.isSubtype(String.class, String.class));
    assertTrue(ClassMethods.isSubtype(String.class, Object.class));
    assertTrue(ClassMethods.isSubtype(String.class, CharSequence.class));
    assertFalse(ClassMethods.isSubtype(Object.class, String.class));
    assertFalse(ClassMethods.isSubtype(CharSequence.class, String.class));
    assertTrue(ClassMethods.isSubtype(String.class, String.class));
    assertFalse(ClassMethods.isSubtype(short.class, int.class));
    assertTrue(ClassMethods.isSubtype(Serializable.class, Object.class));
    assertTrue(ClassMethods.isSubtype(Function.class, Object.class));
  }

  public void isA01() {
    assertTrue(ClassMethods.isA("Foo", String.class));
    assertTrue(ClassMethods.isA("Foo", Object.class));
    assertTrue(ClassMethods.isA("Foo", CharSequence.class));
    assertTrue(ClassMethods.isA(new Object(), String.class));
    assertFalse(ClassMethods.isA((short) 42, int.class));
  }

  @Test // Interesting: Enum.class returns false for Class::isEnum
  public void isEnum01() {
    assertFalse(Enum.class.isEnum());
    assertTrue(ClassMethods.isSubtype(Enum.class, Enum.class));
  }

  @Test
  public void getAllInterfaces00() {
    Set<Class<?>> expected = Set.of(NavigableSet.class,
        Cloneable.class,
        Serializable.class,
        SortedSet.class,
        Set.class,
        Collection.class,
        Iterable.class);
    Set<Class<?>> actual = ClassMethods.getAllInterfaces(TreeSet.class);
    // System.out.println(implode(actual.toArray(), "\n"));
    assertEquals(expected, actual);
  }

  @Test
  public void getAllInterfaces01() {
    Set<Class<?>> expected = Set.of(SortedSet.class,
        Set.class,
        Collection.class,
        Iterable.class);
    Set<Class<?>> actual = ClassMethods.getAllInterfaces(NavigableSet.class);
    // System.out.println(implode(actual.toArray(), "\n"));
    assertEquals(expected, actual);
  }

  @Test
  public void getTypeDefaultIf00() {
    assertNull(ClassMethods.getTypeDefault(Class.class));
    assertNull(ClassMethods.getTypeDefault(Object.class));
    assertNull(ClassMethods.getTypeDefault(Cloneable.class));
    assertEquals((short) 0, (short) ClassMethods.getTypeDefault(short.class));
    assertEquals(0F, (float) ClassMethods.getTypeDefault(float.class), 0F);
  }

  @Test
  public void describe00() {
    assertEquals("null", ClassMethods.describe(null));
    assertEquals("ArrayList[5]",
        ClassMethods.describe(CollectionMethods.initializeList(5, "foo")));
    assertEquals("SetN[5]", ClassMethods.describe(Set.of(1, 2, 3, 4, 5)));
    assertEquals("MapN[2]", ClassMethods.describe(Map.of("foo", 1, "bar", 2)));
    assertEquals("double[4]", ClassMethods.describe(doubles(1.56, 0, 2.3, 4.8)));
    assertEquals("File", ClassMethods.describe(new File("fou/bar")));
    assertEquals("File.class", ClassMethods.describe(File.class));
  }

  @Test
  public void isAutoboxedAs00() {
    assertFalse(isAutoBoxedAs(String.class, Integer.class));
    assertFalse(isAutoBoxedAs(int.class, int.class));
    assertFalse(isAutoBoxedAs(int.class, File.class));
    assertTrue(isAutoBoxedAs(int.class, Integer.class));
    assertTrue(isAutoBoxedAs(char.class, Character.class));
  }

  @Test
  public void unbox00() {
    assertEquals(int.class, unbox(Integer.class));
    assertEquals(int.class, unbox(int.class));
    assertEquals(File.class, unbox(File.class));
  }

  @Test
  public void isWrapper00() {
    assertTrue(isWrapper(Integer.class));
    assertTrue(isWrapper(Short.class));
    assertTrue(isWrapper(Byte.class));
    assertTrue(isWrapper(Double.class));
    assertTrue(isWrapper(Float.class));
    assertTrue(isWrapper(Long.class));
    assertTrue(isWrapper(Boolean.class));
    assertTrue(isWrapper(Character.class));
    assertTrue(isWrapper(Character.class));
    assertTrue(isWrapper(Void.class));
    assertFalse(isWrapper(int.class));
    assertFalse(isWrapper(BigInteger.class));
    assertFalse(isWrapper(void.class));
  }

  @Test
  public void isPrimitiveNumber00() {
    assertTrue(isPrimitiveNumber(int.class));
    assertTrue(isPrimitiveNumber(double.class));
    assertTrue(isPrimitiveNumber(float.class));
    assertTrue(isPrimitiveNumber(long.class));
    assertTrue(isPrimitiveNumber(short.class));
    assertTrue(isPrimitiveNumber(byte.class));
    assertFalse(isPrimitiveNumber(Integer.class));
    assertFalse(isPrimitiveNumber(void.class));
    assertFalse(isPrimitiveNumber(char.class));
    assertFalse(isPrimitiveNumber(Character.class));
    assertFalse(isPrimitiveNumber(File.class));
  }

  @Test(expected = ClassCastException.class)
  public void cast01() {
    // look mum, what I can do!
    ByteBuffer osw = cast(new File("/fou/bar"));
  }

}
