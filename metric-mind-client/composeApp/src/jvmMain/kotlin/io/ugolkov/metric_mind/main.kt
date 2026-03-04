package io.ugolkov.metric_mind

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ugolkov.metric_mind.di.initKoin

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Metric Mind",
        alwaysOnTop = true,
    ) {
        AppScreen()
    }
}