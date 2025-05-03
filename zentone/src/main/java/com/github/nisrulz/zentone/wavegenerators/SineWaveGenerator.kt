package com.github.nisrulz.zentone.wavegenerators

import kotlin.math.sin


/**
 * Sine wave generator
 *
 * Sine Wave: Defined as the sign function
 *
 * @see <a href="https://en.wikipedia.org/wiki/Sine_wave">Wikipedia</a>
 */
object SineWaveGenerator : AngleBaseWaveGenerator() {

    override fun calculateData(angle: Double, amplitude: Int): Byte {
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    private fun waveFunction(angle: Double): Double = sin(angle)
}
