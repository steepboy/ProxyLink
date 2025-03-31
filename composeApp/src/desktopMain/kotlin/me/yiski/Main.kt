package me.yiski

import App
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.net.URL

class Main {
    fun run() = application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ProxyLink",
            state = rememberWindowState(width = 370.dp, height = 480.dp),
            resizable = false
        ) {
            App()
        }
    }

    fun getUrlResourcePath(resourceName: String): String {
        val resource: URL? = javaClass.classLoader.getResource(resourceName)
        if (resource != null) {
            return resource.toURI().path
        }
        throw IllegalArgumentException("Resource $resourceName not found")
    }

}