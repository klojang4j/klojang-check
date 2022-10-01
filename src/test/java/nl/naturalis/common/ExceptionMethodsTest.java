package nl.naturalis.common;

import nl.naturalis.common.exception.RootException;
import nl.naturalis.common.exception.UncheckedException;
import org.junit.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static nl.naturalis.common.ArrayMethods.implode;
import static nl.naturalis.common.ExceptionMethods.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ExceptionMethodsTest {

  @Test
  public void getRootCause00() {
    Throwable a = new NoSuchElementException();
    Throwable b = new IOException(a);
    Throwable c = new RuntimeException(b);
    assertEquals(a, getRootCause(c));
  }

  @Test
  public void getRootStackTrace00() {
    Throwable a = new NoSuchElementException();
    Throwable b = new IOException(a);
    Throwable c = new RuntimeException(b);
    StackTraceElement[] trace = getRootStackTrace(c);
    // THIS method must be the 1st in the stack trace
    assertTrue(trace[0].getMethodName().equals("getRootStackTrace00"));
  }

  @Test
  public void getRootStackTrace01() {
    Throwable a = new NoSuchElementException();
    Throwable b = new IOException(a);
    Throwable c = new RuntimeException(b);
    StackTraceElement[] trace = getRootStackTrace(c, "nl.naturalis");
    assertEquals(1, trace.length);
    assertTrue(trace[0].getClassName().contains("nl.naturalis"));
  }

  @Test
  public void getRootStackTraceAsString00() {
    Throwable a = new NoSuchElementException("foo == bar");
    Throwable b = new IOException(a);
    Throwable c = new RuntimeException(b);
    String s = getRootStackTraceAsString(c);
    //System.out.println(s);
    assertTrue(s.startsWith("java.util.NoSuchElementException: foo == bar"));
  }

  @Test
  public void getRootStackTraceAsString01() {
    Throwable a = new NoSuchElementException("foo == bar");
    Throwable b = new IOException(a);
    Throwable c = new RuntimeException(b);
    String s = getRootStackTraceAsString(c, "NoSuchElement");
    //System.out.println(s);
    assertEquals("java.util.NoSuchElementException: foo == bar", s);
  }

  @Test
  public void getDetailedMessage00() {
    String s = getDetailedMessage(
        new NoSuchElementException("foo == bar"), "foo");
    //System.out.println(s);
    assertTrue(s.contains("foo == bar"));
  }

  @Test
  public void getDetailedMessage01() {
    String s = getDetailedMessage(
        new NoSuchElementException("foo == bar"), "java.lang");
    //System.out.println(s);
    // bit iffy, but pretty safe in practice:
    assertTrue(s.contains("java.lang.reflect.Method.invoke"));
  }

  @Test
  public void wrap00() {
    IllegalArgumentException iae = new IllegalArgumentException(
        "invalid value for foo");
    RuntimeException rte = wrap(iae);
    assertSame(rte, iae);
  }

  @Test
  public void wrap01() {
    IOException ioe = new IOException("I/O error");
    RuntimeException rte = wrap(ioe);
    assertEquals(RuntimeException.class, rte.getClass());
    assertEquals(IOException.class, rte.getCause().getClass());
    assertEquals("java.io.IOException: I/O error", rte.getMessage());
  }

  @Test
  public void wrap02() {
    IOException ioe = new IOException("I/O error");
    RuntimeException rte = wrap(ioe, "Hello world");
    assertEquals(RuntimeException.class, rte.getClass());
    assertEquals(IOException.class, rte.getCause().getClass());
    assertEquals("Hello world", rte.getMessage());
  }

  @Test
  public void wrap03() {
    IOException ioe = new IOException("I/O error");
    RuntimeException rte = wrap(ioe, "Hello %s", "world");
    assertEquals(RuntimeException.class, rte.getClass());
    assertEquals(IOException.class, rte.getCause().getClass());
    assertEquals("Hello world", rte.getMessage());
  }

  @Test
  public void wrap04() {
    IOException ioe = new IOException("I/O error");
    RuntimeException rte = wrap(ioe, "Hello %s", "world");
  }

  @Test
  public void wrap05() {
    IllegalStateException ise = new IllegalStateException("I/O error");
    RuntimeException rte = wrap(ise, UncheckedException::new);
    assertEquals(IllegalStateException.class, rte.getClass());
  }

  @Test
  public void wrap06() {
    IOException ioe = new IOException("I/O error");
    RuntimeException rte = wrap(ioe, IllegalArgumentException::new, "Bad input");
    assertEquals(IllegalArgumentException.class, rte.getClass());
    assertEquals("Bad input", rte.getMessage());
  }

  @Test
  public void wrap07() {
    IOException ioe = new IOException("I/O error");
    RuntimeException rte = wrap(ioe,
        IllegalArgumentException::new,
        "Bad %s",
        "input");
    assertEquals(IllegalArgumentException.class, rte.getClass());
    assertEquals("Bad input", rte.getMessage());
  }

  @Test
  public void uncheck00() {
    Throwable t = uncheck(new IllegalArgumentException("foo"));
    assertSame(IllegalArgumentException.class, t.getClass());
  }

  @Test
  public void uncheck01() {
    Throwable t = uncheck(new IOException("foo"));
    assertSame(UncheckedException.class, t.getClass());
  }

  @Test
  public void uncheck02() {
    Throwable rte = uncheck(new IOException("foo"), "foo == bar");
    assertSame(UncheckedException.class, rte.getClass());
    assertEquals("foo == bar", ((UncheckedException) rte).getCustomMessage().get());
  }

  @Test
  public void rootCause00() {
    Throwable a = new NoSuchElementException();
    Throwable b = new IOException(a);
    Throwable c = new IllegalArgumentException(b);
    assertEquals(IllegalArgumentException.class, rootCause(c).getClass());
  }

  @Test
  public void rootCause01() {
    Throwable a = new NoSuchElementException();
    Throwable b = new IOException(a);
    Throwable c = new IllegalArgumentException(b);
    Throwable d = new Exception(c);
    RuntimeException rte = rootCause(d, "foo == bar");
    assertEquals(RootException.class, rte.getClass());
    assertEquals("foo == bar", ((RootException) rte).getCustomMessage().get());
  }

}
