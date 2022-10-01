package nl.naturalis.common.path;

import org.junit.Test;

import java.util.Iterator;

import static nl.naturalis.common.ArrayMethods.pack;
import static org.junit.Assert.*;

public class PathTest {

  @Test
  public void path() {
    Path path = Path.from("identifications");
    assertEquals("01", 1, path.size());
    path = Path.from("identifications.0.scientificName.fullScientificName");
    assertEquals("02", 4, path.size());
    assertEquals("03", "identifications", path.segment(0));
    assertEquals("04", "0", path.segment(1));
    assertEquals("05", "scientificName", path.segment(2));
    assertEquals("06", "fullScientificName", path.segment(3));
  }

  @Test
  public void parse01() {
    Path path = Path.from("identifications.awk^.ward.scientificName");
    assertEquals("01", 3, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertEquals("03", "awk.ward", path.segment(1));
    assertEquals("04", "scientificName", path.segment(2));
  }

  @Test
  public void parse02() {
    Path path = Path.from("identifications.awk^^.ward.scientificName");
    assertEquals("01", 3, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertEquals("03", "awk^.ward", path.segment(1));
    assertEquals("04", "scientificName", path.segment(2));
  }

  @Test
  public void parse03() {
    Path path = Path.from("identifications.^^^..scientificName");
    assertEquals("01", 3, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertEquals("03", "^^.", path.segment(1));
    assertEquals("04", "scientificName", path.segment(2));
  }

  @Test
  public void parse04() {
    Path path = Path.from("identifications.awk^ward.scientificName");
    assertEquals("01", 3, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertEquals("03", "awk^ward", path.segment(1));
    assertEquals("04", "scientificName", path.segment(2));
  }

  @Test
  public void parse05() {
    Path path = Path.from("identifications.^awk^^^^ward^^.scientificName");
    assertEquals("01", 2, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertEquals("03", "^awk^^^^ward^.scientificName", path.segment(1));
  }

  @Test
  public void parse06() {
    Path path = Path.from("identifications.^0.scientificName");
    assertEquals("01", 3, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertNull("03", path.segment(1));
    assertEquals("04", "scientificName", path.segment(2));
  }

  @Test
  public void parse07() {
    Path path = Path.from("identifications.^^0.scientificName");
    assertEquals("01", 3, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertEquals("03", "^^0", path.segment(1));
    assertEquals("04", "scientificName", path.segment(2));
  }

  @Test
  public void parse08() {
    Path path = Path.from("identifications.^^^0.scientificName");
    assertEquals("01", 3, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertEquals("03", "^^^0", path.segment(1));
    assertEquals("04", "scientificName", path.segment(2));
  }

  @Test
  public void parse09() {
    Path path = Path.from("identifications.^^^0.^0");
    assertEquals("01", 3, path.size());
    assertEquals("02", "identifications", path.segment(0));
    assertEquals("03", "^^^0", path.segment(1));
    assertNull("04", path.segment(2));
  }

  @Test
  public void escape() {
    String segment = "identifications";
    assertTrue("01", segment == Path.escape(segment));
    assertEquals("02", "identifications^.", Path.escape("identifications."));
    assertEquals("03", "i^.dentifications", Path.escape("i.dentifications"));
    assertEquals("04", "^.identifications", Path.escape(".identifications"));
    assertEquals("05", "^.^identifications", Path.escape(".^identifications"));
    assertEquals("06", "^^.identifications", Path.escape("^.identifications"));
    assertEquals("07", "^0", Path.escape(null));
  }

  @Test
  public void getPurePath() {
    Path path = Path.from("identifications.0.scientificName.fullScientificName");
    assertEquals(
        "01",
        "identifications.scientificName.fullScientificName",
        path.getCanonicalPath().toString());
  }

  @Test
  public void append() {
    Path path = Path.from("identifications.0");
    assertEquals(
        "01",
        Path.from("identifications.0.scientificName"),
        path.append("scientificName"));
  }

  @Test
  public void shift() {
    Path path = Path.from("identifications.0.scientificName.fullScientificName");
    assertEquals("01",
        Path.from("0.scientificName.fullScientificName"),
        (path = path.shift()));
    assertEquals("02",
        Path.from("scientificName.fullScientificName"),
        (path = path.shift()));
    assertEquals("03", Path.from("fullScientificName"), (path = path.shift()));
    assertTrue("04", (path = path.shift()) == Path.empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void subpath01() {
    Path p = Path.from("identifications.0.scientificName");
    p.subPath(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void subpath02() {
    Path p = Path.from("identifications.0.scientificName");
    p.subPath(2, 4);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void subpath03() {
    Path p = Path.from("identifications.0.scientificName");
    p.subPath(-1, 5);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void subpath04() {
    Path p = Path.from("identifications.0.scientificName");
    p.subPath(3, 3);
  }

  @Test
  public void subpath05() {
    Path p = Path.from("identifications.0.scientificName");
    assertEquals("01", Path.from("scientificName"), p.subPath(2, 1));
    assertEquals("02", Path.from("0.scientificName"), p.subPath(1, 2));
    assertEquals("03",
        Path.from("identifications.0.scientificName"),
        p.subPath(0, 3));
    assertEquals("04", Path.from("0.scientificName"), p.subPath(1));
  }

  @Test
  public void subpath06() {
    Path p = Path.from("identifications.0.scientificName");
    assertEquals("01", Path.from("0.scientificName"), p.subPath(-2));
    assertEquals("02", Path.from("0"), p.subPath(-2, 1));
    assertEquals("03", Path.empty(), p.subPath(-2, 0));
  }

  @Test
  public void parent01() {
    assertEquals("01", null, Path.empty().parent());
    Path p = Path.from("identifications.0.scientificName");
    assertEquals("02", Path.from("identifications.0"), p.parent());
    assertEquals("03", Path.from("identifications"), p.parent().parent());
    assertEquals("04", Path.empty(), p.parent().parent().parent());
    assertEquals("05", null, p.parent().parent().parent().parent());
  }

  @Test
  public void equals00() {
    assertEquals(Path.from("a.b.c"), Path.from("a.b.c"));
    assertNotEquals(Path.from("a.b.c"), Path.from("a.b"));
    assertNotEquals(Path.from("a.b.c"), null);
    assertNotEquals(Path.from("a.b.c"), new Object());
  }

  @Test
  public void compareTo00() {
    assertEquals(0, Path.from("a.b.c").compareTo(Path.from("a.b.c")));
    assertEquals(1, Path.from("a.b.c").compareTo(Path.from("a.b")));
    assertEquals(-1, Path.from("a.b.c").compareTo(Path.from("a.b.d")));
  }

  @Test
  public void iterator00() {
    Iterator<String> iter = Path.from("a.b.c").iterator();
    assertTrue(iter.hasNext());
    assertEquals("a", iter.next());
    assertTrue(iter.hasNext());
    assertEquals("b", iter.next());
    assertTrue(iter.hasNext());
    assertEquals("c", iter.next());
    assertFalse(iter.hasNext());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void iterator01() {
    Path.empty().iterator().next();
  }

  @Test
  public void replace00() {
    assertEquals(Path.from("a.b.c").replace(1, "x"), Path.from("a.x.c"));
  }

  @Test
  public void copy00() {
    Path p0 = Path.from("a.b.c");
    Path p1 = Path.copyOf(p0);
    assertEquals(Path.from("a.b.c"), p1);
  }

  @Test
  public void toString00() {
    assertEquals("", Path.empty().toString());
  }

  @Test
  public void toString01() {
    assertEquals("", Path.from("").toString());
  }

  @Test
  public void toString02() {
    assertEquals("foo.", Path.from("foo.").toString());
  }

  @Test // test unescape - escape route
  public void toString03() {
    assertEquals("foo.^0", Path.from("foo.^0").toString());
  }

  @Test // test unescape - escape route
  public void toString04() {
    assertEquals("foo.^0.bar", Path.from("foo.^0.bar").toString());
  }

  @Test // test unescape - escape route
  public void toString05() {
    assertEquals("foo.^0.bar^", Path.from("foo.^0.bar^").toString());
  }

  @Test // test unescape - escape route
  public void toString06() {
    assertEquals("foo.^0.bar^..bozo", Path.from("foo.^0.bar^..bozo").toString());
  }

  @Test
  public void empty00() {
    assertEquals(0, Path.empty().size());
  }

  @Test
  public void of00() {
    Path p = Path.of("foo");
    assertEquals(1, p.size());
  }

  @Test
  public void of01() {
    Path p = Path.of("foo", null);
    assertEquals(2, p.size());
  }

  @Test
  public void of02() {
    Path p = Path.of("foo", null, "bar");
    assertEquals(3, p.size());
  }

  @Test
  public void of03() {
    Path p = Path.of("foo", null, "bar", null);
    assertEquals(4, p.size());
  }

  @Test
  public void of04() {
    Path p = Path.of("foo", null, "bar", null, "bozo");
    assertEquals(5, p.size());
  }

  @Test
  public void of05() {
    Path p = Path.of("foo", null, "bar", null, "bozo", null);
    assertEquals(6, p.size());
  }

  @Test
  public void of06() {
    Path p = Path.of("foo", null, "bar", null, "bozo", null, "bonkers");
    assertEquals(7, p.size());
  }

  @Test
  public void ofSegments() {
    Path p = Path.of(pack("foo", null, "bar", null, "bozo", null, "bonkers"));
    assertEquals(7, p.size());
  }

  @Test
  public void reverse00() {
    Path p = Path.of();
    assertSame(p, p.reverse());
    p = Path.from("a");
    assertSame(p, p.reverse());
    p = Path.of("a", "b");
    assertEquals("b.a", p.reverse().toString());
    p = Path.of("a", "b", "c");
    assertEquals(Path.of("c", "b", "a"), p.reverse());
    p = Path.of("a", "b", "c", "d");
    assertEquals("d.c.b.a", p.reverse().toString());
  }

}
