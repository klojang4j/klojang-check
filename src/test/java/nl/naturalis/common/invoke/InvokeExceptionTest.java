package nl.naturalis.common.invoke;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class InvokeExceptionTest {

  @Test
  public void missingNoArgConstructor00() {
    assertEquals("missing no-arg constructor for class java.io.File",
        InvokeException.missingNoArgConstructor(File.class).getMessage());
  }

  @Test
  public void noSuchConstructor00() {
    assertEquals("no such constructor: File(int, Float)",
        InvokeException.noSuchConstructor(File.class, int.class, Float.class).getMessage());
  }

  @Test
  public void wrap00() {
    Throwable exception = new IOException("Something went wrong");
    BeanReader<Person> reader = new BeanReader<>(Person.class);
    Getter getter = reader.getIncludedGetters().get("firstName");
    InvokeException ie = InvokeException.wrap(exception, reader, getter);
    assertEquals(
        "Error while reading BeanReader.firstName: java.io.IOException: Something went wrong",
        ie.getMessage());
  }

}
