package nl.naturalis.common;

import nl.naturalis.common.collection.IntList;
import nl.naturalis.common.collection.WiredList;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static nl.naturalis.common.ArrayMethods.*;
import static nl.naturalis.common.CollectionMethods.asList;
import static nl.naturalis.common.ObjectMethods.*;
import static nl.naturalis.check.CommonChecks.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

@SuppressWarnings("rawtypes")
public class ObjectMethodsTest {

  /*
   * Not a real test. Just here so we can test our understanding of Java.
   */
  @Test
  public void foo() {
    int[] ints = new int[] {1, 2, 3, 4, 5};
    long[] longs = new long[] {1L, 2L, 3L, 4L, 5L};
    Integer[] integers = new Integer[] {1, 2, 3, 4, 5};
    Object[] objects = new Object[] {1, 2, 3, 4, 5};
    assertFalse(ClassMethods.isSubtype(ints.getClass(), longs.getClass()));
    assertFalse(ClassMethods.isSubtype(longs.getClass(), ints.getClass()));
    assertFalse(ClassMethods.isSubtype(ints.getClass(), integers.getClass()));
    assertFalse(ClassMethods.isSubtype(integers.getClass(), ints.getClass()));
    assertTrue("05",
        ClassMethods.isSubtype(integers.getClass(), objects.getClass()));
    assertFalse("06",
        ClassMethods.isSubtype(objects.getClass(), integers.getClass()));
    assertFalse(ints.equals(longs));
    assertFalse(longs.equals(ints));
    assertFalse(ints.equals(integers));
    assertFalse(integers.equals(ints));
    assertFalse(objects.equals(integers));
    assertFalse(integers.equals(objects));
  }

  @Test
  public void isEmpty00() {
    assertTrue(isEmpty((Map) null));
    assertTrue(isEmpty(Map.of()));
    assertFalse(isEmpty(Map.of(1, "two")));
    assertTrue(isEmpty((List) null));
    assertTrue(isEmpty(List.of()));
    assertFalse(isEmpty(List.of(1, "two")));
    assertTrue(isEmpty((String) null));
    assertTrue(isEmpty(""));
    assertFalse(isEmpty("foo"));
    assertTrue(isEmpty((int[]) null));
    assertTrue(isEmpty(new int[0]));
    assertTrue(isEmpty(ArrayMethods.EMPTY_OBJECT_ARRAY));
    assertTrue(isEmpty(ArrayMethods.EMPTY_STRING_ARRAY));
    assertTrue(isEmpty((String[]) null));
    assertFalse(isEmpty(pack("foo")));
    assertFalse(isEmpty(ints(2, 3, 4)));
    assertTrue(isEmpty(Optional.empty()));
    assertTrue(isEmpty(Optional.of(Map.of())));
    assertTrue(isEmpty(Optional.of(List.of())));
    assertTrue(isEmpty(Optional.of("")));
    assertTrue(isEmpty(Optional.of(ArrayMethods.EMPTY_OBJECT_ARRAY)));
    assertFalse(isEmpty(Optional.of("foo")));
    assertFalse(isEmpty(Optional.of(List.of(2))));
    assertTrue(isEmpty((Object) null));
    assertFalse(isEmpty(LocalDate.now()));
    assertFalse(isEmpty(7D));
    assertTrue(isEmpty(IntList.of()));
    assertTrue(isEmpty((Object) IntList.of()));
    assertFalse(isEmpty(IntList.of(0)));
    assertFalse(isEmpty((Object) IntList.of(0)));
    assertFalse(isEmpty(IntList.of(0, 1)));
    assertTrue(isEmpty((IntList) null));
  }

  @Test
  public void e2nEquals00() {
    assertTrue(e2nEquals(null, null));
    assertTrue(e2nEquals(null, ""));
    assertTrue(e2nEquals("", null));
    assertTrue(e2nEquals(null, EMPTY_STRING_ARRAY));
    assertTrue(e2nEquals(Map.of(), null));
    assertTrue(e2nEquals(Optional.of(""), null));
    assertTrue(e2nEquals(null, Optional.empty()));
    assertTrue(e2nEquals(new ArrayList<>(), new WiredList<>()));
    assertFalse(e2nEquals(Map.of(), List.of()));
    assertFalse(e2nEquals("", List.of()));
    assertFalse(e2nEquals("", "0"));
    assertFalse(e2nEquals(2, 3));
    assertFalse(e2nEquals(null, 3));
    assertFalse(e2nEquals("foo", null));
  }

