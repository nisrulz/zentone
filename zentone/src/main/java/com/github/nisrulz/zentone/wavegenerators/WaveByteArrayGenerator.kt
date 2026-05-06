package com.github.nisrulz.zentone.wavegenerators

import com.github.nisrulz.zentone.DEFAULT_AMPLITUDE
import com.github.nisrulz.zentone.DEFAULT_FREQUENCY_HZ
import com.github.nisrulz.zentone.DEFAULT_SAMPLE_RATE
import com.github.nisrulz.zentone.BYTES_PER_PCM_16_SAMPLE
import com.github.nisrulz.zentone.internal.minBufferSize
import kotlin.math.PI

interface WaveByteArrayGenerator {

    var angle: Double
    var angleStep: Double

    /**
     * Generate byte data for tone
     *
     * @param freqOfTone Frequency of the tone you want to generate, in Hz.
     * @param sampleRate Number of samples per second, in Hz
     * @return ByteArray of generated tone
     */
    fun generate(
        freqOfTone: Float = DEFAULT_FREQUENCY_HZ,
        sampleRate: Int = DEFAULT_SAMPLE_RATE
    ): ByteArray {
        val bufferSize = minBufferSize(sampleRate)
        return generateFrameData(
            freqOfTone = freqOfTone,
            sampleRate = sampleRate,
            frameCount = bufferSize / BYTES_PER_PCM_16_SAMPLE
        )
    }

    fun generateFrameData(
        freqOfTone: Float = DEFAULT_FREQUENCY_HZ,
        sampleRate: Int = DEFAULT_SAMPLE_RATE,
        frameCount: Int
    ): ByteArray {
        setup(freqOfTone, sampleRate)
        val generatedSnd = ByteArray(frameCount * BYTES_PER_PCM_16_SAMPLE)

        repeat(frameCount) { frameIndex ->
            val sample = calculateData(DEFAULT_AMPLITUDE)
            val byteIndex = frameIndex * BYTES_PER_PCM_16_SAMPLE
            generatedSnd[byteIndex] = sample.toInt().toByte()
            generatedSnd[byteIndex + 1] = (sample.toInt() shr Byte.SIZE_BITS).toByte()
        }

        return generatedSnd
    }

    fun calculateData(angle: Double, amplitude: Int): Short


    /**
     * Setup required before generating a frame.
     * Must be called at least once before calculateData.
     */
    fun setup(freqOfTone: Float, sampleRate: Int) {
        angleStep = (2 * PI * freqOfTone) / sampleRate
    }

    /**
     * Reset the generator to be able to start over from the start again.
     */
    fun reset() {
        angle = 0.0
    }

    private fun incrementAngle(
        angle: Double,
        angleStep: Double
    ): Double {
        return (angle + angleStep) % (2 * Math.PI)
    }

    private fun calculateData(amplitude: Int): Short {
        val sample = calculateData(angle, amplitude)
        angle = incrementAngle(angle, angleStep)
        return sample
    }

}
