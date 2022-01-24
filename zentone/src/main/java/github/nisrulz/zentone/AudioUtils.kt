package github.nisrulz.zentone

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.Process
import github.nisrulz.zentone.internal.minBufferSize


fun setThreadPriority() = Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)

fun initAudioTrack(sampleRate: Int = DEFAULT_SAMPLE_RATE): AudioTrack {
    val bufferSize = minBufferSize(sampleRate)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(DEFAULT_ENCODING)
                    .setSampleRate(sampleRate)
                    .setChannelMask(DEFAULT_CHANNEL_MASK)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .build()
    } else {
        AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            DEFAULT_CHANNEL_MASK,
            DEFAULT_ENCODING,
            bufferSize,
            AudioTrack.MODE_STREAM
        )
    }
}

fun AudioTrack.stopAndRelease() {
    try {
        stop()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        release()
    }
}

fun AudioTrack.setVolumeLevel(level: Float) {
    /* Sanity Check for max volume, set after write method
    to handle issue in Android 4.0.3 */
    var tempVolume = level
    val maxVolume = AudioTrack.getMaxVolume()
    if (tempVolume > maxVolume) {
        tempVolume = maxVolume
    } else if (tempVolume < 0) {
        tempVolume = 0f
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // For API >= 21
        setVolume(tempVolume)
    } else {
        // For API < 21
        setStereoVolume(tempVolume, tempVolume)
    }
}
