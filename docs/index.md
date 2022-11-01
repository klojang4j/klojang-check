# Defensive Programming with Klojang Check

Klojang Check is a Java module dedicated to defensive programming. It provides a set
of syntactical constructs that make it easy to express checks on program input,
object state, method arguments, variables, computational outcomes and program output.
In addition, it comes with some fifty predefined,
[common checks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
on these program elements. These checks are associated with short, informative error
messages, so you don't have to invent them yourselves.

With a surface area of just over 10 types and zero dependencies outside
```java.base```, Klojang Check is definitely light-weight. It is also fast, as it
doesn't do anything that you yourself _wouldn't_ do if you were hand-coding the same
check.

To use Klojang Check, add the following dependency to your POM file:

```xml

<dependency>
    <groupId>org.klojang.check</groupId>
    <artifactId>klojang-check</artifactId>
    <version>3.0.2</version>
</dependency>
```

The **Javadocs** for Klojang Check can be
found [here](https://klojang4j.github.io/klojang-check/api).

Klojang Check's take on validating pre- and postconditions is different from Guava
[Preconditions](https://guava.dev/releases/19.0/api/docs/com/google/common/base/Preconditions.html)
and
Apache [Validate](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/Validate.html).
Here is an example of Klojang Check in action:

```java
public class InteriorDesigner {

  private final int numChairs;

  public InteriorDesigner(int numChairs) {
    this.numChairs = Check.that(numChairs).is(gte(), 0).is(lte(), 4).is(even()).ok();
  }

  public void applyColors(List<Color> colors) {
    Check.that(colors).is(notEmpty().and(contains(), noneOf(), RED, BLUE, PINK));
    // apply the colors
  }

  public void addCouch(Couch couch) {
    Check.notNull(couch).isNot(c -> c.isExpensive(), "couch too expensive");
    // add the couch
  }

}
```

## Performance

You may sense an object lurking somewhere inside those checks and you would not be
wrong. While Guava's ```Preconditions``` class and Apache's ```Validate``` class contain
static utility methods, validation with Klojang Check happens through actual
instances of check objects.

Nevertheless, benchmarking Klojang Check using [JMH](https://github.com/openjdk/jmh)
yields no difference with hand-coded checks. You can view the results of the
benchmarks [here](https://github.com/openjdk/jmh). Looking at the code example above,
that should not be surprising. The object creation is completely localized and, in
fact, redundant. The check object created by ```Check.that()``` (more about which
later) never gets assigned. Thus the JVM clearly has no trouble compiling it away
altogether.

It is also worth noting that, even though Klojang Check is heavily based on the use
of lambdas and method references, that neither is an impediment to a speedy
performance. The time that lambdas were more sluggish than statically invoked code
(if there ever was such a time) has long gone.

**We Don't Check Your Checks ...**

Perhaps paradoxically, while Klojang Check is all about ensuring your method's
preconditions are met before doing anything else, Klojang Check itself does no such
thing. Take, for instance, this statement (the technical details will be explained
below, but is is probably pretty self-explanatory):

```java
Check.that(fullName).is(hasSubstring(), lastName);
```

Since
the [hasSubstring()](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html#hasSubstring())
check simply wraps ```String::contains```, this statement will fly off the rails with
a ```NullPointerException``` if any of the provided arguments (```fullName```,
```hasSubstring()```, ```lastName```) are null. Yet, Klojang Check will not carry out
a null check on any of them. Nevertheless, we think this is justified. In the
(rather) odd case the test to be executed is not known beforehand and is dynamically
inserted into the ```is(...)``` method, then the test becomes itself program input
and, hence, needs to be tested just like any other argument (and we can warmly
recommend a library to do that). The same applies to the ```lastName``` argument. For
the ```fullName``` argument, which is the value we are testing, the justification is
similar, but slightly different. As stated in the description for the
[CommonChecks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
class: the checks only test what they are documented to be testing. More specifically:
the checks will not execute a preliminary null check on the argument before proceeding
with the actual check. If the argument might be null, always start with a null check.
In other words, if ```fullName``` might be null, the check should be done like this:

```java
Check.notNull(fullName).is(hasSubstring(), lastName);
```

Yet, ultimately, the justification is that, if every check provided by the user is
automatically supplemented with three or four more checks to make sure the check is
correctly specified, that would mostly likely be a show stopper for many developers.
We don't want a check executed via Klojang Check to be more expensive than a
hand-coded check.









