package com.github.nisrulz.zentone.wave_generators

import android.util.Log
import com.github.nisrulz.zentone.DEFAULT_AMPLITUDE
import com.github.nisrulz.zentone.DEFAULT_FREQUENCY_HZ
import com.github.nisrulz.zentone.DEFAULT_SAMPLE_RATE
import com.github.nisrulz.zentone.internal.minBufferSize
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

abstract class WaveByteArrayGenerator {

    private var angle:Double = 0.0
    /**
     * Generate byte data for tone
     *
     * @param freqOfTone
     * @param sampleRate
     * @return ByteArray of generated tone
     */
    fun generate(
        freqOfTone: Float = DEFAULT_FREQUENCY_HZ,
        sampleRate: Int = DEFAULT_SAMPLE_RATE
    ): ByteArray {
        val samplingInterval = sampleRate / freqOfTone
        val angleStep = PI / samplingInterval
        val bufferSize = minBufferSize(sampleRate)

        val generatedSnd = ByteArray(bufferSize)

        generatedSnd.indices.forEach { i ->
            angle = incrementAngle(angle, angleStep)
            generatedSnd[i] = calculateData(angle, DEFAULT_AMPLITUDE)
        }

        return generatedSnd
    }

    fun reset(){
        angle = 0.0
    }

    fun incrementAngle(
        angle: Double,
        angleStep: Double
    ): Double {
        return (angle + angleStep) % (2*Math.PI)
    }

    private fun calculateData(angle: Double, amplitude: Int): Byte {
        return (amplitude * waveFunction(angle) * Byte.MAX_VALUE).toInt().toByte()
    }

    protected abstract fun waveFunction(angle: Double): Double;
}
