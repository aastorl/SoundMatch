package com.example.soundmatch.ui.screens.homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.soundmatch.domain.HomeFeedCarousel
import com.example.soundmatch.domain.HomeFeedCarouselCardInfo
import com.example.soundmatch.domain.HomeFeedFilters
import com.example.soundmatch.ui.components.DefaultSoundMatchErrorMessage
import com.example.soundmatch.ui.components.DefaultSoundMatchLoadingAnimation
import com.example.soundmatch.ui.components.HomeFeedCard
import com.example.soundmatch.ui.components.SoundMatchBottomNavigationConstants
import com.example.soundmatch.ui.components.SoundMatchFilterChip
import com.example.soundmatch.ui.components.SoundMatchMiniPlayerConstants

/**
 * A home screen composable that shows time-based greeting, home feed filters,
 * and carousels. It also handles loading and error states.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    timeBasedGreeting: String,
    homeFeedFilters: List<HomeFeedFilters>,
    currentlySelectedHomeFeedFilter: HomeFeedFilters,
    onHomeFeedFilterClick: (HomeFeedFilters) -> Unit,
    carousels: List<HomeFeedCarousel>,
    onHomeFeedCarouselCardClick: (HomeFeedCarouselCardInfo) -> Unit,
    onErrorRetryButtonClick: () -> Unit,
    isLoading: Boolean,
    isErrorMessageVisible: Boolean,
) {
    val lazyColumnState = rememberLazyListState()
    val isStatusBarSpacerVisible = remember {
        derivedStateOf { lazyColumnState.firstVisibleItemIndex > 1 }
    }
    val lazyColumnBottomPaddingValues = remember {
        SoundMatchBottomNavigationConstants.navigationHeight + SoundMatchMiniPlayerConstants.miniPlayerHeight
    }
    val errorMessageItem = @Composable { modifier: Modifier ->
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DefaultSoundMatchErrorMessage(
                title = "Oops! Something doesn't look right",
                subtitle = "Please check the internet connection",
                onRetryButtonClicked = onErrorRetryButtonClick
            )
        }
    }
    Box {
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = lazyColumnBottomPaddingValues)
        ) {
            item {
                HeaderRow(
                    timeBasedGreeting = timeBasedGreeting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                )
            }
            stickyHeader {
                if (isStatusBarSpacerVisible.value) {
                    Spacer(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                }
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (homeFeedFilter in homeFeedFilters) {
                        SoundMatchFilterChip(
                            text = homeFeedFilter.title ?: continue,
                            onClick = { onHomeFeedFilterClick(homeFeedFilter) },
                            isSelected = homeFeedFilter == currentlySelectedHomeFeedFilter
                        )
                    }
                }
            }
            if (isErrorMessageVisible) {
                item {
                    errorMessageItem(
                        Modifier
                            .fillParentMaxSize()
                            .padding(bottom = lazyColumnBottomPaddingValues)
                    )
                }
            } else {
                items(items = carousels, key = { it.id }) { carousel ->
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = carousel.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    CarouselLazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        carousel = carousel,
                        onHomeFeedCardClick = { onHomeFeedCarouselCardClick(it) }
                    )
                }
            }
        }
        DefaultSoundMatchLoadingAnimation(
            isVisible = isLoading,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun CarouselLazyRow(
    carousel: HomeFeedCarousel,
    onHomeFeedCardClick: (HomeFeedCarouselCardInfo) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = carousel.associatedCards, key = { it.id }) { card ->
            HomeFeedCard(
                imageUrlString = card.imageUrlString,
                caption = card.caption,
                onClick = { onHomeFeedCardClick(card) }
            )
        }
    }
}

@Composable
private fun HeaderRow(modifier: Modifier = Modifier, timeBasedGreeting: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = timeBasedGreeting,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Row {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_listening_history),
                    contentDescription = null
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null
                )
            }
        }
    }
}
