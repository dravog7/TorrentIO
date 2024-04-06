package com.dravog.torrentio.utils.bencode

import java.io.OutputStream

class BencodeWriter(private val outputStream: OutputStream) {

    private fun writeAny(value: Any) {
        when (value) {
            is Long -> writeInteger(value)
            is ByteArray -> writeString(value)
            is List<*> -> writeList(value)
            is Map<*, *> -> writeMap(value as Map<String, *>)
        }
    }

    fun writeInteger(num: Long) {
        outputStream.write("i${num}e".encodeToByteArray())
    }

    fun writeString(str: ByteArray) {
        outputStream.write("${str.size}:".encodeToByteArray())
        outputStream.write(str)
    }

    fun writeList(list: List<*>) {
        outputStream.write("l".encodeToByteArray())
        list.forEach {
            writeAny(it!!)
        }
        outputStream.write("e".encodeToByteArray())
    }

    fun writeMap(map: Map<String, *>) {
        outputStream.write("d".encodeToByteArray())
        map.forEach { (key, value) ->
            writeAny(key.encodeToByteArray())
            writeAny(value!!)
        }
        outputStream.write("e".encodeToByteArray())
    }
}