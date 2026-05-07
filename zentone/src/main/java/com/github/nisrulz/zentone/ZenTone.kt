/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nisrulz.zentone

import android.media.AudioTrack
import com.github.nisrulz.zentone.internal.bytesPerSample
import com.github.nisrulz.zentone.internal.channelCount
import com.github.nisrulz.zentone.internal.limitedParallelism
import com.github.nisrulz.zentone.internal.minBufferSize
import com.github.nisrulz.zentone.internal.sanitizeFrequencyValue
import com.github.nisrulz.zentone.internal.writeOptimizedAudioData
import com.github.nisrulz.zentone.wavegenerators.SineWaveGenerator
import com.github.nisrulz.zentone.wavegenerators.WaveByteArrayGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class ZenTone private constructor(
    private val sampleRate: Int = DEFAULT_SAMPLE_RATE,
    private val encoding: Int = DEFAULT_ENCODING,
    private val channelMask: Int = DEFAULT_CHANNEL_MASK
) : CoroutineScope {

    constructor(
        sampleRate: SampleRate = DEFAULT_SAMPLE_RATE_OPTION,
        channelMask: Int = DEFAULT_CHANNEL_MASK
    ) : this(
        sampleRate = sampleRate.hz,
        encoding = DEFAULT_ENCODING,
        channelMask = channelMask
    )

    override val coroutineContext = limitedParallelism() + SupervisorJob()

    init {
        setThreadPriority()
        bytesPerSample(encoding)
        channelCount(channelMask)
    }

    private val bufferSizeInBytes = minBufferSize(sampleRate, channelMask, encoding)

    private val audioTrack by lazy { initAudioTrack(sampleRate, encoding, channelMask) }

    private var frequency: Float = 0.0F
    private var activePlaybackJob: Job? = null

    private val isPlayingAtomic = AtomicBoolean(false)
    private val playbackSessionId = AtomicLong(0)

    /** Flag to track playback state */
    val isPlaying
        get() = isPlayingAtomic.get()

    private fun setFrequency(frequency: Float) {
        if (this.frequency == frequency) return
        this.frequency = sanitizeFrequencyValue(frequency, sampleRate)
    }

    private fun isValidFrequencyVolume(frequency: Float, volume: Int): Boolean =
        frequency > 0.0f && volume > 0

    /**
     * Start playing the tone as per passed config
     *
     * @param frequency
     * @param volume
     * @param waveByteArrayGenerator
     */
    fun play(
        frequency: Float,
        volume: Int,
        waveByteArrayGenerator: WaveByteArrayGenerator = SineWaveGenerator()
    ) {
        if (!isValidFrequencyVolume(frequency, volume)) return

        if (audioTrack.state != AudioTrack.STATE_INITIALIZED) return

        if (isPlayingAtomic.compareAndSet(false, true)) {
            setFrequency(frequency)
            val playbackId = playbackSessionId.incrementAndGet()
            val playbackChannelCount = channelCount(channelMask)
            val frameCount = bufferSizeInBytes / (bytesPerSample(encoding) * playbackChannelCount)

            audioTrack.apply {
                setVolumeLevel(volume)
                play()

                activePlaybackJob = launch {
                    var phaseAngle = 0.0
                    try {
                        while (isActive && isPlayingAtomic.get() && playbackSessionId.get() == playbackId) {
                            val audioData =
                                waveByteArrayGenerator.generate(
                                    freqOfTone = this@ZenTone.frequency,
                                    sampleRate = sampleRate,
                                    encoding = encoding,
                                    bufferSizeInBytes = bufferSizeInBytes,
                                    channelCount = playbackChannelCount,
                                    initialAngle = phaseAngle
                                )
                            phaseAngle =
                                waveByteArrayGenerator.nextAngle(
                                    freqOfTone = this@ZenTone.frequency,
                                    sampleRate = sampleRate,
                                    frameCount = frameCount,
                                    initialAngle = phaseAngle
                                )
                            writeOptimizedAudioData(audioData)
                        }
                    } finally {
                        if (playbackSessionId.get() == playbackId) {
                            activePlaybackJob = null
                        }
                    }
                }
            }
        }
    }

    /** Stop playing */
    fun stop() {
        with(audioTrack) {
            if (state != AudioTrack.STATE_INITIALIZED) return

            if (isPlayingAtomic.compareAndSet(true, false)) {
                playbackSessionId.incrementAndGet()
                activePlaybackJob?.cancel()
                activePlaybackJob = null
                pause() // Pause instantly instead of stopping abruptly
                flush() // Clear remaining audio data
            }
        }
    }

    /** Release and free up held resources */
    fun release() {
        stop()
        audioTrack.stopAndRelease()
        coroutineContext.cancel()
    }

    fun togglePlayback(frequency: Float, volume: Int) {
        if (isPlaying) {
            stop()
        } else {
            play(frequency, volume)
        }
    }

    companion object {
        fun advanced(
            sampleRate: SampleRate = DEFAULT_SAMPLE_RATE_OPTION,
            encoding: Int = DEFAULT_ENCODING,
            channelMask: Int = DEFAULT_CHANNEL_MASK
        ): ZenTone =
            ZenTone(
                sampleRate = sampleRate.hz,
                encoding = encoding,
                channelMask = channelMask
            )
    }
}
