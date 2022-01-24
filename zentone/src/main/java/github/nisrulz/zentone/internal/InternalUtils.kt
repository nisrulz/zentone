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

internal fun getMaxFrequency(sampleRate: Int) = sampleRate / 2

@Throws(IllegalArgumentException::class)
internal fun validateFrequency(frequency: Float, sampleRate: Int = DEFAULT_SAMPLE_RATE) {
    val maxFrequency = getMaxFrequency(sampleRate)
    if (frequency < MIN_FREQUENCY || frequency > maxFrequency) {
        throw IllegalArgumentException("Frequency is out of range (1 ...$maxFrequency)")
    }
}

internal fun Float.convertIntRangeToFloatRange() = this / 100f
