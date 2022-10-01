package nl.naturalis.common.path;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static nl.naturalis.common.path.ErrorCode.*;
import static org.junit.Assert.*;

public class MapSegmentWriterTest {

  KeyDeserializer kds = (path, segment) -> {
    try {
      return Integer.valueOf(path.segment(-1));
    } catch (Exception e) {
      throw new KeyDeserializationException();
    }
  };

  @Test
  public void test01a() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(true, kds);
    assertTrue(writer.write(m, Path.from("2"), "MARK"));
    assertEquals("MARK", m.get(2));
  }

  @Test
  public void test01b() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(false, kds);
    assertTrue(writer.write(m, Path.from("2"), 42));
    assertEquals(42, m.get(2));
  }

  @Test
  public void test02() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(true, kds);
    assertTrue(writer.write(m, Path.from("path.to.map.3"), 42));
    assertEquals(42, m.get(3));
  }

  @Test
  public void test03a() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(true, kds);
    assertTrue(writer.write(m, Path.from("8"), 42));
  }

  @Test
  public void test03b() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(false, kds);
    writer.write(m, Path.from("8"), 42);
    assertEquals(42, m.get(8));
  }

  @Test
  public void test04a() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(true, kds);
    assertTrue(writer.write(m, Path.from("path.to.map.8"), 42));
  }

  @Test
  public void test04b() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(false, kds);
    writer.write(m, Path.from("path.to.map.8"), 42);
    assertEquals(42, m.get(8));
  }

  @Test
  public void test05a() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(true, kds);
    // KEY_DESERIALIZATION_FAILED
    assertFalse(writer.write(m, Path.from("path.to.map.foo"), 42));
  }

  @Test(expected = PathWalkerException.class)
  public void test05b() {
    Map m = new HashMap(Map.of(1, "john", 2, "mark", 3, "tom", 4, "jim"));
    MapSegmentWriter writer = new MapSegmentWriter(false, kds);
    try {
      writer.write(m, Path.from("path.to.map.foo"), 42);
    } catch (PathWalkerException e) {
      System.out.println(e.getMessage());
      assertEquals(KEY_DESERIALIZATION_FAILED, e.getErrorCode());
      throw e;
    }
  }

  @Test(expected = PathWalkerException.class)
  public void test06() {
    Map m = Map.of("foo", "bar");
    MapSegmentWriter writer = new MapSegmentWriter(false, null);
    try {
      writer.write(m, Path.from("foo"), "fox");
    } catch (PathWalkerException e) {
      assertEquals(NOT_MODIFIABLE, e.getErrorCode());
      throw e;
    }
  }

}
