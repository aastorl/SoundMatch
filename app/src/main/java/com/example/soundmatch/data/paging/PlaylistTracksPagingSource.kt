package com.example.soundmatch.data.paging

import com.example.soundmatch.data.remote.musicservice.SpotifyService
import com.example.soundmatch.data.remote.response.toTrackSearchResult
import com.example.soundmatch.data.repositories.tokenrepository.TokenRepository
import com.example.soundmatch.domain.SearchResult
import retrofit2.HttpException
import java.io.IOException

class PlaylistTracksPagingSource(
    playlistId: String,
    countryCode: String,
    tokenRepository: TokenRepository,
    spotifyService: SpotifyService
) : SpotifyPagingSource<SearchResult.TrackSearchResult>(
    loadBlock = { limit, offset ->
        try {
            val data = spotifyService.getTracksForPlaylist(
                playlistId = playlistId,
                market = countryCode,
                token = tokenRepository.getValidBearerToken(),
                limit = limit,
                offset = offset
            ).items.map { it.track.toTrackSearchResult() }
            SpotifyLoadResult.PageData(data)
        } catch (httpException: HttpException) {
            SpotifyLoadResult.Error(httpException)
        } catch (ioException: IOException) {
            SpotifyLoadResult.Error(ioException)
        }
    }
)