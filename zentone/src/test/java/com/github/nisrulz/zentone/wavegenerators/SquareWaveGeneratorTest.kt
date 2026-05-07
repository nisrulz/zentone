package com.github.nisrulz.zentone.wavegenerators

import com.github.nisrulz.zentone.UNIT_AMPLITUDE
import org.junit.Assert.assertEquals
import org.junit.Test

internal class SquareWaveGeneratorTest {

    @Test
    fun `square wave stays bipolar at zero crossing`() {
        val squareWaveGenerator = createGenerator()

        assertEquals(POSITIVE_RAIL, squareWaveGenerator.calculateData(ZERO_ANGLE, UNIT_AMPLITUDE), ZERO_TOLERANCE)
    }

    private fun createGenerator() = SquareWaveGenerator()

    private companion object {
        const val ZERO_ANGLE = 0.0
        const val POSITIVE_RAIL = 1.0
        const val ZERO_TOLERANCE = 0.0
    }
}
