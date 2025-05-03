package com.github.nisrulz.zentone.wavegenerators

import kotlin.math.asin
import kotlin.math.sin

/**
 * Triangle wave generator
 *
 * Triangle Wave: Defined as the arc sine function of a sinusoid
 *
 * @see <a
 *     href="https://en.wikipedia.org/wiki/Triangle_wave">Wikipedia</a>
 */
object TriangleWaveGenerator : AngleBaseWaveGenerator(){

    override fun calculateData(angle: Double, amplitude: Int): Byte {
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    private fun waveFunction(angle: Double): Double = asin(sin(angle))
}
