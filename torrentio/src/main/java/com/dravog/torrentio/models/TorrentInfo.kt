package com.dravog.torrentio.models

import com.dravog.torrentio.utils.HelperExtensions.getAs
import com.dravog.torrentio.utils.bencode.BencodeWriter
import java.io.ByteArrayOutputStream
import java.security.MessageDigest

data class TorrentInfo(
    val pieceLength: Long,
    val pieces: List<ByteArray>,//TODO - split and process
    val private: Long?,
    val name: String,
    //Single File mode
    val length: Long?,
    val md5sum: String?,
    //Multi File mode
    val files: List<TorrentFile> = listOf(),
    ) {

    private var readMap: Map<String, Any> = mapOf()

    constructor(map: Map<String, Any>) : this(
        (map[PIECE_LENGTH] as Long),
        pieces = splitPieces(map[PIECES] as ByteArray),
        private = map.getAs<Long?>(PRIVATE, null)?.toLong(),
        name = (map[NAME] as ByteArray).decodeToString(),
        length = map.getAs<Long?>(LENGTH, null)?.toLong(),
        md5sum = map.getAs<String?>(MD5SUM, null),
        files = (map.getAs(FILES, listOf<Map<String, Any>>())).map { TorrentFile(it) },
    ) {
        readMap = map
    }

    fun isMultiFile(): Boolean {
        return length?.equals(0L) ?: true
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun toSha1Hash(): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val stream = ByteArrayOutputStream()
        BencodeWriter(stream).writeMap(readMap)
        return digest.digest(stream.toByteArray()).toHexString()
    }

    companion object {
        private fun splitPieces(pieces: ByteArray): List<ByteArray> {
            return (1..(pieces.size / 20)).map {
                pieces.sliceArray((20 * (it - 1))..<(20 * it))
            }
        }

        const val PIECE_LENGTH = "piece length"
        const val PIECES = "pieces"
        const val PRIVATE = "private"
        const val NAME = "name"
        const val LENGTH = "length"
        const val MD5SUM = "md5sum"
        const val FILES = "files"
    }
}