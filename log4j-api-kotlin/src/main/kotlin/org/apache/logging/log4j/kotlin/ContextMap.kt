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

import org.apache.logging.log4j.ThreadContext

/**
 * Provides map-based storage for contextual data used in logging. This is provided as a facade around
 * [ThreadContext] map operations.
 *
 * @since 1.3.0
 */
object ContextMap {

  /**
   * Checks if the provided key is defined in the current context map. If the context map is disabled,
   * this will always return `false`.
   *
   * Example usage:
   *
   * ```
   * if ("requestId" !in ContextMap) {
   *   ContextMap["requestId"] = UUID.randomUUID().toString()
   * }
   * ```
   *
   * @see ThreadContext.containsKey
   */
  operator fun contains(key: String): Boolean = ThreadContext.containsKey(key)

  /**
   * Gets the value corresponding to the provided key from the current context map. If the context map
   * is disabled, this will always return `null`.
   *
   * Example usage:
   *
   * ```
   * val value = ContextMap["key"]
   * ```
   *
   * @see ThreadContext.get
   */
  operator fun get(key: String): String? = ThreadContext.get(key)

  /**
   * Puts a value in the current context map for the given key. If the value is null, this is effectively
   * the same as removing the key. If the context map is disabled, this does nothing.
   *
   * Example usage:
   *
   * ```
   * ContextMap["key"] = "value"
   * ```
   *
   * @see ThreadContext.put
   */
  operator fun set(key: String, value: String?) = ThreadContext.put(key, value)

  /**
   * Puts a key/value pair into the current context map. If the context map is disabled, this does nothing.
   *
   * Example usage:
   *
   * ```
   * ContextMap += "key" to "value"
   * ```
   *
   * @see ThreadContext.put
   */
  operator fun plusAssign(pair: Pair<String, String?>) = ThreadContext.put(pair.first, pair.second)

  /**
   * Puts all the entries from the provided map into the current context map. If the context map is disabled,
   * this does nothing.
   *
   * Example usage:
   *
   * ```
   * ContextMap += mapOf("key" to "value", "otherKey" to "anotherValue")
   * ```
   *
   * @see ThreadContext.putAll
   */
  operator fun plusAssign(map: Map<String, String>) = ThreadContext.putAll(map)

  /**
   * Removes the provided key from the current context map. If the context map is disabled, this does nothing.
   *
   * Example usage:
   *
   * ```
   * ContextMap -= "key"
   * ```
   *
   * @see ThreadContext.remove
   */
  operator fun minusAssign(key: String) = ThreadContext.remove(key)

  /**
   * Removes the provided keys from the current context map. If the context map is disabled, this does nothing.
   *
   * Example usage:
   *
   * ```
   * ContextMap -= listOf("key1", "key2", "key3")
   * ```
   *
   * @see ThreadContext.removeAll
   */
  operator fun minusAssign(keys: Iterable<String>) = ThreadContext.removeAll(keys)

  /**
   * Indicates if the current context map is empty. If the context map is disabled, this always returns `true`.
   *
   * @see ThreadContext.isEmpty
   */
  val empty: Boolean
    get() = ThreadContext.isEmpty()

  /**
   * Provides an immutable view of the current context map. If the context map is disabled, this is always an
   * empty map.
   *
   * @see ThreadContext.getImmutableContext
   */
  val view: Map<String, String>
    get() = ThreadContext.getImmutableContext()

  /**
   * Provides a mutable copy of the current context map. If the context map is disabled, this is always a new
   * mutable map.
   *
   * @see ThreadContext.getContext
   */
  fun copy(): MutableMap<String, String> = ThreadContext.getContext()

  /**
   * Clears the current context map of all data. If the context map is disabled, this does nothing.
   *
   * @see ThreadContext.clearMap
   */
  fun clear() = ThreadContext.clearMap()

}
