package com.dravog.torrentio.models

import com.dravog.torrentio.utils.HelperExtensions.getAs

data class TorrentFile(
    val length: Long,
    val md5sum: String?,
    val path: List<String>
) {
    constructor(map: Map<String, Any>) : this(
        length = map[LENGTH] as Long,
        md5sum = map.getAs<String?>(MD5SUM, null),
        path = (map[PATH] as Iterable<ByteArray>).map { it.decodeToString() }
    )

    companion object {
        const val LENGTH = "length"
        const val MD5SUM = "md5sum"
        const val PATH = "path"
    }
}