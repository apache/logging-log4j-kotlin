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
import org.apache.logging.log4j.ThreadContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.ContinuationInterceptor
import kotlin.test.assertEquals

class ThreadContextTest {
  @Before
  fun setUp() {
    ThreadContext.clearAll()
  }

  @After
  fun tearDown() {
    ThreadContext.clearAll()
  }

  @Test
  fun `Context is not passed by default between coroutines`() = runBlocking<Unit> {
    ThreadContext.put("myKey", "myValue")
    // Scoped launch with MDCContext element
    GlobalScope.launch {
      assertEquals(null, ThreadContext.get("myKey"))
    }.join()
  }

  @Test
  fun `Context can be passed between coroutines`() = runBlocking<Unit> {
    ThreadContext.put("myKey", "myValue")
    // Scoped launch with MDCContext element
    GlobalScope.launch(CoroutineThreadContext()) {
      assertEquals("myValue", ThreadContext.get("myKey"))
    }.join()
  }

  @Test
  fun `Context inheritance`() = runBlocking<Unit> {
    ThreadContext.put("myKey", "myValue")
    // Scoped launch with MDCContext element
    withContext(CoroutineThreadContext()) {
      ThreadContext.put("myKey", "myValue2")
      // Scoped launch with inherited MDContext element
      launch(Dispatchers.Default) {
        assertEquals("myValue", ThreadContext.get("myKey"))
      }
    }
    assertEquals("myValue", ThreadContext.get("myKey"))
  }

  @Test
  fun `Context passed while on main thread`() {
    ThreadContext.put("myKey", "myValue")
    // No MDCContext element
    runBlocking {
      assertEquals("myValue", ThreadContext.get("myKey"))
    }
  }

  @Test
  fun `Context can be passed while on main thread`() {
    ThreadContext.put("myKey", "myValue")
    runBlocking(CoroutineThreadContext()) {
      assertEquals("myValue", ThreadContext.get("myKey"))
    }
  }

  @Test
  fun `Context may be empty`() {
    runBlocking(CoroutineThreadContext()) {
      assertEquals(null, ThreadContext.get("myKey"))
    }
  }

  @Test
  fun `Context with context`() = runBlocking {
    ThreadContext.put("myKey", "myValue")
    val mainDispatcher = coroutineContext[ContinuationInterceptor]!!
    withContext(Dispatchers.Default + CoroutineThreadContext()) {
      assertEquals("myValue", ThreadContext.get("myKey"))
      withContext(mainDispatcher) {
        assertEquals("myValue", ThreadContext.get("myKey"))
      }
    }
  }
}
