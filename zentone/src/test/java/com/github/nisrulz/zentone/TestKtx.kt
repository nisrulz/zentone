package com.github.nisrulz.zentone

import org.junit.Assert.assertTrue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

internal const val DEFAULT_TIMEOUT_SECONDS = 2L
internal const val DEFAULT_TIMEOUT_MILLIS = 2_000L
internal const val TEST_FREQUENCY_HZ = 440f
internal const val TEST_VOLUME = 10
internal const val WAVE_TEST_FREQUENCY_HZ = 200f
internal const val WAVE_TEST_SAMPLE_RATE = 44100
internal const val UNIT_AMPLITUDE = 1
internal const val DOUBLE_TOLERANCE = 1e-12
internal const val SINGLE_FRAME_BUFFER_SIZE_IN_BYTES = 2

internal fun CountDownLatch.awaitOrFail(timeoutSeconds: Long = DEFAULT_TIMEOUT_SECONDS) {
    assertTrue(await(timeoutSeconds, TimeUnit.SECONDS))
}

internal fun waitUntilOrFail(
    timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
    condition: () -> Boolean
) {
    val deadlineMillis = System.currentTimeMillis() + timeoutMillis
    while (System.currentTimeMillis() < deadlineMillis) {
        if (condition()) return
        Thread.sleep(10)
    }
    assertTrue(condition())
}

internal fun ByteArray.toPcm16Samples(): ShortArray =
    ShortArray(size / 2) { index ->
        val byteIndex = index * 2
        ((this[byteIndex].toInt() and 0xFF) or
            (this[byteIndex + 1].toInt() shl 8)).toShort()
    }

internal fun createZenToneForTest(
    audioSink: AudioSink,
    bufferSizeInBytes: Int = SINGLE_FRAME_BUFFER_SIZE_IN_BYTES,
    coroutineContext: CoroutineContext
): ZenTone =
    ZenTone(
        DEFAULT_SAMPLE_RATE,
        DEFAULT_ENCODING,
        DEFAULT_CHANNEL_MASK,
        { _: Int, _: Int, _: Int -> audioSink },
        {},
        bufferSizeInBytes,
        coroutineContext
    )
