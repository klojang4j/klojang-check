package nl.naturalis.check;

import static nl.naturalis.check.MsgUtil.toStr;

final class MsgObjIntRelation {

  private MsgObjIntRelation() {
    throw new AssertionError();
  }

  static PrefabMsgFormatter msgEQ() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not equal " + toStr(x.obj())
        : x.name() + " must equal " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgGT() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be > " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')'
        : x.name() + " must be > " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgGTE() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be >= " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')'
        : x.name() + " must be >= " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgLT() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be < " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')'
        : x.name() + " must be < " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgLTE() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be <= " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')'
        : x.name() + " must be <= " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

}
