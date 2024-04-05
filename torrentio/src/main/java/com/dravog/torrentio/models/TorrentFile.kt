package com.dravog.torrentio.models

data class TorrentFile(
    val length: Long,
    val md5sum: String?,
    val path: List<String>
) {
    companion object {
        fun from(map: Map<String, Any>): TorrentFile {
            return TorrentFile(
                map["length"] as Long,
                map.getOrDefault("md5sum", null) as String?,
                (map["path"] as Iterable<ByteArray>).map { it.decodeToString() }
            )
        }
    }
}