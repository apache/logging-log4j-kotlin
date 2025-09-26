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
package org.apache.logging.log4j.kotlin.extension

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.cachedLoggerOf

/**
 * Provides a logger named after the receiver object's class.
 *
 * Simply import this property and use it.
 *
 * ```
 * import org.apache.logging.log4j.kotlin.extension.logger
 *
 * class MyClass {
 *   // use `logger` as necessary
 * }
 * ```
 *
 * Note that this is significantly slower than creating a logger explicitly, as it requires a lookup of the
 * logger on each call via the property getter. We attempt to minimize the overhead of this by caching the
 * loggers, but according to microbenchmarks, it is still about 3.5 times slower than creating a logger once
 * and using it (about 4.2 nanoseconds per call instead of 1.2 nanoseconds).
 *
 * @since 1.3.0
 */
inline val <reified T> T.logger: KotlinLogger
  get() = cachedLoggerOf(T::class.java)
