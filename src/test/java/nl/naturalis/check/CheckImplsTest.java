package nl.naturalis.check;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static nl.naturalis.check.CommonChecks.indexInclusiveOf;
import static nl.naturalis.check.CommonChecks.indexOf;

public class CheckImplsTest {

  @Test
  public void isStringIndexInclusiveOf00() {
    Check.that(0).is(indexInclusiveOf(), "012345");
  }

  @Test
  public void isStringIndexInclusiveOf01() {
    Check.that(6).is(indexInclusiveOf(), "012345");
  }

  @Test(expected = IllegalArgumentException.class)
  public void isStringIndexInclusiveOf02() {
    Check.that(-1).is(indexInclusiveOf(), "012345");
  }

  @Test(expected = IllegalArgumentException.class)
  public void isStringIndexInclusiveOf03() {
    Check.that(7).is(indexInclusiveOf(), "012345");
  }

  @Test
  public void isListIndexInclusiveOf00() {
    Check.that(0).is(indexInclusiveOf(), List.of(0, 1, 2, 3, 4, 5));
  }

  @Test
  public void isListIndexInclusiveOf01() {
    Check.that(6).is(indexInclusiveOf(), List.of(0, 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void isListIndexInclusiveOf02() {
    Check.that(-1).is(indexInclusiveOf(), List.of(0, 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void isListIndexInclusiveOf03() {
    Check.that(7).is(indexInclusiveOf(), List.of(0, 1, 2, 3, 4, 5));
  }

  @Test
  public void isArrayIndexInclusiveOf00() {
    Check.that(0).is(indexInclusiveOf(), new int[] {0, 1, 2, 3, 4, 5});
  }

  @Test
  public void isArrayIndexInclusiveOf01() {
    Check.that(6).is(indexInclusiveOf(), new int[] {0, 1, 2, 3, 4, 5});
  }

  @Test(expected = IllegalArgumentException.class)
  public void isArrayIndexInclusiveOf02() {
    Check.that(-1).is(indexInclusiveOf(), new int[] {0, 1, 2, 3, 4, 5});
  }

  @Test(expected = IllegalArgumentException.class)
  public void isArrayIndexInclusiveOf03() {
    Check.that(7).is(indexInclusiveOf(), new int[] {0, 1, 2, 3, 4, 5});
  }

  @Test(expected = InvalidCheckException.class)
  public void indexInclusiveOf00() {
    Check.that(7).is(indexInclusiveOf(), new ByteArrayOutputStream());
  }

  @Test
  public void isStringIndexOf00() {
    Check.that(0).is(indexOf(), "012345");
  }

  @Test
  public void isStringIndexOf01() {
    Check.that(5).is(indexOf(), "012345");
  }

  @Test(expected = IllegalArgumentException.class)
  public void isStringIndexOf02() {
    Check.that(-1).is(indexOf(), "012345");
  }

  @Test(expected = IllegalArgumentException.class)
  public void isStringIndexOf03() {
    Check.that(6).is(indexOf(), "012345");
  }

  @Test
  public void isListIndexOf00() {
    Check.that(0).is(indexOf(), List.of(0, 1, 2, 3, 4, 5));
  }

  @Test
  public void isListIndexOf01() {
    Check.that(5).is(indexOf(), List.of(0, 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void isListIndexOf02() {
    Check.that(-1).is(indexOf(), List.of(0, 1, 2, 3, 4, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void isListIndexOf03() {
    Check.that(6).is(indexOf(), List.of(0, 1, 2, 3, 4, 5));
  }

  @Test
  public void isArrayIndexOf00() {
    Check.that(0).is(indexOf(), new int[] {0, 1, 2, 3, 4, 5});
  }

  @Test
  public void isArrayIndexOf01() {
    Check.that(5).is(indexOf(), new int[] {0, 1, 2, 3, 4, 5});
  }

  @Test(expected = IllegalArgumentException.class)
  public void isArrayIndexOf02() {
    Check.that(-1).is(indexOf(), new int[] {0, 1, 2, 3, 4, 5});
  }

  @Test(expected = IllegalArgumentException.class)
  public void isArrayIndexOf03() {
    Check.that(6).is(indexOf(), new int[] {0, 1, 2, 3, 4, 5});
  }

  @Test(expected = InvalidCheckException.class)
  public void indexOf00() {
    Check.that(6).is(indexOf(), new ByteArrayOutputStream());
  }

}
