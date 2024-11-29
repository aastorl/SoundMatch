package com.example.soundmatch.data.paging

import com.example.soundmatch.data.remote.musicservice.SpotifyService
import com.example.soundmatch.data.remote.response.toPodcastEpisode
import com.example.soundmatch.data.repositories.tokenrepository.TokenRepository
import com.example.soundmatch.domain.PodcastEpisode
import retrofit2.HttpException
import java.io.IOException

class PodcastEpisodesForPodcastShowPagingSource(
    showId: String,
    countryCode: String,
    tokenRepository: TokenRepository,
    spotifyService: SpotifyService
) : SpotifyPagingSource<PodcastEpisode>(
    loadBlock = { limit, offset ->
        try {
            val showResponse = spotifyService.getShowWithId(
                token = tokenRepository.getValidBearerToken(),
                id = showId,
                market = countryCode,
            )
            val episodes = spotifyService.getEpisodesForShowWithId(
                token = tokenRepository.getValidBearerToken(),
                id = showId,
                market = countryCode,
                limit = limit,
                offset = offset
            )
                .items
                .map {
                    it.toPodcastEpisode(showResponse)
                }
            SpotifyLoadResult.PageData(episodes)
        } catch (httpException: HttpException) {
            SpotifyLoadResult.Error(httpException)
        } catch (ioException: IOException) {
            SpotifyLoadResult.Error(ioException)
        }
    }
)