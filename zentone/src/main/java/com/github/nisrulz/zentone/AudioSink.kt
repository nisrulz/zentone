package com.github.nisrulz.zentone

import android.media.AudioTrack

internal interface AudioSink {
    val state: Int

    fun play()

    fun pause()

    fun flush()

    fun release()

    fun write(audioData: ByteArray, offsetInBytes: Int, sizeInBytes: Int): Int

    fun setVolumeLevel(level: Int)

    fun stopAndRelease()
}

internal class AudioTrackSink(
    private val audioTrack: AudioTrack
) : AudioSink {
    override val state: Int
        get() = audioTrack.state

    override fun play() {
        audioTrack.play()
    }

    override fun pause() {
        audioTrack.pause()
    }

    override fun flush() {
        audioTrack.flush()
    }

    override fun release() {
        audioTrack.release()
    }

    override fun write(audioData: ByteArray, offsetInBytes: Int, sizeInBytes: Int): Int =
        audioTrack.write(audioData, offsetInBytes, sizeInBytes)

    override fun setVolumeLevel(level: Int) {
        audioTrack.setVolumeLevel(level)
    }

    override fun stopAndRelease() {
        audioTrack.stopAndRelease()
    }
}
