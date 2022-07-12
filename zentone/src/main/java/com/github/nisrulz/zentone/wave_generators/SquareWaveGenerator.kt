package com.github.nisrulz.zentone.wave_generators

import kotlin.math.sign
import kotlin.math.sin

/**
 * Square wave generator
 *
 * Square Wave: Defined as the sign function of a sinusoid
 *
 * @see <a
 *     href="https://en.wikipedia.org/wiki/Square_wave">Wikipedia</a>
 */
object SquareWaveGenerator : WaveByteArrayGenerator {

    override fun calculateData(index: Int, samplingInterval: Float, amplitude: Int): Byte {
        val angle: Double = (Math.PI * index) / samplingInterval
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    private fun waveFunction(angle: Double): Double = sign(sin(angle))
}