package nl.naturalis.check;

import nl.naturalis.common.ArrayType;
import nl.naturalis.common.ClassMethods;
import nl.naturalis.common.StringMethods;
import nl.naturalis.common.x.invoke.InvokeUtils;

import java.util.Collection;
import java.util.Map;

import static java.lang.System.identityHashCode;
import static nl.naturalis.check.Check.EOM;
import static nl.naturalis.check.CommonChecks.MESSAGE_PATTERNS;
import static nl.naturalis.common.StringMethods.ellipsis;

@SuppressWarnings({"rawtypes"})
final class MsgUtil {

  static final String ERR_NULL_MESSAGE = "message and message argument must not be null";

  private MsgUtil() {
    throw new AssertionError();
  }

  // Max display width (characters) for stringified values.
  static final int MAX_STRING_WIDTH = 65;

  static String getPrefabMessage(Object test,
      boolean negated,
      String argName,
      Object argVal,
      Class<?> argType,
      Object obj) {
    PrefabMsgFormatter formatter = MESSAGE_PATTERNS.get(test);
    if (formatter == null) {
      if (argName == null) {
        return "invalid value: " + toStr(argVal);
      }
      return "invalid value for " + argName + ": " + toStr(argVal);
    }
    return formatter.apply(new MsgArgs(test,
        negated,
        argName,
        argVal,
        argType,
        obj));
  }

  static String getCustomMessage(String msg,
      Object[] msgArgs,
      Object test,
      String argName,
      Object argVal,
      Class<?> argType,
      Object obj) {
    if (msgArgs == null
        || (msgArgs.length == 1 && msgArgs[0] == EOM)
        || msg == null) {
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

  //////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////

  static String toStr(Object val) {
    if (val == null) {
      return "null";
    } else if (val instanceof String s) {
      return s.isBlank() ? '"' + s + '"' : ellipsis(s, MAX_STRING_WIDTH);
    } else if (val instanceof Collection<?> c) {
      if (c.size() == 0) {
        return c.getClass().getSimpleName() + "[0]";
      }
      String s = StringMethods.toShortString(val, MAX_STRING_WIDTH);
      return c.getClass().getSimpleName() + '[' + c.size() + "] of " + s;
    } else if (val instanceof Map<?, ?> m) {
      if (m.size() == 0) {
        return m.getClass().getSimpleName() + "[0]";
      }
      String s = StringMethods.toShortString(val, MAX_STRING_WIDTH);
      return m.getClass().getSimpleName() + '[' + m.size() + "] of " + s;
    } else if (val.getClass().isArray()) {
      return arrayToString(val);
    }
    return StringMethods.toShortString(val, MAX_STRING_WIDTH);
  }

  private static String arrayToString(Object val) {
    Class<?> c = val.getClass();
    ArrayType at = ArrayType.forClass(c);
    String baseType = at.baseType().getSimpleName();
    int len = InvokeUtils.getArrayLength(val);
    if (len == 0) {
      if (at.dimensions() == 1) { // happy path
        return baseType + "[0]";
      }
      return baseType + "[0]"
          + "[]".repeat(Math.max(0, at.dimensions() - 1));
    }
    String s = StringMethods.toShortString(val, MAX_STRING_WIDTH);
    return baseType + '[' + len + ']'
        + "[]".repeat(Math.max(0, at.dimensions() - 1))
        + " of " + s;
  }

  static String className(Object obj) {
    if (obj.getClass().getPackageName().startsWith("java.lang")) {
      return simpleClassName(obj);
    }
    return obj.getClass() == Class.class
        ? ClassMethods.className((Class) obj)
        : ClassMethods.className(obj.getClass());
  }

  static String simpleClassName(Object obj) {
    return obj.getClass() == Class.class
        ? ClassMethods.simpleClassName((Class) obj)
        : ClassMethods.simpleClassName(obj.getClass());
  }

  static String identify(Object arg) {
    if (arg == null) {
      return null;
    } else if (arg instanceof Enum<?> e) {
      return e.name();
    } else if (arg instanceof Class<?> c) {
      return ClassMethods.simpleClassName(c) + ".class";
    }
    return ClassMethods.simpleClassName(arg) + '@' + identityHashCode(arg);
  }

  static final String WAS = " (was ";

}
