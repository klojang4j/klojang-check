# Defensive Programming with Klojang Check



Klojang Check is a light-weight Java module dedicated to defensive programming.

```xml
<dependency>
    <groupId>org.klojang.check</groupId>
    <artifactId>klojang-check</artifactId>
    <version>3.0.2</version>
</dependency>
```

## Performance

### We don't check your checks ...

Perhaps surprisingly, while Klojang Check is motivated by the conviction that 
validating pre- and postcondition should at as ingrained a habit as unit testing 
your code, Klojang Check's own code does no such thing. For example, a call like
```java
Check.that(smallString).is(substringOf(), bigString);
```
does not verify that the provided values and expressions are non-null