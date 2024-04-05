package com.dravog.torrentio.models

import java.math.BigInteger

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
    fun isMultiFile(): Boolean {
        return length?.equals(BigInteger.ZERO) ?: true
    }

    companion object {
        fun from(map: Map<String, Any>): TorrentInfo {
            return TorrentInfo(
                map["piece length"] as Long,
                pieces = splitPieces(map["pieces"] as ByteArray),
                private = map.getOrDefault("private", null) as Long?,
                name = (map["name"] as ByteArray).decodeToString(),
                length = map.getOrDefault("length", null) as Long?,
                md5sum = (map.getOrDefault("md5sum", null) as ByteArray?)?.decodeToString(),
                files = (map.getOrDefault(
                    "files",
                    listOf<Map<String, Any>>()
                ) as Iterable<Map<String, Any>>).map { TorrentFile.from(it) }
            )
        }

        private fun splitPieces(pieces: ByteArray): List<ByteArray> {
            return (1..(pieces.size / 20)).map {
                pieces.sliceArray((20 * (it - 1))..<(20 * it))
            }
        }
    }
}