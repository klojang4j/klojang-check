# Klojang Check

Klojang Check is a Java module dedicated to defensive programming. It provides a set
of syntactical constructs that make it easy to specify checks on program input,
object state, method arguments, variables, etc. In addition, it comes with a
well-curated assortment
of [common checks](https://klojang4j.github.io/klojang-check/api/org.klojang.check/org/klojang/check/CommonChecks.html)
on values of various types. These checks are associated with short, informative error
messages, so you don't have to invent them yourselves.

## Getting Started

To use Klojang Check, add the following dependency to your Maven POM file:

```xml
<dependency>
    <groupId>org.klojang</groupId>
    <artifactId>klojang-check</artifactId>
    <version>1.0.4</version>
</dependency>
```

or Gradle build script:
```
implementation group: 'org.klojang', name: 'klojang-check', version: '1.0.4'
```

## Example

Here is an example of Klojang Check in action:

```java
public class InteriorDesigner {

  private final int numChairs;

  public InteriorDesigner(int numChairs) {
    this.numChairs = Check.that(numChairs).is(gte(), 0).is(lte(), 4).is(even()).ok();
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

The **Javadocs** for Klojang Check can be
found **[here](https://klojang4j.github.io/klojang-check/api)**.

The **User Guide** for Klojang Check can be
found **[here](https://klojang4j.github.io/klojang-check/index.html)**.

Klojang Check is also fast. If you are interested in the **JMH test results**, they
can be found **[here](https://github.com/klojang4j/klojang-check-jmh)**.

The latest **test coverage reports**
are **[here](https://klojang4j.github.io/klojang-check/jacoco)**.

## Vulnerabilities

With a surface area of barely 15 types and zero dependencies outside
`java.base`, Klojang Check is genuinely light-weight. Yet, being all about making
your code as robust as possible, the Klojang Check code base is itself regularly
tested for vulnerabilities. Every build gets pulled through the
[OWASP vulnerability scanner](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/)
for Maven. It is currently not affected by any CVE.

## About
<img src="docs/logo-groen.png" style="float:left;width:5%;padding:0 12px 12px 0"/>

Klojang Check is developed by [Naturalis](https://www.naturalis.nl/en), 
the Dutch national biodiversity research institute and natural history museum,
maintaining one of the largest collections of zoological and botanical specimens 
in the world.





