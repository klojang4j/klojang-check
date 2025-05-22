package org.klojang.check.extra;

import org.klojang.check.Check;
import org.klojang.check.CommonChecks;
import org.klojang.check.fallible.FallibleConsumer;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

import static org.klojang.check.CommonChecks.deepNotEmpty;

/**
 * A simple value container that explicitly allows the value to be {@code null}. This class is meant to be
 * used as the return value of methods that would otherwise return {@code null} <b>both</b> as the legitimate
 * outcome of a computation <b>and</b> as a signal that the computation yielded no result. The
 * {@link java.util.HashMap HashMap} class is a well-known example. If its {@code get()} method returns
 * {@code null}, it is not clear whether the requested key was absent, or whether it was present, but
 * associated with value {@code null}. If you wanted to create a {@code Map} implementation where this
 * distinction is clear, you could use the {@code Result} class and return {@link Result#notAvailable()} if
 * the requested key was absent, and {@code Result.of(null)} if resent but {@code null}.
 *
 * @param <T> the type of the result value
 */
public final class Result<T> implements Emptyable {

  private static final Result<?> NONE = new Result<>(null);

  /**
   * Returns a {@code Result} containing the specified value (possibly {@code null}).
   *
   * @param value The value
   * @param <T> The type of the result value
   * @return a {@code Result} containing the specified value
   */
  public static <T> Result<T> of(T value) {
    return new Result<>(value);
  }

  /**
   * Returns a special {@code Result} instance signifying the absence of a result.
   *
   * @param <T> the type of the result value
   * @return a special {@code Result} object signifying the absence of a result
   */
  @SuppressWarnings("unchecked")
  public static <T> Result<T> notAvailable() {
    return (Result<T>) NONE;
  }

  private final T val;

  private Result(T value) {
    this.val = value;
  }

  /**
   * Returns the result. You should have established first that a result value
   * {@linkplain #isAvailable() is available} or a {@link NoSuchElementException} will be thrown.
   *
   * @return the value
   * @throws NoSuchElementException if no result is available
   */
  public T get() throws NoSuchElementException {
    if (this != NONE) {
      return val;
    }
    throw noResult();
  }

  /**
   * Returns {@code true} if the operation that produced this {@code Result} successfully computed the result.
   * If so, the result value can be retrieved via the {@link #get()} method. If not, calling {@code get()}
   * method will result in a {@link NoSuchElementException}.
   *
   * @return {@code true} if a result could be computed
   */
  public boolean isAvailable() {
    return this != NONE;
  }

  /**
   * Returns {@code true} if the operation that produced this {@code Result} could not compute a proper
   * result.
   *
   * @return {@code true} if the operation that produced this {@code Result} could not compute a proper result
   */
  public boolean isUnavailable() {
    return this == NONE;
  }

  /**
   * Returns {@code true} if the operation that produced this {@code Result} successfully computed the result
   * and the result value was {@code null}.
   *
   * @return {@code true} if a result could be computed, and it turned out to be {@code null}
   */
  public boolean isAvailableAndNull() {
    return this != NONE && val == null;
  }

  /**
   * Returns {@code true} if the operation that produced this {@code Result} successfully computed the result
   * and the result value was not {@code null}.
   *
   * @return {@code true} if a result could be computed and it was a non-{@code null} result
   */
  public boolean isAvailableAndNotNull() {
    return this != NONE && val != null;
  }

  /**
   * If available, passes the result to the specified consumer; else does nothing.
   *
   * @param consumer the consumer of the result
   * @param <X> the type of the exception thrown by the consumer
   * @throws X if the consumer experiences an error
   */
  public <X extends Throwable> void ifAvailable(FallibleConsumer<T, X> consumer) throws X {
    Check.notNull(consumer);
    if (isAvailable()) {
      consumer.accept(val);
    }
  }

  /**
   * Returns the result value, if available, else the provided default value.
   *
   * @param defaultValue the default value
   * @return the result value, if available, else the provided default value
   */
  public T orElse(T defaultValue) {
    return isAvailable() ? val : defaultValue;
  }

  /**
   * Returns this {@code Result} if it contains a proper result value (possibly {@code null}), else the
   * provided {@code Result}.
   *
   * @param alternative the value to return if this {@code Result} is {@link Result#notAvailable()}.
   * @return this instance's value if available; else the value provided by the specified {@code Supplier};
   * @throws IllegalArgumentException if the specified {@code Result} is {@code Result.notAvailable()}
   */
  public T orElaseGet(Supplier<T> alternative) throws IllegalArgumentException {
    Check.notNull(alternative);
    return isAvailable() ? val : alternative.get();
  }

  /**
   * Returns {@code true} if no result is available <i>or</i> if the result value is empty as per the
   * {@link CommonChecks#empty() empty()} test.
   *
   * @return {@code true} if no result is available or the result value is empty.
   */
  @Override
  public boolean isEmpty() {
    return this == NONE;
  }

  /**
   * Returns {@code true} if a result is available <i>and</i> the result value is recursively non-empty as per
   * the {@link CommonChecks#deepNotEmpty() deepNotEmpty()} test.
   *
   * @return {@code true} if a result is available and is deep-not-empty
   */
  @Override
  public boolean isDeepNotEmpty() {
    return this != NONE && deepNotEmpty().test(val);
  }

  /**
   * Returns {@code true} if the specified object is a {@code Result} that either is
   * <i>this</i> {@code Result} or contains the same value.
   *
   * @param obj the object to compare this instance with
   * @return whether this instance equals the specified object.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    return this != NONE
        && obj != NONE
        && obj instanceof Result<?> that
        && Objects.equals(this.val, that.val);
  }

  /**
   * Returns the hashcode of the value contained in this {@code Result}, or 0 if no result was available.
   *
   * @return the hashcode of the value contained in this {@code Result}, or 0 if no result was available
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(val);
  }

  /**
   * Returns a string representation analogous to the one provided by {@link java.util.Optional}.
   *
   * @return a string representation analogous to the one provided by {@link java.util.Optional}
   */
  @Override
  public String toString() {
    return this != NONE ? String.format("Result[%s]", val) : "Result.notAvailable";
  }

  private static NoSuchElementException noResult() {
    return new NoSuchElementException("no result available");
  }

}
