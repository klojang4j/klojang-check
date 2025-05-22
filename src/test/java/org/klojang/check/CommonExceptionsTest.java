package org.klojang.check;

import org.junit.Test;
import org.klojang.check.extra.DuplicateValueException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.CommonExceptions.*;
import static org.klojang.check.CommonExceptions.NULL;

public class CommonExceptionsTest {

  @Test(expected = IllegalStateException.class)
  public void illegalState00() {
    Check.on(STATE, 1).is(eq(), 0);
  }

  @Test(expected = IllegalStateException.class)
  public void illegalState01() {
    Check.that(1).is(eq(), 0, illegalState());
  }

  @Test(expected = IllegalStateException.class)
  public void illegalState02() {
    Check.that(1).is(eq(), 0, illegalState("foo"));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void index00() {
    Check.on(INDEX, 7).is(indexOf(), List.of("foo"));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void index01() {
    Check.that(7).is(indexOf(), List.of("foo"), indexOutOfBounds("7 out of bound"));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void index02() {
    Check.that(7).is(indexOf(), List.of("foo"), indexOutOfBounds(7));
  }

  @Test(expected = IOException.class)
  public void io00() throws IOException {
    Check.on(IO, "foo").is(EQ(), "bar");
  }

  @Test(expected = IOException.class)
  public void io01() throws IOException {
    Check.that("foo").is(EQ(), "bar", ioException("foo"));
  }

  @Test(expected = IOException.class)
  public void io02() throws IOException {
    Check.that("foo").is(EQ(), "bar", ioException());
  }

  @Test(expected = NullPointerException.class)
  public void npe00() {
    Check.on(NULL, null).is(notNull());
  }

  @Test(expected = NullPointerException.class)
  public void npe01() throws IOException {
    Check.that(null).is(notNull(), npe("whoops"));
  }

  @Test(expected = NullPointerException.class)
  public void npe02() throws IOException {
    Check.that(null).is(notNull(), CommonExceptions.npe());
  }

  @Test(expected = NoSuchElementException.class)
  public void element00() {
    Check.on(ELEMENT, 1).is(eq(), 2);
  }

  @Test(expected = NoSuchElementException.class)
  public void element01() {
    Check.that(1).is(eq(), 2, noSuchElement("foo"));
  }

  @Test(expected = NoSuchElementException.class)
  public void element02() {
    Check.that(1).is(eq(), 2, noSuchElement());
  }

  @Test(expected = DuplicateValueException.class)
  public void duplicate00() {
    Check.on(DUPLICATE, 1).is(eq(), 2);
  }

  @Test(expected = DuplicateValueException.class)
  public void duplicate02() {
    Check.that(123).is(eq(), 2, duplicateKey(123));
  }

  @Test(expected = DuplicateValueException.class)
  public void duplicate03() {
    Check.that(123).is(eq(), 2, duplicateKey("foo"));
  }

  @Test(expected = DuplicateValueException.class)
  public void duplicate04() {
    Check.that(123).is(eq(), 2, duplicateElement(123));
  }

  @Test(expected = DuplicateValueException.class)
  public void duplicate05() {
    Check.that(123).is(eq(), 2, duplicateElement("foo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void argument00() {
    Check.on(ARGUMENT, 1).is(eq(), 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void argument01() {
    Check.that(1).is(eq(), 2, illegalArgument("foo"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void argument02() {
    Check.that(1).is(eq(), 2, illegalArgument());
  }

}
