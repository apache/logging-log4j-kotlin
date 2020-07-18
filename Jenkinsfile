#!groovy
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

pipeline {
    agent {
        label 'ubuntu'
    }
    tools {
        maven 'Maven 3 (latest)'
        jdk 'JDK 1.8 (latest)'
    }
    options {
        ansiColor 'xterm'
        buildDiscarder logRotator(numToKeepStr: '25')
        timeout time: 1, unit: 'HOURS'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn install'
            }
        }
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                sh 'mvn deploy'
            }
            post {
                fixed {
                    emailext to: 'notifications@logging.apache.org',
                        from: 'Mr. Jenkins <jenkins@ci-builds.apache.org>',
                        subject: "[CI][SUCCESS] ${env.JOB_NAME}#${env.BUILD_NUMBER} back to normal",
                        body: '${SCRIPT, template="groovy-text.template"}'
                }
                failure {
                    emailext to: 'notifications@logging.apache.org',
                        from: 'Mr. Jenkins <jenkins@ci-builds.apache.org>',
                        subject: "[CI][FAILURE] ${env.JOB_NAME}#${env.BUILD_NUMBER} has potential issues",
                        body: '${SCRIPT, template="groovy-text.template"}'
                }
            }
        }
    }
}
