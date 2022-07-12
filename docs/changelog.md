# Changelog

## 2.2.0 [13 Jul 2022]

- 🧹  Improved versioned docs
- ➕  Added `TriangleWaveGenerator`

## 2.1.0 [14 Feb 2022]

- 🧹  Reworked API to allow providing custom Wave Generator
- ➕  Added `SquareWaveGenerator`

## 2.0.0 [14 Feb 2022]

- ✅  Switched to Kotlin
- 👀  Uses coroutines
- 🧹  Reworked API

- ⚠️  `ZenTone`is no more a singleton thus allowing running multiple instances of it.
- ⚠️  `minSdk` = 18
- ❌  Time duration cannot be set anymore. This is now dependent on when one calls `stop` on `ZenTone` instance.

## 1.0.3 [01 Aug 2016]

- ✅  Updated code to handle volume
- 🧹  Replace maven-push.gradle file with the one from github repo
- 📃  Added Java Docs
- 🧰  Code cleanup and updated dependencies
  