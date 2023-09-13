<!--
  ~ Licensed to the Apache Software Foundation (ASF) under one or more
  ~ contributor license agreements.  See the NOTICE file distributed with
  ~ this work for additional information regarding copyright ownership.
  ~ The ASF licenses this file to you under the Apache License, Version 2.0
  ~ (the "License"); you may not use this file except in compliance with
  ~ the License.  You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->

This project uses itself, i.e., `log4j-changelog-maven-plugin`, for keeping a changelog and generating release notes.

Changelog files are located under [`src/changelog`](src/changelog).
Release notes can be generated using `./mvnw -N -P changelog-export` command.
See [`log4j-changelog-maven-plugin`](log4j-changelog-maven-plugin) for details on how it all works.
