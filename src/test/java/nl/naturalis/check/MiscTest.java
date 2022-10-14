package nl.naturalis.check;

import org.junit.Test;

import java.io.OutputStream;
import java.util.*;

import static nl.naturalis.check.Misc.toShortString;
import static org.junit.Assert.assertEquals;

public class MiscTest {

  @Test
  public void className00() {
    assertEquals("java.lang.String", Misc.className(String.class));
    assertEquals("java.lang.String[]", Misc.className(String[].class));
    assertEquals("java.lang.String[][]", Misc.className(String[][].class));
    assertEquals("float[][][]", Misc.className(float[][][].class));

  }

  @Test
  public void describe00() {
    assertEquals("null", Misc.describe(null));
    assertEquals("String.class", Misc.describe(String.class));
    assertEquals("String[].class", Misc.describe(String[].class));
    assertEquals("ArrayList[3]", Misc.describe(new ArrayList(List.of(1, 2, 3))));
    assertEquals("HashMap[3]",
        Misc.describe(new HashMap(Map.of(1, 2, 3D, 4D, 5, 6))));
    assertEquals("int[3]", Misc.describe(new int[] {1, 2, 3}));
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
    Double[] array = new Double[0];
    assertEquals("[]", toShortString(array, 5, 8, 4));
    array = new Double[] {1D};
    assertEquals("[1.0]", toShortString(array, 10, 8, 4));
    array = new Double[] {1D, 2D};
    assertEquals("[1.0, 2.0]", toShortString(array, 10, 8, 4));
    array = new Double[] {1D, 2D, 3D};
    assertEquals("[1.0, 2...", toShortString(array, 10, 8, 4));
    assertEquals("[1.0 (+2)]", toShortString(array, 20, 1, 4));
    assertEquals("[ (+3)]", toShortString(array, 20, 0, 4));
    array = new Double[] {1D, 2D, 3D, 4D, 5D};
    assertEquals("[1....", toShortString(array, 6, 8, 4));
    assertEquals("[1.0, 2...", toShortString(array, 10, 2, 4));
    assertEquals("[1.0, 2.0 ...", toShortString(array, 13, 2, 4));
  }

  @Test
  public void toShortString07() {
    List<Integer> list = List.of();
    assertEquals("[]", toShortString(list, 100, 20, 10));
    list = List.of(1);
    assertEquals("[1]", toShortString(list, 100, 20, 10));
    list = List.of(1, 2);
    assertEquals("[1, 2]", toShortString(list, 100, 20, 10));
    list = List.of(1, 2, 3);
    assertEquals("[1, 2, 3]", toShortString(list, 100, 20, 10));
    list = List.of(1, 2, 3, 4, 5, 6);
    assertEquals("[1, 2, 3, 4, 5, 6]", toShortString(list, 100, 20, 10));
    assertEquals("[1, 2 (+4)]", toShortString(list, 100, 2, 10));
    assertEquals("[1, 2...", toShortString(list, 8, 20, 10));
  }

  @Test
  public void toShortString08() {
    Map<Integer, String> map = Map.of();
    assertEquals("{}", toShortString(map, 100, 20, 10));
    map = Map.of(1, "a");
    assertEquals("{1=a}", toShortString(map, 100, 20, 10));
    map = mapOf(1, "a", 2, "b");
    assertEquals("{1=a, 2=b}", toShortString(map, 100, 20, 10));
    map = mapOf(1, "a", 2, "b", 3, "c", 4, "d", 5, "e");
    assertEquals("{1=a, 2=b, 3=c, 4=d, 5=e}", toShortString(map, 100, 20, 10));
    assertEquals("{1=a, 2=b, 3=c, 4=d (+1)}", toShortString(map, 100, 4, 10));
    assertEquals("{1=a (+4)}", toShortString(map, 100, 1, 10));
    assertEquals("{1=a, 2=b, 3...", toShortString(map, 15, 20, 10));
    assertEquals("{1=a, 2=b (...", toShortString(map, 14, 2, 10));
  }

  private static Map mapOf(Object... objs) {
    Map m = new LinkedHashMap();
    for (int i = 0; i < objs.length - 1; i += 2) {
      m.put(objs[i], objs[i + 1]);
    }
    return m;
  }

}
