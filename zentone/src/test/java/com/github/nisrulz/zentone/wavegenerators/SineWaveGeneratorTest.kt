package com.github.nisrulz.zentone.wavegenerators

import android.media.AudioFormat
import com.github.nisrulz.zentone.DOUBLE_TOLERANCE
import com.github.nisrulz.zentone.UNIT_AMPLITUDE
import com.github.nisrulz.zentone.WAVE_TEST_FREQUENCY_HZ
import com.github.nisrulz.zentone.WAVE_TEST_SAMPLE_RATE
import com.github.nisrulz.zentone.toPcm16Samples
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.PI

internal class SineWaveGeneratorTest {

    @Test
    fun `nextAngle advances with a full cycle step`() {
        val nextAngle = createGenerator().nextAngle(
            freqOfTone = WAVE_TEST_FREQUENCY_HZ,
            sampleRate = WAVE_TEST_SAMPLE_RATE,
            frameCount = SINGLE_FRAME_COUNT,
            initialAngle = ZERO_ANGLE
        )

        assertEquals(expectedAngleStep(), nextAngle, DOUBLE_TOLERANCE)
    }

    @Test
    fun `generateFrameData emits little endian pcm16 samples`() {
        val audioData = generateSineFrames(
            encoding = AudioFormat.ENCODING_PCM_16BIT,
            frameCount = THREE_FRAMES
        )

        assertArrayEquals(EXPECTED_PCM16_MONO_FRAMES, audioData)
    }

    @Test
    fun `generateFrameData duplicates pcm16 samples across stereo channels`() {
        val audioData = generateSineFrames(
            encoding = AudioFormat.ENCODING_PCM_16BIT,
            frameCount = TWO_FRAMES,
            channelCount = STEREO_CHANNEL_COUNT
        )

        assertArrayEquals(EXPECTED_PCM16_STEREO_FRAMES, audioData)
    }

    @Test
    fun `generateFrameData emits unsigned pcm8 samples`() {
        val audioData = generateSineFrames(
            encoding = AudioFormat.ENCODING_PCM_8BIT,
            frameCount = FOUR_FRAMES
        )

        assertArrayEquals(EXPECTED_PCM8_MONO_FRAMES, audioData)
    }

    @Test
    fun `generateFrameData matches requested tone frequency`() {
        val audioData = createGenerator().generateFrameData(
            freqOfTone = WAVE_TEST_FREQUENCY_HZ,
            sampleRate = WAVE_TEST_SAMPLE_RATE,
            encoding = AudioFormat.ENCODING_PCM_16BIT,
            frameCount = WAVE_TEST_SAMPLE_RATE
        )

        val samples = audioData.toPcm16Samples()
        val measuredFrequency = measureFrequency(samples)

        assertEquals(WAVE_TEST_FREQUENCY_HZ.toDouble(), measuredFrequency, FREQUENCY_TOLERANCE)
    }

    private fun generateSineFrames(
        encoding: Int,
        frameCount: Int,
        channelCount: Int = 1
    ): ByteArray =
        createGenerator().generateFrameData(
            freqOfTone = WAVE_TEST_FREQUENCY_HZ,
            sampleRate = WAVE_TEST_SAMPLE_RATE,
            encoding = encoding,
            frameCount = frameCount,
            channelCount = channelCount
        )

    private fun createGenerator() = SineWaveGenerator()

    private fun expectedAngleStep(): Double =
        (2 * PI * WAVE_TEST_FREQUENCY_HZ) / WAVE_TEST_SAMPLE_RATE

    private fun measureFrequency(samples: ShortArray): Double {
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

        return WAVE_TEST_SAMPLE_RATE / averagePeriodInSamples
    }

    private companion object {
        const val ZERO_ANGLE = 0.0
        const val SINGLE_FRAME_COUNT = 1
        const val TWO_FRAMES = 2
        const val THREE_FRAMES = 3
        const val FOUR_FRAMES = 4
        const val STEREO_CHANNEL_COUNT = 2
        const val FREQUENCY_TOLERANCE = 0.5

        val EXPECTED_PCM16_MONO_FRAMES = byteArrayOf(0x00, 0x00, -0x5a, 0x03, 0x4a, 0x07)
        val EXPECTED_PCM16_STEREO_FRAMES =
            byteArrayOf(0x00, 0x00, 0x00, 0x00, -0x5a, 0x03, -0x5a, 0x03)
        val EXPECTED_PCM8_MONO_FRAMES = byteArrayOf(-0x80, -0x7d, -0x79, -0x76)
    }
}
