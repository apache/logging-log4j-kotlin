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
package org.apache.logging.log4j.kotlin.support

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.Logger
import org.apache.logging.log4j.test.appender.ListAppender

fun rootLogger() = LogManager.getRootLogger() as Logger

fun withListAppender(block: (rootLogger: Logger, appender: ListAppender) -> Unit): List<LogEvent> {
  val appender = ListAppender("List").apply { start() }
  val root = rootLogger().apply { addAppender(appender) }

  try {
    block(root, appender)
    return appender.events
  } finally {
    appender.stop()
    root.removeAppender(appender)
  }
}
