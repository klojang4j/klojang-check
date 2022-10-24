package org.klojang.check;

import org.klojang.check.relation.IntObjRelation;
import org.klojang.check.relation.IntRelation;
import org.klojang.check.x.msg.MsgArgs;

import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static org.klojang.check.CommonProperties.formatProperty;
import static org.klojang.check.x.msg.CheckDefs.*;
import static org.klojang.check.x.msg.MsgUtil.*;

/*
 * Helper class for ObjectCheck. Helps with has() methods that extract and validate an int
 * property of the argument.
 */
final class ObjectCheckHelper2<T, X extends Exception> {

  static <T, X extends Exception> ObjectCheckHelper2<T, X> help(ObjectCheck<T, X> check) {
    return new ObjectCheckHelper2<>(check);
  }

  private final ObjectCheck<T, X> check;

  private ObjectCheckHelper2(ObjectCheck<T, X> check) {
    this.check = check;
  }

  ObjectCheck<T, X> has(ToIntFunction<T> prop, IntPredicate test) throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getIntPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, int.class, null));
  }

  ObjectCheck<T, X> notHas(ToIntFunction<T> prop, IntPredicate test) throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.test(val)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getIntPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(name, val));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, int.class, null));
  }

  ObjectCheck<T, X> has(ToIntFunction<T> prop, String name, IntPredicate test)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getIntPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(check.FQN(name), val));
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

  ObjectCheck<T, X> notHas(ToIntFunction<T> prop, String name, IntPredicate test)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.test(val)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getIntPredicateFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultPredicateMessage(check.FQN(name), val));
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

  ObjectCheck<T, X> has(ToIntFunction<T> prop,
      IntPredicate test,
      String msg,
      Object[] msgArgs)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.test(val)) {
      return check;
    }
    throw check.exc.apply(
        getCustomMessage(msg, msgArgs, test, check.argName, val, int.class, null));
  }

  ObjectCheck<T, X> notHas(
      ToIntFunction<T> prop, IntPredicate test, String msg, Object[] msgArgs)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.test(val)) {
      return check;
    }
    throw check.exc.apply(
        getCustomMessage(msg, msgArgs, test, check.argName, val, int.class, null));
  }

  <X2 extends Exception> ObjectCheck<T, X> has(
      ToIntFunction<T> prop, IntPredicate test, Supplier<X2> exception) throws X2 {
    ObjectCheck<T, X> check = this.check;
    if (test.test(prop.applyAsInt(check.arg))) {
      return check;
    }
    throw exception.get();
  }

  public <O> ObjectCheck<T, X> has(ToIntFunction<T> prop,
      IntObjRelation<O> test,
      O obj) throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getIntObjRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, int.class, obj));
  }

  public <O> ObjectCheck<T, X> notHas(ToIntFunction<T> prop,
      IntObjRelation<O> test,
      O obj)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getIntObjRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, int.class, obj));
  }

  <O> ObjectCheck<T, X> has(ToIntFunction<T> prop,
      String name,
      IntObjRelation<O> test,
      O obj)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getIntObjRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(check.FQN(name), val, obj));
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

  <O> ObjectCheck<T, X> notHas(ToIntFunction<T> prop,
      String name,
      IntObjRelation<O> test,
      O obj)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getIntObjRelationFormatter(test);
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

  <O> ObjectCheck<T, X> has(
      ToIntFunction<T> prop,
      IntObjRelation<O> test,
      O obj,
      String msg,
      Object[] msgArgs) throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(getCustomMessage(msg,
        msgArgs,
        test,
        check.argName,
        val,
        int.class,
        obj));
  }

  <O> ObjectCheck<T, X> notHas(
      ToIntFunction<T> prop,
      IntObjRelation<O> test,
      O obj,
      String msg,
      Object[] msgArgs) throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(getCustomMessage(msg,
        msgArgs,
        test,
        check.argName,
        val,
        int.class,
        obj));
  }

  <O, X2 extends Exception> ObjectCheck<T, X> has(
      ToIntFunction<T> prop, IntObjRelation<O> test, O obj, Supplier<X2> exception)
      throws X2 {
    ObjectCheck<T, X> check = this.check;
    if (test.exists(prop.applyAsInt(check.arg), obj)) {
      return check;
    }
    throw exception.get();
  }

  public ObjectCheck<T, X> has(ToIntFunction<T> prop, IntRelation test, int obj)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getIntRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, false, name, val, int.class, obj));
  }

  public ObjectCheck<T, X> notHas(ToIntFunction<T> prop, IntRelation test, int obj)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    String name = formatProperty(check.arg, check.argName, prop, Function.class);
    Function<MsgArgs, String> formatter = getIntRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(name, val, obj));
    }
    throw check.exc.apply(
        getPrefabMessage(formatter, test, true, name, val, int.class, obj));
  }

  ObjectCheck<T, X> has(ToIntFunction<T> prop,
      String name,
      IntRelation test,
      int obj) throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    Function<MsgArgs, String> formatter = getIntRelationFormatter(test);
    if (formatter == null) {
      throw check.exc.apply(getDefaultRelationMessage(check.FQN(name), val, obj));
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

  ObjectCheck<T, X> notHas(ToIntFunction<T> prop,
      String name,
      IntRelation test,
      int obj) throws X {
    ObjectCheck<T, X> check = this.check;
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

  ObjectCheck<T, X> has(
      ToIntFunction<T> prop, IntRelation test, int obj, String msg, Object[] msgArgs)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(getCustomMessage(msg,
        msgArgs,
        test,
        check.argName,
        val,
        int.class,
        obj));
  }

  ObjectCheck<T, X> notHas(
      ToIntFunction<T> prop, IntRelation test, int obj, String msg, Object[] msgArgs)
      throws X {
    ObjectCheck<T, X> check = this.check;
    int val = prop.applyAsInt(check.arg);
    if (!test.exists(val, obj)) {
      return check;
    }
    throw check.exc.apply(getCustomMessage(msg,
        msgArgs,
        test,
        check.argName,
        val,
        int.class,
        obj));
  }

  <X2 extends Exception> ObjectCheck<T, X> has(
      ToIntFunction<T> prop, IntRelation test, int obj, Supplier<X2> exception)
      throws X2 {
    ObjectCheck<T, X> check = this.check;
    if (test.exists(prop.applyAsInt(check.arg), obj)) {
      return check;
    }
    throw exception.get();
  }

}
