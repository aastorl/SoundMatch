package com.example.soundmatch.domain

import android.app.appsearch.SearchResult
import com.example.soundmatch.domain.SearchResult.PlaylistSearchResult

/**
 * A domain class that consists of featured [playlists]. It also contains
 * a [playlistsDescription] that contains a very short description of the
 * playlists. For example, if the featured playlists are fetched at
 * noon, then it may contain a description such as "Afternoon delight"
 */

data class FeaturedPlaylists(
    val playlistsDescription: String,
    val playlists: List<SearchResult.PlaylistSearchResult>
)