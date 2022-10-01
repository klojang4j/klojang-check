package nl.naturalis.common.invoke;

/**
 * Thrown if a {@link BeanWriter} is created for a class with zero public setters, or
 * if they were all excluded while constructing the {@code BeanWriter}.
 *
 * @author Ayco Holleman
 */
public final class NoPublicSettersException extends InvokeException {

  NoPublicSettersException(Class<?> beanClass) {
    super(beanClass
        + " does not have any writable properties, or they have all been excluded");
  }

}
