package org.klojang.check;

import static org.klojang.check.Check.DEF_ARG_NAME;

import java.util.function.*;

import org.klojang.check.fallible.FallibleIntConsumer;
import org.klojang.check.fallible.FallibleIntFunction;
import org.klojang.check.relation.IntObjRelation;
import org.klojang.check.relation.IntRelation;

/**
 * Facilitates the validation of {@code int} values. See the
 * {@link org.klojang.check package description} for a detailed explanation.
 *
 * @param <X> The type of the exception that is thrown by default if the value
 *     fails a test (this can be overridden for individual tests in the chain of
 *     checks)
 */
public final class IntCheck<X extends Exception> {

  final int arg;
  final String argName;
  final Function<String, X> exc;

  IntCheck(int arg, String argName, Function<String, X> exc) {
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
   * @param <X2> the type of the exception thrown if the function fails while
   *     processing the value
   * @return the value computed by the {@code Function}
   * @throws X2 if the transformation function fails while processing the
   *     {@code int} value
   */
  public <R, X2 extends Throwable> R ok(FallibleIntFunction<R, X2> transformer)
      throws X2 {
    return transformer.apply(arg);
  }

  /**
   * Passes the value validated by this instance to the specified {@code Consumer}.
   * To be used as the last call after a chain of checks.
   *
   * @param consumer The {@code Consumer}
   * @param <X2> the type of the exception thrown if the consumer fails while
   *     processing the value
   * @throws X2 if the consumer fails while processing the value
   */
  public <X2 extends Throwable> void then(FallibleIntConsumer<X2> consumer)
      throws X2 {
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
   * @throws X if the {@code int} value does not pass the test
   */
  public IntCheck<X> is(IntPredicate test) throws X {
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
   * @throws X if the {@code int} value does not pass the test
   */
  public IntCheck<X> isNot(IntPredicate test) throws X {
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
   * a custom error message. See the {@link org.klojang.check package description}
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
   * @throws X if the {@code int} value does not pass the test
   */
  public IntCheck<X> is(IntPredicate test, String message, Object... msgArgs)
      throws X {
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
   * a custom error message. See the {@link org.klojang.check package description}
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
   * @throws X if the {@code int} value does not pass the test
   */
  public IntCheck<X> isNot(IntPredicate test, String message, Object... msgArgs)
      throws X {
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
   * @param <X2> the type of the exception thrown if the {@code int} value does
   *     not pass the test
   * @return this instance
   * @throws X2 if the {@code int} value does not pass the test
   */
  public <X2 extends Exception> IntCheck<X> is(IntPredicate test,
      Supplier<X2> exception) throws X2 {
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
   * @param <X2> the type of the exception thrown if the {@code int} value does
   *     not pass the test
   * @return this instance
   * @throws X2 if the {@code int} value does not pass the test
   */
  public <X2 extends Exception> IntCheck<X> isNot(IntPredicate test,
      Supplier<X2> exception)
      throws X2 {
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
   * @throws X if the {@code int} value does not pass the test
   */
  public IntCheck<X> is(IntRelation test, int object) throws X {
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
   * @throws X if the {@code int} value does not pass the test
   */
  public IntCheck<X> isNot(IntRelation test, int object) throws X {
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
   * a custom error message. See the {@link org.klojang.check package description}
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
   * @throws X if the {@code int} value does not pass the test
   */
  public IntCheck<X> is(IntRelation test,
      int object,
      String message,
      Object... msgArgs) throws X {
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
   * a custom error message. See the {@link org.klojang.check package description}
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
   * @throws X if the {@code int} value does not pass the test
   */
  public IntCheck<X> isNot(IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws X {
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
   * @param <X2> the type of the exception thrown if the {@code int} value does
   *     not pass the test
   * @return this instance
   * @throws X2 if the {@code int} value does not pass the test
   */
  public <X2 extends Exception> IntCheck<X> is(IntRelation test,
      int object,
      Supplier<X2> exception)
      throws X2 {
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
   * @param <X2> the type of the exception thrown if the {@code int} value does
   *     not pass the test
   * @return this instance
   * @throws X2 if the {@code int} value does not pass the test
   */
  public <X2 extends Exception> IntCheck<X> isNot(IntRelation test,
      int object,
      Supplier<X2> exception) throws X2 {
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
   * @throws X if the {@code int} value does not pass the test
   */
  public <O> IntCheck<X> is(IntObjRelation<O> test, O object) throws X {
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
   * @throws X if the {@code int} value does not pass the test
   */
  public <O> IntCheck<X> isNot(IntObjRelation<O> test, O object) throws X {
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
   * a custom error message. See the {@link org.klojang.check package description}
   * for how to specify a custom error message.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <O> the type of the object of the {@code IntObjRelation}
   * @return this instance
   * @throws X if the {@code int} value does not pass the test
   */
  public <O> IntCheck<X> is(IntObjRelation<O> test,
      O object,
      String message,
      Object... msgArgs)
      throws X {
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
   * a custom error message. See the {@link org.klojang.check package description}
   * for how to specify a custom error message.
   *
   * @param test the test
   * @param object the object of the {@code IntObjRelation}
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <O> the type of the object of the {@code IntObjRelation}
   * @return this instance
   * @throws X if the {@code int} value does not pass the test
   */
  public <O> IntCheck<X> isNot(IntObjRelation<O> test,
      O object,
      String message,
      Object... msgArgs)
      throws X {
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
   * @param <X2> the type of exception thrown if the {@code int} value does not
   *     pass the test
   * @return this instance
   * @throws X2 if the {@code int} value does not pass the test
   */
  public <O, X2 extends Exception> IntCheck<X> is(
      IntObjRelation<O> test, O object, Supplier<X2> exception) throws X2 {
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
   * @param <X2> the type of exception thrown if the {@code int} value does not
   *     pass the test
   * @return this instance
   * @throws X2 if the {@code int} value does not pass the test
   */
  public <O, X2 extends Exception> IntCheck<X> isNot(
      IntObjRelation<O> test, O object, Supplier<X2> exception) throws X2 {
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
   * @throws X if the property does not pass the test
   */
  public <P> IntCheck<X> has(IntFunction<P> property, Predicate<P> test) throws X {
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
   * @throws X if the property does not pass the test
   */
  public <P> IntCheck<X> notHas(IntFunction<P> property, Predicate<P> test)
      throws X {
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
   * @throws X if the property does not pass the test
   */
  public <P> IntCheck<X> has(IntFunction<P> property, String name, Predicate<P> test)
      throws X {
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
   * @throws X if the property does not pass the test
   */
  public <P> IntCheck<X> notHas(IntFunction<P> property,
      String name,
      Predicate<P> test) throws X {
    return HasIntObj.get(this).notHas(property, name, test);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link org.klojang.check package description} for how to
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
   * @throws X if the property does not pass the test
   */
  public <P> IntCheck<X> has(
      IntFunction<P> property, Predicate<P> test, String message, Object... msgArgs)
      throws X {
    return HasIntObj.get(this).has(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link org.klojang.check package description} for how to
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
   * @throws X if the argument is invalid
   */
  public <P> IntCheck<X> notHas(
      IntFunction<P> property, Predicate<P> test, String message, Object... msgArgs)
      throws X {
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <P, X2 extends Exception> IntCheck<X> has(
      IntFunction<P> property, Predicate<P> test, Supplier<X2> exception) throws X2 {
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <P, X2 extends Exception> IntCheck<X> notHas(
      IntFunction<P> property, Predicate<P> test, Supplier<X2> exception) throws X2 {
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> has(IntUnaryOperator property, IntPredicate test) throws X {
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> notHas(IntUnaryOperator property, IntPredicate test) throws X {
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> has(IntUnaryOperator property, String name, IntPredicate test)
      throws X {
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> notHas(IntUnaryOperator property,
      String name,
      IntPredicate test) throws X {
    return HasIntInt.get(this).notHas(property, name, test);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link org.klojang.check package description} for how to
   * specify a custom error message.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> has(
      IntUnaryOperator property,
      IntPredicate test,
      String message,
      Object... msgArgs) throws X {
    return HasIntInt.get(this).has(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link org.klojang.check package description} for how to
   * specify a custom error message.
   *
   * @param property a function that exposes some property of the {@code int}
   *     value (for example its absolute value or its square root), which is then
   *     validated using the specified test
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> notHas(
      IntUnaryOperator property,
      IntPredicate test,
      String message,
      Object... msgArgs) throws X {
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <X2 extends Exception> IntCheck<X> has(
      IntUnaryOperator property, IntPredicate test, Supplier<X2> exception) throws
      X2 {
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <X2 extends Exception> IntCheck<X> notHas(IntUnaryOperator property,
      IntPredicate test,
      Supplier<X2> exception) throws X2 {
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> has(IntUnaryOperator property, IntRelation test, int object)
      throws X {
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> notHas(IntUnaryOperator property, IntRelation test, int object)
      throws X {
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> has(IntUnaryOperator property,
      String name,
      IntRelation test,
      int object)
      throws X {
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> notHas(IntUnaryOperator property,
      String name,
      IntRelation test,
      int object)
      throws X {
    return HasIntInt.get(this).notHas(property, name, test, object);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link org.klojang.check package description} for how to
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> has(
      IntUnaryOperator property,
      IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws X {
    return HasIntInt.get(this).has(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the {@code int} value. Allows you to provide a custom
   * error message. See the {@link org.klojang.check package description} for how to
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
   * @throws X if the property does not pass the test
   */
  public IntCheck<X> notHas(
      IntUnaryOperator property,
      IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws X {
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
   * @param <X2> the type of the exception thrown if the property does not pass
   *     the test
   * @return this instance
   * @throws X2 if the property does not pass the test
   */
  public <X2 extends Exception> IntCheck<X> has(
      IntUnaryOperator property,
      IntRelation test,
      int object,
      Supplier<X2> exception)
      throws X2 {
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
   * @param <X2> the type of the exception thrown if the property does not pass
   *     the test
   * @return this instance
   * @throws X2 if the property does not pass the test
   */
  public <X2 extends Exception> IntCheck<X> notHas(
      IntUnaryOperator property,
      IntRelation test,
      int object,
      Supplier<X2> exception)
      throws X2 {
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
  public IntCheck<X> and(int arg) {
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
  public IntCheck<X> and(int arg, String argName) {
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
  public <T> ObjectCheck<T, X> and(T arg) {
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
  public <T> ObjectCheck<T, X> and(T arg, String argName) {
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
