package nl.naturalis.common;

import nl.naturalis.check.Check;
import nl.naturalis.common.invoke.BeanWriter;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static nl.naturalis.common.ClassMethods.*;
import static nl.naturalis.check.CommonChecks.notNull;

/**
 * Performs a wide variety of type conversions. The conversions try to strike a
 * balance between being lenient without becoming outlandish or contrived. This class
 * is optionally used by the {@link BeanWriter} class to perform type conversions on
 * the values passed to its {@link BeanWriter#write(Object, String, Object) set}
 * method.
 * <style>
 * .td-morph { padding:2px 10px 2px 3px; border:1px solid #888; text-align:left;
 * font-family: arial; font-size:110%;}
 * </style>
 * <table>
 *   <thead>
 *     <tr>
 *       <th class='td-morph'>Target Type</th>
 *       <th class='td-morph'>Input Type</th>
 *       <th class='td-morph'>Return Value</th>
 *       <th class='td-morph'>Remarks</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *    <tr>
 *      <td class='td-morph'>primitive</td>
 *      <td class='td-morph'>null</td>
 *      <td class='td-morph'>0, 0L, false, '\0', etc.</td>
 *      <td class='td-morph'>primitive default of the target type</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>T</td>
 *      <td class='td-morph'>null</td>
 *      <td class='td-morph'>null</td>
 *      <td class='td-morph'>&nbsp;</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>T</td>
 *      <td class='td-morph'>extends T</td>
 *      <td class='td-morph'>input</td>
 *      <td class='td-morph'>input value returned as-is</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>primitive (e.g. float)</td>
 *      <td class='td-morph'>wrapper (e.g. Float)</td>
 *      <td class='td-morph'>(float) input</td>
 *      <td class='td-morph'>simple cast of the input value</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>String</td>
 *      <td class='td-morph'>byte[&nbsp;]</td>
 *      <td class='td-morph'>new String(input, UTF_8)</td>
 *      <td class='td-morph'>&nbsp;</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>String</td>
 *      <td class='td-morph'>char[&nbsp;]</td>
 *      <td class='td-morph'>new String(input)</td>
 *      <td class='td-morph'>&nbsp;</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>String</td>
 *      <td class='td-morph'>other</td>
 *      <td class='td-morph'>input.toString()</td>
 *      <td class='td-morph'>&nbsp;</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>T[&nbsp;]</td>
 *      <td class='td-morph'>U[&nbsp;]</td>
 *      <td class='td-morph'>T[&nbsp;]</td>
 *      <td class='td-morph'>morph U elements to T (recursive call)</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>T[&nbsp;]</td>
 *      <td class='td-morph'>Collection&gt;U&gt;</td>
 *      <td class='td-morph'>T[&nbsp;]</td>
 *      <td class='td-morph'>morph U elements to T (recursive call)</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>T[&nbsp;]</td>
 *      <td class='td-morph'>IntList</td>
 *      <td class='td-morph'>T[&nbsp;]</td>
 *      <td class='td-morph'>morph int elements to T (recursive call)</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>byte[&nbsp;]</td>
 *      <td class='td-morph'>String</td>
 *      <td class='td-morph'>input.getBytes(UTF_8)</td>
 *      <td class='td-morph'>&nbsp;</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>char[&nbsp;]</td>
 *      <td class='td-morph'>String</td>
 *      <td class='td-morph'>input.toCharArray()</td>
 *      <td class='td-morph'>&nbsp;</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>T[&nbsp;]</td>
 *      <td class='td-morph'>U</td>
 *      <td class='td-morph'>new T[] { convert(u, T.class) }</td>
 *      <td class='td-morph'>single-element array (recursive call)</td>
 *    </tr>
 *    <tr>
 *      <td class='td-morph'>Collection&lt;T&gt;></td>
 *      <td class='td-morph'>U</td>
 *      <td class='td-morph'>new T[] { convert(u, T.class) }</td>
 *      <td class='td-morph'>single-element array (recursive call)</td>
 *    </tr>
 *   </tbody>
 * </table>
 *
 * @param <T> The type to which incoming values will be converted
 * @author Ayco Holleman
 * @see NumberMethods#convert(Number, Class)
 * @see NumberMethods#parse(String, Class)
 * @see Bool
 * @see nl.naturalis.common.util.EnumParser
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Morph<T> {

  /**
   * Converts the specified object to the specified type.
   *
   * @param <U> The target type
   * @param obj The value to convert
   * @param targetType The {@code Class} object corresponding to the target type
   * @return The converted value
   * @throws TypeConversionException If the conversion did not succeed
   */
  public static <U> U convert(Object obj, Class<U> targetType) {
    return new Morph<>(targetType).convert(obj);
  }

  private final Class<T> targetType;

  /**
   * Creates a new {@code Morph} instance that will convert values to the specified
   * type.
   *
   * @param targetType The type to which to convert values
   */
  public Morph(Class<T> targetType) {
    this.targetType = Check.notNull(targetType).ok();
  }

  /**
   * Converts the specified object into an instance of the type specified through the
   * constructor.
   *
   * @param obj The value to convert
   * @return An instance of the target type
   * @throws TypeConversionException If the conversion did not succeed
   */
  public T convert(Object obj) throws TypeConversionException {
    Class<T> toType = this.targetType;
    if (obj == null) {
      return getTypeDefault(toType);
    } else if (toType.isInstance(obj)) {
      return (T) obj;
    } else if (isAutoUnboxedAs(obj.getClass(), toType)) {
      return (T) obj;
    } else if (toType == String.class) {
      if (obj instanceof byte[] bytes) {
        return (T) new String(bytes, StandardCharsets.UTF_8);
      } else if (obj instanceof char[] chars) {
        return (T) new String(chars);
      }
      return (T) obj.toString();
    } else if (toType.isArray()) {
      return MorphToArray.morph(obj, toType);
    } else if (isSubtype(toType, Collection.class)) {
      return MorphToCollection.morph(obj, toType);
    }
    Class myType = obj.getClass();
    if (myType.isArray()) {
      return Array.getLength(obj) == 0
          ? getTypeDefault(toType)
          : convert(Array.get(obj, 0), toType);
    } else if (isSubtype(myType, Collection.class)) {
      Collection coll = (Collection) obj;
      return coll.isEmpty()
          ? getTypeDefault(toType)
          : convert(coll.iterator().next(), toType);
    }
    Object out = MorphToNumber.morph(obj, toType);
    if (out != null) {
      return (T) out;
    } else if (toType.isEnum()) {
      return (T) MorphToEnum.morph(obj, toType);
    }
    throw new TypeConversionException(obj, toType);
  }

  static String stringify(Object obj) {
    return Check.that(obj.toString())
        .is(notNull(), "obj.toString() must not return null")
        .ok();
  }

}
