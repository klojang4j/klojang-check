package nl.naturalis.common.exception;

import nl.naturalis.check.Check;
import nl.naturalis.common.ExceptionMethods;
import nl.naturalis.common.x.Param;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Optional;

import static nl.naturalis.common.ExceptionMethods.getRootCause;
import static nl.naturalis.common.exception.UncheckedException.peal;

/**
 * A {@code RuntimeException} that behaves as though it is the root cause of the
 * provided exception. It wraps the root cause of the provided exception rather than
 * the provided exception itself. It overrides <i>all</i> methods from
 * {@code Exception} by delegating to the same method on the root cause. For example,
 * {@link #getCause()} calls {@code getCause()} on the root cause, and thus returns
 * {@code null}. This makes the stack trace of a {@code RootException} very small and
 * informative, at the cost of not knowing how the original exception (the root
 * cause) bubbled up.
 *
 * @author Ayco Holleman
 * @see UncheckedException
 * @see ExceptionMethods#getRootCause(Throwable)
 */
public final class RootException extends RuntimeException {

  private final Optional<String> msg;

  /**
   * Creates a {@code RootException} wrapping the root cause of the provided
   * {@code Exception}.
   *
   * @param cause the exception whose root cause to wrap
   */
  public RootException(Throwable cause) {
    super(rootOf(cause));
    this.msg = Optional.empty();
  }

  /**
   * Creates a {@code RootException} with a custom message.
   *
   * @param message a custom message
   * @param cause the exception to wrap
   */
  public RootException(String message, Throwable cause) {
    super(rootOf(cause));
    this.msg = Check.notNull(message, Param.MESSAGE).ok(Optional::of);
  }

  private static Throwable rootOf(Throwable t) {
    return Check.notNull(t, "cause").ok(x -> getRootCause(peal(x)));
  }

  /**
   * Returns the exception wrapped by this {@code RootException}. Note that
   * {@code getCause()} does <i>not</i> return that exception. It returns the
   * <i>cause</i> of the root cause (i.e. {@code null}).
   *
   * @param <E> The type of the exception
   * @return the exception directly wrapped by this {@code UncheckedException}
   */
  @SuppressWarnings("unchecked")
  public <E extends Throwable> E unwrap() {
    return (E) super.getCause();
  }

  /**
   * Returns an {@code Optional} containing the custom message passed in through the
   * two-arg constructor, or an empty {@code Optional} if the single-arg constructor
   * was used.
   *
   * @return an {@code Optional} containing the custom message passed in through the
   *     constructor
   */
  public Optional<String> getCustomMessage() {
    return msg;
  }

  /**
   * Calls {@code getMessage()} on the root cause.
   *
   * @return the result of calling {@code getMessage()} on the root cause
   */
  @Override
  public String getMessage() {
    return super.getCause().getMessage();
  }

  /**
   * Calls {@code getLocalizedMessage()} on the root cause.
   *
   * @return the result of calling {@code getLocalizedMessage()} on the root cause
   */
  @Override
  public String getLocalizedMessage() {
    return super.getCause().getLocalizedMessage();
  }

  /**
   * Calls {@code getCause()} on the root exception.
   *
   * @return the result of calling {@code getCause()} on the root exception, so
   *     {@code null}!
   */
  @Override
  public synchronized Throwable getCause() {
    return super.getCause().getCause();
  }

  /**
   * Calls {@code printStackTrace()} on the root exception.
   */
  @Override
  public void printStackTrace() {
    super.getCause().printStackTrace();
  }

  /**
   * Calls {@code printStackTrace()} on the root exception.
   */
  @Override
  public void printStackTrace(PrintStream s) {
    super.getCause().printStackTrace(s);
  }

  /**
   * Calls {@code printStackTrace()} on the wrapped exception
   */
  @Override
  public void printStackTrace(PrintWriter s) {
    super.getCause().printStackTrace(s);
  }

  /**
   * Calls {@code getStackTrace()} on the root exception.
   *
   * @return the result of calling {@code getStackTrace()} on the root exception
   */
  @Override
  public StackTraceElement[] getStackTrace() {
    return super.getCause().getStackTrace();
  }

  /**
   * Calls {@code toString()} on the root exception.
   *
   * @return the result of calling {@code toString()} on the root exception
   */
  @Override
  public String toString() {
    return super.getCause().toString();
  }

  /**
   * Throws an {@code UnsupportedOperationException}.
   */
  @Override
  public synchronized Throwable initCause(Throwable cause) {
    throw new UnsupportedOperationException();
  }

  /**
   * Throws an {@code UnsupportedOperationException}.
   */
  @Override
  public void setStackTrace(StackTraceElement[] stackTrace) {
    throw new UnsupportedOperationException();
  }

}
