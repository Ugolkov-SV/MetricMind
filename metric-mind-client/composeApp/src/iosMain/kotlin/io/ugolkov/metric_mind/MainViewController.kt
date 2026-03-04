package io.ugolkov.metric_mind

import androidx.compose.ui.window.ComposeUIViewController
import io.ugolkov.metric_mind.di.initKoin

@Suppress("unused")
fun MainViewController() {
    initKoin()

    ComposeUIViewController { AppScreen() }
}