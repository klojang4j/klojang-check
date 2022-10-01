package nl.naturalis.common.path;

import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static nl.naturalis.common.CollectionMethods.newHashMap;
import static nl.naturalis.common.path.ErrorCode.INDEX_OUT_OF_BOUNDS;
import static nl.naturalis.common.path.ErrorCode.*;
import static org.junit.Assert.*;

public class PathWalkerTest {

  @Test
  public void test01() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = List.of(Path.empty());
    assertEquals(shell, new PathWalker(paths).read(shell));
  }

  @Test
  public void test02() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("name");
    assertEquals("Shell", new PathWalker(paths).read(shell));
  }

  @Test
  public void test03() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("sales");
    assertEquals(new BigDecimal(Integer.MAX_VALUE),
        new PathWalker(paths).read(shell));
  }

  @Test
  public void test04() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("profit");
    assertEquals(Float.valueOf(100_000_000), new PathWalker(paths).read(shell));
  }

  @Test
  public void test05() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("quarterlySales");
    assertArrayEquals(shellQuarterlySales, new PathWalker(paths).read(shell));
  }

  @Test
  public void test06() throws MalformedURLException {
    PathWalker pw = new PathWalker(paths("quarterlySales.1"));
    Object val = pw.read(shell());
    assertTrue(Arrays.equals(new float[] {20, 21, 22, 23}, (float[]) val));
  }

  @Test
  public void test07() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("quarterlySales.10");
    assertNull(new PathWalker(paths).read(shell));
  }

  @Test
  public void test08() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("quarterlySales.10");
    try {
      new PathWalker(paths, false).read(shell);
    } catch (PathWalkerException e) {
      assertEquals(INDEX_OUT_OF_BOUNDS, e.getErrorCode());
      return;
    }
    fail();
  }

  @Test
  public void test09() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("quarterlySales.10.foo");
    assertNull(new PathWalker(paths, true).read(shell));
  }

  @Test
  public void test10() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("quarterlySales.0.3");
    PathWalker pw = new PathWalker(paths);
    float f = (float) pw.read(shell);
    assertEquals(13F, f, 0);
  }

  @Test
  public void test11() throws MalformedURLException {
    PathWalker pw = new PathWalker(paths("departments.1.reactiveBingoDates.0.0"));
    Object val = pw.read(shell());
    assertEquals(2020, (int) val);
  }

  @Test
  public void test12() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("departments.1.hipsterFriendly");
    assertEquals(true, (boolean) new PathWalker(paths).read(shell));
  }

  @Test
  public void test13() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("departments.0.employees.0.twitter");
    assertNull(new PathWalker(paths).read(shell));
  }

  @Test
  public void test14() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("departments.0.employees.0.extraInfo.https://nos^.nl");
    try {
      new PathWalker(paths, false).read(shell);
    } catch (PathWalkerException e) {
      assertEquals(NO_SUCH_KEY, e.getErrorCode());
      return;
    }
    fail();
  }

  @Test
  public void test15() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("departments.0.employees.0.extraInfo.https://nos^.nl");
    PathWalker pw = new PathWalker(paths, true, (p, s) -> {
      try {
        return new URL(p.segment(-1));
      } catch (MalformedURLException e) {
        throw new KeyDeserializationException();
      }
    });
    assertEquals("OkiDoki", pw.read(shell));
  }

  @Test
  public void test16() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("departments.0.employees.0.extraInfo."
        + Path.escape("https://nos.nl"));
    PathWalker pw = new PathWalker(paths, true, (p, s) -> {
      try {
        return new URL(p.segment(-1));
      } catch (MalformedURLException e) {
        throw new KeyDeserializationException();
      }
    });
    assertEquals("OkiDoki", pw.read(shell));
  }

  @Test
  public void test17() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("departments.0.employees.0.extraInfo.^0");
    PathWalker pw = new PathWalker(paths, true);
    assertEquals("corrupt entry", pw.read(shell));
  }

  @Test
  public void test18() throws MalformedURLException {
    Company shell = shell();
    List<Path> paths = paths("departments.0.employees.0.extraInfo.deep stuff.e=mc2");
    PathWalker pw = new PathWalker(paths, true);
    assertEquals("Einstein", pw.read(shell));
  }

  @Test
  public void write01() throws MalformedURLException {
    Company shell = shell();
    String newName = "Royal Dutch Oil Company";
    PathWalker pw = new PathWalker("name");
    pw.write(shell, newName);
    assertEquals(newName, pw.read(shell));
  }

  @Test
  public void write02() throws MalformedURLException {
    Company shell = shell();
    PathWalker pw = new PathWalker("name", "departments.0.name");
    pw.writeValues(shell, "Foo", "bar");
    assertEquals("01", "Foo", shell.getName());
    assertEquals("02", "bar", shell.getDepartments().get(0).getName());
  }

  @Test
  public void write03() throws MalformedURLException {
    Company shell = shell();
    PathWalker pw = new PathWalker("departments.0.employees.0.extraInfo.hobbies");
    pw.write(shell, List.of("Karaoke", "judo"));
    assertEquals("01",
        List.of("Karaoke", "judo"),
        shell.getDepartments().get(0).getEmployees().get(
            0).getExtraInfo().get("hobbies"));
  }

  @Test
  public void write04() throws MalformedURLException {
    Company shell = shell();
    PathWalker pw = new PathWalker("departments.0.employees.0.extraInfo.^0");
    pw.write(shell, List.of("Karaoke", "judo"));
    assertEquals("01",
        List.of("Karaoke", "judo"),
        shell.getDepartments().get(0).getEmployees().get(
            0).getExtraInfo().get(null));
  }

  private static List<Path> paths(String... strings) {
    return Arrays.stream(strings).map(Path::from).collect(Collectors.toList());
  }

  private static float[][] shellQuarterlySales =
      new float[][] {{10, 11, 12, 13},
          {20, 21, 22, 23},
          {30, 31, 32, 33},
          {40, 41, 42, 43}};

  private static Company shell() throws MalformedURLException {
    Company company = new Company();
    company.setName("Shell");
    company.setSales(new BigDecimal(Integer.MAX_VALUE));
    company.setProfit(100_000_000);
    company.setQuarterlySales(shellQuarterlySales);
    company.setDepartments(new ArrayList<>());
    Department hr = new Department();
    company.getDepartments().add(hr);
    hr.setName("H&R");
    hr.setAddress(new Address("Koeienstraat", 5, "1111AA", "Rotterdam"));
    hr.setTelNos(new String[] {"040-123456"});
    hr.setEmployees(new ArrayList<>());
    Employee piet = new Employee();
    piet.setId(1);
    piet.setFirstName("Piet");
    piet.setLastName("Pietersen");
    piet.setFacebook(new URL("https://facebook.com/piet"));
    piet.setTwitter(null);
    piet.setBirthDate(new int[] {1972, 1, 1});
    piet.setExtraInfo(newHashMap(10,
        Object.class,
        Object.class,
        "hobbies",
        "paardrijden",
        null,
        "corrupt entry",
        new URL("https://nos.nl"),
        "OkiDoki",
        "deep stuff",
        newHashMap(10,
            String.class,
            String.class,
            "e=mc2",
            "Einstein",
            "cogito",
            "Descartes"),
        "numberOfPets",
        2));
    hr.getEmployees().add(piet);
    Employee jan = new Employee();
    jan.setId(1);
    jan.setFirstName("Jan");
    jan.setLastName("Jansen");
    jan.setFacebook(new URL("https://facebook.com/jan"));
    jan.setTwitter(new URL("https://twitter.com/@jan"));
    jan.setBirthDate(new int[] {1972, 2, 2});
    jan.setExtraInfo(newHashMap(10,
        Object.class,
        Object.class,
        "hobbies",
        "null",
        "allergies",
        "gluten",
        4,
        "numberOfChildren",
        "married",
        true,
        "numberOfPets",
        0));
    hr.getEmployees().add(jan);
    hr.setManager(jan);
    DevOps devops = new DevOps();
    company.getDepartments().add(devops);
    devops.setName("DevOps");
    devops.setAddress(null);
    devops.setReactiveBingoDates(new int[][] {{2020, 9, 3}, {2021, 2, 7}});
    devops.setHipsterFriendly(true);
    devops.setTelNos(new String[] {null, "035-123456"});
    return company;
  }

  @Test
  public void readValues00() {
    PathWalker pw = new PathWalker(Path.from("a"), Path.from("b"), Path.from("c"));
    Map<String, Integer> map = Map.of("a", 100, "b", 200, "c", 300);
    Object[] vals = pw.readValues(map);
    assertEquals(3, vals.length);
    assertEquals(100, vals[0]);
    assertEquals(200, vals[1]);
    assertEquals(300, vals[2]);
  }

  @Test
  public void readValues01() {
    PathWalker pw = new PathWalker(Path.from("a"), Path.from("b"), Path.from("c"));
    Map<String, Integer> map = Map.of("a", 100, "b", 200, "c", 300);
    Object[] vals = new Object[4];
    pw.readValues(map, vals);
    assertEquals(100, vals[0]);
    assertEquals(200, vals[1]);
    assertEquals(300, vals[2]);
    assertNull(vals[3]);
  }

  @Test
  public void readValues02() {
    PathWalker pw = new PathWalker(Path.from("a"), Path.from("b"), Path.from("c"));
    Map<String, Integer> mapIn = Map.of("a", 100, "b", 200, "c", 300);
    Map<Path, Object> mapOut = new HashMap();
    pw.readValues(mapIn, mapOut);
    assertEquals(3, mapOut.size());
    assertEquals(100, mapOut.get(Path.from("a")));
    assertEquals(200, mapOut.get(Path.from("b")));
    assertEquals(300, mapOut.get(Path.from("c")));
  }

}
