package com.example.soundmatch.usecases.getPlaybackLoadingStatusUseCase

import kotlinx.coroutines.flow.Flow

interface GetPlaybackLoadingStatusUseCase {
    val loadingStatusStream: Flow<Boolean>
}