package io.ugolkov.metric_mind.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import io.ugolkov.metric_mind.ui.theme.AppTheme

@Preview(
    name = "Light",
    showBackground = true,
    backgroundColor = 0xFFF9F9FF,
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Night",
    showBackground = true,
    backgroundColor = 0xFF111319,
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
)
annotation class ThemePreview

@Composable
fun AppThemePreview(content: @Composable () -> Unit) {
    AppTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .safeContentPadding(),
            content = content
        )
    }
}