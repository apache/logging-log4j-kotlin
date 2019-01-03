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
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.message.EntryMessage
import org.apache.logging.log4j.message.Message
import org.apache.logging.log4j.message.SimpleMessage
import org.apache.logging.log4j.spi.ExtendedLogger

/**
 * An adapter supporting cleaner syntax when calling a logger via Kotlin. This does not implement
 * the Log4j2 [Logger] interface, but instead limits logging methods to those that would be natural
 * to use from Kotlin. For example, the various logging-parameter methods necessary for Java are
 * eschewed in favor of Kotlin lambdas and String interpolation.
 *
 * If you do need access to the underlying [Logger] or [ExtendedLogger], it may be accessed via the
 * `delegate` property.
 *
 * One can use Kotlin's String interpolation for logging without the performance impact of
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
 * The adapter also provides a `runInTrace` utility that avoids having to call traceEnter and traceExit
 * and catch manually. Rather, simply call the `trace` method, passing in an [EntryMessage] and the block to
 * execute within trace enter/exit/catch calls. Location-awareness is currently broken for trace logging with this
 * method as the ExtendedLogger does not expose the enter/exit/catch calls with the FQCN parameter.
 *
 * We also use Kotlin's nullability features to specify unambiguously which parameters must be non-null
 * when passed.
 *
 * Lastly, the ExtendedLogger delegate is available if the underlying Log4j Logger is needed for some reason.
 * Access it via the `delegate` property.
 *
 * TODO: The ExtendedLogger delegate does not yet have support for trace entry and exit with FQCN specification.
 * Therefore, until the Log4j2 API is updated and then this code is updated to match, location awareness will not
 * work for these calls.
 */
@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
class KotlinLogger(val delegate: ExtendedLogger) {
  companion object {
    val FQCN: String = KotlinLogger::class.java.name
  }

  fun log(level: Level, marker: Marker, msg: Message) {
    delegate.logIfEnabled(FQCN, level, marker, msg, null)
  }

