package com.github.nisrulz.samplezentone.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.nisrulz.samplezentone.ui.theme.ZenToneProjectTheme


@Composable
fun MainScreen(modifier: Modifier = Modifier, onFabClick: () -> Unit = {}) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(Icons.Default.PlayArrow, "Play")
            }
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                FreqCard()
                VolumeCard()
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    ZenToneProjectTheme {
        MainScreen()
    }
}

@Composable
private fun FreqCard() {
    CardViewContent(title = "Frequency (Hz)", onValueChange = {})
}

@Composable
private fun VolumeCard() {
    CardViewContent(title = "Volume", onValueChange = {})
}

@Composable
private fun CardViewContent(
    title: String,
    onValueChange: (String) -> Unit,
) {
    Card(shape = RoundedCornerShape(4.dp)) {
        Column {
            TextViewContent(title)
            EditTextContent(onValueChange)
            SeekBarContent()
        }
    }
}


@Composable
fun TextViewContent(title: String) {
    Text(
        text = title,
        fontSize = 36.sp,
        color = Color.Gray
    )
}

@Composable
fun EditTextContent(onValueChange: (String) -> Unit) {
    TextField(
        value = "0",
        onValueChange = onValueChange
    )
}

@Composable
fun SeekBarContent() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it }
    )
}