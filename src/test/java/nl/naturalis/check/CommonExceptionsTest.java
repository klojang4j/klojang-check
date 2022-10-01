package nl.naturalis.check;

import org.junit.Test;

import static nl.naturalis.check.CommonExceptions.*;
import static nl.naturalis.check.CommonChecks.*;

public class CommonExceptionsTest {

  @Test(expected = IllegalStateException.class)
  public void illegalState00() {
    Check.that(1).is(eq(), 0, illegalState());
  }
}
