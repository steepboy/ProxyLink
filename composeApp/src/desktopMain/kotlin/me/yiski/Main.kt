package me.yiski

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.net.URL

class Main {
    fun run() = application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ProxyLink",
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