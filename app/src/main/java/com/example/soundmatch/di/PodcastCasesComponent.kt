package com.example.soundmatch.di

import com.example.soundmatch.usecases.getCurrentlyPlayingEpisodePlaybackStateUseCase.GetCurrentlyPlayingEpisodePlaybackStateUseCase
import com.example.soundmatch.usecases.getCurrentlyPlayingEpisodePlaybackStateUseCase.SoundMatchGetCurrentlyPlayingEpisodePlaybackStateUseCase
import com.example.soundmatch.usecases.getCurrentlyPlayingStreamableUseCase.GetCurrentlyPlayingStreamableUseCase
import com.example.soundmatch.usecases.getCurrentlyPlayingStreamableUseCase.SoundMatchGetCurrentlyPlayingStreamableUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PodcastUseCasesComponent {
    @Binds
    abstract fun bindGetCurrentlyPlayingStreamableUseCase(
        impl: SoundMatchGetCurrentlyPlayingStreamableUseCase
    ): GetCurrentlyPlayingStreamableUseCase

    @Binds
    abstract fun bindGetEpisodePlaybackStateUseCase(
        impl: SoundMatchGetCurrentlyPlayingEpisodePlaybackStateUseCase
    ): GetCurrentlyPlayingEpisodePlaybackStateUseCase
}