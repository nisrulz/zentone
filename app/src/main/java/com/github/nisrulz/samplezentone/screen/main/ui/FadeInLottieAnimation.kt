package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition

@Composable
internal fun FadeInLottieAnimation(
    composition: LottieComposition?,
    modifier: Modifier = Modifier,
    durationMillis: Int = 500,
    placeholderHeight: Dp = 100.dp,
    content: @Composable () -> Unit
) {
    val isVisible = composition != null
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = durationMillis)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(placeholderHeight) // Ensures space is occupied
    ) {
        if (!isVisible) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(placeholderHeight)
            )
        }

        Box(modifier = Modifier.alpha(alpha)) {
            content()
        }
    }
}
