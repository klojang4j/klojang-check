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
import static nl.naturalis.common.x.invoke.InvokeUtils.getPropertyNameFromGetter;

/**
 * Provides and caches {@link Getter getters} for classes.
 *
 * @author Ayco Holleman
 */
public final class GetterFactory {

  /**
   * The one and only instance of {@code GetterFactory}.
   */
  public static final GetterFactory INSTANCE = new GetterFactory();

  private final Map<Class<?>, Map<String, Getter>> cache = new HashMap<>();

  private GetterFactory() {}

  /**
   * Returns the public {@link Getter getters} for the specified class. The returned
   * {@code Map} maps property names to {@code Getter} instances.
   *
   * @param clazz the class for which to retrieve the public getters
   * @param strict if {@code false}, all methods with a zero-length parameter
   *     list and a non-{@code void} return type, except {@code getClass()},
   *     {@code hashCode()} and {@code toString()}, will be regarded as getters.
   *     Otherwise JavaBeans naming conventions will be applied regarding which
   *     methods qualify as getters, with the exception that methods returning a
   *     {@link Boolean} are allowed to have a name starting with "is".
   * @return the public getters of the specified class
   * @throws IllegalAssignmentException if the does not have any public getters
   */
  public Map<String, Getter> getGetters(Class<?> clazz, boolean strict) {
    Map<String, Getter> getters = cache.get(clazz);
    if (getters == null) {
      List<Method> methods = InvokeUtils.getGetters(clazz, strict);
      Check.that(methods).isNot(empty(), () -> new NoPublicGettersException(clazz));
      List<Entry<String, Getter>> entries = new ArrayList<>(methods.size());
      for (Method m : methods) {
        String prop = getPropertyNameFromGetter(m, strict);
        entries.add(entry(prop, new Getter(m, prop)));
      }
      getters = Map.ofEntries(entries.toArray(Entry[]::new));
      cache.put(clazz, getters);
    }
    return getters;
  }

}
