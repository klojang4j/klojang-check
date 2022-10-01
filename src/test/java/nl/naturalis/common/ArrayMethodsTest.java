package nl.naturalis.common;

import org.junit.Test;

import java.util.List;
import java.util.OptionalInt;

import static nl.naturalis.common.ArrayMethods.*;
import static org.junit.Assert.*;

public class ArrayMethodsTest {

  @Test
  public void append01() {
    String[] a = {"a", "b", "c"};
    String[] expected = {"a", "b", "c", "1"};
    String[] actual = append(a, "1");
    assertArrayEquals(expected, actual);
  }

  @Test
  public void append02() {
    String[] a = {"a", "b", "c"};
    String[] expected = {"a", "b", "c", "1", "2"};
    String[] actual = append(a, "1", "2");
    assertArrayEquals(expected, actual);
  }

  @Test
  public void append03() {
    String[] a = {"a", "b", "c"};
    String[] expected = {"a", "b", "c", "1", "2", "3", "4", "5", "6", "7"};
    String[] actual = append(a, "1", "2", "3", "4", "5", "6", "7");
    assertArrayEquals(expected, actual);
  }

  @Test
  public void append04() {
    String[] a = {};
    String[] expected = {"a"};
    String[] actual = append(a, "a");
    assertArrayEquals(expected, actual);
  }

  @Test
  public void concat01() {
    String[] a = {"a", "b", "c"};
    String[] b = {"1", "2", "3"};
    String[] c = {"A", "B", "C"};
    String[] d = {"*", "&", "$"};
    String[] expected = {"a", "b", "c", "1", "2", "3", "A", "B", "C", "*", "&", "$"};
    String[] actual = concat(a, b, c, d);
    assertArrayEquals(expected, actual);
  }

  @Test // With interfaces.
  public void concat02() {
    CharSequence[] a = {"a", "b", "c"};
    CharSequence[] b = {"1", "2", "3"};
    CharSequence[] c = {"A", "B", "C"};
    CharSequence[] d = {"*", "&", "$"};
    CharSequence[] expected = {"a",
        "b",
        "c",
        "1",
        "2",
        "3",
        "A",
        "B",
        "C",
        "*",
        "&",
        "$"};
    CharSequence[] actual = concat(a, b, c, d);
    assertArrayEquals(expected, actual);
  }

  @Test
  public void inArray01() {
    int[] array = {1, 2, 4, 8, 16};
    assertTrue(isElementOf(1, array));
    assertTrue(isElementOf(16, array));
    assertFalse(isElementOf(23, array));
  }

  @Test
  public void indexOf00() {
    assertEquals(2, indexOf(new String[] {"a", "b", "c", "d", "e"}, "c"));
    assertEquals(2, indexOf(new String[] {"a", "b", null, "d", "e"}, null));
    assertEquals(-1, indexOf(new String[] {"a", "b", null, "d", "e"}, "FOO"));
  }

  @Test
  public void lastIndexOf00() {
    assertEquals(2, lastIndexOf(new String[] {"a", "b", "c", "d", "e"}, "c"));
    assertEquals(2, lastIndexOf(new String[] {"a", "b", null, "d", "e"}, null));
    assertEquals(-1, lastIndexOf(new String[] {"a", "b", null, "d", "e"}, "FOO"));
  }

  @Test
  public void indexOf01() {
    int[] array = {1, 2, 4, 8, 16};
    assertEquals(0, indexOf(array, 1).getAsInt());
    assertEquals(4, indexOf(array, 16).getAsInt());
    assertEquals(OptionalInt.empty(), indexOf(array, 23));
  }

  @Test
  public void lastIndexOf01() {
    int[] array = {1, 2, 4, 8, 16};
    assertEquals(0, lastIndexOf(array, 1).getAsInt());
    assertEquals(4, lastIndexOf(array, 16).getAsInt());
    assertEquals(OptionalInt.empty(), lastIndexOf(array, 23));
  }

  @Test
  public void refIndexOf01() {
    String s0 = "Hello";
    // Use constructor. Otherwise compiler detects we're using the same
    // string twice and creates just one instance.
    String s1 = new String("World");
    String s2 = new String("World");
    String[] strings = pack(s0, s1, s2);
    assertEquals(0, refIndexOf(strings, s0));
    assertEquals(1, refIndexOf(strings, s1));
    assertEquals(2, refIndexOf(strings, s2));
    assertEquals(-1, refIndexOf(strings, new String("Hello")));
    assertEquals(-1, refIndexOf(strings, new String("World")));
  }

