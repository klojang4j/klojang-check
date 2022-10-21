package org.klojang.check;

import org.klojang.check.x.ArrayInfo;
import org.klojang.check.x.Misc;
import org.klojang.check.x.msg.PrefabMsgFormatter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static java.lang.System.identityHashCode;
import static org.klojang.check.x.Misc.*;

@SuppressWarnings({"rawtypes"})
public final class MsgUtil {

  private MsgUtil() {
    throw new UnsupportedOperationException();
  }

  // Max display width (characters) for stringified values.
  public static final int MAX_STRING_WIDTH = 65;

  public static final String WAS = " (was ";

  public static String defaultPredicateMessage(String argName, Object argVal) {
    if (argName == null) {
      return "invalid value: " + toStr(argVal);
    }
    return "invalid value for " + argName + ": " + toStr(argVal);
  }

  public static String defaultRelationMessage(String argName,
      Object argVal,
      Object obj) {
    if (argName == null) {
      return "no such relation between " + toStr(argVal) + " and " + toStr(obj);
    }
    return "argument "
        + argName
        + ": no such relation between "
        + toStr(argVal)
        + " and "
        + toStr(obj);
  }

  public static String formatMessage(Function<MsgArgs, String> formatter,
      Object test,
      boolean negated,
      String argName,
      Object argVal,
      Class<?> argType,
      Object obj) {
    return formatter.apply(new MsgArgs(test,
        negated,
        argName,
        argVal,
        argType,
        obj));
  }

  public static String getCustomMessage(String msg,
      Object[] msgArgs,
      Object test,
      String argName,
      Object argVal,
      Class<?> argType,
      Object obj) {
    if (msgArgs == null || msg == null) {
      return msg;
    } else if (msgArgs.length == 0) {
      return CustomMsgFormatter.formatWithPrefabArgsOnly(msg,
          new Object[] {test, argVal, argType, argName, obj});
    }
    Object[] all = new Object[msgArgs.length + 5];
    all[0] = test;
    all[1] = argVal;
    all[2] = argType;
    all[3] = argName;
    all[4] = obj;
    System.arraycopy(msgArgs, 0, all, 5, msgArgs.length);
    return CustomMsgFormatter.format(msg, all);
  }

  public static String toStr(Object val) {
    if (val == null) {
      return "null";
    } else if (val instanceof String s) {
      return s.isBlank()
          ? '"' + ellipsis(s, MAX_STRING_WIDTH) + '"'
          : ellipsis(s, MAX_STRING_WIDTH);
    } else if (val instanceof Collection<?> c) {
      if (c.size() == 0) {
        return c.getClass().getSimpleName() + "[0]";
      }
      String s = toShortString(val, MAX_STRING_WIDTH);
      return c.getClass().getSimpleName() + '[' + c.size() + "] of " + s;
    } else if (val instanceof Map<?, ?> m) {
      if (m.size() == 0) {
        return m.getClass().getSimpleName() + "[0]";
      }
      String s = toShortString(val, MAX_STRING_WIDTH);
      return m.getClass().getSimpleName() + '[' + m.size() + "] of " + s;
    } else if (val.getClass().isArray()) {
      int len = getArrayLength(val);
      if (len == 0) {
        return ArrayInfo.describe(val);
      }
      return ArrayInfo.describe(val) + " of " + toShortString(val, MAX_STRING_WIDTH);
    }
    return toShortString(val, MAX_STRING_WIDTH);
  }

  public static String className(Object obj) {
    if (obj.getClass().getPackageName().startsWith("java.lang")) {
      return simpleClassName(obj);
    }
    return obj.getClass() == Class.class
        ? Misc.className((Class) obj)
        : Misc.className(obj.getClass());
  }

  public static String simpleClassName(Object obj) {
    return obj.getClass() == Class.class
        ? Misc.simpleClassName((Class) obj)
        : Misc.simpleClassName(obj.getClass());
  }

  public static String identify(Object arg) {
    if (arg == null) {
      return null;
    } else if (arg instanceof Enum<?> e) {
      return e.name();
    } else if (arg instanceof Class<?> c) {
      return simpleClassName(c) + ".class";
    }
    return simpleClassName(arg) + '@' + identityHashCode(arg);
  }

}
