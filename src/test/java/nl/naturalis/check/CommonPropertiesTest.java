package nl.naturalis.check;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.DayOfWeek;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

import static nl.naturalis.common.ArrayMethods.ints;
import static nl.naturalis.check.CommonProperties.*;
import static org.junit.Assert.*;

@SuppressWarnings("rawtypes")
public class CommonPropertiesTest {

  @Test
  public void test00() {
    // Just something that is not a key in CommonProperties.NAMES
    Object noSuchGetter = new Object();
    String propName = formatProperty(42, "foo", noSuchGetter, IntFunction.class);
    System.out.println(propName);
    assertEquals("IntFunction.apply(foo)", propName);
    propName = formatProperty(42, null, noSuchGetter, IntFunction.class);
    System.out.println(propName);
    assertEquals("IntFunction.apply(int)", propName);
    propName = formatProperty(42, "foo", noSuchGetter, IntUnaryOperator.class);
    System.out.println(propName);
    assertEquals("IntUnaryOperator.applyAsInt(foo)", propName);
    propName = formatProperty(42, null, noSuchGetter, IntUnaryOperator.class);
    System.out.println(propName);
    assertEquals("IntUnaryOperator.applyAsInt(int)", propName);
  }

  @Test
  public void test01() {
    // Just something that is not a key in CommonProperties.NAMES
    Object noSuchGetter = new Object();
    File file = new File("/tmp/foo/bar.txt");
    String propName = formatProperty(file, "sneaky", noSuchGetter, Function.class);
    System.out.println(propName);
    assertEquals("Function.apply(sneaky)", propName);
    propName = formatProperty(file, null, noSuchGetter, Function.class);
    System.out.println(propName);
    assertEquals("Function.apply(File)", propName);
    propName = formatProperty(file, "sneaky", noSuchGetter, ToIntFunction.class);
    System.out.println(propName);
    assertEquals("ToIntFunction.applyAsInt(sneaky)", propName);
    propName = formatProperty(file, null, noSuchGetter, ToIntFunction.class);
    System.out.println(propName);
    assertEquals("ToIntFunction.applyAsInt(File)", propName);
  }

  @Test
  public void box00() {
    assertEquals(Integer.valueOf(42), box().apply(42));
    assertEquals(Integer.class, box().apply(42).getClass());
    String propName = formatProperty(42, "foo", box(), IntFunction.class);
    System.out.println(propName);
    assertEquals("Integer.valueOf(foo)", propName);
    propName = formatProperty(42, null, box(), IntFunction.class);
    System.out.println(propName);
    assertEquals("Integer.valueOf(argument)", propName);
  }

  @Test
  public void box01() {
    assertTrue(935781 == unbox().applyAsInt(935781));
    String propName = formatProperty((Integer) 42,
        "fox",
        unbox(),
        ToIntFunction.class);
    System.out.println(propName);
    assertEquals("Integer.intValue(fox)", propName);
    propName = formatProperty((Integer) 42, null, unbox(), ToIntFunction.class);
    System.out.println(propName);
    assertEquals("Integer.intValue(argument)", propName);
  }

  @Test
  public void strval00() {
    StringBuilder sb = new StringBuilder("abcd");
    assertEquals("abcd", strval().apply(sb));
    String propName = formatProperty(sb, "mercury", strval(), Function.class);
    System.out.println(propName);
    assertEquals("mercury.toString()", propName);
    propName = formatProperty(sb, null, strval(), Function.class);
    System.out.println(propName);
    assertEquals("StringBuilder.toString()", propName);
  }

  @Test
  public void strlen00() {
    assertEquals(4, strlen().applyAsInt("abcd"));
    String propName = formatProperty("abcd",
        "sombrero",
        strlen(),
        ToIntFunction.class);
    System.out.println(propName);
    assertEquals("sombrero.length()", propName);
    propName = formatProperty("abcd", null, strlen(), ToIntFunction.class);
    System.out.println(propName);
    assertEquals("String.length()", propName);
  }

