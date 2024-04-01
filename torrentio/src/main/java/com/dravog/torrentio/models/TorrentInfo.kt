package com.dravog.torrentio.models

import java.math.BigInteger

data class TorrentInfo(
    val pieceLength: BigInteger,
    val pieces: List<String>,//TODO - split and process
    val private: BigInteger?,
    val name: String,
    //Single File mode
    val length: BigInteger?,
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
                map["piece length"] as BigInteger,
                pieces = splitPieces(map["pieces"] as String),
                private = map.getOrDefault("private", BigInteger.ZERO) as BigInteger,
                name = map["name"] as String,
                length = map.getOrDefault("length", BigInteger.ZERO) as BigInteger,
                md5sum = map.getOrDefault("md5sum", "") as String,
                files = (map.getOrDefault(
                    "files",
                    listOf<Map<String, Any>>()
                ) as List<Map<String, Any>>).map { TorrentFile.from(it) }
            )
        }

        private fun splitPieces(pieces: String): List<String> {
            return pieces.chunked(20)
        }
    }
}