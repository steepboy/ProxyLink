package me.yiski

import java.net.URL

class Utils {
    fun getUrlResourcePath(resourceName: String): String {
        val resource: URL? = javaClass.classLoader.getResource(resourceName)
        if (resource != null) {
            return resource.toURI().path
        }
        throw IllegalArgumentException("Resource $resourceName not found")
    }
}