package com.dravog.torrentio.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.io.FileReader
import java.nio.charset.Charset
import kotlin.math.ceil

class TorrentMetaDataTest {

    companion object {
        @JvmStatic
        fun getTorrentFiles(): List<Arguments> {
            return listOf(
                arguments(
                    "src/test/testAssets/torrents/big-buck-bunny.torrent",
                    mapOf(
                        "info.name" to "Big Buck Bunny",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
                        "hash" to "dd8255ecdc7ca55fb0bbf81323d87062db1f6d1c",
                    )
                ),
                arguments(
                    "src/test/testAssets/torrents/cosmos-laundromat.torrent",
                    mapOf(
                        "info.name" to "Cosmos Laundromat",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
                        "hash" to "c9e15763f722f23e98a29decdfae341b98d53056"
                    )
                ),
                arguments(
                    "src/test/testAssets/torrents/sintel.torrent",
                    mapOf(
                        "info.name" to "Sintel",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
                        "hash" to "08ada5a7a6183aae1e09d831df6748d566095a10"
                    )
                ),
                arguments(
                    "src/test/testAssets/torrents/tears-of-steel.torrent",
                    mapOf(
                        "info.name" to "Tears of Steel",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
                        "hash" to "209c8226b299b308beaf2b9cd3fb49212dbd13ec"
                    )
                ),
                arguments(
                    "src/test/testAssets/torrents/wired-cd.torrent",
                    mapOf(
                        "info.name" to "The WIRED CD - Rip. Sample. Mash. Share",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
                        "hash" to "a88fda5954e89178c372716a6a78b8180ed4dad3"
                    )
                ),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("getTorrentFiles")
    fun readTorrentFile(filePath: String, values: Map<String, Any>) {
        var result: TorrentMetaData
        File(filePath).inputStream()
            .use {
                result = TorrentMetaData(it)
            }
        Assertions.assertEquals(values["info.name"], result.info.name)
        Assertions.assertEquals(values["announce"], result.announce)
        Assertions.assertEquals(values["isMultiFile"], result.isMultiFile())
        val totalSize = result.info.files.map { it.length }.reduce { a, b -> a + b }
        val expectedPieces =
            ceil(totalSize.toFloat() / result.info.pieceLength).toInt()
        Assertions.assertEquals(expectedPieces, result.info.pieces.count())
    }

    @ParameterizedTest
    @MethodSource("getTorrentFiles")
    fun correctSHA1Hash(filePath: String, values: Map<String, Any>) {
        var result: TorrentMetaData
        File(filePath).inputStream()
            .use {
                result = TorrentMetaData(it)
            }
        Assertions.assertEquals((values["hash"] as String), result.getInfoHash())
    }
}