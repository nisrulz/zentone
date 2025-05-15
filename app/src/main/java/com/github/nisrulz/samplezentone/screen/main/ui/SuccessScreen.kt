package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.github.nisrulz.samplezentone.screen.main.ViewState
import com.github.nisrulz.samplezentone.ui.theme.AppTheme

@Composable
internal fun SuccessScreen(
    viewState: ViewState,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit = {},
    onFreqChange: (Float) -> Unit = {},
    onVolumeChange: (Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        FreqAndVolumeComponent(
            viewState = viewState,
            onFreqChange = onFreqChange,
            onVolumeChange = onVolumeChange,
            onValueChangeFinished = onValueChangeFinished
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )

        PlayStopButton(
            isPlaying = viewState.isPlaying,
            onClick = onFabClick
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        SuccessScreen(ViewState())
    }
}
