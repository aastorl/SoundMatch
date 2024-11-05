package com.example.soundmatch.data.repositories.tracksrepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.soundmatch.data.paging.PlaylistTracksPagingSource
import com.example.soundmatch.data.remote.musicservice.SpotifyService
import com.example.soundmatch.data.remote.response.getTracks
import com.example.soundmatch.data.remote.response.toTrackSearchResult
import com.example.soundmatch.data.repositories.tokenrepository.TokenRepository
import com.example.soundmatch.data.repositories.tokenrepository.runCatchingWithToken
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.Genre
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.SoundMatchErrorType
import com.example.soundmatch.domain.toSupportedSpotifyGenreType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SoundMatchTracksRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService,
    private val pagingConfig: PagingConfig
) : TracksRepository {
    override suspend fun fetchTopTenTracksForArtistWithId(
        artistId: String,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, SoundMatchErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getTopTenTracksForArtistWithId(
                artistId = artistId,
                market = countryCode,
                token = it,
            ).value.map { trackDTOWithAlbumMetadata ->
                trackDTOWithAlbumMetadata.toTrackSearchResult()
            }
        }

    override suspend fun fetchTracksForGenre(
        genre: Genre,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, SoundMatchErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getTracksForGenre(
                genre = genre.genreType.toSupportedSpotifyGenreType(),
                market = countryCode,
                token = it
            ).value.map { trackDTOWithAlbumMetadata ->
                trackDTOWithAlbumMetadata.toTrackSearchResult()
            }
        }

    override suspend fun fetchTracksForAlbumWithId(
        albumId: String,
        countryCode: String
    ): FetchedResource<List<SearchResult.TrackSearchResult>, SoundMatchErrorType> =
        tokenRepository.runCatchingWithToken {
            spotifyService.getAlbumWithId(albumId, countryCode, it).getTracks()
        }

    override fun getPaginatedStreamForPlaylistTracks(
        playlistId: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.TrackSearchResult>> = Pager(pagingConfig) {
        PlaylistTracksPagingSource(
            playlistId = playlistId,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow
}