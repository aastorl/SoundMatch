package com.example.soundmatch.data.remote.response

import com.example.soundmatch.data.utils.MapperImageSize
import com.example.soundmatch.data.utils.getImageResponseForImageSize
import com.example.soundmatch.domain.PodcastShow
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A response object that contains information about a specific show.
 */
data class ShowResponse(
    val id: String,
    val name: String,
    val publisher: String,
    @JsonProperty("html_description") val htmlDescription: String,
    val images: List<ImageResponse>
)

/**
 * A mapper function used to map an instance of [ShowResponse] to an instance
 * of [PodcastShow].
 */
fun ShowResponse.toPodcastShow() = PodcastShow(
    id = id,
    name = name,
    imageUrlString = images.getImageResponseForImageSize(MapperImageSize.LARGE).url,
    nameOfPublisher = publisher,
    htmlDescription = htmlDescription
)