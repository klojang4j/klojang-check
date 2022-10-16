package org.klojang.check;

import org.junit.Test;
import org.klojang.check.Check;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.CommonProperties.*;

public class HasObjObjTest {

  private record Person(String firstName, LocalDate birtDate) {}

  @Test
  public void vanilla00() throws IOException {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    Check.that(p, "person").has(Person::firstName, notNull());
    Check.that(p, "person").has(Person::firstName, "firstName", notNull());
    Check.that(p, "person").has(Person::firstName,
        notNull(),
        "Any message you like bro");
    Check.that(p, "person").has(Person::firstName,
        notNull(),
        () -> new IOException());
    Check.that(p, "person").has(type(), EQ(), Person.class);
    Check.that(p, "person").has(type(), "class", EQ(), Person.class);
    Check.that(p, "person").has(type(),
        EQ(),
        Person.class,
        "Any message you like bro");
    Check.that(p, "person").has(type(), EQ(), Person.class, () -> new IOException());
    Check.that(p, "person").has(Person::firstName, EQ(), "john");
    Check.that(p, "person").has(Person::birtDate, LT(), LocalDate.of(2000, 1, 1));
  }

  @Test
  public void vanilla01() throws IOException {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    Check.that(p, "person").notHas(Person::firstName, blank());
    Check.that(p, "person").notHas(Person::firstName, "class", blank());
    Check.that(p, "person").notHas(Person::firstName,
        blank(),
        "Any message you like ${0}",
        "bro");
    Check.that(p, "person").notHas(Person::firstName,
        blank(),
        () -> new IOException());
    Check.that(p, "person").notHas(type(), EQ(), String.class);
    Check.that(p, "person").notHas(type(), "class", EQ(), String.class);
    Check.that(p, "person").notHas(type(),
        EQ(),
        String.class,
        "Any message you like ${0}",
        "bro");
    Check.that(p, "person").notHas(type(),
        EQ(),
        String.class,
        () -> new IOException());
    Check.that(p, "person").notHas(Person::firstName, EQ(), "jim");
    Check.that(p, "person").notHas(Person::birtDate, LT(), LocalDate.of(1900, 1, 1));
  }

  @Test
  public void hasPredicate00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").has(Person::firstName, NULL());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Function.apply(person) must be null (was john)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHasPredicate00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").notHas(Person::firstName, notNull());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Function.apply(person) must be null (was john)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_Predicate00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").has(Person::firstName, "firstName", NULL());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("person.firstName must be null (was john)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_Predicate00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").notHas(Person::firstName, "firstName", notNull());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("person.firstName must be null (was john)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Predicate_CustomMsg00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p).has(Person::firstName, NULL(), "Bad stuff");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Bad stuff", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IOException.class)
  public void notHas_Predicate_CustomMsg00() throws IOException {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    Check.that(p).notHas(Person::firstName, notNull(), () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void has_Predicate_CustomExc00() throws IOException {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    Check.that(p, "person").has(Person::firstName, NULL(), () -> new IOException());
  }

  @Test
  public void notHas_Predicate_CustomExc00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").notHas(Person::firstName,
          notNull(),
          "Failed test ${test}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Failed test notNull", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Relation00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").has(Person::birtDate, GT(), LocalDate.of(2000, 1, 1));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Function.apply(person) must be > 2000-01-01 (was 1966-04-22)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Relation00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").notHas(Person::firstName, EQ(), "john");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Function.apply(person) must not equal john", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Name_Relation00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").has(Person::birtDate,
          "birtDate",
          GT(),
          LocalDate.of(2000, 1, 1));
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("person.birtDate must be > 2000-01-01 (was 1966-04-22)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Name_Relation00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").notHas(Person::firstName, "firstName", EQ(), "john");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("person.firstName must not equal john", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void has_Relation_CustomMsg00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person")
          .has(Person::birtDate,
              GT(),
              LocalDate.of(2000, 1, 1),
              "Bad birth date: ${obj}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Bad birth date: 2000-01-01", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notHas_Relation_CustomMsg00() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    try {
      Check.that(p, "person").notHas(Person::firstName,
          EQ(),
          "john",
          "Bad person: ${arg}");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Bad person: john", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IOException.class)
  public void has_Relation_CustomExc00() throws IOException {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    Check.that(p, "person")
        .has(Person::birtDate,
            GT(),
            LocalDate.of(2000, 1, 1),
            () -> new IOException());
  }

  @Test(expected = IOException.class)
  public void notHas_Relation_CustomExc01() throws IOException {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    Check.that(p, "person").notHas(Person::firstName,
        EQ(),
        "john",
        () -> new IOException());
  }

  @Test
  public void testLambdas() {
    Person p = new Person("john", LocalDate.of(1966, 04, 22));
    Check.that(p).has(Person::firstName, (x, y) -> x.equals(y), "john");
  }

}
