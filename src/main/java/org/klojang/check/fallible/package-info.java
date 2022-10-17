/**
 * A collection of functional interfaces that allow a checked exception to be thrown
 * from their functional method. That makes their use in a multi-threaded program
 * somewhat problematic. Nevertheless, for single-threaded programs they can be very
 * useful, and even code running inside a thread of a multi-threaded program
 * ultimately has no option but to throw an exception if the required preconditions
 * are not in order.
 *
 * @author Ayco Holleman
 * @see org.klojang.check.ObjectCheck#ok(org.klojang.check.fallible.FallibleFunction)
 * @see org.klojang.check.ObjectCheck#then(org.klojang.check.fallible.FallibleConsumer)
 * @see org.klojang.check.IntCheck#ok(org.klojang.check.fallible.FallibleIntFunction)
 * @see org.klojang.check.IntCheck#then(org.klojang.check.fallible.FallibleIntConsumer)
 */
package org.klojang.check.fallible;