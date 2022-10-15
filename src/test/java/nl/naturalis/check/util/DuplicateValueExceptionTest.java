package nl.naturalis.check.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static nl.naturalis.check.util.DuplicateValueException.Usage.*;

public class DuplicateValueExceptionTest {

  @Test
  public void init00() {
    DuplicateValueException exc = new DuplicateValueException("foo");
    assertEquals("foo", exc.getMessage());
  }

  @Test
  public void init01() {
    DuplicateValueException exc = new DuplicateValueException();
    assertEquals("duplicate value", exc.getMessage());
  }

  @Test
  public void init02() {
    DuplicateValueException exc = new DuplicateValueException(KEY);
    assertEquals("duplicate key", exc.getMessage());
  }

  @Test
  public void init03() {
    DuplicateValueException exc = new DuplicateValueException(ELEMENT);
    assertEquals("duplicate element", exc.getMessage());
  }

  @Test
  public void init04() {
    DuplicateValueException exc = new DuplicateValueException(VALUE);
    assertEquals("duplicate value", exc.getMessage());
  }

  @Test
  public void init05() {
    DuplicateValueException exc = new DuplicateValueException(KEY, "foo");
    assertEquals("duplicate key: foo", exc.getMessage());
  }

  @Test
  public void init06() {
    DuplicateValueException exc =
        new DuplicateValueException(ELEMENT, List.of("foo", "bar"));
    assertEquals("duplicate elements: foo, bar", exc.getMessage());
  }

  @Test
  public void init07() {
    DuplicateValueException exc =
        new DuplicateValueException(VALUE, List.of("foo"));
    assertEquals("duplicate values: foo", exc.getMessage());
  }

}