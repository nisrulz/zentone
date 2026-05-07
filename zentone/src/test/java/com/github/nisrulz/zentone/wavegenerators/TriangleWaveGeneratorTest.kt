package com.github.nisrulz.zentone.wavegenerators

import com.github.nisrulz.zentone.DOUBLE_TOLERANCE
import com.github.nisrulz.zentone.UNIT_AMPLITUDE
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.PI

internal class TriangleWaveGeneratorTest {

    @Test
    fun `triangle wave is normalized to unit amplitude`() {
        val triangleWaveGenerator = createGenerator()

        assertEquals(POSITIVE_RAIL, triangleWaveGenerator.calculateData(PI / 2, UNIT_AMPLITUDE), DOUBLE_TOLERANCE)
        assertEquals(NEGATIVE_RAIL, triangleWaveGenerator.calculateData((3 * PI) / 2, UNIT_AMPLITUDE), DOUBLE_TOLERANCE)
    }

    private fun createGenerator() = TriangleWaveGenerator()

    private companion object {
        const val POSITIVE_RAIL = 1.0
        const val NEGATIVE_RAIL = -1.0
    }
}