  @Test
  public void e2nHashCode00() {
    assertEquals(0, e2nHashCode(null));
    assertEquals(0, e2nHashCode(""));
    assertEquals(0, e2nHashCode(List.of()));
    assertEquals(0, e2nHashCode(Set.of()));
    assertEquals(0, e2nHashCode(Map.of()));
    assertEquals(0, e2nHashCode(pack()));
    assertEquals(0, e2nHashCode(ints()));
    assertEquals(0, e2nHashCode(doubles()));
    assertEquals(0, e2nHashCode(Optional.empty()));
    assertEquals(0, e2nHashCode(Optional.of(List.of())));
    assertEquals(31 + 2, e2nHashCode(List.of(2)));
    assertEquals(31 + 2, e2nHashCode(Set.of(2)));
    assertEquals((31 + 2) * 31 + 3, e2nHashCode(Map.of(2, 3)));
    assertEquals(31 + 2, e2nHashCode(pack(2)));
    assertEquals(31 + 2, e2nHashCode(ints(2)));
    assertEquals(2, e2nHashCode(Optional.of(2)));
  }

  @Test
  public void e2nTypedHashCode00() {
    assertEquals(0, e2nTypedHashCode(null));
    assertEquals(7, e2nTypedHashCode(7));
    assertEquals(String.class.hashCode(), e2nTypedHashCode(""));
    assertEquals(List.class.hashCode(), e2nTypedHashCode(List.of()));
    assertEquals(Set.class.hashCode(), e2nTypedHashCode(Set.of()));
    assertEquals(Map.class.hashCode(), e2nTypedHashCode(Map.of()));
    assertEquals(Object[].class.hashCode(), e2nTypedHashCode(pack()));
    assertEquals(int[].class.hashCode(), e2nTypedHashCode(ints()));
    assertEquals(double[].class.hashCode(), e2nTypedHashCode(doubles()));
    assertEquals(Optional.class.hashCode(), e2nTypedHashCode(Optional.empty()));
    assertEquals(Optional.class.hashCode(), e2nTypedHashCode(Optional.of(Set.of())));
    assertEquals(Result.class.hashCode(), e2nTypedHashCode(Result.notAvailable()));
    assertEquals(Result.class.hashCode(), e2nTypedHashCode(Result.of(Set.of())));
    // Just print. Can't print and then feed back into test b/c
    // hash code of Class objects not stable
    System.out.println(">>>>> " + e2nTypedHashCode("Hello World"));
    System.out.println(">>>>> " + e2nTypedHashCode(List.of(1, 2, 3)));
    System.out.println(">>>>> " + e2nTypedHashCode(Set.of(1, 2, 3)));
    System.out.println(">>>>> " + e2nTypedHashCode(Map.of("a", 1, "b", 2)));
    System.out.println(">>>>> " + e2nTypedHashCode(pack("a", "b")));
    System.out.println(">>>>> " + e2nTypedHashCode(ints(1, 2, 3, 4)));
    System.out.println(">>>>> " + e2nTypedHashCode(Optional.of(List.of(1, 2, 3))));
    System.out.println(">>>>> " + e2nTypedHashCode(Result.of(List.of(1, 2, 3))));
  }

  @Test
  public void e2nHash00() {
    assertEquals(0, e2nHash(null));
    assertEquals(31 + 1, e2nHash(1));
    assertEquals((31 + 1) * 31 + 0, e2nHash(1, ""));
    assertEquals((31 + 1) * 31 + 2, e2nHash(1, 2));
  }

  @Test
  public void isDeepNotEmpty00() {
    assertTrue(isDeepNotEmpty(List.of("Hi", new String[] {"Hi", "There"})));
    assertFalse(isDeepNotEmpty(List.of("Hi", new String[0])));
    assertTrue("03",
        isDeepNotEmpty(List.of("Hi", Collections.singletonMap("a", "b"))));
    assertFalse(isDeepNotEmpty(List.of("Hi", Collections.emptyMap())));
    Map map0 = Collections.emptyMap();
    Map map1 = Collections.singletonMap("b", map0);
    Map map2 = Collections.singletonMap("a", map1);
    List list0 = List.of("hi", map2);
    assertFalse(isDeepNotEmpty(list0));
    assertFalse(isDeepNotEmpty(List.of("Hi", Collections.emptySet())));
    assertTrue(isDeepNotEmpty(IntList.of(1, 2)));
    assertFalse(isDeepNotEmpty(IntList.of()));
    assertTrue(isDeepNotEmpty(Optional.of(IntList.of(1, 2))));
    assertFalse(isDeepNotEmpty(Optional.of(IntList.of())));
    assertTrue(isDeepNotEmpty(ints(1, 2, 3)));
    assertFalse(isDeepNotEmpty(ints()));
  }

  @Test
  public void ifNull00() {
    assertEquals("13", ifNull("13", "14"));
    assertEquals("13", ifNull("13", () -> "14"));
    assertEquals("14", ifNull(null, "14"));
    assertEquals("14", ifNull(null, () -> "14"));
  }

