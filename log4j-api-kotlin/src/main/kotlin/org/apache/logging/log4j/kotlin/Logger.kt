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
import org.apache.logging.log4j.message.EntryMessage
import org.apache.logging.log4j.message.Message
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
 * The adapter also provides a `runInTrace` utility that avoids having to call traceEnter and traceExit and
 * catch manually. Rather, simply call the `trace` method, passing in an [EntryMessage] and the block to execute
 * within trace enter/exit/catch calls. Location-awareness is currently broken for trace logging with this
 * method as the ExtendedLogger does not expose the enter/exit/catch calls with the FQCN parameter.
 *
 * Lastly, while Kotlin's delegation capabilities would normally allow this implementation to be
 * significantly less verbose by automatically delegating most methods to the ExtendedLogger delegate, this
 * would break location-awareness, as the ExtendedLogger delegate assumes its own FQCN is the root of the
 * logging stack.
 */
@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
class FunctionalLogger(private val log: ExtendedLogger): Logger by log {
  companion object {
    val FQCN: String = FunctionalLogger::class.java.name
    fun <T: Any?> (() -> T).asLog4jSupplier(): Supplier<T> = Supplier { invoke() }
    fun <T: Any?> (Array<out () -> T>).asLog4jSuppliers(): Array<Supplier<T>> = map { it.asLog4jSupplier() }.toTypedArray()
  }

  override fun log(level: Level, marker: Marker?, msg: Message?) {
    log.logIfEnabled(FQCN, level, marker, msg, null)
  }

