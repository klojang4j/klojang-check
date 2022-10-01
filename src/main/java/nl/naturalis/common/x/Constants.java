package nl.naturalis.common.x;

import nl.naturalis.common.collection.TypeSet;

import java.io.File;
import java.time.temporal.Temporal;
import java.util.Set;

import static nl.naturalis.common.collection.TypeSet.typeGraphSet;

/**
 * Various module-private constants.
 */
public final class Constants {

  /**
   * Default separator for {@code ArrayMethods.implode} and
   * {@code CollectionMethods.implode}.
   */
  public static final String IMPLODE_SEPARATOR = ", ";

  /**
   * Classes whose toString() method return a straightforward and intelligible string
   * representation of the instance.
   */
  public static final Set<Class<?>> DECENT_TO_STRING = typeGraphSet(
      CharSequence.class,
      Number.class,
      Enum.class,
      Temporal.class,
      File.class);

  private Constants() {
    throw new UnsupportedOperationException();
  }

}
