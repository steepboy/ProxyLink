package me.yiski.parse

import kotlinx.serialization.json.Json
import me.yiski.Providers
import me.yiski.Utils
import org.jsoup.Jsoup
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import java.io.File
import java.io.IOException

class ProxyDB {
    private fun findExecutable(path: String): Boolean {
        return try {
            File(path).exists() && File(path).canExecute()
        } catch (e: IOException) {
            false
        }
    }

    private fun getDriver(): WebDriver {
        val chromePaths = listOf(
            "/usr/bin/chromium",
            "/usr/bin/google-chrome",
            "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
            "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
        )

        chromePaths.forEach { path ->
            if (findExecutable(path)) {
                val options = ChromeOptions()
                options.addArguments("--headless")
                return ChromeDriver(options)
            }
        }

        val firefoxPaths = listOf(
            "/usr/bin/firefox",
            "C:\\Program Files\\Mozilla Firefox\\firefox.exe",
            "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"
        )

        firefoxPaths.forEach { path ->
            if (findExecutable(path)) {
                val options = FirefoxOptions()
                options.addArguments("-headless")
                return FirefoxDriver(options)
            }
        }

        throw RuntimeException("Can't find drivers for Chromium or Firefox")
    }

    private fun getHtml(link: String): Set<Pair<String, Int>> {
        val driver = getDriver()
        try {
            driver.get(link)
            val html = driver.pageSource
            return parseProxies(html.toString())
        } finally {
            driver.quit()
        }
    }

    private fun parseProxies(html: String): Set<Pair<String, Int>> {
        val document = Jsoup.parse(html)
        val rows = document.select("table tbody tr")

        val proxies = mutableSetOf<Pair<String, Int>>()

        for (row in rows) {
            val ip = row.select("td a").first()?.text()
            val port = row.select("td a").last()?.text()

            if (ip != null && port != null) {
                proxies.add(ip to port.toInt())
            }
        }

        return proxies
    }

    fun get(protocol: String, countryCode: String): Set<Pair<String, Int>> {
        if (protocol !in Providers.validProtocols) println("Invalid protocol")

        val countries = Json.decodeFromString<Map<String, String>>(
            File(Utils().getUrlResourcePath("countries.json")).readText()
        )

        if (!countries.containsKey(countryCode)) {
            println("Error: Country code $countryCode not found")
        }

        val baseUrl = "https://proxydb.net/?protocol=${protocol.lowercase()}"
        val link = if (countryCode != "ANY") "$baseUrl&country=$countryCode" else baseUrl

        return getHtml(link)
    }
}