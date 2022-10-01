package nl.naturalis.common.invoke;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.util.Map;

import static nl.naturalis.check.CommonChecks.notNull;
import static nl.naturalis.common.invoke.NoSuchPropertyException.noSuchProperty;

/**
 * A dynamic bean reader class for arbitrary types of beans. This class uses the
 * {@code java.lang.invoke} package instead of reflection to read bean properties.
 * Yet it still uses reflection to identify the getter methods of the bean class.
 * Therefore, if you use this class from within a Java module you must open the
 * module to the naturalis-common module.
 *
 * @author Ayco Holleman
 */
public final class AnyBeanReader {

  private final boolean strict;

  /**
   * Creates a new {@code AnyBeanReader}. JavaBeans naming conventions will be
   * applied regarding which methods qualify as getters.
   */
  public AnyBeanReader() {
    this(true);
  }

  /**
   * Creates a new {@code AnyBeanReader}.
   *
   * @param strictNaming if {@code false}, all methods with a zero-length
   *     parameter list and a non-{@code void} return type, except
   *     {@code getClass()}, {@code hashCode()} and {@code toString()}, will be
   *     regarded as getters. Otherwise JavaBeans naming conventions will be applied
   *     regarding which methods qualify as getters. By way of exception, methods
   *     returning a {@link Boolean} are allowed to have a name starting with "is"
   *     (just like methods returning a {@code boolean}). The {@code strictNaming}
   *     parameter is quietly ignored for {@code record} classes. Records are always
   *     processed as though {@code strictNaming} were {@code false}.
   */
  public AnyBeanReader(boolean strictNaming) {
    this.strict = strictNaming;
  }

  /**
   * Returns the value of the specified property on the specified bean. If the
   * property does not exist a {@link NoSuchPropertyException} is thrown.
   *
   * @param bean the bean instance
   * @param property the property
   * @param <U> the return type
   * @return its value
   * @throws NoSuchPropertyException if the specified property does not exist
   */
  public <U> U read(Object bean, String property) throws NoSuchPropertyException {
    Check.notNull(bean, Param.BEAN);
    Check.notNull(property, Param.PROPERTY);
    return doRead(bean, property);
  }

  @SuppressWarnings("unchecked")
  private <U> U doRead(Object bean, String prop) {
    Map<String, Getter> getters = GetterFactory.INSTANCE
        .getGetters(bean.getClass(), strict);
    Getter getter = getters.get(prop);
    Check.that(getter).is(notNull(), () -> noSuchProperty(bean, prop)).ok();
    try {
      return (U) getter.read(bean);
    } catch (Throwable exc) {
      throw InvokeException.wrap(exc, bean, getter);
    }
  }

}
