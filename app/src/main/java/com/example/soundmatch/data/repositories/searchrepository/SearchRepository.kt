package com.example.soundmatch.data.repositories.searchrepository

import androidx.paging.PagingData
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.SearchResults
import com.example.soundmatch.domain.SoundMatchErrorType
import kotlinx.coroutines.flow.Flow

/**
 * A repository that contains all methods related to searching.
 */
interface SearchRepository {
    suspend fun fetchSearchResultsForQuery(
        searchQuery: String,
        countryCode: String
    ): FetchedResource<SearchResults, SoundMatchErrorType>

    fun getPaginatedSearchStreamForAlbums(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.AlbumSearchResult>>

    fun getPaginatedSearchStreamForArtists(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.ArtistSearchResult>>

    fun getPaginatedSearchStreamForTracks(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.TrackSearchResult>>

    fun getPaginatedSearchStreamForPlaylists(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.PlaylistSearchResult>>

    fun getPaginatedSearchStreamForPodcasts(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.PodcastSearchResult>>

    fun getPaginatedSearchStreamForEpisodes(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.EpisodeSearchResult>>
}