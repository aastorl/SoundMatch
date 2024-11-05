package com.example.soundmatch.di

import com.example.soundmatch.usecases.getPlaybackLoadingStatusUseCase.GetPlaybackLoadingStatusUseCase
import com.example.soundmatch.usecases.getPlaybackLoadingStatusUseCase.SoundMatchGetPlaybackLoadingStatusUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MusicPlayerUseCasesComponent {
    @Binds
    abstract fun bindGetPlaybackLoadingStatusUseCase(
        impl: SoundMatchGetPlaybackLoadingStatusUseCase
    ): GetPlaybackLoadingStatusUseCase
}