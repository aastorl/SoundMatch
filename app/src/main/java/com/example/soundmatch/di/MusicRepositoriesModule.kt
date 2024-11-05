package com.example.soundmatch.di

import com.example.soundmatch.data.repositories.albumsrepository.AlbumsRepository
import com.example.soundmatch.data.repositories.albumsrepository.SoundMatchAlbumsRepository
import com.example.soundmatch.data.repositories.genresrepository.GenresRepository
import com.example.soundmatch.data.repositories.genresrepository.SoundMatchGenresRepository
import com.example.soundmatch.data.repositories.homefeedrepository.HomeFeedRepository
import com.example.soundmatch.data.repositories.homefeedrepository.SoundMatchHomeFeedRepository
import com.example.soundmatch.data.repositories.podcastsrepository.PodcastsRepository
import com.example.soundmatch.data.repositories.podcastsrepository.SoundMatchPodcastsRepository
import com.example.soundmatch.data.repositories.searchrepository.SearchRepository
import com.example.soundmatch.data.repositories.searchrepository.SoundMatchSearchRepository
import com.example.soundmatch.data.repositories.tracksrepository.SoundMatchTracksRepository
import com.example.soundmatch.data.repositories.tracksrepository.TracksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MusicRepositoriesModule {
    @Binds
    abstract fun bindTracksRepository(impl: SoundMatchTracksRepository): TracksRepository

    @Binds
    abstract fun bindAlbumsRepository(impl: SoundMatchAlbumsRepository): AlbumsRepository

    @Binds
    abstract fun bindGenresRepository(impl: SoundMatchGenresRepository): GenresRepository

    @Binds
    abstract fun bindSearchRepository(impl: SoundMatchSearchRepository): SearchRepository

    @Binds
    abstract fun bindHomeFeedRepository(impl: SoundMatchHomeFeedRepository): HomeFeedRepository

    @Binds
    abstract fun bindPodcastsRepository(impl: SoundMatchPodcastsRepository): PodcastsRepository
}