package com.github.nisrulz.zentone.wavegenerators

import com.github.nisrulz.zentone.DEFAULT_AMPLITUDE
import com.github.nisrulz.zentone.DEFAULT_FREQUENCY_HZ
import com.github.nisrulz.zentone.DEFAULT_SAMPLE_RATE
import com.github.nisrulz.zentone.internal.minBufferSize
import kotlin.math.PI

interface WaveByteArrayGenerator {

    var angle: Double
    var angleStep: Double

    /**
     * Generate byte data for tone
     *
     * @param freqOfTone
     * @param sampleRate
     * @return ByteArray of generated tone
     */
    fun generate(
        freqOfTone: Float = DEFAULT_FREQUENCY_HZ,
        sampleRate: Int = DEFAULT_SAMPLE_RATE
    ): ByteArray {
        setup(freqOfTone, sampleRate)
        val bufferSize = minBufferSize(sampleRate)

        val generatedSnd = ByteArray(bufferSize)

        generatedSnd.indices.forEach { i ->
            generatedSnd[i] = calculateData(DEFAULT_AMPLITUDE)
        }

        return generatedSnd
    }

    fun calculateData(angle: Double, amplitude: Int): Byte


    /**
     * Setup required before generating a frame. Must be called at least once before calculateData.
     */
    fun setup(freqOfTone: Float, sampleRate: Int) {
        val samplingInterval = sampleRate / freqOfTone
        angleStep = PI / samplingInterval
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

    private fun calculateData(amplitude: Int): Byte {
        angle = incrementAngle(angle, angleStep)
        return calculateData(angle, amplitude)
    }

}