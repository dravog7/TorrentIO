package com.dravog.torrentio.utils.bencode

import com.dravog.torrentio.enums.BencodeType
import java.io.InputStream
import java.io.PushbackInputStream
import java.io.UnsupportedEncodingException

class BencodeReader(private val inputStream: InputStream) {
    private var stream = PushbackInputStream(inputStream)

    private fun nextType(): BencodeType {
        val firstChar = stream.read()
        stream.unread(firstChar)
        return when {
            firstChar == INTEGER_START -> BencodeType.INT
            firstChar == LIST_START -> BencodeType.LIST
            firstChar == MAP_START -> BencodeType.DICTIONARY
            firstChar.toChar().isDigit() -> BencodeType.STRING
            else -> throw UnsupportedEncodingException()
        }
    }

    fun readAny(): Any {
        return when (nextType()) {
            BencodeType.INT -> readInt()
            BencodeType.STRING -> readString()
            BencodeType.LIST -> readList()
            BencodeType.DICTIONARY -> readMap()
        }
    }

    private fun readInt(): Long { //TODO - have invalid leading zero
        stream.read()
        var num = 0L
        var negFlag = 1
        while (true) {
            val char = stream.read()
            if (char == END)
                break
            if (char == NEGATIVE_START) {
                negFlag = -1
                continue
            }
            num = num * 10 + char.toChar().digitToInt()
        }
        return negFlag * num
    }

    private fun readString(): ByteArray {
        var length = 0
        while (true) {
            val char = stream.read()
            if (char == COLON)
                break
            length = length * 10 + char.toChar().digitToInt()
        }
        val str = ByteArray(length)
        stream.read(str)
        return str
    }

    private fun readList(): List<Any> {
        stream.read()
        val list = mutableListOf<Any>()
        while (true) {
            val endChar = stream.read()
            if (endChar == END)
                break
            stream.unread(endChar)
            list.add(readAny())
        }
        return list
    }

    private fun readMap(): Map<String, Any> {
        stream.read()
        val map = mutableMapOf<String, Any>()
        while (true) {
            val endChar = stream.read()
            if (endChar == END)
                break
            stream.unread(endChar)
            val key = readString().decodeToString()
            val value = readAny()
            map[key] = value
        }
        return map
    }

    companion object {
        const val COLON = ':'.code
        const val INTEGER_START = 'i'.code
        const val LIST_START = 'l'.code
        const val MAP_START = 'd'.code
        const val END = 'e'.code
        const val NEGATIVE_START = '-'.code
    }

}