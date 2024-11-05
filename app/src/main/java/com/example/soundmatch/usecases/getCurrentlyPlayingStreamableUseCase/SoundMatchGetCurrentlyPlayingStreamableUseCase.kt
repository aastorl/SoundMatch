package com.example.soundmatch.usecases.getCurrentlyPlayingStreamableUseCase

import com.example.soundmatch.domain.Streamable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SoundMatchGetCurrentlyPlayingStreamableUseCase @Inject constructor(
    musicPlayer: MusicPlayerV2
) : GetCurrentlyPlayingStreamableUseCase {
    override val currentlyPlayingStreamableStream: Flow<Streamable> = musicPlayer
        .currentPlaybackStateStream
        .filterIsInstance<MusicPlayerV2.PlaybackState.Playing>()
        .map { it.currentlyPlayingStreamable }
}