package com.github.nisrulz.zentoneproject.ktx

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

private val Project.catalog
    get() = extensions.getByType<VersionCatalogsExtension>()

private val Project.libs: VersionCatalog
    get() = catalog.named("libs")

internal fun Project.catalogLibrary(alias: String): Provider<MinimalExternalModuleDependency> =
    libs.findLibrary(alias).get()

internal fun Project.catalogBundle(alias: String): Provider<ExternalModuleDependencyBundle> =
    libs.findBundle(alias).get()