  fun log(level: Level, marker: Marker, msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, level, marker, msg, t)
  }

  fun log(level: Level, marker: Marker, msg: CharSequence) {
    delegate.logIfEnabled(FQCN, level, marker, msg, null)
  }

  fun log(level: Level, marker: Marker, msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, level, marker, msg, t)
  }

  fun log(level: Level, marker: Marker, msg: Any) {
    delegate.logIfEnabled(FQCN, level, marker, msg, null)
  }

  fun log(level: Level, marker: Marker, msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, level, marker, msg, t)
  }

  fun log(level: Level, msg: Message) {
    delegate.logIfEnabled(FQCN, level, null, msg, null)
  }

  fun log(level: Level, msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, level, null, msg, t)
  }

  fun log(level: Level, msg: CharSequence) {
    delegate.logIfEnabled(FQCN, level, null, msg, null)
  }

  fun log(level: Level, msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, level, null, msg, t)
  }

  fun log(level: Level, msg: Any) {
    delegate.logIfEnabled(FQCN, level, null, msg, null)
  }

  fun log(level: Level, msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, level, null, msg, t)
  }

  inline fun log(level: Level, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, level, null, supplier.asLog4jSupplier(), null)
  }

  inline fun log(level: Level, t: Throwable, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, level, null, supplier.asLog4jSupplier(), t)
  }

  inline fun log(level: Level, marker: Marker, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, level, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun log(level: Level, marker: Marker, t: Throwable?, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, level, marker, supplier.asLog4jSupplier(), t)
  }

  fun trace(marker: Marker, msg: Message) {
    delegate.logIfEnabled(FQCN, Level.TRACE, marker, msg, null)
  }

  fun trace(marker: Marker, msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, marker, msg, t)
  }

  fun trace(marker: Marker, msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.TRACE, marker, msg, null)
  }

  fun trace(marker: Marker, msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, marker, msg, t)
  }

  fun trace(marker: Marker, msg: Any) {
    delegate.logIfEnabled(FQCN, Level.TRACE, marker, msg, null)
  }

  fun trace(marker: Marker, msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, marker, msg, t)
  }

  fun trace(msg: Message) {
    delegate.logIfEnabled(FQCN, Level.TRACE, null, msg, null)
  }

  fun trace(msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, null, msg, t)
  }

  fun trace(msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.TRACE, null, msg, null)
  }

  fun trace(msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, null, msg, t)
  }

  fun trace(msg: Any) {
    delegate.logIfEnabled(FQCN, Level.TRACE, null, msg, null)
  }

  fun trace(msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, null, msg, t)
  }

  inline fun trace(supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, null, supplier.asLog4jSupplier(), null)
  }

  inline fun trace(t: Throwable, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, null, supplier.asLog4jSupplier(), t)
  }

  inline fun trace(marker: Marker, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun trace(marker: Marker, t: Throwable?, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.TRACE, marker, supplier.asLog4jSupplier(), t)
  }

  // TODO entry with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  fun traceEntry(msg: CharSequence): EntryMessage {
    return delegate.traceEntry(SimpleMessage(msg))
  }

  // TODO entry with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  inline fun traceEntry(supplier: () -> CharSequence): EntryMessage? {
    return if(delegate.isTraceEnabled) delegate.traceEntry(SimpleMessage(supplier())) else null
  }

  @Suppress("NOTHING_TO_INLINE")
  inline fun traceEntry(vararg paramSuppliers: () -> Any?): EntryMessage {
    return delegate.traceEntry(*paramSuppliers.asLog4jSuppliers())
  }

  fun traceEntry(vararg params: Any?): EntryMessage {
    return delegate.traceEntry(null, params)
  }

  // TODO entry with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  fun traceEntry(message: Message): EntryMessage {
    return delegate.traceEntry(message)
  }

  inline fun <R : Any?> runInTrace(block: () -> R): R {
    return runInTrace(delegate.traceEntry(), block)
  }

  // TODO exit and catching with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  inline fun <R : Any?> runInTrace(entryMessage: EntryMessage, block: () -> R): R {
    return try {
      val result = block()
      when(result) {
        Unit -> delegate.traceExit(entryMessage)
        else -> delegate.traceExit(entryMessage, result)
      }
      result
    } catch (e: Throwable) {
      delegate.catching(e)
      throw e
    }
  }

  fun debug(marker: Marker, msg: Message) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, marker, msg, null)
  }

  fun debug(marker: Marker, msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, marker, msg, t)
  }

  fun debug(marker: Marker, msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, marker, msg, null)
  }

  fun debug(marker: Marker, msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, marker, msg, t)
  }

  fun debug(marker: Marker, msg: Any) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, marker, msg, null)
  }

  fun debug(marker: Marker, msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, marker, msg, t)
  }

  fun debug(msg: Message) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, null, msg, null)
  }

  fun debug(msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, null, msg, t)
  }

  fun debug(msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, null, msg, null)
  }

  fun debug(msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, null, msg, t)
  }

  fun debug(msg: Any) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, null, msg, null)
  }

  fun debug(msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, null, msg, t)
  }

  inline fun debug(supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, null, supplier.asLog4jSupplier(), null)
  }

  inline fun debug(t: Throwable, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, null, supplier.asLog4jSupplier(), t)
  }

  inline fun debug(marker: Marker, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun debug(marker: Marker, t: Throwable?, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.DEBUG, marker, supplier.asLog4jSupplier(), t)
  }

  fun info(marker: Marker, msg: Message) {
    delegate.logIfEnabled(FQCN, Level.INFO, marker, msg, null)
  }

  fun info(marker: Marker, msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.INFO, marker, msg, t)
  }

  fun info(marker: Marker, msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.INFO, marker, msg, null)
  }

  fun info(marker: Marker, msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.INFO, marker, msg, t)
  }

  fun info(marker: Marker, msg: Any) {
    delegate.logIfEnabled(FQCN, Level.INFO, marker, msg, null)
  }

  fun info(marker: Marker, msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.INFO, marker, msg, t)
  }

  fun info(msg: Message) {
    delegate.logIfEnabled(FQCN, Level.INFO, null, msg, null)
  }

  fun info(msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.INFO, null, msg, t)
  }

  fun info(msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.INFO, null, msg, null)
  }

  fun info(msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.INFO, null, msg, t)
  }

  fun info(msg: Any) {
    delegate.logIfEnabled(FQCN, Level.INFO, null, msg, null)
  }

  fun info(msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.INFO, null, msg, t)
  }

  inline fun info(supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.INFO, null, supplier.asLog4jSupplier(), null)
  }

  inline fun info(t: Throwable, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.INFO, null, supplier.asLog4jSupplier(), t)
  }

  inline fun info(marker: Marker, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.INFO, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun info(marker: Marker, t: Throwable?, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.INFO, marker, supplier.asLog4jSupplier(), t)
  }

  fun warn(marker: Marker, msg: Message) {
    delegate.logIfEnabled(FQCN, Level.WARN, marker, msg, null)
  }

  fun warn(marker: Marker, msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.WARN, marker, msg, t)
  }

  fun warn(marker: Marker, msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.WARN, marker, msg, null)
  }

  fun warn(marker: Marker, msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.WARN, marker, msg, t)
  }

  fun warn(marker: Marker, msg: Any) {
    delegate.logIfEnabled(FQCN, Level.WARN, marker, msg, null)
  }

  fun warn(marker: Marker, msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.WARN, marker, msg, t)
  }

  fun warn(msg: Message) {
    delegate.logIfEnabled(FQCN, Level.WARN, null, msg, null)
  }

  fun warn(msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.WARN, null, msg, t)
  }

  fun warn(msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.WARN, null, msg, null)
  }

  fun warn(msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.WARN, null, msg, t)
  }

  fun warn(msg: Any) {
    delegate.logIfEnabled(FQCN, Level.WARN, null, msg, null)
  }

  fun warn(msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.WARN, null, msg, t)
  }

  inline fun warn(supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.WARN, null, supplier.asLog4jSupplier(), null)
  }

  inline fun warn(t: Throwable, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.WARN, null, supplier.asLog4jSupplier(), t)
  }

  inline fun warn(marker: Marker, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.WARN, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun warn(marker: Marker, t: Throwable?, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.WARN, marker, supplier.asLog4jSupplier(), t)
  }

  fun error(marker: Marker, msg: Message) {
    delegate.logIfEnabled(FQCN, Level.ERROR, marker, msg, null)
  }

  fun error(marker: Marker, msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, marker, msg, t)
  }

  fun error(marker: Marker, msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.ERROR, marker, msg, null)
  }

  fun error(marker: Marker, msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, marker, msg, t)
  }

  fun error(marker: Marker, msg: Any) {
    delegate.logIfEnabled(FQCN, Level.ERROR, marker, msg, null)
  }

  fun error(marker: Marker, msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, marker, msg, t)
  }

  fun error(msg: Message) {
    delegate.logIfEnabled(FQCN, Level.ERROR, null, msg, null)
  }

  fun error(msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, null, msg, t)
  }

  fun error(msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.ERROR, null, msg, null)
  }

  fun error(msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, null, msg, t)
  }

  fun error(msg: Any) {
    delegate.logIfEnabled(FQCN, Level.ERROR, null, msg, null)
  }

  fun error(msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, null, msg, t)
  }

  inline fun error(supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, null, supplier.asLog4jSupplier(), null)
  }

  inline fun error(t: Throwable, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, null, supplier.asLog4jSupplier(), t)
  }

  inline fun error(marker: Marker, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun error(marker: Marker, t: Throwable?, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.ERROR, marker, supplier.asLog4jSupplier(), t)
  }

  fun fatal(marker: Marker, msg: Message) {
    delegate.logIfEnabled(FQCN, Level.FATAL, marker, msg, null)
  }

  fun fatal(marker: Marker, msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, marker, msg, t)
  }

  fun fatal(marker: Marker, msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.FATAL, marker, msg, null)
  }

  fun fatal(marker: Marker, msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, marker, msg, t)
  }

  fun fatal(marker: Marker, msg: Any) {
    delegate.logIfEnabled(FQCN, Level.FATAL, marker, msg, null)
  }

  fun fatal(marker: Marker, msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, marker, msg, t)
  }

  fun fatal(msg: Message) {
    delegate.logIfEnabled(FQCN, Level.FATAL, null, msg, null)
  }

  fun fatal(msg: Message, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, null, msg, t)
  }

  fun fatal(msg: CharSequence) {
    delegate.logIfEnabled(FQCN, Level.FATAL, null, msg, null)
  }

  fun fatal(msg: CharSequence, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, null, msg, t)
  }

  fun fatal(msg: Any) {
    delegate.logIfEnabled(FQCN, Level.FATAL, null, msg, null)
  }

  fun fatal(msg: Any, t: Throwable?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, null, msg, t)
  }

  inline fun fatal(supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, null, supplier.asLog4jSupplier(), null)
  }

  inline fun fatal(t: Throwable, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, null, supplier.asLog4jSupplier(), t)
  }

  inline fun fatal(marker: Marker, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, marker, supplier.asLog4jSupplier(), null)
  }

  inline fun fatal(marker: Marker, t: Throwable?, supplier: () -> Any?) {
    delegate.logIfEnabled(FQCN, Level.FATAL, marker, supplier.asLog4jSupplier(), t)
  }

}
