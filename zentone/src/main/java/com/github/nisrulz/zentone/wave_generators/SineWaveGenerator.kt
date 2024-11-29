package com.github.nisrulz.zentone.wave_generators

import kotlin.math.sin


/**
 * Sine wave generator
 *
 * Sine Wave: Defined as the sign function
 *
 * @see <a href="https://en.wikipedia.org/wiki/Sine_wave">Wikipedia</a>
 */
object SineWaveGenerator : WaveByteArrayGenerator() {
    override fun waveFunction(angle: Double): Double = sin(angle)
}
