package org.klojang.check;

import static org.klojang.check.MsgUtil.toStr;

import org.klojang.check.PrefabMsgFormatter;

public final class MsgObjIntRelation {

  private MsgObjIntRelation() {
    throw new AssertionError();
  }

  public static PrefabMsgFormatter msgEQ() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not equal " + toStr(x.obj())
        : x.name() + " must equal " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgGT() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be > " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')'
        : x.name() + " must be > " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgGTE() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be >= " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')'
        : x.name() + " must be >= " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgLT() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be < " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')'
        : x.name() + " must be < " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  public static PrefabMsgFormatter msgLTE() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be <= " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')'
        : x.name() + " must be <= " + toStr(x.obj()) + " (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

}
