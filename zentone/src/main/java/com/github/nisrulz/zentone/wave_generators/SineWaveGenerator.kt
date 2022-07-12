package com.github.nisrulz.zentone.wave_generators

import kotlin.math.sin


/**
 * Sine wave generator
 *
 * Sine Wave: Defined as the sign function
 *
 * @see <a href="https://en.wikipedia.org/wiki/Sine_wave">Wikipedia</a>
 */
object SineWaveGenerator : WaveByteArrayGenerator {

    override fun calculateData(index: Int, samplingInterval: Float, amplitude: Int): Byte {
        val angle: Double = (Math.PI * index) / samplingInterval
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    private fun waveFunction(angle: Double): Double = sin(angle)
}