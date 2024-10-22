package com.example.soundmatch.data.repositories.homefeedrepository

import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.FeaturedPlaylists
import com.example.soundmatch.domain.PlaylistsForCategory
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.SoundMatchErrorType

/**
 * An interface that contains the requisite methods required for a repository
 * that contains methods for fetching items to be displayed in the home
 * feed.
 */
interface HomeFeedRepository {
    suspend fun fetchFeaturedPlaylistsForCurrentTimeStamp(
        timestampMillis: Long,
        countryCode: String,
        languageCode: ISO6391LanguageCode,
    ): FetchedResource<FeaturedPlaylists, SoundMatchErrorType>

    suspend fun fetchPlaylistsBasedOnCategoriesAvailableForCountry(
        countryCode: String,
        languageCode: ISO6391LanguageCode,
    ): FetchedResource<List<PlaylistsForCategory>, SoundMatchErrorType>

    suspend fun fetchNewlyReleasedAlbums(
        countryCode: String
    ): FetchedResource<List<SearchResult.AlbumSearchResult>, SoundMatchErrorType>
}