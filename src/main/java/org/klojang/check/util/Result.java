package org.klojang.check.util;

import static org.klojang.check.CommonChecks.deepNotEmpty;
import static org.klojang.check.CommonChecks.empty;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.klojang.check.CommonChecks;
import org.klojang.check.fallible.FallibleConsumer;

/**
 * A value container where the value is explicitly allowed to be {@code null}. This
 * class is meant to be used as the return value of methods that would otherwise
 * return {@code null} as the result of a computation, but also if the computation
 * yielded no result. The {@link java.util.HashMap} class is a prime example. If its
 * {@code get} method returns {@code null}, you still don't know whether the
 * requested key was absent, or whether it was present, but associated with value
 * {@code null}.
 *
 * <p>Another scenario (and one that we can control) would be iterating over an
 * array and returning a particular element, if found. If the element can itself
 * legitimately be {@code null}, it is not clear any longer what a return value of
 * {@code null} actually means: not present or "really" {@code null}. Using the
 * {@code Result} class, you would return a {@code Result} containing {@code null} if
 * the element was present but {@code null}. If the element was not present, you
 * would return {@link Result#notAvailable()}.
 *
 * @param <T> the type of the result value
 */
public final class Result<T> implements Emptyable {

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static final Result NONE = new Result(null);

  /**
   * Returns a {@code Result} containing the specified value (possibly
   * {@code null}).
   *
   * @param value The value
   * @param <U> The type of the value
   * @return a {@code Result} containing the specified value
   */
  public static <U> Result<U> of(U value) {
    return new Result<>(value);
  }

  /**
   * Returns a special {@code Result} instance signifying the absence of a result.
   *
   * @param <U> the type of the result value
   * @return a special {@code Result} object signifying the absence of a result
   */
  @SuppressWarnings("unchecked")
  public static <U> Result<U> notAvailable() {
    return (Result<U>) NONE;
  }

  private final T val;

  private Result(T value) {
    this.val = value;
  }

  /**
   * Returns the result.
   *
   * @return the value
   * @throws NoSuchElementException if this {@code Result} does not contain a
   *     proper result value
   */
  public T get() {
    if (!isAvailable()) {
      throw new NoSuchElementException("no result available");
    }
    return val;
  }

  /**
   * Returns {@code true} if this {@code Result} represents a legitimate outcome
   * (even if possibly {@code null}). If so, the result value can be retrieved via
   * the {@link #get()} method. If not, calling the {@code get()} method will result
   * in a {@link NoSuchElementException}.
   *
   * @return {@code true} if this {@code Result} represents a legitimate outcome
   *     (even if possibly {@code null})
   */
  public boolean isAvailable() {
    return this != NONE;
  }

  public boolean isUnavailable() {
    return this == NONE;
  }

  /**
   * If available, passes the result to the specified consumer; else does nothing.
   *
   * @param consumer the consumer of the result
   * @param <X> the type of the exception thrown by the consumer
   * @throws X if the consumer experiences an error
   */
  public <X extends Throwable> void ifAvailable(FallibleConsumer<T, X> consumer)
      throws X {
    Objects.requireNonNull(consumer);
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
   * Returns this {@code Result} if it represents an available result, else the
   * provided {@code Result}.
   *
   * @param alternative the {@code Result} to return if this {@code Result} is
   *     {@link Result#notAvailable() Result.notAvailable()}. Must not be
   *     {@code null}, and must not be {@code Result.notAvailable()}.
   * @return this instance or the provided instance
   */
  public Result<T> or(Result<T> alternative) {
    Objects.requireNonNull(alternative);
    if (alternative == NONE) {
      throw new IllegalArgumentException(
          "alternative must not beResult.notAvailable()");
    }
    return isAvailable() ? this : alternative;
  }

  /**
   * Returns {@code true} if this instance is {@link Result#notAvailable()}, or if
   * the result value is empty as per the {@link CommonChecks#empty() empty()} test.
   *
   * @return {@code true} if no result is available or the result value is empty.
   */
  @Override
  public boolean isEmpty() {
    return this == NONE || empty().test(val);
  }

  /**
   * Returns {@code true} a result is available and it is recursively non-empty as
   * per the {@link CommonChecks#deepNotEmpty() deepNotEmpty()} test.
   *
   * @return {@code true} if a result is available and it is deep-not-empty
   */
  @Override
  public boolean isDeepNotEmpty() {
    return this != NONE && deepNotEmpty().test(val);
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj
        || (obj instanceof Result<?> other && Objects.equals(val, other.val));
  }

  /**
   * Returns the hashcode of the value contained in this {@code Result}, or 0 if no
   * result was available.
   *
   * @return the hashcode of the value contained in this {@code Result}, or 0 if no
   *     result was available
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(val);
  }

  /**
   * Returns a string representation analogous to the one provided by
   * {@link java.util.Optional}.
   *
   * @return a string representation analogous to the one provided by
   *     {@link java.util.Optional}
   */
  @Override
  public String toString() {
    return val != null ? String.format("Result[%s]", val) : "Result.notAvailable";
  }

}
