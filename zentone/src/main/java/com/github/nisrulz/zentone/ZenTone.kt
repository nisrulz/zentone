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
import com.github.nisrulz.zentone.internal.sanitizeFrequencyValue
import com.github.nisrulz.zentone.wave_generators.SineWaveGenerator
import com.github.nisrulz.zentone.wave_generators.WaveByteArrayGenerator
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ZenTone(
    sampleRate: Int = DEFAULT_SAMPLE_RATE,
    encoding: Int = DEFAULT_ENCODING,
    channelMask: Int = DEFAULT_CHANNEL_MASK
) : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    init {
        setThreadPriority()
    }

    private val audioTrack by lazy { initAudioTrack(sampleRate, encoding, channelMask) }
    var isPlaying = false

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
        if (!isPlaying && volume > 0) {
            val freqOfTone = sanitizeFrequencyValue(frequency)
            val audioData = waveByteArrayGenerator.generate(freqOfTone)

            audioTrack.apply {
                if (state != AudioTrack.STATE_INITIALIZED) cancel() // cancel all jobs

                setVolumeLevel(volume)

                play()
                isPlaying = true

                launch {
                    while (isPlaying) {
                        write(audioData, 0, audioData.size)
                    }

                    stop()
                    // cancel all jobs
                    cancel()
                }
            }
        }
    }

    /** Stop playing the tone */
    fun stop() {
        if (isPlaying) {
            isPlaying = false
        }
    }

    /** Release and free up resources held by ZenTone */
    fun release() {
        stop()
        audioTrack.stopAndRelease()
    }
}