# 📦 Integration

<details>
<summary>Version catalog</summary>

Inside `libs.versions.toml` file:

```toml
[versions]
zentone = "$version"

[dependencies]
zentone = { group = "com.github.nisrulz", name = "zentone", version.ref = "zentone" }
```

Then inside your `build.gradle.kts` file:

```kt
dependencies {
  implementation(libs.zentone)
}
```

</details>

<details>
<summary>Gradle DSL</summary>

```kotlin
dependencies {
    implementation("com.github.nisrulz:zentone:$version")
}
```

</details>

where `$version` corresponds to latest version published in [![Maven Central](https://img.shields.io/maven-central/v/com.github.nisrulz/zentone)](https://search.maven.org/artifact/com.github.nisrulz/zentone)
