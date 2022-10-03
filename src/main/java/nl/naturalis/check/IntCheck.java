package nl.naturalis.check;

import nl.naturalis.base.function.IntObjRelation;
import nl.naturalis.base.function.IntRelation;
import nl.naturalis.base.function.ThrowingIntConsumer;
import nl.naturalis.base.function.ThrowingIntFunction;
import nl.naturalis.check.MsgUtil;

import java.util.function.*;

import static nl.naturalis.check.Check.DEF_ARG_NAME;

/**
 * Facilitates the validation of {@code int} values. See the
 * {@link nl.naturalis.check package description} for a detailed explanation.
 *
 * @param <E> The type of the exception throw if the value fails a test
 */
public final class IntCheck<E extends Exception> {

  final int arg;
  final String argName;
  final Function<String, E> exc;

  IntCheck(int arg, String argName, Function<String, E> exc) {
    this.arg = arg;
    this.argName = argName;
    this.exc = exc;
  }

  /**
   * Returns the {@code int} value validated by this instance. To be used as the last
   * call after a chain of checks.
   *
   * @return the {@code int} value validated by this instance
   */
  public int ok() {
    return arg;
  }

  /**
   * Passes the {@code int} value validated by this instance to the specified
   * {@code Function} and returns the value it computes. To be used as the last call
   * after a chain of checks. For example:
   *
   * <blockquote>
   *
   * <pre>{@code
   * int moderate = Check.that(t, "temperature").has(abs(), lt(), 30).ok(abs());
   * }</pre>
   *
   * </blockquote>
   *
   * @param transformer A {@code Function} that transforms the argument into some
   *     other value
   * @param <R> The type of the returned value
   * @param <X> the type of the exception thrown by the transformer function
   * @return the value computed by the {@code Function}
   * @throws X if the transformation function fails while processing the
   *     {@code int} value
   */
  public <R, X extends Throwable> R ok(ThrowingIntFunction<R, X> transformer)
      throws X {
    return transformer.apply(arg);
  }

  /**
   * Passes the value validated by this instance to the specified {@code Consumer}.
   * To be used as the last call after a chain of checks.
   *
   * @param consumer The {@code Consumer}
   * @param <X> the type of the exception thrown by the consumer
   * @throws X if the consumer fails while processing the value
   */
  public <X extends Throwable> void then(ThrowingIntConsumer<X> consumer) throws X {
    consumer.accept(arg);
  }

