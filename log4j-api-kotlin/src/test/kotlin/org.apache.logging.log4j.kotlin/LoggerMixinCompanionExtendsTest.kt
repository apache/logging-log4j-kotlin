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
import org.apache.logging.log4j.kotlin.support.withListAppender
import org.junit.Test
import kotlin.test.assertEquals

class LoggerMixinCompanionExtendsTest {

  companion object : Logging

  // note: using LoggerContextRule here to init the config does nothing as the initialization happens in the companion
  // log4j will fall back to the default config

  @Test
  fun `Logging from an interface mix-in via companion logs the correct class name`() {
    val msg = "This is an error log."
    val msgs = withListAppender { _, _ ->
      logger.error(msg)
    }

    assertEquals(1, msgs.size.toLong())

    msgs.first().also {
      assertEquals(Level.ERROR, it.level)
      assertEquals(msg, it.message.format)
      assertEquals(LoggerMixinCompanionExtendsTest::class.qualifiedName, it.loggerName)
    }
  }
}
