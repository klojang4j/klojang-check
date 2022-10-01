package nl.naturalis.common;

import static nl.naturalis.common.ArrayMethods.refIndexOf;
import static nl.naturalis.common.ClassMethods.box;
import static nl.naturalis.common.Morph.stringify;
import static nl.naturalis.common.ObjectMethods.bruteCast;

/*
 * Used to morph objects into primitives and primitive wrapper types. Also used to
 * convert to {@code BigDecimal} and {@code BigInteger}. It's pointless to try and
 * use generics here. It will fight you. Too much dynamic stuff going on.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
final class MorphToNumber {

  private MorphToNumber() {
    throw new UnsupportedOperationException();
  }

  static Object morph(Object obj, Class toType) {
    Class type = box(toType);
    if (type == Boolean.class) {
      return Bool.from(obj);
    } else if (type == Character.class) {
      return toChar(obj, toType);
    } else if (ClassMethods.isSubtype(type, Number.class)) {
      return toNumber(obj, type);
    }
    return null;
  }

  private static Number toNumber(Object obj, Class toType) {
    Class myType = obj.getClass();
    if (ClassMethods.isSubtype(myType, Number.class)) {
      return NumberMethods.convert((Number) obj, toType);
    } else if (myType.isEnum()) {
      return refIndexOf(myType.getEnumConstants(), obj);
    } else if (myType == Character.class) {
      return charToNumber(obj, box(toType));
    }
    return NumberMethods.parse(stringify(obj), toType);
  }

  private static Character toChar(Object obj, Class toType) {
    if (obj.getClass() == Boolean.class) {
      return (Boolean) obj ? '1' : '0';
    }
    String s = stringify(obj);
    if (s.length() == 1) {
      return s.charAt(0);
    }
    throw new TypeConversionException(obj,
        toType,
        "String length exceeds 1: %s",
        obj);
  }

  private static Number charToNumber(Object obj, Class targetType) {
    char c = (Character) obj;
    if (c >= '0' && c <= '9') {
      return NumberMethods.convert(c - 48, targetType);
    }
    throw new TypeConversionException(obj, targetType);
  }

}
