package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.nisrulz.samplezentone.ui.theme.AppTheme

@Composable
internal fun SeekBarComponent(
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Slider(
        value = sliderPosition,
        valueRange = valueRange,
        onValueChange = {
            sliderPosition = it
            onValueChange(it)
        },
        onValueChangeFinished = onValueChangeFinished
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        SeekBarComponent(
            onValueChange = {},
            onValueChangeFinished = {}
        )
    }
}
