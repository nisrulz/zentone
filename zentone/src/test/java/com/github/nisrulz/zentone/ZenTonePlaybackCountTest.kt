package com.github.nisrulz.zentone

import android.media.AudioTrack
import com.github.nisrulz.zentone.wavegenerators.SineWaveGenerator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

internal class ZenTonePlaybackCountTest {

    @Test
    fun `playback count of two stops automatically after two writes`() = runTest {
        val writeCallCount = AtomicInteger(0)
        val audioSink = createCountingAudioSink(writeCallCount)
        val zenTone = createZenToneForTest(audioSink, coroutineContext = playbackCoroutineContext())

        zenTone.play(
            frequency = TEST_FREQUENCY_HZ,
            volume = TEST_VOLUME,
            playbackCount = TWO_PLAYBACKS,
            waveByteArrayGenerator = SineWaveGenerator()
        )

        waitUntilOrFail { !zenTone.isPlaying }

        verify(exactly = 1) { audioSink.play() }
        verify(exactly = 2) { audioSink.write(any(), any(), any()) }
        verify(exactly = 1) { audioSink.pause() }
        verify(exactly = 1) { audioSink.flush() }
        assertFalse(zenTone.isPlaying)
    }

    @Test
    fun `default playback count keeps playing until stopped`() = runTest {
        val writeCallCount = AtomicInteger(0)
        val audioSink = createCountingAudioSink(writeCallCount)
        val zenTone = createZenToneForTest(audioSink, coroutineContext = playbackCoroutineContext())

        zenTone.play(
            frequency = TEST_FREQUENCY_HZ,
            volume = TEST_VOLUME,
            waveByteArrayGenerator = SineWaveGenerator()
        )

        waitUntilOrFail { writeCallCount.get() >= TWO_PLAYBACKS }

        assertTrue(zenTone.isPlaying)

        zenTone.stop()
    }

    private fun createCountingAudioSink(writeCallCount: AtomicInteger): AudioSink {
        val audioSink = mockk<AudioSink>(relaxed = true)

        every { audioSink.state } returns AudioTrack.STATE_INITIALIZED
        every { audioSink.write(any(), any(), any()) } answers {
            writeCallCount.incrementAndGet()
            thirdArg()
        }

        return audioSink
    }

    private fun playbackCoroutineContext() = SupervisorJob() + Dispatchers.Default.limitedParallelism(2)

    private companion object {
        const val TWO_PLAYBACKS = 2
    }
}
