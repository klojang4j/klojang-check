package nl.naturalis.common.util;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import nl.naturalis.common.Result;
import org.junit.Test;
import nl.naturalis.common.util.MapBuilder.PathBlockedException;

import static org.junit.Assert.*;

public class MapBuilderTest {

  @Test
  public void set00() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "12 Revolutionary Rd.")
        .set("person.address.state", "CA")
        .set("person.firstName", "John")
        .set("person.lastName", "Smith")
        .set("person.born", LocalDate.of(1967, 4, 4));
    String expected =
        "{person={address={street=12 Revolutionary Rd., state=CA}, firstName=John, lastName=Smith,"
            + " born=1967-04-04}}";
    assertEquals(expected, mw.createMap().toString());
  }

  @Test // Are we OK with null values?
  public void set01() {
    MapBuilder mw = new MapBuilder();
    mw
        .set("person.address.street", "12 Revolutionary Rd.")
        .set("person.address.state", null)
        .set("person.firstName", "John")
        .set("person.lastName", null)
        .set("person.born", LocalDate.of(1967, 4, 4));
    String expected =
        "{person={address={street=12 Revolutionary Rd., state=null}, firstName=John, "
            + "lastName=null, born=1967-04-04}}";
    assertEquals(expected, mw.createMap().toString());
  }

  @Test(expected = PathBlockedException.class)
  public void set02() {
    MapBuilder mw = new MapBuilder();
    mw
        .set("person.address.street", "12 Revolutionary Rd.")
        .set("person.address.street.foo", "bar");
  }

  @Test(expected = PathBlockedException.class)
  public void set03() {
    MapBuilder mw = new MapBuilder();
    mw
        .set("person.address.street", null)
        .set("person.address.street", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void set04() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", new HashMap<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void set05() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", new MapBuilder());
  }

  @Test(expected = PathBlockedException.class)
  public void set06() {
    MapBuilder mw = new MapBuilder();
    mw
        .set("person.address", "foo")
        .set("person.address.street", "Sunset Blvd");
  }

  @Test // do we make the null -> _NULL_ -> null round trip?
  public void set07() {
    MapBuilder mw = new MapBuilder();
    mw
        .set("foo.bar.teapot", null)
        .set("foo.bar.fun", true)
        .set("foo.bar.number", 8);
    Map<String, Object> nested = new HashMap<>();
    nested.put("teapot", null);
    nested.put("fun", true);
    nested.put("number", 8);
    Map<String, Object> expected = Map.of("foo", Map.of("bar", nested));
    assertEquals(expected, mw.createMap());
  }

  @Test
  public void get00() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertEquals("foo", mw.poll("person.address.street").get());
    assertEquals(Map.of("street", "foo"), mw.poll("person.address").get());
    assertEquals(Map.of("address", Map.of("street", "foo")),
        mw.poll("person").get());
    assertEquals(Result.notAvailable(),
        mw.poll("person.address.street.teapot.coffee"));
    assertEquals(Result.notAvailable(), mw.poll("person.address.street.teapot"));
    assertEquals(Result.notAvailable(), mw.poll("person.address.teapot"));
    assertEquals(Result.notAvailable(), mw.poll("person.teapot"));
    assertEquals(Result.notAvailable(), mw.poll("teapot"));
  }

  @Test // do we make the null -> _NULL_ -> null round trip?
  public void get01() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", null);
    assertTrue(mw.isSet("person.address.street"));
    assertNull(mw.poll("person.address.street").get());
  }

  @Test
  public void in00() {
    MapBuilder mw = new MapBuilder();
    mw.in("person")
        .set("firstName", "John")
        .set("lastName", "Smith")
        .set("born", LocalDate.of(1967, 4, 4))
        .in("address")
        .set("street", "12 Revolutionary Rd.")
        .set("state", "CA");
    String expected =
        "{person={firstName=John, lastName=Smith, born=1967-04-04, address={street=12 "
            + "Revolutionary Rd., state=CA}}}";
    assertEquals(expected, mw.createMap().toString());
  }

  @Test(expected = PathBlockedException.class)
  public void in01() {
    MapBuilder mw = new MapBuilder();
    mw.set("foo.bar.bozo", "teapot");
    try {
      mw.in("foo.bar.bozo");
    } catch (PathBlockedException e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  @Test
  public void in02() {
    MapBuilder mw = new MapBuilder();
    mw.set("foo.bar.bozo", "teapot");
    mw.in("foo.bar").set("ping", "pong");
    Map<String, Object> expected = Map.of("foo",
        Map.of("bar", Map.of("bozo", "teapot", "ping", "pong")));
    assertEquals(expected, mw.createMap());
  }

  @Test
  public void in03() {
    MapBuilder mw = new MapBuilder();
    mw.set("foo.bar.bozo", "teapot");
    //@formatter:off
    mw
        .in("foo.bar")
        .set("ping", "pong")
        .set("boom", "bam")
        .in("physics")
        .set("big", "bang");
    //@formatter:on
    Map<String, Object> expected = Map.of("foo",
        Map.of("bar", Map.of(
            "bozo", "teapot",
            "ping", "pong",
            "boom", "bam",
            "physics", Map.of("big", "bang")
        )));
    assertEquals(expected, mw.createMap());
  }

  @Test(expected = PathBlockedException.class)
  public void in04() {
    MapBuilder mw = new MapBuilder();
    mw.set("foo.bar.bozo", "teapot");
    mw
        .in("foo.bar")
        .set("ping", "pong")
        .set("boom", "bam")
        .in("bozo");
  }

  @Test
  public void up00() {
    MapBuilder mw = new MapBuilder();
    mw.in("person.address")
        .set("street", "Sunset Blvd")
        .up("person")
        .set("firstName", "John");
    assertEquals("{person={address={street=Sunset Blvd}, firstName=John}}",
        mw.createMap().toString());
  }

  @Test
  public void up01() {
    MapBuilder mw = new MapBuilder();
    mw.in("person.address")
        .set("street", "Sunset Blvd")
        .up("person")
        .set("firstName", "John");
    Map expected = Map.of("person",
        Map.of("firstName", "John", "address", Map.of("street", "Sunset Blvd")));
    assertEquals(expected, mw.createMap());
  }

  @Test(expected = IllegalArgumentException.class)
  public void up02() {
    MapBuilder mw = new MapBuilder();
    try {
      mw.in("person.address")
          .set("street", "Sunset Blvd")
          .up("teapot");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalStateException.class)
  public void up03() {
    MapBuilder mw = new MapBuilder();
    try {
      mw.up("teapot");
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  @Test
  public void up04() {
    MapBuilder mw = new MapBuilder();
    mw.in("department.manager.address")
        .set("street", "Sunset Blvd")
        .up("manager")
        .up("department")
        .set("foo", "bar");
    Map<String, Object> expected = Map.of(
        "department",
        Map.of("foo",
            "bar",
            "manager",
            Map.of("address", Map.of("street", "Sunset Blvd")))
    );
    assertEquals(expected, mw.createMap());
  }

  @Test
  public void up05() {
    MapBuilder mw = new MapBuilder();
    mw.in("department.manager.address")
        .set("street", "Sunset Blvd")
        .up("manager")
        .up("department")
        .up(null)
        .set("foo", "bar");
    Map<String, Object> expected = Map.of(
        "foo", "bar",
        "department",
        Map.of("manager",
            Map.of("address", Map.of("street", "Sunset Blvd")))
    );
    assertEquals(expected, mw.createMap());
  }

  @Test
  public void up06() {
    MapBuilder mw = new MapBuilder();
    mw.in("department.manager.address")
        .set("street", "Sunset Blvd")
        .up("manager")
        .up("department")
        .up(null)
        .set("foo", "bar");
    Map<String, Object> expected = Map.of(
        "foo", "bar",
        "department",
        Map.of("manager",
            Map.of("address", Map.of("street", "Sunset Blvd")))
    );
    assertEquals(expected, mw.createMap());
  }

  @Test
  public void reset00() {
    MapBuilder mw = new MapBuilder();
    mw.in("person.address")
        .set("street", "Sunset Blvd")
        .reset()
        .set("firstName", "John");
    assertEquals("{person={address={street=Sunset Blvd}}, firstName=John}",
        mw.createMap().toString());
  }

  @Test(expected = IllegalStateException.class)
  public void reset01() {
    MapBuilder mw = new MapBuilder();
    try {
      mw.reset();
    } catch (IllegalStateException e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  @Test
  public void isSet00() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertTrue(mw.isSet("person.address.street.teapot"));
  }

  @Test
  public void isSet01() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertTrue(mw.isSet("person.address.street"));
  }

  @Test
  public void isSet02() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertTrue(mw.isSet("person.address"));
  }

  @Test
  public void isSet03() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertTrue(mw.isSet("person"));
  }

  @Test
  public void isSet04() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertFalse(mw.isSet("teapot"));
  }

  @Test
  public void isSet05() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertFalse(mw.isSet("person.teapot"));
  }

  @Test
  public void isSet06() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertFalse(mw.isSet("person.address.teapot"));
  }

  @Test
  public void isSet07() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertFalse(mw.isSet("person.teapot.address"));
  }

  @Test
  public void isSet08() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertFalse(mw.isSet("person.teapot.address.coffee"));
  }

  @Test
  public void isSet09() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "foo");
    assertFalse(mw.isSet("person.teapot.address.coffee.pot"));
  }

  @Test
  public void unset00() {
    MapBuilder mw = new MapBuilder();
    mw.set("person", "foo");
    assertTrue(mw.isSet("person"));
    mw.unset("person");
    assertFalse(mw.isSet("person"));
  }

  @Test
  public void unset01() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address", "foo");
    assertTrue(mw.isSet("person.address"));
    assertTrue(mw.isSet("person"));
    mw.unset("person.address");
    assertFalse(mw.isSet("person.address"));
    assertTrue(mw.isSet("person"));
    mw.unset("person");
    assertFalse(mw.isSet("person"));
  }

  @Test
  public void unset02() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "Sunset Blvd");
    mw.set("person.address.zipcode", "CA 12345");

    assertFalse(mw.isSet("person.address.country"));
    // can do it nevertheless:
    mw.unset("person.address.country");
    mw.unset("person.address.country.planet");

    assertTrue(mw.isSet("person.address.street"));
    assertTrue(mw.isSet("person.address.zipcode"));
    assertTrue(mw.isSet("person.address"));
    assertTrue(mw.isSet("person"));

    mw.unset("person");

    assertFalse(mw.isSet("person.address.street"));
    assertFalse(mw.isSet("person.address.zipcode"));
    assertFalse(mw.isSet("person.address"));
    assertFalse(mw.isSet("person"));
  }

  @Test
  public void unset03() {
    MapBuilder mw = new MapBuilder();
    mw.set("person.address.street", "Sunset Blvd");
    mw.set("person.address.zipcode", "CA 12345");

    assertFalse(mw.isSet("person.address.country"));
    // can do it nevertheless:
    mw.unset("person.address.country");
    mw.unset("person.address.country.planet");

    assertTrue(mw.isSet("person.address.street"));
    assertTrue(mw.isSet("person.address.zipcode"));
    assertTrue(mw.isSet("person.address"));
    assertTrue(mw.isSet("person"));

    mw.unset("person.address.street");

    assertFalse(mw.isSet("person.address.street"));
    assertTrue(mw.isSet("person.address.zipcode"));
    assertTrue(mw.isSet("person.address"));
    assertTrue(mw.isSet("person"));
  }

  @Test
  public void sourceMap00() {
    Map<String, Object> source = Map.of("foo",
        Map.of("teapot", "coffee"),
        "bar",
        true);
    MapBuilder mw = new MapBuilder(source);
    assertEquals(source, mw.createMap());
    mw.set("ping", 1).set("pong", false);
    Map<String, Object> expected = Map.of("foo",
        Map.of("teapot", "coffee"),
        "bar",
        true, "ping", 1, "pong", false);
    assertEquals(expected, mw.createMap());
  }

  @Test(expected = IllegalArgumentException.class)
  public void sourceMap01() {
    Map source = Map.of(new File("/foo"), "bar");
    try {
      MapBuilder mw = new MapBuilder(source);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  @Test // make null -> _NULL_ -> null round trip
  public void sourceMap02() {
    Map source = new HashMap();
    source.put("foo", null);
    MapBuilder mw = new MapBuilder(source);
    assertEquals(source, mw.createMap());
  }

  @Test(expected = IllegalArgumentException.class)
  public void badSegment00() {
    MapBuilder mw = new MapBuilder();
    try {
      mw.set("person.^0.street", "foo"); // ^0 is escape sequence for null
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  @Test
  public void jump00() {
    MapBuilder mw = new MapBuilder();
    mw
        .set("department.person.address.street", "Main St.")
        .set("department.person.address.state", "CA")
        .set("department.person.firstName", "John")
        .set("car.brand.name", "BMW")
        .set("person.born", LocalDate.of(1967, 4, 4));
    mw = mw.jump("department.person");
    assertEquals("department.person", mw.where());
    mw = mw.jump("car.brand");
    assertEquals("car.brand", mw.where());
    mw = mw.up("car");
    assertEquals("car", mw.where());
    mw = mw.up("");
    assertEquals("", mw.where());
  }

}
