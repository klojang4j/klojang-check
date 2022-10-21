package org.klojang.check;

import org.klojang.check.x.msg.PrefabMsgFormatter;

final class MsgIntRelation {

  private MsgIntRelation() {
    throw new AssertionError();
  }

  static PrefabMsgFormatter msgEq() {
    return x -> x.negated()
        ? x.name() + " must not equal " + x.obj()
        : x.name() + " must equal " + x.obj() + " (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgNe() {
    return x -> x.negated()
        ? x.name() + " must equal " + x.obj() + " (was " + x.arg() + ')'
        : x.name() + " must not equal " + x.obj();
  }

  static PrefabMsgFormatter msgGt() {
    return x -> x.negated()
        ? x.name() + " must not be > " + x.obj() + " (was " + x.arg() + ')'
        : x.name() + " must be > " + x.obj() + " (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgGte() {
    return x -> x.negated()
        ? x.name() + " must not be >= " + x.obj() + " (was " + x.arg() + ')'
        : x.name() + " must be >= " + x.obj() + " (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgLt() {
    return x -> x.negated()
        ? x.name() + " must not be < " + x.obj() + " (was " + x.arg() + ')'
        : x.name() + " must be < " + x.obj() + " (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgLte() {
    return x -> x.negated()
        ? x.name() + " must not be <= " + x.obj() + " (was " + x.arg() + ')'
        : x.name() + " must be <= " + x.obj() + " (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgMultipleOf() {
    return x -> x.negated()
        ? x.name() + " must not be multiple of " + x.obj() + " (was " + x.arg() + ')'
        : x.name() + " must be multiple of " + x.obj() + " (was " + x.arg() + ')';
  }

}
