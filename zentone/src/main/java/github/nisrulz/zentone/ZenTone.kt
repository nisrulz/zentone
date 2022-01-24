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
package github.nisrulz.zentone

import android.media.AudioTrack
import github.nisrulz.zentone.internal.SineWaveGenerator
import github.nisrulz.zentone.internal.validateFrequency
import java.lang.Runnable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ZenTone {
    private var freqOfTone = 0f
    private var volume = 0

    private var shouldContinueProcessingAudio = false
    private var executorService: ExecutorService? = null

    private val audioPlayRunnable = Runnable {
        setThreadPriority()

        val audioTrack = initAudioTrack()

        if (audioTrack.state != AudioTrack.STATE_INITIALIZED) {
            return@Runnable
        }

        //Generating sinusoidal waves
        validateFrequency(freqOfTone)
        val sineWave = SineWaveGenerator.generate(freqOfTone)

        audioTrack.setVolumeLevel(volume)

        // start playing
        audioTrack.play()

        shouldContinueProcessingAudio = true
        while (shouldContinueProcessingAudio) {
            audioTrack.write(sineWave, 0, sineWave.size)
        }

        audioTrack.stopAndRelease()
    }

    fun generate(
        freqOfTone: Float,
        volume: Int
    ) {
        this.freqOfTone = freqOfTone
        this.volume = volume
        start()
    }

    private fun start() {
        if (executorService?.isShutdown?.not() == true) {
            stopThreadAndProcessing()
        }
        executorService = Executors.newSingleThreadExecutor()
        executorService?.submit(audioPlayRunnable)
    }

    private fun stopThreadAndProcessing() {
        // Stop audio processing
        shouldContinueProcessingAudio = false
        // interrupt the thread
        executorService?.shutdown()
        //executorService.shutdownNow();
        executorService = null

    }

    fun stop() {
        stopThreadAndProcessing()
    }

}