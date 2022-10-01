package nl.naturalis.common.path;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static nl.naturalis.common.path.ErrorCode.*;
import static org.junit.Assert.assertEquals;

public class BeanSegmentWriterTest {

  @Test(expected = PathWalkerException.class)
  public void test00() {
    PathWalker pw = new PathWalker(List.of(Path.from("foo")), false);
    try {
      Person person = new Person();
      pw.write(person, 666);
    } catch (PathWalkerException e) {
      assertEquals(NO_SUCH_PROPERTY, e.getErrorCode());
      throw e;
    }
  }

  @Test
  public void test01() {
    BeanSegmentWriter bsw = new BeanSegmentWriter(true, null);
    Person person = new Person();
    bsw.write(person, Path.from("foo"), 666);
    // not much happenin'
  }

  @Test(expected = PathWalkerException.class)
  public void test02() {
    BeanSegmentWriter bsw = new BeanSegmentWriter(false, null);
    Person person = new Person();
    try {
      bsw.write(person, Path.from("firstName.hash"), 666);
    } catch (PathWalkerException e) {
      assertEquals(NO_SUCH_PROPERTY, e.getErrorCode());
      throw e;
    }
  }

  @Test(expected = PathWalkerException.class)
  public void test03() {
    PathWalker pw = new PathWalker(List.of(Path.from("manager.address.street")),
        false);
    Department dept = new Department();
    try {
      pw.write(dept, 666);
    } catch (PathWalkerException e) {
      assertEquals(TERMINAL_VALUE, e.getErrorCode());
      throw e;
    }
  }

  @Test(expected = PathWalkerException.class)
  public void test04() {
    PathWalker pw = new PathWalker(List.of(Path.from("manager..street")), false);
    Department dept = new Department();
    dept.setManager(new Employee());
    try {
      pw.write(dept, 666);
    } catch (PathWalkerException e) {
      assertEquals(EMPTY_SEGMENT, e.getErrorCode());
      throw e;
    }
  }

  @Test(expected = PathWalkerException.class)
  public void test05() {
    PathWalker pw = new PathWalker(List.of(Path.from("naughtyProperty")), false);
    Person person = new Person();
    try {
      pw.write(person, "foo");
    } catch (PathWalkerException e) {
      assertEquals(EXCEPTION, e.getErrorCode());
      throw e;
    }
  }

  @Test(expected = PathWalkerException.class)
  public void test06() {
    PathWalker pw = new PathWalker(List.of(Path.from("firstName")), false);
    Person person = new Person();
    try {
      pw.write(person, new File("/tmp/foo.txt"));
    } catch (PathWalkerException e) {
      assertEquals(TYPE_MISMATCH, e.getErrorCode());
      throw e;
    }
  }

}
