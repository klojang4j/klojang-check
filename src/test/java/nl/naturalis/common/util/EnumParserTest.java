package nl.naturalis.common.util;

import static java.time.DayOfWeek.MONDAY;
import static java.util.Calendar.FRIDAY;
import static nl.naturalis.common.util.EnumParser.DEFAULT_NORMALIZER;
import static nl.naturalis.common.util.EnumParser.ParseTarget.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import nl.naturalis.common.TypeConversionException;
import nl.naturalis.common.collection.DuplicateValueException;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.EnumSet;

public class EnumParserTest {

  private static enum TestEnum {
    DAY_ONE,
    DAY_TWO,
    _THIRD,
    _FOURTH_("^^^fourth^^^"),
    FIFTH_DAY_IN_A_ROW;
    private String s;

    private TestEnum() {
      this.s = name();
    }

    private TestEnum(String s) {
      this.s = s;
    }

    public String toString() {
      return s;
    }
  }

  @Test(expected = DuplicateValueException.class)
  @SuppressWarnings("unused")
  public void testBadNormalizer() {
    new EnumParser<>(TestEnum.class, s -> "Hi");
  }

  @Test
  public void testDefaultNormalizer() {
    assertEquals("abcd", DEFAULT_NORMALIZER.apply("ABCD"));
    assertEquals("abcd", DEFAULT_NORMALIZER.apply("abcd"));
    assertEquals("abcdefg", DEFAULT_NORMALIZER.apply("abcd EFG"));
    assertEquals("abcdefg", DEFAULT_NORMALIZER.apply("ab-cd ef_G"));
    assertEquals(
        "abcdefg",
        DEFAULT_NORMALIZER.apply("ab-c           d ef____-----------G"));
  }

  @Test
  public void parse01() {
    EnumParser<TestEnum> parser = new EnumParser<>(TestEnum.class);
    TestEnum e = parser.parse("day one");
    assertSame("01", TestEnum.DAY_ONE, e);
    e = parser.parse("DayTwo");
    assertSame("02", TestEnum.DAY_TWO, e);
    e = parser.parse("third");
    assertSame("03", TestEnum._THIRD, e);
    e = parser.parse(" fOurTh ");
    assertSame("04", TestEnum._FOURTH_, e);
    e = parser.parse("^^^fOurTh^^^");
    assertSame("05", TestEnum._FOURTH_, e);
    e = parser.parse("fifthDayInARow");
    assertSame("06", TestEnum.FIFTH_DAY_IN_A_ROW, e);
  }

  @Test(expected = TypeConversionException.class)
  public void parse02() {
    EnumParser<TestEnum> parser = new EnumParser<>(TestEnum.class);
    parser.parse("day*one");
  }

  @Test
  public void parse03() {
    EnumParser<DayOfWeek> parser = new EnumParser<>(DayOfWeek.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(IDENTITY));
    assertEquals(MONDAY, parser.parse(MONDAY));
  }

  @Test(expected = TypeConversionException.class)
  public void parse04() {
    EnumParser<DayOfWeek> parser = new EnumParser<>(DayOfWeek.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(IDENTITY));
    parser.parse("MONDAY");
  }

  @Test(expected = TypeConversionException.class)
  public void parse05() {
    EnumParser<DayOfWeek> parser = new EnumParser<>(DayOfWeek.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(IDENTITY));
    parser.parse(1);
  }

  @Test
  public void parse06() {
    EnumParser<TestEnum> parser = new EnumParser<>(TestEnum.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(TO_STRING));
    assertEquals(TestEnum._FOURTH_, parser.parse("^^^fourth^^^"));
  }

  @Test(expected = TypeConversionException.class)
  public void parse07() {
    EnumParser<TestEnum> parser = new EnumParser<>(TestEnum.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(TO_STRING));
    parser.parse("_FOURTH_");
  }

  @Test
  public void parse08() {
    EnumParser<DayOfWeek> parser = new EnumParser<>(DayOfWeek.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(ORDINAL));
    assertEquals(DayOfWeek.FRIDAY, parser.parse(4));
    assertEquals(DayOfWeek.FRIDAY, parser.parse((byte) 4));
    assertEquals(DayOfWeek.FRIDAY, parser.parse((short) 4));
    assertEquals(DayOfWeek.FRIDAY, parser.parse(4L));
  }

  @Test(expected = TypeConversionException.class)
  public void parse09() {
    EnumParser<DayOfWeek> parser = new EnumParser<>(DayOfWeek.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(ORDINAL));
    assertEquals(DayOfWeek.FRIDAY, parser.parse(-1));
  }

  @Test(expected = TypeConversionException.class)
  public void parse10() {
    EnumParser<DayOfWeek> parser = new EnumParser<>(DayOfWeek.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(ORDINAL));
    assertEquals(DayOfWeek.FRIDAY, parser.parse((byte) 255));
  }

  @Test
  public void parse11() {
    EnumParser<TestEnum> parser = new EnumParser<>(TestEnum.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(NAME));
    assertEquals(TestEnum._FOURTH_, parser.parse("_FOURTH_"));
  }

  @Test
  public void parse12() {
    EnumParser<TestEnum> parser = new EnumParser<>(TestEnum.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(NAME));
    assertEquals(TestEnum._FOURTH_, parser.parse("Fourth"));
  }

  @Test(expected = TypeConversionException.class)
  public void parse13() {
    EnumParser<TestEnum> parser = new EnumParser<>(TestEnum.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(NAME));
    parser.parse("^^^fourth^^^");
  }

  @Test(expected = TypeConversionException.class)
  public void parse14() {
    EnumParser<TestEnum> parser = new EnumParser<>(TestEnum.class,
        DEFAULT_NORMALIZER,
        EnumSet.of(NAME));
    parser.parse(null);
  }

}
