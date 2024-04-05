package com.dravog.torrentio.utils

object TypeCastHelpers {

    inline fun <reified T> Any.toList(): List<T> {
        if (this is List<*>)
            return this.filterIsInstance<T>()
        return emptyList()
    }
}