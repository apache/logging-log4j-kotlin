# [Apache Log4j 2 (Kotlin API)](http://logging.apache.org/log4j/2.x/)

Log4j Kotlin API is a Kotlin logging facade based on Log4j 2. Log4j Kotlin API provides Log4j 2 as its
default logging implementation, but this is not strictly required (e.g., this API can also be used with Logback
or other Log4j 2 API provider implementations). Idiomatic Kotlin features are provided as an alternative to using
the Log4j 2 Java API.

[![Maven Central](https://img.shields.io/maven-central/v/org.apache.logging.log4j/log4j-api-kotlin.svg)](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-api-kotlin)
[![Build Status](https://ci-builds.apache.org/job/Logging/job/log4j-kotlin/job/master/lastBuild/badge/icon)](https://builds.apache.org/job/Logging/job/log4j-kotlin/job/master/)

## Pull Requests on Github

By sending a pull request you grant the Apache Software Foundation sufficient rights to use and release the submitted
work under the Apache license. You grant the same rights (copyright license, patent license, etc.) to the
Apache Software Foundation as if you have signed a Contributor License Agreement. For contributions that are
judged to be non-trivial, you will be asked to actually signing a Contributor License Agreement.

## Usage

Users should refer to [Maven, Ivy and Gradle Artifacts](https://logging.apache.org/log4j/kotlin/artifacts.html)
on the Log4j website for instructions on how to include Log4j into their project using their chosen build tool.

Using the Kotlin API is as simple as mixing in the Logging interface to your class. Example:

```kotlin
import org.apache.logging.log4j.kotlin.Logging

class MyClass: BaseClass, Logging {
    fun doStuff() {
        logger.info("Doing stuff")
    }
    
    fun doStuffWithUser(user: User) {
        logger.info { "Doing stuff with ${user.name}." }
    }
}
```

The Logging interface can also be mixed into object declarations, including companions. This is generally preferable over the previous approach as there is a single logger created for every instance of the class.

```kotlin
import org.apache.logging.log4j.kotlin.Logging

class MyClass: BaseClass {
    companion object : Logging
    
    // ...
}
```

Alternatively, a more traditional style can be used to instantiate a logger instance:

```kotlin
import org.apache.logging.log4j.kotlin

class MyClass: BaseClass {
    val logger = logger()
    
    // ...
}
```

The function `logger()` is an extension function on the Any type (or more specifically, any type `T` that extends `Any`).

<!--
TODO: uncomment when 1.3.0 is released:
Beginning in version 1.3.0, an extension property is also available on classes:

```kotlin
import org.apache.logging.log4j.kotlin.logger

class MyClass: BaseClass {
  init {
    logger.info("Hello, world!")
  }
}
```

Also added in version 1.3.0, the `ThreadContext` API has two facade objects provided: `ContextMap` and `ContextStack`.

```kotlin
import org.apache.logging.log4j.kotlin.ContextMap
import org.apache.logging.log4j.kotlin.ContextStack

ContextMap["key"] = "value"
assert(ContextMap["key"] == "value")
assert("key" in ContextMap)

ContextMap += "anotherKey" to "anotherValue"
ContextMap -= "key"

ContextStack.push("message")
assert(!ContextStack.empty)
assert(ContextStack.depth == 1)
val message = ContextStack.peek()
assert(message == ContextStack.pop())
assert(ContextStack.empty)
```
-->

## Documentation

The Kotlin's Log4j 2 User's Guide is available [here](https://logging.apache.org/log4j/kotlin/index.html).

## Requirements

Log4j Kotlin API requires at least Java 8. This also requires Log4j 2 API, but it is specified as transitive
dependencies automatically if you are using SBT, Maven, Gradle, or some other similar build system. This also
requires Log4j 2 Core (or possibly an other implementation of Log4j 2 API) as a runtime dependency. Some
Log4j 2 Core features require optional dependencies which are documented in the 
[Log4j 2 manual](https://logging.apache.org/log4j/2.x/manual/index.html).

The Kotlin API requires the full `kotlin-reflect` dependency in order to name loggers appropriately, and
optionally `kotlinx-coroutines-core` to set the mapped diagnostic context for a coroutine.

The Kotlin dependencies are not exposed transitively -- for maximum compatibility logging-log4j-kotlin is built
with Kotlin 1.3, producing binaries that should be forward compatible. For maximum compat, the Kotlin dependencies
are "provided" i.e. consumers of this library need to depend on them directly rather than transitively, thus
avoiding version clashes.

## License

Apache Log4j 2 is distributed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

## Download

[How to download Log4j](http://logging.apache.org/log4j/2.x/download.html),
and [how to use it from Maven, Ivy and Gradle](http://logging.apache.org/log4j/2.x/maven-artifacts.html).
You can access the latest development snapshot by using the Maven repository `https://repository.apache.org/snapshots`,
see [Snapshot builds](https://logging.apache.org/log4j/2.x/maven-artifacts.html#Snapshot_builds).

## Issue Tracking

Issues, bugs, and feature requests should be submitted to the
[GitHub issues page for this project](https://github.com/apache/logging-log4j-kotlin/issues).

Pull request on GitHub are welcome; corresponding GitHub issues should be referenced in the PR.

## Building From Source

Log4j Kotlin API requires Maven 3 and Java 8 to build. To install to your local
Maven repository, execute the following:

```sh
mvn install
```

## Contributing

We love contributions!
Take a look at [our contributing page](CONTRIBUTING.md).
