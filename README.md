# Klojang Check

<i>Klojang Check</i> is a Java module dedicated to defensive programming &#8212; ensuring
your program or method starts with a clean and workable set of inputs before
continuing with the business logic. Null checks are the most common example of this. It
often seems, however, like everything beyond that is handled as part of the business
logic, even when it arguably isn't. If a method that calculates a price needs some value
from a configuration file, is the presence of the configuration file part of the business
logic? Probably not, but it needs to be checked nonetheless. <i>Klojang Check</i> allows
you to separate precondition validation and business logic in an elegant and concise way.

<i>Klojang Check</i>'s take on precondition validation is rather different from, for
example, Guava's [Preconditions](https://guava.dev/releases/19.0/api/docs/com/google/common/base/Preconditions.html)
class or Apache's [Validate](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/Validate.html)
class. It provides a set of syntactical constructs that make it easy to specify checks on 
program input, object state, method arguments, variables, etc. In addition, it comes with 
a set of [common checks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
on values of various types. These checks are associated with short, informative error
messages, so you don't have to invent them yourselves.

## Getting Started

To use <i>Klojang Check</i>, add the following dependency to your Maven POM file:

```xml
<dependency>
    <groupId>org.klojang</groupId>
    <artifactId>klojang-check</artifactId>
    <version>3.0.2-jdk21</version>
</dependency>
```

or Gradle build script:

```
implementation group: 'org.klojang', name: 'klojang-check', version: '3.0.2-jdk21'
```

## Example

Here is an example of <i>Klojang Check</i> in action:

```java
public class InteriorDesigner {

  private final int numChairs;

  public InteriorDesigner(int numChairs) {
    this.numChairs = Check.that(numChairs)
          .is(gt(), 0)
          .is(lte(), 4)
          .is(even())
          .ok();
  }

  public void applyColors(List<Color> colors) {
    Check.that(colors).is(notEmpty().and(contains(), noneOf(), RED, BLUE, PINK));
    // apply the colors ...
  }

  public void addCouch(Couch couch) {
    Check.that(couch).isNot(Couch::isExpensive, ExpensiveCouchException::new);
    // add the couch ...
  }

}
```

## Documentation

The **Javadocs** for <i>Klojang Check</i> can be
found **[here](https://klojang4j.github.io/klojang-check/api)**.

The **User Guide** for <i>Klojang Check</i> can be
found **[here](https://klojang4j.github.io/klojang-check/index.html)**.

The latest **test coverage results**
are **[here](https://klojang4j.github.io/klojang-check/coverage)**.

## Vulnerabilities

Being all about making code as robust as possible, the <i>Klojang Check</i> code base is 
itself regularly tested for vulnerabilities. It is currently not affected by any CVE,
however light-weight. Its surface consists of barely 15 types and it has zero dependencies 
outside `java.base`. You can find the latest **vulnerabilities report**
**[here](https://klojang4j.github.io/klojang-check/vulnerabilities/dependency-check-report.html)**.

## Performance

No one is going to use a library just to check things that aren't even related to their
business logic if it hogs their CPU. <i>Klojang Check</i> incurs practically zero
overhead. That's because it doesn't really _do stuff_. As mentioned, it only provides a
set of syntactical constructs that make precondition validation more concise. Of course,
if a value needs to be in a `Map` before it even makes sense to continue with the rest of
a computation, you will have to do the lookup. There's no two ways around it. <i>Klojang
Check</i> just lets you express this fact more clearly:

```java
Check.that(value).is(keyIn(), map);
```

The latest **JMH benchmarks** can be found 
**[here](https://github.com/klojang4j/klojang-check-jmh)**.

## Usage

### Null checks

```java
// throws a NullPointerException if foo is null
Check.notNull(foo);
```

### The CommonChecks class

The [CommonChecks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
class is a grab bag of common checks on arguments, fields (a.k.a. state) and other types of 
program input. 

```java
import static org.klojang.check.CommonChecks.*;

Check.that(length).is(gte(), 0);
Check.that(divisor).isNot(zero());
Check.that(file).is(writable());
Check.that(firstName).is(substringOf(), fullName);
Check.that(i).is(indexOf(), list);
```

### Providing an Argument Name

<i>Klojang Check</i> generates a short, informative error message if the input value fails
a test.

```java
Check.that(length).is(gte(), 0);
// error message: argument must be >= 0 (was -42)
```

You can provide a name for the value you are testing to give the user more context:

```java
Check.that(length, "length").is(gte(), 0);
// error message: length must be >= 0 (was -42)
```

The [Tag](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/Tag.html)
class contains string constants for some commonly used argument names:

```java
import static org.klojang.check.Tag.LENGTH;

Check.that(length, LENGTH).is(gte(), 0);
// error message: length must be >= 0 (was -42)
```

### Testing Argument Properties

With <i>Klojang Check</i> you can test not just arguments, but also argument properties. 
To do this, provide a [Function](https://download.java.net/java/early_access/panama/docs/api/java.base/java/util/function/Function.html)
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
Check.that(foo).has(type(), instanceOf(), InputStream.class);
Check.that(angle).has(abs(), lte(), 90);
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

The message may itself contain message arguments:

```java
Check.that(fullName).has(strlen(), lte(), maxLength, 
      "full name must not exceed ${0} characters (was ${1})",
      maxLength
      fullName.length());
```

There are a few predefined message arguments that you can use in your error message:

```java
Check.that(fullName).has(strlen(), lte(), maxLength, 
      "full name must not exceed ${obj} characters (was ${arg})");
```

`${arg}` is the value you are testing while `${obj}` is the value you are testing it
_against_. The reason the latter message argument is called `${obj}` is because it is the
_object_ of the less-than-or-equal-to relationship, while the argument is used as the
_subject_ of that relationship. For more information, see
[here](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/relation/package-summary.html).

### Throwing a Custom Exception

By default, <i>Klojang Check</i> will throw an `IllegalArgumentException` if the input
value fails any of the checks following `Check.that(...)`. This can be customized in two
ways:
1. by providing a `Function` that takes a string (the error message) and returns the exception to be thrown;
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

### Combining Checks

Sometimes you will want to do tests of the form _**x must be either A or B**_, or of the form
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





