package com.github.nisrulz.samplezentone.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.nisrulz.samplezentone.ui.theme.AppTheme


@Composable
internal fun MainScreen(
    viewState: ViewState,
    modifier: Modifier = Modifier,
    onFreqChange: (Float) -> Unit = {},
    onVolumeChange: (Float) -> Unit = {},
    onFabClick: () -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            PlayStopButton(
                isPlaying = viewState.isPlaying,
                onClick = onFabClick,
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewState.loading) {
                    LoadingScreen()
                } else {
                    SuccessScreen(
                        viewState = viewState,
                        onFreqChange = onFreqChange,
                        onVolumeChange = onVolumeChange,
                        onValueChangeFinished = onValueChangeFinished
                    )
                }
            }
        })
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        val viewState = ViewState()
        MainScreen(viewState)
    }
}

