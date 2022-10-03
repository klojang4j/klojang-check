package nl.naturalis.check;

import nl.naturalis.base.function.Relation;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static java.time.DayOfWeek.*;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonExceptions.IO;
import static nl.naturalis.check.CommonExceptions.STATE;
import static org.junit.Assert.*;
import static nl.naturalis.check.TestUtil.*;

public class MsgRelationTest {

  private static final Map<String, String> beatles = new LinkedHashMap<>();

  static {
    beatles.put("john", "lennon");
    beatles.put("paul", "mccartney");
    beatles.put("george", "harrison");
    beatles.put("guess who", "huh?");
  }

  @Test(expected = IllegalArgumentException.class)
  public void relation00() { // Just to cover check without parameter name
    Check.that(Float.valueOf(7.5F)).is(GT(), 16F);
  }

  @Test
  public void relation01() {
    Map<String, Object> map = new HashMap<>();
    map.put("Greeting", "HelloWorld");
    Check.that(map).is((x, y) -> x.containsKey(y), "Greeting");
    Check.that(map).is(Map::containsKey, "Greeting");
    Check.that(map).is(containingKey(), "Greeting");
    Check.that(map).is((Map<String, Object> x, String y) -> x.containsKey(y),
        "Greeting");
    Check.that(map)
        .is((Relation<Map<String, Object>, String>) ((x, y) -> x.containsKey(y)),
            "Greeting");
    Check.that(map).is((Relation<Map<String, Object>, String>) Map::containsKey,
        "Greeting");
  }

