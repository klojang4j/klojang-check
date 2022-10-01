package nl.naturalis.check;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static nl.naturalis.check.MsgUtil.ERR_NULL_MESSAGE;

/**
 * Provides static factory methods for {@link IntCheck} and {@link ObjectCheck}
 * instances. The {@code Check} class also contains a few validation methods itself,
 * like {@link #fromTo(int, int, int) Check.fromTo} and
 * {@link #offsetLength(int, int, int) Check.offsetLength}. These stand somewhat
 * apart from the rest of the Naturalis Check framework as they test multiple things
 * at once. They are included for convenience and for optimal performance.
 *
 * <p>See the {@linkplain nl.naturalis.check package description} for a detailed
 * description of validating preconditions and postconditions using Naturalis Check.
 *
 * @author Ayco Holleman
 */
public final class Check {

  /**
   * A special value that you can use for checks that allow you to provide a custom
   * error message. If this value is the one and only message argument following the
   * message itself, the message will, in fact, not be scanned for message arguments
   * at all. It will be passed, as-is, to the exception's constructor. This will
   * slightly speed up the generation of an exception and restore performance parity
   * with hand-coded checks. Note, however, that the effect is really only measurable
   * if a check almost continuously rejects any value thrown at, which would be
   * rather odd (if not suspicious) for a precondition check.
   */
  public static final Object EOM = new Object();

  private Check() {
    throw new UnsupportedOperationException();
  }

  static final String DEF_ARG_NAME = "argument";

  private static final Function<String, IllegalArgumentException> DEF_EXC_FACTORY =
      CommonExceptions.ARGUMENT;

  /**
   * Static factory method. Returns an {@link IntCheck} instance suitable for testing
   * {@code int} values.
   *
   * @param arg the value to be validated
   * @return an {@code IntCheck} instance suitable for testing {@code int} values
   */
  public static IntCheck<IllegalArgumentException> that(int arg) {
    return new IntCheck<>(arg, null, DEF_EXC_FACTORY);
  }

  /**
   * Static factory method. Returns an {@link ObjectCheck} instance suitable for
   * validating values of type {@code <T>}.
   *
   * @param <T> the type of the value to be validated
   * @param arg the value to be validated
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T> ObjectCheck<T, IllegalArgumentException> that(T arg) {
    return new ObjectCheck<>(arg, null, DEF_EXC_FACTORY);
  }

  /**
   * Static factory method. Returns an {@link IntCheck} instance suitable for
   * validating {@code int} values.
   *
   * @param arg the value to be validated
   * @param argName a descriptive name for the value (in case the value is a
   *     method argument probably something close to the parameter name)
   * @return an {@code IntCheck} instance suitable for testing {@code int} values
   */
  public static IntCheck<IllegalArgumentException> that(int arg, String argName) {
    return new IntCheck<>(arg, argName, DEF_EXC_FACTORY);
  }

  /**
   * Static factory method. Returns an {@code ObjectCheck} instance suitable for
   * validating values of type {@code <T>}.
   *
   * @param <T> the type of the value to be validated
   * @param arg the value to be validated
   * @param argName a descriptive name for the value (in case the value is a
   *     method argument probably something close to the parameter name)
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T> ObjectCheck<T, IllegalArgumentException> that(T arg,
      String argName) {
    return new ObjectCheck<>(arg, argName, DEF_EXC_FACTORY);
  }

  /**
   * Static factory method. Returns an {@code ObjectCheck} instance suitable for
   * validating values of type {@code <T>}. The argument will have already passed the
   * {@linkplain CommonChecks#notNull() null test}, or an
   * {@code IllegalArgumentException} will have been thrown.
   *
   * @param <T> the type of the value to be validated
   * @param arg the value to be validated
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T> ObjectCheck<T, IllegalArgumentException> notNull(T arg)
      throws IllegalArgumentException {
    if (arg != null) {
      return new ObjectCheck<>(arg, null, DEF_EXC_FACTORY);
    }
    throw new IllegalArgumentException("argument must not be null");
  }

  /**
   * Static factory method. Returns a new {@code Check} instance suitable for testing
   * the provided argument. The argument will have already passed the
   * {@link CommonChecks#notNull() notNull} test.
   *
   * @param <T> the type of the value to be validated
   * @param arg the value to be validated
   * @param argName a descriptive name for the value (in case the value is a
   *     method argument probably something close to the parameter name)
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T> ObjectCheck<T, IllegalArgumentException> notNull(T arg,
      String argName)
      throws IllegalArgumentException {
    if (arg != null) {
      return new ObjectCheck<>(arg, argName, DEF_EXC_FACTORY);
    }
    throw new IllegalArgumentException(argName + " must not be null");
  }

  /**
   * Static factory method. Returns an {@link IntCheck} instance suitable for
   * validating {@code int} values. If the value fails any of the tests passed to the
   * {@code IntCheck} instance, the exception produced by the provided exception
   * factory is thrown.
   *
   * @param excFactory a function that will produce the exception if the value
   *     fails to pass a test. The function will be pass the exception message and
   *     must return the exception to be thrown
   * @param arg the value to be validated
   * @param <X> the type of {@code Exception} thrown if the value fails to pass a
   *     test
   * @return an {@code IntCheck} instance suitable for testing {@code int} values
   */
  public static <X extends Exception> IntCheck<X> on(Function<String, X> excFactory,
      int arg) {
    return new IntCheck<>(arg, null, excFactory);
  }

