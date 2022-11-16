/**
 * A collection of functional interfaces that allow a checked exception to be thrown
 * from their functional method.
 *
 * @author Ayco Holleman
 * @see org.klojang.check.ObjectCheck#ok(org.klojang.check.fallible.FallibleFunction)
 * @see org.klojang.check.ObjectCheck#then(org.klojang.check.fallible.FallibleConsumer)
 * @see org.klojang.check.IntCheck#ok(org.klojang.check.fallible.FallibleIntUnaryOperator)
 * @see org.klojang.check.IntCheck#then(org.klojang.check.fallible.FallibleIntConsumer)
 * @see org.klojang.check.IntCheck#mapToObj(org.klojang.check.fallible.FallibleIntFunction)
 */
package org.klojang.check.fallible;