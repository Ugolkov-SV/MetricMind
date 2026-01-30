package io.ugolkov.metric_mind

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Metric Mind",
        alwaysOnTop = true,
    ) {
        App()
    }
}