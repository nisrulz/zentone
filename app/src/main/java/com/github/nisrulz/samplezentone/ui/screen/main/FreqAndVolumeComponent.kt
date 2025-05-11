package com.github.nisrulz.samplezentone.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.nisrulz.samplezentone.R

@Composable
internal fun FreqAndVolumeComponent(
    viewState: ViewState,
    onFreqChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .size(56.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            colorFilter = ColorFilter.tint(
                MaterialTheme.colorScheme.primary
            )
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
        FreqCard(value = viewState.freq, onValueChange = {
            onFreqChange(it)
        }, onValueChangeFinished = onValueChangeFinished)

        VolumeCard(value = viewState.volume, onValueChange = {
            onVolumeChange(it)
        }, onValueChangeFinished = onValueChangeFinished)
        Spacer(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
        )
        WaveAnimation(viewState.isPlaying)
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
    }
}


@Composable
private fun FreqCard(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TitleComponent("Frequency (Hz) [0-21000]")
            ValueText(value.toString())
            SeekBarComponent(valueRange = 0f..21000f, onValueChange, onValueChangeFinished)
        }
    }
}

@Composable
private fun VolumeCard(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TitleComponent("Volume [0-100]")
            ValueText(value.toInt().toString())
            SeekBarComponent(valueRange = 0f..100f, onValueChange, onValueChangeFinished)
        }
    }
}

@Composable
private fun TitleComponent(title: String, fontSize: TextUnit = 24.sp) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        fontSize = fontSize,
        color = Color.Gray,
    )
}

@Composable
private fun ValueText(value: String) {
    Text(
        text = value,
        fontSize = 48.sp,

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
        composition = composition,
        modifier = Modifier
            .fillMaxWidth(),
        iterations = LottieConstants.IterateForever
    )
}

@Composable
private fun SeekBarComponent(
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