package com.example.soundmatch.domain

import android.app.appsearch.SearchResult

data class FeaturedPlaylists(
    val playlists: List<SearchResult,PlaylistSearchResult>
)