  @Test
  public void ifEmpty00() {
    assertEquals("Hi There", ifEmpty("", "Hi There"));
    assertEquals("Hi There", ifEmpty(null, "Hi There"));
    assertEquals(null, ifEmpty(null, (String) null));
    assertEquals("World", ifEmpty("World", "Hi There"));
    assertEquals("Hi There", ifEmpty("", () -> "Hi There"));
    assertEquals("Hi There", ifEmpty(null, () -> "Hi There"));
    assertEquals("World", ifEmpty("World", () -> "Hi There"));
    List list0 = List.of("Hi There");
    assertEquals(list0, ifEmpty(emptyList(), () -> asList("Hi There")));
  }

  @Test
  public void ifNotNull00() {
    assertEquals(7, ifNotNull("7", Integer::valueOf).intValue());
    String s = null;
    assertNull(ifNotNull(s, Integer::valueOf));
    assertEquals(8, ifNotNull(s, Integer::valueOf, 8).intValue());
  }

  @Test
  public void ifNotEmpty00() {
    Optional<String> opt1 = Optional.empty();
    Optional<String> opt2 = Optional.of("");
    Optional<String> opt3 = Optional.of("Hi");
    assertEquals("FOO", ifNotEmpty(opt1, Optional::get, "FOO"));
    assertEquals("FOO", ifNotEmpty(opt2, Optional::get, "FOO"));
    assertEquals("Hi", ifNotEmpty(opt3, Optional::get, "FOO"));
    assertEquals("foo", ifNotEmpty("x", x -> "foo"));
    assertNull(ifNotEmpty("", x -> "foo"));
    assertNull(ifNotEmpty(null, x -> "foo"));
    assertNull(ifNotEmpty(List.of(), x -> "foo"));
    assertEquals(List.of(1, 2, 3, 4),
        ifNotEmpty(WiredList.of(1, 2, 3), x -> x.append(4)));
  }

  @Test
  public void e2nDeepEquals00() {
    assertTrue(e2nDeepEquals(null, ""));
    assertTrue(e2nDeepEquals(null, null));
    assertTrue(e2nDeepEquals(null, new Enum[0]));
    assertTrue(e2nDeepEquals(new int[0], null));
    assertTrue(e2nDeepEquals(new String[0], null));
    assertFalse(e2nDeepEquals(
        new String[] {""},
        null));
    assertFalse(e2nDeepEquals(
        new String[] {"", null, ""},
        null));
    assertFalse(e2nDeepEquals(
        new String[] {"", null, ""},
        new String[] {"", null, "", "", ""}));
    assertTrue(e2nDeepEquals(emptyList(), null));
    assertTrue(e2nDeepEquals(null, new HashSet<>()));
    assertTrue(e2nDeepEquals(null, null));
    assertTrue(e2nDeepEquals("", ""));
    assertTrue(e2nDeepEquals(
        List.of(1, 2, 3, 4),
        List.of(1, 2, 3, 4)));
    assertTrue(e2nDeepEquals(
        new String[] {"To", "be", "or"},
        new String[] {"To", "be", "or"}));
    assertTrue(e2nDeepEquals(
        new int[] {1, 2, 3, 4},
        new int[] {1, 2, 3, 4}));
    assertFalse(e2nDeepEquals(
        new int[0],
        new HashSet<>()));
    assertFalse(e2nDeepEquals("", new HashSet<>()));
    assertFalse(e2nDeepEquals(
        new ArrayList<>(),
        new HashSet<>()));
    assertTrue(e2nDeepEquals(Map.of(), new HashMap<>()));
    assertTrue(e2nDeepEquals(null, new HashMap<>()));
    assertTrue(e2nDeepEquals(Map.of("", Set.of()), Map.of("", Set.of())));
    assertFalse(e2nDeepEquals(Map.of("", Set.of()), Map.of("", List.of())));
    assertFalse(e2nDeepEquals(Map.of(1, 2), new HashMap<>()));
    assertFalse(e2nDeepEquals(null, new File("/tmp")));
  }

