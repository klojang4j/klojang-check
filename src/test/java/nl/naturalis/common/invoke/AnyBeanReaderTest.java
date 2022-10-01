package nl.naturalis.common.invoke;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class AnyBeanReaderTest {

  @Test
  public void read00() {
    Person p0 = new Person();
    p0.setFirstName("John");
    p0.setLastName("Smith");
    p0.setHobbies(List.of("Soccer", "Tennis"));
    p0.setLastModified(LocalDate.of(2022, 03, 07));

    AnyBeanReader reader = new AnyBeanReader();

    assertEquals(p0.getFirstName(), reader.read(p0, "firstName"));
    assertEquals(p0.getLastName(), reader.read(p0, "lastName"));
    assertEquals(p0.getHobbies(), reader.read(p0, "hobbies"));
    assertEquals(p0.getLastModified(), reader.read(p0, "lastModified"));
  }

  @Test
  public void read01() {

    Person p0 = new Person();
    p0.setFirstName("John");
    p0.setLastName("Smith");
    p0.setHobbies(List.of("Soccer", "Tennis"));
    p0.setLastModified(LocalDate.of(2022, 3, 7));

    Person p1 = new Person();
    p1.setFirstName("Mary");
    p1.setLastName("Jones");
    p1.setHobbies(List.of("Reading"));
    p1.setLastModified(LocalDate.of(2021, 9, 9));

    Random random0 = new Random();
    Random random1 = new Random();

    AnyBeanReader reader = new AnyBeanReader();

    for (int i = 0; i < 100; ++i) {
      Person p = random0.nextBoolean() ? p0 : p1;
      switch (random1.nextInt(0, 4)) {
        case 0 -> assertEquals(p.getFirstName(), reader.read(p, "firstName"));
        case 1 -> assertEquals(p.getLastName(), reader.read(p, "lastName"));
        case 2 -> assertEquals(p.getHobbies(), reader.read(p, "hobbies"));
        default -> assertEquals(p.getLastModified(), reader.read(p, "lastModified"));
      }
    }
  }

  @Test(expected = NoSuchPropertyException.class)
  public void read02() {
    AnyBeanReader reader = new AnyBeanReader();
    reader.read(new Person(), "this_is_not_a_property");
  }

  @Test
  public void read03() {
    LocalDate ld = LocalDate.of(1900, 10, 8);
    TestRecord tr = new TestRecord(7, "eight", ld);
    AnyBeanReader reader = new AnyBeanReader();
    assertEquals(7, (int) reader.read(tr, "foo"));
    assertEquals("eight", reader.read(tr, "bar"));
    assertEquals(ld, reader.read(tr, "bozo"));
  }

  @Test
  public void read04() {
    LocalDate ld = LocalDate.of(1900, 10, 8);
    TestRecord tr = new TestRecord(7, "eight", ld);
    AnyBeanReader reader = new AnyBeanReader(true);
    assertEquals(7, (int) reader.read(tr, "foo"));
    assertEquals("eight", reader.read(tr, "bar"));
    assertEquals(ld, reader.read(tr, "bozo"));
  }

}
