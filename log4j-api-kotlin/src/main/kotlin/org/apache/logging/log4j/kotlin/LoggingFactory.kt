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

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.spi.ExtendedLogger
import kotlin.reflect.full.companionObject

/**
 * Logger instantiation by function. Use: `val log = logger()`.
 */
@Suppress("unused")
inline fun <reified T : Any> T.logger() = loggerOf(T::class.java)

/**
 * Logger instantiation by function. Use: `val log = logger()`.
 */
@Suppress("unused")
inline fun <reified T : Any> T.completeLogger() = completeLoggerOf(T::class.java)

fun loggerDelegateOf(ofClass: Class<*>): ExtendedLogger {
  return LogManager.getContext(ofClass.classLoader, false).getLogger(unwrapCompanionClass(ofClass).name)
}

fun loggerOf(ofClass: Class<*>): KotlinLogger {
  return KotlinLogger(loggerDelegateOf(ofClass))
}

fun completeLoggerOf(ofClass: Class<*>): KotlinCompleteLogger {
  return KotlinCompleteLogger(loggerDelegateOf(ofClass))
}

// unwrap companion class to enclosing class given a Java Class
private fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
  return if (ofClass.enclosingClass?.kotlin?.companionObject?.java == ofClass) {
    ofClass.enclosingClass
  } else {
    ofClass
  }
}
