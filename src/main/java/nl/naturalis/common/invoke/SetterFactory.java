package nl.naturalis.common.invoke;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.invoke.InvokeUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry;
import static java.util.Map.entry;
import static nl.naturalis.check.CommonChecks.empty;

/**
 * Provides and caches {@link Setter setters} for classes.
 *
 * @author Ayco Holleman
 */
public final class SetterFactory {

  /**
   * The one and only instance of {@code SetterFactory}.
   */
  public static final SetterFactory INSTANCE = new SetterFactory();

  private final Map<Class<?>, Map<String, Setter>> cache = new HashMap<>();

  private SetterFactory() {}

  /**
   * Returns the public {@link Setter setters} for the specified class. The returned
   * {@code Map} maps property names to {@code Setter} instances.
   *
   * @param clazz The class for which to retrieve the public setters
   * @return The public setters of the specified class
   * @throws IllegalAssignmentException If the does not have any public setters
   */
  public Map<String, Setter> getSetters(Class<?> clazz) {
    Map<String, Setter> setters = cache.get(clazz);
    if (setters == null) {
      List<Method> methods = InvokeUtils.getSetters(clazz);
      Check.that(methods).isNot(empty(), () -> new NoPublicSettersException(clazz));
      List<Entry<String, Setter>> entries = new ArrayList<>(methods.size());
      for (Method m : methods) {
        String prop = InvokeUtils.getPropertyNameFromSetter(m);
        entries.add(entry(prop, new Setter(m, prop)));
      }
      setters = Map.ofEntries(entries.toArray(Entry[]::new));
      cache.put(clazz, setters);
    }
    return setters;
  }

}
