package nl.naturalis.common.invoke;

/**
 * Thrown if a {@link BeanReader} is created for a class with zero public getters, or
 * if they were all excluded while constructing the {@code BeanReader}.
 *
 * @author Ayco Holleman
 */
public final class NoPublicGettersException extends InvokeException {

  NoPublicGettersException(Class<?> beanClass) {
    super(beanClass
        + " does not have any readable properties, or they have all been excluded");
  }

}
