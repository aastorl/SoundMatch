package com.example.soundmatch.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.soundmatch.domain.HomeFeedCarouselCardInfo
import com.example.soundmatch.domain.HomeFeedFilters
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.Streamable
import com.example.soundmatch.ui.dynamicTheme.dynamicbackgroundmodifier.dynamicBackground
import com.example.soundmatch.ui.screens.GetPremiumScreen
import com.example.soundmatch.ui.screens.homescreen.HomeScreen
import com.example.soundmatch.ui.screens.searchscreen.PagingItemsForSearchScreen
import com.example.soundmatch.ui.screens.searchscreen.SearchScreen
import com.example.soundmatch.viewmodels.homefeedviewmodel.HomeFeedViewModel
import com.example.soundmatch.viewmodels.searchviewmodel.SearchFilter
import com.example.soundmatch.viewmodels.searchviewmodel.SearchScreenUiState
import com.example.soundmatch.viewmodels.searchviewmodel.SearchViewModel

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun SoundMatchNavigation(
    navController: NavHostController,
    playStreamable: (Streamable) -> Unit,
    onPausePlayback: () -> Unit,
    isFullScreenNowPlayingOverlayScreenVisible: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = SoundMatchBottomNavigationDestinations.Home.route
    ) {
        navGraphWithDetailScreens(
            navGraphRoute = SoundMatchBottomNavigationDestinations.Home.route,
            startDestination = SoundMatchBottomNavigationDestinations.HomeScreen.route,
            navController = navController,
            playStreamable = playStreamable,
            onPausePlayback = onPausePlayback
        ) { nestedController ->
            homeScreen(
                route = SoundMatchNavigationDestinations.HomeScreen.route,
                onCarouselCardClicked = {
                    nestedController.navigateToDetailScreen(searchResult = it.associatedSearchResult)
                }
            )
        }
        navGraphWithDetailScreens(
            navGraphRoute = SoundMatchBottomNavigationDestinations.Search.route,
            startDestination = SoundMatchNavigationDestinations.SearchScreen.route,
            navController = navController,
            playStreamable = playStreamable,
            onPausePlayback = onPausePlayback
        ) { nestedController ->
            searchScreen(
                route = SoundMatchNavigationDestinations.SearchScreen.route,
                onSearchResultClicked = nestedController::navigateToDetailScreen,
                isFullScreenNowPlayingScreenOverlayVisible = isFullScreenNowPlayingOverlayScreenVisible
            )
        }

        composable(SoundMatchBottomNavigationDestinations.Premium.route) {
            GetPremiumScreen()
        }
    }
}

@ExperimentalFoundationApi
private fun NavGraphBuilder.homeScreen(
    route: String,
    onCarouselCardClicked: (HomeFeedCarouselCardInfo) -> Unit
) {
    composable(route) {
        val homeFeedViewModel = hiltViewModel<HomeFeedViewModel>()
        val filters = remember {
            listOf(
                HomeFeedFilters.Music,
                HomeFeedFilters.PodcastsAndShows
            )
        }
        HomeScreen(
            timeBasedGreeting = homeFeedViewModel.greetingPhrase,
            homeFeedFilters = filters,
            currentlySelectedHomeFeedFilter = HomeFeedFilters.None,
            onHomeFeedFilterClick = {},
            carousels = homeFeedViewModel.homeFeedCarousels.value,
            onHomeFeedCarouselCardClick = onCarouselCardClicked,
            isErrorMessageVisible = homeFeedViewModel.uiState.value == HomeFeedViewModel.HomeFeedUiState.ERROR,
            isLoading = homeFeedViewModel.uiState.value == HomeFeedViewModel.HomeFeedUiState.LOADING,
            onErrorRetryButtonClick = homeFeedViewModel::refreshFeed
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
private fun NavGraphBuilder.searchScreen(
    route: String,
    onSearchResultClicked: (SearchResult) -> Unit,
    isFullScreenNowPlayingScreenOverlayVisible: Boolean,
) {
    composable(route = route) {
        val viewModel = hiltViewModel<SearchViewModel>()
        val albums = viewModel.albumListForSearchQuery.collectAsLazyPagingItems()
        val artists = viewModel.artistListForSearchQuery.collectAsLazyPagingItems()
        val playlists = viewModel.playlistListForSearchQuery.collectAsLazyPagingItems()
        val tracks = viewModel.trackListForSearchQuery.collectAsLazyPagingItems()
        val podcasts = viewModel.podcastListForSearchQuery.collectAsLazyPagingItems()
        val episodes = viewModel.episodeListForSearchQuery.collectAsLazyPagingItems()
        val pagingItems = remember {
            PagingItemsForSearchScreen(
                albums,
                artists,
                tracks,
                playlists,
                podcasts,
                episodes
            )
        }
        val uiState by viewModel.uiState
        val isLoadingError by remember {
            derivedStateOf {
                tracks.loadState.refresh is LoadState.Error || tracks.loadState.append is LoadState.Error || tracks.loadState.prepend is LoadState.Error
            }
        }
        val controller = LocalSoftwareKeyboardController.current
        val genres = remember { viewModel.getAvailableGenres() }
        val filters = remember { SearchFilter.values().toList() }
        val currentlySelectedFilter by viewModel.currentlySelectedFilter
        val dynamicBackgroundResource by remember {
            derivedStateOf {
                val imageUrl = when (currentlySelectedFilter) {
                    SearchFilter.ALBUMS -> albums.itemSnapshotList.firstOrNull()?.albumArtUrlString
                    SearchFilter.TRACKS -> tracks.itemSnapshotList.firstOrNull()?.imageUrlString
                    SearchFilter.ARTISTS -> artists.itemSnapshotList.firstOrNull()?.imageUrlString
                    SearchFilter.PLAYLISTS -> playlists.itemSnapshotList.firstOrNull()?.imageUrlString
                    SearchFilter.PODCASTS -> podcasts.itemSnapshotList.firstOrNull()?.imageUrlString
                }
                if (imageUrl == null) DynamicBackgroundResource.Empty
                else DynamicBackgroundResource.FromImageUrl(imageUrl)
            }
        }
        val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)
        Box(modifier = Modifier.dynamicBackground(dynamicBackgroundResource)) {
            SearchScreen(
                genreList = genres,
                searchScreenFilters = filters,
                onGenreItemClick = {},
                onSearchTextChanged = viewModel::search,
                isLoading = uiState == SearchScreenUiState.LOADING,
                pagingItems = pagingItems,
                onSearchQueryItemClicked = onSearchResultClicked,
                currentlySelectedFilter = viewModel.currentlySelectedFilter.value,
                onSearchFilterChanged = viewModel::updateSearchFilter,
                isSearchErrorMessageVisible = isLoadingError,
                onImeDoneButtonClicked = {
                    // Search only if there was an error while loading.
                    // A manual call to search() is not required
                    // when there is no error because, search()
                    // will be called automatically, everytime the
                    // search text changes. This prevents duplicate
                    // calls when the user manually clicks the done
                    // button after typing the search text, in
                    // which case, the keyboard will just be hidden.
                    if (isLoadingError) viewModel.search(it)
                    controller?.hide()
                },
                currentlyPlayingTrack = currentlyPlayingTrack,
                isFullScreenNowPlayingOverlayScreenVisible = isFullScreenNowPlayingScreenOverlayVisible,
                onErrorRetryButtonClick = viewModel::search
            )
        }
    }
}
