package nl.naturalis.common.exception;

import nl.naturalis.check.Check;
import nl.naturalis.common.ExceptionMethods;

import static nl.naturalis.common.ExceptionMethods.getRootCause;
import static nl.naturalis.common.ObjectMethods.isEmpty;
import static nl.naturalis.common.StringMethods.*;

/**
 * Provides detailed information about the origin of an exception. Useful for tracing
 * back an exception to a statement within some code base (e.g. your own). Example:
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
 * @see ExceptionMethods#getDetailedMessage(Throwable, String)
 */
public final class ExceptionOrigin {

  private final Throwable exc;
  private final String search;
  private final StackTraceElement ste;

  /**
   * Creates a new {@code ExceptionOrigin} for the provided exception, searching its
   * stack trace for an element matching the search string. Matching happens through
   * a simple {@link String#contains(CharSequence) String.contains} on the
   * fully-qualified class name.
   *
   * @param exc the exception to analyze
   * @param search any part of the package name or class name that you want the
   *     exception to be traced back to.
   */
  public ExceptionOrigin(Throwable exc, String search) {
    this.exc = Check.notNull(exc, "exc").ok();
    this.search = Check.notNull(search, "search").ok();
    if (isEmpty(exc.getStackTrace())) {
      this.ste = null;
    } else {
      this.ste = search(exc, search);
    }
  }

  /**
   * Provides a detailed exception message that includes the class, method and line
   * number of the first statement in the stack trace that matches the search string.
   * Note that this may not be the <i>absolute</i> origin of the exception - the
   * statement from which the exception was thrown. If the search term does happen to
   * hit upon the absolute origin of the exception, the detailed exception message
   * will include a notification to that effect. Otherwise the class, method and line
   * number of the statement that did throw the exception is also included in the
   * detailed exception message.
   *
   * @return A detailed exception message
   */
  public String getDetailedMessage() {
    StringBuilder sb = new StringBuilder(200);
    append(sb, exc.getClass().getName(), " *** ");
    if (isEmpty(exc.getStackTrace())) {
      sb.append(" (no stack trace available)");
    } else if (ste == null) {
      append(sb, " (not originating from \"", search, "\")");
    } else {
      append(sb,
          " at ",
          ste.getClassName(),
          ".",
          ste.getMethodName(),
          " (line ",
          ste.getLineNumber(),
          ")");
    }
    if (isBlank(exc.getMessage())) {
      sb.append(" *** [no exception message available]");
    } else {
      append(sb, " *** ", exc.getMessage().strip());
    }
    StackTraceElement origin = getAbsoluteOrigin();

    if (ste == origin) {
      sb.append(" *** absolute origin of exception: yes!");
    } else if (origin != null) {
      append(sb,
          " *** absolute origin of exception: ",
          origin.getClassName(),
          ".",
          origin.getMethodName(),
          " (line ",
          origin.getLineNumber(),
          ")");

    }
    return sb.toString();
  }

  /**
   * Returns {@link #getDetailedMessage()}.
   *
   * @return {@code getDetailedMessage()}
   */
  @Override
  public String toString() {
    return getDetailedMessage();
  }

  /**
   * Returns the exception wrapped by this {@code ExceptionOrigin}.
   *
   * @return the exception wrapped by this {@code ExceptionOrigin}
   */
  public Throwable getException() {
    return exc;
  }

  /**
   * Returns the first stack trace element matching the search string.
   *
   * @return the first stack trace element matching the search string
   */
  public StackTraceElement geStackTraceElement() {
    return ste;
  }

  /**
   * Returns the module in which the exception occurred or {@code null} if the
   * exception came without a stack trace.
   *
   * @return the module in which the exception occurred or {@code null} if the
   *     exception came without a stack trace
   */
  public String getModule() {
    return ste == null ? null : ste.getModuleName();
  }

  /**
   * Returns the class in which the exception occurred or {@code null} if the
   * exception came without a stack trace.
   *
   * @return the class in which the exception occurred or {@code null} if the
   *     exception came without a stack trace
   */
  public String getClassName() {
    return ste == null ? null : ste.getClassName();
  }

  /**
   * Returns the method in which the exception occurred or {@code null} if the
   * exception came without a stack trace.
   *
   * @return the method in which the exception occurred or {@code null} if the
   *     exception came without a stack trace
   */
  public String getMethod() {
    return ste == null ? null : ste.getMethodName();
  }

  /**
   * Returns the line at which the exception occurred or -1 if the exception came
   * without a stack trace.
   *
   * @return the line at which the exception occurred or -1 if the exception came
   *     without a stack trace
   */
  public int getLine() {
    return ste == null ? -1 : ste.getLineNumber();
  }

  /**
   * Returns {@code true} if the stack trace element found using the search term
   * represents the absolute origin of the exception. That is, whether it
   * encapsulates the point at which the exception was thrown.
   *
   * @return {@code true} if the stack trace element found using the search term
   *     represents the absolute origin of the exception
   */
  public boolean isAbsoluteOrigin() {
    Throwable root = getRootCause(exc);
    if (isEmpty(root.getStackTrace())) {
      return false;
    }
    return root.getStackTrace()[0] == ste;
  }

  private static StackTraceElement search(Throwable exc, String search) {
    for (Throwable t = exc; t != null; t = t.getCause()) {
      StackTraceElement[] trace = t.getStackTrace();
      if (!isEmpty(trace)) {
        for (StackTraceElement ste : trace) {
          if (ste.getClassName().contains(search)) {
            return ste;
          }
        }
      }
    }
    return null;
  }

  private StackTraceElement getAbsoluteOrigin() {
    Throwable root = getRootCause(exc);
    if (isEmpty(root.getStackTrace())) {
      return null;
    }
    return root.getStackTrace()[0];
  }

}
