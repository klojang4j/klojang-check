package nl.naturalis.common.x;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR})
public @interface VisibleForTesting {}
