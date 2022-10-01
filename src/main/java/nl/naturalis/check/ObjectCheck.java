package nl.naturalis.check;

import nl.naturalis.common.function.*;

import java.util.function.*;

import static nl.naturalis.check.Check.DEF_ARG_NAME;
import static nl.naturalis.check.MsgUtil.getCustomMessage;
import static nl.naturalis.check.MsgUtil.getPrefabMessage;

/**
 * Facilitates the validation of arbitrarily typed values. See the
 * {@linkplain nl.naturalis.check package description} for a detailed explanation.
 *
 * @param <T> The type of the value to be validated
 * @param <E> The type of the exception thrown if the value does not pass the
 *     test
 */
public final class ObjectCheck<T, E extends Exception> {

  final T arg;
  final String argName;
  final Function<String, E> exc;

  ObjectCheck(T arg, String argName, Function<String, E> exc) {
    this.arg = arg;
    this.argName = argName;
    this.exc = exc;
  }

  /**
   * Returns the value validated by this instance. To be used as the last call after
   * a chain of checks.
   *
   * @return the value validated by this instance
   */
  public T ok() {
    return arg;
  }

  /**
   * Passes the argument to the specified {@code Function} and returns the value it
   * computes. To be used as the last call after a chain of checks. For example:
   *
   * <blockquote>
   *
   * <pre>{@code
   * int age = Check.that(person).has(Person::getAge, "age", lt(), 50).ok(Person::getAge);
   * }</pre>
   *
   * </blockquote>
   *
   * @param <P> the type of the extracted value The type of the returned value
   * @param transformer A {@code Function} that transforms the argument into some
   *     other value
   * @param <X>
   * @return this instance The value computed by the {@code Function}
   * @throws X The exception potentially thrown by the {@code Function}
   */
  public <P, X extends Throwable> P ok(ThrowingFunction<T, P, X> transformer)
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
  public <X extends Throwable> void then(ThrowingConsumer<T, X> consumer) throws X {
    consumer.accept(arg);
  }

