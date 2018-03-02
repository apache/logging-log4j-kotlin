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

import com.nhaarman.mockito_kotlin.*
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.MarkerManager
import org.apache.logging.log4j.junit.LoggerContextRule
import org.apache.logging.log4j.message.DefaultFlowMessageFactory
import org.apache.logging.log4j.message.MessageFactory2
import org.apache.logging.log4j.message.ParameterizedMessage
import org.apache.logging.log4j.message.ParameterizedMessageFactory
import org.apache.logging.log4j.spi.ExtendedLogger
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import kotlin.test.assertTrue

data class Custom(val i: Int)

interface Manager {
  fun fetchValue(): Int
}

typealias LoggerStubbingFn = KStubbing<ExtendedLogger>.() -> Unit

class LoggerTest {
  @Rule @JvmField var init = LoggerContextRule("InfoLogger.xml")

  val msg = ParameterizedMessage("msg {}", 17)
  val entryMsg = DefaultFlowMessageFactory().newEntryMessage(msg)
  val cseqMsg: CharSequence = StringBuilder().append("cseq msg")
  val objectMsg = Custom(17)
  val cause = RuntimeException("cause")
  val marker = MarkerManager.getMarker("marker")
  val result = "foo"

  class Fixture(stubbing: LoggerStubbingFn? = null) {
    val mockLogger = mock<ExtendedLogger> {
      on { getMessageFactory<MessageFactory2>() } doReturn ParameterizedMessageFactory()
      if(stubbing != null) stubbing()
    }

    val manager = mock<Manager> {
      on { fetchValue() } doReturn 4711
    }
  }

  @Test
  fun `Logging works!`() {
    val f = Fixture {
      on { isEnabled(Level.ERROR) } doReturn true
    }
    whenever(f.mockLogger.isEnabled(Level.ERROR)).thenReturn(true)
    val logger = KotlinLogger(f.mockLogger)
    val msg = "This is an error log."
    logger.error(msg)
    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.ERROR), isNull(), eq<CharSequence>(msg), isNull())
  }

  @Test
  fun `Level fatal enabled with String message`() {
    val f = Fixture {
      on { isEnabled(Level.FATAL) } doReturn true
    }
    val logger = KotlinLogger(f.mockLogger)
    val msg = "string msg with value: ${f.manager.fetchValue()}"
    logger.fatal(msg)
    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.FATAL), isNull(), eq<CharSequence>(msg), isNull())
    verify(f.manager).fetchValue()
  }


  @Test
  fun `Level fatal disabled with String message`() {
    // this should fail but it doesn't because unlike Scala, we just delegate, we don't have any extra logic
    val f = Fixture {
      on { isEnabled(Level.FATAL) } doReturn false
    }
    whenever(f.mockLogger.isEnabled(Level.FATAL)).thenReturn(false)
    val logger = KotlinLogger(f.mockLogger)
    val msg = "string msg with value: ${f.manager.fetchValue()}"
    logger.fatal(msg)
    verify(f.mockLogger).logIfEnabled(anyString(), eq(Level.FATAL), isNull(), eq<CharSequence>(msg), isNull())
    verify(f.manager).fetchValue()
  }

  @Test
  fun `Lambda functions are evaluated if the level is high enough`() {
    var count = 0
    fun lamdaFun(): String {
      count++
      return "This should be evaluated."
    }
    val log = logger()
    log.info { lamdaFun() }
    assertTrue { count == 1 }
  }

  @Test
  fun `Lambda functions are not evaluated if the level is low enough`() {
    var count = 0
    fun lamdaFun(): String {
      count++
      return "This should never be evaluated."
    }
    val log = logger()
    log.debug { lamdaFun() }
    assertTrue { count == 0 }
  }
}
