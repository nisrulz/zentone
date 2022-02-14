package com.github.nisrulz.zentone.wave_generators

import kotlin.math.sign
import kotlin.math.sin

// https://en.wikipedia.org/wiki/Square_wave
object SquareWaveGenerator : WaveByteArrayGenerator {

    override fun calculateData(index: Int, samplingInterval: Float, amplitude: Int): Byte {
        val angle: Double = (Math.PI * index) / samplingInterval
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    private fun waveFunction(angle: Double): Double = sign(sin(angle))
}