package nl.naturalis.common;

import nl.naturalis.common.collection.IntList;
import org.junit.Test;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.*;
import static nl.naturalis.common.ArrayMethods.*;
import static nl.naturalis.common.MorphToArray.morph;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MorphToArrayTest {

  @Test
  public void arrayToArray00() {
    Integer[] array = morph(new short[] {(short) 1, (short) 2, (short) 3},
        Integer[].class);
    assertArrayEquals(pack(1, 2, 3), array);
  }

  @Test
  public void arrayToArray01() {
    int[] array = morph(new short[] {(short) 1, (short) 2, (short) 3},
        int[].class);
    assertArrayEquals(ints(1, 2, 3), array);
  }

  @Test
  public void arrayToArray02() {
    BigDecimal[] array = morph(new short[] {(short) 1, (short) 0, (short) 10},
        BigDecimal[].class);
    assertArrayEquals(pack(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN), array);
  }

  @Test
  public void arrayToArray03() {
    DayOfWeek[] array = morph(new short[] {(short) 0, (short) 1, (short) 3},
        DayOfWeek[].class);
    assertArrayEquals(pack(MONDAY, TUESDAY, THURSDAY), array);
  }

  @Test
  public void arrayToArray04() {
    DayOfWeek[] array = morph(new String[] {"MONDAY", "TUESDAY", "THURSDAY"},
        DayOfWeek[].class);
    assertArrayEquals(pack(MONDAY, TUESDAY, THURSDAY), array);
  }

  @Test
  public void collectionToArray00() {
    DayOfWeek[] array = morph(List.of((short) 0, (short) 1, (short) 3),
        DayOfWeek[].class);
    assertArrayEquals(pack(MONDAY, TUESDAY, THURSDAY), array);
  }

  @Test
  public void collectionToArray01() {
    DayOfWeek[] array = morph(List.of("MONDAY", "TUESDAY", "THURSDAY"),
        DayOfWeek[].class);
    assertArrayEquals(pack(MONDAY, TUESDAY, THURSDAY), array);
  }

  @Test
  public void collectionToArray02() {
    OutputStream[] array = morph(List.of(), OutputStream[].class);
    assertArrayEquals(new OutputStream[0], array);
  }

  @Test
  public void intListToArray00() {
    DayOfWeek[] array = morph(IntList.of(0, 1, 3), DayOfWeek[].class);
    assertArrayEquals(pack(MONDAY, TUESDAY, THURSDAY), array);
  }

  @Test
  public void intListToArray01() {
    int[] array = morph(IntList.of(0, 7, 9), int[].class);
    assertArrayEquals(ints(0, 7, 9), array);
  }

  @Test
  public void intListToArray02() {
    Integer[] array = morph(IntList.of(0, 7, 9), Integer[].class);
    assertArrayEquals(pack(0, 7, 9), array);
  }

  @Test
  public void stringToArray00() {
    char[] array = morph("foo", char[].class);
    assertArrayEquals(chars('f', 'o', 'o'), array);
  }

  @Test
  public void stringToArray01() {
    DayOfWeek[] array = morph("MONDAY", DayOfWeek[].class);
    assertArrayEquals(pack(MONDAY), array);
  }

  @Test
  public void objToArray01() {
    DayOfWeek[] array = morph(MONDAY, DayOfWeek[].class);
    assertArrayEquals(pack(MONDAY), array);
  }

}