  /**
   * Static factory method. Returns an {@code ObjectCheck} instance suitable for
   * validating values of type {@code <T>}. If the value fails any of the tests
   * passed to the {@code ObjectCheck} instance, the exception produced by the
   * provided exception factory is thrown.
   *
   * @param <T> the type of the value to be validated
   * @param <X> the type of {@code Exception} thrown if the value fails to pass a
   *     test
   * @param excFactory a function that will produce the exception if the value
   *     fails to pass a test. The function will be pass the exception message and
   *     must return the exception to be thrown
   * @param arg the value to be validated
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T, X extends Exception> ObjectCheck<T, X> on(
      Function<String, X> excFactory, T arg) {
    return new ObjectCheck<>(arg, null, excFactory);
  }

  /**
   * Static factory method. Returns an {@link IntCheck} instance suitable for
   * validating {@code int} values. If the value fails any of the tests passed to the
   * {@code IntCheck} instance, the exception produced by the provided exception
   * factory is thrown.
   *
   * @param excFactory a function that will produce the exception if the value
   *     fails to pass a test. The function will be pass the exception message and
   *     must return the exception to be thrown
   * @param arg the value to be validated
   * @param argName a descriptive name for the value (in case the value is a
   *     method argument probably something close to the parameter name)
   * @param <X> the type of {@code Exception} thrown if the value fails to pass a
   *     test
   * @return an {@code IntCheck} instance suitable for testing {@code int} values
   */
  public static <X extends Exception> IntCheck<X> on(
      Function<String, X> excFactory, int arg, String argName) {
    return new IntCheck<>(arg, argName, excFactory);
  }

