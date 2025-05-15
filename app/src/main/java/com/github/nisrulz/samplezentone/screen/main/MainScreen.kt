package com.github.nisrulz.samplezentone.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.github.nisrulz.samplezentone.screen.main.ui.LoadingScreen
import com.github.nisrulz.samplezentone.screen.main.ui.PlayStopButton
import com.github.nisrulz.samplezentone.screen.main.ui.SuccessScreen
import com.github.nisrulz.samplezentone.ui.theme.AppTheme
import com.github.nisrulz.samplezentone.ui.theme.AppTheme.dimension

@Composable
internal fun MainScreen(
    viewState: ViewState,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onFabClick: () -> Unit,
    onFreqChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            PlayStopButton(
                modifier = Modifier.padding(dimension.dimen16),
                isPlaying = viewState.isPlaying,
                onClick = onFabClick
            )
        }
    ) { innerPadding ->
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
                    onVolumeChange = onVolumeChange,
                    onFreqChange = onFreqChange,
                    onValueChangeFinished = onValueChangeFinished
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun Preview() {
    AppTheme {
        val viewState = ViewState()
        MainScreen(
            viewState = viewState,
            snackBarHostState = remember { SnackbarHostState() },
            onFabClick = {},
            onVolumeChange = {},
            onFreqChange = {},
            onValueChangeFinished = {}
        )
    }
}
