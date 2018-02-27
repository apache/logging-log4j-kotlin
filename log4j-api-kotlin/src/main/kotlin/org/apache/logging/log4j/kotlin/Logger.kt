/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.kotlin

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.spi.ExtendedLogger
import org.apache.logging.log4j.util.MessageSupplier
import org.apache.logging.log4j.util.Supplier
import kotlin.reflect.full.companionObject

/**
 * An adapter supporting cleaner syntax when calling a logger with a Kotlin lambda. A Kotlin lambda can
 * easily be passed to Log4j2 as a `Supplier` via Kotlin's automatic conversion from lambda's to
 * SAM types. However, the compiler selects the incorrect overload of the method unless the lambda
 * type is specified explicitly as `Supplier`, resulting in the lambda itself being logged rather than
 * its evaluation.
 *
 * To avoid this, this delegate provides logging methods that explicitly take a Kotlin Lambda, and
 * then delegate to the underlying Log4j2 method taking a `Supplier`. Just as the Supplier-methods in
 * Log4j2, this does not evaluate the lambda, if the logging level is not enabled.
 *
 * Therefore, one can use Kotlin's String interpolation for logging without the performance impact of
 * evaluating the parameters if the level is not enabled e.g.:
 *
 * ```
 * log.debug { "Value a = $a" }
 * ```
 *
 * In addition, the overloads provide methods in which the lambda is the *last* parameter rather than
 * the first as in the regular Log4j2 API. This means one can use Kotlin's last parameter lambda
 * outside of parentheses syntax e.g.:
 *
 * ```
 * log.error(exc) { "Unexpected exception evaluating $whatever." }
 * ```
 *
 * One known limitation of the Kotlin logging API is that location aware logging does not work
 */
@Suppress("NOTHING_TO_INLINE", "OVERRIDE_BY_INLINE", "UNUSED")
class FunctionalLogger(val log: ExtendedLogger): Logger by log {
  companion object {
    val FQCN: String = FunctionalLogger::class.java.name
    inline fun <T: Any?> (() -> T).asLog4jSupplier(): Supplier<T> = Supplier { invoke() }
  }

  inline fun trace(t: Throwable, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, supplier.asLog4jSupplier(), t)
  }

  inline fun trace(marker: Marker?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun trace(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, supplier.asLog4jSupplier(), t)
  }

  inline fun debug(crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, supplier.asLog4jSupplier(), null)
  }

  inline fun debug(t: Throwable, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, supplier.asLog4jSupplier(), t)
  }

  inline fun debug(marker: Marker?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun debug(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, supplier.asLog4jSupplier(), t)
  }

  inline fun info(crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, supplier.asLog4jSupplier(), null)
  }

  inline fun info(t: Throwable, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, supplier.asLog4jSupplier(), t)
  }

  inline fun info(marker: Marker?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun info(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, supplier.asLog4jSupplier(), t)
  }

  inline fun warn(crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, supplier.asLog4jSupplier(), null)
  }

  inline fun warn(t: Throwable, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, supplier.asLog4jSupplier(), t)
  }

  inline fun warn(marker: Marker?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun warn(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, supplier.asLog4jSupplier(), t)
  }

  inline fun error(crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, supplier.asLog4jSupplier(), null)
  }

  inline fun error(t: Throwable, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, supplier.asLog4jSupplier(), t)
  }

  inline fun error(marker: Marker?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun error(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, supplier.asLog4jSupplier(), t)
  }

  inline fun fatal(crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, supplier.asLog4jSupplier(), null)
  }

  inline fun fatal(t: Throwable, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, supplier.asLog4jSupplier(), t)
  }

  inline fun fatal(marker: Marker?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun fatal(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, supplier.asLog4jSupplier(), t)
  }

  inline fun <R : Any?> trace(block: () -> R): R {
    val entry = traceEntry()
    try {
      val result = block()
      when(result) {
        Unit -> traceExit(entry)
        else -> traceExit(entry, result)
      }
      return result
    } catch (e: Throwable) {
      catching(e)
      throw e
    }
  }

  inline fun <R : Any?> trace(crossinline supplier: () -> Any?, block: () -> R): R {
    val entry = traceEntry(supplier.asLog4jSupplier())
    try {
      val result = block()
      when(result) {
        Unit -> traceExit(entry)
        else -> traceExit(entry, result)
      }
      return result
    } catch (e: Throwable) {
      catching(e)
      throw e
    }
  }

  // define overrides for deprecated MessageSupplier methods, otherwise Kotlin dispatches these over our methods (why?)
  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(Supplier<Message>)"))
  override inline fun trace(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, messageSupplier, null)
  }
  @Deprecated("Use lambda methods.", ReplaceWith("log.debug(Supplier<Message>)"))
  override inline fun debug(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, messageSupplier, null)
  }
  @Deprecated("Use lambda methods.", ReplaceWith("log.info(Supplier<Message>)"))
  override inline fun info(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.INFO, null, messageSupplier, null)
  }
  @Deprecated("Use lambda methods.", ReplaceWith("log.warn(Supplier<Message>)"))
  override inline fun warn(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.WARN, null, messageSupplier, null)
  }
  @Deprecated("Use lambda methods.", ReplaceWith("log.error(Supplier<Message>)"))
  override inline fun error(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, messageSupplier, null)
  }
  @Deprecated("Use lambda methods.", ReplaceWith("log.fatal(Supplier<Message>)"))
  override inline fun fatal(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, messageSupplier, null)
  }
}

/**
 * Logger instantiation by function. Use: `val log = logger()`.
 */
@Suppress("unused")
inline fun <reified T : Any> T.logger() = loggerOf(T::class.java)

fun loggerOf(ofClass: Class<*>): FunctionalLogger {
  return FunctionalLogger(LogManager.getContext(ofClass.classLoader, false).getLogger(unwrapCompanionClass(ofClass).name))
}

// unwrap companion class to enclosing class given a Java Class
fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
  return if (ofClass.enclosingClass?.kotlin?.companionObject?.java == ofClass) {
    ofClass.enclosingClass
  } else {
    ofClass
  }
}
