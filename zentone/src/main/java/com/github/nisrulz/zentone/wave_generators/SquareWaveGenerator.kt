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
object SquareWaveGenerator : AngleBaseWaveGenerator() {

    override fun calculateData(angle: Double, amplitude: Int): Byte {
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    private fun waveFunction(angle: Double): Double = sign(sin(angle))
}
