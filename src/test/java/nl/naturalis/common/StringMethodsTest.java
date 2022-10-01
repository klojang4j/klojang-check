package nl.naturalis.common;

import nl.naturalis.common.collection.IntList;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static nl.naturalis.common.ArrayMethods.ints;
import static nl.naturalis.common.StringMethods.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class StringMethodsTest {

  @Test
  public void append00() {
    assertEquals("", append(new StringBuilder(), "").toString());
    assertEquals("null", append(new StringBuilder(), null).toString());
    assertEquals("12", append(new StringBuilder(), 1, '2').toString());
    assertEquals("12345", append(new StringBuilder(), 1, '2', "345").toString());
    assertEquals("12345 ",
        append(new StringBuilder(), 1, '2', "345", ' ').toString());
    assertEquals(
        "12345 SUNDAY",
        append(new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY).toString());
    assertEquals(
        "12345 SUNDAY ",
        append(new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY,
            " ").toString());
    assertEquals(
        "12345 SUNDAY 1.2",
        append(new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY,
            " ",
            1.2).toString());
    assertEquals(
        "12345 SUNDAY 1.2750",
        append(new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY,
            " ",
            1.2,
            (short) 750)
            .toString());
    assertEquals(
        "12345 SUNDAY 1.2750[1, 3]",
        append(
            new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY,
            " ",
            1.2,
            (short) 750,
            List.of(1, 3))
            .toString());
    assertEquals(
        "12345 SUNDAY 1.2750[1, 3] ",
        append(
            new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY,
            " ",
            1.2,
            (short) 750,
            List.of(1, 3),
            ' ')
            .toString());
    assertEquals(
        "12345 SUNDAY 1.2750[1, 3] null",
        append(
            new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY,
            " ",
            1.2,
            (short) 750,
            List.of(1, 3),
            ' ',
            (Integer) null)
            .toString());
    assertEquals(
        "12345 SUNDAY 1.2750[1, 3] null",
        append(
            new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY,
            " ",
            1.2,
            (short) 750,
            List.of(1, 3),
            ' ',
            null,
            "")
            .toString());
    assertEquals(
        "12345 SUNDAY 1.2750[1, 3] null333",
        append(
            new StringBuilder(),
            1,
            '2',
            "345",
            ' ',
            DayOfWeek.SUNDAY,
            " ",
            1.2,
            (short) 750,
            List.of(1, 3),
            ' ',
            null,
            "",
            333)
            .toString());
  }

  @Test
  public void fromToIndex00() {
    String s = "012";
    assertEquals("", s.substring(2, 2));
  }

  @Test
  public void fromToIndex01() {
    String s = "012";
    // One position past the end of the string - still allowed with subXXXXX methods in the JDK
    assertEquals("", s.substring(3, 3));
  }

  @Test
  public void fromToIndex02() {
    String s = "012";
    assertEquals("", s.substring(3));
  }

  @Test(expected = StringIndexOutOfBoundsException.class)
  public void fromToIndex03() {
    String s = "012";
    s.substring(3, 4);
  }

  @Test
  public void count01() {
    String THIS_IS_BLISS = "This is This is This is BLISS!";
    assertEquals(3, count(THIS_IS_BLISS, "This is"));
    assertEquals(6, count(THIS_IS_BLISS, "is"));
    assertEquals(7, count(THIS_IS_BLISS, "is", true));

    assertEquals(6, count(">>>>>>", ">"));
    assertEquals(5, count(">>>>>>", ">>"));
    assertEquals(4, count(">>>>>>", ">>>"));
    assertEquals(3, count(">>>>>>", ">>>>"));
    assertEquals(2, count(">>>>>>", ">>>>>"));
    assertEquals(1, count(">>>>>>", ">>>>>>"));
    assertEquals(0, count(">>>>>>", ">>>>>>>"));
    assertEquals(0, count(">>>>>>", ">>>>>>>>"));

    assertEquals(4, count("ZZZZZZ", "z", true, 4));
    assertEquals(4, count("ZZZZZZ", "Z", false, 4));

    assertEquals(6, count(">>>>>>", ">", true, 0));
    assertEquals(6, count(">>>>>>", ">", true, 1000));
    assertEquals(1, count(">>>>>>", ">>", false, 1));
    assertEquals(2, count(">>>>>>", ">>", false, 2));
    assertEquals(5, count(">>>>>>", ">>", false, 0));
    assertEquals(5, count(">>>>>>", ">>", false, 750));

    assertEquals(0, count("", "FOO"));
    assertEquals(0, count(null, "FOO"));

    assertEquals(0, count("foo", "FOO", false));
    assertEquals(1, count("foo", "FOO", true));
  }

  @Test
  public void countDiscrete01() {
    String THIS_IS_BLISS = "This is This is This is BLISS!";
    assertEquals(3, countDiscrete(THIS_IS_BLISS, "This is"));
    assertEquals(6, countDiscrete(THIS_IS_BLISS, "is"));
    assertEquals(7, countDiscrete(THIS_IS_BLISS, "is", true, 0));
    String A12 = "AAAA" + "AAAA" + "AAAA";
    assertEquals(0, countDiscrete(A12, "a"));
    assertEquals(12, countDiscrete(A12, "a", true));
    assertEquals(6, countDiscrete(A12, "Aa", true));
    assertEquals(4, countDiscrete(A12, "AaA", true));
    assertEquals(3, countDiscrete(A12, "AaAa", true));
    assertEquals(2, countDiscrete(A12, "AaAaA", true));
    assertEquals(2, countDiscrete(A12, "AaAaAa", true));
    assertEquals(1, countDiscrete(A12, "AaAaAaA", true));
    assertEquals(1, countDiscrete(A12, "AaAaAaAa", true));
    assertEquals(1, countDiscrete(A12, "AaAaAaAaA", true));
    assertEquals(1, countDiscrete(A12, "AaAaAaAaAa", true));
    assertEquals(1, countDiscrete(A12, "AaAaAaAaAaA", true));
    assertEquals(1, countDiscrete(A12, "AaAaAaAaAaAa", true));
    assertEquals(0, countDiscrete(A12, "AaAaAaAaAaAaA", true));
    assertEquals(0, countDiscrete(A12, "AaAaAaAaAaAaAa", true));
    assertEquals(3, countDiscrete(A12, "a", true, 3));
    assertEquals(12, countDiscrete(A12, "a", true, 0));
    assertEquals(12, countDiscrete(A12, "a", true, 888));
    assertEquals(6, countDiscrete(A12, "Aa", true, 0));
    assertEquals(6, countDiscrete(A12, "Aa", true, 777));
    assertEquals(1, countDiscrete(A12, "Aa", true, 1));
    assertEquals(2, countDiscrete(A12, "Aa", true, 2));
    assertEquals(0, countDiscrete("", "FOO"));
    assertEquals(0, countDiscrete(null, "FOO"));
  }

  @Test
  public void ellipsis01() {
    String hello = "Hello World, how are you?";
    assertEquals("Hello W...", ellipsis(hello, 10));
    assertEquals("H...", ellipsis(hello, 4));
    assertEquals("Hello World, how are you?", ellipsis(hello, 100));
    assertEquals("", ellipsis(null, 100));
  }

  @Test
  public void ellipsis02() {
    // maxWidth will tacitly be clamped to 4
    assertEquals("H...", ellipsis("Hello World, how are you?", 0));
  }

  @Test
  public void toShortString00() {
    String s = "01234567890123456789012345678901234567890123456789012345678901234";
    assertEquals("01234567890123456789012345678901234567890123456...",
        toShortString(s));
  }

  @Test
  public void toShortString01a() {
    assertEquals("[0, 1, 2, 3, 4, 5, 6, ...]",
        toShortString(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
  }

  @Test
  public void toShortString01b() {
    assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]",
        toShortString(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 100));
  }

  @Test
  public void toShortString01c() {
    assertEquals("[0, 1, ...]",
        toShortString(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 100, 2, 2));
  }

  @Test
  public void toShortString03a() {
    Map<String, Double> m = new LinkedHashMap<>();
    m.put("foo", 0D);
    m.put("bar", 22.3);
    m.put("bozo", 42.0);
    m.put("ringo", 7.8);
    m.put("john", 34.04);
    m.put("paul", 4.3);
    m.put("george", 11.2);
    m.put("mark", 666.0);
    assertEquals("{foo=0.0, bar=22.3, bozo=42.0, ringo=7.8, ...}", toShortString(m));
  }

  @Test
  public void toShortString03b() {
    Map<String, Double> m = new LinkedHashMap<>();
    m.put("foo", 0D);
    m.put("bar", 22.3);
    m.put("bozo", 42.0);
    m.put("ringo", 7.8);
    m.put("john", 34.04);
    m.put("paul", 4.3);
    m.put("george", 11.2);
    m.put("mark", 666.0);
    assertEquals("{foo=0....", toShortString(m, 10));
  }

  @Test
  public void toShortString03c() {
    Map<String, Double> m = new LinkedHashMap<>();
    m.put("foo", 0D);
    m.put("bar", 22.3);
    m.put("bozo", 42.0);
    m.put("ringo", 7.8);
    m.put("john", 34.04);
    m.put("paul", 4.3);
    m.put("george", 11.2);
    m.put("mark", 666.0);
    assertEquals("{foo=0.0, bar=22.3, bozo=42.0, ...}", toShortString(m, 100, 3, 3));
  }

  @Test
  public void toShortString04a() {
    assertEquals("[0, 1, 2, 3, 4, 5, 6, ...]",
        toShortString(ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
  }

  @Test
  public void toShortString04b() {
    assertEquals("[0, 1, ...",
        toShortString(ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 10));
  }

  @Test
  public void toShortString04d() {
    assertEquals("[0, 1, ...",
        toShortString(ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 10));
  }

  @Test
  public void toShortString04e() {
    assertEquals("[0, 1, ...]",
        toShortString(ints(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 100, 2, 2));
  }

  @Test
  public void toShortString05a() {
    assertEquals("[0, 1, 2, 3, 4, 5, 6, ...]",
        toShortString(IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
  }

  @Test
  public void toShortString05b() {
    assertEquals("[0, 1, 2, ...]",
        toShortString(IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 20));
  }

  @Test
  public void toShortString05c() {
    assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]",
        toShortString(IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 2000, 20, 3));
  }

  @Test
  public void startsWith00() {
    String s = "The cat is both dead and alive";
    assertTrue(startsWith(s, true, "THE CAt", "test").isPresent());
    assertTrue(startsWith(s, true, "test", "the ").isPresent());
    assertTrue(startsWith(s, true, "test", "a", "b", "THE", "c").isPresent());
    assertTrue(startsWith(s,
        true,
        List.of("test", "a", "b", "THE", "c")).isPresent());
    assertTrue(startsWith(s, false, "THE", "ALIVE").isEmpty());
    assertTrue(startsWith(s, true, "cat", "is").isEmpty());
    assertTrue(startsWith(null, false, "ab", "cd").isEmpty());
    assertTrue(startsWith("", false, "ab", "cd").isEmpty());
  }

  @Test
  public void endsWith00() {
    String s = "The cat is both dead and alive";
    assertTrue(endsWith(s, true, "ALIVE", "test").isPresent());
    assertTrue(endsWith(s, true, "test", "ALIVE").isPresent());
    assertTrue(endsWith(s, true, "test", "a", "b", "ALIVE", "c").isPresent());
    assertTrue(endsWith(s,
        true,
        List.of("test", "a", "b", "ALIVE", "c")).isPresent());
    assertTrue(endsWith(s, false, "DEAD", "ALIVE").isEmpty());
    assertTrue(endsWith(s, true, "dead", "and").isEmpty());
    assertTrue(endsWith(null, false, "ab", "cd").isEmpty());
    assertTrue(endsWith("", false, "ab", "cd").isEmpty());
  }

  @Test
  public void substr00() {
    assertEquals("ever", substr("whatever", -4));
    assertEquals("ever", substr("whatever", 4));
    assertEquals("tever", substr("whatever", -5));
    assertEquals("ver", substr("whatever", 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void substr01() {
    substr("", -1);
  }

  @Test
  public void substr02() {
    assertEquals("", substr("", 0, 0));
    assertEquals("what", substr("whatever", 0, 4));
    assertEquals("ever", substr("whatever", -4, 4));
    assertEquals("eve", substr("whatever", -4, 3));
    assertEquals("e", substr("whatever", -4, 1));
    assertEquals("e", substr("whatever", 4, 1));
    assertEquals("", substr("whatever", 0, 0));
    assertEquals("", substr("whatever", 1, 0));
    assertEquals("", substr("whatever", 7, 0));
    assertEquals("r", substr("whatever", 7, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void substr03() {
    substr("whatever", 250, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void substr04() {
    substr("whatever", -4, 250);
  }

  @Test(expected = IllegalArgumentException.class)
  public void substr05() {
    substr("whatever", -250, 4);
  }

  @Test
  public void ensurePrefix00() {
    assertEquals("ABC", ensurePrefix(null, "ABC"));
    assertEquals("ABC", ensurePrefix("", "ABC"));
    assertEquals("ABC", ensurePrefix("ABC", "ABC"));
    assertEquals("ABCDE", ensurePrefix("DE", "ABC"));
    assertEquals("ABCDE", ensurePrefix("ABCDE", "ABC"));
    assertEquals("ABCFRIDAY", ensurePrefix(DayOfWeek.FRIDAY, "ABC"));
    assertEquals("", ensurePrefix(null, ""));
    assertEquals("", ensurePrefix("", ""));
  }

  @Test
  public void ensureSuffix00() {
    assertEquals("XYZ", ensureSuffix(null, "XYZ"));
    assertEquals("XYZ", ensureSuffix("", "XYZ"));
    assertEquals("XYZ", ensureSuffix("XYZ", "XYZ"));
    assertEquals("WXYZ", ensureSuffix("WXYZ", "XYZ"));
    assertEquals("ABXYZ", ensureSuffix("AB", "XYZ"));
    assertEquals("9XYZ", ensureSuffix(9, "XYZ"));
    assertEquals("", ensureSuffix(null, ""));
    assertEquals("", ensureSuffix("", ""));
  }

  @Test
  public void substr06() {
    assertEquals("w", substr("whatever", 0, -1));
    assertEquals("h", substr("whatever", 1, -1));
    assertEquals("ha", substr("whatever", 2, -2));
    assertEquals("hate", substr("whatever", 4, -4));
    assertEquals("r", substr("whatever", 7, -1));
    assertEquals("r", substr("whatever", -1, -1));
    assertEquals("er", substr("whatever", -1, -2));
    assertEquals("eve", substr("whatever", -2, -3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void substr07() {
    substr("whatever", 2, -100);
  }

  @Test
  public void indexOf00() {
    assertEquals(0, indexOf("012345678901234", "0", 1));
    assertEquals(0, indexOf("012345678901234", "012", 1));
    assertEquals(0, indexOf("012345678901234", "0123456", 1));
    assertEquals(10, indexOf("012345678901234", "0", 2));
    assertEquals(10, indexOf("012345678901234", "012", 2));
    assertEquals(-1, indexOf("012345678901234", "0123456", 2));
    assertEquals(20, indexOf("01234567890123456789012", "0", 3));
    assertEquals(20, indexOf("01234567890123456789012", "012", 3));
    assertEquals(-1, indexOf("01234567890123456789012", "0123", 3));
    assertEquals(9, indexOf("01234567890123456789012", "9", 1));
    assertEquals(9, indexOf("01234567890123456789012", "90", 1));
    assertEquals(-1, indexOf(null, "0123", 1));
    assertEquals(-1, indexOf("", "0123", 1));
    assertEquals(0, indexOf("0", "0", 1));
    assertEquals(0, indexOf("01", "01", 1));
    assertEquals(-1, indexOf("abc", "123", 1));
    assertEquals(-1, indexOf("abc", "123", 2));
    assertEquals(-1, indexOf("abc", "1", 1));
    assertEquals(-1, indexOf("abc", "1", 2));
    assertEquals(-1, indexOf("abc", "1", -1));
    assertEquals(-1, indexOf("abc", "1", -2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void indexOf01() {
    indexOf("01345", null, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void indexOf02() {
    indexOf("01345", "", 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void indexOf03() {
    indexOf("01345", "1", 0);
  }

  @Test
  public void indexOf04() {
    assertEquals(10, indexOf("012345678901234", "0", -1));
    assertEquals(10, indexOf("012345678901234", "012", -1));
    assertEquals(0, indexOf("012345678901234", "0123456", -1));

    assertEquals(0, indexOf("012345678901234", "0", -2));
    assertEquals(0, indexOf("012345678901234", "012", -2));
    assertEquals(1, indexOf("012345678901234", "123", -2));
  }

  @Test
  public void substrBefore00() {
    String subject = "012345678901234567890123";
    assertEquals("", substringBefore(subject, "012", 1));
    assertEquals("0123456789", substringBefore(subject, "012", 2));
    assertEquals("01234567890123456789", substringBefore(subject, "012", 3));
    assertSame(subject, substringBefore(subject, "012", 4));
    assertEquals("0", substringBefore(subject, "1", 1));
    assertEquals("0", substringBefore(subject, "12", 1));
    assertSame(subject, substringBefore(subject, "012", -4));
    assertEquals("", substringBefore(subject, "012", -3));
    assertEquals("0123456789", substringBefore(subject, "012", -2));
    assertEquals("01234567890123456789", substringBefore(subject, "012", -1));
    assertEquals("", substringBefore(null, "abc", 2));
    assertEquals("class java", substringBefore(String.class, ".", 1));
    assertEquals("class java.", substringBefore(String.class, "lang", 1));
    assertEquals("class java.lang", substringBefore(String.class, ".", 2));
  }

  @Test
  public void substrTo00() {
    String subject = "012345678901234567890123";
    assertEquals("012", substringOnTo(subject, "012", 1));
    assertEquals("0123456789012", substringOnTo(subject, "012", 2));
    assertEquals("01234567890123456789012", substringOnTo(subject, "012", 3));
    assertEquals(subject, substringOnTo(subject, "3", 3));
    assertNotSame(subject, substringOnTo(subject, "3", 3));
    assertSame(subject, substringOnTo(subject, "012", 4));
    assertEquals("01", substringOnTo(subject, "1", 1));
    assertEquals("012", substringOnTo(subject, "12", 1));
    assertSame(subject, substringOnTo(subject, "012", -4));
    assertEquals("012", substringOnTo(subject, "012", -3));
    assertEquals("0123456789012", substringOnTo(subject, "012", -2));
    assertEquals("01234567890123456789012", substringOnTo(subject, "012", -1));
    assertEquals("", substringOnTo(null, "abc", 2));
    assertEquals("class java.", substringOnTo(String.class, ".", 1));
    assertEquals("class java.lang", substringOnTo(String.class, "lang", 1));
    assertEquals("class java.lang.", substringOnTo(String.class, ".", 2));
  }

  @Test
  public void substrFrom00() {
    String subject = "012345678901234567890123";
    String s = substrFrom(subject, "012", 1);
    assertEquals(subject, substrFrom(subject, "012", 1));
    assertNotSame(subject, substrFrom(subject, "012", 1));
    assertSame(subject, substrFrom(subject, "FOO", 1));
    assertEquals("01234567890123", substrFrom(subject, "012", 2));
    assertEquals("0123", substrFrom(subject, "012", 3));
    assertSame(subject, substrFrom(subject, "012", 4));
    assertEquals("12345678901234567890123", substrFrom(subject, "1", 1));
    assertEquals("12345678901234567890123", substrFrom(subject, "12", 1));
    assertSame(subject, substrFrom(subject, "012", -4));
    assertEquals(subject, substrFrom(subject, "012", -3));
    assertNotSame(subject, substrFrom(subject, "012", -3));
    assertEquals("01234567890123", substrFrom(subject, "012", -2));
    assertEquals("0123", substrFrom(subject, "012", -1));
    assertEquals("", substrFrom(null, "abc", 2));
    assertEquals(".lang.String", substrFrom(String.class, ".", 1));
    assertEquals("lang.String", substrFrom(String.class, "lang", 1));
    assertEquals(".String", substrFrom(String.class, ".", 2));
  }

  @Test
  public void substrAfter00() {
    String subject = "012345678901234567890123";
    assertEquals("345678901234567890123", substrAfter(subject, "012", 1));
    assertEquals("34567890123", substrAfter(subject, "012", 2));
    assertEquals("3", substrAfter(subject, "012", 3));
    assertEquals("", substrAfter(subject, "0123", 3));
    assertSame(subject, substrAfter(subject, "012", 4));
    assertSame(subject, substrAfter(subject, "012", -4));
    assertEquals("345678901234567890123", substrAfter(subject, "012", -3));
    assertEquals("34567890123", substrAfter(subject, "012", -2));
    assertEquals("3", substrAfter(subject, "012", -1));
    assertEquals("", substrAfter(subject, "0123", -1));
    assertEquals("", substrAfter(null, "abc", 2));
    assertEquals("lang.String", substrAfter(String.class, ".", 1));
    assertEquals(".String", substrAfter(String.class, "lang", 1));
    assertEquals("String", substrAfter(String.class, ".", 2));
  }

  @Test
  public void lchop00() {
    assertEquals("", lchop(null, "ab"));
    assertEquals("", lchop("", "ab"));
    assertEquals("cd", lchop("cd", "ab"));
    assertEquals("cd", lchop("cd", "a"));
    assertEquals("cdefg", lchop("abcdefg", "ab"));
    assertEquals("g", lchop("abcdefg", "ab", "cd", "ef"));
    assertEquals("cdefg", lchop("abcdefg", "ab", "d", "ef"));
    assertEquals("abcdefg", lchop("abcdefg", "AB"));
    assertEquals("cdefg", lchop("abcdefg", true, "AB"));
    assertEquals("", lchop("abcdefg", true, "AB", "CDEFG"));
  }

  @Test
  public void rchop00() {
    assertEquals("", rchop(null, "hi"));
    assertEquals("", rchop("", "hi"));
    assertEquals("hi", rchop("hi", "HI"));
    assertEquals("", rchop("hi", true, "HI"));
    assertEquals("abcdefghij", rchop("abcdefghij", true, "HI"));
    assertEquals("abcdefg", rchop("abcdefghi", true, "HI"));
    assertEquals("abcdef", rchop("abcdefghi", true, "HI", "G"));
    assertEquals("ab", rchop("abcdefghi", "g", "hi", "cd", "ef"));
    assertEquals("ab", rchop("abcdefghi", "g", "hi", "cd", "ef", "abc"));
    assertEquals("", rchop("abcdefghi", "g", "hi", "cd", "ef", "ab"));
  }

  @Test
  public void ltrim00() {
    assertEquals("", ltrim(null, "a"));
    assertEquals("", ltrim("", "abc"));
    assertEquals("", ltrim("a", "abc"));
    assertEquals("", ltrim("ab", "abc"));
    assertEquals("", ltrim("abc", "abc"));
    assertEquals("", ltrim("acb", "abc"));
    assertEquals("db", ltrim("adb", "abc"));
    assertEquals("da", ltrim("abcda", "abc"));
  }

  @Test
  public void rtrim00() {
    assertEquals("", rtrim(null, "a"));
    assertEquals("", rtrim("", "a"));
    assertEquals("", rtrim("a", "a"));
    assertEquals("", rtrim("aab", "ab"));
    assertEquals("", rtrim("aab", "ba"));
    assertEquals("", rtrim("aab", "cba"));
    assertEquals("aabd", rtrim("aabd", "cba"));
    assertEquals("aab", rtrim("aabdef", "fde"));
    assertEquals("a", rtrim("abdef", "gfdeb"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void rtrim01() {
    assertEquals("", rtrim("a", null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void rtrim02() {
    assertEquals("", rtrim("a", ""));
  }

  @Test
  public void trim00() {
    assertEquals("", trim(null, "ab"));
    assertEquals("", trim("", "ab"));
    assertEquals("cdef", trim("abcdefab", "ab"));
    assertEquals("bcdefab", trim("abcdefab", "a"));
    assertEquals("abcdefa", trim("abcdefab", "b"));
    assertEquals("abcdefab", trim("abcdefab", "cdef"));
  }

  @Test
  public void lpad00() {
    assertEquals("hello", lpad("hello", 5));
    assertEquals(" hello", lpad("hello", 6));
    assertEquals("  hello", lpad("hello", 7));
    assertEquals("       ", lpad("", 7));
    assertEquals("       ", lpad(null, 7));
    assertEquals("hello", lpad("hello", 0));
  }

  @Test
  public void lpad01() {
    assertEquals("hello|", lpad("hello", 5, '.', "|"));
    assertEquals(".hello|", lpad("hello", 6, '.', "|"));
    assertEquals("..hello|", lpad("hello", 7, '.', "|"));
    assertEquals(".......|", lpad("", 7, '.', "|"));
    assertEquals(".......|", lpad(null, 7, '.', "|"));
    assertEquals("hello|", lpad("hello", 0, '.', "|"));
    assertEquals("<<<hello", lpad("hello", 8, '<'));
  }

  @Test
  public void rpad00() {
    assertEquals("hello", rpad("hello", 5));
    assertEquals("hello ", rpad("hello", 6));
    assertEquals("hello  ", rpad("hello", 7));
    assertEquals("       ", rpad("", 7));
    assertEquals("       ", rpad(null, 7));
    assertEquals("hello", rpad("hello", 0));
    assertEquals("hello>>>", rpad("hello", 8, '>'));
  }

  @Test
  public void rpad01() {
    assertEquals("hello|", rpad("hello", 5, '.', "|"));
    assertEquals("hello.|", rpad("hello", 6, '.', "|"));
    assertEquals("hello..|", rpad("hello", 7, '.', "|"));
    assertEquals(".......|", rpad("", 7, '.', "|"));
    assertEquals(".......|", rpad(null, 7, '.', "|"));
    assertEquals("hello|", rpad("hello", 0, '.', "|"));
  }

  @Test
  public void pad00() {
    assertEquals("hello", pad("hello", 5));
    assertEquals("hello ", pad("hello", 6));
    assertEquals(" hello ", pad("hello", 7));
    assertEquals(" hello  ", pad("hello", 8));
    assertEquals("  hello  ", pad("hello", 9));
    assertEquals(".hello..", pad("hello", 8, '.'));
    assertEquals("..hello..", pad("hello", 9, '.'));
    assertEquals("hello", pad("hello", 0));
  }

  @Test
  public void pad01() {
    assertEquals("hello|", pad("hello", 5, '.', "|"));
    assertEquals("hello.|", pad("hello", 6, '.', "|"));
    assertEquals(".hello.|", pad("hello", 7, '.', "|"));
    assertEquals(".......|", pad("", 7, '.', "|"));
    assertEquals(".......|", pad(null, 7, '.', "|"));
    assertEquals("hello|", pad("hello", 0, '.', "|"));
  }

  @Test
  public void getLineAndColumn00() {
    String s = "To be\nOr not to be\nThat is the question\n Whether 't is nobler\nIn the mind";
    int idx = 0;
    assertArrayEquals(new int[] {0, 0}, getLineAndColumn(s, idx, "\n"));
    idx = s.indexOf("not");
    assertArrayEquals(new int[] {1, 3}, getLineAndColumn(s, idx, "\n"));
    idx = s.indexOf("is");
    assertArrayEquals(new int[] {2, 5}, getLineAndColumn(s, idx, "\n"));
    idx = s.indexOf("mind");
    assertArrayEquals(new int[] {4, 7}, getLineAndColumn(s, idx, "\n"));
    idx = s.indexOf("\n"); // hmmm ...
    assertArrayEquals(new int[] {0, 5}, getLineAndColumn(s, idx, "\n"));
  }

  @Test
  public void firstToUpper00() {
    assertEquals("Hello", firstToUpper("hello"));
    assertEquals("H", firstToUpper("h"));
    assertSame("+hello", firstToUpper("+hello"));
    assertSame(" hello", firstToUpper(" hello"));
    assertEquals("", firstToUpper(""));
    assertEquals("", firstToUpper(null));
  }

  @Test
  public void firstToLower00() {
    assertEquals("hello", firstToLower("Hello"));
    assertEquals("h", firstToLower("H"));
    assertSame("+hello", firstToLower("+hello"));
    assertSame(" Hello", firstToLower(" Hello"));
    assertEquals("", firstToLower(""));
    assertEquals("", firstToLower(null));
  }

  @Test
  public void concat00() {
    assertEquals("There are 7 days in a week",
        concat("There are ", 7, ' ', "days in a ", "week"));
  }

  @Test
  public void ifBlank00() {
    assertEquals("abc", ifBlank(null, "abc"));
    assertEquals("abc", ifBlank("", "abc"));
    assertEquals("abc", ifBlank("\r\n", "abc"));
    assertEquals("abcd", ifBlank("abcd", "abc"));
  }

}
