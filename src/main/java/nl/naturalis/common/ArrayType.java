package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.common.collection.TypeMap;
import nl.naturalis.common.x.invoke.InvokeUtils;

import java.util.concurrent.atomic.AtomicReference;

import static nl.naturalis.check.CommonChecks.*;
import static nl.naturalis.check.CommonProperties.type;

/**
 * Provides metadata about an array type. An {@code ArrayType} consists of a base
 * type and a dimension count. The base type is the <i>lowest-level</i>
 * {@linkplain Class#getComponentType() component type} of an array class. So for
 * {@code int[][][]} that would be {@code int} and not {@code int[][]}. In other
 * words, <b>the base type of an array always is a non-array type</b>.
 *
 * @param baseType The lowest-level component type
 * @param dimensions The number of dimensions
 * @author Ayco Holleman
 */
public record ArrayType(Class<?> baseType, int dimensions) {

  /**
   * {@code ArrayType} encoding a one-dimensional {@code byte} array.
   */
  public static final ArrayType BYTE_ARRAY = new ArrayType(byte.class, 1);

  /**
   * {@code ArrayType} encoding a one-dimensional {@code int} array.
   */
  public static final ArrayType INT_ARRAY = new ArrayType(int.class, 1);

  /**
   * Returns a description of the provided array. It contains the base type's simple
   * class name and the length of the outermost array. For example, for an array
   * defined as {@code new Double[4][12]}, it would return "Double[4][]".
   *
   * @param array The array to describe
   * @return A description of the array
   */
  public static String describe(Object array) {
    ArrayType at = forArray(array);
    int len = InvokeUtils.getArrayLength(array);
    StringBuilder sb = new StringBuilder(at.baseType.getSimpleName())
        .append('[')
        .append(len)
        .append(']');
    for (int i = 1; i < at.dimensions; ++i) {
      sb.append("[]");
    }
    return sb.toString();
  }

  // Let's just arbitrarily start with byte[]
  private static final AtomicReference<Tuple2<Class<?>, ArrayType>> cache =
      new AtomicReference<>(Tuple2.of(byte[].class, BYTE_ARRAY));

  /**
   * Returns the {@code ArrayType} corresponding to the specified array class. An
   * {@link IllegalArgumentException} is thrown if the provided object is not an
   * array type.
   *
   * @param arrayClass The array class
   * @return The {@code ArrayType} instance
   */
  public static ArrayType forClass(Class<?> arrayClass) {
    Check.notNull(arrayClass).is(array());
    return create(arrayClass);
  }

  /**
   * Returns the {@code ArrayType} corresponding to the specified array object. An
   * {@link IllegalArgumentException} is thrown if the provided object is not an
   * array.
   *
   * @param array The array
   * @return The {@code ArrayType} instance
   */
  public static ArrayType forArray(Object array) {
    Check.notNull(array).is(array());
    return create(array.getClass());
  }

  /**
   * Returns zero for non-array types, and the number of dimensions for array types.
   *
   * @param c The type for which to get the number of dimensions
   * @return The dimensionality of the type.
   */
  public static int dimensions(Class<?> c) {
    Check.notNull(c);
    int x = 0;
    for (; c.isArray(); c = c.getComponentType()) {
      ++x;
    }
    return x;
  }

  /**
   * Creates a new {@code ArrayType} instance. The {@code baseType} argument is, in
   * fact, allowed to be an array type, but the base type recorded by the instance
   * will then be the base type of <i>that</i> array type, and the number of
   * dimensions of the array type will be added to the provided number of dimensions.
   * The provided number of dimensions may then even be zero or negative (as long as
   * the sum of the dimensions remains positive):
   *
   * <blockquote><pre>{@code
   * ArrayType at1 = new ArrayType(float[][].class, 1); // float[][][].class
   * ArrayType at2 = new ArrayType(float[][].class, -1); // float[].class
   * }</pre></blockquote>
   *
   * @param baseType The base type of the array
   * @param dimensions The number of dimensions
   */
  public ArrayType(Class<?> baseType, int dimensions) {
    Check.notNull(baseType, "baseType");
    if (baseType.isArray()) {
      ArrayType tmp = create(baseType);
      this.baseType = tmp.baseType;
      int x = dimensions + tmp.dimensions;
      this.dimensions = Check.that(x, "dimensions").is(positive()).ok();
    } else {
      Check.that(dimensions, "dimensions").is(positive());
      this.baseType = baseType;
      this.dimensions = dimensions;
    }
  }

