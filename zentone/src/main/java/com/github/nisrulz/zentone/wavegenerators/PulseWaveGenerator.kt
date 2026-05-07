package com.github.nisrulz.zentone.wavegenerators

import kotlin.math.PI

/**
 * Pulse wave generator
 *
 * Pulse Wave: A pulse wave is a square-like waveform whose positive portion only lasts for a
 * configurable fraction of each cycle.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Pulse_wave">Wikipedia</a>
 */
class PulseWaveGenerator(
    private val dutyCycle: Double = DEFAULT_DUTY_CYCLE
) : WaveByteArrayGenerator {

    init {
        require(dutyCycle > 0.0 && dutyCycle <= 1.0) {
            "dutyCycle must be in the range (0.0, 1.0]."
        }
    }

    override fun calculateData(angle: Double, amplitude: Int): Double {
        val cycleLength = 2.0 * PI
        val wrappedAngle = angle % cycleLength
        val normalizedPhase = wrappedAngle / cycleLength
        // Stay at the positive rail for the requested duty-cycle portion of the cycle.
        val pulsePolarity = if (normalizedPhase < dutyCycle) 1.0 else -1.0
        val amplitudeScale = amplitude.toDouble()

        return amplitudeScale * pulsePolarity
    }

    companion object {
        private const val DEFAULT_DUTY_CYCLE = 0.25
    }
}
