package com.example.soundmatch.data.paging

import com.example.soundmatch.data.remote.musicservice.SpotifyService
import com.example.soundmatch.data.remote.musicservice.toAlbumSearchResult
import com.example.soundmatch.data.repositories.tokenrepository.TokenRepository
import com.example.soundmatch.domain.SearchResult
import retrofit2.HttpException
import java.io.IOException

class AlbumsOfArtistPagingSource(
    private val artistId: String,
    private val market: String,
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService
) : SpotifyPagingSource<SearchResult.AlbumSearchResult>(
    loadBlock = { limit, offset ->
        try {
            val albumsMetadataResponse = spotifyService.getAlbumsOfArtistWithId(
                artistId = artistId,
                market = market,
                token = tokenRepository.getValidBearerToken(),
                limit = limit,
                offset = offset,
            )
            val data = albumsMetadataResponse.items.map { it.toAlbumSearchResult() }
            SpotifyLoadResult.PageData(data)
        } catch (httpException: HttpException) {
            SpotifyLoadResult.Error(httpException)
        } catch (ioException: IOException) {
            SpotifyLoadResult.Error(ioException)
        }
    }
)