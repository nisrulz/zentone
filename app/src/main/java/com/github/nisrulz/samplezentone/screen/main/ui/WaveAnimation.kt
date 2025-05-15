package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.nisrulz.samplezentone.R

@Composable
internal fun WaveAnimation(isPlaying: Boolean, modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.waveform_anim))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = LottieConstants.IterateForever
    )

    FadeInLottieAnimation(
        composition = composition,
        modifier = modifier,
        placeholderHeight = 90.dp
    ) {
        LottieAnimation(
            progress = { progress },
            composition = composition,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
