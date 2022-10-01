package nl.naturalis.common.util;

import org.junit.Test;

import java.nio.BufferOverflowException;

import static nl.naturalis.common.util.ResizeMethod.*;
import static org.junit.Assert.assertEquals;

public class ResizeMethodTest {

  private static final int MAX_INT = Integer.MAX_VALUE;

  @Test
  public void multiply00() {
    assertEquals(30, MULTIPLY.resize(10, 3, 2));
    assertEquals(13, MULTIPLY.resize(10, 1.3, 2));
    // zero capacity will be tacitly upgraded to 1
    assertEquals(10, MULTIPLY.resize(0, 10));
    assertEquals(10 + MAX_INCREASE, MULTIPLY.resize(10, MAX_INT, 2));
    assertEquals(MAX_INT, MULTIPLY.resize(MAX_INT - 7, 1.5, 7));
  }

  @Test(expected = BufferOverflowException.class)
  public void multiply01() {
    MULTIPLY.resize(MAX_INT, 3, 2);
  }

  @Test(expected = BufferOverflowException.class)
  public void multiply02() {
    MULTIPLY.resize(MAX_INT - 7, 1.5, 8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void multiply03() {
    MULTIPLY.resize(-1, 1.5, 8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void multiply04() {
    MULTIPLY.resize(10, 1.5, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void multiply05() {
    MULTIPLY.resize(10, .2);
  }

  @Test
  public void percentage00() {
    assertEquals(15, PERCENTAGE.resize(10, 50, 2));
    assertEquals(10 + MAX_INCREASE + 5, PERCENTAGE.resize(10, 50, MAX_INCREASE + 5));
    assertEquals(120, PERCENTAGE.resize(100, 20, 10));
    assertEquals(110, PERCENTAGE.resize(100, 5, 10));
    assertEquals(105, PERCENTAGE.resize(100, 5));
    assertEquals(100_000_005, PERCENTAGE.resize(100_000_000, .000005));
  }

  @Test(expected = IllegalArgumentException.class)
  public void percentage01() {
    PERCENTAGE.resize(10, 0, 2);
  }

  @Test
  public void add00() {
    assertEquals(15, ADD.resize(0, 15, 2));
    assertEquals(16, ADD.resize(1, 15, 15));
    assertEquals(17, ADD.resize(1, 15, 16));
  }

  @Test(expected = IllegalArgumentException.class)
  public void add01() {
    ADD.resize(10, 1.5, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void add02() {
    ADD.resize(10, 0, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void add03() {
    ADD.resize(10, 5, 0);
  }

  @Test
  public void getMinIncrease00() {
    assertEquals(5, getMinIncrease(10, 5, 10));
    assertEquals(15, getMinIncrease(10, 5, 20));
  }

  @Test(expected = BufferOverflowException.class)
  public void getMinIncrease01() {
    assertEquals(5, getMinIncrease(MAX_INT - 2, MAX_INT - 3, 10));
  }

}
