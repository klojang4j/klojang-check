package org.klojang.check;

import org.junit.Test;
import org.klojang.check.Check;
import org.klojang.check.InvalidCheckException;
import org.klojang.check.util.Result;

import static org.klojang.check.CommonChecks.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

  @Test
  public void isEmpty00() {
    Check.that(Map.of()).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isEmpty01() {
    Check.that(Map.of(1, 1)).is(empty());
  }

  @Test
  public void isEmpty02() {
    Check.that(Result.of(null)).is(empty());
  }

  @Test
  public void isEmpty03() {
    Check.that(Result.notAvailable()).is(empty());
  }

  @Test
  public void isEmpty04() {
    Check.that(Result.of("")).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isEmpty05() {
    Check.that(Result.of("666")).is(empty());
  }

  @Test
  public void isEmpty06() {
    Check.that(new String[0]).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isEmpty07() {
    Check.that(new String[] {"hi"}).is(empty());
  }

  @Test
  public void isEmpty08() {
    Check.that(new int[0]).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isEmpty09() {
    Check.that(new int[] {666}).is(empty());
  }

  @Test
  public void isEmpty10() {
    Check.that(Optional.empty()).is(empty());
  }

  @Test
  public void isEmpty11() {
    Check.that(Optional.of(List.of())).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isEmpty12() {
    Check.that(Optional.of(List.of(1))).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isEmpty13() throws IOException {
    File tmp = File.createTempFile("foo", "bar");
    try {
      Files.writeString(tmp.toPath(), "     Hi there");
      Check.that(tmp).is(empty());
    } finally {
      tmp.delete();
    }
  }

  @Test
  public void isEmpty14() throws IOException {
    File tmp = File.createTempFile("foo", "bar");
    try {
      Check.that(tmp).is(empty());
    } finally {
      tmp.delete();
    }
  }

  @Test
  public void isEmpty15() throws IOException {
    Check.that(new String[] {}).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isEmpty16() throws IOException {
    Check.that(new String[] {"hello", "world"}).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty00() {
    Check.that(Map.of()).is(notEmpty());
  }

  @Test
  public void isNotEmpty01() {
    Check.that(Map.of(1, 1)).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty02() {
    Check.that(Result.of(null)).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty03() {
    Check.that(Result.notAvailable()).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty04() {
    Check.that(Result.of("")).is(notEmpty());
  }

  @Test
  public void isNotEmpty05() {
    Check.that(Result.of("666")).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty06() {
    Check.that(new String[0]).is(notEmpty());
  }

  @Test
  public void isNotEmpty07() {
    Check.that(new String[] {"hi"}).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty08() {
    Check.that(new int[0]).is(notEmpty());
  }

  @Test
  public void isNotEmpty09() {
    Check.that(new int[] {666}).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty10() {
    Check.that(Optional.empty()).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty11() {
    Check.that(Optional.of(List.of())).is(notEmpty());
  }

  @Test
  public void isNotEmpty12() {
    Check.that(Optional.of(List.of(1))).is(notEmpty());
  }

  @Test
  public void isNotEmpty13() throws IOException {
    File tmp = File.createTempFile("foo", "bar");
    try {
      Files.writeString(tmp.toPath(), "     Hi there");
      Check.that(tmp).is(notEmpty());
    } finally {
      tmp.delete();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty14() throws IOException {
    File tmp = File.createTempFile("foo", "bar");
    try {
      Check.that(tmp).is(notEmpty());
    } finally {
      tmp.delete();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void isNotEmpty15() throws IOException {
    Check.that(new String[] {}).is(notEmpty());
  }

  @Test
  public void isNotEmpty16() throws IOException {
    Check.that(new String[] {"hello", "world"}).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void isDeepNotEmpty00() throws IOException {
    File tmp = File.createTempFile("foo", "bar");
    try {
      Files.writeString(tmp.toPath(), "       Hi there        ");
      Check.that(tmp).is(deepNotEmpty());
    } finally {
      tmp.delete();
    }
  }

  @Test
  public void isDeepNotEmpty01() throws IOException {
    File tmp = File.createTempFile("foo", "bar");
    try {
      Files.writeString(tmp.toPath(), "             \t\n             ");
      Check.that(tmp).is(deepNotEmpty());
    } finally {
      tmp.delete();
    }
  }

  @Test
  public void isDeepNotEmpty02() throws IOException {
    File tmp = File.createTempFile("foo", "bar");
    try {
      Check.that(tmp).is(deepNotEmpty());
    } finally {
      tmp.delete();
    }
  }

  @Test
  public void inArray00() {
    String[] array = {"to", "be", null, "not", "to", "be"};
    Check.that(null).is(inArray(), array);
  }

  @Test(expected = IllegalArgumentException.class)
  public void inArray01() {
    String[] array = {"to", "be", "or", "not", "to", "be"};
    Check.that(null).is(inArray(), array);
  }

}
