package com.github.nisrulz.zentone.wavegenerators

import com.github.nisrulz.zentone.DOUBLE_TOLERANCE
import com.github.nisrulz.zentone.UNIT_AMPLITUDE
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.PI

internal class SawtoothWaveGeneratorTest {

    @Test
    fun `sawtooth wave ramps from minimum toward maximum over a cycle`() {
        val sawtoothWaveGenerator = createGenerator()

        assertEquals(MINIMUM_RAIL, sawtoothWaveGenerator.calculateData(ZERO_ANGLE, UNIT_AMPLITUDE), DOUBLE_TOLERANCE)
        assertEquals(ZERO_VALUE, sawtoothWaveGenerator.calculateData(PI, UNIT_AMPLITUDE), DOUBLE_TOLERANCE)
        assertEquals(HALF_SCALE_VALUE, sawtoothWaveGenerator.calculateData(1.5 * PI, UNIT_AMPLITUDE), DOUBLE_TOLERANCE)
    }

    private fun createGenerator() = SawtoothWaveGenerator()

    private companion object {
        const val ZERO_ANGLE = 0.0
        const val MINIMUM_RAIL = -1.0
        const val ZERO_VALUE = 0.0
        const val HALF_SCALE_VALUE = 0.5
    }
}
