package com.github.nisrulz.zentone.internal

import android.media.AudioFormat
import android.media.AudioTrack
import com.github.nisrulz.zentone.BYTES_PER_PCM_8_SAMPLE
import com.github.nisrulz.zentone.BYTES_PER_PCM_16_SAMPLE
import com.github.nisrulz.zentone.DEFAULT_CHANNEL_MASK
import com.github.nisrulz.zentone.DEFAULT_ENCODING
import com.github.nisrulz.zentone.DEFAULT_SAMPLE_RATE
import com.github.nisrulz.zentone.MIN_FREQUENCY
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


/**
 * Calculates the minimum buffer size required for an AudioTrack, considering the sample rate and a specified factor.
 *
 * This function utilizes the `AudioTrack.getMinBufferSize()` method to determine the minimum buffer size.
 * The buffer size is then multiplied by a given factor to accommodate potential variations in audio data.
 *
 * @param sampleRate The sample rate of the audio (in Hz). Defaults to `DEFAULT_SAMPLE_RATE`.
 * @param factor The factor to increase the buffer size by. Defaults to 1.  A value of 2, for instance, will
 *               double the buffer size.
 * @return The calculated minimum buffer size.
 */
internal fun minBufferSize(sampleRate: Int = DEFAULT_SAMPLE_RATE, factor: Int = 1): Int {
    return minBufferSize(
        sampleRate = sampleRate,
        channelMask = DEFAULT_CHANNEL_MASK,
        encoding = DEFAULT_ENCODING,
        factor = factor
    )
}

internal fun minBufferSize(
    sampleRate: Int = DEFAULT_SAMPLE_RATE,
    channelMask: Int = DEFAULT_CHANNEL_MASK,
    encoding: Int = DEFAULT_ENCODING,
    factor: Int = 1
): Int {
    val value = AudioTrack.getMinBufferSize(
        sampleRate,
        channelMask,
        encoding
    )
    require(value > 0) {
        "Could not determine a valid AudioTrack buffer size for sampleRate=$sampleRate, channelMask=$channelMask, encoding=$encoding. getMinBufferSize() returned $value."
    }
    return value * factor
}

internal fun channelCount(channelMask: Int): Int =
    when (channelMask) {
        AudioFormat.CHANNEL_OUT_MONO -> 1
        AudioFormat.CHANNEL_OUT_STEREO -> 2
        else -> error("Unsupported channel mask: $channelMask. Only mono and stereo are supported.")
    }

internal fun bytesPerSample(encoding: Int): Int =
    when (encoding) {
        AudioFormat.ENCODING_PCM_8BIT -> BYTES_PER_PCM_8_SAMPLE
        AudioFormat.ENCODING_PCM_16BIT -> Short.SIZE_BYTES
        else -> error("Unsupported encoding: $encoding. Only PCM 8-bit and PCM 16-bit are supported.")
    }

internal fun getMaxFrequency(sampleRate: Int) = sampleRate / 2.0f

internal fun sanitizeFrequencyValue(
    frequency: Float,
    sampleRate: Int = DEFAULT_SAMPLE_RATE
): Float {
    val maxFrequency = getMaxFrequency(sampleRate)
    return when {
        frequency < MIN_FREQUENCY -> {
            MIN_FREQUENCY
        }

        frequency > maxFrequency -> {
            maxFrequency
        }

        else -> frequency
    }
}

internal fun Int.convertIntRangeToFloatRange() = this / 100f


internal fun limitedParallelism(n: Int = 1): CoroutineContext =
    Dispatchers.Default.limitedParallelism(n)


internal fun AudioTrack.writeOptimizedAudioData(audioData: ByteArray) {
    val chunkSize = maxOf(4096, audioData.size / 4)
    var index = 0
    while (index < audioData.size) {
        val size = minOf(chunkSize, audioData.size - index)
        val bytesWritten = write(audioData, index, size)
        if (bytesWritten <= 0) {
            error("AudioTrack.write() failed with code $bytesWritten.")
        }
        index += bytesWritten
    }
}
