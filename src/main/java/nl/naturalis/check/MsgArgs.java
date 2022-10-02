package nl.naturalis.check;

/**
 * @param test
 * @param negated
 * @param name
 * @param arg
 * @param type
 * @param obj
 */
record MsgArgs(Object test,
    boolean negated,
    String name,
    Object arg,
    Class<?> type,
    Object obj) {

  @Override
  public Class<?> type() {
    if (type == null) {
      if (arg != null) {
        return arg.getClass();
      }
      return null;
    }
    return type;
  }

  @Override
  public String name() {
    return name == null ? Check.DEF_ARG_NAME : name;
  }

  public String typeAndName() {
    Class<?> c = type();
    if (c == null) {
      return name();
    }
    return Misc.simpleClassName(c) + ' ' + name();
  }

}
