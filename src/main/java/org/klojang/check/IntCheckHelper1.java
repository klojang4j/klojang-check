package org.klojang.check;

import org.klojang.check.x.msg.MsgArgs;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.klojang.check.CommonProperties.formatProperty;
import static org.klojang.check.x.msg.CheckDefs.getPredicateFormatter;
import static org.klojang.check.x.msg.MsgUtil.*;

final class IntCheckHelper1<X extends Exception> {

  static <X extends Exception> IntCheckHelper1<X> help(IntCheck<X> check) {
    return new IntCheckHelper1<>(check);
  }

  private final IntCheck<X> check;

  private IntCheckHelper1(IntCheck<X> check) {
    this.check = check;
  }

  <P> IntCheck<X> has(IntFunction<P> prop, Predicate<P> test) throws X {
    IntCheck<X> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, IntFunction.class);
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, null, null));
  }

  <P> IntCheck<X> notHas(IntFunction<P> prop, Predicate<P> test) throws X {
    IntCheck<X> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, IntFunction.class);
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, null, null));
  }

  <P> IntCheck<X> has(IntFunction<P> prop, String name, Predicate<P> test) throws X {
    IntCheck<X> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(check.FQN(name), val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, check.FQN(name), val, null, null));
  }

  <P> IntCheck<X> notHas(IntFunction<P> prop, String name, Predicate<P> test)
      throws X {
    IntCheck<X> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(check.FQN(name), val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, check.FQN(name), val, null, null));
  }

  <P> IntCheck<X> has(IntFunction<P> prop,
      Predicate<P> test,
      String msg,
      Object[] msgArgs)
      throws X {
    IntCheck<X> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    throw check.exc.apply(
        getCustomMessage(msg,
            msgArgs,
            test,
            check.argName,
            val,
            null,
            null));
  }

  <P> IntCheck<X> notHas(IntFunction<P> prop,
      Predicate<P> test,
      String msg,
      Object[] msgArgs)
      throws X {
    IntCheck<X> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    throw check.exc.apply(
        getCustomMessage(msg,
            msgArgs,
            test,
            check.argName,
            val,
            null,
            null));
  }

  <P, X2 extends Exception> IntCheck<X> has(
      IntFunction<P> prop, Predicate<P> test, Supplier<X2> exception) throws X2 {
    IntCheck<X> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    throw exception.get();
  }

  /* Not currently exposed via API, but let's keep them around:

    <P, O> IntCheck<E> has(IntFunction<P> prop, Relation<P, O> test, O obj) throws E {
      IntCheck<E> check = this.check;
      P val = prop.apply(check.arg);
      if (test.exists(val, obj)) {
        return check;
      }
      String name = formatProperty(check.arg, check.argName, prop, IntFunction.class);
      throw check.exc.apply(getPrefabMessage(test, false, name, val, null, obj));
    }

    <P, O> IntCheck<E> notHas(IntFunction<P> prop, Relation<P, O> test, O obj) throws E {
      IntCheck<E> check = this.check;
      P val = prop.apply(check.arg);
      if (!test.exists(val, obj)) {
        return check;
      }
      String name = formatProperty(check.arg, check.argName, prop, IntFunction.class);
      throw check.exc.apply(getPrefabMessage(test, true, name, val, null, obj));
    }

    <P, O> IntCheck<E> has(IntFunction<P> prop, String name, Relation<P, O> test, O obj) throws E {
      IntCheck<E> check = this.check;
      P val = prop.apply(check.arg);
      if (test.exists(val, obj)) {
        return check;
      }
      throw check.exc.apply(getPrefabMessage(test, false, check.FQN(name), val, null, obj));
    }

    <P, O> IntCheck<E> notHas(IntFunction<P> prop, String name, Relation<P, O> test, O obj) throws E {
      IntCheck<E> check = this.check;
      P val = prop.apply(check.arg);
      if (!test.exists(val, obj)) {
        return check;
      }
      throw check.exc.apply(getPrefabMessage(test, true, check.FQN(name), val, null, obj));
    }

    <P, O> IntCheck<E> has(
        IntFunction<P> prop, Relation<P, O> test, O obj, String msg, Object[] msgArgs) throws E {
      IntCheck<E> check = this.check;
      P val = prop.apply(check.arg);
      if (test.exists(val, obj)) {
        return check;
      }
      throw check.exc.apply(getCustomMessage(msg, msgArgs, test, check.argName, val, null, obj));
    }

    <P, O> IntCheck<E> notHas(
        IntFunction<P> prop, Relation<P, O> test, O obj, String msg, Object[] msgArgs) throws E {
      IntCheck<E> check = this.check;
      P val = prop.apply(check.arg);
      if (!test.exists(val, obj)) {
        return check;
      }
      throw check.exc.apply(getCustomMessage(msg, msgArgs, test, check.argName, val, null, obj));
    }

    <P, O, X2 extends Exception> IntCheck<E> has(
        IntFunction<P> prop, Relation<P, O> test, O obj, Supplier<X2> exception) throws X2 {
      IntCheck<E> check = this.check;
      if (test.exists(prop.apply(check.arg), obj)) {
        return check;
      }
      throw exception.get();
    }
  */
}
