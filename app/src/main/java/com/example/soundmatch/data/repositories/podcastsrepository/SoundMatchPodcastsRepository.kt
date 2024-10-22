package com.example.soundmatch.data.repositories.podcastsrepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.soundmatch.data.paging.PodcastEpisodesForPodcastShowPagingSource
import com.example.soundmatch.data.remote.musicservice.SpotifyService
import com.example.soundmatch.data.remote.response.toPodcastEpisode
import com.example.soundmatch.data.remote.response.toPodcastShow
import com.example.soundmatch.data.repositories.tokenrepository.TokenRepository
import com.example.soundmatch.data.repositories.tokenrepository.runCatchingWithToken
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.PodcastEpisode
import com.example.soundmatch.domain.SoundMatchErrorType
import com.example.soundmatch.domain.PodcastShow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SoundMatchPodcastsRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService,
    private val pagingConfig: PagingConfig
) : PodcastsRepository {

    override suspend fun fetchPodcastEpisode(
        episodeId: String,
        countryCode: String
    ): FetchedResource<PodcastEpisode, SoundMatchErrorType> = tokenRepository.runCatchingWithToken {
        spotifyService.getEpisodeWithId(
            token = it, id = episodeId, market = countryCode
        ).toPodcastEpisode()
    }

    override suspend fun fetchPodcastShow(
        showId: String,
        countryCode: String
    ): FetchedResource<PodcastShow, SoundMatchErrorType> = tokenRepository.runCatchingWithToken {
        spotifyService.getShowWithId(
            token = it, id = showId, market = countryCode
        ).toPodcastShow()
    }

    override fun getPodcastEpisodesStreamForPodcastShow(
        showId: String,
        countryCode: String
    ): Flow<PagingData<PodcastEpisode>> = Pager(pagingConfig) {
        PodcastEpisodesForPodcastShowPagingSource(
            showId = showId,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow
}