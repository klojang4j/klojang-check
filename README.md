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

### Tagging the Tested Value

<i>Klojang Check</i> generates a short, informative error message if the input value fails
a test.

```java
Check.notNull(foo);
// error message: argument must not be null

Check.that(length).is(gte(), 0);
// error message: argument must be >= 0 (was -42)
```

You can provide a "tag" for the value you are testing to give the user more context:

```java
Check.notNull(foo, "foo");
// error message: foo must not be null

Check.that(length, "length").is(gte(), 0);
// error message: length must be >= 0 (was -42)
```

This is especially useful when checking multiple method arguments within a method as the
error message makes it immediately clear which argument violated some constraint. The tag
could be the name of the method parameter, but it can really be anything you like. The 
[Tag](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/Tag.html)
class contains string constants for some commonly used argument names:

```java
import static org.klojang.check.Tag.LENGTH;

Check.that(length, LENGTH).is(gte(), 0);
// error message: length must be >= 0 (was -42)
```

### Testing Argument Properties

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

Check.that(fullName).has(strlen(),lte(),100);
Check.that(foo).has(type(),instanceOf(),InputStream .class);
Check.that(angle).has(abs(),lte(),90);
```

As the last example illustrates, the word "property" needs to be taken in the broadest
sense here. These are really just functions that are passed the argument and return the
value to be tested.

### Providing a Custom Error Message

If you prefer, you can provide your own error message:

```java
Check.that(foo).is(notNull(), "there you go again");
Check.that(fullName).has(strlen(), lte(), 100, "full name must not exceed 100 characters");
```

The message may contain message arguments:

```java
Check.that(length).is(lte(),maxLen, "length must be <= ${0} (was ${1})", maxLen, length);
```

There are a five predefined message arguments that you can use in your error message:

- **${arg}** &#8212; The value being tested.
- **${obj}** &#8212; The value against which the input value is tested, if applicable. For
  example, in `Check.that(length).is(lt(), 100)`, the value of `length` is tested against
  the value of `maxLen`.
- **${type}** &#8212; The type of the value being tested.
- **${tag}** &#8212; The name you gave to the value, or "argument" if you did not provide
  a name.
- **${test}** &#8212; The name of the test. For example: "notNull" or "lte". If you
  executed a custom check (a lambda or method reference), this will be a pretty
  unintelligible string.

Thus, the previous check could also written as follows:

```java
Check.that(length).is(lte(), maxLen, "length must be <= ${obj} (was ${arg})");
```

Note that the above error message contains message arguments, but you do not provide them
yourself.

### Throwing a Custom Exception

By default, <i>Klojang Check</i> will throw an `IllegalArgumentException` if the input
value fails any of the checks following `Check.that(...)`. This can be customized in two
ways:

1. by providing a `Function` that takes a string (the error message) and returns the
   exception to be thrown;
2. by providing a `Supplier` that supplies the exception to be thrown.

Here is an example of each of these:

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

### Composite Checks

Sometimes you will want to do tests of the form _**x must be either A or B**_, or of the
form
_**either x must be A or y must be B**_:

```java
Check.that(collection).is(empty().or(contains(), "FOO"));
Check.that(collection1).is(empty().or(collection2, contains(), "FOO"));
```

The latter example nicely maintains the <i>Klojang Check</i> idiom, but if you prefer
your code with less syntactical sugar, you can also just write:

```java
Check.that(collection1).is(empty().or(collection2.contains("FOO"));
```

When combining checks you can also employ
[logical quantifiers](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/relation/Quantifier.html):

```java
import static org.klojang.check.relation.Quantifier.noneOf;
import static org.klojang.check.CommonChecks.notEmpty;
import static org.klojang.check.CommonChecks.contains;

Check.that(collection).is(notEmpty().and(contains(), noneOf(), "FOO", "BAR"));
```

## About

<img src="docs/logo-groen.png" style="float:left;width:5%;padding:0 12px 12px 0" />

<i>Klojang Check</i> is developed by [Naturalis](https://www.naturalis.nl/en), the
Dutch national biodiversity research institute. It maintains one of the largest
collections of zoological and botanical specimens in the world. Help fund biodiversity
research by
[donating to Naturalis](https://www.naturalis.nl/over-ons/steun-naturalis/doneren).





