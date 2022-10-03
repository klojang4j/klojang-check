package nl.naturalis.check;

record ArrayInfo(Class<?> baseType, int dimensions) {

  static ArrayInfo create(Class<?> arrayClass) {
    var c = arrayClass.getComponentType();
    int i = 1;
    for (; c.isArray(); c = c.getComponentType()) {
      ++i;
    }
    return new ArrayInfo(c, i);
  }

  static String describe(Object array) {
    ArrayInfo info = create(array.getClass());
    int len = Misc.getArrayLength(array);
    StringBuilder sb = new StringBuilder(info.baseType.getSimpleName())
        .append('[')
        .append(len)
        .append(']');
    for (int i = 1; i < info.dimensions; ++i) {
      sb.append("[]");
    }
    return sb.toString();
  }

  String name() {
    return toString(baseType.getName());
  }

  String simpleName() {
    return toString(baseType().getSimpleName());
  }

  @Override
  public String toString() {
    return simpleName();
  }

  private String toString(String name) {
    if (dimensions == 1) { // happy path for 99% of the cases
      return name + "[]";
    }
    StringBuilder sb = new StringBuilder(name.length() + dimensions * 2);
    sb.append(name);
    for (int i = 0; i < dimensions; ++i) {
      sb.append("[]");
    }
    return sb.toString();
  }

}
