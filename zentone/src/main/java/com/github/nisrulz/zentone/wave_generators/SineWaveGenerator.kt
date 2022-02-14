package com.github.nisrulz.zentone.wave_generators

import kotlin.math.sin

// https://en.wikipedia.org/wiki/Sine_wave
object SineWaveGenerator : WaveByteArrayGenerator {
    override fun waveFunction(angle: Double): Double = sin(angle)
}