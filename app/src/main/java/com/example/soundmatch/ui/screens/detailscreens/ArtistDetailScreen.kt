package com.example.soundmatch.ui.screens.detailscreens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.ui.components.AsyncImageWithPlaceholder
import com.example.soundmatch.ui.components.DefaultSoundMatchLoadingAnimation
import com.example.soundmatch.ui.components.DetailScreenTopAppBar
import com.example.soundmatch.ui.components.ListItemCardType
import com.example.soundmatch.ui.components.SoundMatchBottomNavigationConstants
import com.example.soundmatch.ui.components.SoundMatchCompactListItemCard
import com.example.soundmatch.ui.components.SoundMatchCompactTrackCard
import com.example.soundmatch.ui.components.SoundMatchMiniPlayerConstants
import com.example.soundmatch.ui.dynamicTheme.dynamicbackgroundmodifier.DynamicBackgroundResource
import kotlinx.coroutines.launch

@Composable
fun ArtistDetailScreen(
    artistName: String,
    artistImageUrlString: String?,
    popularTracks: List<SearchResult.TrackSearchResult>,
    releases: LazyPagingItems<SearchResult.AlbumSearchResult>,
    currentlyPlayingTrack: SearchResult.TrackSearchResult?,
    onBackButtonClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,
    onTrackClicked: (SearchResult.TrackSearchResult) -> Unit,
    onAlbumClicked: (SearchResult.AlbumSearchResult) -> Unit,
    isLoading: Boolean,
    @DrawableRes fallbackImageRes: Int,
    isErrorMessageVisible: Boolean
) {
    val subtitleTextColorWithAlpha = MaterialTheme.colorScheme.onBackground.copy(
        alpha = 0.38f
    )
    var isCoverArtPlaceholderVisible by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val fallbackImagePainter =
        rememberVectorPainter(ImageVector.vectorResource(id = fallbackImageRes))
    val isAppBarVisible by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
    }
    val dynamicBackgroundResource = remember {
        if (artistImageUrlString == null) DynamicBackgroundResource.Empty
        else DynamicBackgroundResource.FromImageUrl(artistImageUrlString)
    }
    val coroutineScope = rememberCoroutineScope()
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(
                bottom = SoundMatchBottomNavigationConstants.navigationHeight + SoundMatchMiniPlayerConstants.miniPlayerHeight
            )
        ) {
            artistCoverArtHeaderItem(
                artistName = artistName,
                artistCoverArtUrlString = artistImageUrlString,
                onBackButtonClicked = onBackButtonClicked,
                onPLayButtonClick = onPlayButtonClicked,
                isLoadingPlaceholderVisible = isCoverArtPlaceholderVisible,
                onCoverArtLoading = { isCoverArtPlaceholderVisible = true },
                onCoverArtLoaded = { isCoverArtPlaceholderVisible = false },
                fallbackImagePainter = fallbackImagePainter
            )
            item {
                SubtitleText(
                    modifier = Modifier.padding(all = 16.dp),
                    text = "Popular tracks"
                )
            }
            itemsIndexed(popularTracks) { index, track ->
                Row(
                    modifier = Modifier.height(64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = if (index + 1 < 10) Modifier.padding(16.dp)
                        else Modifier.padding(
                            top = 16.dp,
                            bottom = 16.dp,
                            start = 16.dp,
                            end = 8.dp
                        ),
                        text = "${index + 1}"
                    )
                    SoundMatchCompactTrackCard(
                        track = track,
                        subtitleTextStyle = MaterialTheme.typography
                            .bodySmall
                            .copy(color = subtitleTextColorWithAlpha),
                        onClick = onTrackClicked,
                        isLoadingPlaceholderVisible = false,
                        onImageLoading = {},
                        onImageLoadingFinished = { _, _ -> },
                        isCurrentlyPlaying = track == currentlyPlayingTrack
                    )
                }
            }
            item {
                SubtitleText(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Releases"
                )
            }
            items(
                count = releases.itemCount,
                key = { index -> releases.peek(index)?.id ?: index } // Ajusta `id` según tu modelo.
            ) { index ->
                val album = releases[index] // Accede al elemento directamente por índice.
                album?.let {
                    SoundMatchCompactListItemCard(
                        modifier = Modifier
                            .height(80.dp)
                            .padding(horizontal = 16.dp),
                        cardType = ListItemCardType.ALBUM,
                        thumbnailImageUrlString = it.albumArtUrlString,
                        title = it.name,
                        titleTextStyle = MaterialTheme.typography.titleLarge,
                        subtitle = it.yearOfReleaseString,
                        subtitleTextStyle = MaterialTheme.typography.titleSmall.copy(
                            color = subtitleTextColorWithAlpha
                        ),
                        onClick = { onAlbumClicked(it) },
                        onTrailingButtonIconClick = { onAlbumClicked(it) }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
            if (isErrorMessageVisible) {
                item {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Oops! Something doesn't look right",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Please check the internet connection",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
        DefaultSoundMatchLoadingAnimation(
            modifier = Modifier.align(Alignment.Center),
            isVisible = isLoading
        )
        AnimatedVisibility(
            visible = isAppBarVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DetailScreenTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .statusBarsPadding(),
                title = artistName,
                onBackButtonClicked = onBackButtonClicked,
                dynamicBackgroundResource = dynamicBackgroundResource,
                onClick = {
                    coroutineScope.launch { lazyListState.animateScrollToItem(0) }
                }
            )
        }
    }
}

@Composable
private fun SubtitleText(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
}

private fun LazyListScope.artistCoverArtHeaderItem(
    artistName: String,
    artistCoverArtUrlString: String?,
    fallbackImagePainter: Painter,
    onBackButtonClicked: () -> Unit,
    onPLayButtonClick: () -> Unit,
    isLoadingPlaceholderVisible: Boolean = false,
    onCoverArtLoading: (() -> Unit)? = null,
    onCoverArtLoaded: ((Throwable?) -> Unit)? = null,
) {
    item {
        Box(
            modifier = Modifier
                .fillParentMaxHeight(0.6f)
                .fillParentMaxWidth()
        ) {
            if (artistCoverArtUrlString != null) {
                AsyncImageWithPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                    model = artistCoverArtUrlString,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    isLoadingPlaceholderVisible = isLoadingPlaceholderVisible,
                    onImageLoading = { onCoverArtLoading?.invoke() },
                    onImageLoadingFinished = { onCoverArtLoaded?.invoke(it) }
                )
            } else {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = fallbackImagePainter,
                    contentDescription = null
                )
            }

            // scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                onClick = onBackButtonClicked
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                text = artistName,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp)
                    .offset(y = 24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = onPLayButtonClick
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null
                )
            }
        }
    }
}