  @Test
  public void inIntArray00() {
    assertTrue(isElementOf(2, ArrayMethods.pack(0, 4, 6, 3, 2)));
    assertFalse(isElementOf(2, ArrayMethods.pack(0, 4, 6, 3, 9)));
  }

  @Test
  public void prefix01() {
    String[] a = {"a", "b", "c"};
    String[] expected = {"1", "a", "b", "c"};
    String[] actual = prefix(a, "1");
    assertArrayEquals(expected, actual);
  }

  @Test
  public void prefix02() {
    String[] a = {"a", "b", "c"};
    String[] expected = {"1", "2", "a", "b", "c"};
    String[] actual = prefix(a, "1", "2");
    assertArrayEquals(expected, actual);
  }

  @Test
  public void prefix03() {
    String[] a = {"a", "b", "c"};
    String[] expected = {"1", "2", "3", "4", "5", "6", "7", "a", "b", "c"};
    String[] actual = prefix(a, "1", "2", "3", "4", "5", "6", "7");
    assertArrayEquals(expected, actual);
  }

  @Test
  public void prefix04() {
    String[] a = {};
    String[] expected = {"a"};
    String[] actual = prefix(a, "a");
    assertArrayEquals(expected, actual);
  }

  @Test
  public void implodeInts00() {
    int[] ints = {1, 2, 3, 4, 5};
    assertEquals("2|4", implodeInts(ints, i -> "" + (2 * i), "|", 0, 2));
    assertEquals("2|4|6|8|10", implodeInts(ints, i -> "" + (2 * i), "|", 0, -1));
    assertEquals("2|4|6|8|10", implodeInts(ints, i -> "" + (2 * i), "|", 0, 100));
    assertEquals("4|6|8|10", implodeInts(ints, i -> "" + (2 * i), "|", 1, 100));
    assertEquals("4|6|8", implodeInts(ints, i -> "" + (2 * i), "|", 1, 4));
    assertEquals("1|2|3", implodeInts(ints, "|", 3));
    assertEquals("1|2|3|4|5", implodeInts(ints, "|"));
    assertEquals("1, 2, 3", implodeInts(ints, 3));
    assertEquals("1, 2, 3, 4, 5", implodeInts(ints));
    assertEquals("2, 4, 6, 8, 10", implodeInts(ints, i -> "" + (2 * i)));
  }

  @Test
  public void implodeAny00() {
    long[] longs = {1, 2, 3, 4, 5};
    assertEquals("2|4", implodeAny(longs, l -> "" + (2 * (long) l), "|", 0, 2));
    assertEquals("2|4|6|8|10",
        implodeAny(longs, l -> "" + (2 * (long) l), "|", 0, -1));
    assertEquals("2|4|6|8|10",
        implodeAny(longs, l -> "" + (2 * (long) l), "|", 0, 100));
    assertEquals("4|6|8|10",
        implodeAny(longs, l -> "" + (2 * (long) l), "|", 1, 100));
    assertEquals("4|6|8", implodeAny(longs, l -> "" + (2 * (long) l), "|", 1, 4));
    assertEquals("1|2|3", implodeAny(longs, "|", 3));
    assertEquals("1|2|3|4|5", implodeAny(longs, "|"));
    assertEquals("1, 2, 3", implodeAny(longs, 3));
    assertEquals("1, 2, 3, 4, 5", implodeAny(longs));
    byte[] bytes = {(byte) 1, (byte) 2, (byte) 3};
    assertEquals("2, 4, 6", implodeAny(bytes, i -> "" + (2 * (byte) i)));
  }

  @Test
  public void implode00() {
    Integer[] ints = {1, 2, 3, 4, 5};
    assertEquals("2|4", implode(ints, i -> "" + (2 * (int) i), "|", 0, 2));
    assertEquals("2|4|6|8|10", implode(ints, i -> "" + (2 * (int) i), "|", 0, -1));
    assertEquals("2|4|6|8|10", implode(ints, i -> "" + (2 * (int) i), "|", 0, 100));
    assertEquals("4|6|8|10", implode(ints, i -> "" + (2 * (int) i), "|", 1, 100));
    assertEquals("4|6|8", implode(ints, i -> "" + (2 * (int) i), "|", 1, 4));
    assertEquals("1|2|3", implode(ints, "|", 3));
    assertEquals("1|2|3|4|5", implode(ints, "|"));
    assertEquals("1, 2, 3", implode(ints, 3));
    assertEquals("1, 2, 3, 4, 5", implode(ints));
    assertEquals("2, 4, 6, 8, 10", implode(ints, i -> "" + (2 * i)));
  }

