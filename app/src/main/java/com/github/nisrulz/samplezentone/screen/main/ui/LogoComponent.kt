package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.nisrulz.samplezentone.R
import com.github.nisrulz.samplezentone.ui.theme.AppTheme
import com.github.nisrulz.samplezentone.ui.theme.AppTheme.dimension

@Composable
internal fun LogoComponent(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimension.dimen32)
            .size(dimension.dimen56),
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        colorFilter = ColorFilter.tint(
            MaterialTheme.colorScheme.primary
        )
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        LogoComponent()
    }
}
