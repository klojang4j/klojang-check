package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.common.exception.ExceptionOrigin;
import nl.naturalis.common.exception.RootException;
import nl.naturalis.common.exception.UncheckedException;
import nl.naturalis.common.x.Param;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Methods related to exception handling.
 *
 * @author Ayco Holleman
 */
public final class ExceptionMethods {

  private ExceptionMethods() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the root cause of the specified {@code Throwable}, or the
   * {@code Throwable} itself if it has no cause.
   *
   * @param exc the exception whose root cause to retrieve
   * @return the root cause of the exception
   */
  public static Throwable getRootCause(Throwable exc) {
    Check.notNull(exc, Param.EXCEPTION);
    while (exc.getCause() != null) {
      exc = exc.getCause();
    }
    return exc;
  }

  /**
   * Returns the stack trace of the root cause of the specified {@code Throwable} as
   * a {@code String}.
   *
   * @param exc the exception
   * @return the root stack trace as a string
   */
  public static String getRootStackTraceAsString(Throwable exc) {
    Check.notNull(exc, Param.EXCEPTION);
    ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
    getRootCause(exc).printStackTrace(new PrintStream(out));
    return out.toString(UTF_8).strip();
  }

  /**
   * Returns the exception message and stack trace of the root cause of the specified
   * {@code Throwable}, using the specified string(s) to filter stack trace elements.
   * The stack trace is filtered using a simple
   * {@link String#contains(CharSequence) String.contains} on the
   * {@link StackTraceElement#getClassName() className} property of the
   * {@code StackTraceElement}.
   *
   * @param exc the exception
   * @param filter one or more filters on stack trace elements
   * @return the root stack trace as a string
   */
  public static String getRootStackTraceAsString(Throwable exc, String... filter) {
    Check.notNull(exc, Param.EXCEPTION);
    Check.notNull(filter, Param.FILTER);
    ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
    PrintStream pw = new PrintStream(out);
    Throwable t = getRootCause(exc);
    pw.println(t);
    for (StackTraceElement ste : t.getStackTrace()) {
      for (String f : filter) {
        if (ste.getClassName().contains(f)) {
          pw.println("\tat " + ste);
          break;
        }
      }
    }
    return out.toString(UTF_8).strip();
  }

  /**
   * Returns the stack trace of the root cause of the specified exception, using the
   * specified string(s) to filter stack trace elements. If the
   * {@link StackTraceElement#getClassName() class name} of the stack trace element
   * {@link String#contains(CharSequence) contains} the filter string, the stack
   * trace element will be included in the returned array.
   *
   * @param exc the exception
   * @param filter One or more filters on stack trace elements
   * @return the root stack trace
   */
  public static StackTraceElement[] getRootStackTrace(Throwable exc,
      String... filter) {
    Check.notNull(exc, Param.EXCEPTION);
    Check.notNull(filter, Param.FILTER);
    if (filter.length == 0) {
      return getRootCause(exc).getStackTrace();
    }
    var trace = new ArrayList<StackTraceElement>();
    for (StackTraceElement ste : getRootCause(exc).getStackTrace()) {
      for (String f : filter) {
        if (ste.getClassName().contains(f)) {
          trace.add(ste);
          break;
        }
      }
    }
    return trace.toArray(StackTraceElement[]::new);
  }

  /**
   * Provides a detailed exception message that includes the class, method and line
   * number of the first statement in the specified exception's stack trace that
   * matches the search string. Note that this may not be the <i>absolute</i> origin
   * of the exception - the statement from which the exception was thrown. If the
   * search term does happen to hit upon the absolute origin of the exception, the
   * detailed exception message will include a notification to that effect. Otherwise
   * the class, method and line number of the statement that did throw the exception
   * is also included in the detailed exception message.
   *
   * <blockquote><pre>{@code
   * try {
   *   // stuff ...
   * } catch (Exception e) {
   *   // Log exception message plus class and line number within the
   *   // com.mycompany code base where things flew off the rails
   *   logger.error(new ExceptionOrigin(e, "com.mycompany").getDetailedMessage());
   * }
   * }</pre></blockquote>
   *
   * @param exc the exception to extract the extra information from
   * @param search The (partial) name of the package or class you want to zoom in
   *     on
   * @return A detailed exception message
   * @see ExceptionOrigin#getDetailedMessage()
   */
  public static String getDetailedMessage(Throwable exc, String search) {
    Check.notNull(exc, Param.EXCEPTION);
    return new ExceptionOrigin(exc, search).getDetailedMessage();
  }

  /**
   * Returns the specified throwable if it already is a {@code RuntimeException},
   * else a {@code RuntimeException} wrapping the throwable.
   *
   * @param exc a checked or unchecked exception
   * @return the specified throwable or a {@code RuntimeException} wrapping it
   */
  public static RuntimeException wrap(Throwable exc) {
    if (Check.notNull(exc, Param.EXCEPTION).ok() instanceof RuntimeException rte) {
      return rte;
    }
    return new RuntimeException(exc);
  }

  /**
   * Returns the specified throwable if it already is a {@code RuntimeException},
   * else a {@code RuntimeException} wrapping the throwable.
   *
   * @param exc a checked or unchecked exception
   * @param customMessage a custom message passed on to the
   *     {@code RuntimeException} wrapping the original exception
   * @param msgArgs the {@code String.format} message arguments to the custom
   *     message
   * @return the specified throwable or a {@code RuntimeException} wrapping it
   */
  public static RuntimeException wrap(Throwable exc,
      String customMessage,
      Object... msgArgs) {
    Check.notNull(exc, Param.EXCEPTION);
    Check.notNull(customMessage, "customMessage");
    Check.notNull(msgArgs, Param.MSG_ARGS);
    if (exc instanceof RuntimeException rte) {
      return rte;
    }
    if (msgArgs.length == 0) {
      return new RuntimeException(customMessage, exc);
    }
    return new RuntimeException(String.format(customMessage, msgArgs), exc);
  }