  /**
   * Returns the {@code Class} object corresponding to this {@code ArrayType}.
   *
   * @return A {@code Class} object
   */
  public Class<?> toClass() {
    return toClass(baseType, dimensions);
  }

  /**
   * Returns the {@code Class} object corresponding to an {@code ArrayType} with the
   * specified base type and with the same number of dimensions as this instance.
   *
   * @return A {@code Class} object
   */
  public Class<?> toClass(Class<?> baseType) {
    Class<?> c = Check.notNull(baseType).ok();
    return toClass(baseType, dimensions);
  }

  /**
   * Returns the {@code Class} object corresponding to an {@code ArrayType} equal to
   * this one, but with the boxed version of the base type. So for
   * {@code int[][].class} it would return {@code Integer[][].class}. If the base
   * type is not a primitive type, this method is equivalent to {@link #toClass()}.
   *
   * @return A {@code Class} object
   * @see ClassMethods#box(Class)
   */
  public Class<?> box() {
    return toClass(ClassMethods.box(baseType));
  }

  /**
   * Returns the {@code Class} object corresponding to an {@code ArrayType} equal to
   * this one, but with the unboxed version of the base type. So for
   * {@code Float[][].class} it would return {@code float[][].class}. If the base
   * type is not a primitive type, this method is equivalent to {@link #toClass()}.
   *
   * @return A {@code Class} object
   * @see ClassMethods#unbox(Class)
   */
  public Class<?> unbox() {
    return toClass(ClassMethods.unbox(baseType));
  }

  /**
   * Returns the {@code Class} object corresponding to an {@code ArrayType} with the
   * specified number of dimensions and with the same base type as this instance.
   *
   * @return A {@code Class} object
   */
  public Class<?> toClass(int dimensions) {
    Check.that(dimensions, "dimensions").is(positive());
    return toClass(baseType, dimensions);
  }

  /**
   * Returns the {@code ArrayType} for the boxed version of the base type.
   *
   * @return The {@code ArrayType} for the boxed version of the base type
   */
  public ArrayType boxed() {
    return new ArrayType(ClassMethods.box(baseType), dimensions);
  }

  /**
   * Returns the {@code ArrayType} for the unboxed version of the base type.
   *
   * @return The {@code ArrayType} for the unboxed version of the base type
   */
  public ArrayType unboxed() {
    return new ArrayType(ClassMethods.unbox(baseType), dimensions);
  }

  /**
   * Returns the simple class name of the array type encoded by this instance. The
   * returned string is somewhat easier to read than what you get from
   * {@link Class#getSimpleName()}. For example the returned value for
   * {@code int[][].class} would be "int[][]".
   *
   * @return The simple class name of the array type encoded by this
   *     {@code ArrayType}
   */
  @Override
  public String toString() {
    if (dimensions == 1) { // happy path for 99% of the cases
      return baseType().getSimpleName() + "[]";
    }
    String s = baseType().getSimpleName();
    StringBuilder sb = new StringBuilder(s.length() + dimensions * 2);
    sb.append(s);
    for (int i = 0; i < dimensions; ++i) {
      sb.append("[]");
    }
    return sb.toString();
  }

  /**
   * Returns the fully-qualified class name of the array type encoded by this
   * instance. The returned string is somewhat easier to read than what you get from
   * {@link Class#getName()}. For example, the returned value for
   * {@code String[][].class} would be "java.lang.String[][]".
   *
   * @return The fully-qualified class name of the array type encoded by this
   *     {@code ArrayType}
   */
  public String arrayClassName() {
    StringBuilder sb = new StringBuilder(baseType().getName());
    for (int i = 0; i < dimensions; ++i) {
      sb.append("[]");
    }
    return sb.toString();
  }

  private static ArrayType create(Class<?> arrayClass) {
    return cache.updateAndGet(tuple -> {
      if (tuple.first() == arrayClass) {
        return tuple;
      }
      var c = arrayClass.getComponentType();
      int i = 1;
      for (; c.isArray(); c = c.getComponentType()) {
        ++i;
      }
      return Tuple2.of(arrayClass, new ArrayType(c, i));
    }).second();
  }

  private static Class<?> toClass(Class<?> baseType, int dimensions) {
    Class<?> c = baseType;
    for (int i = 0; i < dimensions; ++i) {
      c = c.arrayType();
    }
    return c;
  }

}
