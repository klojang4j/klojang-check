package org.klojang.check.x.msg;

import static org.klojang.check.x.msg.MsgUtil.*;

@SuppressWarnings("rawtypes")
public final class MsgRelation {

  private MsgRelation() {}

  public static PrefabMsgFormatter msgSameAs() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be " + identify(x.obj())
        : x.name() + " must be " + identify(x.obj()) + WAS + identify(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgNullOr() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be null or " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be null or " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgInstanceOf() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be instance of " + className(x.obj()) + WAS + x.arg() + ')'
        : x.name() + " must be instance of " + className(x.obj()) + WAS + className(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgSubtypeOf() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be subtype of " + className(x.obj()) + WAS + className(x.arg()) + ')'
        : x.name() + " must be subtype of " + className(x.obj()) + WAS + className(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgSupertypeOf() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be supertype of " + className(x.obj()) + WAS + className(x.arg()) + ')'
        : x.name() + " must be supertype of " + className(x.obj()) + WAS + className(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgContains() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not contain " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must contain " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgContainsKey() {
    return x -> x.negated()
        ? x.name() + " must not contain key " + toStr(x.obj())
        : x.name() + " must contain key " + toStr(x.obj());
  }

  public static PrefabMsgFormatter msgContainsValue() {
    return x -> x.negated()
        ? x.name() + " must not contain value " + toStr(x.obj())
        : x.name() + " must contain value " + toStr(x.obj());
  }

  public static PrefabMsgFormatter msgIn() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be element of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be element of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgKeyIn() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be key in " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be key in " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgValueIn() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be value in " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be value in " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgEnclosing() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be superset of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be superset of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgEnclosedBy() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be subset of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be subset of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgHasSubstring() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not contain " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must contain " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgSubstringOf() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be substring of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be substring of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgEqualsIC() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be equal, ignoring case, to " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be equal, ignoring case, to " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }


  public static PrefabMsgFormatter msgStartsWithIC() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not start with, ignoring case, to " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must start with, ignoring case, to " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }


  public static PrefabMsgFormatter msgEndsWithIC() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not end with, ignoring case, to " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must end with, ignoring case, to " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgHasSubstringIC() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not contain, ignoring case, " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must contain, ignoring case, " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }



  public static PrefabMsgFormatter msgStartsWith() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not start with " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must start with " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgEndsWith() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not end with " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must end with " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgHasPattern() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not match " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must match " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }


  public static PrefabMsgFormatter msgContainsPattern() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not contain pattern " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must contain pattern " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgNumerical() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be parsable into " + simpleClassName(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " cannot be parsed into " + simpleClassName(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  public static PrefabMsgFormatter msgParsableAs() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be parsable into " + simpleClassName(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " cannot be parsed into " + simpleClassName(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

}
