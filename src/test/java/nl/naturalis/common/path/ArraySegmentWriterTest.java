package nl.naturalis.common.path;

import org.junit.Test;

import static nl.naturalis.common.path.ErrorCode.*;
import static org.junit.Assert.*;

public class ArraySegmentWriterTest {

  @Test
  public void test01a() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(true, null);
    assertTrue(writer.write(array, Path.from("2"), 42));
    assertEquals(42, array[2]);
  }

  @Test
  public void test01b() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(false, null);
    assertTrue(writer.write(array, Path.from("2"), 42));
    assertEquals(42, array[2]);
  }

  @Test
  public void test02() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(true, null);
    assertTrue(writer.write(array, Path.from("path.to.array.3"), 42));
    assertEquals(42, array[3]);
  }

  @Test
  public void test03a() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(true, null);
    // INDEX_OUT_OF_BOUNDS
    assertFalse(writer.write(array, Path.from("8"), 42));
  }

  @Test(expected = PathWalkerException.class)
  public void test03b() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(false, null);
    try {
      writer.write(array, Path.from("8"), 42);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_OUT_OF_BOUNDS, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void test04a() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(true, null);
    // INDEX_OUT_OF_BOUNDS
    assertFalse(writer.write(array, Path.from("path.to.array.8"), 42));
  }

  @Test(expected = PathWalkerException.class)
  public void test04b() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(false, null);
    try {
      writer.write(array, Path.from("path.to.array.8"), 42);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_OUT_OF_BOUNDS, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void test05a() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(true, null);
    // INDEX_EXPECTED
    assertFalse(writer.write(array, Path.from("path.to.array.foo"), 42));
  }

  @Test(expected = PathWalkerException.class)
  public void test05b() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(false, null);
    try {
      writer.write(array, Path.from("path.to.array.foo"), 42);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_EXPECTED, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void test06a() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(false, null);
    try {
      writer.write(array, Path.from("path.to.array."), 42);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_EXPECTED, e.getErrorCode());
      return;
    }
    fail();
  }

  @Test
  public void test06b() throws Throwable {
    Object[] array = new Object[] {1, 2, 3, 4};
    ArraySegmentWriter writer = new ArraySegmentWriter(true, null);
    assertFalse(writer.write(array, Path.from("path.to.array."), 42));
  }

}
