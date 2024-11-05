package com.example.soundmatch.usecases.getCurrentlyPlayingTrackUseCase

import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.usecases.getCurrentlyPlayingStreamableUseCase.GetCurrentlyPlayingStreamableUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject

class SoundMatchGetCurrentlyPlayingTrackUseCase @Inject constructor(
    getCurrentlyPlayingStreamableUseCase: GetCurrentlyPlayingStreamableUseCase
) : GetCurrentlyPlayingTrackUseCase {
    @Suppress("RemoveExplicitTypeArguments")
    override val currentlyPlayingTrackStream: Flow<SearchResult.TrackSearchResult> =
        getCurrentlyPlayingStreamableUseCase
            .currentlyPlayingStreamableStream
            .filterIsInstance<SearchResult.TrackSearchResult>()
}