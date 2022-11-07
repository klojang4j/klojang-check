package org.klojang.check.x;

import org.klojang.check.Range;

public final class RangeInclusive extends Range {

  private final int lower;
  private final int upper;

  public RangeInclusive(int lower, int upper) {
    this.lower = lower;
    this.upper = upper;
  }

  public int lower() {return lower;}

  public int upper() {return upper;}

  @Override
  public String toString() {
    return "[" + lower + ", " + upper + "]";
  }

}
