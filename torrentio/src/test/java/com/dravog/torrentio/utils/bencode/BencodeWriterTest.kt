package com.dravog.torrentio.utils.bencode

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayOutputStream

class BencodeWriterTest {

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
    fun writeIntegerTest(expected: String, input: Long) {
        val outputStream = ByteArrayOutputStream()
        BencodeWriter(outputStream).writeInteger(input)
        Assertions.assertEquals(expected, outputStream.toByteArray().decodeToString())
    }

    @ParameterizedTest
    @MethodSource("getStringTestcases")
    fun writeStringTest(expected: String, input: ByteArray) {
        val outputStream = ByteArrayOutputStream()
        BencodeWriter(outputStream).writeString(input)
        Assertions.assertEquals(expected, outputStream.toByteArray().decodeToString())
    }

    @ParameterizedTest
    @MethodSource("getListTestcases")
    fun writeListTest(expected: String, input: List<*>) {
        val outputStream = ByteArrayOutputStream()
        BencodeWriter(outputStream).writeList(input)
        Assertions.assertEquals(expected, outputStream.toByteArray().decodeToString())
    }

    @ParameterizedTest
    @MethodSource("getMapTestcases")
    fun writeMapTest(expected: String, input: Map<String, *>) {
        val outputStream = ByteArrayOutputStream()
        BencodeWriter(outputStream).writeMap(input)
        Assertions.assertEquals(expected, outputStream.toByteArray().decodeToString())
    }

}