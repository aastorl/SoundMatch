package com.example.soundmatch.data.repositories.podcastsrepository

import androidx.paging.PagingData
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.PodcastEpisode
import com.example.soundmatch.domain.PodcastShow
import com.example.soundmatch.domain.SoundMatchErrorType
import kotlinx.coroutines.flow.Flow

/**
 * A repository that contains all methods related to podcasts.
 */
interface PodcastsRepository {
    suspend fun fetchPodcastEpisode(
        episodeId: String,
        countryCode: String
    ): FetchedResource<PodcastEpisode, SoundMatchErrorType>

    suspend fun fetchPodcastShow(
        showId: String,
        countryCode: String
    ): FetchedResource<PodcastShow, SoundMatchErrorType>

    fun getPodcastEpisodesStreamForPodcastShow(
        showId: String,
        countryCode: String
    ): Flow<PagingData<PodcastEpisode>>
}