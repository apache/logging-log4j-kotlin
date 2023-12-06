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

import kotlinx.coroutines.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.coroutines.ContinuationInterceptor
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ThreadContextTest {
  @BeforeEach
  fun setUp() {
    ContextMap.clear()
    ContextStack.clear()
  }

  @AfterEach
  fun tearDown() {
    ContextMap.clear()
    ContextStack.clear()
  }

  @DelicateCoroutinesApi
  @Test
  fun `Context is not passed by default between coroutines`() = runBlocking {
    ContextMap["myKey"] = "myValue"
    ContextStack.push("test")
    // Scoped launch with MDCContext element
    GlobalScope.launch {
      assertNull(ContextMap["myKey"])
      assertTrue(ContextStack.empty)
    }.join()
  }

  @DelicateCoroutinesApi
  @Test
  fun `Context can be passed between coroutines`() = runBlocking {
    ContextMap["myKey"] = "myValue"
    ContextStack.push("test")
    // Scoped launch with MDCContext element
    GlobalScope.launch(CoroutineThreadContext()) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
    }.join()
  }

  @Test
  fun `Context inheritance`() = runBlocking {
    ContextMap["myKey"] = "myValue"
    ContextStack.push("test")
    // Scoped launch with MDCContext element
    withContext(CoroutineThreadContext()) {
      ContextMap["myKey"] = "myValue2"
      ContextStack.push("test2")
      // Scoped launch with inherited MDContext element
      launch(Dispatchers.Default) {
        assertEquals("myValue", ContextMap["myKey"])
        assertEquals("test", ContextStack.peek())
      }
    }
    assertEquals("myValue", ContextMap["myKey"])
    assertEquals("test", ContextStack.peek())
  }

  @Test
  fun `Context passed while on main thread`() {
    ContextMap["myKey"] = "myValue"
    ContextStack.push("test")
    // No MDCContext element
    runBlocking {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
    }
  }

  @Test
  fun `Context can be passed while on main thread`() {
    ContextMap["myKey"] = "myValue"
    ContextStack.push("test")
    runBlocking(CoroutineThreadContext()) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
    }
  }

  @Test
  fun `Context may be empty`() {
    runBlocking(CoroutineThreadContext()) {
      assertTrue(ContextMap.empty)
      assertTrue(ContextStack.empty)
    }
  }

  @Test
  fun `Context with context`() = runBlocking {
    ContextMap["myKey"] = "myValue"
    ContextStack.push("test")
    val mainDispatcher = coroutineContext[ContinuationInterceptor]!!
    withContext(Dispatchers.Default + CoroutineThreadContext()) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
      withContext(mainDispatcher) {
        assertEquals("myValue", ContextMap["myKey"])
        assertEquals("test", ContextStack.peek())
      }
    }
  }

  @Test
  fun `Context is restored after a context block is complete`() = runBlocking {
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)
    withContext(CoroutineThreadContext(ThreadContextData(mapOf("myKey" to "myValue"), listOf("test")))) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
    }
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)
  }
}
