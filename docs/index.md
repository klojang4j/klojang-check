# Defensive Programming with Klojang Check



Klojang Check is a light-weight Java module dedicated to defensive programming.

## Performance

### We don't check your checks ...

Perhaps surprisingly, while Klojang Check is motivated by the conviction that 
validating pre- and postcondition should at as ingrained a habit as unit testing 
your code, Klojang Check's own code does no such thing. For example, a call like

```java
Check.that(smallString).is(substringOf(), bigString);
```

does not verify that the
provided test (```substringOf()``` in this case) is not null. Thus, in the 
odd case that the test is somehow dynamically inserted into the call and it 
_does_ turn out to be null, a hard and raw NullPointerException will follow.