package com.dravog.torrentio.models

import java.math.BigInteger

data class TorrentFile(
    val length: BigInteger,
    val md5sum: String?,
    val path: List<String>
) {
    companion object {
        fun from(map: Map<String, Any>): TorrentFile {
            return TorrentFile(
                map["length"] as BigInteger,
                map.getOrDefault("md5sum", null) as String?,
                map["path"] as List<String>
            )
        }
    }
}