package nl.naturalis.check;

import org.junit.Test;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.naturalis.check.Misc.toShortString;
import static org.junit.Assert.assertEquals;

public class MiscTest {

  @Test
  public void describe00() {
    assertEquals("null", Misc.describe(null));
    assertEquals("String.class", Misc.describe(String.class));
    assertEquals("String[].class", Misc.describe(String[].class));
    assertEquals("ArrayList[3]", Misc.describe(new ArrayList(List.of(1, 2, 3))));
    assertEquals("HashMap[3]",
        Misc.describe(new HashMap(Map.of(1, 2, 3D, 4D, 5, 6))));
    assertEquals("int[3]", Misc.describe(new int[] {1, 2, 3}));
    assertEquals("int", Misc.describe(7));
    assertEquals("Float", Misc.describe(7F));
  }

  @Test
  public void toShortString00() {
    assertEquals("null", toShortString(null, 10));
    assertEquals("null", toShortString(null, 1));
  }

  @Test
  public void toShortString02() {
    assertEquals("OutputStream", toShortString(OutputStream.class, 15));
    assertEquals("Outpu...", toShortString(OutputStream.class, 8));
    assertEquals("OutputStream[][]", toShortString(OutputStream[][].class, 20));
    assertEquals("O...", toShortString(OutputStream[][].class, 3));
  }

  @Test
  public void toShortString03() {
    assertEquals("hello", toShortString("hello", 10));
    assertEquals("hello", toShortString("hello", 5));
    assertEquals("h...", toShortString("hello", 4));
    assertEquals("h...", toShortString("hello", 3));
    assertEquals("h...", toShortString("hello", 2));
    assertEquals("el...", toShortString("elephant", 5));
  }

  @Test
  public void toShortString04() {
    assertEquals("[]", toShortString(new int[0], 5, 8, 4));
    assertEquals("[1]", toShortString(new int[] {1}, 10, 8, 4));
    assertEquals("[1, 2]", toShortString(new int[] {1, 2}, 10, 8, 4));
    assertEquals("[1, 2, 3]", toShortString(new int[] {1, 2, 3}, 10, 8, 4));
    assertEquals("[1 (+2)]", toShortString(new int[] {1, 2, 3}, 20, 1, 4));
    assertEquals("[ (+3)]", toShortString(new int[] {1, 2, 3}, 20, 0, 4));
    assertEquals("[1,...",
        toShortString(new int[] {1, 2, 3, 4, 5}, 6, 8, 4));
    assertEquals("[1, 2 (...",
        toShortString(new int[] {1, 2, 3, 4, 5}, 10, 2, 4));
    assertEquals("[1, 2 (+3)]",
        toShortString(new int[] {1, 2, 3, 4, 5}, 13, 2, 4));
  }

  @Test
  public void toShortString05() {
    assertEquals("[]", toShortString(new double[0], 5, 8, 4));
    assertEquals("[1.0]", toShortString(new double[] {1}, 10, 8, 4));
    assertEquals("[1.0, 2.0]", toShortString(new double[] {1, 2}, 10, 8, 4));
    assertEquals("[1.0, 2...", toShortString(new double[] {1, 2, 3}, 10, 8, 4));
    assertEquals("[1.0 (+2)]", toShortString(new double[] {1, 2, 3}, 20, 1, 4));
    assertEquals("[ (+3)]", toShortString(new double[] {1, 2, 3}, 20, 0, 4));
    assertEquals("[1....",
        toShortString(new double[] {1, 2, 3D, 4D, 5}, 6, 8, 4));
    assertEquals("[1.0, 2...",
        toShortString(new double[] {1, 2, 3D, 4D, 5}, 10, 2, 4));
    assertEquals("[1.0, 2.0 ...",
        toShortString(new double[] {1, 2, 3D, 4D, 5}, 13, 2, 4));
  }

  @Test
  public void toShortString06() {
    assertEquals("[]", toShortString(new double[0], 5, 8, 4));
    assertEquals("[1.0]", toShortString(new double[] {1}, 10, 8, 4));
    assertEquals("[1.0, 2.0]", toShortString(new double[] {1, 2}, 10, 8, 4));
    assertEquals("[1.0, 2...", toShortString(new double[] {1, 2, 3}, 10, 8, 4));
    assertEquals("[1.0 (+2)]", toShortString(new double[] {1, 2, 3}, 20, 1, 4));
    assertEquals("[ (+3)]", toShortString(new double[] {1, 2, 3}, 20, 0, 4));
    assertEquals("[1....",
        toShortString(new double[] {1, 2, 3D, 4D, 5}, 6, 8, 4));
    assertEquals("[1.0, 2...",
        toShortString(new double[] {1, 2, 3D, 4D, 5}, 10, 2, 4));
    assertEquals("[1.0, 2.0 ...",
        toShortString(new double[] {1, 2, 3D, 4D, 5}, 13, 2, 4));
    assertEquals("[]", toShortString(new Double[0], 5, 8, 4));
    assertEquals("[1.0]",
        toShortString(new Double[] {1D}, 10, 8, 4));
    assertEquals("[1.0, 2.0]",
        toShortString(new Double[] {1D, 2D}, 10, 8, 4));
    assertEquals("[1.0, 2...",
        toShortString(new Double[] {1D, 2D, 3D}, 10, 8, 4));
    assertEquals("[ (+3)]",
        toShortString(new Double[] {1D, 2D, 3D}, 20, 0, 4));
    assertEquals("[ (+3)]",
        toShortString(new Double[] {1D, 2D, 3D}, 20, 0, 4));
    assertEquals("[1....",
        toShortString(new Double[] {1D, 2D, 3D, 4D, 5D}, 6, 8, 4));
    assertEquals("[1.0, 2...",
        toShortString(new Double[] {1D, 2D, 3D, 4D, 5D}, 10, 2, 4));
    assertEquals("[1.0, 2.0 ...",
        toShortString(new Double[] {1D, 2D, 3D, 4D, 5D}, 13, 2, 4));

  }

}
