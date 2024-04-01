package com.dravog.torrentio.models

import com.dravog.torrentio.utils.TypeCastHelpers.toList
import com.dravog.torrentio.utils.bencode.BencodeReader
import java.io.InputStream

data class TorrentMetaData(
    val info: TorrentInfo,
    val announce: String,
    val announceList: List<String>?,
    val creationDate: Long?, //TODO - alternative
    val comment: String?,
    val createdBy: String?,
    val encoding: String?
) {
    constructor(stream: InputStream) : this(BencodeReader(stream).readAny() as Map<String, Any>)

    constructor(msg: String) : this(BencodeReader(msg.byteInputStream()).readAny() as Map<String, Any>)

    constructor(value: Map<String, Any>) : this(
        info = TorrentInfo(value["info"] as Map<String, Any>),
        announceList = value.getOrDefault(
            "announce-list",
            null
        )?.toList<ByteArray>()?.map { it.decodeToString() },
        creationDate = value.getOrDefault("creation date", null) as Long?,
        comment = (value.getOrDefault("comment", null) as ByteArray?)?.decodeToString(),
        createdBy = (value.getOrDefault(
            "created by",
            null
        ) as ByteArray?)?.decodeToString(),
        encoding = (value.getOrDefault("encoding", null) as ByteArray?)?.decodeToString(),
    )

    fun isMultiFile(): Boolean {
        return info.isMultiFile()
    }

    fun getInfoHash(): String {
        return info.toSha1Hash()
    }
}