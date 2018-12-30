# [Apache Log4j 2 Kotlin API](http://logging.apache.org/log4j/2.x/)

Log4j Kotlin API is a Kotlin logging facade based on Log4j 2. Log4j Kotlin API provides Log4j 2 as its
default logging implementation, but this is not strictly required (e.g., this API can also be used with Logback
or other Log4j 2 API provider implementations). Idiomatic Kotlin features are provided as an alternative to using
the Log4j 2 Java API.

[![Build Status](https://builds.apache.org/buildStatus/icon?job=Log4jKotlin)](https://builds.apache.org/job/Log4jKotlin)

## Usage

Gradle users can add the following dependencies to their `build.gradle` file:

TODO

```groovy
compile "org.apache.logging.log4j:log4j-api-kotlin:1.0.0"
compile "org.apache.logging.log4j:log4j-api:2.8.2"
compile "org.apache.logging.log4j:log4j-core:2.8.2"
```

## Documentation

[//]: # "The Log4j Kotlin API is documented [in the Log4j 2 manual](https://logging.apache.org/log4j/2.x/manual/kotlin-api.html)"
[//]: # "and in the [KDocs](https://logging.apache.org/log4j/2.x/log4j-api-kotlin/kdocs/index.html#org.apache.logging.log4j.kotlin.package)."

TODO

## Requirements

Log4j Kotlin API requires at least Java 7. This also requires Log4j 2 API, but it is specified as transitive
dependencies automatically if you are using SBT, Maven, Gradle, or some other similar build system. This also
requires Log4j 2 Core (or possibly an other implementation of Log4j 2 API) as a runtime dependency. Some
Log4j 2 Core features require optional dependencies which are documented in the 
[Log4j 2 manual](https://logging.apache.org/log4j/2.x/manual/index.html).

## License

Apache Log4j 2 is distributed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).

## Download

[How to download Log4j](http://logging.apache.org/log4j/2.x/download.html),
and [how to use it from SBT, Maven, Ivy and Gradle](http://logging.apache.org/log4j/2.x/maven-artifacts.html).

## Issue Tracking

Issues, bugs, and feature requests should be submitted to the 
[JIRA issue tracking system for this project](https://issues.apache.org/jira/browse/LOG4J2).

Pull request on GitHub are welcome, but please open a ticket in the JIRA issue tracker first, and mention the 
JIRA issue in the Pull Request.

## Building From Source

Log4j Kotlin API requires Maven 3 and Java 8 to build. To install to your local
Maven repository, execute the following:

```sh
mvn install
```

## Contributing

We love contributions! Take a look at [our contributing page](https://github.com/apache/logging-log4j-kotlin/blob/master/src/main/asciidoc/contributing.adoc).
