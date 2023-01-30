# [Apache Log4j 2 (Kotlin API)](http://logging.apache.org/log4j/2.x/)

Log4j Kotlin API is a Kotlin logging facade based on Apache Log4j API. Log4j Kotlin API provides Log4j Core 2.x as its
default logging implementation, but this is not strictly required (e.g., this API can also be used with Logback
or other Log4j 2 API provider implementations). Idiomatic Kotlin features are provided as an alternative to using
the Log4j Java API.

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
  fun doStuff() {
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

The user guide for Log4j Kotlin API is [available here](https://logging.apache.org/log4j/kotlin/).

## Requirements

The minimum requirements for Log4j Kotlin API are Java 8 and Kotlin 1.3.x. Log4j API is also required, though
this is already specified as a transitive dependency for `log4j-api-kotlin` which is supported by common build
systems like Maven, Gradle, SBT, and Ivy. A logging backend library such as Log4j Core, Logback, or `java.util.logging`
is required at runtime for an application to configure the output of logging. Log4j Core version 2.x includes
various plugins and configuration options which may require additional dependencies. See the
[Log4j manual](https://logging.apache.org/log4j/2.x/manual/) for more details.

This library requires a dependency on `kotlin-reflect` in order to determine appropriate logger names from
classes. When `kotlinx-coroutines-core` is available, this library provides a `CoroutineThreadContext` for
supporting the `ThreadContext` API (and MDC/NDC APIs) in coroutines.

This library declares a `provided` scope dependency on Kotlin 1.3. This is to ensure that consumers of this library
specify the proper Kotlin dependencies corresponding to the version of the Kotlin language in use and avoiding
dependency conflicts.

## License

Apache Log4j Kotlin API is distributed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

## Download

[How to download Log4j Kotlin API](http://logging.apache.org/log4j/kotlin/download.html),
and [how to use it from Maven, Ivy and Gradle](http://logging.apache.org/log4j/kotlin/artifacts.html).
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
