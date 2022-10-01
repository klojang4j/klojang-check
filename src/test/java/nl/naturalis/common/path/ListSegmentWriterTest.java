package nl.naturalis.common.path;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static nl.naturalis.common.path.ErrorCode.*;
import static org.junit.Assert.*;

public class ListSegmentWriterTest {

  @Test
  public void test01a() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(true, null);
    assertTrue(writer.write(l, Path.from("2"), 42));
    assertEquals(42, l.get(2));
  }

  @Test
  public void test01b() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(false, null);
    assertTrue(writer.write(l, Path.from("2"), 42));
    assertEquals(42, l.get(2));
  }

  @Test
  public void test02() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(true, null);
    assertTrue(writer.write(l, Path.from("path.to.list.3"), 42));
    assertEquals(42, l.get(3));
  }

  @Test
  public void test03a() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(true, null);
    assertFalse(writer.write(l, Path.from("8"), 42));
  }

  @Test(expected = PathWalkerException.class)
  public void test03b() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(false, null);
    try {
      writer.write(l, Path.from("8"), 42);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_OUT_OF_BOUNDS, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void test04a() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(true, null);
    assertFalse(writer.write(l, Path.from("path.to.list.8"), 42));
  }

  @Test(expected = PathWalkerException.class)
  public void test04b() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(false, null);
    try {
      writer.write(l, Path.from("path.to.list.8"), 42);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_OUT_OF_BOUNDS, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void test05a() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(true, null);
    assertFalse(writer.write(l, Path.from("path.to.list.foo"), 42));
  }

  @Test(expected = PathWalkerException.class)
  public void test05b() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(false, null);
    try {
      writer.write(l, Path.from("path.to.list.foo"), 42);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_EXPECTED, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void test06a() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(false, null);
    try {
      writer.write(l, Path.from("path.to.list."), 42);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_EXPECTED, e.getErrorCode());
      return;
    }
    fail();
  }

  @Test
  public void test06b() {
    List l = new ArrayList(List.of(1, 2, 3, 4));
    ListSegmentWriter writer = new ListSegmentWriter(true, null);
    assertFalse(writer.write(l, Path.from("path.to.list."), 42));
  }

}
