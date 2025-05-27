package org.klojang.check.extra;

import org.klojang.check.Check;
import org.klojang.check.fallible.FallibleConsumer;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A simple value container that explicitly allows the value to be {@code null}. This class is meant to be
 * used as the return value of methods that would otherwise return {@code null} <b>both</b> as the legitimate
 * outcome of a computation <b>and</b> as a signal that the computation yielded no result. The
 * {@link java.util.HashMap HashMap} class is a well-known example. If its {@code get()} method returns
 * {@code null}, it is not clear whether the requested key was absent, or whether it was present, but
 * associated with value {@code null}. If you wanted to create a {@code Map} implementation where this
 * distinction is clear, you could use the {@code Result} class and return {@link Result#notAvailable()} if
 * the requested key was absent, and {@code Result.of(null)} (or {@link Result#nullResult()}) if present but
 * {@code null}.
 *
 * @param <T> the type of the result value
 */
public final class Result<T> {

  private static final Result<?> NULL = new Result<>(null);
  private static final Result<?> NONE = new Result<>(new Object());

  /**
   * Returns a {@code Result} containing the specified value (possibly {@code null}).
   *
   * @param value The value
   * @param <T> The type of the result value
   * @return a {@code Result} containing the specified value
   */
  @SuppressWarnings("unchecked")
  public static <T> Result<T> of(T value) {
    if (value == null) {
      return (Result<T>) NULL;
    }
    return new Result<>(value);
  }

  /**
   * Returns a special {@code Result} instance indicating the absence of a result.
   *
   * @param <T> the type of the result value
   * @return a special {@code Result} object indicating the absence of a result
   */
  @SuppressWarnings("unchecked")
  public static <T> Result<T> notAvailable() {
    return (Result<T>) NONE;
  }

  /**
   * Returns a {@code Result} containing {@code null}.
   *
   * @param <T> the type of the result value
   * @return a {@code Result} containing {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <T> Result<T> nullResult() {
    return (Result<T>) NULL;
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
   * and the result value was not {@code null}.
   *
   * @return {@code true} if a result could be computed and it was a non-{@code null} result
   */
  public boolean isUnavailableOrNull() {
    return this == NONE || this == NULL;
  }

  /**
   * Returns {@code true} if the operation that produced this {@code Result} successfully computed the result
   * and the result value was {@code null}.
   *
   * @return {@code true} if a result could be computed, and it turned out to be {@code null}
   */
  public boolean isAvailableAndNull() {
    return this == NULL;
  }

  /**
   * Returns {@code true} if the operation that produced this {@code Result} successfully computed the result
   * and the result value was not {@code null}.
   *
   * @return {@code true} if a result could be computed and it was a non-{@code null} result
   */
  public boolean isAvailableAndNotNull() {
    return this != NONE && this != NULL;
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
   * Returns this {@code Result} if available, else the provided {@code Result}.
   *
   * @param supplier the value to return if this {@code Result} is {@link Result#notAvailable()}.
   * @return this instance's value if available; else the value provided by the specified {@code Supplier};
   * @throws IllegalArgumentException if the specified {@code Result} is {@code Result.notAvailable()}
   */
  public T orElseGet(Supplier<T> supplier) throws IllegalArgumentException {
    Check.notNull(supplier);
    return isAvailable() ? val : supplier.get();
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
    return obj instanceof Result<?> that && Objects.equals(this.val, that.val);
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
