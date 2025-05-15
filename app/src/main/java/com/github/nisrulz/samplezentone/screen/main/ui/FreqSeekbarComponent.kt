package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.nisrulz.samplezentone.ui.theme.AppTheme
import com.github.nisrulz.samplezentone.ui.theme.AppTheme.dimension

@Composable
internal fun FreqSeekbarComponent(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(dimension.dimen16)) {
            TitleComponent("Frequency (Hz) [0-21000]")
            ValueText(value.toString())
            SeekBarComponent(valueRange = 0f..21000f, onValueChange, onValueChangeFinished)
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        FreqSeekbarComponent(
            value = 200.0f,
            onValueChange = {},
            onValueChangeFinished = {}
        )
    }
}
