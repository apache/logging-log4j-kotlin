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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import kotlinx.coroutines.ThreadContextElement
import org.apache.logging.log4j.ThreadContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * Snapshot of the current [ContextMap] and [ContextStack].
 *
 * @param map immutable view of the current context map
 * @param stack immutable view of the current context stack
 * @see ContextMap.view
 * @see ContextStack.view
 */
@SuppressFBWarnings("EI_EXPOSE_REP", "EI_EXPOSE_REP2")
data class ThreadContextData(
  val map: Map<String, String>? = ContextMap.view,
  val stack: Collection<String>? = ContextStack.view
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
 * ContextMap["kotlin"] = "rocks" // Put a value into the Thread context
 *
 * launch(CoroutineThreadContext()) {
 *     logger.info { "..." }   // The Thread context contains the mapping here
 * }
 * ```
 *
 * Note, that you cannot update Thread context from inside of the coroutine simply
 * using [ThreadContext.put] or [ContextMap.set]. These updates are going to be lost on the next suspension and
 * reinstalled to the Thread context that was captured or explicitly specified in
 * [contextData] when this object was created on the next resumption.
 * Use `withContext(CoroutineThreadContext()) { ... }` to capture updated map of Thread keys and values
 * for the specified block of code.
 *
 * @param contextData the value of [Thread] context map and context stack.
 * Default value is the copy of the current thread's context map that is acquired via
 * [ContextMap.view] and [ContextStack.view].
 */
class CoroutineThreadContext(
  /**
   * The value of [Thread] context map.
   */
  val contextData: ThreadContextData = ThreadContextData()
) : ThreadContextElement<ThreadContextData>, AbstractCoroutineContextElement(Key) {
  /**
   * Key of [ThreadContext] in [CoroutineContext].
   */
  companion object Key : CoroutineContext.Key<CoroutineThreadContext>

  /** @suppress */
  @SuppressFBWarnings("EI_EXPOSE_REP")
  override fun updateThreadContext(context: CoroutineContext): ThreadContextData {
    val oldState = ThreadContextData(ContextMap.view, ContextStack.view)
    setCurrent(contextData)
    return oldState
  }

  /** @suppress */
  @SuppressFBWarnings("EI_EXPOSE_REP")
  override fun restoreThreadContext(context: CoroutineContext, oldState: ThreadContextData) {
    setCurrent(oldState)
  }

  private fun setCurrent(contextData: ThreadContextData) {
    ContextMap.clear()
    ContextStack.clear()
    contextData.map?.let { ContextMap += it }
    contextData.stack?.let { ContextStack.set(it) }
  }
}
