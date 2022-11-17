package org.klojang.check.x.msg;

import org.klojang.check.x.Misc;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

import static org.klojang.check.x.Misc.toShortString;
import static org.klojang.check.x.msg.MsgUtil.*;

/*
 * Formats client-provided messages.
 */
public final class CustomMsgFormatter {

  private CustomMsgFormatter() {}

  private static final String ARG_START = "${";
  private static final char ARG_END = '}';

  /*
   * It turns out we consistently get +/- 50% better performance if we hand-code the
   * string replacements. Since clients will probably have low tolerance for argument
   * checks costing them anything but a few CPU cycles, let's go for it.
   * Nevertheless, keep the regex around.
   */
  private static final String REGEX = "\\$\\{(tag|arg|obj|type|test|\\d+)}";

  @SuppressWarnings("unused")
  private static final Pattern PATTERN = Pattern.compile(REGEX);

  // Will be called if the user provided at least one message argument, implying the
  // message pattern will contain ${0}, ${1} etc. and possibly also predefined
  // message arguments like ${arg}, ${tag}, ...
  static String format(String msg, Object[] msgArgs) {
    int x;
    if ((x = msg.indexOf(ARG_START)) == -1) {
      return msg;
    }
    StringBuilder sb = new StringBuilder(msg.length() + 20);
    int y = 0;
    do {
      sb.append(msg, y, x);
      if ((y = msg.indexOf(ARG_END, x += 2)) == -1) {
        return sb.append(ARG_START).append(msg, x, msg.length()).toString();
      }
      sb.append(getArgVal(msg.substring(x, y), msgArgs));
      if ((x = msg.indexOf(ARG_START, y += 1)) == -1) {
        return sb.append(msg, y, msg.length()).toString();
      }
    } while (true);
  }

  // Only ${arg}, ${tag}, etc.
  static String formatWithPrefabArgs(String msg, Object[] msgArgs) {
    int x;
    if ((x = msg.indexOf(ARG_START)) == -1) {
      return msg;
    }
    StringBuilder sb = new StringBuilder(msg.length() + 20);
    int y = 0;
    do {
      sb.append(msg, y, x);
      if ((y = msg.indexOf(ARG_END, x += 2)) == -1) {
        return sb.append(ARG_START).append(msg, x, msg.length()).toString();
      }
      sb.append(getPrefabArgVal(msg.substring(x, y), msgArgs));
      if ((x = msg.indexOf(ARG_START, y += 1)) == -1) {
        return sb.append(msg, y, msg.length()).toString();
      }
    } while (true);
  }

  // Only  ${0}, ${1}, etc. Used by Check.fail()
  public static String formatWithUserArgs(String msg, Object[] msgArgs) {
    int x;
    if ((x = msg.indexOf(ARG_START)) == -1) {
      return msg;
    }
    StringBuilder out = new StringBuilder(msg.length() + 20);
    int y = 0;
    do {
      out.append(msg, y, x);
      if ((y = msg.indexOf(ARG_END, x += 2)) == -1) {
        return out.append(ARG_START).append(msg, x, msg.length()).toString();
      }
      out.append(getUserArgVal(msg.substring(x, y), msgArgs));
      if ((x = msg.indexOf(ARG_START, y += 1)) == -1) {
        return out.append(msg, y, msg.length()).toString();
      }
    } while (true);
  }

  //@formatter:off
  private static final Map<String, Function<Object[], String>> ARG_LOOKUPS =
      Map.of(
          "test", CustomMsgFormatter::getCheck,
          "arg",  args -> toShortString(args[1], MAX_STRING_WIDTH),
          "type", CustomMsgFormatter::getType,
          "tag",  CustomMsgFormatter::getTag,
          "obj",  args -> toShortString(args[4], MAX_STRING_WIDTH));
  //@formatter:on

  private static String getArgVal(String arg, Object[] args) {
    Function<Object[], String> fnc = ARG_LOOKUPS.get(arg);
    if (fnc != null) {
      return fnc.apply(args);
    }
    try {
      int i = 5 + new BigInteger(arg).intValueExact();
      if (i < args.length) {
        return Objects.toString(args[i]);
      }
    } catch(NumberFormatException ignore) {
    }
    return ARG_START + arg + ARG_END;
  }

  // ${test}, ${arg} ... etc.
  private static String getPrefabArgVal(String arg, Object[] args) {
    Function<Object[], String> fnc = ARG_LOOKUPS.get(arg);
    if (fnc != null) {
      return fnc.apply(args);
    }
    return ARG_START + arg + ARG_END;
  }

  // ${0}, ${1} ... etc.
  private static String getUserArgVal(String arg, Object[] args) {
    try {
      int i = new BigInteger(arg).intValueExact();
      if (i >= 0 && i < args.length) {
        return Objects.toString(args[i]);
      }
    } catch (NumberFormatException ignore) {
    }
    return ARG_START + arg + ARG_END;
  }

  private static String getCheck(Object[] args) {
    String name;
    if ((name = CheckDefs.nameOf(args[0])) != null) {
      return name;
    }
    return args[0].getClass().getSimpleName();
  }

  private static String getTag(Object[] args) {
    if (args[3] == null) {
      return DEF_ARG_NAME;
    }
    return args[3].toString();
  }

  private static String getType(Object[] args) {
    if (args[2] == null) { // the type of the validated value
      if (args[1] != null) { // the validated value
        return Misc.describe(args[1]);
      }
      return null;
    }
    return simpleClassName(args[2]);
  }

}
