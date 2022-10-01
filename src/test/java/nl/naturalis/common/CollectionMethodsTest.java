package nl.naturalis.common;

import nl.naturalis.common.collection.DuplicateValueException;
import nl.naturalis.common.collection.WiredList;
import nl.naturalis.common.util.MutableInt;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import static nl.naturalis.common.CollectionMethods.*;
import static nl.naturalis.check.CommonProperties.strval;
import static org.junit.Assert.*;

public class CollectionMethodsTest {

  @Test
  public void fromToIndex00() {
    List<Integer> l = List.of(0, 1, 2);
    assertEquals(Collections.emptyList(), l.subList(2, 2));
  }

  @Test
  public void fromToIndex01() {
    List<Integer> l = List.of(0, 1, 2);
    // One position past the end of the list - still allowed with subXXXXX methods
    // in the JDK
    assertEquals(Collections.emptyList(), l.subList(3, 3));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void fromToIndex03() {
    List<Integer> l = List.of(0, 1, 2);
    l.subList(3, 4);
  }

  @Test
  public void asList00() {
    assertEquals(Collections.singletonList(null), asList(null));
    assertEquals(List.of("Hello World"), asList("Hello World"));
    assertEquals(List.of("Hello", "World"), asList(new String[] {"Hello", "World"}));
    Object obj = new Object();
    assertEquals(Collections.singletonList(obj), asList(obj));
    assertEquals(List.of(1, 2, 3, 4, 5), asList(new int[] {1, 2, 3, 4, 5}));
    assertEquals(List.of(1D, 2D, 3D, 4D, 5D), asList(new double[] {1, 2, 3, 4, 5}));
    assertEquals(List.of(1L, 2L, 3L, 4L, 5L), asList(new long[] {1, 2, 3, 4, 5}));
    assertEquals(List.of(1F, 2F, 3F, 4F, 5F), asList(new float[] {1, 2, 3, 4, 5}));
    assertEquals(List.of((short) 1, (short) 2, (short) 3),
        asList(new short[] {1, 2, 3}));
    assertEquals(List.of((byte) 1, (byte) 2, (byte) 3),
        asList(new byte[] {1, 2, 3}));
    assertEquals(List.of((char) 1, (char) 2, (char) 3),
        asList(new char[] {1, 2, 3}));
    Set<Integer> s = new LinkedHashSet<>();
    s.add(1);
    s.add(2);
    s.add(3);
    assertEquals(List.of(1, 2, 3), asList(s));
    assertEquals(
        List.of(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE),
        asList(new boolean[] {true, false, true}));
    List l = new ArrayList();
    l.add(10);
    assertSame(l, asList(l));
  }

  @Test
  public void sublist00() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("234", concat(sublist(chars, 2, 3)));
  }

  @Test
  public void sublist01() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("234", concat(sublist(chars, 4, -3)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void sublist02() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    sublist(chars, 4, -50);
  }

  @Test
  public void sublist03() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("89", concat(sublist(chars, -2, 2)));
  }

