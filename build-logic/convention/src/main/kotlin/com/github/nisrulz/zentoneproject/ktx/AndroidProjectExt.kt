package com.github.nisrulz.zentoneproject.ktx

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.github.nisrulz.zentoneproject.info.ApplicationInfo
import com.github.nisrulz.zentoneproject.info.BuildSdkInfo
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

/**
 * Set JVM toolchain
 */
private fun Project.configureKotlin(commonExtension: CommonExtension<*, *, *, *, *, *>) =
    commonExtension.apply {
        // https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
        // Note: Setting a toolchain via the kotlin extension updates the toolchain for Java compile
        // tasks as well.
        kotlinExtension.jvmToolchain(BuildSdkInfo.JVM_TARGET)
    }

/**
 * Common configuration for Android modules
 */
private fun Project.configureAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) =
    commonExtension.apply {
        compileSdk = BuildSdkInfo.COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = BuildSdkInfo.MIN_SDK_VERSION

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        configureKotlin(this)
    }

/**
 *  Configuration for Android Application
 */
internal fun Project.configureAndroidApp() =
    configure<ApplicationExtension> {
        configureAndroid(this)

        namespace = ApplicationInfo.BASE_NAMESPACE

        defaultConfig {
            targetSdk = BuildSdkInfo.TARGET_SDK_VERSION

            versionCode = ApplicationInfo.VERSION_CODE
            versionName = ApplicationInfo.VERSION_NAME

            vectorDrawables.useSupportLibrary = true
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        buildFeatures {
            buildConfig = true
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
            }
        }
    }

internal fun Project.configureAndroidLibrary() =
    configure<LibraryExtension> {
        configureAndroid(this)

        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
            }
        }
    }