  /**
   * Returns the specified throwable if it already is a {@code RuntimeException},
   * else a {@code RuntimeException} produced by the specified function.
   *
   * @param <T> The type of the {@code RuntimeException}
   * @param exc the exception to be wrapped if it is not a
   *     {@code RuntimeException}
   * @param exceptionFactory the producer of the {@code RuntimeException},
   *     typically the one-arg constructor of an {@code Exception} that takes a
   *     {@code Throwable} argument ("cause")
   * @return the specified throwable or a {@code RuntimeException} wrapping it
   */
  public static <T extends RuntimeException> RuntimeException wrap(
      Throwable exc, Function<Throwable, T> exceptionFactory) {
    Check.notNull(exc, Param.EXCEPTION);
    Check.notNull(exceptionFactory, "exceptionFactory");
    if (exc instanceof RuntimeException rte) {
      return rte;
    }
    return exceptionFactory.apply(exc);
  }

  /**
   * Returns the specified throwable if it already is a {@code RuntimeException},
   * else a {@code RuntimeException} produced by the specified function. For
   * example:
   *
   * <blockquote><pre>{@code
   * try {
   *  // stuff ...
   * } catch(Throwable t) {
   *  throw ExceptionMethods.wrap(t, "Bad stuff happening", IllegalStateException::new);
   * }
   * }</pre></blockquote>
   *
   * @param <T> The type of the {@code RuntimeException}
   * @param exception the exception to be wrapped if it is not a
   *     {@code RuntimeException}
   * @param exceptionFactory the producer of the {@code RuntimeException},
   *     typically the two-argument constructor of an {@code Exception} that takes a
   *     {@code String} argument and a {@code Throwable} argument
   * @param customMessage a custom message passed on to the
   *     {@code RuntimeException} wrapping the original exception
   * @param msgArgs the {@code String.format} message arguments to the custom
   *     message
   * @return the specified throwable or a {@code RuntimeException} wrapping it
   */
  public static <T extends RuntimeException> RuntimeException wrap(
      Throwable exception,
      BiFunction<String, Throwable, T> exceptionFactory,
      String customMessage,
      Object... msgArgs) {
    Check.notNull(exception, Param.EXCEPTION);
    if (exception instanceof RuntimeException rte) {
      return rte;
    }
    Check.notNull(customMessage, "customMessage");
    Check.notNull(exceptionFactory, "exceptionFactory");
    if (msgArgs.length == 0) {
      return exceptionFactory.apply(customMessage, exception);
    }
    return exceptionFactory.apply(String.format(customMessage, msgArgs), exception);
  }

  /**
   * Returns the specified throwable if it already is a {@code RuntimeException},
   * else an {@link UncheckedException} wrapping the throwable. This method is
   * primarily meant to "uncheck" checked exceptions that you cannot in practice
   * properly deal with, and are therefore, for all practical purposes, a runtime
   * exception. For example an {@code IOException} which is documented as being
   * thrown "if an I/O error occurs".
   *
   * @param exc a checked or unchecked exception
   * @return the provided {@code Throwable} or an {@code UncheckedException} wrapping
   *     it
   * @see UncheckedException
   */
  public static RuntimeException uncheck(Throwable exc) {
    return wrap(exc, UncheckedException::new);
  }

  /**
   * Returns the specified throwable if it already is a {@code RuntimeException},
   * else an {@link UncheckedException} wrapping the throwable. This method is
   * primarily meant to "uncheck" checked exceptions that you cannot in practice
   * properly deal with, and are therefore, for all practical purposes, a runtime
   * exception. For example an {@code IOException} which is documented as being
   * thrown "if an I/O error occurs".
   *
   * @param exc a checked or unchecked exception
   * @param customMessage a custom message to pass to the constructor of
   *     {@code UncheckedException}
   * @return the provided {@code Throwable} or an {@code UncheckedException} wrapping
   *     it
   * @see UncheckedException
   */
  public static RuntimeException uncheck(Throwable exc, String customMessage) {
    return wrap(exc, UncheckedException::new, customMessage);
  }

  /**
   * Returns the specified throwable if it already is a {@code RuntimeException},
   * else an {@link RootException} exception wrapping the <i>root cause</i>  of the
   * provided exception.
   *
   * @param exc a checked or unchecked exception
   * @return the provided {@code Throwable} or an {@code UncheckedException} wrapping
   *     it
   * @see RootException
   */
  public static RuntimeException rootCause(Throwable exc) {
    return wrap(exc, RootException::new);
  }

  /**
   * Returns the specified throwable if it already is a {@code RuntimeException},
   * else an {@link RootException} exception wrapping the <i>root cause</i> of the
   * provided exception.
   *
   * @param exc a checked or unchecked exception
   * @param customMessage a custom message to pass to the constructor of
   *     {@code UncheckedException}
   * @return the provided {@code Throwable} or a {@code RootException} wrapping its
   *     root cause
   * @see RootException
   */
  public static RuntimeException rootCause(Throwable exc, String customMessage) {
    return wrap(exc, RootException::new, customMessage);
  }

}
