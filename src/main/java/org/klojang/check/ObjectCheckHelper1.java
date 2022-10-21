package org.klojang.check;

import static org.klojang.check.CommonProperties.formatProperty;
import static org.klojang.check.MsgUtil.getCustomMessage;
import static org.klojang.check.MsgUtil.getPrefabMessage;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.klojang.check.relation.Relation;

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
    throw check.exc.apply(getPrefabMessage(test,
        false,
        check.fullyQualified(name),
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
    throw check.exc.apply(getPrefabMessage(test,
        true,
        check.fullyQualified(name),
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
    throw check.exc.apply(getPrefabMessage(test, false, name, val, null, null));
  }

  <P> ObjectCheck<T, E> notHas(Function<T, P> prop, Predicate<P> test) throws E {
    ObjectCheck<T, E> check = this.check;
    P val = prop.apply(check.arg);
    if (!test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    throw check.exc.apply(getPrefabMessage(test, true, name, val, null, null));
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
    throw check.exc.apply(getPrefabMessage(test, false, name, val, null, obj));
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
    throw check.exc.apply(getPrefabMessage(test, true, name, val, null, obj));
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
    throw check.exc.apply(getPrefabMessage(test,
        false,
        check.fullyQualified(name),
        val,
        null,
        obj));
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
    throw check.exc.apply(getPrefabMessage(test,
        true,
        check.fullyQualified(name),
        val,
        null,
        obj));
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
