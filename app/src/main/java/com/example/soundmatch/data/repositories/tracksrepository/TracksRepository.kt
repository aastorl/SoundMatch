package com.example.soundmatch.data.repositories.tracksrepository

import androidx.paging.PagingData
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.Genre
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.SoundMatchErrorType
import kotlinx.coroutines.flow.Flow

/**
 * A repository that contains methods related to tracks. **All methods
 * of this interface will always return an instance of [SearchResult.TrackSearchResult]**
 * encapsulated inside [FetchedResource.Success] if the resource was
 * fetched successfully. This ensures that the return value of all the
 * methods of [TracksRepository] will always return [SearchResult.TrackSearchResult]
 * in the case of a successful fetch operation.
 */
interface TracksRepository {
    suspend fun fetchTopTenTracksForArtistWithId(
        artistId: String,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, SoundMatchErrorType>

    suspend fun fetchTracksForGenre(
        genre: Genre,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, SoundMatchErrorType>

    suspend fun fetchTracksForAlbumWithId(
        albumId: String,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, SoundMatchErrorType>

    fun getPaginatedStreamForPlaylistTracks(
        playlistId: String,
        countryCode: String,
    ): Flow<PagingData<SearchResult.TrackSearchResult>>
}