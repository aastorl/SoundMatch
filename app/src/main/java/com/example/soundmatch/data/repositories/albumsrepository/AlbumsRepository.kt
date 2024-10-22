package com.example.soundmatch.data.repositories.albumsrepository

import androidx.paging.PagingData
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.SoundMatchErrorType
import kotlinx.coroutines.flow.Flow

/**
 * A repository that contains methods related to albums. **All methods
 * of this interface will always return an instance of [SearchResult.AlbumSearchResult]**
 * encapsulated inside [FetchedResource.Success] if the resource was
 * fetched successfully. This ensures that the return value of all the
 * methods of [AlbumsRepository] will always return [SearchResult.AlbumSearchResult]
 * in the case of a successful fetch operation.
 */
interface AlbumsRepository {
    suspend fun fetchAlbumWithId(
        albumId: String,
        countryCode: String
    ): FetchedResource<SearchResult.AlbumSearchResult, SoundMatchErrorType>

    suspend fun fetchAlbumsOfArtistWithId(
        artistId: String,
        countryCode: String
    ): FetchedResource<List<SearchResult.AlbumSearchResult>, SoundMatchErrorType>

    fun getPaginatedStreamForAlbumsOfArtist(
        artistId: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.AlbumSearchResult>>
}
