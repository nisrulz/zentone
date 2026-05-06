package com.github.nisrulz.zentone.wavegenerators

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.PI

class WaveByteArrayGeneratorTest {

    @Test
    fun `setup uses full cycle angle step`() {
        SineWaveGenerator.reset()

        SineWaveGenerator.setup(freqOfTone = 200f, sampleRate = 44100)

        assertEquals((2 * PI * 200.0) / 44100.0, SineWaveGenerator.angleStep, 1e-12)
    }

    @Test
    fun `generateFrameData emits little endian pcm16 samples`() {
        SineWaveGenerator.reset()

        val audioData = SineWaveGenerator.generateFrameData(
            freqOfTone = 200f,
            sampleRate = 44100,
            frameCount = 3
        )

        assertArrayEquals(
            byteArrayOf(0x00, 0x00, -0x5b, 0x03, 0x4a, 0x07),
            audioData
        )
    }

    @Test
    fun `generateFrameData matches requested tone frequency`() {
        SineWaveGenerator.reset()

        val sampleRate = 44100
        val targetFrequency = 200f
        val audioData = SineWaveGenerator.generateFrameData(
            freqOfTone = targetFrequency,
            sampleRate = sampleRate,
            frameCount = sampleRate
        )

        val samples = ShortArray(audioData.size / 2) { index ->
            val byteIndex = index * 2
            ((audioData[byteIndex].toInt() and 0xFF) or
                (audioData[byteIndex + 1].toInt() shl 8)).toShort()
        }

        val risingZeroCrossings = mutableListOf<Int>()
        for (index in 1 until samples.size) {
            if (samples[index - 1] <= 0 && samples[index] > 0) {
                risingZeroCrossings += index
            }
        }

        val averagePeriodInSamples =
            risingZeroCrossings
                .zipWithNext()
                .map { (start, end) -> end - start }
                .average()

        val measuredFrequency = sampleRate / averagePeriodInSamples

        assertEquals(targetFrequency.toDouble(), measuredFrequency, 0.5)
    }
}
