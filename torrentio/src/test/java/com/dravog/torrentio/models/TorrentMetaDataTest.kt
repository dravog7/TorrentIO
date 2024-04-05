package com.dravog.torrentio.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
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
                    )
                ),
                arguments(
                    "src/test/testAssets/torrents/cosmos-laundromat.torrent",
                    mapOf(
                        "info.name" to "Cosmos Laundromat",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
                    )
                ),
                arguments(
                    "src/test/testAssets/torrents/sintel.torrent",
                    mapOf(
                        "info.name" to "Sintel",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
                    )
                ),
                arguments(
                    "src/test/testAssets/torrents/tears-of-steel.torrent",
                    mapOf(
                        "info.name" to "Tears of Steel",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
                    )
                ),
                arguments(
                    "src/test/testAssets/torrents/wired-cd.torrent",
                    mapOf(
                        "info.name" to "The WIRED CD - Rip. Sample. Mash. Share",
                        "announce" to "udp://tracker.leechers-paradise.org:6969",
                        "isMultiFile" to true,
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
                result = TorrentMetaData.from(it)
            }
        Assertions.assertEquals(values["info.name"], result.info.name)
        Assertions.assertEquals(values["announce"], result.announce)
        Assertions.assertEquals(values["isMultiFile"], result.isMultiFile())
        val totalSize = result.info.files.map { it.length }.reduce { a, b -> a + b }
        val expectedPieces =
            ceil(totalSize.toFloat() / result.info.pieceLength).toInt()
        Assertions.assertEquals(expectedPieces, result.info.pieces.count())
    }
}