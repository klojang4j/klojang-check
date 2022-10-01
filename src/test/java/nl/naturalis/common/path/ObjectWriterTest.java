package nl.naturalis.common.path;

import nl.naturalis.common.ArrayMethods;
import nl.naturalis.common.util.MapBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static nl.naturalis.common.ArrayMethods.ints;
import static nl.naturalis.common.ArrayMethods.pack;
import static nl.naturalis.common.CollectionMethods.implode;
import static org.junit.Assert.*;

public class ObjectWriterTest {

  @Test
  public void write00() { // list
    Map<String, Object> map = MapBuilder.begin()
        .set("foo.bar.bozo", Arrays.asList("to", "be", "or", "not", "to", "be"))
        .createMap();
    ObjectWriter ow = new ObjectWriter(true, null);
    ow.write(map, Path.from("foo.bar.bozo.2"), "nor");
    List<String> l = MapBuilder.begin(map).get("foo.bar.bozo");
    assertEquals("to be nor not to be", implode(l, " "));
  }

  @Test
  public void write01() { // array
    Map<String, Object> map = MapBuilder.begin()
        .set("foo.bar.bozo", pack("to", "be", "or", "not", "to", "be"))
        .createMap();
    ObjectWriter ow = new ObjectWriter(true, null);
    ow.write(map, Path.from("foo.bar.bozo.2"), "nor");
    String[] array = MapBuilder.begin(map).get("foo.bar.bozo");
    assertEquals("to be nor not to be", ArrayMethods.implode(array, " "));
  }

  @Test
  public void write02() { // primitive array
    Map<String, Object> map = MapBuilder.begin()
        .set("foo.bar.bozo", ints(0, 1, 2, 3, 4, 5))
        .createMap();
    ObjectWriter ow = new ObjectWriter(true, null);
    ow.write(map, Path.from("foo.bar.bozo.2"), 42);
    int[] array = MapBuilder.begin(map).get("foo.bar.bozo");
    assertArrayEquals(ints(0, 1, 42, 3, 4, 5), array);
  }

  @Test(expected = PathWalkerException.class)
  public void write03() { // primitive array
    Map<String, Object> map = MapBuilder.begin()
        .set("foo.bar.bozo", null)
        .createMap();
    ObjectWriter ow = new ObjectWriter(false, null);
    try {
      ow.write(map, Path.from("foo.bar.bozo.teapot"), 42);
    } catch (PathWalkerException e) {
      assertEquals(ErrorCode.TERMINAL_VALUE, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void write04() {
    Map<String, Object> map = MapBuilder.begin()
        .set("foo.bar.bozo", 42)
        .createMap();
    ObjectWriter ow = new ObjectWriter(true, null);
    assertFalse(ow.write(map, Path.from("foo.bar.bozo.teapot"), "one step too far"));
  }

  @Test(expected = PathWalkerException.class)
  public void write05() {
    Map<String, Object> map = MapBuilder.begin()
        .set("foo.bar.bozo", 42)
        .createMap();
    ObjectWriter ow = new ObjectWriter(false, null);
    try {
      ow.write(map, Path.from("foo.bar.bozo.teapot"), "one step too far");
    } catch (PathWalkerException e) {
      assertEquals(ErrorCode.TERMINAL_VALUE, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void write07() {
    ObjectWriter ow = new ObjectWriter(true, null);
    assertFalse(ow.write(null, Path.of("foo"), 7));
  }

  @Test(expected = PathWalkerException.class)
  public void write08() {
    ObjectWriter ow = new ObjectWriter(false, null);
    try {
      ow.write(null, Path.of("foo"), 7);
    } catch (PathWalkerException e) {
      assertEquals(ErrorCode.TERMINAL_VALUE, e.getErrorCode());
      throw e;
    }
  }

}