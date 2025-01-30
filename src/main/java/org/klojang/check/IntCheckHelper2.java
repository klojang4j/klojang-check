package org.klojang.check;

import org.klojang.check.types.IntRelation;
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
final class IntCheckHelper2<X extends Exception> {

  static <X extends Exception> IntCheckHelper2<X> help(IntCheck<X> check) {
    return new IntCheckHelper2<>(check);
  }

  private final IntCheck<X> check;

  private IntCheckHelper2(IntCheck<X> check) {
    this.check = check;
  }

  IntCheck<X> has(IntUnaryOperator prop, IntPredicate test) throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> notHas(IntUnaryOperator prop, IntPredicate test) throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> has(IntUnaryOperator prop, String name, IntPredicate test) throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> notHas(IntUnaryOperator prop, String name, IntPredicate test)
      throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> has(IntUnaryOperator prop,
      IntPredicate test,
      String msg,
      Object[] msgArgs) throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> notHas(IntUnaryOperator prop,
      IntPredicate test,
      String msg,
      Object[] msgArgs)
      throws X {
    IntCheck<X> check = this.check;
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

  <X2 extends Exception> IntCheck<X> has(IntUnaryOperator prop,
      IntPredicate test,
      Supplier<X2> exc)
      throws X2 {
    IntCheck<X> check = this.check;
    if (test.test(prop.applyAsInt(check.arg))) {
      return check;
    }
    throw exc.get();
  }

  IntCheck<X> has(IntUnaryOperator prop, IntRelation test, int obj) throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> notHas(IntUnaryOperator prop, IntRelation test, int obj) throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> has(IntUnaryOperator prop, String name, IntRelation test, int obj)
      throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> notHas(IntUnaryOperator prop, String name, IntRelation test, int obj)
      throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> has(IntUnaryOperator prop,
      IntRelation test,
      int obj,
      String msg,
      Object[] msgArgs)
      throws X {
    IntCheck<X> check = this.check;
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

  IntCheck<X> notHas(IntUnaryOperator prop,
      IntRelation test,
      int obj,
      String msg,
      Object[] msgArgs)
      throws X {
    IntCheck<X> check = this.check;
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

  <X2 extends Exception> IntCheck<X> has(
      IntUnaryOperator prop, IntRelation test, int obj, Supplier<X2> exc) throws X2 {
    IntCheck<X> check = this.check;
    if (test.exists(prop.applyAsInt(check.arg), obj)) {
      return check;
    }
    throw exc.get();
  }

}
