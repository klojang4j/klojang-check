package nl.naturalis.common.x.invoke;

import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.invoke.WrongMethodTypeException;
import java.time.LocalDate;

import static nl.naturalis.common.ClassMethods.simpleClassName;
import static org.junit.Assert.assertEquals;

public class InvokeUtilsTest {

  @Test
  public void test000a() {
    int[] array = new int[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(int[].class);
    vh.set(array, 2, 5); // Works - obviously
    assertEquals(5, array[2]);
  }

  @Test
  public void test000b() {
    int[] array = new int[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(int[].class);
    vh.set(array, 2, (byte) 5); // Works
    assertEquals(5, array[2]);
  }

  @Test
  public void test000c() {
    int[] array = new int[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(int[].class);
    vh.set(array, 2, (short) 5); // Works
    assertEquals(5, array[2]);
  }

  @Test(expected = WrongMethodTypeException.class)
  public void test000d() {
    int[] array = new int[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(int[].class);
    vh.set(array, 2, 5L); // Doesn't work
  }

  @Test(expected = WrongMethodTypeException.class)
  public void test000e() {
    int[] array = new int[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(int[].class);
    vh.set(array, 2, 5F); // Doesn't work
  }

  @Test
  public void test000f() {
    int[] array = new int[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(int[].class);
    vh.set(array, 2, 'a');
    assertEquals(97, array[2]); // Works !!!
  }

  @Test
  public void test001a() {
    short[] array = new short[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(short[].class);
    vh.set(array, 2, (byte) 5);
    assertEquals(5, array[2]);
  }

  @Test
  public void test002a() {
    int[] array = new int[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(int[].class);
    vh.set(array, 2, Short.valueOf((short) 5)); // Works !!!
    assertEquals(5, array[2]);
  }

  @Test
  public void test003a() {
    float[] array = new float[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(float[].class);
    vh.set(array, 2, Short.valueOf((short) 5)); // Works !!!
    assertEquals(5F, array[2], 0F);
  }

  @Test
  public void test003b() {
    float[] array = new float[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(float[].class);
    vh.set(array, 2, 'a'); // Works !!!
    assertEquals(97F, array[2], 0F);
  }

  @Test
  public void test003c() {
    float[] array = new float[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(float[].class);
    vh.set(array, 2, Character.valueOf('a')); // Works !!!
    assertEquals(97F, array[2], 0F);
  }

  @Test
  public void test003d() {
    float[] array = new float[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(float[].class);
    vh.set(array, 2, Long.valueOf(5)); // Works !!!
    assertEquals(5F, array[2], 0F);
  }

  @Test(expected = WrongMethodTypeException.class)
  public void test003e() {
    float[] array = new float[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(float[].class);
    vh.set(array, 2, 5.1);
    assertEquals(5.1F, array[2], 0F);
  }

  /*
   * Interestingly (and bafflingly) with the 004 tests, you get different exceptions for
   * 004a and 004b, even though the method signature is VarHandle.set(Object... args)!
   * So the (javac) compiler hasn't autoboxed the arguments yet and the JVM sees different
   * byte code! Is this some special treatment for java.lang.invoke ???
   */

  @Test(expected = ClassCastException.class)
  public void test004a() {
    Integer[] array = new Integer[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(Integer[].class);
    vh.set(array, 2, Byte.valueOf((byte) 5));
    assertEquals((Integer) 5, array[2]);
  }

  @Test(expected = WrongMethodTypeException.class)
  public void test004b() {
    Integer[] array = new Integer[10];
    VarHandle vh = MethodHandles.arrayElementVarHandle(Integer[].class);
    vh.set(array, 2, (byte) 5);
    assertEquals((Integer) 5, array[2]);
  }

  @Test
  public void test005a() throws Throwable {
    Object[] arr = new LocalDate[5];
    assertEquals(5, MethodHandles.arrayLength(LocalDate[].class).invoke(arr));
    MethodHandle mh = MethodHandles.arrayElementSetter(LocalDate[].class);
    mh.invoke(arr, 2, LocalDate.of(2000, 4, 4));
    mh = MethodHandles.arrayElementGetter(LocalDate[].class);
    LocalDate ld = (LocalDate) mh.invoke(arr, 2);
    assertEquals(ld, LocalDate.of(2000, 4, 4));
  }

}
