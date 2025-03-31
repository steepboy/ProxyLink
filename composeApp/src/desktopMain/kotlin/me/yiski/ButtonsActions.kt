package me.yiski

import java.awt.Desktop
import java.io.IOException
import java.net.URI

class ButtonsActions {
    fun openLinkInBrowser(url: String) {
        try {
            when {
                System.getProperty("os.name").contains("Linux") -> {
                    val command = "xdg-open $url"
                    val process = Runtime.getRuntime().exec(command)
                    process.waitFor()
                }

                else -> {
                    if (Desktop.isDesktopSupported()) {
                        val desktop = Desktop.getDesktop()
                        desktop.browse(URI(url))
                    } else {
                        println("Desktop not supported")
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}