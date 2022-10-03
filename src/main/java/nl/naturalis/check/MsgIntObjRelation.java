package nl.naturalis.check;

import nl.naturalis.check.PrefabMsgFormatter;
import nl.naturalis.check.MsgUtil;

import java.util.List;

import static nl.naturalis.check.Misc.*;

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
          ? x.name() + " must be < 0 or >= " + max + MsgUtil.WAS + x.arg() + ')'
          : x.name() + " must be >= 0 and < " + max + MsgUtil.WAS + x.arg() + ')';
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
          ? x.name() + " must be < 0 or > " + max + MsgUtil.WAS + x.arg() + ')'
          : x.name() + " must be >= 0 and <= " + max + MsgUtil.WAS + x.arg() + ')';
    };
  }

  static PrefabMsgFormatter msgInRangeOf() {
    return x -> {
      int[] ints = (int[]) x.obj();
      int min = ints[0];
      int max = ints[1];
      //@formatter:off
      return x.negated()
          ? x.name() + " must be < " + min + " or >= " + max + MsgUtil.WAS + (x.arg()) +')'
          : x.name() + " must be >= " + min + " and < " + max + MsgUtil.WAS + (x.arg()) +')';
      //@formatter:on
    };
  }

  static PrefabMsgFormatter msgBetween() {
    return x -> {
      int[] ints = (int[]) x.obj();
      int min = ints[0];
      int max = ints[1];
      //@formatter:off
      return x.negated()
          ? x.name() + " must be < " + min + " or > " + max + MsgUtil.WAS + (x.arg()) +')'
          : x.name() + " must be >= " + min + " and <= " + max + MsgUtil.WAS + (x.arg()) +')';
      //@formatter:on
    };
  }

}