  @Test // behaviour with sets (pretty extreme edge cases)
  public void e2nDeepEquals01() {

    Set subsubset1 = setOf("John");
    Set subsubset2 = setOf("John", null);
    Set subsubset3 = setOf("John", "", null, new int[0]);
    Set subsubset4 = setOf("John", "", null, new short[0]);
    Set subsubset5 = setOf("John", emptyList(), null, new short[0]);
    Set subsubset6 = setOf("Mark", emptyList(), null, new short[0]);
    Set subsubset7 = setOf("John", "Mark", new String[0], null, new short[0]);

    Set subset1 = setOf("Mary", subsubset1, subsubset2, subsubset4);
    Set subset2 = setOf("Mary", subsubset2, subsubset3, subsubset4);
    Set subset3 = setOf("Mary",
        subsubset2,
        subsubset3,
        subsubset4,
        Collections.emptySet());
    Set subset4 = setOf("Mary", subsubset3, subsubset4, subsubset5, new short[0]);
    Set subset5 = setOf("Mary", subsubset4, subsubset5, new short[] {1, 2});
    Set subset6 = setOf(subsubset4);
    Set subset7 = setOf(subsubset5);

    assertFalse(e2nDeepEquals(subsubset1, subsubset2));
    assertTrue(e2nDeepEquals(subsubset2, subsubset3));
    assertTrue(e2nDeepEquals(subsubset4, subsubset5));
    assertFalse(e2nDeepEquals(subsubset5, subsubset6));
    assertFalse(e2nDeepEquals(subsubset5, subsubset7));

    assertFalse(e2nDeepEquals(subset1, subset2));
    assertFalse(e2nDeepEquals(subset2, subset4));
    assertTrue(e2nDeepEquals(subset3, subset4));
    assertFalse(e2nDeepEquals(subset4, subset5));
    assertTrue(e2nDeepEquals(subset6, subset7));
  }

  private static Set setOf(Object... objs) {
    return Arrays.stream(objs).collect(Collectors.toSet());
  }

  @Test
  public void n2e00() {
    assertTrue(n2e((String) null).equals(""));
    assertTrue(n2e((List) null).equals(List.of()));
    assertTrue(n2e((Set) null).equals(Set.of()));
    assertTrue(n2e((Map) null).equals(Map.of()));
    assertTrue(n2e((Integer) null) == 0);
    assertTrue(n2e((Double) null) == 0D);
    assertTrue(n2e((Long) null) == 0L);
    assertTrue(n2e((Float) null) == 0F);
    assertTrue(n2e((Short) null) == (short) 0);
    assertTrue(n2e((Byte) null) == (byte) 0);
    assertTrue(n2e((Character) null) == '\0');
    assertTrue(n2e((Boolean) null) == false);
    assertTrue(n2e(2) == 2);
    assertTrue(n2e(2.0) == 2D);
    assertTrue(n2e(2L) == 2L);
    assertTrue(n2e(2.0F) == 2F);
    assertTrue(n2e((short) 2) == (short) 2);
    assertTrue(n2e((byte) 2) == (byte) 2);
  }

  @Test
  public void e2n00() {
    assertNull(e2n(""));
    assertNull(e2n(List.of()));
    assertNull(e2n(Set.of()));
    assertNull(e2n(Map.of()));
    assertNull(e2n(EMPTY_STRING_ARRAY));
    assertNull(e2n(new int[0]));
    assertNull(e2n(new char[0]));

    assertNotNull(e2n("foo"));
    assertNotNull(e2n(List.of("foo")));
    assertNotNull(e2n(Set.of("foo")));
    assertNotNull(e2n(Map.of("foo", "bar")));
    assertNotNull(e2n(pack("foo", "bar")));
    assertNotNull(e2n(ints(1, 2, 3)));
    assertNotNull(e2n(chars('a', 'b', 'c')));
  }

  @Test
  public void nullUnless00() {
    assertNull(nullUnless(8, 6, 7, 9));
    assertEquals(8, (int) nullUnless(8, 6, 7, 8));
  }

  @Test
  public void nullIf00() {
    assertNull(nullIf(8, 6, 7, 8));
    assertEquals(8, (int) nullIf(8, 6, 7, 9));
  }

  @Test
  public void replaceIf00() {
    assertEquals("42", replaceIf("42", empty(), "43"));
    assertEquals("43", replaceIf("", empty(), "43"));
    assertEquals("42", clamp("42", GT(), "43"));
    assertEquals("43", clamp("43", GT(), "43"));
    assertEquals("43", clamp("44", GT(), "43"));
    assertEquals("44", replaceIf("44", LT(), "43", "50"));
    assertEquals("50", replaceIf("40", LT(), "43", "50"));
  }

  @Test
  public void replaceIf02() {
    assertEquals(42, replaceIf(42, odd(), -1));
    assertEquals(-1, replaceIf(42, even(), -1));
    assertEquals(42, ObjectMethods.clamp(42, gt(), 43));
    assertEquals(43, ObjectMethods.clamp(43, gt(), 43));
    assertEquals(43, ObjectMethods.clamp(44, gt(), 43));
    assertEquals(44, replaceIf(44, lt(), 43, 50));
    assertEquals(50, replaceIf(40, lt(), 43, 50));
  }

  @Test(expected = ClassCastException.class)
  public void bruteCast00() {
    String s = bruteCast(new File("/tmp/foo.txt"));
  }

  @Test
  public void bruteCast01() {
    List<CharSequence> l0 = List.of("Hello", "world");
    // WON'T COMPILE: List<String> l2 = l0;
    List<String> l2 = bruteCast(l0);
    assertEquals(Arrays.asList("Hello", "world"), l2);
  }

}
