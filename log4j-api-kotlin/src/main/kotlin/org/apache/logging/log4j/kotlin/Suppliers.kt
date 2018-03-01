package org.apache.logging.log4j.kotlin

import org.apache.logging.log4j.util.Supplier

fun <T: Any?> (() -> T).asLog4jSupplier(): Supplier<T> = Supplier { invoke() }

fun <T: Any?> (Array<out () -> T>).asLog4jSuppliers(): Array<Supplier<T>> = map { it.asLog4jSupplier() }.toTypedArray()
