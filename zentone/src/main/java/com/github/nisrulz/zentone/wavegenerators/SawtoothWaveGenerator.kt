package com.github.nisrulz.zentone.wavegenerators

import kotlin.math.PI

/**
 * Sawtooth wave generator
 *
 * Sawtooth Wave: A sawtooth wave ramps upward linearly and then drops sharply to the minimum.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Sawtooth_wave">Wikipedia</a>
 */
class SawtoothWaveGenerator : WaveByteArrayGenerator {
    override fun calculateData(angle: Double, amplitude: Int): Double {
        val cycleLength = 2.0 * PI
        val wrappedAngle = angle % cycleLength
        val normalizedPhase = wrappedAngle / cycleLength
        // Map one cycle from [0, 1) into the normalized audio range [-1, 1).
        val normalizedSawtoothValue = (2.0 * normalizedPhase) - 1.0
        val amplitudeScale = amplitude.toDouble()

        return amplitudeScale * normalizedSawtoothValue
    }
}
