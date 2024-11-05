package com.example.soundmatch.usecases.getCurrentlyPlayingStreamableUseCase

import com.example.soundmatch.domain.Streamable
import kotlinx.coroutines.flow.Flow

interface GetCurrentlyPlayingStreamableUseCase {
    val currentlyPlayingStreamableStream: Flow<Streamable>
}