package com.example.soundmatch.domain

import com.example.soundmatch.domain.SearchResult.AlbumSearchResult
import com.example.soundmatch.domain.SearchResult.ArtistSearchResult
import com.example.soundmatch.domain.SearchResult.EpisodeSearchResult
import com.example.soundmatch.domain.SearchResult.PlaylistSearchResult
import com.example.soundmatch.domain.SearchResult.PodcastSearchResult
import com.example.soundmatch.domain.SearchResult.TrackSearchResult

/**
 * A class that models a search result. It contains all the [tracks],
 * [albums],[artists] and [playlists] that matched a search query.
 */
data class SearchResults(
    val tracks: List<TrackSearchResult>,
    val albums: List<AlbumSearchResult>,
    val artists: List<ArtistSearchResult>,
    val playlists: List<PlaylistSearchResult>,
    val shows: List<PodcastSearchResult>,
    val episodes: List<EpisodeSearchResult>,
)

/**
 * A utility function that returns an instance of  [SearchResults] with
 * empty read-only lists.
 */
fun emptySearchResults() = SearchResults(
    tracks = emptyList(),
    albums = emptyList(),
    artists = emptyList(),
    playlists = emptyList(),
    shows = emptyList(),
    episodes = emptyList()
)