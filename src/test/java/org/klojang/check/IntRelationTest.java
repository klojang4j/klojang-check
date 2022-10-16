package org.klojang.check;

import org.junit.Test;
import org.klojang.check.Check;

import java.util.List;

import static org.junit.Assert.*;
import static org.klojang.check.CommonChecks.*;

public class IntRelationTest {

  @Test
  public void negate00() {
    assertTrue(lt().negate().exists(8, 6));
    assertTrue(lt().negate().exists(6, 6));
    assertFalse(lt().negate().exists(5, 6));
  }

  @Test
  public void converse00() {
    assertTrue(lt().converse().exists(8, 6));
    assertFalse(lt().converse().exists(6, 8));
  }

}