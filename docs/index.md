# Defensive Programming with Klojang Check

Klojang Check is a Java module dedicated to defensive programming. It provides a set
of syntactical constructs that make it easy to express checks on program input,
object state, method arguments, variables, computational outcomes and program output.
In addition, it comes with some fifty predefined,
[common checks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
on these program elements. These checks are associated with short, informative error
messages, so you don't have to invent them yourselves.

With a effective surface area of just over 10 types and zero dependencies outside
```java.base```, Klojang Check is light-weight. It is also fast, as it doesn't do
anything that you yourself _wouldn't_ do if you were hand-coding the same check.

Klojang Check's take on validating pre- and postconditions is different from Guava
[Preconditions](https://guava.dev/releases/19.0/api/docs/com/google/common/base/Preconditions.html)
and
Apache [Validate](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/Validate.html)
. Here is an example of what validating pre- and postconditions using Klojang Check
looks like:

```java
public class InteriorDesigner {

  private final int numChairs;

  public InteriorDesigner(int numChairs) {
    this.numChairs = Check.that(numChairs).is(gte(), 0).is(lte(), 4).is(even()).ok();
  }

  public void applyColors(List<Color> colors) {
    Check.that(colors).is(notEmpty()
        .and(contains(), noneOf(), PINK, YELLOW, PURPLE));
    // apply the colors
  }
}
```

## Performance

Ouch, you might now squirm &#8211; do I sense an object lurking somewhere inside
those checks? And you would not be wrong. Guava's ```Preconditions``` class and
Apache's ```Validate``` class are static utility classes, but with Klojang Check,
validation happens through actual instances of check objects.

Nevertheless, benchmarking Klojang Check using [JMH](https://github.com/openjdk/jmh)
yields _no difference_ with hand-coded checks. You can view the results of the 
benchmarks [here](https://github.com/openjdk/jmh). Looking at the code example 
above, that shouldn't be entirely surprising. The object creation is so localized 
and redundant that the JVM clearly how now trouble compiling it away altogether.

It is also worth noting that, even though Klojang Check is heavily based on the 
use of lambdas and method references, that neither is an impediment to a speedy 
performance. The time that lambdas were more sluggish than statically invoked 
code (if there ever was such a time) has long gone.

### We Don't Check Your Checks ...
Perhaps paradoxically, while Klojang Check is all about ensuring your method's 
preconditions are met before doing anything else, Klojang Check itself does no 
such thing. Take, for instance, this check (the technical detail will be explained 
below, but is is probably pretty self-explanatory):

```java
Check.that(fullName).is(hasSubstring(), lastName);
```


Strutting your code with unit tests has long since become established practice. Build
tools, IDEs and frameworks like JUnit and Mockito all intimately understand that part
of your code base. The same cannot be said of verifying the sanity of what enters and
comes out of your code once it actually run. Probably no one will deny its
importance, but it is not the fully ritualized practice that unit testing has become.

Why this should be so ... Performance consideration may play a role here, as may
aesthetics: exhaustive validation of preconditions and postconditions could
substantially increase the size of your otherwise mean and lean method. Not to
mention that each ```if``` check you introduce, will have to be supplemented with a
unit test in order to keep your test coverage acceptable.

Klojang Check aims to take away these concerns. It provides a set of syntactical
constructs that makes it easy to define and express checks on program input, object
state, method arguments, variables, computational outcomes and program output. In
addition, it comes with some fifty predefined,
[common checks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
on these program elements. These checks are associated with short, informative error
messages, so you don't have to invent them yourselves. In short, Klojang Check
significantly lowers the bar for acquiring a habit of validating preconditions and
postconditions.

