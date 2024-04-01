package com.dravog.torrentio.utils

import info.skyblond.bencode.BEntry
import info.skyblond.bencode.decoder.BencodeReader
import info.skyblond.bencode.encoder.BencodeWriter
import java.io.StringWriter

class Bencode {

    fun encode(value: Any): String {
        val writer = StringWriter()
        BencodeWriter(writer).write(value)
        return writer.toString()
    }

    fun decode(msg: String): Any {
        val reader = BencodeReader(msg.reader())
        return when (reader.nextType()) {
            BEntry.BInteger -> reader.readInteger()
            BEntry.BString -> reader.readString()
            BEntry.BList -> reader.readList()
            BEntry.BMap -> reader.readMap()
            else -> error("Impossible type")
        }
    }
}