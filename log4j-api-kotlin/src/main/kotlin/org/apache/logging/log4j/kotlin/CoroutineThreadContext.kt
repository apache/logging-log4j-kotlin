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

import kotlinx.coroutines.ThreadContextElement
import org.apache.logging.log4j.ThreadContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * The value of [ThreadContext] map and stack.
 * See [ThreadContext.getImmutableContext] and [ThreadContext.getImmutableStack].
 */
data class ThreadContextData(
  val map: Map<String, String>?,
  val stack: Collection<String>?
)

/**
 * Log4j2 [ThreadContext] element for [CoroutineContext].
 *
 * This is based on the SLF4J MDCContext maintained by Jetbrains:
 * https://github.com/Kotlin/kotlinx.coroutines/blob/master/integration/kotlinx-coroutines-slf4j/src/MDCContext.kt
 *
 * Example:
 *
 * ```
 * ThreadContext.put("kotlin", "rocks") // Put a value into the Thread context
 *
 * launch(CoroutineThreadContext()) {
 *     logger.info { "..." }   // The Thread context contains the mapping here
 * }
 * ```
 *
 * Note, that you cannot update Thread context from inside of the coroutine simply
 * using [ThreadContext.put]. These updates are going to be lost on the next suspension and
 * reinstalled to the Thread context that was captured or explicitly specified in
 * [contextMap] when this object was created on the next resumption.
 * Use `withContext(ThreadContext()) { ... }` to capture updated map of Thread keys and values
 * for the specified block of code.
 *
 * @param contextMap the value of [Thread] context map.
 * Default value is the copy of the current thread's context map that is acquired via
 * [ThreadContext.getContext].
 */
class CoroutineThreadContext(
  /**
   * The value of [Thread] context map.
   */
  val contextData: ThreadContextData = ThreadContextData(ThreadContext.getImmutableContext(), ThreadContext.getImmutableStack())
) : ThreadContextElement<ThreadContextData>, AbstractCoroutineContextElement(Key) {
  /**
   * Key of [ThreadContext] in [CoroutineContext].
   */
  companion object Key : CoroutineContext.Key<CoroutineThreadContext>

  /** @suppress */
  override fun updateThreadContext(context: CoroutineContext): ThreadContextData {
    val oldState = ThreadContextData(ThreadContext.getImmutableContext(), ThreadContext.getImmutableStack())
    setCurrent(contextData)
    return oldState
  }

  /** @suppress */
  override fun restoreThreadContext(context: CoroutineContext, oldState: ThreadContextData) {
    setCurrent(oldState)
  }

  private fun setCurrent(contextData: ThreadContextData) {
    if (contextData.map == null) {
      ThreadContext.clearMap()
    } else {
      ThreadContext.putAll(contextData.map)
    }
    if (contextData.stack == null) {
      ThreadContext.clearStack()
    } else {
      ThreadContext.setStack(contextData.stack)
    }
  }
}
