package nl.naturalis.common;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a public type, method or field must be treated as
 * <i>not</i> part of a module's public API. It is only declared public so it is
 * visible across packages <i>within</i> the module. The module can be a real Java 9+
 * module or, more generally, a jar file. Note that this annotation is itself
 * annotated with the <code>&#64;Documented</code> annotation, so the
 * "non-visibility" of the type, method or field is in a sense part of the public
 * API. Clients cannot rely on it sticking around. It may disappear at any time. Note
 * that in case of a true Java 9+ module the type may be in a non-exported package,
 * which provides hard invisibility anyhow. This annotation then becomes purely
 * documentative (like <code> &#64;VisibleForTesting</code>).
 *
 * @author Ayco Holleman
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD, CONSTRUCTOR, FIELD})
public @interface ModulePrivate {}
