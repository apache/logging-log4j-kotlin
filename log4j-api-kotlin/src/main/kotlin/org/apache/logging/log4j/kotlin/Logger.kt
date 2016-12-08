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

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.spi.ExtendedLogger
import org.apache.logging.log4j.util.Supplier
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.companionObject

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
class FunctionalLogger(val log: ExtendedLogger): Logger by log {
  companion object {
    @Suppress("NOTHING_TO_INLINE")
    inline fun <T: Any?> (() -> T).asLog4jSupplier(): Supplier<T> = Supplier { invoke() }
  }

  inline fun trace(t: Throwable, crossinline supplier: () -> Any?) {
    log.trace(supplier.asLog4jSupplier(), t)
  }

  inline fun trace(marker: Marker?, crossinline supplier: () -> Any?) {
    log.trace(marker, supplier.asLog4jSupplier())
  }

  inline fun trace(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.trace(marker, supplier.asLog4jSupplier(), t)
  }

  inline fun debug(crossinline supplier: () -> Any?) {
    log.debug(supplier.asLog4jSupplier())
  }

  inline fun debug(t: Throwable, crossinline supplier: () -> Any?) {
    log.debug(supplier.asLog4jSupplier(), t)
  }

  inline fun debug(marker: Marker?, crossinline supplier: () -> Any?) {
    log.debug(marker, supplier.asLog4jSupplier())
  }

  inline fun debug(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.debug(marker, supplier.asLog4jSupplier(), t)
  }

  inline fun info(crossinline supplier: () -> Any?) {
    log.info(supplier.asLog4jSupplier())
  }

  inline fun info(t: Throwable, crossinline supplier: () -> Any?) {
    log.info(supplier.asLog4jSupplier(), t)
  }

  inline fun info(marker: Marker?, crossinline supplier: () -> Any?) {
    log.info(marker, supplier.asLog4jSupplier())
  }

  inline fun info(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.info(marker, supplier.asLog4jSupplier(), t)
  }

  inline fun warn(crossinline supplier: () -> Any?) {
    log.warn(supplier.asLog4jSupplier())
  }

  inline fun warn(t: Throwable, crossinline supplier: () -> Any?) {
    log.warn(supplier.asLog4jSupplier(), t)
  }

  inline fun warn(marker: Marker?, crossinline supplier: () -> Any?) {
    log.warn(marker, supplier.asLog4jSupplier())
  }

  inline fun warn(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.warn(marker, supplier.asLog4jSupplier(), t)
  }

  inline fun error(crossinline supplier: () -> Any?) {
    log.error(supplier.asLog4jSupplier())
  }

  inline fun error(t: Throwable, crossinline supplier: () -> Any?) {
    log.error(supplier.asLog4jSupplier(), t)
  }

  inline fun error(marker: Marker?, crossinline supplier: () -> Any?) {
    log.error(marker, supplier.asLog4jSupplier())
  }

  inline fun error(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.error(marker, supplier.asLog4jSupplier(), t)
  }

  inline fun fatal(crossinline supplier: () -> Any?) {
    log.fatal(supplier.asLog4jSupplier())
  }

  inline fun fatal(t: Throwable, crossinline supplier: () -> Any?) {
    log.fatal(supplier.asLog4jSupplier(), t)
  }

  inline fun fatal(marker: Marker?, crossinline supplier: () -> Any?) {
    log.fatal(marker, supplier.asLog4jSupplier())
  }

  inline fun fatal(marker: Marker?, t: Throwable?, crossinline supplier: () -> Any?) {
    log.fatal(marker, supplier.asLog4jSupplier(), t)
  }

  inline fun <R : Any?> trace(block: () -> R): R {
    val entry = traceEntry()
    try {
      val result = block()
      when(result) {
        is Unit -> traceExit(entry)
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
        is Unit -> traceExit(entry)
        else -> traceExit(entry, result)
      }
      return result
    } catch (e: Throwable) {
      catching(e)
      throw e
    }
  }
}

/**
 * Logger instantiation. Use: `val log = logger()`.
 */
@Suppress("unused")
inline fun <reified T : Any> T.logger(): FunctionalLogger =
  FunctionalLogger(LogManager.getContext(T::class.java.classLoader, false).getLogger(unwrapCompanionClass(T::class.java).name))

// unwrap companion class to enclosing class given a Java Class
fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
  return if (ofClass.enclosingClass?.kotlin?.companionObject?.java == ofClass) {
    ofClass.enclosingClass
  } else {
    ofClass
  }
}
