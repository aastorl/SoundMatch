package com.example.soundmatch.ui.screens.searchscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.ui.components.DefaultSoundMatchErrorMessage
import com.example.soundmatch.ui.components.EpisodeListCard
import com.example.soundmatch.ui.components.ListItemCardType
import com.example.soundmatch.ui.components.PodcastCard
import com.example.soundmatch.ui.components.SoundMatchCompactListItemCard
import com.example.soundmatch.ui.components.SoundMatchCompactTrackCard
import com.example.soundmatch.ui.components.SoundMatchCompactTrackCardDefaults
import com.example.soundmatch.utils.itemsSafe

/**
 * A color that is meant to be applied to all types of search items.
 * [Color.Transparent] is specified as the background color for the
 * cards. Since the search screen's background uses a dynamic color based
 * on the currently playing track, the background color of the cards
 * needs to be transparent in order for the dynamic color to be
 * visible.
 */
private val CardBackgroundColor @Composable get() = Color.Transparent
private val CardShape = RectangleShape

fun LazyListScope.searchTrackListItems(
    tracksListForSearchQuery: LazyPagingItems<SearchResult.TrackSearchResult>,
    currentlyPlayingTrack: SearchResult.TrackSearchResult?,
    onItemClick: (SearchResult) -> Unit,
    isLoadingPlaceholderVisible: (SearchResult.TrackSearchResult) -> Boolean,
    onImageLoading: (SearchResult) -> Unit,
    onImageLoadingFinished: (SearchResult.TrackSearchResult, Throwable?) -> Unit
) {
    itemsIndexedWithEmptyListContent(
        items = tracksListForSearchQuery,
        cardType = ListItemCardType.TRACK,
        key = { index, track -> "$index${track.id}" }
    ) { _, track ->
        track?.let {
            SoundMatchCompactTrackCard(
                backgroundColor = CardBackgroundColor,
                shape = CardShape,
                track = it,
                onClick = onItemClick,
                isLoadingPlaceholderVisible = isLoadingPlaceholderVisible(it),
                onImageLoading = onImageLoading,
                onImageLoadingFinished = onImageLoadingFinished,
                isCurrentlyPlaying = it == currentlyPlayingTrack
            )
        }
    }
}


fun LazyListScope.searchAlbumListItems(
    albumListForSearchQuery: LazyPagingItems<SearchResult.AlbumSearchResult>,
    onItemClick: (SearchResult) -> Unit,
    isLoadingPlaceholderVisible: (SearchResult.AlbumSearchResult) -> Boolean,
    onImageLoading: (SearchResult) -> Unit,
    onImageLoadingFinished: (SearchResult.AlbumSearchResult, Throwable?) -> Unit
) {

    itemsIndexedWithEmptyListContent(
        items = albumListForSearchQuery,
        cardType = ListItemCardType.ALBUM,
        key = { index, album -> "$index${album.id}" }
    ) { _, album ->
        album?.let {
            SoundMatchCompactListItemCard(
                backgroundColor = CardBackgroundColor,
                shape = CardShape,
                cardType = ListItemCardType.ALBUM,
                thumbnailImageUrlString = it.albumArtUrlString,
                title = it.name,
                subtitle = it.artistsString,
                onClick = { onItemClick(it) },
                onTrailingButtonIconClick = { onItemClick(it) },
                isLoadingPlaceHolderVisible = isLoadingPlaceholderVisible(it),
                onThumbnailImageLoadingFinished = { throwable ->
                    onImageLoadingFinished(it, throwable)
                },
                onThumbnailLoading = { onImageLoading(it) },
                contentPadding = SoundMatchCompactTrackCardDefaults.defaultContentPadding
            )
        }
    }
}

fun LazyListScope.searchArtistListItems(
    artistListForSearchQuery: LazyPagingItems<SearchResult.ArtistSearchResult>,
    onItemClick: (SearchResult) -> Unit,
    isLoadingPlaceholderVisible: (SearchResult.ArtistSearchResult) -> Boolean,
    onImageLoading: (SearchResult) -> Unit,
    onImageLoadingFinished: (SearchResult.ArtistSearchResult, Throwable?) -> Unit,
    artistImageErrorPainter: Painter
) {
    itemsIndexedWithEmptyListContent(
        items = artistListForSearchQuery,
        cardType = ListItemCardType.PLAYLIST,
        key = { index, artist -> "$index${artist.id}" }
    ) { _, artist ->
        artist?.let {
            SoundMatchCompactListItemCard(
                backgroundColor = CardBackgroundColor,
                shape = CardShape,
                cardType = ListItemCardType.ARTIST,
                thumbnailImageUrlString = it.imageUrlString ?: "",
                title = it.name,
                subtitle = "Artist",
                onClick = { onItemClick(it) },
                onTrailingButtonIconClick = { onItemClick(it) },
                isLoadingPlaceHolderVisible = isLoadingPlaceholderVisible(it),
                onThumbnailImageLoadingFinished = { throwable ->
                    onImageLoadingFinished(it, throwable)
                },
                onThumbnailLoading = { onImageLoading(it) },
                errorPainter = artistImageErrorPainter,
                contentPadding = SoundMatchCompactTrackCardDefaults.defaultContentPadding
            )
        }
    }
}

