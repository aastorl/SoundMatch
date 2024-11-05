package com.example.soundmatch.di

import com.example.soundmatch.usecases.downloadDrawableFromUrlCase.DownloadDrawableFromUrlUseCase
import com.example.soundmatch.usecases.downloadDrawableFromUrlCase.SoundMatchDownloadDrawableFromUrlUseCase
import com.example.soundmatch.usecases.getCurrentlyPlayingTrackUseCase.GetCurrentlyPlayingTrackUseCase
import com.example.soundmatch.usecases.getCurrentlyPlayingTrackUseCase.SoundMatchGetCurrentlyPlayingTrackUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TrackUseCasesComponent {
    @Binds
    abstract fun bindDownloadDrawableFromUrlUseCase(
        impl: SoundMatchDownloadDrawableFromUrlUseCase
    ): DownloadDrawableFromUrlUseCase

    @Binds
    abstract fun bindGetCurrentlyPlayingTrackUseCase(
        impl: SoundMatchGetCurrentlyPlayingTrackUseCase
    ): GetCurrentlyPlayingTrackUseCase
}