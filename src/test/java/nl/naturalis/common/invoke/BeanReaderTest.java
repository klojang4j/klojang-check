package nl.naturalis.common.invoke;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static nl.naturalis.common.invoke.IncludeExclude.EXCLUDE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class BeanReaderTest {

  @Test
  public void test00() {
    Person p0 = new Person();
    p0.setFirstName("John");
    p0.setLastName("Smith");
    p0.setHobbies(List.of("Soccer", "Tennis"));
    p0.setLastModified(LocalDate.of(2022, 03, 07));

    BeanReader<Person> reader = new BeanReader<>(Person.class);

    assertSame(Person.class, reader.getBeanClass());

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

    BeanReader<Person> reader = new BeanReader<>(Person.class);

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

  @Test
  public void test02() {
    BeanReader<Person> reader = new BeanReader<>(Person.class,
        "firstName",
        "lastName");
    assertEquals(Set.of("firstName", "lastName"), reader.getReadableProperties());
  }

  @Test(expected = NoSuchPropertyException.class)
  public void test04() {
    BeanReader<Person> reader = new BeanReader<>(Person.class,
        "firstName",
        "lastName");
    reader.read(new Person(), "lastModified");
  }

  @Test(expected = NoSuchPropertyException.class)
  public void test05() {
    BeanReader<Person> reader = new BeanReader<>(Person.class,
        EXCLUDE,
        "firstName",
        "lastName");
    reader.read(new Person(), "firstName");
  }

  @Test
  public void test06() {
    LocalDate ld = LocalDate.of(1900, 10, 8);
    TestRecord tr = new TestRecord(7, "eight", ld);
    BeanReader reader = new BeanReader(TestRecord.class);
    assertEquals(7, (int) reader.read(tr, "foo"));
    assertEquals("eight", reader.read(tr, "bar"));
    assertEquals(ld, reader.read(tr, "bozo"));

  }

}
