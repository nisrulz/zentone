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
 * Configure Kotlin toolchain for all Android modules.
 */
private fun Project.configureKotlin(ext: CommonExtension) =
    ext.apply {
        kotlinExtension.jvmToolchain(BuildSdkInfo.JVM_TARGET)
    }

/**
 * Shared Android configuration for both Application and Library modules.
 */
private fun Project.configureAndroid(ext: CommonExtension) {
    when (ext) {
        is ApplicationExtension,
        is LibraryExtension,
        -> {
            ext.apply {
                compileSdk = BuildSdkInfo.COMPILE_SDK_VERSION

                defaultConfig.minSdk = BuildSdkInfo.MIN_SDK_VERSION
                defaultConfig.testInstrumentationRunner =
                    "androidx.test.runner.AndroidJUnitRunner"
            }
        }
    }

    configureKotlin(ext)
}

/**
 * Android Application configuration.
 */
internal fun Project.configureAndroidApp() =
    configure<ApplicationExtension> {
        configureAndroid(this)

        namespace = ApplicationInfo.BASE_NAMESPACE

        defaultConfig.apply {
            targetSdk = BuildSdkInfo.TARGET_SDK_VERSION

            versionCode = ApplicationInfo.VERSION_CODE
            versionName = ApplicationInfo.VERSION_NAME

            vectorDrawables.useSupportLibrary = true
        }

        packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

        buildFeatures.buildConfig = true

        buildTypes.getByName("release").apply {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

internal fun Project.configureAndroidLibrary() =
    configure<LibraryExtension> {
        configureAndroid(this)

        defaultConfig.consumerProguardFiles("consumer-proguard-rules.pro")

        buildTypes.getByName("release").apply {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
