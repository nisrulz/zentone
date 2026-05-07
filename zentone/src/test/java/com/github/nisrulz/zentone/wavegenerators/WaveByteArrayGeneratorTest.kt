package com.github.nisrulz.zentone.wavegenerators

import android.media.AudioFormat
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.PI

class WaveByteArrayGeneratorTest {

    @Test
    fun `nextAngle advances with a full cycle step`() {
        val generator = sineGenerator()

        val nextAngle = generator.nextAngle(
            freqOfTone = 200f,
            sampleRate = 44100,
            frameCount = 1,
            initialAngle = 0.0
        )

        assertEquals((2 * PI * 200.0) / 44100.0, nextAngle, 1e-12)
    }

    @Test
    fun `generateFrameData emits little endian pcm16 samples`() {
        val audioData = generateSineFrames(
            encoding = AudioFormat.ENCODING_PCM_16BIT,
            frameCount = 3
        )

        assertArrayEquals(
            byteArrayOf(0x00, 0x00, -0x5a, 0x03, 0x4a, 0x07),
            audioData
        )
    }

    @Test
    fun `generateFrameData duplicates pcm16 samples across stereo channels`() {
        val audioData = generateSineFrames(
            encoding = AudioFormat.ENCODING_PCM_16BIT,
            frameCount = 2,
            channelCount = 2
        )

        assertArrayEquals(
            byteArrayOf(0x00, 0x00, 0x00, 0x00, -0x5a, 0x03, -0x5a, 0x03),
            audioData
        )
    }

    @Test
    fun `generateFrameData emits unsigned pcm8 samples`() {
        val audioData = generateSineFrames(encoding = AudioFormat.ENCODING_PCM_8BIT, frameCount = 4)

        assertArrayEquals(
            byteArrayOf(-0x80, -0x7d, -0x79, -0x76),
            audioData
        )
    }

    @Test
    fun `generateFrameData matches requested tone frequency`() {
        val sampleRate = 44100
        val targetFrequency = 200f
        val audioData = sineGenerator().generateFrameData(
            freqOfTone = targetFrequency,
            sampleRate = sampleRate,
            encoding = AudioFormat.ENCODING_PCM_16BIT,
            frameCount = sampleRate
        )

        val samples = audioData.toPcm16Samples()

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

    private fun sineGenerator() = SineWaveGenerator()

    private fun generateSineFrames(
        encoding: Int,
        frameCount: Int,
        channelCount: Int = 1
    ): ByteArray =
        sineGenerator().generateFrameData(
            freqOfTone = 200f,
            sampleRate = 44100,
            encoding = encoding,
            frameCount = frameCount,
            channelCount = channelCount
        )

    private fun ByteArray.toPcm16Samples(): ShortArray =
        ShortArray(size / 2) { index ->
            val byteIndex = index * 2
            ((this[byteIndex].toInt() and 0xFF) or
                (this[byteIndex + 1].toInt() shl 8)).toShort()
        }
}
