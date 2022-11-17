# Klojang Check

Klojang Check is a Java module dedicated to defensive programming. It provides a set
of syntactical constructs that make it easy to specify checks on program input,
object state, method arguments, variables, etc. In addition, it comes with a
well-curated assortment
of [common checks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
on values of various types. These checks are associated with short, informative error
messages, so you don't have to invent them yourselves.

With a surface area of barely 15 types and zero dependencies outside
```java.base```, Klojang Check is genuinely light-weight. Yet, being all about secure
programming, the Klojang Check code base is itself regularly tested for
vulnerabilities. Every build gets pulled through
the [OWASP vulnerability scanner](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/)
for Maven.

## Getting Started

To use Klojang Check, add the following dependency to your Maven POM file:

```xml
<dependency>
    <groupId>org.klojang</groupId>
    <artifactId>klojang-check</artifactId>
    <version>1.0.3</version>
</dependency>
```

## Documentation

The **Javadocs** for Klojang Check can be
found **[here](https://klojang4j.github.io/klojang-check/api)**.

The **User Guide** for Klojang Check can be
found **[here](https://klojang4j.github.io/klojang-check/index.html)**.

Klojang Check is also fast. If you are interested in the **JMH test results**, they
can be found **[here](https://github.com/klojang4j/klojang-check-jmh)**.

The latest **test coverage reports**
are **[here](https://klojang4j.github.io/klojang-check/jacoco)**.

## Example

Here is an example of Klojang Check in action:

```java
import org.klojang.check.Check;

import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.relation.Quantifier.noneOf;

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

  public void addCouch(Couch couch) {
    Check.that(couch).isNot(Couch::isExpensive, ExpensiveCouchException::new);
    // add the couch
  }

}
```



