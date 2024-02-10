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

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
    GlobalScope.launch(loggingContext()) {
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
    GlobalScope.launch(additionalLoggingContext()) {
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
      // Scoped launch with non-inherited MDContext element
      launch(Dispatchers.Default) {
        assertEquals("myValue", ContextMap["myKey"])
        assertEquals("test", ContextStack.peek())
      }
      // Scoped launch with non-inherited MDContext element
      launch(Dispatchers.Default + loggingContext()) {
        assertTrue(ContextMap.empty)
        assertTrue(ContextStack.empty)
      }
      // Scoped launch with non-inherited MDContext element
      launch(Dispatchers.Default + loggingContext(mapOf("myKey2" to "myValue2"), listOf("test3"))) {
        assertEquals(null, ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test3"), ContextStack.view.asList())
      }
      // Scoped launch with inherited MDContext element
      launch(Dispatchers.Default + CoroutineThreadContext()) {
        assertEquals("myValue2", ContextMap["myKey"])
        assertEquals("test2", ContextStack.peek())
      }
      // Scoped launch with inherited plus additional empty MDContext element
      launch(Dispatchers.Default + additionalLoggingContext()) {
        assertEquals("myValue2", ContextMap["myKey"])
        assertEquals(listOf("test", "test2"), ContextStack.view.asList())
      }
      launch(Dispatchers.Default + additionalLoggingContext(mapOf("myKey2" to "myValue2"), listOf("test3"))) {
        assertEquals("myValue2", ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test", "test2", "test3"), ContextStack.view.asList())
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
    runBlocking(additionalLoggingContext()) {
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
    runBlocking(loggingContext()) {
      assertTrue(ContextMap.empty)
      assertTrue(ContextStack.empty)
    }
    runBlocking(additionalLoggingContext()) {
      assertTrue(ContextMap.empty)
      assertTrue(ContextStack.empty)
    }
  }

  @Test
  fun `Context using withContext`() = runBlocking {
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
    withContext(Dispatchers.Default + additionalLoggingContext()) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
      withContext(mainDispatcher) {
        assertEquals("myValue", ContextMap["myKey"])
        assertEquals("test", ContextStack.peek())
      }
    }
    withContext(Dispatchers.Default + additionalLoggingContext(mapOf("myKey2" to "myValue2"), listOf("test2"))) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("myValue2", ContextMap["myKey2"])
      assertEquals(listOf("test", "test2"), ContextStack.view.asList())
      withContext(mainDispatcher) {
        assertEquals("myValue", ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test", "test2"), ContextStack.view.asList())
      }
    }
    withContext(Dispatchers.Default + loggingContext(mapOf("myKey2" to "myValue2"), listOf("test2"))) {
      assertEquals(null, ContextMap["myKey"])
      assertEquals("myValue2", ContextMap["myKey2"])
      assertEquals(listOf("test2"), ContextStack.view.asList())
      withContext(mainDispatcher) {
        assertEquals(null, ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test2"), ContextStack.view.asList())
      }
    }
  }

  @Test
  fun `Context using withLoggingContext`() = runBlocking {
    ContextMap["myKey"] = "myValue"
    ContextStack.push("test")
    val mainDispatcher = coroutineContext[ContinuationInterceptor]!!
    withAdditionalLoggingContext(mapOf("myKey2" to "myValue2"), listOf("test2")) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("myValue2", ContextMap["myKey2"])
      assertEquals(listOf("test", "test2"), ContextStack.view.asList())
      withContext(mainDispatcher) {
        assertEquals("myValue", ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test", "test2"), ContextStack.view.asList())
      }
      withAdditionalLoggingContext(mapOf("myKey3" to "myValue3"), listOf("test3")) {
        assertEquals("myValue", ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals("myValue3", ContextMap["myKey3"])
        assertEquals(listOf("test", "test2", "test3"), ContextStack.view.asList())
      }
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("myValue2", ContextMap["myKey2"])
      assertEquals(null, ContextMap["myKey3"])
      assertEquals(listOf("test", "test2"), ContextStack.view.asList())
    }
    withLoggingContext(mapOf("myKey2" to "myValue2"), listOf("test2")) {
      assertEquals(null, ContextMap["myKey"])
      assertEquals("myValue2", ContextMap["myKey2"])
      assertEquals(listOf("test2"), ContextStack.view.asList())
      withContext(mainDispatcher) {
        assertEquals(null, ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test2"), ContextStack.view.asList())
      }
      withLoggingContext(mapOf("myKey3" to "myValue3"), listOf("test3")) {
        assertEquals(null, ContextMap["myKey"])
        assertEquals(null, ContextMap["myKey2"])
        assertEquals(listOf("test3"), ContextStack.view.asList())
      }
      assertEquals(null, ContextMap["myKey"])
      assertEquals("myValue2", ContextMap["myKey2"])
      assertEquals(null, ContextMap["myKey3"])
      assertEquals(listOf("test2"), ContextStack.view.asList())
    }
  }

  @Test
  fun `Context is restored after a context block is complete`() = runBlocking {
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)
    withContext(CoroutineThreadContext(ThreadContextData(mapOf("myKey" to "myValue"), listOf("test")))) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
      withContext(CoroutineThreadContext(ThreadContextData(mapOf("myKey2" to "myValue2"), listOf("test2")))) {
        assertEquals(null, ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test2"), ContextStack.view.asList())
      }
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
    }
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)

    withContext(loggingContext(mapOf("myKey" to "myValue"), listOf("test"))) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
      withContext(loggingContext(mapOf("myKey2" to "myValue2"), listOf("test2"))) {
        assertEquals(null, ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test2"), ContextStack.view.asList())
      }
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
    }
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)

    withLoggingContext(mapOf("myKey" to "myValue"), listOf("test")) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
      withLoggingContext(mapOf("myKey2" to "myValue2"), listOf("test2")) {
        assertEquals(null, ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test2"), ContextStack.view.asList())
      }
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
    }
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)

    withAdditionalLoggingContext(mapOf("myKey" to "myValue"), listOf("test")) {
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals("test", ContextStack.peek())
      withAdditionalLoggingContext(mapOf("myKey2" to "myValue2"), listOf("test2")) {
        assertEquals("myValue", ContextMap["myKey"])
        assertEquals("myValue2", ContextMap["myKey2"])
        assertEquals(listOf("test", "test2"), ContextStack.view.asList())
      }
      assertEquals("myValue", ContextMap["myKey"])
      assertEquals(null, ContextMap["myKey2"])
      assertEquals("test", ContextStack.peek())
    }
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)
  }

  @Test
  fun `Can override existing context, and restore it`() = runBlocking {
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)
    withLoggingContext(mapOf("myKey" to "myValue", "myKey2" to "myValue2"), listOf("test1", "test2")) {
      assertEquals(mapOf("myKey" to "myValue", "myKey2" to "myValue2"), ContextMap.view)
      assertEquals(listOf("test1", "test2"), ContextStack.view.asList())
      withLoggingContext(mapOf("myKey3" to "myValue3", "myKey4" to "myValue4"), listOf("test3", "test4")) {
        assertEquals(mapOf("myKey3" to "myValue3", "myKey4" to "myValue4"), ContextMap.view)
        assertEquals(listOf("test3", "test4"), ContextStack.view.asList())
        withAdditionalLoggingContext(mapOf("myKey4" to "myValue4Modified", "myKey5" to "myValue5"), listOf("test5")) {
          assertEquals(mapOf("myKey3" to "myValue3", "myKey4" to "myValue4Modified", "myKey5" to "myValue5"), ContextMap.view)
          assertEquals(listOf("test3", "test4", "test5"), ContextStack.view.asList())
        }
        assertEquals(mapOf("myKey3" to "myValue3", "myKey4" to "myValue4"), ContextMap.view)
        assertEquals(listOf("test3", "test4"), ContextStack.view.asList())
      }
      assertEquals(mapOf("myKey" to "myValue", "myKey2" to "myValue2"), ContextMap.view)
      assertEquals(listOf("test1", "test2"), ContextStack.view.asList())
    }
    assertTrue(ContextMap.empty)
    assertTrue(ContextStack.empty)
  }
}
