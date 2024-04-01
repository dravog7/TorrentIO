package com.dravog.torrentio.models

import com.dravog.torrentio.utils.Bencode
import java.io.Reader
import java.math.BigInteger

data class TorrentMetaData(
    val info: TorrentInfo,
    val announce: String,
    val announceList: List<String> = listOf(),
    val creationDate: BigInteger?, //TODO - alternative
    val comment: String?,
    val createdBy: String?,
    val encoding: String?
) {
    companion object {

        fun from(file: Reader): TorrentMetaData {
            return from(file.readText())
        }

        fun from(msg: String): TorrentMetaData {
            val value = Bencode().decode(msg) as Map<String, Any>
            return TorrentMetaData(
                info = TorrentInfo.from(value["info"] as Map<String, Any>),
                announce = value["announce"] as String,
                announceList = value.getOrDefault(
                    "announce-list",
                    listOf<String>()
                ) as List<String>,
                creationDate = value.getOrDefault("creation date", null) as BigInteger?,
                comment = value.getOrDefault("comment", null) as String?,
                createdBy = value.getOrDefault("created by", null) as String?,
                encoding = value.getOrDefault("encoding", null) as String?,
            )
        }
    }

    fun isMultiFile(): Boolean {
        return info.isMultiFile()
    }
}