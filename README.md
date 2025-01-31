# Klojang Check

<i>Klojang Check</i> is a tiny framework for validating program input, object state,
method arguments, variables &#8212; anything that needs to have the right value before
you can safely execute the next line of code. It enables you to separate precondition 
validation and business logic in an elegant and concise way.

<i>Klojang Check</i> works rather differently than, for example,
Guava's [Preconditions](https://guava.dev/releases/19.0/api/docs/com/google/common/base/Preconditions.html)
class or
Apache's [Validate](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/Validate.html)
class, which server a similar purpose. It provides a set of syntactical constructs that 
make it easy to specify your checks. In addition, it comes with a large set of predefined
checks on values of various types. These checks are associated with short, informative 
error messages, so you don't have to invent them yourselves.

## Getting Started

To use <i>Klojang Check</i>, add the following dependency to your Maven POM file:

```xml

<dependency>
    <groupId>org.klojang</groupId>
    <artifactId>klojang-check</artifactId>
    <version>21.2.0</version>
</dependency>
```

or Gradle build script:

```
implementation group: 'org.klojang', name: 'klojang-check', version: '21.2.0'
```

## Documentation

The **Javadocs** for <i>Klojang Check</i> can be
found **[here](https://klojang4j.github.io/klojang-check/api)**.

The latest **test coverage results**
are **[here](https://klojang4j.github.io/klojang-check/coverage)**.

## Performance

<i>Klojang Check</i> incurs practically zero
overhead. That is because, by itself, it doesn't really _do stuff_. As mentioned, it only
provides a set of syntactical constructs that make precondition validation more concise.
Of course, if you want to ascertain that a is in a `Map` before using it, you will have to
do the lookup. There is no two ways around it. <i>Klojang Check</i> just lets you express
this fact more clearly:

```java
Check.that(value).is(keyIn(), map);
```

The latest **JMH benchmarks** can be found
**[here](https://github.com/klojang4j/klojang-check-jmh)**.

## Usage

### Null checks

```java
// throws a NullPointerException if foo is null:
Check.notNull(foo);
```

### The CommonChecks class

The [CommonChecks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
class is a grab bag of common checks on arguments, fields (a.k.a. state) and other types
of program input.

```java
import static org.klojang.check.CommonChecks.*;

Check.that(length).is(gte(), 0);
Check.that(divisor).isNot(zero());
Check.that(file).is(writable());
Check.that(firstName).is(substringOf(), fullName);
Check.that(i).is(indexOf(), list);
Check.that(employee.isManager()).is(yes());
```

### Custom Checks

Of course, you can also provide your own checks:

```java
Check.that(length).is(i -> i >= 0);
Check.that(file).is(File::canWrite);
Check.that(employee).is(Employee::isManager);
```

### Chaining Checks

Checks on the same value can be chained:

```java
Check.that(numberOfChairs).is(positive()).is(lte(), 4).is(even());
```

Checks on different values can be also be chained:

```java
Check.that(numberOfChairs).is(positive()).and(numberOfTables).is(one());
```

Generally, though, we do not recommend the latter practice. Just write:

```java
Check.that(numberOfChairs).is(positive());
Check.that(numberOfTables).is(one());
```

This makes it easier to see which values have been validated.

### Tagging the Tested Value

<i>Klojang Check</i> generates a short, informative error message if the input value fails
a test.

```java
Check.notNull(foo);
// error message: argument must not be null

Check.that(length).is(gte(), 0);
// error message: argument must be >= 0 (was -42)
```

You can provide a "tag" for the value you are testing in order to give the user more 
context:

```java
Check.notNull(foo, "foo");
// error message: foo must not be null

Check.that(length, "length").is(gte(), 0);
// error message: length must be >= 0 (was -42)
```

This is especially useful when checking multiple method arguments as the
error message will make it immediately clear which argument failed to pass a test. The tag
could be the name of the method parameter, but it can really be anything you like. The
[Tag](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/Tag.html)
class contains string constants for some commonly used argument names:

```java
import static org.klojang.check.Tag.LENGTH;

Check.that(length, LENGTH).is(gte(), 0);
// error message: length must be >= 0 (was -42)
```

### Validating Argument Properties

With <i>Klojang Check</i> you can test not just arguments, but also argument properties.
To do this, provide
a [Function](https://download.java.net/java/early_access/panama/docs/api/java.base/java/util/function/Function.html)
that extracts the value to be tested from the argument.

```java
Check.that(fullName).has(String::length, lte(), 100);
```

The [CommonProperties](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonProperties.html)
class contains some `Function`, `ToIntFunction` and `IntFunction` constants that might be
of help:

```java
import static org.klojang.check.CommonProperties.strlen;
import static org.klojang.check.CommonProperties.type;
import static org.klojang.check.CommonProperties.abs;

Check.that(fullName).has(strlen(), lte(), 100);
Check.that(foo).has(type(), instanceOf(), InputStream .class);
Check.that(angle).has(abs(), lte(), 90);
```

As the last example illustrates, the word "property" needs to be taken in the broadest
sense here. These are really just functions that are passed the argument and return the
value to be tested.

### Providing a Custom Error Message

If you prefer, you can provide your own error message:

```java
Check.that(foo).is(notNull(), "null is not a valid value for foo");
Check.that(length).is(lte(), 100, "length must not exceed 100 characters");
```

The message may contain message arguments:

```java
Check.that(length).is(lte(), maxLen, "length must be <= ${0} (was ${1})", maxLen, length);
```

There are a five predefined message arguments that you can use in your error message:

- ${arg} &#8212; The value being tested.
- ${obj} &#8212; The value against which the input value is tested, if applicable. For
  example, in `Check.that(length).is(lte(), maxLen)`, the value of `length` is tested against
  the value of `maxLen`.
- ${type} &#8212; The type of the value being tested.
- ${tag} &#8212; The tag you gave to the value (defaults to "argument" if you did not provide a tag).
- ${test} &#8212; The name of the test. For example: "notNull" or "lte". If you
  executed a custom check (a lambda or method reference), this will be a pretty
  unintelligible string.

Thus, the previous check can also be written as follows:

```java
Check.that(length).is(lte(), maxLen, "length must be <= ${obj} (was ${arg})");
```

Note that the above error message contains message arguments, but you don't need to
provide them yourself.

### Throwing a Custom Exception

By default, <i>Klojang Check</i> will throw an `IllegalArgumentException` if the input
value fails any of the checks following `Check.that(...)`. This can be customized in two
ways:

1. By using the `Check.on(...)` methods instead of the `Check.that(...)` methods. These
   methods allow you to provide a `Function` that takes a string (the error message) and
   returns the exception to be thrown.
2. By providing a `Supplier` that supplies the exception to be thrown.

Here is an example of each:

```java
// Error message "stale connection" is passed to the constructor of IllegalStateException:
Check.on(IllegalStateException::new, connection.isOpen()).is(yes(), "stale connection");
Check.that(connection.isOpen()).is(yes(), () -> new IllegalStateException("stale connection"));
```

The [CommonExceptions](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonExceptions.html)
class contains exception factories for some common exceptions:

```java
import static org.klojang.check.CommonExceptions.STATE;
import static org.klojang.check.CommonExceptions.illegalState;

Check.on(STATE, connection.isOpen()).is(yes(), "stale connection");
Check.that(connection.isOpen()).is(yes(), illegalState("stale connection"));
```

### Returning the Validated Value

Once a value has passed all checks, you can assign it to a field or variable using
[ObjectCheck::ok()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/ObjectCheck.html#ok())
or [IntCheck::ok()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/IntCheck.html#ok()):

```java
this.person = Check.notNull(person).has(Person::age, gte(), 18).ok();
```

You can optionally pass a transformation function to the `ok()` method:

```java
this.age = Check.notNull(person).ok(Person::age);
```

The transformation function is allowed to throw a checked exception, so even at this late
stage you may decide the value is invalid after all. This is because what gets passed to
the `ok()` method is not a
[Function](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Function.html)
or [ToIntFunction](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/ToIntFunction.html)
but a [FallibleFunction](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/fallible/FallibleFunction.html)
or [FallibleToIntFunction](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/fallible/FallibleToIntFunction.html),
respectively.

### Composite Checks

Sometimes, proper validation can only be done through a combination of checks.
Schematically, this is what you would want to accomplish:

- _**x must be either A or B**_
- or: _**either x must be A or y must be B**_:

In the first case you want to provide two alternative checks for the same value. In the
second case you want to validate two interrelated values.

<i>Klojang Check</i> enables you to do this using the `default` methods on the
[ComposablePredicate](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/relation/ComposablePredicate.html)
and [ComposableIntPredicate](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/relation/ComposableIntPredicate.html)
interfaces. Here we use the `or(...)` method:

```java
Check.that(collection).is(empty().or(contains(), "FOO"));
Check.that(collection1).is(empty().or(collection2, contains(), "FOO"));
// or, if you prefer a little less syntactic sugar:
Check.that(collection1).is(empty().or(collection2.contains("FOO"));
```

What if you want the first check to be a
[Relation](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/types/Relation.html),
like [gte()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html#gte())
or [instanceOf()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html#instanceOf()),
or you want the first check to be a self-written lambda or a method reference?
In that case you can start your composition with one of four special checks:
[valid()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html#valid()),
[validInt()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html#validInt()),
[invalid()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html#invalid()),
and [invalidInt()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html#invalidInt()).
These really are dummy checks that you should not use as the only check on a value. The 
first two checks always pass and can be used as the start of a series of AND-joined 
checks. The last two always fail and can be used as the start of a series of OR-joined 
checks.

```java
import static org.klojang.check.CommonChecks.valid;
import static org.klojang.check.CommonChecks.invalid;

Check.that(collection).is(valid().and(contains(), "FOO").and(contains(), "BAR"));
Check.that(collection).is(invalid().or(contains(), "FOO").or(contains(), "BAR"));
```

### Logical Quantifiers

When combining checks you can also employ
[logical quantifiers](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/types/Quantifier.html).
Using logical qualifiers you can provide multiple values against which to validate the
input value. They allow you to specify that the input value must pass a check for 
**_all of_** the provided values, **_at least one of_** the provided values, or 
**_none of_** the provided values. Here is an example:

```java
import static org.klojang.check.types.Quantifier.noneOf;

Check.that(collection).is(valid().and(contains(), noneOf(), "FOO", "BAR", "BOZO"));
```

What if there is just one check you want to execute, but you want to use a logical 
quantifier? Again you can use the dummy checks mentioned above:

```java
import static org.klojang.check.types.Quantifier.allOf;

Check.that(collection).is(valid().and(contains(), allOf(), "FOO", "BAR", "BOZO"));
```

## About

<img src="docs/logo-groen.png" style="float:left;width:5%;padding:0 12px 12px 0" />

<i>Klojang Check</i> is developed by [Naturalis](https://www.naturalis.nl/en), the
Dutch national biodiversity research institute. It maintains one of the largest
collections of zoological and botanical specimens in the world. Help fund biodiversity
research by
[donating to Naturalis](https://www.naturalis.nl/over-ons/steun-naturalis/doneren).





