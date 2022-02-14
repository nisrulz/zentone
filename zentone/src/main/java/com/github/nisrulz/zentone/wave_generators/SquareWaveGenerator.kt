package com.github.nisrulz.zentone.wave_generators

import kotlin.math.sign
import kotlin.math.sin

// https://en.wikipedia.org/wiki/Square_wave
object SquareWaveGenerator : WaveByteArrayGenerator {
    override fun waveFunction(angle: Double): Double = sign(sin(angle))
}