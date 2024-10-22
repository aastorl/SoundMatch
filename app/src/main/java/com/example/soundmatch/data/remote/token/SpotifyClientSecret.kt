package com.example.soundmatch.data.remote.token

import com.example.soundmatch.data.encoder.Base64Encoder
import com.example.soundmatch.BuildConfig

/**
 * A function that uses the [base64Encoder] to get an encoded
 * spotify client secret.
 */
fun getSpotifyClientSecret(base64Encoder: Base64Encoder): String {
    val clientId = BuildConfig.SPOTIFY_CLIENT_ID
    val clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
    val encodedString = base64Encoder.encodeToString("$clientId:$clientSecret".toByteArray())
    return "Basic $encodedString"
}