package org.klojang.check;

import org.klojang.check.x.msg.MsgArgs;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.klojang.check.CommonProperties.formatProperty;
import static org.klojang.check.x.msg.CheckDefs.getPredicateFormatter;
import static org.klojang.check.x.msg.MsgUtil.*;

final class IntCheckHelper1<E extends Exception> {

  static <E0 extends Exception> IntCheckHelper1<E0> get(IntCheck<E0> check) {
    return new IntCheckHelper1<>(check);
  }

  private final IntCheck<E> check;

  private IntCheckHelper1(IntCheck<E> check) {
    this.check = check;
  }

  <P> IntCheck<E> has(IntFunction<P> prop, Predicate<P> test) throws E {
    IntCheck<E> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, IntFunction.class);
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, null, null));
  }

  <P> IntCheck<E> notHas(IntFunction<P> prop, Predicate<P> test) throws E {
    IntCheck<E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, IntFunction.class);
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, null, null));
  }

  <P> IntCheck<E> has(IntFunction<P> prop, String name, Predicate<P> test) throws E {
    IntCheck<E> check = this.check;
    P val = prop.apply(check.arg);
    if (test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultPredicateMessage(check.FQN(name), val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, check.FQN(name), val, null, null));
  }

  <P> IntCheck<E> notHas(IntFunction<P> prop, String name, Predicate<P> test)
      throws E {
    IntCheck<E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(defaultPredicateMessage(check.FQN(name), val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, check.FQN(name), val, null, null));
  }

  <P> IntCheck<E> has(IntFunction<P> prop,
      Predicate<P> test,
      String msg,
      Object[] msgArgs)
      throws E {
    IntCheck<E> check = this.check;
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

  <P> IntCheck<E> notHas(IntFunction<P> prop,
      Predicate<P> test,
      String msg,
      Object[] msgArgs)
      throws E {
    IntCheck<E> check = this.check;
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

  <P, X extends Exception> IntCheck<E> has(
      IntFunction<P> prop, Predicate<P> test, Supplier<X> exception) throws X {
    IntCheck<E> check = this.check;
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

    <P, O, X extends Exception> IntCheck<E> has(
        IntFunction<P> prop, Relation<P, O> test, O obj, Supplier<X> exception) throws X {
      IntCheck<E> check = this.check;
      if (test.exists(prop.apply(check.arg), obj)) {
        return check;
      }
      throw exception.get();
    }
  */
}
