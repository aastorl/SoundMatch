package com.example.soundmatch.data.encoder

fun interface Base64Encoder {
    fun encodeToString(bytes: ByteArray): String
}