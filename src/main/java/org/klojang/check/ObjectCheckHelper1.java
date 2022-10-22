package org.klojang.check;

import static org.klojang.check.x.msg.CheckDefs.getPredicateFormatter;
import static org.klojang.check.x.msg.CheckDefs.getRelationFormatter;
import static org.klojang.check.CommonProperties.formatProperty;
import static org.klojang.check.x.msg.MsgUtil.*;
import static org.klojang.check.x.msg.MsgUtil.getPrefabMessage;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.klojang.check.relation.Relation;
import org.klojang.check.x.msg.MsgArgs;

/**
 * Helper class for ObjectCheck.
 */
final class ObjectCheckHelper1<T, E extends Exception> {

  static <T0, E0 extends Exception> ObjectCheckHelper1<T0, E0> get(ObjectCheck<T0, E0> check) {
    return new ObjectCheckHelper1<>(check);
  }

  private final ObjectCheck<T, E> check;

  private ObjectCheckHelper1(ObjectCheck<T, E> check) {
    this.check = check;
  }

  <P> ObjectCheck<T, E> has(Function<T, P> prop, String name, Predicate<P> test)
      throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultPredicateMessage(check.FQN(name), val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter,
            test,
            false,
            check.FQN(name),
            val,
            null,
            null));
  }

  <P> ObjectCheck<T, E> notHas(Function<T, P> prop, String name, Predicate<P> test)
      throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultPredicateMessage(check.FQN(name), val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter,
            test,
            true,
            check.FQN(name),
            val,
            null,
            null));
  }

  <P> ObjectCheck<T, E> has(Function<T, P> prop, Predicate<P> test) throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, null, null));
  }

  <P> ObjectCheck<T, E> notHas(Function<T, P> prop, Predicate<P> test) throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, null, null));
  }

  <P> ObjectCheck<T, E> has(Function<T, P> prop,
      Predicate<P> test,
      String msg,
      Object[] msgArgs)
      throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    throw check.exc.apply(getCustomMessage(msg,
        msgArgs,
        test,
        check.argName,
        val,
        null,
        null));
  }

  <P> ObjectCheck<T, E> notHas(Function<T, P> prop,
      Predicate<P> test,
      String msg,
      Object[] msgArgs)
      throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    throw check.exc.apply(getCustomMessage(msg,
        msgArgs,
        test,
        check.argName,
        val,
        null,
        null));
  }

  <P, X extends Exception> ObjectCheck<T, E> has(
      Function<T, P> prop, Predicate<P> test, Supplier<X> exc) throws X {
    ObjectCheck<T, E> check = this.check;
    if (test.test(prop.apply(check.arg))) {
      return check;
    }
    throw exc.get();
  }

  public <P, O> ObjectCheck<T, E> has(Function<T, P> prop,
      Relation<P, O> test,
      O obj) throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      // Yes, that's correct: we pass null as the property name, and we pass the
      // property name as the property value. This will yield the most intelligible
      // error message. We anyhow don't have much to brew a property name from, and
      // what we have looks like "Function.apply(42.0)" - assuming 42.0 was the
      // argument. That looks much more like the value than the name of what we are
      // comparing to obj.
      throw check.exc.apply(defaultRelationMessage(null, name, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, null, obj));
  }

  public <P, O> ObjectCheck<T, E> notHas(Function<T, P> prop,
      Relation<P, O> test,
      O obj) throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, null, obj));
  }

  <P, O> ObjectCheck<T, E> has(Function<T, P> prop,
      String name,
      Relation<P, O> test,
      O obj)
      throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultRelationMessage(check.FQN(name), val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, check.FQN(name), val, null, obj));
  }

  <P, O> ObjectCheck<T, E> notHas(Function<T, P> prop,
      String name,
      Relation<P, O> test,
      O obj)
      throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultRelationMessage(check.FQN(name), val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, check.FQN(name), val, null, obj));
  }

  <P, O> ObjectCheck<T, E> has(
      Function<T, P> prop, Relation<P, O> test, O obj, String msg, Object[] msgArgs)
      throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(getCustomMessage(msg,
        msgArgs,
        test,
        check.argName,
        val,
        null,
        obj));
  }

  <P, O> ObjectCheck<T, E> notHas(
      Function<T, P> prop, Relation<P, O> test, O obj, String msg, Object[] msgArgs)
      throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(getCustomMessage(msg,
        msgArgs,
        test,
        check.argName,
        val,
        null,
        obj));
  }

  <P, O, X extends Exception> ObjectCheck<T, E> has(
      Function<T, P> prop, Relation<P, O> test, O obj, Supplier<X> exc) throws X {
    ObjectCheck<T, E> check = this.check;
    if (test.exists(prop.apply(check.arg), obj)) {
      return check;
    }
    throw exc.get();
  }

}