  @Test
  public void toUpperCase00() {
    assertEquals("ABCD", toUpperCase().apply("abcd"));
    String propName = formatProperty("abcd",
        "mozart",
        toUpperCase(),
        Function.class);
    System.out.println(propName);
    assertEquals("mozart.toUpperCase()", propName);
    propName = formatProperty("abcd", null, toUpperCase(), Function.class);
    System.out.println(propName);
    assertEquals("String.toUpperCase()", propName);
  }

  @Test
  public void toLowerCase00() {
    assertEquals("abcd", toLowerCase().apply("AbcD"));
    String propName = formatProperty("AbcD", "alpha", toLowerCase(), Function.class);
    System.out.println(propName);
    assertEquals("alpha.toLowerCase()", propName);
    propName = formatProperty("AbcD", null, toLowerCase(), Function.class);
    System.out.println(propName);
    assertEquals("String.toLowerCase()", propName);
  }

  @Test
  public void type00() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    assertEquals(ByteArrayOutputStream.class, type().apply(baos));
    String propName = formatProperty(baos, "kentucky", type(), Function.class);
    System.out.println(propName);
    assertEquals("kentucky.getClass()", propName);
    propName = formatProperty(baos, null, type(), Function.class);
    System.out.println(propName);
    assertEquals("ByteArrayOutputStream.getClass()", propName);
  }

  @Test
  public <T> void constants00() {
    DayOfWeek[] days = (DayOfWeek[]) constants().apply((Class) DayOfWeek.class);
    assertArrayEquals(DayOfWeek.class.getEnumConstants(), days);
    String propName = formatProperty(DayOfWeek.class,
        "gamma",
        constants(),
        Function.class);
    System.out.println(propName);
    assertEquals("gamma.getEnumConstants()", propName);
    propName = formatProperty(DayOfWeek.class, null, constants(), Function.class);
    System.out.println(propName);
    assertEquals("DayOfWeek.getEnumConstants()", propName);
  }

  @Test
  public <T> void name00() {
    assertEquals("MONDAY", name().apply(DayOfWeek.MONDAY));
    String propName = formatProperty(DayOfWeek.MONDAY,
        "mercedes",
        name(),
        Function.class);
    System.out.println(propName);
    assertEquals("mercedes.name()", propName);
    propName = formatProperty(DayOfWeek.MONDAY, null, name(), Function.class);
    System.out.println(propName);
    assertEquals("DayOfWeek.name()", propName);
  }

  @Test
  public <T> void ordinal00() {
    assertEquals(0, ordinal().applyAsInt(DayOfWeek.MONDAY));
    String propName = formatProperty(DayOfWeek.MONDAY,
        "laptop",
        ordinal(),
        ToIntFunction.class);
    System.out.println(propName);
    assertEquals("laptop.ordinal()", propName);
    propName = formatProperty(DayOfWeek.MONDAY,
        null,
        ordinal(),
        ToIntFunction.class);
    System.out.println(propName);
    assertEquals("DayOfWeek.ordinal()", propName);
  }

  @Test
  public void length00() {
    assertEquals(3, length().applyAsInt(ints(3, 7, 9)));
    String propName = formatProperty(ints(3, 7, 9),
        "pharao",
        length(),
        ToIntFunction.class);
    System.out.println(propName);
    assertEquals("pharao.length", propName);
    propName = formatProperty(ints(3, 7, 9), null, length(), ToIntFunction.class);
    System.out.println(propName);
    assertEquals("int[].length", propName);
  }

  @Test
  public void mapSize00() {
    assertEquals(3, mapSize().applyAsInt(Map.of(3, 6, 7, 14, 9, 18)));
    String propName =
        formatProperty(Map.of(3, 6, 7, 14, 9, 18),
            "jordan",
            mapSize(),
            ToIntFunction.class);
    System.out.println(propName);
    assertEquals("jordan.size()", propName);
    propName = formatProperty(Map.of(3, 6, 7, 14, 9, 18),
        null,
        mapSize(),
        ToIntFunction.class);
    System.out.println(propName);
    assertEquals("MapN.size()", propName);
  }

  @Test
  public void size00() {
    assertEquals(6, size().applyAsInt(List.of(3, 6, 7, 14, 9, 18)));
    String propName =
        formatProperty(List.of(3, 6, 7, 14, 9, 18),
            "matthew",
            size(),
            ToIntFunction.class);
    System.out.println(propName);
    assertEquals("matthew.size()", propName);
    propName = formatProperty(List.of(3, 6, 7, 14, 9, 18),
        null,
        size(),
        ToIntFunction.class);
    System.out.println(propName);
    assertEquals("ListN.size()", propName);
  }

  @Test
  public void listSize00() {
    assertEquals(6, listSize().applyAsInt(List.of(3, 6, 7, 14, 9, 18)));
  }

  @Test
  public void setSize00() {
    assertEquals(6, setSize().applyAsInt(Set.of(3, 6, 7, 14, 9, 18)));
  }

  @Test
  public void keySet00() {
    Map<Integer, Integer> map = Map.of(3, 6, 7, 14, 9, 18);
    Set actual = keySet().apply((Map) map);
    Set<Integer> expected = Set.of(3, 7, 9);
    assertEquals(expected, actual);
    String propName = formatProperty(map, "jupiter", keySet(), Function.class);
    System.out.println(propName);
    assertEquals("jupiter.keySet()", propName);
    propName = formatProperty(map, null, keySet(), Function.class);
    System.out.println(propName);
    assertEquals("MapN.keySet()", propName);
  }

  @Test
  public void values00() {
    Map<Integer, Integer> map = Map.of(3, 6, 7, 14, 9, 18);
    Collection actual = new HashSet(values().apply((Map) map));
    Set<Integer> expected = Set.of(6, 14, 18);
    assertEquals(expected, actual);
    String propName = formatProperty(map, "pronto", values(), Function.class);
    System.out.println(propName);
    assertEquals("pronto.values()", propName);
    propName = formatProperty(map, null, values(), Function.class);
    System.out.println(propName);
    assertEquals("MapN.values()", propName);
  }

  @Test
  public void abs00() {
    assertEquals(6, abs().applyAsInt(-6));
    String propName = formatProperty(-6, "dragon", abs(), IntUnaryOperator.class);
    System.out.println(propName);
    assertEquals("Math.abs(dragon)", propName);
    propName = formatProperty(-6, null, abs(), IntUnaryOperator.class);
    System.out.println(propName);
    assertEquals("Math.abs(argument)", propName);
  }

  @Test
  public void ABS00() {
    assertEquals(Integer.valueOf(6), ABS().apply(Integer.valueOf(-6)));
    assertEquals(Float.valueOf(6F), ABS().apply(Float.valueOf(-6F)));
    String propName = formatProperty(-6F, "skippy", ABS(), Function.class);
    System.out.println(propName);
    assertEquals("Math.abs(skippy)", propName);
    propName = formatProperty(-6F, null, ABS(), Function.class);
    System.out.println(propName);
    assertEquals("Math.abs(Float)", propName);
  }

  @Test
  public void key00() {
    Map.Entry e = Map.entry("foo", "bar");
    assertEquals("foo", key().apply(e));
    String propName = formatProperty(-6, "lasagna", key(), Function.class);
    System.out.println(propName);
    assertEquals("lasagna.getKey()", propName);
    propName = formatProperty(e, null, key(), Function.class);
    System.out.println(propName);
    assertEquals("KeyValueHolder.getKey()", propName);
  }

  @Test
  public void value00() {
    Map.Entry e = Map.entry("foo", "bar");
    assertEquals("bar", value().apply(e));
    String propName = formatProperty(-6, "bordeaux", value(), Function.class);
    System.out.println(propName);
    assertEquals("bordeaux.getValue()", propName);
    propName = formatProperty(e, null, value(), Function.class);
    System.out.println(propName);
    assertEquals("KeyValueHolder.getValue()", propName);
  }

}
