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
final class ObjectCheckHelper1<T, X extends Exception> {

  static <T, X extends Exception> ObjectCheckHelper1<T, X> help(ObjectCheck<T, X> check) {
    return new ObjectCheckHelper1<>(check);
  }

  private final ObjectCheck<T, X> check;

  private ObjectCheckHelper1(ObjectCheck<T, X> check) {
    this.check = check;
  }

  <P> ObjectCheck<T, X> has(Function<T, P> prop, String name, Predicate<P> test)
      throws X {
    ObjectCheck<T, X> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(check.FQN(name), val));
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

  <P> ObjectCheck<T, X> notHas(Function<T, P> prop, String name, Predicate<P> test)
      throws X {
    ObjectCheck<T, X> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(check.FQN(name), val));
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

  <P> ObjectCheck<T, X> has(Function<T, P> prop, Predicate<P> test) throws X {
    ObjectCheck<T, X> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, null, null));
  }

  <P> ObjectCheck<T, X> notHas(Function<T, P> prop, Predicate<P> test) throws X {
    ObjectCheck<T, X> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, null, null));
  }

  <P> ObjectCheck<T, X> has(Function<T, P> prop,
      Predicate<P> test,
      String msg,
      Object[] msgArgs)
      throws X {
    ObjectCheck<T, X> check = this.check;
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

  <P> ObjectCheck<T, X> notHas(Function<T, P> prop,
      Predicate<P> test,
      String msg,
      Object[] msgArgs)
      throws X {
    ObjectCheck<T, X> check = this.check;
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

  <P, X2 extends Exception> ObjectCheck<T, X> has(
      Function<T, P> prop, Predicate<P> test, Supplier<X2> exc) throws X2 {
    ObjectCheck<T, X> check = this.check;
    if (test.test(prop.apply(check.arg))) {
      return check;
    }
    throw exc.get();
  }

  public <P, O> ObjectCheck<T, X> has(Function<T, P> prop,
      Relation<P, O> test,
      O obj) throws X {
    ObjectCheck<T, X> check = this.check;
    P val = prop.apply(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
       throw check.exc.apply(getDefaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, null, obj));
  }

  public <P, O> ObjectCheck<T, X> notHas(Function<T, P> prop,
      Relation<P, O> test,
      O obj) throws X {
    ObjectCheck<T, X> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, null, obj));
  }

  <P, O> ObjectCheck<T, X> has(Function<T, P> prop,
      String name,
      Relation<P, O> test,
      O obj)
      throws X {
    ObjectCheck<T, X> check = this.check;
    P val = prop.apply(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(check.FQN(name), val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, check.FQN(name), val, null, obj));
  }

  <P, O> ObjectCheck<T, X> notHas(Function<T, P> prop,
      String name,
      Relation<P, O> test,
      O obj)
      throws X {
    ObjectCheck<T, X> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(check.FQN(name), val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, check.FQN(name), val, null, obj));
  }

  <P, O> ObjectCheck<T, X> has(
      Function<T, P> prop, Relation<P, O> test, O obj, String msg, Object[] msgArgs)
      throws X {
    ObjectCheck<T, X> check = this.check;
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

  <P, O> ObjectCheck<T, X> notHas(
      Function<T, P> prop, Relation<P, O> test, O obj, String msg, Object[] msgArgs)
      throws X {
    ObjectCheck<T, X> check = this.check;
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

  <P, O, X2 extends Exception> ObjectCheck<T, X> has(
      Function<T, P> prop, Relation<P, O> test, O obj, Supplier<X2> exc) throws X2 {
    ObjectCheck<T, X> check = this.check;
    if (test.exists(prop.apply(check.arg), obj)) {
      return check;
    }
    throw exc.get();
  }

}
