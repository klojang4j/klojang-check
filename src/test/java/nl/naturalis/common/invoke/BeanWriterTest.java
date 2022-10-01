package nl.naturalis.common.invoke;

import nl.naturalis.common.function.ThrowingBiFunction;
import nl.naturalis.common.util.MapBuilder;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static nl.naturalis.common.invoke.IncludeExclude.EXCLUDE;
import static nl.naturalis.common.invoke.IncludeExclude.INCLUDE;
import static org.junit.Assert.*;

public class BeanWriterTest {

  @Test
  public void set00() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "firstName", "John");
    assertEquals("John", person.getFirstName());
  }

  @Test
  public void set01() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someNumber", 3);
    assertEquals(person.getSomeNumber().getClass(), Integer.class);
    assertEquals(3, person.getSomeNumber());
  }

  @Test
  public void set02() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someNumber", 3.5);
    assertEquals(person.getSomeNumber().getClass(), Double.class);
    assertEquals(3.5, person.getSomeNumber());
  }

  @Test
  public void set03() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    try {
      writer.write(person, "someShort", 3.5);
    } catch (IllegalAssignmentException e) {
      assertEquals(Person.class, e.getBeanClass());
      assertEquals(e.getPropertyName(), "someShort");
      assertEquals(e.getPropertyType(), short.class);
      assertEquals(e.getValue(), 3.5);
      return;
    }
    fail();
  }

  @Test(expected = IllegalAssignmentException.class)
  public void set04() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someShort", null);
  }

  @Test
  public void set05() throws Throwable {
    Person person = new Person();
    person.setLastName("Smith");
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "lastName", null);
    assertNull(person.getLastName());
  }

  @Test
  public void set06() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someInt", (byte) 125);
    assertEquals(125, person.getSomeInt());
  }

  @Test
  public void set07() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someNumber", (byte) 125);
    assertEquals((byte) 125, person.getSomeNumber());
  }

  @Test
  public void set08() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "id", (byte) 125);
    assertEquals(125, person.getId());
  }

  @Test
  public void set09() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "id", 'a');
    assertEquals(97, person.getId());
  }

  @Test
  // So, which casts/conversions exactly take place automatically?
  public void set10() throws Throwable {
    int i = (short) 3;
    // i = 6L; won't compile
    short s = (int) 3;
    // s = (int) Short.MAX_VALUE + 1; won't compile
    // s = 3L; won't compile
    s = 'a';
    byte b = (short) 3;
    // b = Short.MAX_VALUE; won't compile
    // b = 'ć'; won't compile: c-acute == 263
    float f = (byte) 4;
    f = Long.MAX_VALUE;
    long l = (int) 7;
    // l = 2.4F; won't compile
    char c = (short) 97;
    Short bigShort = (byte) 3;
    assertTrue(Short.class.isInstance((short) c));
    assertFalse(Short.class.isInstance(null)); // false but no NPE
  }

  @Test(expected = IllegalAssignmentException.class)
  public void set11() throws Throwable {
    // int i = 6L;
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "id", 6L);
  }

  @Test(expected = IllegalAssignmentException.class)
  public void set12() throws Throwable {
    byte b = (short) 3; // compiles!
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someByte", (short) 3);
    // So that interesting. While we can write:
    // byte b = (short) 3;
    // we can't do the same via method handles (in spite of
    // java.lang.invoke stating that all necessary conversions
    // will be done behind the scenes)!
  }

  @Test(expected = IllegalAssignmentException.class)
  public void set13() throws Throwable {
    char c = (short) 97; // compiles!
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someChar", (short) 97);
  }

  @Test(expected = IllegalAssignmentException.class)
  public void set14() throws Throwable {
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someChar", null);
  }

  @Test(expected = IllegalAssignmentException.class)
  public void set15() throws Throwable {
    // Double d = (byte) 4; won't compile
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someDoubleWrapper", (byte) 4);
  }

  @Test(expected = IllegalAssignmentException.class)
  public void set16() throws Throwable {
    Short s = (byte) 4; // compiles!
    // s = Byte.valueOf((byte)4); won't compile
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class);
    writer.write(person, "someShortWrapper", (byte) 4);
  }

  @Test
  public void set17() throws Throwable {
    ThrowingBiFunction<Setter, Object, Object, Throwable> tbf = (setter, value) -> {
      if (setter.getParamType() == String.class) {
        return String.valueOf(value);
      }
      return value;
    };
    Person person = new Person();
    BeanWriter writer = new BeanWriter(Person.class, tbf);
    writer.write(person, "firstName", new StringBuilder("Jack"));
    writer.write(person, "lastName", 42);
    assertEquals("Jack", person.getFirstName());
    assertEquals("42", person.getLastName());
  }

  @Test
  public void copy00() throws Throwable {
    Person person0 = new Person();
    person0.setId(100);
    person0.setFirstName("John");
    person0.setLastName("Smith");
    person0.setHobbies(null);
    person0.setLastModified(LocalDate.of(2022, 04, 03));
    person0.setSomeCharSequence("Hello World");

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(LocalDate.of(2021, 04, 03));
    person1.setSomeCharSequence("Hi There");
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "id",
        "firstName",
        "lastName",
        "hobbies",
        "lastModified",
        "someCharSequence");
    writer.copy(person0, person1);

    assertEquals(person0.getId(), person1.getId());
    assertEquals(person0.getFirstName(), person1.getFirstName());
    assertEquals(person0.getLastName(), person1.getLastName());
    assertEquals(person0.getHobbies(), person1.getHobbies());
    assertEquals(person0.getLastModified(), person1.getLastModified());
    assertEquals(person0.getSomeCharSequence(), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

  @Test
  public void copy01() throws Throwable {
    Person person0 = new Person();
    person0.setId(100);
    person0.setFirstName("John");
    person0.setLastName("Smith");
    person0.setHobbies(null);
    person0.setLastModified(LocalDate.of(2022, 04, 03));
    person0.setSomeCharSequence("Hello World");

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(LocalDate.of(2021, 04, 03));
    person1.setSomeCharSequence("Hi There");
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "firstName",
        "lastName",
        "hobbies",
        "lastModified",
        "someCharSequence");
    writer.copy(person0, person1);

    assertEquals(80, person1.getId()); // id not included
    assertEquals(person0.getFirstName(), person1.getFirstName());
    assertEquals(person0.getLastName(), person1.getLastName());
    assertEquals(person0.getHobbies(), person1.getHobbies());
    assertEquals(person0.getLastModified(), person1.getLastModified());
    assertEquals(person0.getSomeCharSequence(), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

  @Test
  public void copy02() throws Throwable {
    Person person0 = new Person();
    person0.setId(100);
    person0.setFirstName("John");
    person0.setLastName("Smith");
    person0.setHobbies(null);
    person0.setLastModified(LocalDate.of(2022, 04, 03));
    person0.setSomeCharSequence("Hello World");

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(LocalDate.of(2021, 04, 03));
    person1.setSomeCharSequence("Hi There");
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        EXCLUDE,
        "firstName",
        "lastName");
    writer.copy(person0, person1);

    assertEquals(person0.getId(), person1.getId()); // id not included
    assertEquals("Patrick", person1.getFirstName());
    assertEquals("Steward", person1.getLastName());
    assertEquals(person0.getHobbies(), person1.getHobbies());
    assertEquals(person0.getLastModified(), person1.getLastModified());
    assertEquals(person0.getSomeCharSequence(), person1.getSomeCharSequence());
    assertEquals('\0', person1.getSomeChar()); // Yes, that's EXCLUDE for you
  }

  @Test
  public void copyNonNull00() throws Throwable {
    Person person0 = new Person();
    person0.setId(100);
    person0.setFirstName("John");
    person0.setLastName(null);
    person0.setHobbies(null);
    person0.setLastModified(LocalDate.of(2022, 04, 03));
    person0.setSomeCharSequence("Hello World");

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(LocalDate.of(2021, 04, 03));
    person1.setSomeCharSequence("Hi There");
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "id",
        "firstName",
        "lastName",
        "hobbies",
        "lastModified",
        "someCharSequence");
    writer.copyNonNull(person0, person1);

    assertEquals(person0.getId(), person1.getId());
    assertEquals(person0.getFirstName(), person1.getFirstName());
    assertEquals("Steward", person1.getLastName());
    assertEquals(List.of("Tennis"), person1.getHobbies());
    assertEquals(person0.getLastModified(), person1.getLastModified());
    assertEquals(person0.getSomeCharSequence(), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

  @Test
  public void copyNonNull01() throws Throwable {
    Person person0 = new Person();
    person0.setId(100);
    person0.setFirstName("John");
    person0.setLastName(null);
    person0.setHobbies(null);
    person0.setLastModified(LocalDate.of(2022, 04, 03));
    person0.setSomeCharSequence("Hello World");

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(LocalDate.of(2021, 04, 03));
    person1.setSomeCharSequence("Hi There");
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "firstName",
        "lastName",
        "hobbies",
        "lastModified",
        "someCharSequence");
    writer.copyNonNull(person0, person1);

    assertEquals(80, person1.getId()); // id not included
    assertEquals(person0.getFirstName(), person1.getFirstName());
    assertEquals("Steward", person1.getLastName());
    assertEquals(List.of("Tennis"), person1.getHobbies());
    assertEquals(person0.getLastModified(), person1.getLastModified());
    assertEquals(person0.getSomeCharSequence(), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

  @Test
  public void enrich00() throws Throwable {
    Person person0 = new Person();
    person0.setId(100);
    person0.setFirstName("John");
    person0.setLastName(null);
    person0.setHobbies(null);
    person0.setLastModified(LocalDate.of(2022, 04, 03));
    person0.setSomeCharSequence("Hello World");

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(null);
    person1.setSomeCharSequence(null);
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "id",
        "firstName",
        "lastName",
        "hobbies",
        "lastModified",
        "someCharSequence");
    writer.enrich(person0, person1);

    assertEquals(80, person1.getId());
    assertEquals("Patrick", person1.getFirstName());
    assertEquals("Steward", person1.getLastName());
    assertEquals(List.of("Tennis"), person1.getHobbies());
    assertEquals(person0.getLastModified(), person1.getLastModified());
    assertEquals(person0.getSomeCharSequence(), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

  @Test
  public void enrich01() throws Throwable {
    Person person0 = new Person();
    person0.setId(100);
    person0.setFirstName("John");
    person0.setLastName(null);
    person0.setHobbies(null);
    person0.setLastModified(LocalDate.of(2022, 04, 03));
    person0.setSomeCharSequence("Hello World");

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(null);
    person1.setSomeCharSequence(null);
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "id",
        "firstName",
        "lastName",
        "hobbies",
        "someCharSequence");
    writer.enrich(person0, person1);

    assertEquals(80, person1.getId());
    assertEquals("Patrick", person1.getFirstName());
    assertEquals("Steward", person1.getLastName());
    assertEquals(List.of("Tennis"), person1.getHobbies());
    assertNull(person1.getLastModified()); // dateLastModified not included
    assertEquals(person0.getSomeCharSequence(), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

  @Test
  public void mapCopy00() throws Throwable {
    Map<String, Object> person0 = new MapBuilder().set("id", 100)
        .set("firstName", "John")
        .set("lastName", "Smith")
        .set("hobbies", null)
        .set("lastModified", LocalDate.of(2022, 04, 03))
        .set("someCharSequence", "Hello World")
        .createMap();

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(LocalDate.of(2021, 04, 03));
    person1.setSomeCharSequence("Hi There");
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "id",
        "firstName",
        "lastName",
        "hobbies",
        "lastModified",
        "someCharSequence");
    writer.copy(person0, person1);

    assertEquals(person0.get("id"), person1.getId());
    assertEquals(person0.get("firstName"), person1.getFirstName());
    assertEquals(person0.get("lastName"), person1.getLastName());
    assertEquals(person0.get("hobbies"), person1.getHobbies());
    assertEquals(person0.get("lastModified"), person1.getLastModified());
    assertEquals(person0.get("someCharSequence"), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

  @Test
  public void mapCopyNonNull00() throws Throwable {

    Map<String, Object> person0 = new MapBuilder().set("id", 100)
        .set("firstName", "John")
        .set("lastName", null)
        .set("hobbies", null)
        .set("lastModified", LocalDate.of(2022, 04, 03))
        .set("someCharSequence", "Hello World")
        .createMap();

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(LocalDate.of(2021, 04, 03));
    person1.setSomeCharSequence("Hi There");
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "id",
        "firstName",
        "lastName",
        "hobbies",
        "lastModified",
        "someCharSequence");
    writer.copyNonNull(person0, person1);

    assertEquals(person0.get("id"), person1.getId());
    assertEquals(person0.get("firstName"), person1.getFirstName());
    assertEquals("Steward", person1.getLastName());
    assertEquals(List.of("Tennis"), person1.getHobbies());
    assertEquals(person0.get("lastModified"), person1.getLastModified());
    assertEquals(person0.get("someCharSequence"), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

  @Test
  public void mapEnrich00() throws Throwable {

    Map<String, Object> person0 = new MapBuilder().set("id", 100)
        .set("firstName", "John")
        .set("lastName", null)
        .set("hobbies", null)
        .set("lastModified", LocalDate.of(2022, 04, 03))
        .set("someCharSequence", "Hello World")
        .createMap();

    Person person1 = new Person();
    person1.setId(80);
    person1.setFirstName("Patrick");
    person1.setLastName("Steward");
    person1.setHobbies(List.of("Tennis"));
    person1.setLastModified(null);
    person1.setSomeCharSequence(null);
    person1.setSomeChar('A');

    BeanWriter writer = new BeanWriter(Person.class,
        INCLUDE,
        "id",
        "firstName",
        "lastName",
        "hobbies",
        "lastModified",
        "someCharSequence");
    writer.enrich(person0, person1);

    assertEquals(80, person1.getId());
    assertEquals("Patrick", person1.getFirstName());
    assertEquals("Steward", person1.getLastName());
    assertEquals(List.of("Tennis"), person1.getHobbies());
    assertEquals(person0.get("lastModified"), person1.getLastModified());
    assertEquals(person0.get("someCharSequence"), person1.getSomeCharSequence());
    assertEquals('A', person1.getSomeChar());
  }

}
