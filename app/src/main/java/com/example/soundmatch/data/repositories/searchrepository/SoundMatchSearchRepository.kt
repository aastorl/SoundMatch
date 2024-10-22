package com.example.soundmatch.data.repositories.searchrepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.soundmatch.data.paging.*
import com.example.soundmatch.data.remote.musicservice.SpotifyService
import com.example.soundmatch.data.remote.response.toSearchResults
import com.example.soundmatch.data.repositories.tokenrepository.TokenRepository
import com.example.soundmatch.data.repositories.tokenrepository.runCatchingWithToken
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.SearchResults
import com.example.soundmatch.domain.SoundMatchErrorType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SoundMatchSearchRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService,
    private val pagingConfig: PagingConfig
) : SearchRepository {
    override suspend fun fetchSearchResultsForQuery(
        searchQuery: String,
        countryCode: String
    ): FetchedResource<SearchResults, SoundMatchErrorType> = tokenRepository.runCatchingWithToken {
        spotifyService.search(searchQuery, countryCode, it).toSearchResults()
    }

    override fun getPaginatedSearchStreamForAlbums(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.AlbumSearchResult>> = Pager(pagingConfig) {
        SpotifyAlbumSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForArtists(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.ArtistSearchResult>> = Pager(pagingConfig) {
        SpotifyArtistSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForTracks(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.TrackSearchResult>> = Pager(pagingConfig) {
        SpotifyTrackSearchPagingSources(
            searchQuery = searchQuery,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForPlaylists(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.PlaylistSearchResult>> = Pager(pagingConfig) {
        SpotifyPlaylistSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForPodcasts(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.PodcastSearchResult>> = Pager(pagingConfig) {
        SpotifyPodcastSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow

    override fun getPaginatedSearchStreamForEpisodes(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.EpisodeSearchResult>> = Pager(pagingConfig) {
        SpotifyEpisodeSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow
}