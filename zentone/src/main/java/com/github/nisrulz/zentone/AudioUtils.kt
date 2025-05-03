package com.github.nisrulz.zentone

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.Process
import com.github.nisrulz.zentone.internal.convertIntRangeToFloatRange
import com.github.nisrulz.zentone.internal.minBufferSize


fun setThreadPriority() = Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)

@Suppress("DEPRECATION")
fun initAudioTrack(sampleRate: Int, encoding: Int, channelMask: Int): AudioTrack {
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
                    .setEncoding(encoding)
                    .setSampleRate(sampleRate)
                    .setChannelMask(channelMask)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .build()
    } else {
        AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            channelMask,
            encoding,
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

fun AudioTrack.setVolumeLevel(level: Int) {
    // Sanity Check for max volume, set after write method to handle issue in Android 4.0.3
    var tempVolume = level.convertIntRangeToFloatRange()
    val maxVolume = AudioTrack.getMaxVolume()
    if (tempVolume > maxVolume) {
        tempVolume = maxVolume
    } else if (tempVolume < 0) {
        tempVolume = 0f
    }
    setVolume(tempVolume)
}
