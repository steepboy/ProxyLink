package me.yiski

import App
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ProxyLink",
        state = rememberWindowState(width = 370.dp, height = 480.dp),
        resizable = false
    ) {
        App()
    }
}