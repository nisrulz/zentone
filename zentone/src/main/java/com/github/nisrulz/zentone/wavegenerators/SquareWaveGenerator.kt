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
class SquareWaveGenerator : WaveByteArrayGenerator {
    override fun calculateData(angle: Double, amplitude: Int): Double {
        return amplitude * sign(sin(angle))
    }
}
