package com.github.nisrulz.zentone

import android.media.AudioTrack
import com.github.nisrulz.zentone.wavegenerators.SineWaveGenerator
import com.github.nisrulz.zentone.wavegenerators.WaveByteArrayGenerator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

internal class ZenToneConcurrencyTest {

    @Test
    fun `rapid stop and replay does not let old playback stop the new session`() = runTest {
        val playbackSync = PlaybackSync()
        val audioSink = createBlockingAudioSink(playbackSync)
        val zenTone = createZenToneForTest(audioSink, coroutineContext = playbackCoroutineContext())

        zenTone.play(TEST_FREQUENCY_HZ, TEST_VOLUME, waveByteArrayGenerator = SineWaveGenerator())
        playbackSync.firstWriteStarted.awaitOrFail()
        val stopThread = stopPlaybackAsync(zenTone)
        waitUntilOrFail { !zenTone.isPlaying }
        zenTone.play(TEST_FREQUENCY_HZ, TEST_VOLUME, waveByteArrayGenerator = SineWaveGenerator())
        playbackSync.releaseWrites.countDown()
        playbackSync.secondWriteStarted.awaitOrFail()
        stopThread.join(DEFAULT_TIMEOUT_MILLIS)

        verify(exactly = 2) { audioSink.play() }
        verify(exactly = 1) { audioSink.pause() }
        verify(exactly = 1) { audioSink.flush() }
        assertFalse(stopThread.isAlive)
        assertTrue(zenTone.isPlaying)

        zenTone.release()
    }

    @Test
    fun `reused custom generator instance does not share phase across replays`() = runTest {
        val playbackSync = PlaybackSync()
        val firstAngles = mutableListOf<Double>()
        val audioSink = createBlockingAudioSink(playbackSync)
        val recordingGenerator =
            object : WaveByteArrayGenerator {
                override fun calculateData(angle: Double, amplitude: Int): Double {
                    if (firstAngles.size < 2) {
                        firstAngles += angle
                    }
                    return 0.0
                }
            }

        val zenTone = createZenToneForTest(audioSink, coroutineContext = playbackCoroutineContext())

        zenTone.play(TEST_FREQUENCY_HZ, TEST_VOLUME, waveByteArrayGenerator = recordingGenerator)
        playbackSync.firstWriteStarted.awaitOrFail()
        val stopThread = stopPlaybackAsync(zenTone)
        waitUntilOrFail { !zenTone.isPlaying }
        zenTone.play(TEST_FREQUENCY_HZ, TEST_VOLUME, waveByteArrayGenerator = recordingGenerator)
        playbackSync.releaseWrites.countDown()
        playbackSync.secondWriteStarted.awaitOrFail()
        stopThread.join(DEFAULT_TIMEOUT_MILLIS)

        assertEquals(listOf(0.0, 0.0), firstAngles)
        assertFalse(stopThread.isAlive)

        zenTone.release()
    }

    private fun createBlockingAudioSink(playbackSync: PlaybackSync): AudioSink {
        val audioSink = mockk<AudioSink>(relaxed = true)
        var writeCallCount = 0

        every { audioSink.state } returns AudioTrack.STATE_INITIALIZED
        every { audioSink.write(any(), any(), any()) } answers {
            writeCallCount += 1
            when (writeCallCount) {
                FIRST_WRITE_CALL -> {
                    playbackSync.firstWriteStarted.countDown()
                    playbackSync.releaseWrites.awaitOrFail()
                }

                SECOND_WRITE_CALL -> playbackSync.secondWriteStarted.countDown()
            }
            thirdArg()
        }
        return audioSink
    }

    private fun playbackCoroutineContext() = SupervisorJob() + Dispatchers.Default.limitedParallelism(2)

    private fun stopPlaybackAsync(zenTone: ZenTone) =
        thread(start = true) {
            zenTone.stop()
        }

    private class PlaybackSync {
        val firstWriteStarted = CountDownLatch(1)
        val secondWriteStarted = CountDownLatch(1)
        val releaseWrites = CountDownLatch(1)
    }

    private companion object {
        const val FIRST_WRITE_CALL = 1
        const val SECOND_WRITE_CALL = 2
    }
}
