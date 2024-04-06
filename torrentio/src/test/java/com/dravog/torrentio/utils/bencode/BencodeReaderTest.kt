package com.dravog.torrentio.utils.bencode

import com.dravog.torrentio.utils.TypeCastHelpers.toList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class BencodeReaderTest {

    companion object {

        @JvmStatic
        fun getIntegerTestcases(): List<Arguments> {
            return listOf(
                Arguments.arguments("i0e", 0),
                Arguments.arguments("i12345e", 12345L),
                Arguments.arguments("i-95434e", -95434L),
            )
        }

        @JvmStatic
        fun getStringTestcases(): List<Arguments> {
            return listOf(
                Arguments.arguments("11:hello world", "hello world".encodeToByteArray()),
                Arguments.arguments("0:", "".encodeToByteArray()),
                Arguments.arguments("8:hell:did", "hell:did".encodeToByteArray())
            )
        }

        @JvmStatic
        fun getListTestcases(): List<Arguments> {
            return listOf(
                Arguments.arguments("le", listOf<Int>()),
                Arguments.arguments("li1ei2ei3ee", listOf(1L, 2L, 3L)),
                Arguments.arguments(
                    "l1:a1:b1:ce",
                    listOf(
                        "a".encodeToByteArray(),
                        "b".encodeToByteArray(),
                        "c".encodeToByteArray()
                    )
                ),
                Arguments.arguments("li12e5:abcdee", listOf(12L, "abcde".encodeToByteArray()))
            )
        }

        @JvmStatic
        fun getMapTestcases(): List<Arguments> {
            return listOf(
                Arguments.arguments("de", mapOf<String, Any>()),
                Arguments.arguments("d1:a5:abcdee", mapOf("a" to "abcde".encodeToByteArray())),
                Arguments.arguments("d1:ai567ee", mapOf("a" to 567L)),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("getIntegerTestcases")
    fun readIntegerTest(encoded: String, expected: Any) {
        Assertions.assertEquals(expected, BencodeReader(encoded.byteInputStream()).readAny())
    }

    @ParameterizedTest
    @MethodSource("getStringTestcases")
    fun readStringTest(encoded: String, expected: Any) {
        Assertions.assertArrayEquals(
            expected as ByteArray,
            BencodeReader(encoded.byteInputStream()).readAny() as ByteArray
        )
    }

    @ParameterizedTest
    @MethodSource("getListTestcases")
    fun readListTest(encoded: String, expected: List<Any>) {
        val actual = BencodeReader(encoded.byteInputStream()).readAny().toList<Any>()
        actual.zip(expected).forEach {
            val actualElement = it.first
            val expectedElement = it.second
            if (actualElement is Long)
                Assertions.assertEquals(expectedElement, actualElement)
            if (actualElement is ByteArray)
                Assertions.assertArrayEquals(expectedElement as ByteArray, actualElement)
        }
    }

    @ParameterizedTest
    @MethodSource("getMapTestcases")
    fun readMapTest(encoded: String, expected: Map<String, Any>) {
        val actual = BencodeReader(encoded.byteInputStream()).readAny() as Map<String, Any>
        expected.forEach { (key, value) ->
            if (value is Long)
                Assertions.assertEquals(actual[key], value)
            if (value is ByteArray)
                Assertions.assertArrayEquals(actual[key] as ByteArray, value)
        }
    }
}