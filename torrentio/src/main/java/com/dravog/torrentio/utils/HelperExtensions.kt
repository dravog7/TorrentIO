package com.dravog.torrentio.utils

object HelperExtensions {
    inline fun <reified T> Map<String, Any>.getAs(key: String, default: T): T {
        return this.getOrDefault(key, default) as T
    }
}