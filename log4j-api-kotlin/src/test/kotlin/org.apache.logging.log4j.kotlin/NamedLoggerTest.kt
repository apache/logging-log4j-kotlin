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
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

const val loggerName = "Foo"

class NamedLoggerTest {

  val log = logger(loggerName)

  @Test
  fun `Logging from an explicitly named logger logs with the correct name`() {
    val msg = "This is an error log."
    val msgs = withListAppender { _, _ ->
      log.error(msg)
    }

    assertEquals(1, msgs.size.toLong())

    msgs.first().also {
      assertEquals(Level.ERROR, it.level)
      assertEquals(msg, it.message.format)
      assertEquals(loggerName, it.loggerName)
    }
  }
}
