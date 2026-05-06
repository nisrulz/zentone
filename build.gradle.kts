/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false

    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.maven.publish) apply false

    alias(libs.plugins.dokka) apply false

    alias(libs.plugins.compose.compiler) apply false
}

abstract class GradleExecTask
    @javax.inject.Inject
    constructor(
        private val execOps: ExecOperations,
    ) : DefaultTask() {
    lateinit var moduleName: String
    lateinit var gradleTask: String

    @TaskAction
    fun run() {
        execOps.exec {
            commandLine(
                "./gradlew",
                ":$moduleName:$gradleTask",
                "--no-configuration-cache",
            )
        }
    }
}

//region Publishing Tasks
tasks.register<GradleExecTask>("releaseToMavenLocal") {
    moduleName = "zentone"
    gradleTask = "publishToMavenLocal"
}

tasks.register<GradleExecTask>("releaseToMavenCentral") {
    moduleName = "zentone"
    gradleTask = "publishToMavenCentral"
}
//endregion

//region Docs
tasks.register<GradleExecTask>("assembleDocs") {
    moduleName = "zentone"
    gradleTask = "dokkaHtml"
}
//endregion
