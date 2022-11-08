package org.klojang.check;

import org.klojang.check.fallible.FallibleConsumer;
import org.klojang.check.fallible.FallibleFunction;
import org.klojang.check.relation.IntObjRelation;
import org.klojang.check.relation.IntRelation;
import org.klojang.check.relation.Relation;
import org.klojang.check.x.msg.MsgArgs;

import java.util.function.*;

import static org.klojang.check.x.msg.CheckDefs.getPredicateFormatter;
import static org.klojang.check.x.msg.CheckDefs.getRelationFormatter;
import static org.klojang.check.x.msg.MsgUtil.*;

/**
 * Facilitates the validation of arbitrarily typed values. See the
 * <a href="../../../module-summary.html">module summary</a> for a detailed
 * explanation.
 *
 * @param <T> The type of the value to be validated
 * @param <X> The type of the exception that is thrown by default if the value
 *     fails a test (this can be overridden for individual tests in the chain of
 *     checks)
 */
public final class ObjectCheck<T, X extends Exception> {

  final T arg;
  final String argName;
  final Function<String, X> exc;

  ObjectCheck(T arg, String argName, Function<String, X> exc) {
    this.arg = arg;
    this.argName = argName;
    this.exc = exc;
  }

  /**
   * Returns the validated value. To be used as the last call after a chain of
   * checks.
   *
   * @return the value validated by this instance
   */
  public T ok() {
    return arg;
  }

  /**
   * Passes the validated value to the specified function and returns the value it
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
   * @param transformer a function that transforms the value validated by this
   *     instance
   * @param <R> the type of the returned value
   * @param <X2> the type of the exception thrown if the transformation fails
   * @return the value computed by the transformation function
   * @throws X2 if the transformation fails
   */
  public <R, X2 extends Throwable> R ok(FallibleFunction<T, R, X2> transformer)
      throws X2 {
    return transformer.apply(arg);
  }

