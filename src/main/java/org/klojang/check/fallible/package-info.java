/**
 * A collection of functional interfaces that allow a checked exception to be thrown
 * from their functional method. That makes their use in multi-threaded programs
 * somewhat problematic, but for single-threaded programs they can be very
 * useful.
 *
 * @author Ayco Holleman
 */
package org.klojang.check.fallible;