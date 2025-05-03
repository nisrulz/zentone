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
import com.github.nisrulz.zentone.wavegenerators.SineWaveGenerator
import com.github.nisrulz.zentone.wavegenerators.WaveByteArrayGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

class ZenTone(
    sampleRate: Int = DEFAULT_SAMPLE_RATE,
    encoding: Int = DEFAULT_ENCODING,
    channelMask: Int = DEFAULT_CHANNEL_MASK
) : CoroutineScope {

    private val mutex = Mutex()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    init {
        setThreadPriority()
    }

    private val audioTrack by lazy { initAudioTrack(sampleRate, encoding, channelMask) }

    /** Boolean flag to check if ZenTone is playing tone */
    var isPlaying = false

    /** Current frequency in use by ZenTone.*/
    private var frequency: Float = 0.0F

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
        if (!isPlaying && volume > 0 && frequency > 0.0f) {
            setFrequency(frequency)

            audioTrack.apply {
                if (state != AudioTrack.STATE_INITIALIZED) cancel()

                setVolumeLevel(volume)

                play()
                isPlaying = true

                launch {
                    mutex.withLock {
                        while (isPlaying) {
                            val audioData = waveByteArrayGenerator.generate(this@ZenTone.frequency)
                            write(audioData, 0, audioData.size)
                        }

                        stop()
                        cancel()
                        waveByteArrayGenerator.reset()
                    }
                }
            }
        }
    }

    private fun setFrequency(frequency: Float) {
        this.frequency = sanitizeFrequencyValue(frequency)
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