  /**
   * Validates the argument using the specified test. While not strictly required,
   * this method is meant to be used with a check from the {@link CommonChecks} class
   * so that an informative error message is generated if the argument turns out to
   * be invalid.
   *
   * @param test the test
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> is(Predicate<T> test) throws E {
    if (test.test(arg)) {
      return this;
    }
    throw exc.apply(getPrefabMessage(test, false, argName, arg, null, null));
  }

  /**
   * Validates the argument using the specified test. While not strictly required,
   * this method is meant to be used with a check from the {@link CommonChecks} class
   * so that an informative error message is generated if the argument turns out to
   * be invalid.
   *
   * @param test the test
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> isNot(Predicate<T> test) throws E {
    if (!test.test(arg)) {
      return this;
    }
    throw exc.apply(getPrefabMessage(test, true, argName, arg, null, null));
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test very often, and performance is absolutely essential, specify
   *     <i>exactly</i> one message argument: {@code '\0'} (the NULL character).
   *     This will cause the message to remain unparsed. It will be passed on as-is
   *     to the exception. (Of course, the message should not contain any message
   *     arguments in that case.)
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> is(Predicate<T> test, String message, Object... msgArgs)
      throws E {
    if (test.test(arg)) {
      return this;
    }
    throw exc.apply(getCustomMessage(message,
        msgArgs,
        test,
        argName,
        arg,
        null,
        null));
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test very often, and performance is absolutely essential, specify
   *     <i>exactly</i> one message argument: {@code '\0'} (the NULL character).
   *     This will cause the message to remain unparsed. It will be passed on as-is
   *     to the exception. (Of course, the message should not contain any message
   *     arguments in that case.)
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> isNot(Predicate<T> test,
      String message,
      Object... msgArgs) throws E {
    // WATCH OUT. Don't call: is(test.negate(), message, msgArgs)
    // If the test came from the CommonChecks class it must preserve its identity
    // in order to be looked up in the CommonChecks.NAMES map
    if (!test.test(arg)) {
      return this;
    }
    throw exc.apply(getCustomMessage(message,
        msgArgs,
        test,
        argName,
        arg,
        null,
        null));
  }

  /**
   * Validates the argument using the specified test. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <X extends Exception> ObjectCheck<T, E> is(Predicate<T> test,
      Supplier<X> exception)
      throws X {
    if (test.test(arg)) {
      return this;
    }
    throw exception.get();
  }

  /**
   * Validates the argument using the specified test. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <X extends Exception> ObjectCheck<T, E> isNot(Predicate<T> test,
      Supplier<X> exception)
      throws X {
    return is(test.negate(), exception);
  }

  /**
   * Validates the argument using the specified test. While not strictly required,
   * this method is meant to be used in combination with a check from the
   * {@link CommonChecks} class so that an informative error message is generated if
   * the argument turns out to be invalid.
   *
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> is(Relation<T, O> test, O object) throws E {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(getPrefabMessage(test, false, argName, arg, null, object));
  }

  /**
   * Validates the argument using the specified test. While not strictly required,
   * this method is meant to be used in combination with a check from the
   * {@link CommonChecks} class so that an informative error message is generated if
   * the argument turns out to be invalid.
   *
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> isNot(Relation<T, O> test, O object) throws E {
    if (!test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(getPrefabMessage(test, true, argName, arg, null, object));
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test very often, and performance is absolutely essential, specify
   *     <i>exactly</i> one message argument: {@code '\0'} (the NULL character).
   *     This will cause the message to remain unparsed. It will be passed on as-is
   *     to the exception. (Of course, the message should not contain any message
   *     arguments in that case.)
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> is(Relation<T, O> test,
      O object,
      String message,
      Object... msgArgs)
      throws E {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(getCustomMessage(message,
        msgArgs,
        test,
        argName,
        arg,
        null,
        object));
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the {@link nl.naturalis.check package description} for how to
   * specify a custom error message.
   *
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test very often, and performance is absolutely essential, specify
   *     <i>exactly</i> one message argument: {@code '\0'} (the NULL character).
   *     This will cause the message to remain unparsed. It will be passed on as-is
   *     to the exception. (Of course, the message should not contain any message
   *     arguments in that case.)
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> isNot(
      Relation<T, O> test, O object, String message, Object... msgArgs) throws E {
    if (!test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(getCustomMessage(message,
        msgArgs,
        test,
        argName,
        arg,
        null,
        object));
  }

  /**
   * Validates the argument using the specified test. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <O> The type of the value being tested against
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O, X extends Exception> ObjectCheck<T, E> is(
      Relation<T, O> test, O object, Supplier<X> exception) throws X {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exception.get();
  }

  /**
   * Validates the argument using the specified test. Allows you to throw a different
   * type of exception for this particular test.
   *
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <O> The type of the value being tested against
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O, X extends Exception> ObjectCheck<T, E> isNot(
      Relation<T, O> test, O object, Supplier<X> exception) throws X {
    return is(test.negate(), object, exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P> ObjectCheck<T, E> has(Function<T, P> property, Predicate<P> test)
      throws E {
    return HasObjObj.get(this).has(property, test);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P> ObjectCheck<T, E> notHas(Function<T, P> property, Predicate<P> test)
      throws E {
    return HasObjObj.get(this).notHas(property, test);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P> ObjectCheck<T, E> has(Function<T, P> property,
      String name,
      Predicate<P> test)
      throws E {
    return HasObjObj.get(this).has(property, name, test);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested.
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P> ObjectCheck<T, E> notHas(Function<T, P> property,
      String name,
      Predicate<P> test)
      throws E {
    return HasObjObj.get(this).notHas(property, name, test);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test very often, and performance is absolutely essential, specify
   *     <i>exactly</i> one message argument: {@code '\0'} (the NULL character).
   *     This will cause the message to remain unparsed. It will be passed on as-is
   *     to the exception. (Of course, the message should not contain any message
   *     arguments in that case.)
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P> ObjectCheck<T, E> has(
      Function<T, P> property, Predicate<P> test, String message, Object... msgArgs)
      throws E {
    return HasObjObj.get(this).has(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments. If you expect the argument to fail the
   *     test very often, and performance is absolutely essential, specify
   *     <i>exactly</i> one message argument: {@code '\0'} (the NULL character).
   *     This will cause the message to remain unparsed. It will be passed on as-is
   *     to the exception. (Of course, the message should not contain any message
   *     arguments in that case.)
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P> ObjectCheck<T, E> notHas(
      Function<T, P> property, Predicate<P> test, String message, Object... msgArgs)
      throws E {
    return HasObjObj.get(this).notHas(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <P> the type of the extracted value
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, X extends Exception> ObjectCheck<T, E> has(
      Function<T, P> property, Predicate<P> test, Supplier<X> exception) throws X {
    return HasObjObj.get(this).has(property, test, exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <P> the type of the extracted value
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, X extends Exception> ObjectCheck<T, E> notHas(
      Function<T, P> property, Predicate<P> test, Supplier<X> exception) throws X {
    return HasObjObj.get(this).has(property, test.negate(), exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <P> the type of the extracted value
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P, O> ObjectCheck<T, E> has(Function<T, P> property,
      Relation<P, O> test,
      O object)
      throws E {
    return HasObjObj.get(this).has(property, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <P> the type of the extracted value
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P, O> ObjectCheck<T, E> notHas(Function<T, P> property,
      Relation<P, O> test,
      O object)
      throws E {
    return HasObjObj.get(this).notHas(property, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <P> the type of the extracted value
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P, O> ObjectCheck<T, E> has(
      Function<T, P> property, String name, Relation<P, O> test, O object) throws E {
    return HasObjObj.get(this).has(property, name, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <P> the type of the extracted value
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P, O> ObjectCheck<T, E> notHas(
      Function<T, P> property, String name, Relation<P, O> test, O object) throws E {
    return HasObjObj.get(this).notHas(property, name, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <P> the type of the extracted value
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P, O> ObjectCheck<T, E> has(
      Function<T, P> property,
      Relation<P, O> test,
      O object,
      String message,
      Object... msgArgs)
      throws E {
    return HasObjObj.get(this).has(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <P> the type of the extracted value
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <P, O> ObjectCheck<T, E> notHas(
      Function<T, P> property,
      Relation<P, O> test,
      O object,
      String message,
      Object... msgArgs)
      throws E {
    return HasObjObj.get(this).notHas(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <P> the type of the extracted value
   * @param <O> The type of the value being tested against
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, O, X extends Exception> ObjectCheck<T, E> has(
      Function<T, P> property, Relation<P, O> test, O object, Supplier<X> exception)
      throws X {
    return HasObjObj.get(this).has(property, test, object, exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <P> the type of the extracted value
   * @param <O> The type of the value being tested against
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, O, X extends Exception> ObjectCheck<T, E> notHas(
      Function<T, P> property, Relation<P, O> test, O object, Supplier<X> exception)
      throws X {
    return HasObjObj.get(this).has(property, test.negate(), object, exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> has(ToIntFunction<T> property, IntPredicate test)
      throws E {
    return HasObjInt.get(this).has(property, test);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> notHas(ToIntFunction<T> property, IntPredicate test)
      throws E {
    return HasObjInt.get(this).notHas(property, test);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested.
   * @param test the test
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> has(ToIntFunction<T> property,
      String name,
      IntPredicate test) throws E {
    return HasObjInt.get(this).has(property, name, test);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested.
   * @param test the test
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> notHas(ToIntFunction<T> property,
      String name,
      IntPredicate test)
      throws E {
    return HasObjInt.get(this).notHas(property, name, test);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> has(
      ToIntFunction<T> property,
      IntPredicate test,
      String message,
      Object... msgArgs) throws E {
    return HasObjInt.get(this).has(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> notHas(
      ToIntFunction<T> property,
      IntPredicate test,
      String message,
      Object... msgArgs) throws E {
    return HasObjInt.get(this).notHas(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <X extends Exception> ObjectCheck<T, E> has(
      ToIntFunction<T> property, IntPredicate test, Supplier<X> exception) throws X {
    return HasObjInt.get(this).has(property, test, exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <X extends Exception> ObjectCheck<T, E> notHas(
      ToIntFunction<T> property, IntPredicate test, Supplier<X> exception) throws X {
    return HasObjInt.get(this).has(property, test.negate(), exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> has(ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object)
      throws E {
    return HasObjInt.get(this).has(property, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> notHas(ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object)
      throws E {
    return HasObjInt.get(this).notHas(property, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> has(
      ToIntFunction<T> property, String name, IntObjRelation<O> test, O object)
      throws E {
    return HasObjInt.get(this).has(property, name, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> notHas(
      ToIntFunction<T> property, String name, IntObjRelation<O> test, O object)
      throws E {
    return HasObjInt.get(this).notHas(property, name, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> has(
      ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object,
      String message,
      Object... msgArgs)
      throws E {
    return HasObjInt.get(this).has(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <O> The type of the value being tested against
   * @return this instance
   * @throws E If the argument is invalid
   */
  public <O> ObjectCheck<T, E> notHas(
      ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object,
      String message,
      Object... msgArgs)
      throws E {
    return HasObjInt.get(this).notHas(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <O> The type of the value being tested against
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O, X extends Exception> ObjectCheck<T, E> has(
      ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object,
      Supplier<X> exception) throws X {
    return HasObjInt.get(this).has(property, test, object, exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <O> The type of the value being tested against
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O, X extends Exception> ObjectCheck<T, E> notHas(
      ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object,
      Supplier<X> exception) throws X {
    return HasObjInt.get(this).has(property, test.negate(), object, exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> has(ToIntFunction<T> property,
      IntRelation test,
      int object) throws E {
    return HasObjInt.get(this).has(property, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class
   * <i>and</i> a "property" from the {@link CommonProperties} class so that an
   * informative error message is generated if the argument turns out to be invalid.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> notHas(ToIntFunction<T> property,
      IntRelation test,
      int object)
      throws E {
    return HasObjInt.get(this).notHas(property, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> has(ToIntFunction<T> property,
      String name,
      IntRelation test,
      int object)
      throws E {
    return HasObjInt.get(this).has(property, name, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. While not strictly required, this method is meant to
   * be used in combination with a check from the {@link CommonChecks} class so that
   * an informative error message is generated if the argument turns out to be
   * invalid.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param name The name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> notHas(
      ToIntFunction<T> property, String name, IntRelation test, int object)
      throws E {
    return HasObjInt.get(this).notHas(property, name, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> has(
      ToIntFunction<T> property,
      IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws E {
    return HasObjInt.get(this).has(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link nl.naturalis.check package description} for how to specify a custom error
   * message.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws E If the argument is invalid
   */
  public ObjectCheck<T, E> notHas(
      ToIntFunction<T> property,
      IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws E {
    return HasObjInt.get(this).notHas(property, test, object, message, msgArgs);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <X extends Exception> ObjectCheck<T, E> has(
      ToIntFunction<T> property, IntRelation test, int object, Supplier<X> exception)
      throws X {
    return HasObjInt.get(this).has(property, test, object, exception);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to throw a different type of exception for
   * this particular test.
   *
   * <p>Note that this method is heavily overloaded. Therefore you need to pay
   * attention when providing a lambda or method reference for <b>both</b> the
   * {@code property} argument <b>and</b> the {@code test} argument. Plain lambdas or
   * method references may cause the compiler to complain about an <b>Ambiguous
   * method call</b>. If so, see the <a
   * href="package-summary.html#custom-checks">package description</a> for what you
   * need to do.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param exception the supplier of the exception to be thrown if the argument
   *     is invalid
   * @param <X> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <X extends Exception> ObjectCheck<T, E> notHas(
      ToIntFunction<T> property, IntRelation test, int object, Supplier<X> exception)
      throws X {
    return HasObjInt.get(this).has(property, test.negate(), object, exception);
  }

  /**
   * Returns a new {@link IntCheck} instance for validating the specified value. The
   * new instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @return a new {@code IntCheck} instance for validating the specified value
   */
  public IntCheck<E> and(int arg) {
    return new IntCheck<>(arg, DEF_ARG_NAME, exc);
  }

  /**
   * Returns a new {@link IntCheck} instance for validating the specified value. The
   * new instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param argName the name of the argument, field or variable being validated
   * @return a new {@code IntCheck} instance for validating the specified value
   */
  public IntCheck<E> and(int arg, String argName) {
    return new IntCheck<>(arg, argName, exc);
  }

  /**
   * Returns a <i>new</i> {@code ObjectCheck} instance for validating the specified
   * value; supposedly another value than the one validated by this instance. The new
   * instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param <T> the type of the value
   * @return an {@link ObjectCheck} instance for validating the specified value
   */
  public <T> ObjectCheck<T, E> and(T arg) {
    return new ObjectCheck<>(arg, DEF_ARG_NAME, exc);
  }

  /**
   * Returns a <i>new</i> {@code ObjectCheck} instance for validating the specified
   * value; supposedly another value than the one validated by this instance. The new
   * instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param argName the name of the argument, field or variable being validated
   * @param <T> the type of the value
   * @return an {@link ObjectCheck} instance for validating the specified value
   */
  public <T> ObjectCheck<T, E> and(T arg, String argName) {
    return new ObjectCheck<>(arg, argName, exc);
  }

  String fullyQualified(String propName) {
    return argName + "." + propName;
  }

}
