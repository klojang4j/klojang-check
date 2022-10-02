package nl.naturalis.check;

import nl.naturalis.check.Check;
import org.junit.Test;

import java.util.List;

import static nl.naturalis.check.CommonChecks.*;
import static org.junit.Assert.*;

public class IntRelationTest {

  @Test
  public void negate00() {
    assertTrue(lt().negate().exists(8, 6));
    assertTrue(lt().negate().exists(6, 6));
    assertFalse(lt().negate().exists(5, 6));
  }

  @Test
  public void converse00() {
    assertTrue(lt().converse().exists(6, 8));
    assertFalse(lt().converse().exists(8, 6));
  }

}