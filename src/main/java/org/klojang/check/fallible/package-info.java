/**
 * A collection of functional interfaces that allow a checked exception to be thrown
 * from their functional method. That makes their use in multithreaded programs
 * somewhat problematic, but for single-threaded programs they can be very useful.
 * The
 * {@link
 * org.klojang.check.ObjectCheck#ok(org.klojang.check.fallible.FallibleFunction)
 * ok()} and
 * {@link
 * org.klojang.check.ObjectCheck#then(org.klojang.check.fallible.FallibleConsumer)
 * then()} methods of {@link org.klojang.check.ObjectCheck ObjectCheck} and
 * {@link org.klojang.check.IntCheck IntCheck} use these interfaces, as do may
 * methods in other klojang modules.
 *
 * @author Ayco Holleman
 */
package org.klojang.check.fallible;