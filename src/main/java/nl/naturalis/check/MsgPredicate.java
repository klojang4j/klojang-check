package nl.naturalis.check;

import nl.naturalis.check.ArrayInfo;
import nl.naturalis.check.PrefabMsgFormatter;

import static nl.naturalis.check.MsgUtil.className;
import static nl.naturalis.check.MsgUtil.toStr;

final class MsgPredicate {

  private static final String NULL = "null";
  private static final String TRUE = "true";
  private static final String FALSE = "false";

  private MsgPredicate() {}

  static PrefabMsgFormatter msgNull() {
    return x -> x.negated()
        ? x.name() + " must not be null"
        : x.name() + " must be null (was " + toStr(x.arg()) + ')';
  }

  static PrefabMsgFormatter msgNotNull() {
    return x -> x.negated()
        ? x.name() + " must be null (was " + toStr(x.arg()) + ')'
        : x.name() + " must not be null";
  }

  static PrefabMsgFormatter msgYes() {
    return x -> x.negated()
        ? x.name() + " must not be true"
        : x.name() + " must be true";
  }

  static PrefabMsgFormatter msgNo() {
    return x -> x.negated()
        ? x.name() + " must not be false"
        : x.name() + " must be false";
  }

  static PrefabMsgFormatter msgEmpty() {
    return x -> x.negated()
        ? x.name() + " must not be null or empty (was " + toStr(x.arg()) + ')'
        : x.name() + " must be null or empty (was " + toStr(x.arg()) + ')';
  }

  static PrefabMsgFormatter msgNotEmpty() {
    return x -> x.negated()
        ? x.name() + " must be null or empty (was " + toStr(x.arg()) + ')'
        : x.name() + " must not be null or empty (was " + toStr(x.arg()) + ')';
  }

  static PrefabMsgFormatter msgDeepNotNull() {
    //@formatter:off
    return x ->  x.negated()
            ? x.name() + " must be null or contain null values (was " + toStr(x.arg()) + ')'
            : x.name() + " must not be null or contain null values (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgDeepNotEmpty() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must be empty or contain empty values (was " + toStr(x.arg()) + ')'
        : x.name() + " must not be empty or contain empty values (was " + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgBlank() {
    return x -> x.negated()
        ? x.name() + " must not be null or blank (was " + toStr(x.arg()) + ')'
        : x.name() + " must be null or blank (was " + toStr(x.arg()) + ')';
  }

  static PrefabMsgFormatter msgInt32() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be parsable into an int (was " + toStr(x.arg()) + ')'
        : x.name() + " cannot be parsed into an int (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgInt64() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be parsable into a long (was " + toStr(x.arg()) + ')'
        : x.name() + " cannot be parsed into a long (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgInt16() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be parsable into a short (was " + toStr(x.arg()) + ')'
        : x.name() + " cannot be parsed into a short (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgInt8() {
    //@formatter:off
    return x ->
        x.negated()
            ? x.name() + " must not be parsable into a byte (was " + toStr(x.arg()) + ')'
            : x.name() + " cannot be parsed into a byte (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgFloat32() {
    //@formatter:off
    return x ->
        x.negated()
            ? x.name() + " must not be parsable into a float (was " + toStr(x.arg()) + ')'
            : x.name() + " cannot be parsed into a float (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgFloat64() {
    //@formatter:off
    return x ->
        x.negated()
            ? x.name() + " must not be parsable into a double (was " + toStr(x.arg()) + ')'
            : x.name() + " cannot be parsed into a double (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgArray() {
    //@formatter:off
    return x -> x.arg() instanceof Class<?> c
        ? x.negated()
            ? x.name() + " must not be an array type (was " + ArrayInfo.create(c) + ')'
            : x.name() + " must be an array type (was " + className(c) + ')'
        : x.negated()
            ? x.name() + " must not be an array (was " + ArrayInfo.describe(x.arg()) + ')'
            : x.name() + " must be an array (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgFile() {
    return x -> x.negated()
        ? x.name() + " must not exist or not be a normal file (was " + x.arg() + ')'
        : x.name() + " must be an existing, normal file (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgDirectory() {
    return x -> x.negated()
        ? x.name() + " must not exist or not be a directory (was " + x.arg() + ')'
        : x.name() + " must be an existing directory (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgFileExists() {
    return x -> x.negated()
        ? x.name() + " must not exist (was " + x.arg() + ')'
        : x.name() + " must exist (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgReadable() {
    return x ->
        x.negated()
            ? x.name() + " must not be readable (was " + x.arg() + ')'
            : x.name() + " must be readable (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgWritable() {
    return x ->
        x.negated()
            ? x.name() + " must not be writable (was " + x.arg() + ')'
            : x.name() + " must be writable (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgPresent() {
    //@formatter:off
    return x ->
        x.negated()
            ? "Optional " + x.name() + " must be empty (was " + toStr(x.arg()) + ')'
            : "Optional " + x.name() + " must not be empty";
    //@formatter:off
  }

  static PrefabMsgFormatter msgAvailable() {
    return x ->
        x.negated()
            ? "Result " + x.name() + " must not be available (was " + toStr(x.arg()) + ')'
            : "No result available for " + x.name();
  }

}