  override fun log(level: Level, marker: Marker?, msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, marker, msg, t)
  }

  override fun log(level: Level, marker: Marker?, msg: CharSequence?) {
    log.logIfEnabled(FQCN, level, marker, msg, null)
  }

  override fun log(level: Level, marker: Marker?, msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, marker, msg, t)
  }

  override fun log(level: Level, marker: Marker?, msg: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, null)
  }

  override fun log(level: Level, marker: Marker?, msg: String?) {
    log.logIfEnabled(FQCN, level, marker, msg, null as Throwable?)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, *params)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, marker, msg, t)
  }

  override fun log(level: Level, msg: Message?) {
    log.logIfEnabled(FQCN, level, null, msg, null)
  }

  override fun log(level: Level, msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, null, msg, t)
  }

  override fun log(level: Level, msg: CharSequence?) {
    log.logIfEnabled(FQCN, level, null, msg, null)
  }

  override fun log(level: Level, msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, null, msg, t)
  }

  override fun log(level: Level, msg: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, null)
  }

  override fun log(level: Level, msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, null, msg, t)
  }

  override fun log(level: Level, msg: String?) {
    log.logIfEnabled(FQCN, level, null, msg, null as Throwable?)
  }

  override fun log(level: Level, msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, *params)
  }

  override fun log(level: Level, msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, null, msg, t)
  }

  override fun log(level: Level, msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, level, null, msgSupplier, null)
  }

  fun log(level: Level, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, level, null, supplier.asLog4jSupplier(), null)
  }

  override fun log(level: Level, msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, null, msgSupplier, t)
  }

  fun log(level: Level, t: Throwable, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, level, null, supplier.asLog4jSupplier(), t)
  }

  override fun log(level: Level, marker: Marker?, msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, level, marker, msgSupplier, null)
  }

  fun log(level: Level, marker: Marker?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, level, marker, supplier.asLog4jSupplier(), null)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, level, marker, msg, *paramSuppliers)
  }

  fun log(level: Level, marker: Marker?, msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, *paramSuppliers.asLog4jSuppliers())
  }

  override fun log(level: Level, marker: Marker?, msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, marker, msgSupplier, t)
  }

  fun log(level: Level, marker: Marker?, t: Throwable?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, level, marker, supplier.asLog4jSupplier(), t)
  }

  override fun log(level: Level, msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, level, null, msg, *paramSuppliers)
  }

  fun log(level: Level, msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, level, null, msg, *paramSuppliers.asLog4jSuppliers())
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(Marker, () -> Any?>)"))
  override fun log(level: Level, marker: Marker?, msgSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, level, marker, msgSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(Marker, Throwable, () -> Any?>)"))
  override fun log(level: Level, marker: Marker?, msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, marker, msgSupplier, t)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(() -> Any?>)"))
  override fun log(level: Level, messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, level, null, messageSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(Throwable, () -> Any?>)"))
  override fun log(level: Level, msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, null, msgSupplier, t)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p1)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p2, p2)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p1, p2, p3)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p1, p2, p3, p4)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun log(level: Level, marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun log(level: Level, marker: Marker?, msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, level, marker, msg, t)
  }

  override fun log(level: Level, msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1, p2)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1, p2, p3)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1, p2, p3, p4)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun log(level: Level, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, level, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun trace(marker: Marker?, msg: Message?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, null)
  }

  override fun trace(marker: Marker?, msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, t)
  }

  override fun trace(marker: Marker?, msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, null)
  }

  override fun trace(marker: Marker?, msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, t)
  }

  override fun trace(marker: Marker?, msg: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, null)
  }

  override fun trace(marker: Marker?, msg: String?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, null as Throwable?)
  }

  override fun trace(marker: Marker?, msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, *params)
  }

  override fun trace(marker: Marker?, msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, t)
  }

  override fun trace(msg: Message?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, null)
  }

  override fun trace(msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, t)
  }

  override fun trace(msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, null)
  }

  override fun trace(msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, t)
  }

  override fun trace(msg: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, null)
  }

  override fun trace(msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, t)
  }

  override fun trace(msg: String?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, null as Throwable?)
  }

  override fun trace(msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, *params)
  }

  override fun trace(msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, t)
  }

  override fun trace(msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msgSupplier, null)
  }

  fun trace(supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, supplier.asLog4jSupplier(), null)
  }

  override fun trace(msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msgSupplier, t)
  }

  fun trace(t: Throwable, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, supplier.asLog4jSupplier(), t)
  }

  override fun trace(marker: Marker?, msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msgSupplier, null)
  }

  fun trace(marker: Marker?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, supplier.asLog4jSupplier(), null)
  }

  override fun trace(marker: Marker?, msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, *paramSuppliers)
  }

  fun trace(marker: Marker?, msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, *paramSuppliers.asLog4jSuppliers())
  }

  override fun trace(marker: Marker?, msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msgSupplier, t)
  }

  fun trace(marker: Marker?, t: Throwable?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, supplier.asLog4jSupplier(), t)
  }

  override fun trace(msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, *paramSuppliers)
  }

  fun trace(msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, *paramSuppliers.asLog4jSuppliers())
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(Marker, () -> Any?>)"))
  override fun trace(marker: Marker?, msgSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msgSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(Marker, Throwable, () -> Any?>)"))
  override fun trace(marker: Marker?, msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msgSupplier, t)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(() -> Any?>)"))
  override fun trace(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, messageSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.trace(Throwable, () -> Any?>)"))
  override fun trace(msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msgSupplier, t)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p1)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p2, p2)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p1, p2, p3)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p1, p2, p3, p4)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun trace(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun trace(marker: Marker?, msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.TRACE, marker, msg, t)
  }

  override fun trace(msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1, p2)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1, p2, p3)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1, p2, p3, p4)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun trace(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.TRACE, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  // TODO entry with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun traceEntry(): EntryMessage {
    return log.traceEntry()
  }

  // TODO entry with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun traceEntry(format: String?, vararg params: Any?): EntryMessage {
    return log.traceEntry(format, *params)
  }

  // TODO entry with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun traceEntry(vararg paramSuppliers: Supplier<*>?): EntryMessage {
    return log.traceEntry(*paramSuppliers)
  }

  fun traceEntry(vararg paramSuppliers: () -> Any?): EntryMessage {
    return log.traceEntry(*paramSuppliers.asLog4jSuppliers())
  }

  // TODO entry with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun traceEntry(format: String?, vararg paramSuppliers: Supplier<*>?): EntryMessage {
    return log.traceEntry(format, *paramSuppliers)
  }

  fun traceEntry(format: String?, vararg paramSuppliers: () -> Any?): EntryMessage {
    return log.traceEntry(format, *paramSuppliers.asLog4jSuppliers())
  }

  // TODO entry with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun traceEntry(message: Message?): EntryMessage {
    return log.traceEntry(message)
  }

  // TODO exit with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun traceExit() {
    log.traceExit()
  }

  // TODO exit with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun <R : Any?> traceExit(format: String?, result: R): R {
    return log.traceExit(format, result)
  }

  // TODO exit with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun <R : Any?> traceExit(message: Message?, result: R): R {
    return log.traceExit(message, result)
  }

  // TODO exit with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun traceExit(message: EntryMessage?) {
    log.traceExit(message)
  }

  // TODO exit with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun <R : Any?> traceExit(result: R): R {
    return log.traceExit(result)
  }

  // TODO exit with fqcn is not part of the ExtendedLogger interface, location-awareness will be broken
  override fun <R : Any?> traceExit(message: EntryMessage?, result: R): R {
    return log.traceExit(message, result)
  }

  fun <R : Any?> runInTrace(block: () -> R): R {
    return runInTrace(traceEntry(), block)
  }

  fun <R : Any?> runInTrace(entryMessage: EntryMessage, block: () -> R): R {
    return try {
      val result = block()
      when(result) {
        Unit -> traceExit(entryMessage)
        else -> traceExit(entryMessage, result)
      }
      result
    } catch (e: Throwable) {
      catching(e)
      throw e
    }
  }

  override fun debug(marker: Marker?, msg: Message?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, null)
  }

  override fun debug(marker: Marker?, msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, t)
  }

  override fun debug(marker: Marker?, msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, null)
  }

  override fun debug(marker: Marker?, msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, t)
  }

  override fun debug(marker: Marker?, msg: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, null)
  }

  override fun debug(marker: Marker?, msg: String?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, null as Throwable?)
  }

  override fun debug(marker: Marker?, msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, *params)
  }

  override fun debug(marker: Marker?, msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, t)
  }

  override fun debug(msg: Message?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, null)
  }

  override fun debug(msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, t)
  }

  override fun debug(msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, null)
  }

  override fun debug(msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, t)
  }

  override fun debug(msg: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, null)
  }

  override fun debug(msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, t)
  }

  override fun debug(msg: String?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, null as Throwable?)
  }

  override fun debug(msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, *params)
  }

  override fun debug(msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, t)
  }

  override fun debug(msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msgSupplier, null)
  }

  fun debug(supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, supplier.asLog4jSupplier(), null)
  }

  override fun debug(msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msgSupplier, t)
  }

  fun debug(t: Throwable, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, supplier.asLog4jSupplier(), t)
  }

  override fun debug(marker: Marker?, msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msgSupplier, null)
  }

  fun debug(marker: Marker?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, supplier.asLog4jSupplier(), null)
  }

  override fun debug(marker: Marker?, msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, *paramSuppliers)
  }

  fun debug(marker: Marker?, msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, *paramSuppliers.asLog4jSuppliers())
  }

  override fun debug(marker: Marker?, msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msgSupplier, t)
  }

  fun debug(marker: Marker?, t: Throwable?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, supplier.asLog4jSupplier(), t)
  }

  override fun debug(msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, *paramSuppliers)
  }

  fun debug(msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, *paramSuppliers.asLog4jSuppliers())
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.debug(Marker, () -> Any?>)"))
  override fun debug(marker: Marker?, msgSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msgSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.debug(Marker, Throwable, () -> Any?>)"))
  override fun debug(marker: Marker?, msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msgSupplier, t)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.debug(() -> Any?>)"))
  override fun debug(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, messageSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.debug(Throwable, () -> Any?>)"))
  override fun debug(msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msgSupplier, t)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p1)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p2, p2)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p1, p2, p3)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p1, p2, p3, p4)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun debug(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun debug(marker: Marker?, msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.DEBUG, marker, msg, t)
  }

  override fun debug(msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1, p2)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1, p2, p3)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1, p2, p3, p4)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun debug(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.DEBUG, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun info(marker: Marker?, msg: Message?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, null)
  }

  override fun info(marker: Marker?, msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, t)
  }

  override fun info(marker: Marker?, msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, null)
  }

  override fun info(marker: Marker?, msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, t)
  }

  override fun info(marker: Marker?, msg: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, null)
  }

  override fun info(marker: Marker?, msg: String?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, null as Throwable?)
  }

  override fun info(marker: Marker?, msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, *params)
  }

  override fun info(marker: Marker?, msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, t)
  }

  override fun info(msg: Message?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, null)
  }

  override fun info(msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, t)
  }

  override fun info(msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, null)
  }

  override fun info(msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, t)
  }

  override fun info(msg: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, null)
  }

  override fun info(msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, t)
  }

  override fun info(msg: String?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, null as Throwable?)
  }

  override fun info(msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, *params)
  }

  override fun info(msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, t)
  }

  override fun info(msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msgSupplier, null)
  }

  fun info(supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, supplier.asLog4jSupplier(), null)
  }

  override fun info(msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msgSupplier, t)
  }

  fun info(t: Throwable, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, supplier.asLog4jSupplier(), t)
  }

  override fun info(marker: Marker?, msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msgSupplier, null)
  }

  fun info(marker: Marker?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, supplier.asLog4jSupplier(), null)
  }

  override fun info(marker: Marker?, msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, *paramSuppliers)
  }

  fun info(marker: Marker?, msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, *paramSuppliers.asLog4jSuppliers())
  }

  override fun info(marker: Marker?, msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msgSupplier, t)
  }

  fun info(marker: Marker?, t: Throwable?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, supplier.asLog4jSupplier(), t)
  }

  override fun info(msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, *paramSuppliers)
  }

  fun info(msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, *paramSuppliers.asLog4jSuppliers())
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.info(Marker, () -> Any?>)"))
  override fun info(marker: Marker?, msgSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msgSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.info(Marker, Throwable, () -> Any?>)"))
  override fun info(marker: Marker?, msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msgSupplier, t)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.info(() -> Any?>)"))
  override fun info(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.INFO, null, messageSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.info(Throwable, () -> Any?>)"))
  override fun info(msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msgSupplier, t)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p1)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p2, p2)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p1, p2, p3)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p1, p2, p3, p4)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun info(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun info(marker: Marker?, msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.INFO, marker, msg, t)
  }

  override fun info(msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1, p2)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1, p2, p3)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1, p2, p3, p4)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun info(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.INFO, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun warn(marker: Marker?, msg: Message?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, null)
  }

  override fun warn(marker: Marker?, msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, t)
  }

  override fun warn(marker: Marker?, msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, null)
  }

  override fun warn(marker: Marker?, msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, t)
  }

  override fun warn(marker: Marker?, msg: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, null)
  }

  override fun warn(marker: Marker?, msg: String?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, null as Throwable?)
  }

  override fun warn(marker: Marker?, msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, *params)
  }

  override fun warn(marker: Marker?, msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, t)
  }

  override fun warn(msg: Message?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, null)
  }

  override fun warn(msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, t)
  }

  override fun warn(msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, null)
  }

  override fun warn(msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, t)
  }

  override fun warn(msg: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, null)
  }

  override fun warn(msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, t)
  }

  override fun warn(msg: String?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, null as Throwable?)
  }

  override fun warn(msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, *params)
  }

  override fun warn(msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, t)
  }

  override fun warn(msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msgSupplier, null)
  }

  fun warn(supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, supplier.asLog4jSupplier(), null)
  }

  override fun warn(msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msgSupplier, t)
  }

  fun warn(t: Throwable, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, supplier.asLog4jSupplier(), t)
  }

  override fun warn(marker: Marker?, msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msgSupplier, null)
  }

  fun warn(marker: Marker?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, supplier.asLog4jSupplier(), null)
  }

  override fun warn(marker: Marker?, msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, *paramSuppliers)
  }

  fun warn(marker: Marker?, msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, *paramSuppliers.asLog4jSuppliers())
  }

  override fun warn(marker: Marker?, msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msgSupplier, t)
  }

  fun warn(marker: Marker?, t: Throwable?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, supplier.asLog4jSupplier(), t)
  }

  override fun warn(msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, *paramSuppliers)
  }

  fun warn(msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, *paramSuppliers.asLog4jSuppliers())
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.warn(Marker, () -> Any?>)"))
  override fun warn(marker: Marker?, msgSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msgSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.warn(Marker, Throwable, () -> Any?>)"))
  override fun warn(marker: Marker?, msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msgSupplier, t)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.warn(() -> Any?>)"))
  override fun warn(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.WARN, null, messageSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.warn(Throwable, () -> Any?>)"))
  override fun warn(msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msgSupplier, t)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p1)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p2, p2)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p1, p2, p3)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p1, p2, p3, p4)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun warn(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun warn(marker: Marker?, msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.WARN, marker, msg, t)
  }

  override fun warn(msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1, p2)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1, p2, p3)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1, p2, p3, p4)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun warn(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.WARN, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun error(marker: Marker?, msg: Message?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, null)
  }

  override fun error(marker: Marker?, msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, t)
  }

  override fun error(marker: Marker?, msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, null)
  }

  override fun error(marker: Marker?, msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, t)
  }

  override fun error(marker: Marker?, msg: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, null)
  }

  override fun error(marker: Marker?, msg: String?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, null as Throwable?)
  }

  override fun error(marker: Marker?, msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, *params)
  }

  override fun error(marker: Marker?, msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, t)
  }

  override fun error(msg: Message?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, null)
  }

  override fun error(msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, t)
  }

  override fun error(msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, null)
  }

  override fun error(msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, t)
  }

  override fun error(msg: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, null)
  }

  override fun error(msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, t)
  }

  override fun error(msg: String?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, null as Throwable?)
  }

  override fun error(msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, *params)
  }

  override fun error(msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, t)
  }

  override fun error(msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msgSupplier, null)
  }

  fun error(supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, supplier.asLog4jSupplier(), null)
  }

  override fun error(msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msgSupplier, t)
  }

  fun error(t: Throwable, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, supplier.asLog4jSupplier(), t)
  }

  override fun error(marker: Marker?, msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msgSupplier, null)
  }

  fun error(marker: Marker?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, supplier.asLog4jSupplier(), null)
  }

  override fun error(marker: Marker?, msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, *paramSuppliers)
  }

  fun error(marker: Marker?, msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, *paramSuppliers.asLog4jSuppliers())
  }

  override fun error(marker: Marker?, msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msgSupplier, t)
  }

  fun error(marker: Marker?, t: Throwable?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, supplier.asLog4jSupplier(), t)
  }

  override fun error(msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, *paramSuppliers)
  }

  fun error(msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, *paramSuppliers.asLog4jSuppliers())
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.error(Marker, () -> Any?>)"))
  override fun error(marker: Marker?, msgSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msgSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.error(Marker, Throwable, () -> Any?>)"))
  override fun error(marker: Marker?, msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msgSupplier, t)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.error(() -> Any?>)"))
  override fun error(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, messageSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.error(Throwable, () -> Any?>)"))
  override fun error(msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msgSupplier, t)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p1)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p2, p2)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p1, p2, p3)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p1, p2, p3, p4)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun error(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun error(marker: Marker?, msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.ERROR, marker, msg, t)
  }

  override fun error(msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1, p2)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1, p2, p3)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1, p2, p3, p4)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun error(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.ERROR, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun fatal(marker: Marker?, msg: Message?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, null)
  }

  override fun fatal(marker: Marker?, msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, t)
  }

  override fun fatal(marker: Marker?, msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, null)
  }

  override fun fatal(marker: Marker?, msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, t)
  }

  override fun fatal(marker: Marker?, msg: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, null)
  }

  override fun fatal(marker: Marker?, msg: String?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, null as Throwable?)
  }

  override fun fatal(marker: Marker?, msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, *params)
  }

  override fun fatal(marker: Marker?, msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, t)
  }

  override fun fatal(msg: Message?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, null)
  }

  override fun fatal(msg: Message?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, t)
  }

  override fun fatal(msg: CharSequence?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, null)
  }

  override fun fatal(msg: CharSequence?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, t)
  }

  override fun fatal(msg: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, null)
  }

  override fun fatal(msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, t)
  }

  override fun fatal(msg: String?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, null as Throwable?)
  }

  override fun fatal(msg: String?, vararg params: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, *params)
  }

  override fun fatal(msg: String?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, t)
  }

  override fun fatal(msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msgSupplier, null)
  }

  fun fatal(supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, supplier.asLog4jSupplier(), null)
  }

  override fun fatal(msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msgSupplier, t)
  }

  fun fatal(t: Throwable, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, supplier.asLog4jSupplier(), t)
  }

  override fun fatal(marker: Marker?, msgSupplier: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msgSupplier, null)
  }

  fun fatal(marker: Marker?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, supplier.asLog4jSupplier(), null)
  }

  override fun fatal(marker: Marker?, msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, *paramSuppliers)
  }

  fun fatal(marker: Marker?, msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, *paramSuppliers.asLog4jSuppliers())
  }

  override fun fatal(marker: Marker?, msgSupplier: Supplier<*>?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msgSupplier, t)
  }

  fun fatal(marker: Marker?, t: Throwable?, supplier: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, supplier.asLog4jSupplier(), t)
  }

  override fun fatal(msg: String?, vararg paramSuppliers: Supplier<*>?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, *paramSuppliers)
  }

  fun fatal(msg: String?, vararg paramSuppliers: () -> Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, *paramSuppliers.asLog4jSuppliers())
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.fatal(Marker, () -> Any?>)"))
  override fun fatal(marker: Marker?, msgSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msgSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.fatal(Marker, Throwable, () -> Any?>)"))
  override fun fatal(marker: Marker?, msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msgSupplier, t)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.fatal(() -> Any?>)"))
  override fun fatal(messageSupplier: MessageSupplier?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, messageSupplier, null)
  }

  @Deprecated("Use lambda methods.", ReplaceWith("log.fatal(Throwable, () -> Any?>)"))
  override fun fatal(msgSupplier: MessageSupplier?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msgSupplier, t)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p1)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p2, p2)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p1, p2, p3)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p1, p2, p3, p4)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun fatal(marker: Marker?, msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
  }

  override fun fatal(marker: Marker?, msg: Any?, t: Throwable?) {
    log.logIfEnabled(FQCN, Level.FATAL, marker, msg, t)
  }

  override fun fatal(msg: String?, p0: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?, p2: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1, p2)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1, p2, p3)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1, p2, p3, p4)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1, p2, p3, p4, p5)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1, p2, p3, p4, p5, p6)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1, p2, p3, p4, p5, p6, p7)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8)
  }

  override fun fatal(msg: String?, p0: Any?, p1: Any?, p2: Any?, p3: Any?, p4: Any?, p5: Any?, p6: Any?, p7: Any?, p8: Any?, p9: Any?) {
    log.logIfEnabled(FQCN, Level.FATAL, null, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)
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
