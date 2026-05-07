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
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.CoroutineContext

class ZenTone internal constructor(
    private val sampleRate: Int = DEFAULT_SAMPLE_RATE,
    private val encoding: Int = DEFAULT_ENCODING,
    private val channelMask: Int = DEFAULT_CHANNEL_MASK,
    private val audioSinkFactory: (Int, Int, Int) -> AudioSink = ::initAudioSink,
    private val threadPrioritySetter: () -> Unit = ::setThreadPriority,
    private val bufferSizeInBytesOverride: Int? = null,
    coroutineContext: CoroutineContext = limitedParallelism() + SupervisorJob()
) : CoroutineScope {

    constructor(
        sampleRate: SampleRate = DEFAULT_SAMPLE_RATE_OPTION,
        channelMask: Int = DEFAULT_CHANNEL_MASK
    ) : this(
        sampleRate = sampleRate.hz,
        encoding = DEFAULT_ENCODING,
        channelMask = channelMask
    )

    override val coroutineContext = coroutineContext

    init {
        threadPrioritySetter()
        bytesPerSample(encoding)
        channelCount(channelMask)
    }

    private val bufferSizeInBytes =
        bufferSizeInBytesOverride ?: minBufferSize(sampleRate, channelMask, encoding)

    private val audioSink by lazy { audioSinkFactory(sampleRate, encoding, channelMask) }

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

    private fun isValidPlaybackCount(playbackCount: Int): Boolean = playbackCount >= 0

    /**
     * Start playing the tone as per passed config
     *
     * @param frequency
     * @param volume
     * @param playbackCount Number of times to write the generated signal. `0` keeps playing
     * indefinitely.
     * @param waveByteArrayGenerator
     */
    fun play(
        frequency: Float,
        volume: Int,
        playbackCount: Int = UNLIMITED_PLAYBACK_COUNT,
        waveByteArrayGenerator: WaveByteArrayGenerator = SineWaveGenerator()
    ) {
        if (!isValidFrequencyVolume(frequency, volume) || !isValidPlaybackCount(playbackCount)) return

        if (audioSink.state != AudioTrack.STATE_INITIALIZED) return

        if (isPlayingAtomic.compareAndSet(false, true)) {
            setFrequency(frequency)
            val playbackId = playbackSessionId.incrementAndGet()
            val playbackChannelCount = channelCount(channelMask)
            val frameCount = bufferSizeInBytes / (bytesPerSample(encoding) * playbackChannelCount)

            audioSink.apply {
                setVolumeLevel(volume)
                play()

                activePlaybackJob = launch {
                    var phaseAngle = 0.0
                    var remainingPlaybackCount = playbackCount
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
                            if (remainingPlaybackCount != UNLIMITED_PLAYBACK_COUNT) {
                                remainingPlaybackCount -= 1
                                if (remainingPlaybackCount == 0) {
                                    isPlayingAtomic.set(false)
                                    pause()
                                    flush()
                                    break
                                }
                            }
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
        with(audioSink) {
            if (state != AudioTrack.STATE_INITIALIZED) return

            if (isPlayingAtomic.compareAndSet(true, false)) {
                playbackSessionId.incrementAndGet()
                runBlocking {
                    activePlaybackJob?.cancelAndJoin()
                }
                activePlaybackJob = null
                pause() // Pause instantly instead of stopping abruptly
                flush() // Clear remaining audio data
            }
        }
    }

    /** Release and free up held resources */
    fun release() {
        stop()
        audioSink.stopAndRelease()
        coroutineContext.cancel()
    }

    fun togglePlayback(
        frequency: Float,
        volume: Int,
        playbackCount: Int = UNLIMITED_PLAYBACK_COUNT
    ) {
        if (isPlaying) {
            stop()
        } else {
            play(frequency, volume, playbackCount)
        }
    }

    companion object {
        const val UNLIMITED_PLAYBACK_COUNT = 0

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
