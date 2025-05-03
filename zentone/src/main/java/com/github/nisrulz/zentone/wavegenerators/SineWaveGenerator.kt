package com.github.nisrulz.zentone.wavegenerators

import kotlin.math.sin


/**
 * Sine wave generator
 *
 * Sine Wave: The most basic and simple waveform, a sine wave has a simple hollow sound.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Sine_wave">Wikipedia</a>
 */
object SineWaveGenerator : WaveByteArrayGenerator {
    override var angle: Double = 0.0
    override var angleStep: Double = 0.0

    override fun calculateData(angle: Double, amplitude: Int): Byte {
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    private fun waveFunction(angle: Double): Double = sin(angle)
}
