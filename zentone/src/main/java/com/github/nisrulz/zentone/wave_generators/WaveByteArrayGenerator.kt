package com.github.nisrulz.zentone.wave_generators

import com.github.nisrulz.zentone.DEFAULT_AMPLITUDE
import com.github.nisrulz.zentone.DEFAULT_FREQUENCY_HZ
import com.github.nisrulz.zentone.DEFAULT_SAMPLE_RATE
import com.github.nisrulz.zentone.internal.minBufferSize

interface WaveByteArrayGenerator {
    fun generate(
        freqOfTone: Float = DEFAULT_FREQUENCY_HZ,
        sampleRate: Int = DEFAULT_SAMPLE_RATE
    ): ByteArray {
        val bufferSize = minBufferSize(sampleRate)
        val samplingInterval = sampleRate / freqOfTone

        val generatedSnd = ByteArray(bufferSize)

        generatedSnd.indices.forEach { i ->
            generatedSnd[i] = calculateData(i, samplingInterval, DEFAULT_AMPLITUDE)
        }

        return generatedSnd
    }

    fun calculateData(index: Int, samplingInterval: Float, amplitude: Int): Byte {
        val angle: Double = (Math.PI * index) / samplingInterval
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    fun waveFunction(angle: Double): Double
}