  @Test
  public void sublist04() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("78", concat(sublist(chars, -2, -2)));
  }

  @Test
  public void sublist05() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("4567", concat(sublist(chars, -3, -4)));
  }

  @Test
  public void sublist06() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("", concat(sublist(chars, 10, 0)));
  }

  @Test
  public void sublist07() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("", concat(sublist(chars, 9, 0)));
  }

  @Test
  public void sublist08() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("", concat(sublist(chars, 0, 0)));
  }

  private static String concat(List<String> chars) {
    return chars.stream().collect(Collectors.joining());
  }

  @Test
  public void implode01() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("234", implode(chars, Objects::toString, "", 2, 5));
  }

  @Test
  public void implode02() {
    List<String> chars = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    assertEquals("2/3/4", implode(chars, Objects::toString, "/", 2, 5));
  }

  @Test
  public void implode03() {
    Collection<Class<?>> coll =
        Arrays.asList(StringMethods.class, ArrayMethods.class, ClassMethods.class);
    assertEquals(
        "StringMethods, ArrayMethods, ClassMethods",
        implode(coll, ClassMethods::simpleClassName));
  }

  @Test
  public void implode04() {
    assertEquals("0, 1, 2", implode(List.of("0", "1", "2")));
  }

  @Test
  public void implode05() {
    assertEquals("0, 1", implode(List.of("0", "1", "2"), 2));
  }

  @Test
  public void implode06() {
    assertEquals("0|1|2", implode(List.of("0", "1", "2"), "|", 20));
  }

  @Test
  public void implode07() {
    Set<Integer> s = new LinkedHashSet<>();
    s.add(1);
    s.add(2);
    s.add(3);
    assertEquals("2|3", implode(s, strval(), "|", 1, 20));
  }

  @Test
  public void implode08() {
    Set<Integer> s = new LinkedHashSet<>();
    s.add(1);
    s.add(2);
    s.add(3);
    assertEquals("2|3", implode(s, strval(), "|", 1, -1));
  }

  @Test
  public void implode09() {
    List<Integer> l = List.of(1, 2, 3);
    assertEquals("2|3", implode(l, strval(), "|", 1, -1));
  }

  @Test
  public void initializeList00() {
    List<String> l = initializeList(5, "FOO");
    assertEquals(List.of("FOO", "FOO", "FOO", "FOO", "FOO"), l);
  }

  @Test
  public void initializeList01() {
    List<String> l = initializeList(0, "FOO");
    assertEquals(List.of(), l);
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeList02() {
    initializeList(5, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeList03() {
    initializeList(-1, "FOO");
  }

  @Test
  public void initializeList04() {
    MutableInt provider = new MutableInt(10);
    List<Integer> l = initializeList(5, provider::pp);
    assertEquals(List.of(10, 11, 12, 13, 14), l);
  }

  @Test
  public void initializeMap00() {
    Map<String, Integer> m = newHashMap(10,
        String.class,
        Integer.class,
        "a",
        1,
        "b",
        2,
        "c",
        null);
    assertEquals(3, m.size());
    assertEquals(Integer.valueOf(1), m.get("a"));
    assertEquals(Integer.valueOf(2), m.get("b"));
    assertTrue(m.containsKey("c"));
    assertNull(m.get("c"));
  }

  @Test
  public void initializeMap01() {
    Map<String, Integer> m = newHashMap(1,
        String.class,
        Integer.class,
        "a",
        1,
        "b",
        2,
        "c",
        null);
    assertEquals(3, m.size());
    assertEquals(Integer.valueOf(1), m.get("a"));
    assertEquals(Integer.valueOf(2), m.get("b"));
    assertTrue(m.containsKey("c"));
    assertNull(m.get("c"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializeMap02() {
    int badSize = -3;
    newHashMap(badSize,
        String.class,
        Object.class,
        "a",
        new File("/"),
        "b",
        2,
        "c",
        null);
  }

  @Test
  public void freeze00() {
    Map<String, Integer> src =
        newHashMap(10, String.class, Integer.class, "foo", 1, "bar", 2, "baz", 3);
    Map<String, String> map0 = freeze(src, String::valueOf);
    Map<String, String> expected0 = Map.of("foo", "1", "bar", "2", "baz", "3");
    Map<String, Short> map1 = freeze(src, i -> (short) (i * 3));
    Map<String, Short> expected1 = Map.of("foo",
        (short) 3,
        "bar",
        (short) 6,
        "baz",
        (short) 9);
    assertEquals(expected1, map1);
  }

  @Test
  public void freeze01() {
    Map<Integer, Integer> src =
        newHashMap(10,
            Integer.class,
            Integer.class,
            1,
            1,
            2,
            2,
            3,
            3,
            4,
            4,
            5,
            5,
            6,
            6);
    Map<Integer, Integer> out =
        freeze(
            src,
            (k, v) -> {
              if (k % 3 == 0) {
                return k;
              }
              return v * 2;
            });
    Map<Integer, Integer> expected =
        newHashMap(10,
            Integer.class,
            Integer.class,
            1,
            2,
            2,
            4,
            3,
            3,
            4,
            8,
            5,
            10,
            6,
            6);
    assertEquals(expected, out);
  }

  @Test
  public void freeze02() {
    List<Integer> out = freeze(List.of(1, 2, 3), i -> -i);
    assertEquals(List.of(-1, -2, -3), out);
  }

  @Test
  public void freeze03() {
    Set<Integer> out = freeze(Set.of(1, 2, 3), i -> -i);
    assertEquals(Set.of(-1, -2, -3), out);
  }

  @Test(expected = FileNotFoundException.class)
  public void freeze04() throws FileNotFoundException {
    freeze(
        Set.of(1, 2, 3),
        i -> {
          if (i == 2) {
            throw new FileNotFoundException();
          }
          return 1;
        });
  }

  @Test
  public void deepFreeze00() {
    Map<Integer, String> src =
        newHashMap(10, Integer.class, String.class, 1, "foo", 2, "bar", 3, "baz");
    Map<Integer, Integer> out = deepFreeze(src,
        e -> Map.entry(e.getValue().length(), e.getKey()));
    assertEquals(1, out.size());
    assertTrue(out.containsKey(3));
  }

  @Test
  public void collectionToList00() {
    List<Integer> src = Arrays.asList(1, 2, 3);
    List<Integer> list0 = collectionToList(src, i -> i * 2);
    assertEquals(List.of(2, 4, 6), list0);
    List<String> list1 = collectionToList(src, String::valueOf);
    assertEquals(List.of("1", "2", "3"), list1);
  }

  @Test
  public void collectionToSet00() {
    List<Integer> src = Arrays.asList(1, 2, 3);
    Set<Integer> set0 = collectionToSet(src, i -> i * 2);
    assertEquals(Set.of(2, 4, 6), set0);
    Set<String> set1 = collectionToSet(src, String::valueOf);
    assertEquals(Set.of("1", "2", "3"), set1);
  }

  @Test
  public void collectionToMap00() {
    List<Integer> src = Arrays.asList(1, 2, 3);
    Map<String, Integer> map0 = collectionToMap(src, i -> String.valueOf(i * 2));
    Map<String, Integer> expected = Map.of("2", 1, "4", 2, "6", 3);
    assertEquals(expected, map0);
  }

  @Test
  public void swap00() {
    Map<String, Integer> src = Map.of("a", 1, "b", 2, "c", 3);
    Map<Integer, String> swapped = swap(src);
    Map<Integer, String> expected = Map.of(1, "a", 2, "b", 3, "c");
    assertEquals(expected, swapped);
  }

  @Test(expected = DuplicateValueException.class)
  public void swap01() {
    Map<String, Integer> src = Map.of("a", 1, "b", 2, "c", 2);
    swap(src);
  }

  @Test
  public void swap02() {
    Map<String, Integer> src = Map.of("a", 1, "b", 2, "c", 3);
    Map<Integer, String> swapped = swap(src, LinkedHashMap::new);
    Map<Integer, String> expected = Map.of(1, "a", 2, "b", 3, "c");
    assertEquals(expected, swapped);
    assertEquals(LinkedHashMap.class, swapped.getClass());
  }

  @Test
  public void swapAndFreeze00() {
    Map<String, Integer> src = Map.of("a", 1, "b", 2, "c", 3);
    Map<Integer, String> swapped = swapAndFreeze(src);
    Map<Integer, String> expected = Map.of(1, "a", 2, "b", 3, "c");
    assertEquals(expected, swapped);
  }

  @Test(expected = IllegalArgumentException.class)
  public void swapAndFreeze01() {
    Map<String, Integer> src = new HashMap<>(Map.of("a", 1, "b", 2, "c", 3));
    src.put("4", null);
    swapAndFreeze(src);
  }

  @Test
  public void saturatedEnumMap00() {
    Map<DayOfWeek, String> m =
        saturatedEnumMap(
            DayOfWeek.class,
            "monday",
            "tuesday",
            "wednesday",
            "thursday",
            "friday",
            "saturday",
            "sunday");
    Arrays.stream(DayOfWeek.values())
        .forEach(dow -> assertEquals(dow.name().toLowerCase(), m.get(dow)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void saturatedEnumMap01() {
    saturatedEnumMap(DayOfWeek.class, "monday", "tuesday", "saturday", "sunday");
  }

  @Test
  public void isNullRepellent00() {
    assertFalse(isNullRepellent(new ArrayList<>()));
    assertFalse(isNullRepellent(new HashSet<>()));
    assertFalse(isNullRepellent(new LinkedList<>()));
    assertFalse(isNullRepellent(new WiredList<>()));
    assertTrue(isNullRepellent(Set.of("foo")));
    assertTrue(isNullRepellent(List.of("foo")));
    assertTrue(isNullRepellent(Set.of("foo", "bar", "bozo", "teapot")));
    assertTrue(isNullRepellent(List.of("foo", "bar", "bozo", "teapot")));
    assertTrue(isNullRepellent(Collections.emptyList()));
    assertTrue(isNullRepellent(Collections.emptySet()));
  }

}
