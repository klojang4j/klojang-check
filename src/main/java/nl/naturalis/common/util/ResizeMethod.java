package nl.naturalis.common.util;

import nl.naturalis.check.Check;
import nl.naturalis.common.collection.IntArrayList;

import java.nio.BufferOverflowException;

import static java.lang.Math.*;
import static nl.naturalis.check.Check.fail;
import static nl.naturalis.check.CommonChecks.*;

/**
 * Defines ways to increase the capacity of backing arrays and other types of
 * internally managed buffers and caches. Can be used if you want to leave it to
 * clients of your class to decide upon a proper resizing strategy. If you use the
 * {@link #resize(int, double, int) resize} methods to calculate a new size for the
 * cache, buffer or backing array, the increase in size will max out at {@link
 * #MAX_INCREASE} for the {@link #MULTIPLY} and {@link #PERCENTAGE} resize methods.
 * After that, they will behave like the {@code ADD} method, adding {@code
 * MAX_INCREASE} to the backing array's current capacity each time it needs to be
 * resized. In addition, the {@code resize} method will also take care of trapping
 * buffer overflows (the backing array's length reaching or exceeding {@link
 * Integer#MAX_VALUE}).
 *
 * <p>The following example illustrates how to use {@code ResizeMethod}:
 *
 * <blockquote><pre>{@code
 * public class DIYArrayList<E> extends AbstractList<E> {
 *
 *  private final ResizeMethod resizeMethod;
 *  private final double resizeAmount;
 *
 *  private Object[] buf;
 *  private int size;
 *
 *  public DIYArrayList(int initialCapacity, ResizeMethod resizeMethod, double resizeAmount) {
 *    this.buf = new Object[initialCapacity];
 *    this.resizeMethod = resizeMethod;
 *    this.resizeAmount = resizeAmount;
 *  }
 *
 *  @Override
 *  public boolean addAll(Collection<?> c) {
 *    int minIncrease = ResizeMethod.getMinIncrease(buf.length, size, c.size());
 *    if(minIncrease > 0) {
 *      increaseCapacity(minIncrease);
 *    }
 *    // append the collection ...
 *  }
 *
 *  private void increaseCapacity(int minIncrease) {
 *    int newCap = resizeMethod.resize(buf.length, resizeAmount, minIncrease);
 *    Object[] newBuf = new Object[newCap];
 *    System.arraycopy(buf, 0, newBuf, 0, size);
 *    buf = newBuf;
 *  }
 *
 * }
 * }</pre></blockquote>
 *
 * @author Ayco Holleman
 * @see IntArrayList#IntArrayList(int, ResizeMethod, float)
 */
public enum ResizeMethod {
  /**
   * Increase the capacity by a fixed amount each time the buffer reaches full
   * capacity.
   */
  ADD,
  /**
   * Increase the capacity by multiplying the current capacity by a fixed amount.
   * Using this resize method with a resize factor of 2 is the "classic" way of
   * resizing a backing array.
   */
  MULTIPLY,
  /**
   * Increase the capacity by adding a fixed percentage of the current capacity to
   * the current capacity.
   */
  PERCENTAGE;

  /**
   * The maximum increase in size allowed by the {@link #MULTIPLY} and {@link
   * #PERCENTAGE} resize methods: {@code 8 * 1024 * 1024}.
   */
  public static final int MAX_INCREASE = 8 * 1024 * 1024;

  private static final int MAX_INT = Integer.MAX_VALUE;

  /**
   * Calculates the <i>extra</i> capacity required to add {@code itemCount} items to
   * an internally managed buffer, cache or backing array. If the result is zero or
   * negative, no resizing is necessary. The return value of this method can be used
   * as the {@code minIncrease} argument for the {@link #resize(int, double, int)
   * resize} method.
   *
   * @param curCapacity The current length or size of the buffer, cache or
   *     backing array
   * @param curSize The number of items already in the buffer, cache or backing
   *     array
   * @param itemCount The number of items to be added to the buffer, cache or
   *     backing array
   * @return If positive, the <i>extra capacity</i> required for the new items, else
   *     the remaining capacity once the new items have been added
   */
  public static int getMinIncrease(int curCapacity, int curSize, int itemCount) {
    long required = (long) curSize + itemCount;
    if (required > (long) MAX_INT) {
      throw new BufferOverflowException();
    }
    return (int) required - curCapacity;
  }

  /**
   * Calculates the new capacity for a buffer, cache or backing array using this
   * {@code ResizeMethod} and the specified resize amount (c&#46;q&#46; factor
   * c&#46;q&#46; percentage) The capacity will increase by at least 1.
   *
   * @param curCapacity The current capacity
   * @param amount The amount, factor or percentage by which to increase the
   *     current capacity. This would typically be the value of a (final) instance
   *     variable in the class managing the backing array, and it would typically be
   *     passed in through the constructor, along with the resize method.
   * @return The value to be used as the length for the new backing array
   */
  public int resize(int curCapacity, double amount) {
    return resize(curCapacity, amount, 1);
  }

  /**
   * Calculates the new capacity for a buffer, cache or backing array using this
   * {@code ResizeMethod}. The capacity will increase by at least {@code
   * minIncrease}. A {@link BufferOverflowException} is thrown if the current
   * capacity is {@link Integer#MAX_VALUE}, or if {@code curCapacity + minIncrease}
   * is greater than {@code Integer.MAX_VALUE}.
   *
   * @param curCapacity The current capacity. It is allowed to start off with
   *     zero initial capacity. When resizing using the {@link #MULTIPLY} or {@link
   *     #PERCENTAGE} method, the initial capacity will first be tacitly increased to
   *     1.
   * @param amount The amount, factor or percentage by which to increase the
   *     current capacity. This would typically be the value of a (final) instance
   *     variable in the class managing the backing array, and it would typically be
   *     passed in through the constructor, along with the resize method.
   * @param minIncrease The minimum amount by which to increase current capacity.
   *     This would typically be the size of an incoming block of data that
   *     necessitated the resizing of the backing array. The provided value will
   *     override {@link #MAX_INCREASE} and it must be at least 1.
   * @return The value to be used as the length for the new backing array
   */
  public int resize(int curCapacity, double amount, int minIncrease) {
    Check.that(curCapacity, "current capacity").is(gte(), 0);
    Check.that(minIncrease, "minimum increase").is(gt(), 0);
    Check.that(curCapacity).is(ne(), MAX_INT, BufferOverflowException::new);
    long minCap = (long) curCapacity + minIncrease;
    if (minCap > (long) MAX_INT) {
      throw new BufferOverflowException();
    }
    if (this != ADD && curCapacity == 0) {
      curCapacity = 1;
    }
    long newCap = switch (this) {
      case ADD:
        int x = (int) amount;
        if (x != amount || x <= 0) {
          yield fail("resize term must be positive, non-fractional number");
        }
        yield curCapacity + x;
      case MULTIPLY:
        if (amount < 1) {
          yield fail("resize factor must be greater than 1");
        }
        yield round(curCapacity * amount);
      case PERCENTAGE:
        if (amount <= 0) {
          yield fail("resize percentage must be greater than 0");
        }
        yield curCapacity + round((amount / 100) * curCapacity);
    };
    newCap = max(newCap, minCap);
    if (minIncrease < MAX_INCREASE && this != ADD) {
      newCap = min(newCap, (long) curCapacity + MAX_INCREASE);
    }
    return (int) min(MAX_INT, newCap);
  }

}
