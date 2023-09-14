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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.apache.logging.log4j.util.Supplier

fun <T: Any?> (() -> T).asLog4jSupplier(): Supplier<T> = Supplier { invoke() }

@SuppressFBWarnings("BC_BAD_CAST_TO_ABSTRACT_COLLECTION")
fun <T: Any?> (Array<out () -> T>).asLog4jSuppliers(): Array<Supplier<T>> = map { it.asLog4jSupplier() }.toTypedArray()