  @Test
  public void relation02() {
    Check.that(String.class).is(subtypeOf(), CharSequence.class);
    Check.that("foo").is(instanceOf(), CharSequence.class);
    Check.that(CharSequence.class).isNot(instanceOf(), String.class);
    Check.that(Set.of("1", "2", "3")).is(containing(), "2");
    Check.that(Set.of("1", "2", "3")).isNot(containing(), "4");
    Check.that((Integer) 2).is(in(), List.of(1, 2, 3));
    Check.that((Integer) 4).isNot(in(), List.of(1, 2, 3));
    Check.that(Set.of("1", "2", "3")).is(enclosing(), List.of("1", "2"));
    Check.that(Set.of("1", "4", "5")).isNot(enclosing(), List.of("1", "2"));
    Check.that(Set.of(MONDAY, TUESDAY, WEDNESDAY))
        .is(enclosedBy(), List.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY));
    Check.that(Set.of(MONDAY, TUESDAY, SATURDAY))
        .isNot(enclosedBy(), List.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY));
    Map<Integer, Integer> map = Map.of(1, 1, 2, 4, 3, 6, 4, 8, 5, 10);
    Check.that(map).is(containingKey(), 1);
    Check.that(map).isNot(containingKey(), 11);
    Check.that(map).is(containingValue(), 4);
    Check.that(map).isNot(containingValue(), 7);
    Check.that((Integer) 5).is(keyIn(), map);
    Check.that((Integer) 7).isNot(valueIn(), map);
    Check.that((Integer) 7).is(inArray(), pack(1, 7, 10));
    Check.that("Hello").is(EQ(), new String("Hello"));
    Check.that("Hello").isNot(sameAs(), new String("Hello"));
    Check.that("Hello").is(equalsIgnoreCase(), "HELLO");
    Check.that(null).is(nullOr(), Boolean.TRUE);
    Check.that(true).is(nullOr(), Boolean.TRUE);
    Check.that(7.23F).is(GT(), 2F);
    Check.that(7.230F).is(LTE(), 7.230F);
    Check.that((Short) (short) 17).is(LT(), (short) 31);
    Check.that((Short) (short) 17).is(GTE(), (short) 17);
    Check.that("ZZZ").is(GT(), "AAA");
    Check.that("hello").isNot(startsWith(), "foo");
    Check.that("hello").is(endsWith(), "lo");
    Check.that("hello").is(containingString(), "lo");
    Check.that("abc").is(substringOf(), "abcde");
    Check.that("abc").is(substringOf(), "abc");
    Check.that("abc").isNot(substringOf(), "ab");
  }

  @Test
  public void EQ00() {
    try {
      Check.that("foo").is(EQ(), 7);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("argument must equal 7 (was foo)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void EQ01() {
    try {
      Check.that("foo").isNot(EQ(), "foo");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("argument must not equal foo", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void GT00() {
    try {
      Check.that("aaa", "foo").is(GT(), "bbb");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo must be > bbb (was aaa)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void GT01() {
    try {
      Check.that(9.0).is(GT(), 9.5);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("argument must be > 9.5 (was 9.0)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void GTE00() {
    try {
      Check.that("aaa", "foo").is(GTE(), "bbb");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("foo must be >= bbb (was aaa)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void LT00() {
    try {
      Check.that("aaa", "zappa").isNot(LT(), "bbb");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("zappa must not be < bbb (was aaa)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void LT01() {
    try {
      Check.that(9.7, "zappa").is(LT(), 9.5);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("zappa must be < 9.5 (was 9.7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void LTE00() {
    try {
      Check.that(9.7F, "zorro").is(LTE(), 9.5F);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("zorro must be <= 9.5 (was 9.7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void LTE01() {
    try {
      Check.that(WEDNESDAY, "zorro").isNot(LTE(), FRIDAY);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("zorro must not be <= FRIDAY (was WEDNESDAY)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void sameAs00() {
    try {
      Check.that(9.7F, "siphon").is(sameAs(), 9.7D);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertTrue(e.getMessage().startsWith("siphon must be Double@"));
      assertTrue(e.getMessage().contains(" (was Float@"));
      assertTrue(e.getMessage().endsWith(")"));
      return;
    }
    fail();
  }

  @Test
  public void sameAs01() {
    try {
      Check.that(WEDNESDAY, "siphon").isNot(sameAs(), WEDNESDAY);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("siphon must not be WEDNESDAY", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void sameAs02() {
    try {
      Check.that(null, "siphon").is(sameAs(), String.class);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("siphon must be String.class (was null)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void sameAs03() {
    try {
      Check.that(WEDNESDAY, "siphon").is(sameAs(), null);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("siphon must be null (was WEDNESDAY)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nullOr00() {
    try {
      Check.that(9.7F, "xavier").is(nullOr(), 9.5F);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("xavier must be null or 9.5 (was 9.7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void nullOr01() {
    try {
      Check.that(null, "xavier").isNot(nullOr(), SUNDAY);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("xavier must not be null or SUNDAY (was null)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void instanceOf00() {
    try {
      Check.that(9.7F, "pipe").is(instanceOf(), String.class);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("pipe must be instance of String (was Float)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void instanceOf01() {
    try {
      Check.on(STATE, 9.7F, "pipe").isNot(instanceOf(), Float.class);
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
      assertEquals("pipe must not be instance of Float (was 9.7)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void supertype00() {
    try {
      Check.that(OutputStream.class, "trevor").is(supertypeOf(),
          OutputStream[].class);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("trevor must be supertype of OutputStream[] (was OutputStream)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void supertype01() {
    try {
      Check.that(OutputStream.class, "trevor").isNot(supertypeOf(),
          FileOutputStream.class);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "trevor must not be supertype of FileOutputStream (was OutputStream)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void subtypeOf00() {
    try {
      Check.that(OutputStream.class, "babbage").is(subtypeOf(),
          OutputStream[].class);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "babbage must be subtype of OutputStream[] (was OutputStream)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void subtypeOf01() {
    try {
      Check.that(OutputStream.class, "babbage").is(subtypeOf(), Comparable.class);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "babbage must be subtype of Comparable (was OutputStream)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void subtypeOf02() {
    try {
      Check.that(String.class, "babbage").isNot(subtypeOf(), CharSequence.class);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("babbage must not be subtype of CharSequence (was String)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void subtypeOf03() {
    try {
      Check.that(Float.class, "babbage").isNot(subtypeOf(), Comparable.class);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("babbage must not be subtype of Comparable (was Float)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void contains00() {
    try {
      List<String> names = List.of("john", "paul", "george", "guess who");
      Check.on(IO, names, "poseidon").is(containing(), "ringo");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      assertEquals("poseidon must contain ringo", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void contains01() {
    try {
      List<String> names = List.of("john", "paul", "george", "guess who");
      Check.on(IO, names, "poseidon").isNot(containing(), "paul");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      assertEquals("poseidon must not contain paul", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void hasKey00() {
    try {
      Map<String, String> map =
          Map.of(
              "john",
              "lennon",
              "paul",
              "mccartney",
              "george",
              "harrison",
              "guess who",
              "huh?");
      Check.on(STATE, map, "thor").is(containingKey(), "ringo");
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
      assertEquals("thor must contain key ringo", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void hasKey01() {
    try {
      Map<String, String> map =
          Map.of(
              "john",
              "lennon",
              "paul",
              "mccartney",
              "george",
              "harrison",
              "guess who",
              "huh?");
      Check.on(STATE, map, "thor").isNot(containingKey(), "john");
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
      assertEquals("thor must not contain key john", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void hasValue00() {
    try {
      Map<String, String> map =
          Map.of(
              "john",
              "lennon",
              "paul",
              "mccartney",
              "george",
              "harrison",
              "guess who",
              "huh?");
      Check.that(map, "morpheus").is(containingValue(), "star");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("morpheus must contain value star", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void hasValue01() {
    try {
      Map<String, String> map =
          Map.of(
              "john",
              "lennon",
              "paul",
              "mccartney",
              "george",
              "harrison",
              "guess who",
              "huh?");
      Check.that(map, "morpheus").isNot(containingValue(), "huh?");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("morpheus must not contain value huh?", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void in00() {
    try {
      List<String> names = List.of("john", "paul", "george", "guess who");
      Check.that("ringo", "tetrapod").is(in(), names);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "tetrapod must be element of ListN[4] of [john, paul, george, guess who] (was ringo)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void in01() {
    try {
      List<String> names = List.of("john", "paul", "george", "guess who");
      Check.that("paul", "tetrapod").isNot(in(), names);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "tetrapod must not be element of ListN[4] of [john, paul, george, guess who] (was paul)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void keyIn00() {
    try {
      Check.that("ringo", "flavius").is(keyIn(), beatles);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "flavius must be key in LinkedHashMap[4] of {john=lennon, paul=mccartney, george=harrison, guess who=huh?} (was ringo)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void keyIn01() {
    try {
      Check.that("john", "flavius").isNot(keyIn(), beatles);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "flavius must not be key in LinkedHashMap[4] of {john=lennon, paul=mccartney, george=harrison, guess who=huh?} (was john)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void valueIn00() {
    try {
      Check.that("star", "werner").is(valueIn(), beatles);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "werner must be value in LinkedHashMap[4] of {john=lennon, paul=mccartney, george=harrison, guess who=huh?} (was star)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void valueIn01() {
    try {
      Check.that("lennon", "werner").isNot(valueIn(), beatles);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "werner must not be value in LinkedHashMap[4] of {john=lennon, paul=mccartney, george=harrison, guess who=huh?} (was lennon)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void elementOf00() {
    try {
      Check.that("lennon", "tolstoy").is(inArray(),
          pack("mccartney", "harrisson", "star"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "tolstoy must be element of String[3] of [mccartney, harrisson, star] (was lennon)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void elementOf01() {
    try {
      Check.that("star", "tolstoy").isNot(inArray(),
          pack("mccartney", "harrisson", "star"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "tolstoy must not be element of String[3] of [mccartney, harrisson, star] (was star)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void supersetOf00() {
    try {
      Check.that(List.of("mccartney", "harrisson", "lennon"), "frodo")
          .is(enclosing(), List.of("mccartney", "harrisson", "star"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "frodo must be superset of ListN[3] of [mccartney, harrisson, star] "
              + "(was ListN[3] of [mccartney, harrisson, lennon])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void superset01() {
    try {
      Check.that(List.of("lennon", "mccartney", "harrisson", "star"), "frodo")
          .isNot(enclosing(), List.of("mccartney", "harrisson", "star"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "frodo must not be superset of ListN[3] of [mccartney, harrisson, star] "
              + "(was ListN[4] of [lennon, mccartney, harrisson, star])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void subsetOf00() {
    try {
      Check.that(List.of("mccartney", "harrisson", "lennon"), "kremlin")
          .is(enclosedBy(), List.of("mccartney", "harrisson", "star"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "kremlin must be subset of ListN[3] of [mccartney, harrisson, star] "
              + "(was ListN[3] of [mccartney, harrisson, lennon])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void subsetOf01() {
    try {
      Check.that(List.of("lennon", "mccartney", "harrisson", "star"), "kremlin")
          .isNot(enclosedBy(), List.of("lennon", "mccartney", "harrisson", "star"));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "kremlin must not be subset of ListN[4] of [lennon, mccartney, harrisson, star] "
              + "(was ListN[4] of [lennon, mccartney, harrisson, star])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void hasSubstring00() {
    try {
      Check.that("abcd", "BMW").is(containingString(), "qwe");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("BMW must contain qwe (was abcd)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void hasSubstring01() {
    try {
      Check.that("abcd", "BMW").isNot(containingString(), "abc");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("BMW must not contain abc (was abcd)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void substringOf01() {
    try {
      Check.that("xyz", "bandung").is(substringOf(), "abcde");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("bandung must be substring of abcde (was xyz)", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IOException.class)
  public void substringOf02() throws IOException {
    Check.that("xyz").is(substringOf(), "abcde", () -> new IOException());
  }

  @Test
  public void substringOf03() {
    try {
      Check.on(IO, "xyz").isNot(substringOf(), "xyz");
    } catch (IOException e) {
      System.out.println(e.getMessage());
      assertEquals("argument must not be substring of xyz (was xyz)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void substringOf04() {
    try {
      Check.that("xyz", "bandung").is(substringOf(), "");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("bandung must be substring of \"\" (was xyz)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void substringOf05() {
    try {
      Check.that("     ", "bandung").is(substringOf(), "abcd");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("bandung must be substring of abcd (was \"     \")",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void equalsIgnoreCase00() {
    try {
      Check.that("abc", "mordor").is(equalsIgnoreCase(), "XYZ");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("mordor must be equal (ignoring case) to XYZ (was abc)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void equalsIgnoreCase01() {
    try {
      Check.that("123", "mordor").isNot(equalsIgnoreCase(), "123");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("mordor must not be equal (ignoring case) to 123 (was 123)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void startsWith00() {
    try {
      Check.that("abc", "coco").is(startsWith(), "XYZ");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("coco must start with XYZ (was abc)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void startsWith01() {
    try {
      Check.that("abc", "coco").isNot(startsWith(), "a");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("coco must not start with a (was abc)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void endsWith00() {
    try {
      Check.that("Thus spoke Zarathustra", "pathos").is(endsWith(), "STRA");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("pathos must end with STRA (was Thus spoke Zarathustra)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void endsWit01() {
    try {
      Check.that("Thus spoke Zarathustra", "pathos").isNot(endsWith(), "stra");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("pathos must not end with stra (was Thus spoke Zarathustra)",
          e.getMessage());
      return;
    }
    fail();
  }

  //////////////////////////////////////////////////////////////////////////
  // TESTS WITH CUSTOM MESSAGE
  //////////////////////////////////////////////////////////////////////////

  @Test
  public void GTE01() {
    try {
      Check.that(9.0).is(GTE(), 9.5, "${arg} is not ${test} ${obj} (${0})", "sorry");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("9.0 is not GTE 9.5 (sorry)", e.getMessage());
      return;
    }
    fail();
  }

}
