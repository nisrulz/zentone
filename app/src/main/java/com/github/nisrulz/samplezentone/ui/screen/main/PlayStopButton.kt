package com.github.nisrulz.samplezentone.ui.screen.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.github.nisrulz.samplezentone.R


@Composable
internal fun PlayStopButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        if (isPlaying) {
            Icon(
                painterResource(R.drawable.stop),
                "Stop",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            Icon(Icons.Filled.PlayArrow, "Play")
        }
    }
}
