package github.nisrulz.zentone.internal

import github.nisrulz.zentone.DEFAULT_FREQUENCY_HZ
import github.nisrulz.zentone.DEFAULT_SAMPLE_RATE

interface WaveByteArrayGenerator {
    fun generate(
        freqOfTone: Int = DEFAULT_FREQUENCY_HZ,
        sampleRate: Int = DEFAULT_SAMPLE_RATE
    ): ByteArray {
        val bufferSize = minBufferSize(sampleRate)
        val samplingInterval = sampleRate / freqOfTone
        val amplitude = 1

        val generatedSnd = ByteArray(bufferSize)

        generatedSnd.indices.forEach { i ->
            generatedSnd[i] = calculateData(i, samplingInterval, amplitude)
        }

        return generatedSnd
    }

    fun calculateData(index: Int, samplingInterval: Int, amplitude: Int): Byte
}