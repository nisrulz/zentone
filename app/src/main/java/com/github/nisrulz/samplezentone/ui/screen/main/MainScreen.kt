package com.github.nisrulz.samplezentone.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.nisrulz.samplezentone.R
import com.github.nisrulz.samplezentone.ui.theme.AppTheme


@Composable
internal fun MainScreen(modifier: Modifier = Modifier, onFabClick: () -> Unit = {}) {

    var isPlaying by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isPlaying = !isPlaying
                onFabClick()
            }) {
                if (isPlaying) {
                    Icon(Icons.Filled.PlayArrow, "Play")
                } else {
                    Icon(
                        painterResource(R.drawable.stop),
                        "Stop",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .fillMaxWidth()
                )
                FreqCard()
                VolumeCard()
                WaveAnimation(isPlaying)
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                        .fillMaxWidth()
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
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
    Card(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextViewContent(title)
            EditTextContent(onValueChange)
            SeekBarContent()
        }
    }
}


@Composable
private fun TextViewContent(title: String) {
    Text(
        text = title,
        fontSize = 36.sp,
        color = Color.Gray
    )
}

@Composable
private fun EditTextContent(onValueChange: (String) -> Unit) {
    TextField(
        value = "0",
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        textStyle = TextStyle(
            fontSize = 96.sp
        ),
        colors = TextFieldDefaults.colors().copy(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
private fun WaveAnimation(isPlaying: Boolean) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.waveform_anim))
    LottieAnimation(
        isPlaying = isPlaying,
        modifier = Modifier.padding(top = 48.dp),
        composition = composition, iterations = LottieConstants.IterateForever
    )
}

@Composable
private fun SeekBarContent() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it }
    )
}