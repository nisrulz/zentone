package com.github.nisrulz.zentone

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.Process
import com.github.nisrulz.zentone.internal.convertIntRangeToFloatRange
import com.github.nisrulz.zentone.internal.minBufferSize

/**
 * Set the thread priority to the highest possible value for AudioTrack.
 * This method should be called before creating the AudioTrack object.
 *
 * @return true if the priority was set successfully, false otherwise.
 */
fun setThreadPriority() = Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)

/**
 * Initialize the AudioTrack object with the specified parameters.
 *
 * @param sampleRate The sample rate of the audio track.
 * @param encoding The encoding of the audio track.
 * @param channelMask The channel mask of the audio track.
 * @return The initialized AudioTrack object.
 */
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

/**
 * Set volume level for audio track
 *
 * @param level 0-100
 */
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