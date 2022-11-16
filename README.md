# Klojang Check

Klojang Check is a Java module dedicated to defensive programming. It provides a set
of syntactical constructs that make it easy to specify checks on program input,
object state, method arguments, variables, etc. In addition, it comes with a
well-curated assortment
of [common checks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
on values of various types. These checks are associated with short, informative error
messages, so you don't have to invent them yourselves. In short, Klojang Check
significantly lowers the bar for acquiring a habit of validating preconditions and
postconditions.

With a surface area of barely 15 types and no dependencies outside
```java.base```, Klojang Check is light-weight. It is also fast, as it doesn't do
anything that you yourself _wouldn't_ do if you were hand-coding the same check.
Being all about defensive programming, the Klojang Check code base itself is
regularly tested for vulnerabilities. Every release build gets pulled through
the [OWASP vulnerability scanner](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/)
for Maven.

Here is an example of what validating pre- and postconditions using Klojang Check
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

The **Javadocs** for Klojang Check can be
found [here](https://klojang4j.github.io/klojang-check/api).

To use Klojang Check, include the following in your Maven POM file:

```xml

<dependency>
    <groupId>org.klojang.check</groupId>
    <artifactId>klojang-check</artifactId>
    <version>3.0.2</version>
</dependency>
```



