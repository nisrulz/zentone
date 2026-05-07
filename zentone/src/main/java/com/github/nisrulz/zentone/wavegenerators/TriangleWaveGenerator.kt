package com.github.nisrulz.zentone.wavegenerators

import kotlin.math.asin
import kotlin.math.sin

/**
 * Triangle wave generator
 *
 * Triangle Wave: A triangle wave increases and decreases linearly, creating a wave that looks
 * like a series of triangles.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Triangle_wave">Wikipedia</a>
 */
class TriangleWaveGenerator : WaveByteArrayGenerator {
    override fun calculateData(angle: Double, amplitude: Int): Double {
        return amplitude * asin(sin(angle))
    }
}
