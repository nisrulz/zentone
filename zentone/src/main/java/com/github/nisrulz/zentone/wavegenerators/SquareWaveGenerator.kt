package com.github.nisrulz.zentone.wavegenerators

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
        val sineValue = sin(angle)
        // A square wave snaps every sample to the positive or negative rail.
        val squarePolarity = if (sineValue >= 0.0) 1.0 else -1.0
        val amplitudeScale = amplitude.toDouble()

        return amplitudeScale * squarePolarity
    }
}
