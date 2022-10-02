package nl.naturalis.check;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.regex.Pattern;

import static nl.naturalis.base.ArrayType.describe;
import static nl.naturalis.check.Check.DEF_ARG_NAME;
import static nl.naturalis.check.CommonChecks.NAMES;
import static nl.naturalis.check.MsgUtil.MAX_STRING_WIDTH;
import static nl.naturalis.check.MsgUtil.simpleClassName;
import static nl.naturalis.check.Misc.toShortString;

/*
 * Formats client-provided messages.
 */
final class CustomMsgFormatter {

  private CustomMsgFormatter() {}

  private static final String ARG_START = "${";
  private static final char ARG_END = '}';

  /*
   * It turns out we consistently get +/- 50% better performance if we hand-code the
   * string replacements. Since clients will probably have low tolerance for argument
   * checks costing them anything but a few CPU cycles, let's go for it.
   * Nevertheless, keep the regex around.
   */
  private static final String REGEX = "\\$\\{(test|arg|type|name|obj|\\d+)}";

  @SuppressWarnings("unused")
  private static final Pattern PATTERN = Pattern.compile(REGEX);

  // Will be called if the use provided at least one message argument, so
  // implying the message pattern contains ${0}, ${1} etc. and possibly also
  // predefined message arguments like ${arg}, ${name}, ...
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

  static String formatWithPrefabArgsOnly(String msg, Object[] msgArgs) {
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

  // Only positional args: ${0}, ${1}, etc. Used by Check.fail()
  static String formatSimple(String msg, Object[] msgArgs) {
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
          "test", CustomMsgFormatter::getCheckName,
          "arg",  args -> toShortString(args[1],MAX_STRING_WIDTH),
          "type", args -> getArgumentType(args),
          "name", CustomMsgFormatter::getArgumentName,
          "obj",  args -> toShortString(args[4],MAX_STRING_WIDTH));
  //@formatter:on

  private static String getArgVal(String arg, Object[] args) {
    Function<Object[], String> fnc = ARG_LOOKUPS.get(arg);
    if (fnc != null) {
      return fnc.apply(args);
    }
    OptionalInt x = getInt(arg);
    if (x.isPresent()) {
      int i = 5 + x.getAsInt();
      if (i < args.length) {
        return Objects.toString(args[i]);
      }
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
    OptionalInt x = getInt(arg);
    if (x.isPresent()) {
      int i = x.getAsInt();
      if (i >= 0 && i < args.length) {
        return Objects.toString(args[i]);
      }
    }
    return ARG_START + arg + ARG_END;
  }

  private static OptionalInt getInt(String arg) {
    try {
      return OptionalInt.of(new BigInteger(arg).intValueExact());
    } catch (NumberFormatException e) {
      return OptionalInt.empty();
    }
  }

  private static String getCheckName(Object[] args) {
    String name;
    if ((name = NAMES.get(args[0])) != null) {
      return name;
    }
    return args[0].getClass().getSimpleName();
  }

  private static String getArgumentName(Object[] args) {
    if (args[3] == null) {
      return DEF_ARG_NAME;
    }
    return args[3].toString();
  }

  private static String getArgumentType(Object[] args) {
    if (args[2] == null) {
      if (args[1] != null) {
        return describe(args[1]);
      }
      return null;
    }
    return simpleClassName((Class<?>) args[2]);
  }

}
