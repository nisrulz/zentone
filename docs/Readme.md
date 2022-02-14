![Banner](https://github.com/nisrulz/zentone/raw/master/img/github_banner.png)

[![Maven Central](https://img.shields.io/maven-central/v/com.github.nisrulz/zentone)](https://search.maven.org/artifact/com.github.nisrulz/zentone)  [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Zentone-green.svg?style=true)](https://android-arsenal.com/details/1/3470) [![GitHub stars](https://img.shields.io/github/stars/nisrulz/zentone.svg?style=social&label=Star)](https://github.com/nisrulz/zentone) [![GitHub forks](https://img.shields.io/github/forks/nisrulz/zentone.svg?style=social&label=Fork)](https://github.com/nisrulz/zentone/fork) [![GitHub watchers](https://img.shields.io/github/watchers/nisrulz/zentone.svg?style=social&label=Watch)](https://github.com/nisrulz/zentone)

## Including in your project

ZenTone is available in the MavenCentral, so getting it as simple as adding it as a dependency
inside your build.gradle file

```gradle
implementation "com.github.nisrulz:zentone:$version"
```

where `$version` corresponds to latest version published in [![Maven Central](https://img.shields.io/maven-central/v/com.github.nisrulz/zentone)](https://search.maven.org/artifact/com.github.nisrulz/zentone)

## Usage

Create an instance of `ZenTone`:

```kt
val zenTone = ZenTone()
```

`ZenTone` accepts 3 arguments, each having a sensible default:

1. sampleRate: Int = 44100,
2. encoding: Int = AudioFormat.ENCODING_PCM_16BIT,
3. channelMask: Int = AudioFormat.CHANNEL_OUT_MONO

based on your requirement, you can pass a different value when instantiating `ZenTone` i.e

```kt
val zenTone = ZenTone(channelMask = AudioFormat.CHANNEL_OUT_STEREO)
```

To start playing audio with a frequency and volume:

```kt
zenTone.play(frequency = 400f, volume = 2)
```

here

- `400f` is frequency of type `Float`
- `2` is volume of type `Int`. It ranges from 0 to 100, where 0 is no audio and 100 is full volume.

To stop playing audio:

```kt
zenTone.stop()
```

To release resources held by `ZenTone` i.e release mic, you can call `release()` function.

Usually you'll need to call this in `onDestroy()`:

```kt
override fun onDestroy() {
    super.onDestroy()
    zenTone.release()
}
```

Check if `ZenTone` is playing audio by querying the `isPlaying` flag:

```kt
val isPlaying = zenTone.isPlaying
```
