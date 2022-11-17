/**
 * <p>
 * Klojang Check is a Java module dedicated to defensive programming. It lets you
 * specify checks on program input, object state and method arguments in a concise
 * and elegant manner. Here is an example:
 *
 * <blockquote><pre>{@code
 * this.numChairs = Check.that(numChairs).is(positive()).is(lte(), 4).is(even()).ok();
 * }</pre></blockquote>
 *
 * <h2>Documentation</h2>
 *
 * <p>See the <b><a href="https://klojang4j.github.io/klojang-check/index.html">User
 * Guide</a></b> for a detailed description of Defensive Programming using Klojang
 * Check.
 *
 * <h2>IntCheck and ObjectCheck</h2>
 * <p>
 * There are two types of check objects: {@link org.klojang.check.IntCheck IntCheck},
 * for validating {@code int} values, and
 * {@link org.klojang.check.ObjectCheck ObjectCheck&lt;T&gt;}, for validating values
 * of type {@code T}. You cannot directly instantiate these classes. You obtain an
 * instance of them through the static factory methods on the
 * {@link org.klojang.check.Check Check} class. In the example above, the
 * {@link org.klojang.check.Check#that(int) that()} static factory method returns an
 * {@code IntCheck} instance.
 *
 *
 *
 *
 *
 *
 * <h2>Common Checks</h2>
 *
 * <p>
 * Klojang Check provides a grab bag of common checks on arguments and other types of
 * values in the form of the {@link org.klojang.check.CommonChecks CommonChecks}
 * class. The {@code lte()}, {@code positive()} and {@code even()} checks shown above
 * are static imports from this class. Here are some more examples:
 *
 * <blockquote> <pre>{@code
 * Check.that(obj, "vehicle").is(instanceOf(), Car.class);
 * Check.that(list).isNot(empty());
 * Check.that(word).is(keyIn(), dictionary);
 * Check.that(dictionary).is(containsKey(), word);
 * Check.that(file).is(writable());
 * }</pre></blockquote>
 * <p>
 * These checks are associated with predefined error messages, so you don't need to
 * invent them yourself. The first of the above statements, for example, would cause
 * the following error message to be generated if the argument was an instance of
 * {@code Bike}:
 *
 * <blockquote> <pre>{@code
 * vehicle must be instance of Car (was Bike)
 * }</pre></blockquote>
 *
 *
 *
 *
 *
 * <h2>Predicates and Relations</h2>
 *
 * <p>The checks you pass to the {@code is(...)} methods fall apart in two broad
 * categories: {@link java.util.function.Predicate Predicate} and
 * {@link java.util.function.IntPredicate IntPredicate} on the one hand, and
 * {@link org.klojang.check.relation.Relation Relation},
 * {@link org.klojang.check.relation.IntRelation IntRelation} and
 * {@link org.klojang.check.relation.IntRelation IntObjRelation} on the other. The
 * latter are not part of the JDK. They reside inside the Klojang Check module
 * itself. They can be thought of as a "BiPredicate" (which neither exists in the
 * JDK): a function that takes <i>two</i> arguments and returns a boolean. If the
 * two arguments have a certain relationship with each other, the relation is said
 * to <i>exist</i> and the function returns {@code true}. Within the context of
 * Klojang Check, the first argument is always the value currently being validated
 * while the second argument is the value that it is to be validated <i>against</i>.
 * In the examples above
 * {@link org.klojang.check.CommonChecks#positive() positive()},
 * {@link org.klojang.check.CommonChecks#even() even()},
 * {@link org.klojang.check.CommonChecks#empty() empty()} and
 * {@link org.klojang.check.CommonChecks#writable() writable()} are predicates;
 * {@link org.klojang.check.CommonChecks#lte() lte()},
 * {@link org.klojang.check.CommonChecks#instanceOf() instanceOf()},
 * {@link org.klojang.check.CommonChecks#keyIn() keyIn()} and
 * {@link org.klojang.check.CommonChecks#containsKey() containsKey()} are relations.
 * For more information, see the package description of
 * {@link org.klojang.check.relation}.
 *
 *
 *
 *
 *
 *
 * <h2>Handling Validation Errors</h2>
 * <p>
 * When a value fails a test, an error message needs to be generated and an exception
 * needs to be thrown. You have three options here:
 * <ul>
 *   <li>Klojang Check generates both the exception and the exception message
 *   <li>Klojang Check generates the exception and you provide the exception message
 *   <li>You do both
 * </ul>
 * The following code snippet provides an example of each of the three variants:
 *
 * <blockquote><pre>{@code
 * Check.that(obj, "vehicle").is(instanceOf(), Car.class);
 * Check.that(obj).is(instanceOf(), Car.class, "Bikes are not for rent here");
 * Check.that(obj).is(instanceOf(), Car.class, () -> new RentalException("Bikes are not for rent here"));
 * }</pre></blockquote>
 *
 *
 *
 *
 *
 *
 * <h2 id="custom-error-messages">Custom Error Messages</h2>
 *
 * <p> If you prefer to emit a custom error message, you can do so by specifying a
 * message pattern and zero or more message arguments. The first message argument can
 * be referenced from within the message pattern as {@code ${0}}, the second as
 * {@code ${1}}, etc. For example:
 *
 * <blockquote><pre>{@code
 * Check.that(word).is(keyIn(), dictionary, "Spelling error. Did you mean: \"${0}\"?", "Pterodactylus");
 * }</pre></blockquote>
 *
 * <p>The following message arguments are automatically available within the message
 * pattern:
 *
 * <ol>
 *   <li><b><code>${test}</code></b> The name of the check that was executed, like
 *      "lt" or "instanceOf" or "notNull".
 *   <li><b><code>${arg}</code></b> The value being validated.
 *   <li><b><code>${type}</code></b> The simple class name of the value.
 *   <li><b><code>${tag}</code></b> The name of the parameter, field or variable
 *      being validated, or, possibly, something more descriptive. If you
 *      did not provide a name, <code>${tag}</code> defaults to "argument".
 *   <li><b><code>${obj}</code></b> The object of the relationship, in case the
 *      check took the form of a
 *      {@link org.klojang.check.relation.Relation Relation} or one of its
 *      sister interfaces. For example, for the
 *      {@link org.klojang.check.CommonChecks#instanceOf() instanceOf()} check,
 *      <code>${obj}</code> would be the class that the argument must be an instance
 *      of (<code>Car.class</code> in the example above). For checks expressed
 *      through a {@link java.util.function.Predicate Predicate} or
 *      {@link java.util.function.IntPredicate IntPredicate}, ${obj} will be
 *      {@code null}.
 * </ol>
 * <p>
 * Here is an example where you don't provide any message arguments yourself, yet
 * still have a dynamically generated error message:
 *
 * <blockquote><pre>{@code
 * Check.that(word).is(keyIn(), dictionary, "Missing key \"${arg}\" in ${obj}");
 * }</pre></blockquote>
 *
 *
 *
 *
 *
 *
 * <h2>Validating Argument Properties</h2>
 *
 * <p>Klojang Check lets you validate argument <i>properties</i> as part of
 * validating the argument.
 *
 * <blockquote><pre>{@code
 * this.query = Check.that(query, "query")
 *  .notHas(Query::offset, "offset", negative())
 *  .has(Query::limit, "limit", gte(), 10)
 *  .has(Query::limit, "limit", lt(), 100)
 *  .ok();
 * }</pre></blockquote>
 *
 * <p>This would cause the following error message to be generated if the user
 * specified a limit of 125:
 *
 * <blockquote><pre>{@code
 * query.limit must be < 100 (was 125)
 * }</pre></blockquote>
 *
 * <p>The <b>{@link org.klojang.check.CommonProperties CommonProperties}</b> class
 * provides some commonly used properties of well-known classes and interfaces, like
 * the {@code size} property of a {@code Collection}. As with the
 * {@code CommonChecks} class, these properties are already associated with a
 * descriptive name of the property they expose. Thus, the error message to be
 * generated requires minimal input from you:
 *
 * <blockquote><pre>{@code
 * Check.notNull(emps, "employees").has(size(), gte(), 100);
 * }</pre></blockquote>
 *
 * <p>This will cause the following error message to be generated if the size of the
 * {@code emps} collection is, say, 42:
 *
 * <blockquote><pre>{@code
 * employees.size() must be >= 100 (was 42)
 * }</pre></blockquote>
 *
 *
 *
 *
 *
 *
 * <h2 id="custom-checks">Custom Checks</h2>
 *
 * <p>You are not limited to using the checks from the {@code CommonChecks} class.
 * You can also define your own checks in the form of lambdas or method references:
 *
 * <blockquote><pre>{@code
 * double angle = 45.0;
 * Check.that(angle).is(a -> Math.sin(a) > 0, "sine of angle must be positive");
 * }</pre></blockquote>
 *
 * <p>Be careful, however, when passing lambdas to the {@code has} and {@code notHas}
 * methods. These methods are heavily overloaded. Therefore, "vanilla" lambdas
 * (without any type information) may cause the compiler to complain about an
 * <b>Ambiguous method call</b>:
 *
 * <blockquote><pre>{@code
 * // WON'T COMPILE! Ambiguous method call
 * Check.that(temperature).has(i -> Math.abs(i), i -> i < 30);
 * }</pre></blockquote>
 *
 * <p>You can disambiguate this for the compiler by specifying the type of the
 * lambda parameter, or by casting the entire lambda or method reference:
 *
 * <blockquote><pre>{@code
 * // specify the type of lambda parameter:
 * Check.that(temperature).has(i -> Math.abs(i), (int i) -> i < 30);
 * // cast the lambda that extracts the property from the argument:
 * Check.that(temperature).has((IntUnaryOperator) i -> Math.abs(i), i -> i < 30);
 * // cast the lambda that tests the property:
 * Check.that(temperature).has(i -> Math.abs(i), (IntPredicate) i -> i < 30);
 * }</pre></blockquote>
 *
 *
 *
 *
 *
 *
 * <h2>Throwing a Different Type of Exception</h2>
 *
 * <p>By default, Klojang Check throws an {@code IllegalArgumentException} if any
 * of the tests following {@code Check.that(...)} fail. To customize this, use the
 * {@code Check.on(...)} static factory methods instead. These allow you to
 * change the default exception. The default exception can itself be overridden
 * within the checks themselves. The following example changes the default exception
 * to {@code SQLException}, but overrides it for the null check:
 *
 * <blockquote><pre>{@code
 * this.query = Check.on(SQLException::new, query, "query")
 *  .is(notNull(), () -> new NullPointerException())
 *  .notHas(Query::offset, "offset", negative())
 *  .has(Query::limit, "limit", gte(), 10)
 *  .has(Query::limit, "limit", lte(), 10000)
 *  .ok();
 * }</pre></blockquote>
 *
 * <p>Here, too, Klojang Check provides some useful shortcuts through the
 * <b>{@link org.klojang.check.CommonExceptions CommonExceptions}</b> class:
 *
 * <blockquote><pre>{@code
 * Check.that(word)
 *  .is(notNull(), npe()) // throw a NullPointerException
 *  .is(keyIn(), dictionary, illegalState("no such word: \"" + word + "\"");
 * }</pre></blockquote>
 *
 * <p><i>NB when you supply your own exception, you cannot use the {@code ${...}}
 * message arguments.</i> You will have to construct the message yourself.
 */
module org.klojang.check {
  exports org.klojang.check;
  exports org.klojang.check.aux;
  exports org.klojang.check.fallible;
  exports org.klojang.check.relation;
}
