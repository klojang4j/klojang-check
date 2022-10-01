package nl.naturalis.common;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MorphTest {

  public MorphTest() {}

  @Test
  public void test00() {
    assertTrue(true);
    assertFalse(false);
    assertEquals(1, 1);
  }

  @Test
  public void test01() {
    int i = Morph.convert(2F, int.class);
    assertEquals(2, i);
  }

  @Test
  public void test02() {
    Integer i = Morph.convert(2F, Integer.class);
    assertEquals(Integer.valueOf(2), i);
  }

  @Test
  public void test03() {
    int i = Morph.convert('2', int.class);
    assertEquals(2, i);
  }

  @Test
  public void test04() {
    int i = Morph.convert(new int[] {2}, int.class);
    assertEquals(2, i);
  }

  @Test
  public void test05() {
    int i = Morph.convert(new int[] {2, 3, 4}, int.class);
    assertEquals(2, i);
  }

  @Test
  public void test06() {
    int i = Morph.convert(new int[0], int.class);
    assertEquals(0, i);
  }
}
