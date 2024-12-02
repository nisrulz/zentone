plugins {
    `kotlin-dsl`
}

group = "com.github.nisrulz.zentoneproject"

// https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
// Note: Setting a toolchain via the kotlin extension updates the toolchain for Java compile
// tasks as well.
kotlin {
    jvmToolchain(17)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

dependencies {
    // Android
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
}

// Register Convention Plugins
gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "zentoneproject.android.application"
            implementationClass = "$group.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "zentoneproject.android.library"
            implementationClass = "$group.AndroidLibraryConventionPlugin"
        }
    }
}
