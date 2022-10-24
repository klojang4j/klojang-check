package org.klojang.check;

import org.klojang.check.relation.IntRelation;
import org.klojang.check.x.msg.MsgArgs;

import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import static org.klojang.check.CommonProperties.formatProperty;
import static org.klojang.check.x.msg.CheckDefs.getIntPredicateFormatter;
import static org.klojang.check.x.msg.CheckDefs.getIntRelationFormatter;
import static org.klojang.check.x.msg.MsgUtil.*;

/**
 * Helper class for IntCheck.
 */
final class IntCheckHelper2<E extends Exception> {

  static <E0 extends Exception> IntCheckHelper2<E0> help(IntCheck<E0> check) {
    return new IntCheckHelper2<>(check);
  }

  private final IntCheck<E> check;

  private IntCheckHelper2(IntCheck<E> check) {
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
    Function<MsgArgs, String> formatter = getIntPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, int.class, null));
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
    Function<MsgArgs, String> formatter = getIntPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(name, check.arg));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, int.class, null));
  }

  IntCheck<E> has(IntUnaryOperator prop, String name, IntPredicate test) throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getIntPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(check.FQN(name), check.arg));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter,
            test,
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
    Function<MsgArgs, String> formatter = getIntPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(check.FQN(name), check.arg));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter,
            test,
            true,
            check.FQN(name),
            val,
            int.class,
            null));
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
        getCustomMessage(msg,
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
        getCustomMessage(msg,
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
    Function<MsgArgs, String> formatter = getIntRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, int.class, obj));
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
    Function<MsgArgs, String> formatter = getIntRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, int.class, obj));
  }

  IntCheck<E> has(IntUnaryOperator prop, String name, IntRelation test, int obj)
      throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getIntRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(check.FQN(name), check.arg, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter,
            test,
            false,
            check.FQN(name),
            val,
            int.class,
            obj));
  }

  IntCheck<E> notHas(IntUnaryOperator prop, String name, IntRelation test, int obj)
      throws E {
    IntCheck<E> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getIntRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(check.FQN(name), val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter,
            test,
            true,
            check.FQN(name),
            val,
            int.class,
            obj));
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
        getCustomMessage(msg,
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
        getCustomMessage(msg,
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
