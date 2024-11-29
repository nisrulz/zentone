package com.github.nisrulz.zentone.wave_generators

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
object TriangleWaveGenerator : WaveByteArrayGenerator(){
    override fun waveFunction(angle: Double): Double = asin(sin(angle))
}
