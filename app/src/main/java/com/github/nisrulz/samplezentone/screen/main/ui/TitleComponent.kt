package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.github.nisrulz.samplezentone.ui.theme.AppTheme

@Composable
internal fun TitleComponent(title: String, fontSize: TextUnit = 24.sp) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        fontSize = fontSize,
        color = Color.Gray
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        TitleComponent("Some Title Text")
    }
}
