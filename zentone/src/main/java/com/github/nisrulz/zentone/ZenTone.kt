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
import com.github.nisrulz.zentone.internal.limitedParallelism
import com.github.nisrulz.zentone.internal.sanitizeFrequencyValue
import com.github.nisrulz.zentone.internal.writeOptimizedAudioData
import com.github.nisrulz.zentone.wavegenerators.SineWaveGenerator
import com.github.nisrulz.zentone.wavegenerators.WaveByteArrayGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class ZenTone(
    sampleRate: Int = DEFAULT_SAMPLE_RATE,
    encoding: Int = DEFAULT_ENCODING,
    channelMask: Int = DEFAULT_CHANNEL_MASK
) : CoroutineScope {

    override val coroutineContext = limitedParallelism() + SupervisorJob()

    init {
        setThreadPriority()
    }

    private val audioTrack by lazy { initAudioTrack(sampleRate, encoding, channelMask) }

    private var frequency: Float = 0.0F

    private val isPlayingAtomic = AtomicBoolean(false)

    /** Flag to track playback state */
    val isPlaying
        get() = isPlayingAtomic.get()

    private fun setFrequency(frequency: Float) {
        if (this.frequency == frequency) return
        this.frequency = sanitizeFrequencyValue(frequency)
    }

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
        waveByteArrayGenerator: WaveByteArrayGenerator = SineWaveGenerator
    ) {
        if (isPlayingAtomic.compareAndSet(false, true) && volume > 0 && frequency > 0.0f) {
            setFrequency(frequency)

            audioTrack.apply {
                if (state != AudioTrack.STATE_INITIALIZED) return

                setVolumeLevel(volume)
                play()

                launch {
                    while (isPlaying) {
                        val audioData = waveByteArrayGenerator.generate(this@ZenTone.frequency)
                        writeOptimizedAudioData(audioData)
                    }
                    waveByteArrayGenerator.reset()
                    stop()

                }

            }
        }
    }

    /** Stop playing */
    fun stop() {
        if (isPlayingAtomic.compareAndSet(true, false)) {
            audioTrack.pause() // Pause instantly instead of stopping abruptly
            audioTrack.flush() // Clear remaining audio data
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
}
