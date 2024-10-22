package com.example.soundmatch.data.remote.response

import com.example.soundmatch.data.utils.MapperImageSize
import com.example.soundmatch.data.utils.getImageResponseForImageSize
import com.example.soundmatch.domain.SearchResult.ArtistSearchResult

/**
 * A response object that contains information about an Artist.
 */
data class ArtistResponse(
    val id: String,
    val name: String,
    val images: List<ImageResponse>,
    val followers: Followers
) {
    /**
     * A response class that holds the number of followers that follow
     * an Artist.
     */
    data class Followers(val total: String)
}

/**
 * A mapper function used to map an instance of [ArtistResponse] to
 * an instance of [ArtistSearchResult].
 */
fun ArtistResponse.toArtistSearchResult() = ArtistSearchResult(
    id = id,
    name = name,
    imageUrlString = if (images.isEmpty()) null
    else if (images.size != 3) images.first().url
    else images.getImageResponseForImageSize(MapperImageSize.LARGE).url
)

