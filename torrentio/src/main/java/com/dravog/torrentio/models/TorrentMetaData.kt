package com.dravog.torrentio.models

import com.dravog.torrentio.utils.BencodeReader
import com.dravog.torrentio.utils.TypeCastHelpers.toList
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
    companion object {
        fun from(stream: InputStream): TorrentMetaData {
            val value = BencodeReader(stream).readAny() as Map<String, Any>
            return TorrentMetaData(
                info = TorrentInfo.from(value["info"] as Map<String, Any>),
                announce = (value["announce"] as ByteArray).decodeToString(),
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
        }
    }

    fun isMultiFile(): Boolean {
        return info.isMultiFile()
    }
}