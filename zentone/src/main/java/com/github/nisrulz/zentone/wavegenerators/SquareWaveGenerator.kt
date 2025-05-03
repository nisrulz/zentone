package com.github.nisrulz.zentone.wavegenerators

import kotlin.math.sign
import kotlin.math.sin

/**
 * Square wave generator
 *
 * Square Wave: The square wave has only odd harmonics. This harmonic structure gives the square
 * wave a little more bite to the sound.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Square_wave">Wikipedia</a>
 */
object SquareWaveGenerator : WaveByteArrayGenerator {

    override var angle: Double = 0.0
    override var angleStep: Double = 0.0

    override fun calculateData(angle: Double, amplitude: Int): Byte {
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    private fun waveFunction(angle: Double): Double = sign(sin(angle))
}
