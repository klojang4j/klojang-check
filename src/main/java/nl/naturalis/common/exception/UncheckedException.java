package nl.naturalis.common.exception;

import nl.naturalis.common.ExceptionMethods;
import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * A {@code RuntimeException} that behaves just like {@link Exception} it wraps. it
 * overrides <i>all</i> methods from {@code Exception} by delegating to the same
 * method on the wrapped exception. For example, {@link #getCause()} does not return
 * the wrapped exception but the cause of the wrapped exception. You <i>can</i>
 * provide a custom message, specific to the {@code UncheckedException} itself.
 * However, it must be retrieved via {@link #getCustomMessage()} (as
 * {@link #getMessage()} would return the message of the wrapped exception).
 *
 * <p>This behaviour can be useful when wrapping checked exceptions that in
 * practice cannot sensibly be dealt with. This is often the case with, for example,
 * {@code IOException}, {@code SQLException} and other exceptions where the Javadocs
 * state that they are thrown "when something goes wrong". These exceptions are
 * runtime exceptions for all practical purposes.
 *
 * <p>By hiding completely behind the wrapped exception, an
 * {@code UncheckedException} has a less cumbersome stack trace than a straight
 * {@code RuntimeException}. Beware of surprises though, as the only way of knowing
 * you are dealing with an {@code UncheckedException} is by calling
 * {@code getClass()} on it.
 *
 * <p>An {@code UncheckedException} can be safely wrapped into an
 * {@code UncheckedException}. The constructors "bore through" the causes until they
 * find something that is not an {@code UncheckedException} and not a
 * {@link RootException}.
 *
 * @author Ayco Holleman
 * @see RootException
 * @see ExceptionMethods#uncheck(Throwable)
 */
public final class UncheckedException extends RuntimeException {

  static Throwable peal(Throwable t) {
    do {
      if (t instanceof UncheckedException ue) {
        t = ue.unwrap();
      } else if (t instanceof RootException re) {
        t = re.unwrap();
      } else {
        break;
      }
    } while (true);
    return t;
  }

  private final String msg;

  /**
   * Creates an {@code UncheckedException} wrapping the provided {@code Throwable}.
   *
   * @param cause the exception to wrap and mask
   */
  public UncheckedException(Throwable cause) {
    super(Check.notNull(cause, "cause").ok(UncheckedException::peal));
    this.msg = null;
  }

  /**
   * Creates an {@code UncheckedException} with the provided custom message, wrapping
   * the provided {@code Throwable}.
   *
   * @param message a custom message
   * @param cause the exception to wrap and mask
   */
  public UncheckedException(String message, Throwable cause) {
    super(Check.notNull(cause, "cause").ok(UncheckedException::peal));
    this.msg = Check.notNull(message, Param.MESSAGE).ok();
  }

  /**
   * Returns the exception wrapped by this {@code UncheckedException}. Note that
   * {@link #getCause()} does <i>not</i> return that exception. It returns the cause
   * of the cause.
   *
   * @param <E> the type of the exception
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
    return Optional.ofNullable(msg);
  }

  /**
   * Calls {@code getMessage()} on the wrapped exception
   *
   * @return the result of calling {@code getMessage()} on the wrapped exception
   */
  @Override
  public String getMessage() {
    return super.getCause().getMessage();
  }

  /**
   * Calls {@code getLocalizedMessage()} on the wrapped exception
   *
   * @return the result of calling {@code getLocalizedMessage()} on the wrapped
   *     exception
   */
  @Override
  public String getLocalizedMessage() {
    return super.getCause().getLocalizedMessage();
  }

  /**
   * Calls {@code getCause()} on the wrapped exception.
   *
   * @return the result of calling {@code getCause()} on the wrapped exception
   */
  @Override
  public synchronized Throwable getCause() {
    return super.getCause().getCause();
  }

  /**
   * Calls {@code printStackTrace()} on the wrapped exception
   */
  @Override
  public void printStackTrace() {
    super.getCause().printStackTrace();
  }

  /**
   * Calls {@code printStackTrace()} on the wrapped exception
   */
  @Override
  public void printStackTrace(PrintStream s) {
    super.getCause().printStackTrace(s);
  }

  /**
   * Calls {@code printStackTrace} on the wrapped exception.
   */
  @Override
  public void printStackTrace(PrintWriter s) {
    super.getCause().printStackTrace(s);
  }

  /**
   * Calls {@code getStackTrace()} on the wrapped exception
   *
   * @return the result of calling {@code getStackTrace()} on the wrapped exception
   */
  @Override
  public StackTraceElement[] getStackTrace() {
    return super.getCause().getStackTrace();
  }

  /**
   * Calls {@code toString()} on the wrapped exception.
   *
   * @return the result of calling {@code toString()} on the wrapped exception
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
