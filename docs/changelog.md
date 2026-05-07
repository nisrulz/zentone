# Changelog

## 2.4.0 [07 May 2026]

- 🔊 Fixed tone waveform math, PCM sample generation, sample-rate handling, PCM encoding, and channel layout
- 🔒 Improved playback stability by preventing overlapping playback work and guarding invalid or uninitialized audio track state
- 🎛 Added safer audio configuration with sample rate options, advanced encoding/channel overrides, and configurable playback count
- 🌊 Expanded and simplified wave generator APIs, including new `SawtoothWaveGenerator()` and `PulseWaveGenerator()`, stateless playback behavior, and removal of `AngleBaseWaveGenerator`
- 🧪 Modernized test coverage and introduced an `AudioSink` seam to improve playback testability
- 📃 Updated README, usage docs, and sample app to reflect the new API and supported audio formats
- 🧰 Internal cleanup, API refinements, and general dependency modernization across the project
- 🔗 Full changelog: https://github.com/nisrulz/zentone/compare/2.3.0...2.4.0

## 2.3.0 [15 May 2025]

- 🐞 Removed tick sound when playing.
- 🧹 Updated the sample app
- 🧰 Code cleanup, improvements and updated dependencies

## 2.2.0 [13 Jul 2022]

- 🧹 Improved versioned docs
- ➕ Added `TriangleWaveGenerator`

## 2.1.0 [14 Feb 2022]

- 🧹 Reworked API to allow providing custom Wave Generator
- ➕ Added `SquareWaveGenerator`

## 2.0.0 [14 Feb 2022]

- ✅ Switched to Kotlin
- 👀 Uses coroutines
- 🧹 Reworked API

- ⚠️ `ZenTone`is no more a singleton thus allowing running multiple instances of it.
- ⚠️ `minSdk` = 18
- ❌ Time duration cannot be set anymore. This is now dependent on when one calls `stop` on `ZenTone` instance.

## 1.0.3 [01 Aug 2016]

- ✅ Updated code to handle volume
- 🧹 Replace maven-push.gradle file with the one from github repo
- 📃 Added Java Docs
- 🧰 Code cleanup and updated dependencies
