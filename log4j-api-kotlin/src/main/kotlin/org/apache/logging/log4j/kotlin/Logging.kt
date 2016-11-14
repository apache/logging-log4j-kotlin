package org.apache.logging.log4j.kotlin

import org.apache.logging.log4j.Logger

/**
 * An interface-based "mixin" to easily add a log val to a class, named by the enclosing class. This allows
 * code like this:
 *
 * ```
 * import org.apache.logging.log4j.kotlin.Logging
 *
 * class MyClass: Logging {
 *   override val log: Logger = logger()
 * }
 *
 * ```
 *
 * A simpler mechanism is to use the delegated property extension directly, like:
 *
 * ```
 * import org.apache.logging.log4j.kotlin.logger
 *
 * class MyClass {
 *   val log by logger()
 * }
 *
 * ```
 */
interface Logging {
  val log: Logger

  fun logger(): Logger = this.javaClass.logger()
}
