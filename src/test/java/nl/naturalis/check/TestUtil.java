package nl.naturalis.check;

import nl.naturalis.check.util.Result;
import org.junit.Test;

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
    public void or00() {
      assertEquals(Result.of(42), Result.of(42).or(Result.of(43)));
      assertEquals(Result.of(42), Result.notAvailable().or(Result.of(42)));
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

  }

}
