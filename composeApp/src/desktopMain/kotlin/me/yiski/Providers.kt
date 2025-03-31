package me.yiski

import me.yiski.parse.ProxyDB
import java.io.InputStreamReader
import java.net.*

class Providers {
    companion object {
        val validProtocols: List<String> = listOf("HTTP", "HTTPS", "SOCKS4", "SOCKS5")
    }

    private fun checkProxy(proxyHost: String, proxyPort: Int, protocol: String): Pair<String, String>? {
        return when (protocol.lowercase()) {
            "http", "https", "socks4", "socks5" -> checkWorkingProxy(proxyHost, proxyPort, protocol.lowercase())
            else -> throw RuntimeException("Invalid proxy protocol: $protocol")
        }
    }

    private fun checkWorkingProxy(
        proxyHost: String,
        proxyPort: Int,
        type: String,
        connectTimeout: Int = 3000,
        readTimeout: Int = 3000
    ): Pair<String, String>? {
        return try {
            val socket = Socket()
            val proxy = Proxy(
                if (type in setOf("socks4", "socks5")) Proxy.Type.SOCKS else Proxy.Type.HTTP,
                InetSocketAddress(proxyHost, proxyPort)
            )
            socket.connect(InetSocketAddress(proxyHost, proxyPort))
            socket.close()

            val url = URL("http://ifconfig.me")
            val connection = url.openConnection(proxy) as HttpURLConnection
            connection.connectTimeout = connectTimeout
            connection.readTimeout = readTimeout
            connection.requestMethod = "GET"
            connection.connect()

            val reader = InputStreamReader(connection.inputStream)
            reader.close()

            Pair<String, String>(proxyHost, proxyPort.toString())
        } catch (e: Exception) {
            return null
        }
    }

    fun startWorkingProxy(): Boolean {
        val proxyList =
            ProxyDB().get(SelectionStorage.selectedProtocol.toString(), SelectionStorage.selectedCountry!!.code)

        if (proxyList.isEmpty()) return false

        var isProxyFound = false

        for ((proxyHost, proxyPort) in proxyList) {
            val result = Providers().checkProxy(proxyHost, proxyPort, SelectionStorage.selectedProtocol.toString())
            if (result != null) {
                val command: String = "chromium --proxy-server=${
                    SelectionStorage.selectedProtocol.toString().lowercase()
                }://${result.first}:${result.second}"

                Runtime.getRuntime().exec(command)

                isProxyFound = true
                break
            }
        }

        return isProxyFound
    }
}

