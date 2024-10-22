package com.example.soundmatch.data.repositories.homefeedrepository

import com.example.soundmatch.data.remote.musicservice.SpotifyService
import com.example.soundmatch.data.remote.response.toAlbumSearchResultList
import com.example.soundmatch.data.remote.response.toFeaturedPlaylists
import com.example.soundmatch.data.remote.response.toPlaylistSearchResultList
import com.example.soundmatch.data.repositories.tokenrepository.TokenRepository
import com.example.soundmatch.data.repositories.tokenrepository.runCatchingWithToken
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.FeaturedPlaylists
import com.example.soundmatch.domain.PlaylistsForCategory
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.SoundMatchErrorType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlin.collections.get

class SoundMatchHomeFeedRepository @Inject constructor(
    private val spotifyService: SpotifyService,
    private val tokenRepository: TokenRepository
) : HomeFeedRepository {
    override suspend fun fetchNewlyReleasedAlbums(
        countryCode: String
    ): FetchedResource<List<SearchResult.AlbumSearchResult>, SoundMatchErrorType> =
        tokenRepository.runCatchingWithToken { token ->
            spotifyService
                .getNewReleases(token = token, market = countryCode)
                .toAlbumSearchResultList()
        }


    override suspend fun fetchFeaturedPlaylistsForCurrentTimeStamp(
        timestampMillis: Long,
        countryCode: String,
        languageCode: ISO6391LanguageCode,
    ): FetchedResource<FeaturedPlaylists, SoundMatchErrorType> =
        tokenRepository.runCatchingWithToken { token ->
            val timestamp = ISODateTimeString.from(timestampMillis)
            spotifyService.getFeaturedPlaylists(
                token = token,
                market = countryCode,
                locale = "${languageCode.value}_$countryCode",
                timestamp = timestamp
            ).toFeaturedPlaylists()
        }

    override suspend fun fetchPlaylistsBasedOnCategoriesAvailableForCountry(
        countryCode: String,
        languageCode: ISO6391LanguageCode,
    ): FetchedResource<List<PlaylistsForCategory>, SoundMatchErrorType> =
        tokenRepository.runCatchingWithToken { token ->
            val locale = "${languageCode.value}_$countryCode"
            val categories = spotifyService.getBrowseCategories(
                token = token,
                market = countryCode,
                locale = locale
            ).categories.items
            coroutineScope {
                // instead of fetching playlists for each category in a sequential manner
                // fetch it in parallel
                val playlistsMap = categories.map { huh ->
                    async {
                        spotifyService.getPlaylistsForCategory(
                            token = token,
                            categoryId = huh.id,
                            market = countryCode
                        ).toPlaylistSearchResultList()
                    }
                }
                playlistsMap.awaitAll().mapIndexed { index, playlists ->
                    PlaylistsForCategory(
                        categoryId = categories[index].id,
                        nameOfCategory = categories[index].name,
                        associatedPlaylists = playlists
                    )
                }
            }
        }
}