package org.klojang.check.x.msg;

import org.klojang.check.x.RangeExclusive;
import org.klojang.check.x.RangeInclusive;

import static org.klojang.check.x.Misc.*;
import static org.klojang.check.x.msg.MsgUtil.WAS;

import java.util.List;

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
  }

  static PrefabMsgFormatter msgInRange() {
    return x -> {
      int[] ints = (int[]) x.obj();
      int min = ints[0];
      int max = ints[1];
      //@formatter:off
      return x.negated()
          ? x.name() + " must be < " + min + " or >= " + max + WAS + (x.arg()) +')'
          : x.name() + " must be >= " + min + " and < " + max + WAS + (x.arg()) +')';
      //@formatter:on
    };
  }

  static PrefabMsgFormatter msgBetween() {
    return x -> {
      if (x.obj() instanceof RangeExclusive re) {
        //@formatter:off
        return x.negated()
            ? x.name() + " must be < " + re.lower() + " or >= " + re.upper() + WAS + (x.arg()) +')'
            : x.name() + " must be >= " + re.lower() + " and < " + re.upper() + WAS + (x.arg()) +')';
        //@formatter:on
      } else if (x.obj() instanceof RangeInclusive ri) {
        //@formatter:off
        return x.negated()
            ? x.name() + " must be < " + ri.lower() + " or > " + ri.upper() + WAS + (x.arg()) +')'
            : x.name() + " must be >= " + ri.lower() + " and <= " + ri.upper() + WAS + (x.arg()) +')';
        //@formatter:on
      }
      // will never get here
      return null;
    };
  }

}
