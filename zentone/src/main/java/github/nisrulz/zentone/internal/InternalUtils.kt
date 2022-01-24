package github.nisrulz.zentone.internal

import android.media.AudioTrack
import github.nisrulz.zentone.DEFAULT_CHANNEL_MASK
import github.nisrulz.zentone.DEFAULT_ENCODING
import github.nisrulz.zentone.DEFAULT_SAMPLE_RATE
import github.nisrulz.zentone.MIN_FREQUENCY


internal fun minBufferSize(sampleRate: Int = DEFAULT_SAMPLE_RATE): Int {
    return AudioTrack.getMinBufferSize(
        sampleRate,
        DEFAULT_CHANNEL_MASK,
        DEFAULT_ENCODING
    )
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
