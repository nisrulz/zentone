# Usage

## Create an instance of `ZenTone`:

```kt
val zenTone = ZenTone()
```

`ZenTone` accepts 2 commonly-used arguments, each having a sensible default:

1. `sampleRate`: Int = 44100
2. `channelMask`: Int = AudioFormat.CHANNEL_OUT_MONO

based on your requirement, you can pass a different value when instantiating `ZenTone` i.e

```kt
val zenTone = ZenTone(channelMask = AudioFormat.CHANNEL_OUT_STEREO)
```

ZenTone uses `AudioFormat.ENCODING_PCM_16BIT` by default.

If you need to override the encoding, use the advanced factory:

```kt
val zenTone = ZenTone.advanced(
    sampleRate = 44100,
    encoding = AudioFormat.ENCODING_PCM_8BIT,
    channelMask = AudioFormat.CHANNEL_OUT_MONO
)
```

Currently, ZenTone supports `AudioFormat.ENCODING_PCM_8BIT` and `AudioFormat.ENCODING_PCM_16BIT` output, with mono or stereo channel masks.

## Start playing audio with a frequency and volume:

```kt
zenTone.play(frequency = 400f, volume = 2)
```

`play()` accepts 3 arguments:

1. `frequency`: Float
2. `volume`: Int. It ranges from 0 to 100, where 0 is no audio and 100 is full volume.
3. `waveByteArrayGenerator`: WaveByteArrayGenerator = SineWaveGenerator, here `SineWaveGenerator` is a sensible default.
   - POssible options are `SineWaveGenerator`, `SquareWaveGenerator` and `TriangleWaveGenerator`

based on your requirement, you can pass a different value when calling `play()` i.e

```kt
zenTone.play(frequency = 440f,
            volume = 10,
            waveByteArrayGenerator = SquareWaveGenerator)
```

## Stop playing audio:

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

## Check if `ZenTone` is playing audio by querying the `isPlaying` flag:

```kt
val isPlaying = zenTone.isPlaying
```

## Toggle playback

```kt
zenTone.togglePlayback(frequency = 440f, volume = 10)
```

`togglePlayback()` accepts 3 arguments:

1. `frequency`: Float
2. `volume`: Int. It ranges from 0 to 100, where 0 is no audio and 100 is full volume.
