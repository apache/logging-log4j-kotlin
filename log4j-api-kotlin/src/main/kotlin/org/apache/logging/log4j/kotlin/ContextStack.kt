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
import org.apache.logging.log4j.ThreadContext.ContextStack as ThreadContextStack

/**
 * Provides stack-based storage for contextual data used in logging. This is provided as a facade around
 * [ThreadContext] stack operations.
 *
 * @since 1.3.0
 */
object ContextStack {

  /**
   * Returns the depth of the current context stack. If the context stack is disabled, this always returns `0`.
   *
   * @see ThreadContext.getDepth
   */
  val depth: Int
    get() = ThreadContext.getDepth()

  /**
   * Indicates whether the current context stack is empty. If the context stack is disabled, this always returns `true`.
   */
  val empty: Boolean
    get() = depth == 0

  /**
   * Returns an immutable view of the current context stack. If the context stack is disabled, this always returns
   * an empty stack.
   *
   * @see ThreadContext.getImmutableStack
   */
  val view: ThreadContextStack
    get() = ThreadContext.getImmutableStack()

  /**
   * Returns a mutable copy of the current context stack. If the context stack is disabled, this always returns a new
   * stack.
   *
   * @see ThreadContext.cloneStack
   */
  fun copy(): ThreadContextStack = ThreadContext.cloneStack()

  /**
   * Clears the current context stack of all its messages. If the context stack is disabled, this does nothing.
   *
   * @see ThreadContext.clearStack
   */
  fun clear() = ThreadContext.clearStack()

  /**
   * Overwrites the current context stack using the provided collection of messages. If the context stack is disabled,
   * this does nothing.
   *
   * @see ThreadContext.setStack
   */
  fun set(stack: Collection<String>) = ThreadContext.setStack(stack)

  /**
   * Removes and returns the top-most message in the current context stack. If the context stack is empty or disabled,
   * this will always return an empty string.
   *
   * @see ThreadContext.pop
   */
  fun pop(): String = ThreadContext.pop()

  /**
   * Returns without removing the top-most message in the current context stack. If the context stack is empty or
   * disabled, this will always return an empty string.
   *
   * @see ThreadContext.peek
   */
  fun peek(): String = ThreadContext.peek()

  /**
   * Adds the provided message to the top of the current context stack. If the context stack is disabled, this does
   * nothing.
   *
   * @see ThreadContext.push
   */
  fun push(message: String) = ThreadContext.push(message)

  /**
   * Adds a formatted message to the top of the current context stack using the provided parameterized message and
   * arguments.
   *
   * @see ThreadContext.push
   */
  fun push(message: String, vararg args: Any?) = ThreadContext.push(message, args)

  /**
   * Trims the current context stack to at most the given depth.
   *
   * @see ThreadContext.trim
   */
  fun trim(depth: Int) = ThreadContext.trim(depth)

}
