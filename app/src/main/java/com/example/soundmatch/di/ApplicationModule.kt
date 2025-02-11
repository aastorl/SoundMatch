package com.example.soundmatch.di

import com.example.soundmatch.data.encoder.AndroidBase64Encoder
import com.example.soundmatch.data.encoder.Base64Encoder
import com.example.soundmatch.data.repositories.tokenrepository.SpotifyTokenRepository
import com.example.soundmatch.data.repositories.tokenrepository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {
    @Binds
    abstract fun bindBase64Encoder(
        androidBase64Encoder: AndroidBase64Encoder
    ) : Base64Encoder
    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        spotifyTokenRepository: SpotifyTokenRepository
    ) : TokenRepository
}