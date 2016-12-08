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
package org.apache.logging.log4j.kotlin.sample

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.kotlin.logger
import java.util.*

object LoggingApp {
  val log = logger()

  @JvmStatic
  fun main(args: Array<String>) {
    val s1 = "foo"
    val s2 = "bar"
    val t = RuntimeException("error")

    log.info { "Hello, world: $s1 $s2" }

    log.trace("Regular trace")

    log.trace {
      log.info("Inside trace extension!")
    }

    log.trace({ "Trace extension with entry message." }) {
      log.info("Inside trace extension with supplier!")
    }

    fun getKey(): Int = log.trace {
      Random().nextInt(10)
    }

    fun getKeyError(): Int = log.trace {
      throw Exception("Oops!")
    }

    log.info { "Key was ${getKey()}" }
    try {
      log.info { "Key was ${getKeyError()}" }
    } catch(e: Exception) {
      Unit
    }

    log.throwing(t)
    log.throwing(Level.INFO, t)

    log.catching(t)

  }
}
