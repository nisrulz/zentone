package com.github.nisrulz.zentone.wavegenerators

import kotlin.math.sin


/**
 * Sine wave generator
 *
 * Sine Wave: The most basic and simple waveform, a sine wave has a simple hollow sound.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Sine_wave">Wikipedia</a>
 */
class SineWaveGenerator : WaveByteArrayGenerator {
    override fun calculateData(angle: Double, amplitude: Int): Double {
        val sineValue = sin(angle)
        val amplitudeScale = amplitude.toDouble()

        return amplitudeScale * sineValue
    }
}
