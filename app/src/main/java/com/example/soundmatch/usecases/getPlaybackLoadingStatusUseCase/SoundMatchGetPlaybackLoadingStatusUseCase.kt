package com.example.soundmatch.usecases.getPlaybackLoadingStatusUseCase

import com.example.soundmatch.musicplayer.MusicPlayerV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SoundMatchGetPlaybackLoadingStatusUseCase @Inject constructor(
    musicPlayerV2: MusicPlayerV2
) : GetPlaybackLoadingStatusUseCase {
    override val loadingStatusStream: Flow<Boolean> = musicPlayerV2.currentPlaybackStateStream
        .map { it is MusicPlayerV2.PlaybackState.Loading }
        .distinctUntilChanged()
}