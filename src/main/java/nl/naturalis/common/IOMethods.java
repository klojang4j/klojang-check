package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;
import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonExceptions.IO;
import static nl.naturalis.common.ObjectMethods.isEmpty;
import static nl.naturalis.common.StringMethods.*;
import static nl.naturalis.common.x.Param.*;

/**
 * I/O-related methods.
 *
 * @author Ayco Holleman
 */
public class IOMethods {

  private static final String INVALID_PATH = "No such resource: \"${0}\"";

  // parameter names:
  private static final String CONTENTS = "contents";
  private static final String CHUNK_SIZE = "chunkSize";
  private static final String REQUESTER = "requester";

  private static final String DOT = ".";

  private IOMethods() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the contents of the specified file.
   *
   * @param path the path to the file
   * @return the contents of the specified file
   */
  public static String getContents(String path) {
    Check.notNull(path, PATH);
    File f = Check.that(new File(path)).is(file()).ok();
    try (FileInputStream fis = new FileInputStream(f)) {
      return getContents(fis);
    } catch (IOException e) {
      throw ExceptionMethods.uncheck(e);
    }
  }

  /**
   * Returns the contents of the specified resource. Bytes are read in chunks of 512
   * bytes.
   *
   * @param clazz the {@code Class} to call
   *     {@link Class#getResourceAsStream(String) getResourceAsStream} on
   * @param path the path to the resource
   * @return the contents of the specified resource
   */
  public static String getContents(Class<?> clazz, String path) {
    return getContents(clazz, path, 512);
  }

  /**
   * Returns the contents of the specified resource.
   *
   * @param clazz the {@code Class} to call
   *     {@link Class#getResourceAsStream(String) getResourceAsStream} on
   * @param path the path to the resource
   * @param chunkSize the number of bytes to read at a time
   * @return the contents of the specified resource
   */
  public static String getContents(Class<?> clazz, String path, int chunkSize) {
    Check.that(clazz, CLAZZ).is(notNull()).and(path, PATH).is(notNull());
    try (InputStream in = clazz.getResourceAsStream(path)) {
      Check.that(in).is(notNull(), INVALID_PATH, path);
      return getContents(in, chunkSize);
    } catch (IOException e) {
      throw ExceptionMethods.uncheck(e);
    }
  }

  /**
   * Returns a {@code String} created from the bytes read from the specified input
   * stream. Bytes are read in chunks of 512 bytes. The input stream is <i>not</i>
   * closed once all bytes have been read.
   *
   * @param in the input stream
   * @return a {@code String} from the bytes read from the specified input stream
   */
  public static String getContents(InputStream in) {
    return getContents(in, 512);
  }

  /**
   * Returns a {@code String} created from the bytes read from the specified input
   * stream. The input stream is <i>not</i> closed once all bytes have been read.
   *
   * @param in the input stream
   * @param chunkSize the number of bytes to read at a time
   * @return a {@code String} from the bytes read from the specified input stream
   */
  public static String getContents(InputStream in, int chunkSize) {
    Check.that(chunkSize, CHUNK_SIZE).is(gt(), 0);
    ByteArrayOutputStream out = new ByteArrayOutputStream(chunkSize);
    pipe(in, out, chunkSize);
    return out.toString(UTF_8);
  }