  /**
   * Passes the validated value to a consumer for further processing. To be used as
   * the last call after a chain of checks.
   *
   * @param consumer a consumer of the value validated by this instance
   * @param <X2> the type of the exception thrown if the consumer fails while
   *     processing the value
   * @throws X2 if the consumer fails while processing the value
   */
  public <X2 extends Throwable> void then(FallibleConsumer<T, X2> consumer) throws
      X2 {
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
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> is(Predicate<T> test) throws X {
    if (test.test(arg)) {
      return this;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw exc.apply(getDefaultPredicateMessage(argName, arg));
    }
    throw exc.apply(
        getPrefabMessage(formatter,
            test,
            false,
            argName,
            arg,
            null,
            null));
  }

  /**
   * Validates the argument using the specified test. While not strictly required,
   * this method is meant to be used with a check from the {@link CommonChecks} class
   * so that an informative error message is generated if the argument turns out to
   * be invalid.
   *
   * @param test the test
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> isNot(Predicate<T> test) throws X {
    if (!test.test(arg)) {
      return this;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw exc.apply(getDefaultPredicateMessage(argName, arg));
    }
    throw exc.apply(
        getPrefabMessage(formatter,
            test,
            true,
            argName,
            arg,
            null,
            null));
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
   *
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> is(Predicate<T> test, String message, Object... msgArgs)
      throws X {
    if (test.test(arg)) {
      return this;
    }
    throw exc.apply(
        getCustomMessage(message,
            msgArgs,
            test,
            argName,
            arg,
            null,
            null));
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
   *
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> isNot(Predicate<T> test,
      String message,
      Object... msgArgs) throws X {
    // WATCH OUT. Don't call: is(test.negate(), message, msgArgs)
    // If the test came from the CommonChecks class it must preserve its identity
    // in order to be looked up in the CommonChecks.NAMES map
    if (!test.test(arg)) {
      return this;
    }
    throw exc.apply(
        getCustomMessage(message,
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <X2 extends Exception> ObjectCheck<T, X> is(Predicate<T> test,
      Supplier<X2> exception)
      throws X2 {
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <X2 extends Exception> ObjectCheck<T, X> isNot(Predicate<T> test,
      Supplier<X2> exception)
      throws X2 {
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> is(Relation<T, O> test, O object) throws X {
    if (test.exists(arg, object)) {
      return this;
    }
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      throw exc.apply(getDefaultRelationMessage(argName, arg, object));
    }
    throw exc.apply(
        getPrefabMessage(formatter, test, false, argName, arg, null, object));
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> isNot(Relation<T, O> test, O object) throws X {
    if (!test.exists(arg, object)) {
      return this;
    }
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      throw exc.apply(getDefaultRelationMessage(argName, arg, object));
    }
    throw exc.apply(
        getPrefabMessage(formatter, test, true, argName, arg, null, object));
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
   *
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> is(Relation<T, O> test,
      O object,
      String message,
      Object... msgArgs)
      throws X {
    if (test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(
        getCustomMessage(message,
            msgArgs,
            test,
            argName,
            arg,
            null,
            object));
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
   *
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> isNot(
      Relation<T, O> test, O object, String message, Object... msgArgs) throws X {
    if (!test.exists(arg, object)) {
      return this;
    }
    throw exc.apply(
        getCustomMessage(message,
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
   * @param <O> the type of the value being tested against
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <O, X2 extends Exception> ObjectCheck<T, X> is(
      Relation<T, O> test, O object, Supplier<X2> exception) throws X2 {
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
   * @param <O> the type of the value being tested against
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <O, X2 extends Exception> ObjectCheck<T, X> isNot(
      Relation<T, O> test, O object, Supplier<X2> exception) throws X2 {
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
   * @throws X if the argument is invalid
   */
  public <P> ObjectCheck<T, X> has(Function<T, P> property, Predicate<P> test)
      throws X {
    return ObjectCheckHelper1.help(this).has(property, test);
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
   * @throws X if the argument is invalid
   */
  public <P> ObjectCheck<T, X> notHas(Function<T, P> property, Predicate<P> test)
      throws X {
    return ObjectCheckHelper1.help(this).notHas(property, test);
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
   * @param name the name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P> ObjectCheck<T, X> has(Function<T, P> property,
      String name,
      Predicate<P> test)
      throws X {
    return ObjectCheckHelper1.help(this).has(property, name, test);
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
   * @param name the name of the property being tested
   * @param test the test
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P> ObjectCheck<T, X> notHas(Function<T, P> property,
      String name,
      Predicate<P> test)
      throws X {
    return ObjectCheckHelper1.help(this).notHas(property, name, test);
  }

  /**
   * Validates a property of the argument using the specified test. Allows you to
   * provide a custom error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P> ObjectCheck<T, X> has(
      Function<T, P> property, Predicate<P> test, String message, Object... msgArgs)
      throws X {
    return ObjectCheckHelper1.help(this).has(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the argument using the specified test. Allows you to
   * provide a custom error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @param <P> the type of the extracted value
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P> ObjectCheck<T, X> notHas(
      Function<T, P> property, Predicate<P> test, String message, Object... msgArgs)
      throws X {
    return ObjectCheckHelper1.help(this).notHas(property, test, message, msgArgs);
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <P, X2 extends Exception> ObjectCheck<T, X> has(
      Function<T, P> property, Predicate<P> test, Supplier<X2> exception) throws X2 {
    return ObjectCheckHelper1.help(this).has(property, test, exception);
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <P, X2 extends Exception> ObjectCheck<T, X> notHas(
      Function<T, P> property, Predicate<P> test, Supplier<X2> exception) throws X2 {
    return ObjectCheckHelper1.help(this).has(property, test.negate(), exception);
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, O> ObjectCheck<T, X> has(Function<T, P> property,
      Relation<P, O> test,
      O object)
      throws X {
    return ObjectCheckHelper1.help(this).has(property, test, object);
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, O> ObjectCheck<T, X> notHas(Function<T, P> property,
      Relation<P, O> test,
      O object)
      throws X {
    return ObjectCheckHelper1.help(this).notHas(property, test, object);
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
   * @param name the name of the property being tested.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <P> the type of the extracted value
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, O> ObjectCheck<T, X> has(
      Function<T, P> property, String name, Relation<P, O> test, O object) throws X {
    return ObjectCheckHelper1.help(this).has(property, name, test, object);
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
   * @param name the name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <P> the type of the extracted value
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, O> ObjectCheck<T, X> notHas(
      Function<T, P> property, String name, Relation<P, O> test, O object) throws X {
    return ObjectCheckHelper1.help(this).notHas(property, name, test, object);
  }

  /**
   * Validates a property of the argument using the specified test. Allows you to
   * provide a custom error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, O> ObjectCheck<T, X> has(
      Function<T, P> property,
      Relation<P, O> test,
      O object,
      String message,
      Object... msgArgs)
      throws X {
    return ObjectCheckHelper1.help(this).has(property,
        test,
        object,
        message,
        msgArgs);
  }

  /**
   * Validates a property of the argument using the specified test. Allows you to
   * provide a custom error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <P, O> ObjectCheck<T, X> notHas(
      Function<T, P> property,
      Relation<P, O> test,
      O object,
      String message,
      Object... msgArgs)
      throws X {
    return ObjectCheckHelper1.help(this).notHas(property,
        test,
        object,
        message,
        msgArgs);
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
   * @param <O> the type of the value being tested against
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <P, O, X2 extends Exception> ObjectCheck<T, X> has(
      Function<T, P> property, Relation<P, O> test, O object, Supplier<X2> exception)
      throws X2 {
    return ObjectCheckHelper1.help(this).has(property, test, object, exception);
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
   * @param <O> the type of the value being tested against
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <P, O, X2 extends Exception> ObjectCheck<T, X> notHas(
      Function<T, P> property, Relation<P, O> test, O object, Supplier<X2> exception)
      throws X2 {
    return ObjectCheckHelper1.help(this).has(property,
        test.negate(),
        object,
        exception);
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
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> has(ToIntFunction<T> property, IntPredicate test)
      throws X {
    return ObjectCheckHelper2.help(this).has(property, test);
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
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> notHas(ToIntFunction<T> property, IntPredicate test)
      throws X {
    return ObjectCheckHelper2.help(this).notHas(property, test);
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
   * @param name the name of the property being tested.
   * @param test the test
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> has(ToIntFunction<T> property,
      String name,
      IntPredicate test) throws X {
    return ObjectCheckHelper2.help(this).has(property, name, test);
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
   * @param name the name of the property being tested.
   * @param test the test
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> notHas(ToIntFunction<T> property,
      String name,
      IntPredicate test)
      throws X {
    return ObjectCheckHelper2.help(this).notHas(property, name, test);
  }

  /**
   * Validates a property of the argument using the specified test. Allows you to
   * provide a custom error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> has(
      ToIntFunction<T> property,
      IntPredicate test,
      String message,
      Object... msgArgs) throws X {
    return ObjectCheckHelper2.help(this).has(property, test, message, msgArgs);
  }

  /**
   * Validates a property of the argument using the specified test. Allows you to
   * provide a custom error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
   *
   * @param property a function that transforms the value validated by this
   *     {@code ObjectCheck} into another value, which is to be validated using the
   *     specified test. This would typically be a function that extracts a bean
   *     property from a JavaBean
   * @param test the test
   * @param message the message pattern
   * @param msgArgs the message arguments
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> notHas(
      ToIntFunction<T> property,
      IntPredicate test,
      String message,
      Object... msgArgs) throws X {
    return ObjectCheckHelper2.help(this).notHas(property, test, message, msgArgs);
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <X2 extends Exception> ObjectCheck<T, X> has(
      ToIntFunction<T> property, IntPredicate test, Supplier<X2> exception) throws
      X2 {
    return ObjectCheckHelper2.help(this).has(property, test, exception);
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <X2 extends Exception> ObjectCheck<T, X> notHas(
      ToIntFunction<T> property, IntPredicate test, Supplier<X2> exception) throws
      X2 {
    return ObjectCheckHelper2.help(this).has(property, test.negate(), exception);
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> has(ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object)
      throws X {
    return ObjectCheckHelper2.help(this).has(property, test, object);
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> notHas(ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object)
      throws X {
    return ObjectCheckHelper2.help(this).notHas(property, test, object);
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
   * @param name the name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> has(
      ToIntFunction<T> property, String name, IntObjRelation<O> test, O object)
      throws X {
    return ObjectCheckHelper2.help(this).has(property, name, test, object);
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
   * @param name the name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> notHas(
      ToIntFunction<T> property, String name, IntObjRelation<O> test, O object)
      throws X {
    return ObjectCheckHelper2.help(this).notHas(property, name, test, object);
  }

  /**
   * Validates the argument using the specified test. Allows you to provide a custom
   * error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> has(
      ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object,
      String message,
      Object... msgArgs)
      throws X {
    return ObjectCheckHelper2.help(this).has(property,
        test,
        object,
        message,
        msgArgs);
  }

  /**
   * Validates a property of the argument using the specified test. Allows you to
   * provide a custom error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
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
   * @param <O> the type of the value being tested against
   * @return this instance
   * @throws X if the argument is invalid
   */
  public <O> ObjectCheck<T, X> notHas(
      ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object,
      String message,
      Object... msgArgs)
      throws X {
    return ObjectCheckHelper2.help(this).notHas(property,
        test,
        object,
        message,
        msgArgs);
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
   * @param <O> the type of the value being tested against
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <O, X2 extends Exception> ObjectCheck<T, X> has(
      ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object,
      Supplier<X2> exception) throws X2 {
    return ObjectCheckHelper2.help(this).has(property, test, object, exception);
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
   * @param <O> the type of the value being tested against
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <O, X2 extends Exception> ObjectCheck<T, X> notHas(
      ToIntFunction<T> property,
      IntObjRelation<O> test,
      O object,
      Supplier<X2> exception) throws X2 {
    return ObjectCheckHelper2.help(this).has(property,
        test.negate(),
        object,
        exception);
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
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> has(ToIntFunction<T> property,
      IntRelation test,
      int object) throws X {
    return ObjectCheckHelper2.help(this).has(property, test, object);
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
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> notHas(ToIntFunction<T> property,
      IntRelation test,
      int object)
      throws X {
    return ObjectCheckHelper2.help(this).notHas(property, test, object);
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
   * @param name the name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> has(ToIntFunction<T> property,
      String name,
      IntRelation test,
      int object)
      throws X {
    return ObjectCheckHelper2.help(this).has(property, name, test, object);
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
   * @param name the name of the property being tested. In error messages the
   *     fully-qualified name will be used and constructed using
   *     {@code argName + "." + name}.
   * @param test the test
   * @param object the value that the argument is tested against (called "the
   *     object" of a relation)
   * @return this instance
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> notHas(
      ToIntFunction<T> property, String name, IntRelation test, int object)
      throws X {
    return ObjectCheckHelper2.help(this).notHas(property, name, test, object);
  }

  /**
   * Validates a property of the argument, retrieved through the specified function,
   * using the specified test. Allows you to provide a custom error message. See the
   * {@link org.klojang.check package description} for how to specify a custom error
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
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> has(
      ToIntFunction<T> property,
      IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws X {
    return ObjectCheckHelper2.help(this).has(property,
        test,
        object,
        message,
        msgArgs);
  }

  /**
   * Validates a property of the argument using the specified test. Allows you to
   * provide a custom error message. See the paragraph on <a
   * href="../../../module-summary.html#custom-error-messages">Custom Error
   * Messages</a> in the module description for how to specify a custom message.
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
   * @throws X if the argument is invalid
   */
  public ObjectCheck<T, X> notHas(
      ToIntFunction<T> property,
      IntRelation test,
      int object,
      String message,
      Object... msgArgs)
      throws X {
    return ObjectCheckHelper2.help(this).notHas(property,
        test,
        object,
        message,
        msgArgs);
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <X2 extends Exception> ObjectCheck<T, X> has(
      ToIntFunction<T> property,
      IntRelation test,
      int object,
      Supplier<X2> exception)
      throws X2 {
    return ObjectCheckHelper2.help(this).has(property, test, object, exception);
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
   * @param <X2> the type of the exception thrown if the argument is invalid
   * @return this instance
   * @throws X2 if the argument is invalid
   */
  public <X2 extends Exception> ObjectCheck<T, X> notHas(
      ToIntFunction<T> property,
      IntRelation test,
      int object,
      Supplier<X2> exception)
      throws X2 {
    return ObjectCheckHelper2.help(this).has(property,
        test.negate(),
        object,
        exception);
  }

  /**
   * Returns an {@link IntCheck} instance for validating the specified value. The new
   * instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @return a new {@code IntCheck} instance for validating the specified value
   */
  public IntCheck<X> and(int arg) {
    return new IntCheck<>(arg, DEF_ARG_NAME, exc);
  }

  /**
   * Returns an {@link IntCheck} instance for validating the specified value. The new
   * instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param argName the name of the argument, field or variable being validated
   * @return a new {@code IntCheck} instance for validating the specified value
   */
  public IntCheck<X> and(int arg, String argName) {
    return new IntCheck<>(arg, argName, exc);
  }

  /**
   * Returns a <i>new</i> {@code ObjectCheck} instance for validating the specified
   * value; supposedly another value than the one validated by this instance. The new
   * instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param <U> the type of the value
   * @return a new {@code ObjectCheck} instance for validating the specified value
   */
  public <U> ObjectCheck<U, X> and(U arg) {
    return new ObjectCheck<>(arg, DEF_ARG_NAME, exc);
  }

  /**
   * Returns a <i>new</i> {@code ObjectCheck} instance for validating the specified
   * value; supposedly another value than the one validated by this instance. The new
   * instance inherits the exception factory of this instance.
   *
   * @param arg the value to be validated.
   * @param argName the name of the argument, field or variable being validated
   * @param <U> the type of the value
   * @return a new {@code ObjectCheck} instance for validating the specified value
   */
  public <U> ObjectCheck<U, X> and(U arg, String argName) {
    return new ObjectCheck<>(arg, argName, exc);
  }

  String FQN(String propName) {
    if (argName == null) {
      return propName;
    }
    return argName + "." + propName;
  }

}
