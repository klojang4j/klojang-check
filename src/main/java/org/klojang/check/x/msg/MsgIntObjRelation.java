package org.klojang.check.x.msg;

import java.util.List;

import static org.klojang.check.x.Misc.getArrayLength;
import static org.klojang.check.x.msg.MsgUtil.WAS;

final class MsgIntObjRelation {

  private MsgIntObjRelation() {
    throw new UnsupportedOperationException();
  }

  static PrefabMsgFormatter msgIndexOf() {
    return x -> {
      int max;
      if (x.obj() instanceof String s) {
        max = s.length();
      } else if (x.obj() instanceof List<?> l) {
        max = l.size();
      } else {
        max = getArrayLength(x.obj());
      }
      return x.negated()
          ? x.name() + " must be < 0 or >= " + max + WAS + x.arg() + ')'
          : x.name() + " must be >= 0 and < " + max + WAS + x.arg() + ')';
    };
  }

  static PrefabMsgFormatter msgIndexInclusiveInto() {
    return x -> {
      int max;
      if (x.obj() instanceof String s) {
        max = s.length();
      } else if (x.obj() instanceof List<?> l) {
        max = l.size();
      } else {
        max = getArrayLength(x.obj());
      }
      return x.negated()
          ? x.name() + " must be < 0 or > " + max + WAS + x.arg() + ')'
          : x.name() + " must be >= 0 and <= " + max + WAS + x.arg() + ')';
    };
    //
  }

}
