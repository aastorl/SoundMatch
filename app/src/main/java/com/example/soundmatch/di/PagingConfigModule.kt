package com.example.soundmatch.di

import androidx.paging.PagingConfig
import com.example.soundmatch.data.paging.SpotifyPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PagingConfigModule {
    @Provides
    fun provideDefaultPagingConfig() = PagingConfig(
        pageSize = SpotifyPagingSource.DEFAULT_PAGE_SIZE,
        initialLoadSize = SpotifyPagingSource.DEFAULT_PAGE_SIZE
    )
}