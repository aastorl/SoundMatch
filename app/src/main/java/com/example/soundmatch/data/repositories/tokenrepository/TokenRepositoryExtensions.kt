package com.example.soundmatch.data.repositories.tokenrepository

import com.example.soundmatch.data.remote.token.BearerToken
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.SoundMatchErrorType
import com.example.soundmatch.domain.getAssociatedSoundMatchErrorType
import com.fasterxml.jackson.core.JacksonException
import retrofit2.HttpException
import java.io.IOException

/**
 * A utility function used to run the [block] with a token retrieved
 * from the [TokenRepository] instance. It returns an instance of
 * [FetchedResource.Success] if the block did not throw an exception.
 * If the block throws either - a [HttpException] or an [IOException],
 * then [FetchedResource.Failure] containing the corresponding exception
 * will be returned. Any other exception thrown by the [block]
 * **will not be caught**.
 */
suspend fun <R> TokenRepository.runCatchingWithToken(block: suspend (BearerToken) -> R): FetchedResource<R, SoundMatchErrorType> =
    try {
        FetchedResource.Success(block(getValidBearerToken()))
    } catch (httpException: HttpException) {
        FetchedResource.Failure(httpException.getAssociatedSoundMatchErrorType())
    } catch (ioException: IOException) {
        FetchedResource.Failure(
            if (ioException is JacksonException) SoundMatchErrorType.DESERIALIZATION_ERROR
            else SoundMatchErrorType.NETWORK_CONNECTION_FAILURE
        )
    }