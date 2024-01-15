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
import org.apache.logging.log4j.kotlin.Logging
import java.util.Random

@SuppressFBWarnings("PREDICTABLE_RANDOM", "DMI_RANDOM_USED_ONLY_ONCE")
object LoggingAppMixin: Logging {
  @JvmStatic
  fun main(args: Array<String>) {
    val s1 = "foo"
    val s2 = "bar"

    logger.info { "Hello, world: $s1 $s2" }

    logger.trace("Regular trace")

    logger.runInTrace {
      logger.info("Inside trace extension!")
    }

    logger.runInTrace(logger.traceEntry({ "param1" }, { "param2" })) {
      logger.info("Inside trace extension with params suppliers!")
    }

    fun getKey(): Int = logger.runInTrace {
      Random().nextInt(10)
    }

    @SuppressFBWarnings("NP_ALWAYS_NULL")
    fun getKeyError(): Int = logger.runInTrace {
      throw Exception("Oops!")
    }

    logger.info { "Key was ${getKey()}" }
    try {
      logger.info { "Key was ${getKeyError()}" }
    } catch(e: Exception) {
      logger.info { "Key threw ${e.message}" }
    }
  }
}
