package nl.naturalis.check;

import nl.naturalis.check.PrefabMsgFormatter;

import static nl.naturalis.check.MsgUtil.*;

@SuppressWarnings("rawtypes")
final class MsgRelation {

  private MsgRelation() {}

  static PrefabMsgFormatter msgSameAs() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be " + identify(x.obj())
        : x.name() + " must be " + identify(x.obj()) + WAS + identify(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgNullOr() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be null or " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be null or " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgInstanceOf() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be instance of " + className(x.obj()) + WAS + x.arg() + ')'
        : x.name() + " must be instance of " + className(x.obj()) + WAS + className(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgSubtypeOf() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be subtype of " + className(x.obj()) + WAS + className(x.arg()) + ')'
        : x.name() + " must be subtype of " + className(x.obj()) + WAS + className(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgSupertypeOf() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be supertype of " + className(x.obj()) + WAS + className(x.arg()) + ')'
        : x.name() + " must be supertype of " + className(x.obj()) + WAS + className(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgContaining() {
    return x -> x.negated()
        ? x.name() + " must not contain " + toStr(x.obj())
        : x.name() + " must contain " + toStr(x.obj());
  }

  static PrefabMsgFormatter msgContainingKey() {
    return x -> x.negated()
        ? x.name() + " must not contain key " + toStr(x.obj())
        : x.name() + " must contain key " + toStr(x.obj());
  }

  static PrefabMsgFormatter msgContainingValue() {
    return x -> x.negated()
        ? x.name() + " must not contain value " + toStr(x.obj())
        : x.name() + " must contain value " + toStr(x.obj());
  }

  static PrefabMsgFormatter msgIn() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be element of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be element of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgKeyIn() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be key in " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be key in " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgValueIn() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be value in " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be value in " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgEnclosing() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be superset of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be superset of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgEnclosedBy() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be subset of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be subset of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgContainingString() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not contain " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must contain " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgSubstringOf() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be substring of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be substring of " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgEqualsIgnoreCase() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be equal (ignoring case) to " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must be equal (ignoring case) to " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgStartsWith() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not start with " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must start with " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgEndsWith() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not end with " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')'
        : x.name() + " must end with " + toStr(x.obj()) + WAS + toStr(x.arg()) + ')';
    //@formatter:off
  }

}