  @Test
  public void asPrimitiveArray00() {
    Integer[] ints = {1, 2, 3, 4, 5};
    int[] expected = {1, 2, 3, 4, 5};
    assertArrayEquals(expected, unbox(ints));
  }

  @Test
  public void asPrimitiveArray01() {
    Integer[] ints = {1, 2, null, 4, 5};
    assertArrayEquals(ints(1, 2, 0, 4, 5), unbox(ints));
  }

  @Test
  public void asPrimitiveArray02() {
    Integer[] ints = {1, 2, null, 4, 5};
    int[] expected = {1, 2, 42, 4, 5};
    assertArrayEquals(expected, unbox(ints, 42));
  }

  @Test
  public void asWrapperArray00() {
    assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, box(new int[] {1, 2, 3, 4, 5}));
    assertArrayEquals(new Long[] {1L, 2L, 3L, 4L, 5L},
        box(new long[] {1, 2, 3, 4, 5}));
    assertArrayEquals(new Double[] {1D, 2D, 3D, 4D, 5D},
        box(new double[] {1, 2, 3, 4, 5}));
    assertArrayEquals(new Float[] {1F, 2F, 3F, 4F, 5F},
        box(new float[] {1, 2, 3, 4, 5}));
    assertArrayEquals(new Short[] {1, 2, 3, 4, 5}, box(new short[] {1, 2, 3, 4, 5}));
    assertArrayEquals(new Byte[] {1, 2, 3, 4, 5}, box(new byte[] {1, 2, 3, 4, 5}));
    assertArrayEquals(new Character[] {1, 2, 3, 4, 5},
        box(new char[] {1, 2, 3, 4, 5}));
    assertArrayEquals(
        new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.TRUE},
        box(new boolean[] {false, false, true}));
    assertArrayEquals(new Integer[0], box(new int[0]));
  }

  @Test
  public void asList00() {
    assertEquals(List.of(1, 2, 3), asList(new int[] {1, 2, 3}));
    assertEquals(List.of(1L, 2L, 3L), asList(new long[] {1, 2, 3}));
    assertEquals(List.of(1D, 2D, 3D), asList(new double[] {1, 2, 3}));
    assertEquals(List.of(1F, 2F, 3F), asList(new float[] {1, 2, 3}));
    assertEquals(List.of((short) 1, (short) 2, (short) 3),
        asList(new short[] {1, 2, 3}));
    assertEquals(List.of((byte) 1, (byte) 2, (byte) 3),
        asList(new byte[] {1, 2, 3}));
    assertEquals(List.of((char) 1, (char) 2, (char) 3),
        asList(new char[] {1, 2, 3}));
    assertEquals(List.of(Boolean.FALSE, Boolean.TRUE),
        asList(new boolean[] {false, true}));
    assertNotEquals(List.of(1, 2, 3), new long[] {1, 2, 3});
  }

  @Test
  public void packAndFriends00() {
    assertArrayEquals(new String[] {"a", "b", "c"}, pack("a", "b", "c"));
    assertArrayEquals(new char[] {'a', 'b', 'c'}, chars('a', 'b', 'c'));
    assertArrayEquals(new int[] {1, 2, 3}, ints(1, 2, 3));
    assertArrayEquals(new long[] {1, 2, 3}, longs(1, 2, 3));
    assertArrayEquals(new double[] {6D, 3.2, 8.0}, doubles(6D, 3.2, 8.0), .000001);
    assertArrayEquals(new float[] {6F, 3.2F, 8.0F},
        floats(6F, 3.2F, 8.0F),
        .000001F);
    assertArrayEquals(new String[0], pack());
    assertArrayEquals(new Integer[0], pack());
    assertArrayEquals(new String[] {null},
        pack((String) null)); // Need to cast - otherwise NPE
  }

  @Test
  public void reverse00() {
    int[] array = ints();
    reverse(array);
    assertArrayEquals(EMPTY_INT_ARRAY, array);
    array = ints(1);
    reverse(array);
    assertArrayEquals(ints(1), array);
    array = ints(1, 2);
    reverse(array);
    assertArrayEquals(ints(2, 1), array);
    array = ints(1, 2, 3);
    reverse(array);
    assertArrayEquals(ints(3, 2, 1), array);
    array = ints(1, 2, 3, 4);
    reverse(array);
    assertArrayEquals(ints(4, 3, 2, 1), array);
    array = ints(1, 2, 3, 4, 5);
    reverse(array);
    assertArrayEquals(ints(5, 4, 3, 2, 1), array);
    array = ints(1, 2, 3, 4, 5, 6);
    reverse(array);
    assertArrayEquals(ints(6, 5, 4, 3, 2, 1), array);
  }

  @Test
  public void reverse01() {
    Integer[] array = pack();
    reverse(array);
    assertArrayEquals(EMPTY_OBJECT_ARRAY, array);
    array = pack(1);
    reverse(array);
    assertArrayEquals(pack(1), array);
    array = pack(1, 2);
    reverse(array);
    assertArrayEquals(pack(2, 1), array);
    array = pack(1, 2, 3);
    reverse(array);
    assertArrayEquals(pack(3, 2, 1), array);
    array = pack(1, 2, 3, 4);
    reverse(array);
    assertArrayEquals(pack(4, 3, 2, 1), array);
    array = pack(1, 2, 3, 4, 5);
    reverse(array);
    assertArrayEquals(pack(5, 4, 3, 2, 1), array);
    array = pack(1, 2, 3, 4, 5, 6);
    reverse(array);
    assertArrayEquals(pack(6, 5, 4, 3, 2, 1), array);
  }

  @Test
  public void reverse02() {
    int[] array = ints(0, 1, 2, 3, 4, 5, 6);
    reverse(array, 1, 7);
    assertArrayEquals(ints(0, 6, 5, 4, 3, 2, 1), array);
    array = ints(0, 1, 2, 3, 4, 5, 6);
    reverse(array, 1, 6);
    assertArrayEquals(ints(0, 5, 4, 3, 2, 1, 6), array);
    array = ints(0, 1, 2, 3, 4, 5, 6);
    reverse(array, 1, 2);
    assertArrayEquals(ints(0, 1, 2, 3, 4, 5, 6), array);
    array = ints(0, 1, 2, 3, 4, 5, 6);
    reverse(array, 1, 3);
    assertArrayEquals(ints(0, 2, 1, 3, 4, 5, 6), array);
  }

  @Test
  public void reverse03() {
    Integer[] array = pack(0, 1, 2, 3, 4, 5, 6);
    reverse(array, 1, 7);
    assertArrayEquals(pack(0, 6, 5, 4, 3, 2, 1), array);
    array = pack(0, 1, 2, 3, 4, 5, 6);
    reverse(array, 1, 6);
    assertArrayEquals(pack(0, 5, 4, 3, 2, 1, 6), array);
    array = pack(0, 1, 2, 3, 4, 5, 6);
    reverse(array, 1, 2);
    assertArrayEquals(pack(0, 1, 2, 3, 4, 5, 6), array);
    array = pack(0, 1, 2, 3, 4, 5, 6);
    reverse(array, 1, 3);
    assertArrayEquals(pack(0, 2, 1, 3, 4, 5, 6), array);
  }

  @Test
  public void find00() {
    Result<Integer> x = find(pack(0, 1, 2, 3, 4, 5), i -> (int) i > 3);
    assertEquals(4, (int) x.get());
  }

  @Test
  public void find01() {
    Result<Integer> x = find(pack(0, 1, 2, 3, 4, 5), i -> (int) i > 5);
    assertFalse(x.isAvailable());
    Integer y = find(pack(0, 1, 2, 3, 4, 5), i -> (int) i > 5).orElse(100);
    assertEquals(100, (int) y);
  }

  @Test
  public void find02() {
    OptionalInt x = find(ints(0, 1, 2, 3, 4, 5), i -> i > 3);
    assertEquals(4, x.getAsInt());
  }

  @Test
  public void find03() {
    OptionalInt x = find(ints(0, 1, 2, 3, 4, 5), i -> i > 5);
    assertFalse(x.isPresent());
  }

  @Test
  public void refLastIndexOf00() {
    String s = "world";
    String[] strs = pack("hello", s, s);
    assertEquals(2, refLastIndexOf(strs, s));
  }

  @Test
  public void refLastIndexOf01() {
    String s = "world";
    String[] strs = pack("hello", s, s, "foo", "bar");
    assertEquals(2, refLastIndexOf(strs, s));
  }

}
