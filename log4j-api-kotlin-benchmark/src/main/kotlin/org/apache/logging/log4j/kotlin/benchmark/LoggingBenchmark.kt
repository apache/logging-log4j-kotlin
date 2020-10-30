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
package org.apache.logging.log4j.kotlin.benchmark

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.kotlin.contextName
import org.apache.logging.log4j.kotlin.logger
//import org.apache.logging.log4j.kotlin.logger1
import org.apache.logging.log4j.util.Supplier
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

val LOGGER1 = logger("Bar")
val LOGGER2 = logger(contextName {})

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"])
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 5, time = 1)
open class LoggingBenchmark {
  companion object {
    @JvmStatic
    val LOGGER3 = logger()
    val LOGGER4: Logger = LogManager.getLogger()
  }

  @Benchmark
  fun topLevelNamedLoggerFunctional() {
    LOGGER1.info { "Test" }
  }

  @Benchmark
  fun topLevelNamedLoggerDirect() {
    LOGGER1.info("Test")
  }

  @Benchmark
  fun topLevelLoggerWithContextLookupFunctional() {
    LOGGER2.info {"Test" }
  }

  @Benchmark
  fun topLevelLoggerWithContextLookupDirect() {
    LOGGER2.info("Test")
  }

  @Benchmark
  fun companionObjectKotlinLoggerFunctional() {
    LOGGER3.info { "Test" }
  }

  @Benchmark
  fun companionObjectKotlinLoggerDirect() {
    LOGGER3.info("Test")
  }

  @Benchmark
  fun companionObjectLog4jLoggerFunctional() {
    LOGGER4.info(Supplier { "Test" })
  }

  @Benchmark
  fun companionObjectLog4jLoggerDirect() {
    LOGGER4.info("Test")
  }
}
