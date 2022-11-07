package org.klojang.check;

import org.klojang.check.x.RangeExclusive;
import org.klojang.check.x.RangeInclusive;

/**
 * Conveys the lower bound and upper bound of a valid range of integer values. This
 * class solely exists for the {@link CommonChecks#inRange() inRange()} check of the
 * {@link CommonChecks} class.
 */
public abstract sealed class Range permits RangeInclusive, RangeExclusive {

  /**
   * Returns a "classic" {@code Range} object in which the specified upper bound is
   * not itself part of the range of valid values.
   *
   * @param lowerBound the lower bound of the range (inclusive)
   * @param upperBoundExclusive the upper bound of the range (exclusive)
   * @return a "classic" {@code Range} object in which the specified upper bound is
   *     not itself part of the range of valid values
   */
  public static Range open(int lowerBound, int upperBoundExclusive) {
    return new RangeExclusive(lowerBound, upperBoundExclusive);
  }

  /**
   * Returns a {@code Range} object in which the upper bound is itself part of the
   * range of valid values.
   *
   * @param lowerBound the lower bound of the range (inclusive)
   * @param upperBoundInclusive the upper bound of the range (inclusive)
   * @return a {@code Range} object in which the upper bound is itself part of the
   *     range of valid values
   */
  public static Range closed(int lowerBound, int upperBoundInclusive) {
    return new RangeInclusive(lowerBound, upperBoundInclusive);
  }

}
