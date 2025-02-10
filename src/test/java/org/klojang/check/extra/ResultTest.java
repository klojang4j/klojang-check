package org.klojang.check.extra;

import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
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
  public void toString00() {
    assertEquals("Result[Hi there]", Result.of("Hi there").toString());
    assertEquals("Result.notAvailable", Result.notAvailable().toString());
  }

  @Test
  public void or00() {
    assertEquals(Result.of(42), Result.of(42).or(Result.of(43)));
    assertEquals(Result.of(42), Result.notAvailable().or(Result.of(42)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void or01() {
    Result.notAvailable().or(Result.notAvailable());
  }

  @Test
  public void isEmpty00() {
    assertTrue(Result.notAvailable().isEmpty());
    assertTrue(Result.of("").isEmpty());
    assertTrue(Result.of(Set.of()).isEmpty());
    assertFalse(Result.of("hi there").isEmpty());
    assertFalse(Result.of(Set.of(1, 2, 3)).isEmpty());
  }

  @Test
  public void isDeepNotEmpty00() {
    assertFalse(Result.notAvailable().isDeepNotEmpty());
    assertFalse(Result.of("").isDeepNotEmpty());
    assertFalse(Result.of(Set.of()).isDeepNotEmpty());
    assertTrue(Result.of("hi there").isDeepNotEmpty());
    assertTrue(Result.of(Set.of(1, 2, 3)).isDeepNotEmpty());
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
    r = Result.of("foo");
    assertFalse(r.isAvailableAndNull());
  }

  @Test
  public void isAvailableAndNotNull00() {
    Result<String> r = Result.of(null);
    assertFalse(r.isAvailableAndNotNull());
    r = Result.of("foo");
    assertTrue(r.isAvailableAndNotNull());
  }

}