  /**
   * Static factory method. Returns a new {@code Check} instance suitable for testing
   * the provided argument.
   *
   * @param <T> the type of the value to be validated
   * @param <X> the type of {@code Exception} thrown if the value fails to pass a
   *     test
   * @param excFactory a function that will produce the exception if the value
   *     fails to pass a test. The function will be pass the exception message and
   *     must return the exception to be thrown
   * @param arg the value to be validated
   * @param argName a descriptive name for the value (in case the value is a
   *     method argument probably something close to the parameter name)
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T, X extends Exception> ObjectCheck<T, X> on(
      Function<String, X> excFactory, T arg, String argName) {
    return new ObjectCheck<>(arg, argName, excFactory);
  }

  /**
   * All-in-one check for the specified array, offset and length. Verifies that
   * {@code offset} and {@code length} are valid, given the length of the specified
   * array. More precisely:
   *
   * <ol>
   *   <li>throws an {@code IllegalArgumentException} if the array is {@code null}.
   *   <li>throws an {@code IndexOutOfBoundsException} if {@code offset} or {@code length} is less than zero
   *   <li>throws an {@code IndexOutOfBoundsException} if {@code offset+length} is greater than the length of the array
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param array the array
   * @param offset the offset within the array
   * @param length the length of the segment
   * @see #offsetLength(int, int, int)
   * @see java.io.OutputStream#write(byte[], int, int)
   * @see java.io.InputStream#read(byte[], int, int)
   */
  public static void offsetLength(byte[] array, int offset, int length) {
    if (array == null) {
      throw new IllegalArgumentException("array must not be null");
    }
    if ((offset | length) < 0 || offset + length > array.length) {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * All-in-one check for the provided size, offset and length. Verifies that
   * {@code offset} and {@code length} are valid, given tan array or array-like
   * object with the specified size.
   *
   * <ol>
   *   <li>throws an {@code IndexOutOfBoundsException} if {@code size} or {@code offset} or {@code length} is less than zero
   *   <li>throws an {@code IndexOutOfBoundsException} if {@code offset+length} is greater than the length of the array
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param size the length/size of the array or array-like object
   * @param offset the offset
   * @param length the length of the segment
   */
  public static void offsetLength(int size, int offset, int length) {
    if ((size | offset | length) < 0 || size < offset + length) {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * An all-in-one check for the provided list, from-index and to-index. Verifies
   * that the sublist specified through the from-index and to-index stays within the
   * boundaries of the list. More precisely:
   *
   * <ol>
   *   <li>Throws an {@code IllegalArgumentException} if the list is {@code null}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code fromIndex} or {@code toIndex} is less than zero
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is less than {@code fromIndex}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is greater than the size of the list
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param list the list
   * @param fromIndex the start index of the sublist
   * @param toIndex the end index of the sublist
   * @return the {@code size} of the sublist
   * @see #fromTo(int, int, int)
   * @see List#subList(int, int)
   */
  public static int fromTo(List<?> list, int fromIndex, int toIndex) {
    if (list == null) {
      throw new IllegalArgumentException("list must not be null");
    }
    if ((fromIndex | toIndex) < 0 || toIndex < fromIndex || list.size() < toIndex) {
      throw new IndexOutOfBoundsException();
    }
    return toIndex - fromIndex;
  }

  /**
   * An all-in-one check for the provided array, from-index and to-index. Verifies
   * that the segment specified through the from-index and to-index stays within the
   * boundaries of the array. More precisely:
   *
   * <ol>
   *   <li>Throws an {@code IllegalArgumentException} if the array is {@code null}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code fromIndex} or {@code toIndex} is less than zero
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is less than {@code fromIndex}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is greater than the size of the list
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param array the array
   * @param fromIndex the start index of the array segment
   * @param toIndex the end index of the array segment
   * @return the {@code length} of the array segment
   * @see #fromTo(int, int, int)
   * @see Arrays#copyOfRange(Object[], int, int)
   */
  public static <T> int fromTo(T[] array, int fromIndex, int toIndex) {
    if (array == null) {
      throw new IllegalArgumentException("array must not be null");
    }
    if ((fromIndex | toIndex) < 0 || toIndex < fromIndex || array.length < toIndex) {
      throw new IndexOutOfBoundsException();
    }
    return toIndex - fromIndex;
  }

  /**
   * An all-in-one check for the provided string, from-index and to-index. Verifies
   * that the substring specified through the from-index and to-index stays within
   * the boundaries of the string. More precisely:
   *
   * <ol>
   *   <li>Throws an {@code IllegalArgumentException} if the array is {@code null}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code fromIndex} or {@code toIndex} is less than zero
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is less than {@code fromIndex}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is greater than the size of the list
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param string The string
   * @param fromIndex the start index of the substring
   * @param toIndex the end index of the substring
   * @return the {@code length} of the substring
   * @see #fromTo(int, int, int)
   * @see String#substring(int, int)
   */
  public static int fromTo(String string, int fromIndex, int toIndex) {
    if (string == null) {
      throw new IllegalArgumentException("string must not be null");
    }
    if ((fromIndex | toIndex) < 0
        || toIndex < fromIndex
        || string.length() < toIndex) {
      throw new IndexOutOfBoundsException();
    }
    return toIndex - fromIndex;
  }

  /**
   * An all-in-one check for the provided size, from-index and to-index. Verifies
   * that the segment defined by the specified from-index and to-index stays within
   * the boundaries of an array or array-like object with the specified size. More
   * precisely:
   *
   * <ol>
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code size} or {@code fromIndex} or {@code toIndex} is less than zero
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is less than {@code fromIndex}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is greater than the size of the list
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param size the size (or length) of the array, string, list, etc.
   * @param fromIndex the start index of the segment
   * @param toIndex the end index of the segment
   * @return the {@code length} of the segment
   */
  public static int fromTo(int size, int fromIndex, int toIndex) {
    if ((size | fromIndex | toIndex) < 0 || toIndex < fromIndex || size < toIndex) {
      throw new IndexOutOfBoundsException();
    }
    return toIndex - fromIndex;
  }

  /**
   * Throws an {@code IllegalArgumentException} with the specified message and
   * message arguments. The method is still declared to return a value of type
   * {@code <T>} so it can be used as the expression for a {@code return} statement.
   *
   * @param <T> the desired type of the return value
   * @param message The message
   * @param msgArgs The message argument
   * @return nothing, but allows {@code fail} to be used as the expression in a
   *     {@code return} statement
   */
  public static <T> T fail(String message, Object... msgArgs) {
    return failOn(DEF_EXC_FACTORY, message, msgArgs);
  }

  /**
   * Throw the exception supplied by the specified {@code Supplier}. The method is
   * still declared to return a value of type {@code <T>} so it can be used as the
   * expression for a {@code return} statement.
   *
   * @param excFactory The supplier of the exception
   * @param <T> the desired type of the return value
   * @param <X> the type of the exception
   * @return nothing, but allows {@code fail} to be used as the expression in a
   *     {@code return} statement
   * @throws X always
   */
  public static <T, X extends Throwable> T fail(Supplier<X> excFactory) throws X {
    throw excFactory.get();
  }

  /**
   * Throws an exception created by the specified exception factory with the
   * specified message and message arguments.
   *
   * @param <T> the type of the object that would have been returned if it had
   *     passed the checks
   * @param <X> the type of the exception
   * @param message The message
   * @param msgArgs The message argument
   * @return nothing, but allows {@code fail} to be used as the expression in a
   *     {@code return} statement
   * @throws X always
   */
  public static <T, X extends Throwable> T failOn(
      Function<String, X> excFactory, String message, Object... msgArgs) throws X {
    if (msgArgs == null
        || (msgArgs.length == 1 && msgArgs[0] == EOM)
        || message == null) {
      throw excFactory.apply(message);
    }
    throw excFactory.apply(CustomMsgFormatter.formatSimple(message, msgArgs));
  }

}