  /**
   * Validates the {@code int} value using the specified test. While not strictly
   * required, this method is meant to be passed a check from the
   * {@link CommonChecks} class. When providing your own lambda or method reference,
   * the error message wil not be very intelligible.
   *
   * @param test the test
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public IntCheck<E> is(IntPredicate test) throws E {
    if (test.test(arg)) {
      return this;
    }
    throw exc.apply(MsgUtil.getPrefabMessage(test,
        false,
        argName,
        arg,
        int.class,
        null));
  }

  /**
   * Validates the {@code int} value using the specified test. While not strictly
   * required, this method is meant to be passed a check from the
   * {@link CommonChecks} class. When providing your own lambda or method reference,
   * the error message wil not be very intelligible.
   *
   * @param test the test
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public IntCheck<E> isNot(IntPredicate test) throws E {
    if (!test.test(arg)) {
      return this;
    }
    throw exc.apply(MsgUtil.getPrefabMessage(test,
        true,
        argName,
        arg,
        int.class,
        null));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to provide
   * a custom error message. See the {@link nl.naturalis.check package description}
   * for how to specify a custom error message.
   *
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test repetitively, and performance, even while handling the exception, is
   *     critical, specify <i>exactly</i> one message argument: {@code '\0'} (the
   *     NULL character). This will cause the message not to be parsed, and simply be
   *     passed as-is to the exception. (Of course, the message should not contain
   *     any message arguments then.)
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public IntCheck<E> is(IntPredicate test, String message, Object... msgArgs)
      throws E {
    if (test.test(arg)) {
      return this;
    }
    throw exc.apply(
        MsgUtil.getCustomMessage(message,
            msgArgs,
            test,
            argName,
            arg,
            int.class,
            null));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to provide
   * a custom error message. See the {@link nl.naturalis.check package description}
   * for how to specify a custom error message.
   *
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test repetitively, and performance, even while handling the exception, is
   *     critical, specify <i>exactly</i> one message argument: {@code '\0'} (the
   *     NULL character). This will cause the message not to be parsed, and simply be
   *     passed as-is to the exception. (Of course, the message should not contain
   *     any message arguments then.)
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public IntCheck<E> isNot(IntPredicate test, String message, Object... msgArgs)
      throws E {
    // WATCH OUT. Do not call: is(test.negate(), message, msgArgs). If the test came
    // from the CommonChecks class it must preserve its identity in order to be looked
    // up in the CommonChecks.NAMES map
    if (!test.test(arg)) {
      return this;
    }
    throw exc.apply(
        MsgUtil.getCustomMessage(message,
            msgArgs,
            test,
            argName,
            arg,
            int.class,
            null));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to throw a
   * different type of exception for this particular test.
   *
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid The {@code Supplier} of the exception to be thrown if the
   *     argument is invalid
   * @param <X> the type of the exception thrown if the {@code int} value does
   *     not pass the test
   * @return this instance
   * @throws X if the {@code int} value does not pass the test
   */
  public <X extends Exception> IntCheck<E> is(IntPredicate test,
      Supplier<X> exception) throws X {
    if (test.test(arg)) {
      return this;
    }
    throw exception.get();
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to throw a
   * different type of exception for this particular test.
   *
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid The {@code Supplier} of the exception to be thrown if the
   *     argument is invalid
   * @param <X> the type of the exception thrown if the {@code int} value does
   *     not pass the test
   * @return this instance
   * @throws X if the {@code int} value does not pass the test
   */
  public <X extends Exception> IntCheck<E> isNot(IntPredicate test,
      Supplier<X> exception)
      throws X {
    return is(test.negate(), exception);
  }

  /**
   * Validates the {@code int} value using the specified test. While not strictly
   * required, this method is meant to be passed a check from the
   * {@link CommonChecks} class. When providing your own lambda or method reference,
   * the error message wil not be very intelligible.
   *
   * @param test the test
   * @param object the object of the {@code Relation}
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public IntCheck<E> is(IntRelation test, int object) throws E {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(MsgUtil.getPrefabMessage(test,
        false,
        argName,
        arg,
        int.class,
        object));
  }

  /**
   * Validates the {@code int} value using the specified test. While not strictly
   * required, this method is meant to be passed a check from the
   * {@link CommonChecks} class. When providing your own lambda or method reference,
   * the error message wil not be very intelligible.
   *
   * @param test the test
   * @param object the object of the {@code Relation}
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public IntCheck<E> isNot(IntRelation test, int object) throws E {
    if (!test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(MsgUtil.getPrefabMessage(test,
        true,
        argName,
        arg,
        int.class,
        object));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to provide
   * a custom error message. See the {@link nl.naturalis.check package description}
   * for how to specify a custom error message.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test repetitively, and performance, even while handling the exception, is
   *     critical, specify <i>exactly</i> one message argument: {@code '\0'} (the
   *     NULL character). This will cause the message not to be parsed, and simply be
   *     passed as-is to the exception. (Of course, the message should not contain
   *     any message arguments then.)
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public IntCheck<E> is(IntRelation test,
      int object,
      String message,
      Object... msgArgs) throws E {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(
        MsgUtil.getCustomMessage(message,
            msgArgs,
            test,
            argName,
            arg,
            int.class,
            object));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to provide
   * a custom error message. See the {@link nl.naturalis.check package description}
   * for how to specify a custom error message.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test repetitively, and performance, even while handling the exception, is
   *     critical, specify <i>exactly</i> one message argument: {@code '\0'} (the
   *     NULL character). This will cause the message not to be parsed, and simply be
   *     passed as-is to the exception. (Of course, the message should not contain
   *     any message arguments then.)
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public IntCheck<E> isNot(IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws E {
    if (!test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(
        MsgUtil.getCustomMessage(message,
            msgArgs,
            test,
            argName,
            arg,
            int.class,
            object));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to throw a
   * different type of exception for this particular test.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid The {@code Supplier} of the exception to be thrown if the
   *     argument is invalid
   * @param <X> the type of the exception thrown if the {@code int} value does
   *     not pass the test
   * @return this instance
   * @throws X if the {@code int} value does not pass the test
   */
  public <X extends Exception> IntCheck<E> is(IntRelation test,
      int object,
      Supplier<X> exception)
      throws X {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exception.get();
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to throw a
   * different type of exception for this particular test.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid The {@code Supplier} of the exception to be thrown if the
   *     argument is invalid
   * @param <X> the type of the exception thrown if the {@code int} value does
   *     not pass the test
   * @return this instance
   * @throws X if the {@code int} value does not pass the test
   */
  public <X extends Exception> IntCheck<E> isNot(
      IntRelation test, int object, Supplier<X> exception) throws X {
    return is(test.negate(), object, exception);
  }

  /**
   * Validates the {@code int} value using the specified test. While not strictly
   * required, this method is meant to be passed a check from the
   * {@link CommonChecks} class. When providing your own lambda or method reference,
   * the error message wil not be very intelligible.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param <O> The type of the object of the {@code IntObjRelation}
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public <O> IntCheck<E> is(IntObjRelation<O> test, O object) throws E {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(MsgUtil.getPrefabMessage(test,
        false,
        argName,
        arg,
        int.class,
        object));
  }

  /**
   * Validates the {@code int} value using the specified test. While not strictly
   * required, this method is meant to be passed a check from the
   * {@link CommonChecks} class. When providing your own lambda or method reference,
   * the error message wil not be very intelligible.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param <O> the type of the object of the {@code IntObjRelation}
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public <O> IntCheck<E> isNot(IntObjRelation<O> test, O object) throws E {
    if (!test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(MsgUtil.getPrefabMessage(test,
        true,
        argName,
        arg,
        int.class,
        object));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to provide
   * a custom error message. See the {@link nl.naturalis.check package description}
   * for how to specify a custom error message.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <O> the type of the object of the {@code IntObjRelation}
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public <O> IntCheck<E> is(IntObjRelation<O> test,
      O object,
      String message,
      Object... msgArgs)
      throws E {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(
        MsgUtil.getCustomMessage(message,
            msgArgs,
            test,
            argName,
            arg,
            int.class,
            object));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to provide
   * a custom error message. See the {@link nl.naturalis.check package description}
   * for how to specify a custom error message.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <O> the type of the object of the {@code IntObjRelation}
   * @return this instance
   * @throws E if the {@code int} value does not pass the test
   */
  public <O> IntCheck<E> isNot(IntObjRelation<O> test,
      O object,
      String message,
      Object... msgArgs)
      throws E {
    if (!test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(
        MsgUtil.getCustomMessage(message,
            msgArgs,
            test,
            argName,
            arg,
            int.class,
            object));
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to throw a
   * different type of exception for this particular test.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid The {@code Supplier} of the exception to be thrown if the
   *     argument is invalid
   * @param <O> the type of the object of the {@code IntObjRelation}
   * @param <X> the type of exception thrown if the {@code int} value does not
   *     pass the test
   * @return this instance
   * @throws X if the {@code int} value does not pass the test
   */
  public <O, X extends Exception> IntCheck<E> is(
      IntObjRelation<O> test, O object, Supplier<X> exception) throws X {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exception.get();
  }

  /**
   * Validates the {@code int} value using the specified test. Allows you to throw a
   * different type of exception for this particular test.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid The {@code Supplier} of the exception to be thrown if the
   *     argument is invalid
   * @param <O> the type of the object of the {@code IntObjRelation}
   * @param <X> the type of exception thrown if the {@code int} value does not
   *     pass the test
   * @return this instance
   * @throws X if the {@code int} value does not pass the test
   */
  public <O, X extends Exception> IntCheck<E> isNot(
      IntObjRelation<O> test, O object, Supplier<X> exception) throws X {
    return is(test.negate(), object, exception);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public <P> IntCheck<E> has(IntFunction<P> property, Predicate<P> test) throws E {
    return HasIntObj.get(this).has(property, test);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public <P> IntCheck<E> notHas(IntFunction<P> property, Predicate<P> test)
      throws E {
    return HasIntObj.get(this).notHas(property, test);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param name The name of the property being tested.
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public <P> IntCheck<E> has(IntFunction<P> property, String name, Predicate<P> test)
      throws E {
    return HasIntObj.get(this).has(property, name, test);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param name The name of the property being tested.
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public <P> IntCheck<E> notHas(IntFunction<P> property,
      String name,
      Predicate<P> test) throws E {
    return HasIntObj.get(this).notHas(property, name, test);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public <P> IntCheck<E> has(
      IntFunction<P> property, Predicate<P> test, String message, Object... msgArgs)
      throws E {
    return HasIntObj.get(this).has(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E if the argument is invalid
   */
  public <P> IntCheck<E> notHas(
      IntFunction<P> property, Predicate<P> test, String message, Object... msgArgs)
      throws E {
    return HasIntObj.get(this).notHas(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <P> the type of the extracted value
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, X extends Exception> IntCheck<E> has(
      IntFunction<P> property, Predicate<P> test, Supplier<X> exception) throws X {
    return HasIntObj.get(this).has(property, test, exception);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <P> the type of the extracted value
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, X extends Exception> IntCheck<E> notHas(
      IntFunction<P> property, Predicate<P> test, Supplier<X> exception) throws X {
    return HasIntObj.get(this).has(property, test.negate(), exception);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> has(IntUnaryOperator property, IntPredicate test) throws E {
    return HasIntInt.get(this).has(property, test);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> notHas(IntUnaryOperator property, IntPredicate test) throws E {
    return HasIntInt.get(this).notHas(property, test);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param name The name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> has(IntUnaryOperator property, String name, IntPredicate test)
      throws E {
    return HasIntInt.get(this).has(property, name, test);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param name The name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> notHas(IntUnaryOperator property,
      String name,
      IntPredicate test) throws E {
    return HasIntInt.get(this).notHas(property, name, test);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> has(
      IntUnaryOperator property,
      IntPredicate test,
      String message,
      Object... msgArgs) throws E {
    return HasIntInt.get(this).has(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> notHas(
      IntUnaryOperator property,
      IntPredicate test,
      String message,
      Object... msgArgs) throws E {
    return HasIntInt.get(this).notHas(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <X extends Exception> IntCheck<E> has(
      IntUnaryOperator property, IntPredicate test, Supplier<X> exception) throws X {
    return HasIntInt.get(this).has(property, test, exception);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <X extends Exception> IntCheck<E> notHas(
      IntUnaryOperator property, IntPredicate test, Supplier<X> exception) throws X {
    return HasIntInt.get(this).has(property, test.negate(), exception);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param object the object of the {@code IntRelation}
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> has(IntUnaryOperator property, IntRelation test, int object)
      throws E {
    return HasIntInt.get(this).has(property, test, object);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param object the object of the {@code IntRelation}
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> notHas(IntUnaryOperator property, IntRelation test, int object)
      throws E {
    return HasIntInt.get(this).notHas(property, test, object);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param name The name of the property being tested
   * @param test the test
   * @param object the object of the {@code IntRelation}
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> has(IntUnaryOperator property,
      String name,
      IntRelation test,
      int object)
      throws E {
    return HasIntInt.get(this).has(property, name, test, object);
  }

  /**
   * Validates a property of the {@code int} value. While not strictly required, this
   * method is meant to be passed a check from the {@link CommonChecks} class
   * <i>and</i> a property extractor function from the {@link CommonProperties}
   * class. When providing your own lambdas and/or method references, the error
   * message wil not be very intelligible
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param name a descriptive name for the property being tested
   * @param test the test
   * @param object the object of the {@code IntRelation}
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> notHas(IntUnaryOperator property,
      String name,
      IntRelation test,
      int object)
      throws E {
    return HasIntInt.get(this).notHas(property, name, test, object);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param object the object of the {@code IntRelation}
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> has(
      IntUnaryOperator property,
      IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws E {
    return HasIntInt.get(this).has(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param object the object of the {@code IntRelation}
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws E if the property does not pass the test
   */
  public IntCheck<E> notHas(
      IntUnaryOperator property,
      IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws E {
    return HasIntInt.get(this).notHas(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param object the object of the {@code IntRelation}
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the property does not pass
   *     the test
   * @return this instance
   * @throws X if the property does not pass the test
   */
  public <X extends Exception> IntCheck<E> has(
      IntUnaryOperator property, IntRelation test, int object, Supplier<X> exception)
      throws X {
    return HasIntInt.get(this).has(property, test, object, exception);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param object the object of the {@code IntRelation}
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the property does not pass
   *     the test
   * @return this instance
   * @throws X if the property does not pass the test
   */
  public <X extends Exception> IntCheck<E> notHas(
      IntUnaryOperator property, IntRelation test, int object, Supplier<X> exception)
      throws X {
    return has(property, test.negate(), object, exception);
  }

  /**
   * Returns a <i>new</i> {@code IntCheck} instance for validating the specified
   * {@code int} value. This allows you to chain not just multiple checks for a
   * single argument, but also multiple checks for multiple arguments:
   *
   * <blockquote><pre>{@code
   * Check.that(list).is(notNull())
   *  .and(off).is(gte(), 0)
   *  .and(len).is(gte(), 0)
   *  .and(off+len).is(lte(), list.size());
   * }</pre></blockquote>
   *
   * <p>The new instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @return an {@link IntCheck} instance for validating the specified value
   */
  public IntCheck<E> and(int arg) {
    return new IntCheck<>(arg, DEF_ARG_NAME, exc);
  }

  /**
   * Returns a <i>new</i> {@code IntCheck} instance for validating the specified
   * value. The new instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param argName the name of the argument, field or variable being validated
   * @return an {@link IntCheck} instance for validating the specified value
   */
  public IntCheck<E> and(int arg, String argName) {
    return new IntCheck<>(arg, argName, exc);
  }

  /**
   * Returns a new {@link ObjectCheck} instance for validating the specified value.
   * The new instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param <T> the type of the value to be validated
   * @return a new {@code IntCheck} instance for validating the specified value
   */
  public <T> ObjectCheck<T, E> and(T arg) {
    return new ObjectCheck<>(arg, DEF_ARG_NAME, exc);
  }

  /**
   * Returns a new {@link ObjectCheck} instance for validating the specified value.
   * The new instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param argName the name of the argument, field or variable being validated
   * @param <T> the type of the value to be validated
   * @return a new {@code IntCheck} instance for validating the specified value
   */
  public <T> ObjectCheck<T, E> and(T arg, String argName) {
    return new ObjectCheck<>(arg, argName, exc);
  }

  /* Returns fully-qualified name of the property with the specified name */
  String FQN(String name) {
    if (argName == null) {
      return name;
    }
    return argName + "." + name;
  }

}
