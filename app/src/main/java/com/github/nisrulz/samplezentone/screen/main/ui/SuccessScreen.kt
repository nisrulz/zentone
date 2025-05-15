package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.github.nisrulz.samplezentone.screen.main.ViewState
import com.github.nisrulz.samplezentone.ui.theme.AppTheme
import com.github.nisrulz.samplezentone.ui.theme.AppTheme.dimension

@Composable
internal fun SuccessScreen(
    viewState: ViewState,
    modifier: Modifier = Modifier,
    onFreqChange: (Float) -> Unit = {},
    onVolumeChange: (Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoComponent(Modifier.padding(top = dimension.dimen64))

        FreqSeekbarComponent(
            modifier = Modifier.padding(dimension.dimen16),
            value = viewState.freq,
            onValueChange = {
                onFreqChange(it)
            },
            onValueChangeFinished = onValueChangeFinished
        )

        VolumeSeekbarComponent(
            modifier = Modifier.padding(horizontal = dimension.dimen16),
            value = viewState.volume,
            onValueChange = {
                onVolumeChange(it)
            },
            onValueChangeFinished = onValueChangeFinished
        )

        WaveAnimation(
            modifier = Modifier.padding(top = dimension.dimen32),
            isPlaying = viewState.isPlaying
        )
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun Preview() {
    AppTheme {
        SuccessScreen(ViewState())
    }
}
