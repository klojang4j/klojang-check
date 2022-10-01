package nl.naturalis.common.util;

/**
 * The ubiquitous mutable-integer class.
 */
public final class MutableInt {

  private int i;

  /**
   * Instantiates a {@code MutableInt} with an initial value of 0 (zero).
   */
  public MutableInt() {}

  /**
   * Instantiates a {@code MutableInt} with the specified initial value.
   *
   * @param value The initial value of this instance
   */
  public MutableInt(int value) {
    i = value;
  }

  /**
   * Instantiates a {@code MutableInt} initialized to the current value of another
   * instance.
   *
   * @param other The instance used to initialize this instance.
   */
  public MutableInt(MutableInt other) {
    i = other.i;
  }

  /**
   * Returns the current value of this {@code MutableInt}.
   *
   * @return The current value of this {@code MutableInt}
   */
  public int get() {
    return i;
  }

  /**
   * Corresponds to the {@code ++} postfix operator ({@code i++}).
   *
   * @return The original value (before incrementation)
   */
  public int pp() {
    return i++;
  }

  /**
   * Corresponds to the {@code ++} prefix operator ({@code ++i}).
   *
   * @return The incremented value (after incrementation)
   */
  public int ppi() {
    return ++i;
  }

  /**
   * Corresponds to the {@code --} postfix operator ({@code i--}).
   *
   * @return The original value
   */
  public int mm() {
    return i--;
  }

  /**
   * Corresponds to the {@code --} prefix operator ({@code --i}).
   *
   * @return The decremented value
   */
  public int mmi() {
    return --i;
  }

  /**
   * Corresponds to the {@code +=} operation.
   *
   * @param j The value to add
   * @return The new value
   */
  public int plusIs(int j) {
    return i += j;
  }

  /**
   * Corresponds to the {@code +=} operation.
   *
   * @param other The value the add
   * @return The new value
   */
  public int plusIs(MutableInt other) {
    return i += other.i;
  }

  /**
   * Corresponds to the {@code -=} (minus-is) operation.
   *
   * @param j The value to subtract
   * @return The new value
   */
  public int minIs(int j) {
    return i -= j;
  }

  /**
   * Corresponds to the {@code -=} (minus-is) operation.
   *
   * @param other The value to subtract
   * @return The new value
   */
  public int minIs(MutableInt other) {
    return i -= other.i;
  }

  /**
   * Corresponds to the assignment operation ({@code i = j}).
   *
   * @param j The value to assign to this {@code MutableInt}
   * @return The new value
   */
  public int set(int j) {
    return i = j;
  }

  /**
   * Corresponds to the assignment operation ({@code i = j}).
   *
   * @param other The value to assign to this {@code MutableInt}
   * @return The new value
   */
  public int set(MutableInt other) {
    return i = other.i;
  }

  /**
   * Corresponds to the {@code ==} (equals) operation.
   *
   * @param j The value to compare this instance with
   * @return {@code true} if equal, {@code false} otherwise
   */
  public boolean eq(int j) {
    return i == j;
  }

  /**
   * Corresponds to the {@code ==} (equals) operation.
   *
   * @param other The value to compare this instance with
   * @return {@code true} if equal, {@code false} otherwise
   */
  public boolean eq(MutableInt other) {
    return i == other.i;
  }

  /**
   * Corresponds to the {@code !=} (not equals) operation.
   *
   * @param j The value to compare this instance with
   * @return {@code true} if not equal, {@code false} otherwise
   */
  public boolean ne(int j) {
    return i != j;
  }

  /**
   * Corresponds to the {@code !=} (not equals) operation.
   *
   * @param other The value to compare this instance with
   * @return {@code true} if equal, {@code false} otherwise
   */
  public boolean ne(MutableInt other) {
    return i != other.i;
  }

  /**
   * Corresponds to the {@code >} (greater than) operation.
   *
   * @param j The value to compare this instance with
   * @return {@code true} if this instance has a value greater than the specified
   *     value, {@code false} otherwise
   */
  public boolean gt(int j) {
    return i > j;
  }

  /**
   * Corresponds to the {@code >} (greater than) operation.
   *
   * @param other The value to compare this instance with
   * @return {@code true} if this instance has a value greater than the specified
   *     value, {@code false} otherwise
   */
  public boolean gt(MutableInt other) {
    return i > other.i;
  }

  /**
   * Corresponds to the {@code <} (less than) operation.
   *
   * @param j The value to compare this instance with
   * @return {@code true} if this instance has a value less than the specified value,
   *     {@code false} otherwise
   */
  public boolean lt(int j) {
    return i < j;
  }

  /**
   * Corresponds to the {@code <} (less than) operation.
   *
   * @param other The value to compare this instance with
   * @return {@code true} if this instance has a value less than the specified value,
   *     {@code false} otherwise
   */
  public boolean lt(MutableInt other) {
    return i < other.i;
  }

  /**
   * Corresponds to the {@code >=} (greater or equal) operation.
   *
   * @param j The value to compare this instance with
   * @return {@code true} if this instance has a value greater than, or equal to the
   *     specified value, {@code false} otherwise
   */
  public boolean gte(int j) {
    return i >= j;
  }

  /**
   * Corresponds to the {@code >=} (greater or equal) operation.
   *
   * @param other The value to compare this instance with
   * @return {@code true} if this instance has a value greater than, or equal to the
   *     specified value, {@code false} otherwise
   */
  public boolean gte(MutableInt other) {
    return i >= other.i;
  }

  /**
   * Corresponds to the {@code <=} (less or equal) operation.
   *
   * @param j The value to compare this instance with
   * @return {@code true} if this instance has a value less than, or equal to the
   *     specified value, {@code false} otherwise
   */
  public boolean lte(int j) {
    return i <= j;
  }

  /**
   * Corresponds to the {@code <=} (less or equal) operation.
   *
   * @param other The value to compare this instance with
   * @return {@code true} if this instance has a value less than, or equal to the
   *     specified value, {@code false} otherwise
   */
  public boolean lte(MutableInt other) {
    return i <= other.i;
  }

  /**
   * Sets the value to 0 (zero).
   *
   * @return 0 (zero)
   */
  public int reset() {
    return i = 0;
  }

  @Override
  public int hashCode() {
    return i;
  }

  /**
   * Returns true if {@code obj} is a {@code MutableInt}, {@link Integer}, {@link
   * Short} or {@link Byte} with the same value as this {@code MutableInt}, {@code
   * false otherwise}.
   *
   * @param obj The value to compare this instance with
   * @return Whether this {@code MutableInt} is equal to the specified value
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (obj instanceof MutableInt other) {
      return i == other.i;
    } else if (obj instanceof Integer x) {
      return i == x;
    } else if (obj instanceof Short x) {
      return i == x;
    } else if (obj instanceof Byte x) {
      return i == x;
    }
    return false;
  }

  @Override
  public String toString() {
    return String.valueOf(i);
  }

}
