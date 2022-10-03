package nl.naturalis.check;

import nl.naturalis.check.PrefabMsgFormatter;

final class MsgIntPredicate {

  private MsgIntPredicate() {
    throw new AssertionError();
  }

  static PrefabMsgFormatter msgEven() {
    return x -> x.negated()
        ? x.name() + " must not be even (was " + x.arg() + ')'
        : x.name() + " must be even (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgOdd() {
    return x -> x.negated()
        ? x.name() + " must not be odd (was " + x.arg() + ')'
        : x.name() + " must be odd (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgPositive() {
    return x -> x.negated()
        ? x.name() + " must not be positive (was " + x.arg() + ')'
        : x.name() + " must be positive (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgNegative() {
    return x -> x.negated()
        ? x.name() + " must not be negative (was " + x.arg() + ')'
        : x.name() + " must be negative (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgZero() {
    return x -> x.negated()
        ? x.name() + " must not be 0"
        : x.name() + " must be 0 (was " + x.arg() + ')';
  }

}
