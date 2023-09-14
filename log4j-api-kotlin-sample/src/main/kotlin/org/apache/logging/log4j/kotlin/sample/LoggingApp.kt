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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.apache.logging.log4j.kotlin.logger
import java.util.*

@SuppressFBWarnings("PREDICTABLE_RANDOM", "DMI_RANDOM_USED_ONLY_ONCE")
object LoggingApp {
  val log = logger()

  @JvmStatic
  fun main(args: Array<String>) {
    val s1 = "foo"
    val s2 = "bar"

    log.info { "Hello, world: $s1 $s2" }

    log.trace("Regular trace")

    log.runInTrace {
      log.info("Inside trace extension!")
    }

    log.runInTrace(log.traceEntry({ "param1" }, { "param2" })) {
      log.info("Inside trace extension with params suppliers!")
    }

    fun getKey(): Int = log.runInTrace {
      Random().nextInt(10)
    }

    @SuppressFBWarnings("NP_ALWAYS_NULL")
    fun getKeyError(): Int = log.runInTrace {
      throw Exception("Oops!")
    }

    log.info { "Key was ${getKey()}" }
    try {
      log.info { "Key was ${getKeyError()}" }
    } catch(e: Exception) {
      log.info { "Key threw ${e.message}" }
    }
  }
}
