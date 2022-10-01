package nl.naturalis.common.path;

import org.junit.Test;

import java.io.File;
import java.nio.channels.FileChannel;

import static org.junit.Assert.*;

public class PathWalkerExceptionTest {

  @Test
  public void terminalValue() {
    PathWalkerException.Factory excFactory = PathWalkerException.terminalValue(
        Path.from("foo.bar.bozo"), 1, "teapot");
    assertEquals(
        "invalid path: \"foo.bar.bozo\" (segment 2) *** terminal value encountered at segment \"bar\": (String) teapot",
        excFactory.get().getMessage());
  }

  @Test
  public void typeMismatch00() {
    PathWalkerException.Factory excFactory = PathWalkerException.typeMismatch(
        Path.from("foo.bar.bozo"), 1, "my message to you");
    assertEquals(
        "path foo.bar.bozo, segment 2: my message to you",
        excFactory.get().getMessage());
  }

  @Test
  public void typeMismatch01() {
    PathWalkerException.Factory excFactory = PathWalkerException.typeMismatch(
        Path.from("foo.bar.bozo"), 1, File.class, FileChannel.class);
    assertEquals(
        "path foo.bar.bozo, segment 2: cannot assign File to FileChannel",
        excFactory.get().getMessage());
  }

  @Test
  public void keyDeserializationFailed00() {
    PathWalkerException.Factory excFactory = PathWalkerException.keyDeserializationFailed(
        Path.from("foo.bar.bozo"), 0, new KeyDeserializationException("no can do"));

    assertEquals(
        "invalid path: \"foo.bar.bozo\" (segment 1) *** no can do",
        excFactory.get().getMessage());
  }

  @Test
  public void keyDeserializationFailed01() {
    PathWalkerException.Factory excFactory = PathWalkerException.keyDeserializationFailed(
        Path.from("foo.bar.bozo"), 0, new KeyDeserializationException());
    assertEquals(
        "invalid path: \"foo.bar.bozo\" (segment 1) *** failed to deserialize \"foo\" into map key",
        excFactory.get().getMessage());
  }

}