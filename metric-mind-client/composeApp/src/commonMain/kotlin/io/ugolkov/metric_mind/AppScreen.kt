package io.ugolkov.metric_mind

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.ugolkov.metric_mind.ui.main.MainScreen
import io.ugolkov.metric_mind.ui.theme.AppTheme

@Composable
@Preview
fun AppScreen() {
    AppTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .safeContentPadding(),
        ) {
            MainScreen()
        }
    }
}