package com.dravog.torrentio.models

import com.dravog.torrentio.utils.HelperExtensions.getAs
import java.math.BigInteger

data class TorrentFile(
    val length: Long,
    val md5sum: String?,
    val path: List<String>
) {
    fun getMap(): Map<String, Any?> = mapOf(
        "length" to length,
        "md5sum" to md5sum,
        "path" to path
    )

    constructor(map: Map<String, Any>) : this(
        length = map["length"] as Long,
        md5sum = map.getAs<String?>("md5sum", null),
        path = (map["path"] as Iterable<ByteArray>).map { it.decodeToString() }
    )
}