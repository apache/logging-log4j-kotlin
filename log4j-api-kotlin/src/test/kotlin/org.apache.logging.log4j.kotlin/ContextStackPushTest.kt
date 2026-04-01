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

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ContextStackPushTest {
  @AfterEach
  fun tearDown() {
    ContextStack.clear()
  }

  @Test
  fun `Single parameter push keeps value format`() {
    ContextStack.push("user={}", "admin")
    assertEquals("user=admin", ContextStack.peek())
  }

  @Test
  fun `Parameterized push keeps second parameter`() {
    ContextStack.push("user={} action={}", "admin", "login")
    assertEquals("user=admin action=login", ContextStack.peek())
  }

  @Test
  fun `Parameterized push keeps third parameter`() {
    ContextStack.push("a={} b={} c={}", "1", "2", "3")
    assertEquals("a=1 b=2 c=3", ContextStack.peek())
  }
}
