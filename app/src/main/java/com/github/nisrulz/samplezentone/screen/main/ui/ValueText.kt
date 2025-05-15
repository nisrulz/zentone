package com.github.nisrulz.samplezentone.screen.main.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.github.nisrulz.samplezentone.ui.theme.AppTheme.dimension

@Composable
internal fun ValueText(value: String) {
    Text(
        text = value,
        fontSize = 48.sp,

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimension.dimen8)
    )
}
