package nl.naturalis.common;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class MorphToEnumTest {

  @Test
  public void test00() {
    assertEquals(Month.FEBRUARY, MorphToEnum.morph('1', Month.class));
    assertEquals(Month.DECEMBER, MorphToEnum.morph(11, Month.class));
    assertEquals(Month.MARCH, MorphToEnum.morph("march", Month.class));
    assertEquals(Month.JANUARY, MorphToEnum.morph("0", Month.class));
    assertEquals(Month.APRIL, MorphToEnum.morph((short) 3, Month.class));
    assertEquals(DayOfWeek.SATURDAY, MorphToEnum.morph(5L, DayOfWeek.class));
  }

}
