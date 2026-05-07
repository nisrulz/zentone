package com.github.nisrulz.zentone.wavegenerators

import android.media.AudioFormat
import com.github.nisrulz.zentone.DEFAULT_AMPLITUDE
import com.github.nisrulz.zentone.DEFAULT_ENCODING
import com.github.nisrulz.zentone.DEFAULT_FREQUENCY_HZ
import com.github.nisrulz.zentone.DEFAULT_SAMPLE_RATE
import com.github.nisrulz.zentone.internal.bytesPerSample
import com.github.nisrulz.zentone.internal.minBufferSize
import kotlin.math.PI
import kotlin.math.roundToInt

interface WaveByteArrayGenerator {

    var angle: Double
    var angleStep: Double

    /**
     * Generate byte data for tone
     *
     * @param freqOfTone Frequency of the tone you want to generate, in Hz.
     * @param sampleRate Number of samples per second, in Hz
     * @return ByteArray of generated tone
     */
    fun generate(
        freqOfTone: Float = DEFAULT_FREQUENCY_HZ,
        sampleRate: Int = DEFAULT_SAMPLE_RATE,
        encoding: Int = DEFAULT_ENCODING,
        bufferSizeInBytes: Int = minBufferSize(sampleRate),
        channelCount: Int = 1
    ): ByteArray {
        val bytesPerSample = bytesPerSample(encoding)
        return generateFrameData(
            freqOfTone = freqOfTone,
            sampleRate = sampleRate,
            encoding = encoding,
            frameCount = bufferSizeInBytes / (bytesPerSample * channelCount),
            channelCount = channelCount
        )
    }

    fun generateFrameData(
        freqOfTone: Float = DEFAULT_FREQUENCY_HZ,
        sampleRate: Int = DEFAULT_SAMPLE_RATE,
        encoding: Int = DEFAULT_ENCODING,
        frameCount: Int,
        channelCount: Int = 1
    ): ByteArray {
        setup(freqOfTone, sampleRate)
        val bytesPerSample = bytesPerSample(encoding)
        val generatedSnd = ByteArray(frameCount * bytesPerSample * channelCount)

        repeat(frameCount) { frameIndex ->
            val sample = calculateData(DEFAULT_AMPLITUDE)
            repeat(channelCount) { channelIndex ->
                val byteIndex =
                    (frameIndex * channelCount * bytesPerSample) +
                        (channelIndex * bytesPerSample)
                writeSample(
                    output = generatedSnd,
                    byteIndex = byteIndex,
                    normalizedSample = sample,
                    encoding = encoding
                )
            }
        }

        return generatedSnd
    }

    fun calculateData(angle: Double, amplitude: Int): Double


    /**
     * Setup required before generating a frame.
     * Must be called at least once before calculateData.
     */
    fun setup(freqOfTone: Float, sampleRate: Int) {
        angleStep = (2 * PI * freqOfTone) / sampleRate
    }

    /**
     * Reset the generator to be able to start over from the start again.
     */
    fun reset() {
        angle = 0.0
    }

    private fun incrementAngle(
        angle: Double,
        angleStep: Double
    ): Double {
        return (angle + angleStep) % (2 * Math.PI)
    }

    private fun calculateData(amplitude: Int): Double {
        val sample = calculateData(angle, amplitude)
        angle = incrementAngle(angle, angleStep)
        return sample
    }

    private fun writeSample(
        output: ByteArray,
        byteIndex: Int,
        normalizedSample: Double,
        encoding: Int
    ) {
        when (encoding) {
            AudioFormat.ENCODING_PCM_8BIT -> {
                val pcm8Sample =
                    ((normalizedSample + 1.0) * 127.5)
                        .roundToInt()
                        .coerceIn(0, 255)
                output[byteIndex] = pcm8Sample.toByte()
            }

            AudioFormat.ENCODING_PCM_16BIT -> {
                val pcm16Sample =
                    (normalizedSample * Short.MAX_VALUE)
                        .roundToInt()
                        .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                        .toShort()
                output[byteIndex] = pcm16Sample.toInt().toByte()
                output[byteIndex + 1] = (pcm16Sample.toInt() shr Byte.SIZE_BITS).toByte()
            }
        }
    }

}