fun LazyListScope.searchPlaylistListItems(
    playlistListForSearchQuery: LazyPagingItems<SearchResult.PlaylistSearchResult>,
    onItemClick: (SearchResult) -> Unit,
    isLoadingPlaceholderVisible: (SearchResult.PlaylistSearchResult) -> Boolean,
    onImageLoading: (SearchResult) -> Unit,
    onImageLoadingFinished: (SearchResult.PlaylistSearchResult, Throwable?) -> Unit,
    playlistImageErrorPainter: Painter
) {
    itemsIndexedWithEmptyListContent(
        items = playlistListForSearchQuery,
        cardType = ListItemCardType.PLAYLIST,
        key = { index, playlist -> "$index${playlist.id}" }
    ) { _, playlist ->
        playlist?.let {
            SoundMatchCompactListItemCard(
                backgroundColor = CardBackgroundColor,
                shape = CardShape,
                cardType = ListItemCardType.PLAYLIST,
                thumbnailImageUrlString = it.imageUrlString ?: "",
                title = it.name,
                subtitle = "Playlist",
                onClick = { onItemClick(it) },
                onTrailingButtonIconClick = { onItemClick(it) },
                isLoadingPlaceHolderVisible = isLoadingPlaceholderVisible(it),
                onThumbnailImageLoadingFinished = { throwable ->
                    onImageLoadingFinished(it, throwable)
                },
                onThumbnailLoading = { onImageLoading(it) },
                errorPainter = playlistImageErrorPainter,
                contentPadding = SoundMatchCompactTrackCardDefaults.defaultContentPadding
            )
        }
    }
}

fun LazyListScope.searchPodcastListItems(
    podcastsForSearchQuery: LazyPagingItems<SearchResult.PodcastSearchResult>,
    episodesForSearchQuery: LazyPagingItems<SearchResult.EpisodeSearchResult>,
    onPodcastItemClicked: (SearchResult.PodcastSearchResult) -> Unit,
    onEpisodeItemClicked: (SearchResult.EpisodeSearchResult) -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            text = "Podcasts & Shows",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        LazyRow(
            modifier = Modifier
                .fillParentMaxWidth()
                .height(238.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsSafe(podcastsForSearchQuery) { podcast ->
                PodcastCard(
                    podcastArtUrlString = podcast.imageUrlString,
                    name = podcast.name,
                    nameOfPublisher = podcast.nameOfPublisher,
                    onClick = { onPodcastItemClicked(podcast) }
                )
            }
        }
    }

    itemsIndexedWithEmptyListContent(episodesForSearchQuery) { _, episode ->
        episode?.let {
            EpisodeListCard(
                episodeSearchResult = it,
                onClick = { onEpisodeItemClicked(it) }
            )
        }
    }
}

private fun <T : Any> LazyListScope.itemsIndexedWithEmptyListContent(
    items: LazyPagingItems<T>,
    cardType: ListItemCardType? = null,
    key: ((index: Int, item: T) -> Any)? = null,
    emptyListContent: @Composable LazyItemScope.() -> Unit = {
        val title = remember(cardType) {
            "Couldn't find " +
                    "${
                        when (cardType) {
                            ListItemCardType.ALBUM -> "any albums"
                            ListItemCardType.ARTIST -> "any artists"
                            ListItemCardType.TRACK -> "any tracks"
                            ListItemCardType.PLAYLIST -> "any playlists"
                            null -> "anything"
                        }
                    } matching the search query."
        }
        DefaultSoundMatchErrorMessage(
            title = title,
            subtitle = "Try searching again using a different spelling or keyword.",
            modifier = Modifier
                .fillParentMaxSize()
                .padding(horizontal = 16.dp)
                .windowInsetsPadding(WindowInsets.ime)
        )
    },
    itemContent: @Composable LazyItemScope.(index: Int, value: T) -> Unit
) {
    if (items.loadState.append.endOfPaginationReached && items.itemCount == 0) {
        item { emptyListContent.invoke(this) }
    } else {
        items.itemCount.let { count ->
            items(count) { index ->
                items[index]?.let {
                    itemContent(index, it)
                }
            }
        }
    }
}

