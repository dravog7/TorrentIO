package com.dravog.torrentio.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class BencodeTest {

    @ParameterizedTest
    @MethodSource("getParameters")
    fun encode(value: Any, expected: String) {
        Assertions.assertEquals(expected, Bencode().encode(value))
    }

    @ParameterizedTest
    @MethodSource("getParameters")
    fun decode(expected: Any, msg: String) {
        Assertions.assertEquals(expected.toString(), Bencode().decode(msg).toString())
    }

    companion object {
        @JvmStatic
        fun getParameters(): List<Arguments> {
            return listOf(
                arguments("hello world", "11:hello world"),
                arguments(1,"i1e"),
                arguments(listOf(1,2,3),"li1ei2ei3ee"),
                arguments(mapOf(1 to 2),"d1:1i2ee"),
                arguments(mapOf("d" to "d","l" to "l"),"d1:d1:d1:l1:le"),
                arguments(listOf("l","d","e","f"),"l1:l1:d1:e1:fe"),
            )
        }
    }
}