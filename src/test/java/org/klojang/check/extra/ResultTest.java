package org.klojang.check.extra;

import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class ResultTest {

  @Test
  public void get00() {
    var result = Result.of("Hi there");
    assertEquals("Hi there", result.get());
  }

  @Test(expected = NoSuchElementException.class)
  public void get01() {
    Result.notAvailable().get();
  }

  @Test
  public void orElse00() {
    assertEquals("Hi there", Result.of("Hi there").orElse("Where are you?"));
    assertEquals("Where are you?", Result.notAvailable().orElse("Where are you?"));
  }

  @Test
  public void hashCode00() {
    assertEquals(42, Result.of(42).hashCode());
  }

  @Test
  public void hashCode01() {
    assertEquals(0, Result.nullResult().hashCode());
  }

  @Test
  public void equals00() {
    Result result = Result.of(List.of(1, 2, 3));
    assertTrue(result.equals(result));
    assertTrue(result.equals(Result.of(List.of(1, 2, 3))));
    assertFalse(result.equals(null));
    assertFalse(result.equals(List.of(1, 2, 3)));
    assertFalse(Result.notAvailable().equals(result));
    assertFalse(result.equals(Result.notAvailable()));
    assertFalse(result.equals(new Object()));
    assertFalse(result.equals(Result.of(List.of(1, 2))));
  }

  @Test
  public void equals01() {
    assertNotEquals(Result.notAvailable(), Result.nullResult());
  }

  @Test
  public void equals02() {
    assertNotEquals(Result.notAvailable(), Result.of(new Object()));
  }

  @Test
  public void toString00() {
    assertEquals("Result[Hi there]", Result.of("Hi there").toString());
    assertEquals("Result.notAvailable", Result.notAvailable().toString());
  }

  @Test
  public void orElseGet00() {
    assertEquals((Integer) 42, Result.of(42).orElseGet(() -> 43));
    assertEquals(42, Result.notAvailable().orElseGet(() -> 42));
  }

  @Test
  public void ifAvailable00() {
    AtomicReference ar = new AtomicReference<>("bar");
    Result<String> r = Result.of("foo");
    r.ifAvailable(s -> ar.set(s));
    assertEquals("foo", ar.get());
  }

  @Test
  public void ifAvailable01() {
    AtomicReference ar = new AtomicReference<>("bar");
    Result<String> r = Result.notAvailable();
    r.ifAvailable(s -> ar.set(s));
    assertEquals("bar", ar.get());
  }

  @Test
  public void isAvailable00() {
    assertFalse(Result.notAvailable().isAvailable());
    assertTrue(Result.notAvailable().isUnavailable());
    assertTrue(Result.of(null).isAvailable());
    assertFalse(Result.of(null).isUnavailable());
    assertFalse(Result.of("foo").isUnavailable());
  }

  @Test
  public void isAvailableAndNull00() {
    Result<String> r = Result.of(null);
    assertTrue(r.isAvailableAndNull());
  }

  @Test
  public void isAvailableAndNull01() {
    Result<String> r = Result.of("foo");
    assertFalse(r.isAvailableAndNull());
  }

  @Test
  public void isAvailableAndNull02() {
    Result<String> r = Result.of(null);
    assertSame(Result.nullResult(), r);
  }

  @Test
  public void isAvailableAndNotNull00() {
    Result<String> r = Result.of(null);
    assertFalse(r.isAvailableAndNotNull());
  }

  @Test
  public void isAvailableAndNotNull01() {
    Result<String> r = Result.of("foo");
    assertTrue(r.isAvailableAndNotNull());
  }

  @Test
  public void isAvailableAndNotNull02() {
    Result<String> r = Result.notAvailable();
    assertFalse(r.isAvailableAndNotNull());
  }

  @Test
  public void isUnavailableOrNull00() {
    assertTrue(Result.notAvailable().isUnavailableOrNull());
  }

  @Test
  public void isUnavailableOrNull01() {
    assertTrue(Result.nullResult().isUnavailableOrNull());
  }

  @Test
  public void isUnavailableOrNull02() {
    assertFalse(Result.of("foo").isUnavailableOrNull());
  }

}
