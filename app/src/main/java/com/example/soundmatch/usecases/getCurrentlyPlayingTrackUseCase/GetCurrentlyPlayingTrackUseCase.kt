package com.example.soundmatch.usecases.getCurrentlyPlayingTrackUseCase

import com.example.soundmatch.domain.SearchResult
import kotlinx.coroutines.flow.Flow

interface GetCurrentlyPlayingTrackUseCase {
    val currentlyPlayingTrackStream:Flow<SearchResult.TrackSearchResult?>
}