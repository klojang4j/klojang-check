package org.klojang.check;

import org.junit.Test;
import org.klojang.check.extra.Result;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.*;

public class TestUtil {

  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  public static final String[] EMPTY_STRING_ARRAY = new String[0];

  public static <T> T[] pack(T... objs) {
    return objs;
  }

  public static int[] ints(int... ints) {
    return ints;
  }

  public static float[] floats(float... floats) {
    return floats;
  }

  public static class ResultTest {

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
    public void orElseGet00() {
      assertEquals((Integer) 42, Result.of(42).orElseGet(() -> 43));
      assertEquals(42, Result.notAvailable().orElseGet(() -> 42));
    }

  }

}
