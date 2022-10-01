package nl.naturalis.common;

import org.junit.Test;

import java.io.File;

import static nl.naturalis.common.ArrayMethods.pack;
import static nl.naturalis.common.ArrayType.describe;
import static org.junit.Assert.*;

public class ArrayTypeTest {

  @Test
  public void dimensions00() {
    assertEquals(0, ArrayType.dimensions(int.class));
    assertEquals(1, ArrayType.dimensions(int[].class));
    assertEquals(2, ArrayType.dimensions(int[][].class));
  }

  @Test
  public void forClass00() {
    ArrayType at0 = ArrayType.forClass(String[][].class);
    ArrayType at1 = ArrayType.forClass(String[][].class);
    ArrayType at2 = ArrayType.forClass(String[][].class);
    ArrayType at3 = ArrayType.forClass(int[][].class);
    ArrayType at4 = ArrayType.forClass(String[][].class);
    assertSame(at0, at1);
    assertSame(at0, at2);
    assertNotSame(at0, at3);
    assertNotSame(at0, at4);
    assertEquals(at0, at4);
  }

  @Test
  public void forClass01() {
    ArrayType at = ArrayType.forClass(String[][].class);
    assertEquals(String.class, at.baseType());
    assertEquals(2, at.dimensions());
  }

  @Test
  public void forClass02() {
    ArrayType at = ArrayType.forClass(int[][][][][].class);
    assertEquals(int.class, at.baseType());
    assertEquals(5, at.dimensions());
  }

  @Test
  public void forArray00() {
    ArrayType at0 = ArrayType.forArray(new File[0][][]);
    ArrayType at1 = ArrayType.forArray(new File[6][][]);
    ArrayType at2 = ArrayType.forArray(new File[11][][]);
    assertSame(at0, at1);
    assertSame(at0, at2);
  }

  @Test
  public void constructor00() {
    ArrayType at = new ArrayType(long.class, 1);
    assertEquals(long.class, at.baseType());
    assertEquals(1, at.dimensions());
    assertEquals(long[].class, at.toClass());
  }

  @Test
  public void constructor01() {
    ArrayType at = new ArrayType(long[][].class, 1);
    assertEquals(long.class, at.baseType());
    assertEquals(3, at.dimensions());
    assertEquals(long[][][].class, at.toClass());
  }

  @Test
  public void constructor02() {
    ArrayType at = new ArrayType(long[][].class, 0);
    assertEquals(long.class, at.baseType());
    assertEquals(2, at.dimensions());
    assertEquals(long[][].class, at.toClass());
  }

  @Test
  public void constructor03() {
    ArrayType at = new ArrayType(long[][].class, -1);
    assertEquals(long.class, at.baseType());
    assertEquals(1, at.dimensions());
    assertEquals(long[].class, at.toClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor04() {
    new ArrayType(void.class, -1);
  }

  @Test
  public void forArray01() {
    ArrayType at = ArrayType.forArray(new File[0][][]);
    assertEquals(File.class, at.baseType());
    assertEquals(3, at.dimensions());
  }

  @Test
  public void toClass00() {
    ArrayType at = new ArrayType(short.class, 3);
    assertEquals(short[][][].class, at.toClass());
  }

  @Test
  public void toClass01() {
    ArrayType at = new ArrayType(short.class, 3);
    assertEquals(int[][][].class, at.toClass(int.class));
  }

  @Test
  public void toClass02() {
    ArrayType at = new ArrayType(short.class, 3);
    assertEquals(short[][][][].class, at.toClass(4));
  }

  @Test
  public void boxed00() {
    ArrayType at = new ArrayType(short.class, 1);
    assertEquals(new ArrayType(Short.class, 1), at.boxed());
  }

  @Test
  public void unboxed00() {
    ArrayType at = new ArrayType(Double.class, 4);
    assertEquals(new ArrayType(double.class, 4), at.unboxed());
  }

  @Test
  public void box00() {
    ArrayType at = new ArrayType(short.class, 1);
    assertEquals(Short[].class, at.box());
  }

  @Test
  public void unbox00() {
    ArrayType at = new ArrayType(Double.class, 4);
    assertEquals(double[][][][].class, at.unbox());
  }

  @Test
  public void toString00() {
    ArrayType at = new ArrayType(double.class, 2);
    assertEquals("double[][]", at.toString());
  }

  @Test
  public void toString01() {
    ArrayType at = new ArrayType(double[].class, 2);
    assertEquals("double[][][]", at.toString());
  }

  @Test
  public void toString02() {
    ArrayType at = new ArrayType(double[][].class, 2);
    assertEquals("double[][][][]", at.toString());
  }

  @Test
  public void arrayClassName00() {
    ArrayType at = new ArrayType(Double.class, 2);
    assertEquals("java.lang.Double[][]", at.arrayClassName());
  }

  @Test
  public void describe00() {
    assertEquals("Double[7][]", describe(new Double[7][4]));
    assertEquals("String[2]", describe(pack("hello", "world")));
    assertEquals("int[0]", describe(new int[0]));
  }

  @Test(expected = IllegalArgumentException.class)
  public void describe01() {
    describe("hello");
  }

}
