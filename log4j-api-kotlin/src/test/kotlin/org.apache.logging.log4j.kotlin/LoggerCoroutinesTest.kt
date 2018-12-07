package org.apache.logging.log4j.kotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.junit.LoggerContextRule
import org.junit.Rule
import org.junit.Test

class LoggerCoroutinesTest {
  @Rule @JvmField var init = LoggerContextRule("InfoLogger.xml")

  @Test
  fun `Can use suppliers with suspend functions`() = runBlocking {
    logger().error {
      // expensive operation with suspend, because the lambda is inlined we can call this
      delay(5)
    }
  }
}
