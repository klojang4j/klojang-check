package nl.naturalis.check;

public class TestUtil {

  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  public static final String[] EMPTY_STRING_ARRAY = new String[0];

  public static <T> T[] pack(T... objs) {
    return objs;
  }

  public static int[] ints(int... ints) {
    return ints;
  }

  public static float[] floats(float... floats) {
    return floats;
  }

}
