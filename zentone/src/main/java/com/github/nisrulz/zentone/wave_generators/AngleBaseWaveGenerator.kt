package com.github.nisrulz.zentone.wave_generators

import kotlin.math.PI

/**
 * Base class for angle based wave generators. Generates a continuously changing angle for the
 * the wave generators to use. The angle wraps around 2*Pi.
 */
abstract class AngleBaseWaveGenerator : WaveByteArrayGenerator {

    private var angle:Double = 0.0
    private var angleStep:Double = 0.0

    /**
     * Reset the generator to be able to start over from the start again.
     */
    override fun reset(){
        angle = 0.0
    }

    /**
     * Setup required before generating a frame. Must be called at least once before calculateData.
     */
    override fun setup(freqOfTone: Float, sampleRate: Int) {
        val samplingInterval = sampleRate / freqOfTone
        angleStep = PI / samplingInterval
    }

    private fun incrementAngle(
        angle: Double,
        angleStep: Double
    ): Double {
        return (angle + angleStep) % (2*Math.PI)
    }

    override fun calculateData(index: Int, amplitude: Int): Byte {
        angle = incrementAngle(angle, angleStep)
        return calculateData(angle, amplitude)
    }

    protected abstract fun calculateData(angle: Double, amplitude: Int): Byte;

}
