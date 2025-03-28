package com.example.soundmatch.ui.screens.detailscreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.ui.components.DefaultSoundMatchLoadingAnimation
import com.example.soundmatch.ui.components.DetailScreenTopAppBar
import com.example.soundmatch.ui.components.HeaderImageSource
import com.example.soundmatch.ui.components.ImageHeaderWithMetadata
import com.example.soundmatch.ui.components.SoundMatchBottomNavigationConstants
import com.example.soundmatch.ui.components.SoundMatchCompactTrackCard
import com.example.soundmatch.ui.components.SoundMatchMiniPlayerConstants
import com.example.soundmatch.ui.dynamicTheme.dynamicbackgroundmodifier.DynamicBackgroundResource
import com.example.soundmatch.ui.dynamicTheme.dynamicbackgroundmodifier.dynamicBackground
import kotlinx.coroutines.launch

@Composable
fun AlbumDetailScreen(
    albumName: String,
    artistsString: String,
    yearOfRelease: String,
    albumArtUrlString: String,
    trackList: List<SearchResult.TrackSearchResult>,
    onTrackItemClick: (SearchResult.TrackSearchResult) -> Unit,
    onBackButtonClicked: () -> Unit,
    isLoading: Boolean,
    isErrorMessageVisible: Boolean,
    currentlyPlayingTrack: SearchResult.TrackSearchResult?
) {
    var isLoadingPlaceholderForAlbumArtVisible by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val isAppBarVisible by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
    }
    val dynamicBackgroundResource =
        remember { DynamicBackgroundResource.FromImageUrl(albumArtUrlString) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = SoundMatchBottomNavigationConstants.navigationHeight + SoundMatchMiniPlayerConstants.miniPlayerHeight
            ),
            state = lazyListState
        ) {
            headerWithImageItem(
                dynamicBackgroundResource = dynamicBackgroundResource,
                albumName = albumName,
                albumArtUrlString = albumArtUrlString,
                artistsString = artistsString,
                yearOfRelease = yearOfRelease,
                isLoadingPlaceholderForAlbumArtVisible = isLoadingPlaceholderForAlbumArtVisible,
                onImageLoading = { isLoadingPlaceholderForAlbumArtVisible = true },
                onImageLoaded = { isLoadingPlaceholderForAlbumArtVisible = false },
                onBackButtonClicked = onBackButtonClicked
            )

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
            } else {
                items(trackList) { track ->
                    SoundMatchCompactTrackCard(
                        track = track,
                        onClick = onTrackItemClick,
                        isLoadingPlaceholderVisible = false,
                        isCurrentlyPlaying = currentlyPlayingTrack?.let { it == track } ?: false,
                        isAlbumArtVisible = false,
                        subtitleTextStyle = LocalTextStyle.current.copy(
                            fontWeight = FontWeight.Thin,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f),
                        ),
                        contentPadding = PaddingValues(16.dp)
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        .padding(bottom = 16.dp)
                )
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
                title = albumName,
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
private fun AlbumArtHeaderMetadata(yearOfRelease: String) {
    Text(
        text = "Album • $yearOfRelease",
        fontWeight = FontWeight.Normal,
        style = MaterialTheme.typography
            .titleSmall
            .copy(
                color = MaterialTheme.colorScheme
                    .onBackground
                    .copy()
            )
    )
}

private fun LazyListScope.headerWithImageItem(
    dynamicBackgroundResource: DynamicBackgroundResource,
    albumName: String,
    albumArtUrlString: String,
    artistsString: String,
    yearOfRelease: String,
    isLoadingPlaceholderForAlbumArtVisible: Boolean,
    onImageLoading: () -> Unit,
    onImageLoaded: (Throwable?) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    item {
        Column(
            modifier = Modifier
                .dynamicBackground(dynamicBackgroundResource)
                .statusBarsPadding()
        ) {
            ImageHeaderWithMetadata(
                title = albumName,
                headerImageSource = HeaderImageSource.ImageFromUrlString(albumArtUrlString),
                subtitle = artistsString,
                onBackButtonClicked = onBackButtonClicked,
                isLoadingPlaceholderVisible = isLoadingPlaceholderForAlbumArtVisible,
                onImageLoading = onImageLoading,
                onImageLoaded = onImageLoaded,
                additionalMetadataContent = { AlbumArtHeaderMetadata(yearOfRelease) }
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}