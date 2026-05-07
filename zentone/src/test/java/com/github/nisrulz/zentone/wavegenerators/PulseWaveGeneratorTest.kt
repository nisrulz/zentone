package com.github.nisrulz.zentone.wavegenerators

import com.github.nisrulz.zentone.DOUBLE_TOLERANCE
import com.github.nisrulz.zentone.UNIT_AMPLITUDE
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.PI

internal class PulseWaveGeneratorTest {

    @Test
    fun `pulse wave stays high only for the configured duty cycle`() {
        val pulseWaveGenerator = createGenerator()

        assertEquals(POSITIVE_RAIL, pulseWaveGenerator.calculateData(ZERO_ANGLE, UNIT_AMPLITUDE), DOUBLE_TOLERANCE)
        assertEquals(
            POSITIVE_RAIL,
            pulseWaveGenerator.calculateData((PI / 4.0) - DUTY_CYCLE_EPSILON, UNIT_AMPLITUDE),
            DUTY_CYCLE_EPSILON
        )
        assertEquals(NEGATIVE_RAIL, pulseWaveGenerator.calculateData(PI / 2.0, UNIT_AMPLITUDE), DOUBLE_TOLERANCE)
        assertEquals(NEGATIVE_RAIL, pulseWaveGenerator.calculateData(PI, UNIT_AMPLITUDE), DOUBLE_TOLERANCE)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `pulse wave rejects invalid duty cycles`() {
        PulseWaveGenerator(dutyCycle = INVALID_DUTY_CYCLE)
    }

    private fun createGenerator() = PulseWaveGenerator(dutyCycle = DUTY_CYCLE)

    private companion object {
        const val ZERO_ANGLE = 0.0
        const val DUTY_CYCLE = 0.25
        const val INVALID_DUTY_CYCLE = 0.0
        const val DUTY_CYCLE_EPSILON = 1e-9
        const val POSITIVE_RAIL = 1.0
        const val NEGATIVE_RAIL = -1.0
    }
}
