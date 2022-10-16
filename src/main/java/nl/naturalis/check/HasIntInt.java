package nl.naturalis.check;

import nl.naturalis.check.relation.IntRelation;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import static nl.naturalis.check.CommonProperties.formatProperty;

/**
 * Helper class for IntCheck.
 */
final class HasIntInt<E extends Exception> {

  static <E0 extends Exception> HasIntInt<E0> get(IntCheck<E0> check) {
    return new HasIntInt<>(check);
  }

  private final IntCheck<E> check;

  private HasIntInt(IntCheck<E> check) {
    this.check = check;
  }

  IntCheck<E> has(IntUnaryOperator prop, IntPredicate test) throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg,
        check.argName,
        prop,
        IntUnaryOperator.class);
    throw check.exc.apply(MsgUtil.getPrefabMessage(test,
        false,
        name,
        val,
        int.class,
        null));
  }

  IntCheck<E> notHas(IntUnaryOperator prop, IntPredicate test) throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg,
        check.argName,
        prop,
        IntUnaryOperator.class);
    throw check.exc.apply(MsgUtil.getPrefabMessage(test,
        true,
        name,
        val,
        int.class,
        null));
  }

  IntCheck<E> has(IntUnaryOperator prop, String name, IntPredicate test) throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.test(val)) {
      return check;
    }
    throw check.exc.apply(
        MsgUtil.getPrefabMessage(test,
            false,
            check.FQN(name),
            val,
            int.class,
            null));
  }

  IntCheck<E> notHas(IntUnaryOperator prop, String name, IntPredicate test)
      throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.test(val)) {
      return check;
    }
    throw check.exc.apply(
        MsgUtil.getPrefabMessage(test, true, check.FQN(name), val, int.class, null));
  }

  IntCheck<E> has(IntUnaryOperator prop,
      IntPredicate test,
      String msg,
      Object[] msgArgs) throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.test(val)) {
      return check;
    }
    throw check.exc.apply(
        MsgUtil.getCustomMessage(msg,
            msgArgs,
            test,
            check.argName,
            val,
            int.class,
            null));
  }

  IntCheck<E> notHas(IntUnaryOperator prop,
      IntPredicate test,
      String msg,
      Object[] msgArgs)
      throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.test(val)) {
      return check;
    }
    throw check.exc.apply(
        MsgUtil.getCustomMessage(msg,
            msgArgs,
            test,
            check.argName,
            val,
            int.class,
            null));
  }

  <X extends Exception> IntCheck<E> has(IntUnaryOperator prop,
      IntPredicate test,
      Supplier<X> exc)
      throws X {
    IntCheck<E> check = this.check;
    if (test.test(prop.applyAsInt(check.arg))) {
      return check;
    }
    throw exc.get();
  }

  IntCheck<E> has(IntUnaryOperator prop, IntRelation test, int obj) throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg,
        check.argName,
        prop,
        IntUnaryOperator.class);
    throw check.exc.apply(MsgUtil.getPrefabMessage(test,
        false,
        name,
        val,
        int.class,
        obj));
  }

  IntCheck<E> notHas(IntUnaryOperator prop, IntRelation test, int obj) throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg,
        check.argName,
        prop,
        IntUnaryOperator.class);
    throw check.exc.apply(MsgUtil.getPrefabMessage(test,
        true,
        name,
        val,
        int.class,
        obj));
  }

  IntCheck<E> has(IntUnaryOperator prop, String name, IntRelation test, int obj)
      throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(
        MsgUtil.getPrefabMessage(test, false, check.FQN(name), val, int.class, obj));
  }

  IntCheck<E> notHas(IntUnaryOperator prop, String name, IntRelation test, int obj)
      throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(
        MsgUtil.getPrefabMessage(test, true, check.FQN(name), val, int.class, obj));
  }

  IntCheck<E> has(IntUnaryOperator prop,
      IntRelation test,
      int obj,
      String msg,
      Object[] msgArgs)
      throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(
        MsgUtil.getCustomMessage(msg,
            msgArgs,
            test,
            check.argName,
            val,
            int.class,
            obj));
  }

  IntCheck<E> notHas(IntUnaryOperator prop,
      IntRelation test,
      int obj,
      String msg,
      Object[] msgArgs)
      throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(
        MsgUtil.getCustomMessage(msg,
            msgArgs,
            test,
            check.argName,
            val,
            int.class,
            obj));
  }

  <X extends Exception> IntCheck<E> has(
      IntUnaryOperator prop, IntRelation test, int obj, Supplier<X> exc) throws X {
    IntCheck<E> check = this.check;
    if (test.exists(prop.applyAsInt(check.arg), obj)) {
      return check;
    }
    throw exc.get();
  }

}
