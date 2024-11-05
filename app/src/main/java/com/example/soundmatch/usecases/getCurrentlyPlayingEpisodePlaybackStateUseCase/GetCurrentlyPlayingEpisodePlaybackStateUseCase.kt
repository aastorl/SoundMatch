package com.example.soundmatch.usecases.getCurrentlyPlayingEpisodePlaybackStateUseCase

import com.example.soundmatch.domain.PodcastEpisode

interface GetCurrentlyPlayingEpisodePlaybackStateUseCase {
    sealed interface PlaybackState {
        data class Playing(val playingEpisode: PodcastEpisode) : PlaybackState
        data class Paused(val pausedEpisode: PodcastEpisode) : PlaybackState
        object Loading : PlaybackState
        object Idle : PlaybackState
    }
}