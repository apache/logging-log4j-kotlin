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
import org.apache.logging.log4j.MarkerManager
import org.apache.logging.log4j.message.DefaultFlowMessageFactory
import org.apache.logging.log4j.message.EntryMessage
import org.apache.logging.log4j.message.MessageFactory2
import org.apache.logging.log4j.message.ParameterizedMessage
import org.apache.logging.log4j.message.ParameterizedMessageFactory
import org.apache.logging.log4j.spi.ExtendedLogger
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.KStubbing
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

data class Custom(val i: Int)

interface Manager {
  fun fetchValue(): Int
}

typealias LoggerStubbingFn = KStubbing<ExtendedLogger>.() -> Unit

class LoggerTest {
  companion object {
    val msg = ParameterizedMessage("msg {}", 17)
    val entryMsg = DefaultFlowMessageFactory().newEntryMessage(msg)
    val cseqMsg: CharSequence = StringBuilder().append("cseq msg")
    val objectMsg = Custom(17)
    val cause = RuntimeException("cause")
    val marker = MarkerManager.getMarker("marker")
    val result = "foo"
    val managerValue: Int = 4711
  }

  class Fixture(stubbing: LoggerStubbingFn? = null) {
    val mockLogger = mock<ExtendedLogger> {
      on { getMessageFactory<MessageFactory2>() } doReturn ParameterizedMessageFactory()
      if(stubbing != null) stubbing()
    }

    val manager = mock<Manager> {
      on { fetchValue() } doReturn managerValue
    }
  }

  fun withFixture(stubbing: LoggerStubbingFn?, level: Level, returning: Boolean, block: Fixture.(KotlinLogger) -> Unit): Fixture {
    val f = Fixture(stubbing)
    whenever(f.mockLogger.isEnabled(level)).thenReturn(returning)
    val logger = KotlinLogger(f.mockLogger)
    block(f, logger)
    return f
  }

  fun withLevelFixture(level: Level, returning: Boolean, block: Fixture.(KotlinLogger) -> Unit): Fixture {
    return withFixture({
      on { isEnabled(level) } doReturn returning
    }, level, returning, block)
  }

  @Test
  fun `Logging works!`() {
    val f = withLevelFixture(Level.ERROR, true) {
      it.error(result)
    }
    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.ERROR), isNull(), eq<CharSequence>(result), isNull())
  }

  @Test
  fun `Level fatal enabled with String message`() {
    val f = withLevelFixture(Level.FATAL, true) {
      it.fatal("string msg with value: ${manager.fetchValue()}")
    }
    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.FATAL), isNull(), eq<CharSequence>("string msg with value: $managerValue"), isNull())
    verify(f.manager).fetchValue()
  }


  @Test
  fun `Level fatal disabled with String message`() {
    val f = withLevelFixture(Level.FATAL, false) {
      it.fatal("string msg with value: ${manager.fetchValue()}")
    }

    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.FATAL), isNull(), eq<CharSequence>("string msg with value: $managerValue"), isNull())
    // one might expect times(0), but we just delegate so times(1), we don't have any extra macro-based logic like the Scala api
    // use the lambda approach to not evaluate the message if the level is not enabled
    verify(f.manager, times(1)).fetchValue()
  }

  @Test
  fun `Lambda functions are evaluated if the level is high enough`() {
    var count = 0
    fun lamdaFun(): String {
      count++
      return result
    }
    val log = logger()
    log.error { lamdaFun() }
    assertEquals(1, count)
  }

  @Test
  fun `Lambda functions are not evaluated if the level is low enough`() {
    var count = 0
    fun lamdaFun(): String {
      count++
      return result
    }
    val log = logger()
    log.debug { lamdaFun() }
    assertEquals(0, count)
  }

  @Test
  fun `CharSequence messages are logged`() {
    val f = withLevelFixture(Level.INFO, true) {
      it.info(cseqMsg)
    }
    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.INFO), isNull(), eq(cseqMsg), isNull())
  }

  @Test
  fun `Object messages are logged`() {
    val f = withLevelFixture(Level.INFO, true) {
      it.info(objectMsg)
    }
    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.INFO), isNull(), eq(objectMsg), isNull())
  }

  @Test
  fun `Markers are logged`() {
    val f = withLevelFixture(Level.INFO, true) {
      it.info(marker, result)
    }
    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.INFO), eq(marker), eq<CharSequence>(result), isNull())
  }

  @Test
  fun `Run in trace with no result`() {
    var count = 0
    val f = withLevelFixture(Level.INFO, true) {
      it.runInTrace(entryMsg) {
        ++count
        Unit
      }
    }
    assertEquals(1, count)
    verify(f.mockLogger).traceExit(eq(entryMsg))
  }

  @Test
  fun `Run in trace with result`() {
    var count = 0
    var returnedCount = 0
    val f = withLevelFixture(Level.INFO, true) {
      returnedCount = it.runInTrace(entryMsg) {
        ++count
      }
    }
    assertEquals(1, count)
    assertEquals(1, returnedCount)
    verify(f.mockLogger).traceEntry(eq(entryMsg))
    verify(f.mockLogger).traceExit(eq(entryMsg), eq(1))
  }

  @Test
  fun `Run in trace with Exception`() {
    var count = 0
    val f = withLevelFixture(Level.INFO, true) {
      assertFailsWith<RuntimeException> {
        it.runInTrace(entryMsg) {
          ++count
          throw cause
        }
      }
    }
    assertEquals(1, count)
    verify(f.mockLogger, times(1)).traceEntry(any<EntryMessage>())
    verify(f.mockLogger, times(0)).traceExit(any())
    verify(f.mockLogger).catching(argThat { message == "cause" })
  }
}
