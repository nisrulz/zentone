package com.github.nisrulz.zentone.wave_generators

import com.github.nisrulz.zentone.DEFAULT_AMPLITUDE
import com.github.nisrulz.zentone.DEFAULT_FREQUENCY_HZ
import com.github.nisrulz.zentone.DEFAULT_SAMPLE_RATE
import com.github.nisrulz.zentone.internal.minBufferSize

interface WaveByteArrayGenerator {

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
            generatedSnd[i] = calculateData(i, DEFAULT_AMPLITUDE)
        }

        return generatedSnd
    }

    fun calculateData(index: Int, amplitude: Int): Byte
    fun reset()
    fun setup(freqOfTone: Float, sampleRate: Int)
}