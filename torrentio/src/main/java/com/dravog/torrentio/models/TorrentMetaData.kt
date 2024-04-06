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
        info = TorrentInfo(value[INFO] as Map<String, Any>),
        announce = (value[ANNOUNCE] as ByteArray).decodeToString(),
        announceList = value.getOrDefault(
            ANNOUNCE_LIST,
            null
        )?.toList<ByteArray>()?.map { it.decodeToString() },
        creationDate = value.getOrDefault(CREATION_DATE, null) as Long?,
        comment = (value.getOrDefault(COMMENT, null) as ByteArray?)?.decodeToString(),
        createdBy = (value.getOrDefault(
            CREATED_BY,
            null
        ) as ByteArray?)?.decodeToString(),
        encoding = (value.getOrDefault(ENCODING, null) as ByteArray?)?.decodeToString(),
    )

    fun isMultiFile(): Boolean {
        return info.isMultiFile()
    }

    fun getInfoHash(): String {
        return info.toSha1Hash()
    }

    companion object {
        const val INFO = "info"
        const val ANNOUNCE = "announce"
        const val ANNOUNCE_LIST = "announce-list"
        const val CREATION_DATE = "creation date"
        const val COMMENT = "comment"
        const val CREATED_BY = "created by"
        const val ENCODING = "encoding"
    }
}