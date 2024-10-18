package com.example.soundmatch.domain

import retrofit2.HttpException

/**
 * An enum that contains different error types associated with [HttpException.code].
 */

enum class SoundMatchErrorType {
    BAD_OR_EXPIRED_TOKEN,
    BAD_OAUTH_REQUEST,
    INVALID_REQUEST,
    RATE_LIMIT_EXCEEDED,
    UNKNOWN_ERROR,
    NETWORK_CONNECTION_FAILURE,
    RESOURCE_NOT_FOUND,
    DESERIALIZATION_ERROR
}
/**
 * An extension property on [retrofit2.HttpException] that indicates
 * the [SoundMatchErrorType] associated with the [retrofit2.HttpException.code]
 */
fun HttpException.getAssociatedSoundMatchErrorType() : SoundMatchErrorType =
    when (this.code()) {
        400 -> SoundMatchErrorType.INVALID_REQUEST
        401 -> SoundMatchErrorType.BAD_OR_EXPIRED_TOKEN
        403 -> SoundMatchErrorType.BAD_OAUTH_REQUEST
        429 -> SoundMatchErrorType.RATE_LIMIT_EXCEEDED
        404 -> SoundMatchErrorType.RESOURCE_NOT_FOUND
        else -> SoundMatchErrorType.UNKNOWN_ERROR
    }