  /**
   * Simple file-write method. Not efficient, but easy to use. Overwrites
   * pre-existing contents.
   *
   * @param path the path to the file
   * @param contents The contents to be written
   */
  public static void write(String path, String contents) {
    Check.that(path, PATH).is(notNull()).and(contents, CONTENTS).is(notNull());
    try {
      Files.writeString(Path.of(path), contents, UTF_8, CREATE, TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw ExceptionMethods.uncheck(e);
    }
  }

  /**
   * Simple file-write method. Not efficient, but easy to use. Appends the specified
   * string to the contents of the specified file.
   *
   * @param path the path to the file
   * @param contents The contents to be written
   */
  public static void append(String path, String contents) {
    Check.that(path, PATH).is(notNull()).and(contents, CONTENTS).is(notNull());
    try {
      Files.writeString(Path.of(path), contents, UTF_8, CREATE, APPEND);
    } catch (IOException e) {
      throw ExceptionMethods.uncheck(e);
    }
  }

  /**
   * Returns the contents of the specified resource as a byte array. Bytes are read
   * in chunks of 512 bytes.
   *
   * @param clazz the {@code Class} to call
   *     {@link Class#getResourceAsStream(String) getResourceAsStream} on
   * @param path the path to the resource
   * @return the bytes contained in the specified resource
   */
  public static byte[] readBytes(Class<?> clazz, String path) {
    return readBytes(clazz, path, 512);
  }

  /**
   * Returns the contents of the specified resource as a byte array.
   *
   * @param clazz the {@code Class} to call
   *     {@link Class#getResourceAsStream(String) getResourceAsStream} on
   * @param path the path to the resource
   * @param chunkSize the number of bytes to read at a time
   * @return the contents of the specified resource
   */
  public static byte[] readBytes(Class<?> clazz, String path, int chunkSize) {
    Check.that(clazz, CLAZZ).is(notNull()).and(path, PATH).is(notNull());
    try (InputStream in = clazz.getResourceAsStream(path)) {
      Check.that(in).is(notNull(), INVALID_PATH, path);
      return readBytes(in, chunkSize);
    } catch (IOException e) {
      throw ExceptionMethods.uncheck(e);
    }
  }

  /**
   * Returns a {@code byte[]} array containing the bytes read from the specified
   * input stream. Bytes are read in chunks of 512 bytes. <i>The input stream is not
   * closed by this method.</i>
   *
   * @param in the input stream
   * @return a {@code byte[]} array containing the bytes read from the specified
   *     input stream
   */
  public static byte[] readBytes(InputStream in) {
    return readBytes(in, 512);
  }

  /**
   * Returns a {@code byte[]} array containing the bytes read from the specified
   * input stream. Bytes are read in chunks of the specified size. <i>The input
   * stream is not closed by this method.</i>
   *
   * @param in the input stream
   * @param chunkSize the number of bytes to read at a time
   * @return a {@code byte[]} array containing the bytes read from the specified
   *     input stream
   */
  public static byte[] readBytes(InputStream in, int chunkSize) {
    ByteArrayOutputStream out = new ByteArrayOutputStream(chunkSize);
    pipe(in, out, chunkSize);
    return out.toByteArray();
  }

  /**
   * Reads all bytes from the specified input stream and writes them to the specified
   * output stream. Bytes are read and written in chunks of 512 bytes at a time.
   * <i>The input stream and the output stream are not closed by this method.</i>
   *
   * @param in the input stream
   * @param out the output stream
   */
  public static void pipe(InputStream in, OutputStream out) {
    pipe(in, out, 512);
  }

  /**
   * Reads all bytes from the specified input stream and writes them to the specified
   * output stream. Bytes are read and written in chunks of the specified size.
   * <i>The input stream and the output stream are not closed by this method.</i>
   *
   * @param in the input stream
   * @param out the output stream
   * @param chunkSize the number of bytes read/written at a time
   */
  public static void pipe(InputStream in, OutputStream out, int chunkSize) {
    Check.that(in, IN).is(notNull())
        .and(out, OUT).is(notNull())
        .and(chunkSize, CHUNK_SIZE).is(gt(), 0);
    byte[] data = new byte[chunkSize];
    try {
      int n = in.read(data, 0, data.length);
      while (n != -1) {
        out.write(data, 0, n);
        out.flush();
        n = in.read(data, 0, data.length);
      }
    } catch (IOException e) {
      throw ExceptionMethods.uncheck(e);
    }
  }

  /**
   * Creates a new, empty file in the file system's temp directory. Equivalent to:
   *
   * <blockquote><pre>{@code
   * createTempFile(IOMethods.class, ".tmp", true)
   * }</pre></blockquote>
   *
   * @return a {@code File} object for a new, empty file in the file system's temp
   *     directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempFile() throws IOException {
    return createTempFile(IOMethods.class);
  }

  /**
   * Creates a new, empty file in the file system's temp directory. Equivalent to:
   *
   * <blockquote><pre>{@code
   * createTempFile(requester ".tmp", true)
   * }</pre></blockquote>
   *
   * @param requester the class requesting the temp file (its simple name will
   *     become part of the file name)
   * @return a {@code File} object for a new, empty file in the file system's temp
   *     directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempFile(Class<?> requester) throws IOException {
    return createTempFile(requester, true);
  }

  /**
   * Creates a {@code File} object with a unique file name, located file system's
   * temp directory. Equivalent to:
   *
   * <blockquote><pre>{@code
   * createTempFile(requester "tmp", touch)
   * }</pre></blockquote>
   *
   * @param requester the class requesting the temp file (its simple name will
   *     become part of the file name)
   * @param touch whether to actually create the file on the file system
   * @return a {@code File} object for a new, empty file in the file system's temp
   *     directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempFile(Class<?> requester, boolean touch)
      throws IOException {
    return createTempFile(requester, ".tmp", touch);
  }

  /**
   * Creates a {@code File} object with a unique file name, located in file system's
   * temp directory.
   *
   * @param requester the class requesting the temp file (its simple name will
   *     become part of the file name)
   * @param extension the extension to append to the generated directory name. If
   *     empty or {@code null}, no extension will be appended to the file name.
   *     Otherwise the extension may or may not start with "." (the dot will be
   *     prepended if absent).
   * @param touch whether to actually create the file on the file system
   * @return a {@code File} object for a new, empty file in the file system's temp
   *     directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempFile(Class<?> requester,
      String extension,
      boolean touch)
      throws IOException {
    Check.notNull(requester, REQUESTER);
    return doCreateTempFile(requester, File.separator, extension, touch);
  }

  /**
   * Creates a {@code File} object with a unique file name, located in file system's
   * temp directory.
   *
   * @param requester the class requesting the temp directory (its simple name
   *     will become part of the file name)
   * @param relativePath any intermediate directories between the file system's
   *     temp directory and the file to be created. "/" means the file will be
   *     created directly underneath the file system's temp directory. Intermediate
   *     directories will be created if necessary. If the path does not start and/or
   *     end with "/" ({@link File#separator}), it will be prepended resp. appended.
   * @param extension the extension to append to the generated directory name. If
   *     empty or {@code null}, no extension will be appended to the directory name.
   *     Otherwise the extension may or may not start with "." (the dot will be
   *     prepended if absent).
   * @param touch whether to actually create the directory on the file system
   * @return a {@code File} object for a new, empty file in the file system's temp
   *     directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempFile(
      Class<?> requester, String relativePath, String extension, boolean touch)
      throws IOException {
    Check.notNull(requester, REQUESTER);
    Check.notNull(relativePath, Param.RELATIVE_PATH);
    return doCreateTempFile(requester, relativePath, extension, touch);
  }

  /**
   * Creates a new, empty directory under the file system's temp directory.
   * Equivalent to:
   *
   * <blockquote><pre>{@code
   * createTempFile(requester ".d", true)
   * }</pre></blockquote>
   *
   * @return a {@code File} object for a new, empty directory in the file system's
   *     temp directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempDir() throws IOException {
    return createTempDir(IOMethods.class);
  }

  /**
   * Creates a new, empty directory under the file system's temp directory.
   * Equivalent to:
   *
   * <blockquote><pre>{@code
   * createTempFile(requester, ".d", true)
   * }</pre></blockquote>
   *
   * @param requester the class requesting the temp file (its simple name will
   *     become part of the file name)
   * @return a {@code File} object for a new, empty file in the file system's temp
   *     directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempDir(Class<?> requester) throws IOException {
    return createTempDir(requester, true);
  }

  /**
   * Creates a {@code File} object with a unique file name, located file system's
   * temp directory. Equivalent to:
   *
   * <blockquote><pre>{@code
   * createTempDir(requester ".d", touch)
   * }</pre></blockquote>
   *
   * @param requester the class requesting the temp directory (its simple name
   *     will become part of the file name)
   * @param touch whether to actually create the directory on the file system
   * @return a {@code File} object for a new, empty directory in the file system's
   *     temp directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempDir(Class<?> requester, boolean touch)
      throws IOException {
    Check.notNull(requester, REQUESTER);
    return doCreateTempDir(requester, File.separator, ".d", touch);
  }

  /**
   * Creates a {@code File} object with a unique file name, located in file system's
   * temp directory. If {@code touch} equals {@code true}, the {@code File} object
   * will be used to create a directory underneath the temp directory.
   *
   * @param requester the class requesting the temp directory (its simple name
   *     will become part of the file name)
   * @param extension the extension to append to the generated directory name. If
   *     empty or {@code null}, no extension will be appended to the directory name.
   *     Otherwise the extension may or may not start with "." (the dot will be
   *     prepended if absent).
   * @param touch whether to actually create the directory on the file system
   * @return a {@code File} object for a new, empty directory in the file system's
   *     temp directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempDir(Class<?> requester,
      String extension,
      boolean touch)
      throws IOException {
    Check.notNull(requester, REQUESTER);
    return doCreateTempDir(requester, File.separator, extension, touch);
  }

  /**
   * Creates a {@code File} object with a unique file name, located in file system's
   * temp directory. If {@code touch} equals {@code true}, the {@code File} object
   * will be used to create a directory underneath the temp directory.
   *
   * @param requester the class requesting the temp directory (its simple name
   *     will become part of the file name)
   * @param relativePath any intermediate directories between the file system's
   *     temp directory and the directory to be created. "/" means the directory will
   *     be created directly underneath the file system's temp directory.
   *     Intermediate directories will be created if necessary. If the path does not
   *     start or end with "/" ({@link File#separator}), it will be prepended resp.
   *     appended.
   * @param extension the extension to append to the generated directory name. If
   *     empty or {@code null}, no extension will be appended to the directory name.
   *     Otherwise the extension may or may not start with "." (the dot will be
   *     prepended if absent).
   * @param touch whether to actually create the directory on the file system
   * @return a {@code File} object for a new, empty directory in the file system's
   *     temp directory
   * @throws IOException if an I/O error occurs
   */
  public static File createTempDir(
      Class<?> requester, String relativePath, String extension, boolean touch)
      throws IOException {
    Check.notNull(requester, REQUESTER);
    Check.notNull(relativePath, Param.RELATIVE_PATH);
    return doCreateTempDir(requester, relativePath, extension, touch);
  }

  /**
   * Deletes a file or directory. Directories need not be empty. If the file or
   * directory does not exist, this method returns quietly.
   *
   * @param path the path of the file/directory to be deleted
   */
  public static void rm(String path) {
    Path p = Check.notNull(path).ok(Path::of);
    if (Files.exists(p)) {
      try {
        rm(p);
      } catch (IOException e) {
        throw ExceptionMethods.uncheck(e);
      }
    }
  }

  /**
   * Deletes a file or directory. Directories need not be empty. If the file or
   * directory does not exist, this method returns quietly.
   *
   * @param file The file or directory to delete.
   */
  public static void rm(File file) {
    Path p = Check.notNull(file).ok(File::toPath);
    if (Files.exists(p)) {
      try {
        rm(p);
      } catch (IOException e) {
        throw ExceptionMethods.uncheck(e);
      }
    }
  }

  private static void rm(Path p) throws IOException {
    Files.walkFileTree(
        p,
        new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc)
              throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
          }
        });
  }

  private static synchronized File doCreateTempFile(
      Class<?> requester, String relPath, String ext, boolean touch)
      throws IOException {
    String path = uniquePath(requester, relPath, ext);
    File file = new File(path);
    if (touch) {
      if (!relPath.isEmpty() && !relPath.equals(File.separator)) {
        File dir = Path.of(tmpDir(), relPath).toFile();
        dir.mkdirs();
      }
      Check.on(IO, file.createNewFile()).is(yes(),
          "Unable to create temp file \"{arg}\"");
    }
    return file;
  }

  private static synchronized File doCreateTempDir(
      Class<?> requester, String relPath, String ext, boolean touch)
      throws IOException {
    String path = uniquePath(requester, relPath, ext);
    File dir = new File(path);
    if (touch) {
      Check.on(IO, dir.mkdirs()).is(yes(), "Unable to create temp dir \"{arg}\"");
    }
    return dir;
  }

  private static final AtomicLong counter = new AtomicLong();

  private static String uniquePath(Class<?> requester,
      String relativePath,
      String extension) {
    String sep = File.separator;
    relativePath = ensureSuffix(ensurePrefix(relativePath, sep), sep);
    if (isEmpty(extension)) {
      extension = EMPTY_STRING;
    } else {
      extension = ensurePrefix(extension, DOT);
    }
    return tmpDir()
        + relativePath
        + requester.getSimpleName()
        + DOT
        + Thread.currentThread().getName()
        + DOT
        + System.currentTimeMillis()
        + DOT
        + lpad(counter.incrementAndGet(), 8, '0')
        + extension;
  }

  private static String tmpDir() {
    return System.getProperty("java.io.tmpdir");
  }

}
