package com.example.soundmatch.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.R
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.soundmatch.domain.PodcastEpisode
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.Streamable
import com.example.soundmatch.ui.components.DefaultSoundMatchErrorMessage
import com.example.soundmatch.ui.components.DefaultSoundMatchLoadingAnimation
import com.example.soundmatch.ui.screens.detailscreens.AlbumDetailScreen
import com.example.soundmatch.ui.screens.detailscreens.ArtistDetailScreen
import com.example.soundmatch.ui.screens.detailscreens.PlaylistDetailScreen
import com.example.soundmatch.ui.screens.detailscreens.PodcastEpisodeDetailScreen
import com.example.soundmatch.ui.screens.podcastshowdetailscreen.PodcastShowDetailScreen
import com.example.soundmatch.viewmodels.AlbumDetailUiState
import com.example.soundmatch.viewmodels.AlbumDetailViewModel
import com.example.soundmatch.viewmodels.PlaylistDetailViewModel
import com.example.soundmatch.viewmodels.PodcastEpisodeDetailViewModel
import com.example.soundmatch.viewmodels.PodcastShowDetailViewModel
import com.example.soundmatch.viewmodels.artistviewmodel.ArtistDetailScreenUiState
import com.example.soundmatch.viewmodels.artistviewmodel.ArtistDetailViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * A nested navigation graph that consists of detail screens.
 *
 * It uses [prefixedWithRouteOfNavGraphRoute] for the nested destinations.
 * For information on why [prefixedWithRouteOfNavGraphRoute] see
 * docs of [NavGraphWithDetailScreensNestedController] class.
 *
 * @param navGraphRoute the destination's unique route
 * @param navController the nav controller to be associated with the nav graph.
 * @param startDestination the route for the start destination.
 * @param playStreamable lambda to execute when a [Streamable] is to be played.
 * @param builder the builder used to define other composables that belong
 * to this nested graph.
 * @see NavGraphBuilder.artistDetailScreen
 * @see NavGraphBuilder.albumDetailScreen
 * @see NavGraphBuilder.playlistDetailScreen
 */

fun NavGraphBuilder.navGraphWithDetailScreens(
    navGraphRoute: String,
    navController: NavHostController,
    playStreamable: (Streamable) -> Unit,
    onPausePlayback: () -> Unit,
    startDestination: String,
    builder: NavGraphBuilder.(nestedController: NavGraphWithDetailScreensNestedController) -> Unit
) {
    val onBackButtonClicked = {
        navController.popBackStack()
        Unit // Need to specify explicitly inorder to avoid compilation errors
    }
    val nestedController = NavGraphWithDetailScreensNestedController(
        navController = navController,
        associatedNavGraphRoute = navGraphRoute,
        playTrack = playStreamable
    )
    navigation(
        route = navGraphRoute,
        startDestination = startDestination
    ) {
        builder(nestedController)
        artistDetailScreen(
            route = SoundMatchNavigationDestinations
                .ArtistDetailScreen
                .prefixedWithRouteOfNavGraphRoute(navGraphRoute),
            arguments = listOf(
                navArgument(SoundMatchNavigationDestinations.ArtistDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING) {
                    nullable = true
                }
            ),
            onBackButtonClicked = onBackButtonClicked,
            onAlbumClicked = nestedController::navigateToDetailScreen,
            onPlayTrack = playStreamable
        )
        albumDetailScreen(
            route = SoundMatchNavigationDestinations
                .AlbumDetailScreen
                .prefixedWithRouteOfNavGraphRoute(navGraphRoute),
            onBackButtonClicked = onBackButtonClicked,
            onPlayTrack = playStreamable
        )
        playlistDetailScreen(
            route = SoundMatchNavigationDestinations
                .PlaylistDetailScreen
                .prefixedWithRouteOfNavGraphRoute(navGraphRoute),
            onBackButtonClicked = onBackButtonClicked,
            onPlayTrack = playStreamable
        )
        podcastEpisodeDetailScreen(
            route = SoundMatchNavigationDestinations
                .PodcastEpisodeDetailScreen
                .prefixedWithRouteOfNavGraphRoute(navGraphRoute),
            onBackButtonClicked = onBackButtonClicked,
            onPlayButtonClicked = playStreamable,
            onPauseButtonClicked = onPausePlayback,
            navigateToPodcastShowDetailScreen = nestedController::navigateToDetailScreen
        )

        podcastShowDetailScreen(
            route = SoundMatchNavigationDestinations
                .PodcastShowDetailScreen
                .prefixedWithRouteOfNavGraphRoute(navGraphRoute),
            onEpisodePlayButtonClicked = playStreamable,
            onEpisodePauseButtonClicked = { onPausePlayback() },
            onEpisodeClicked = playStreamable,
            onBackButtonClicked = onBackButtonClicked
        )

    }
}

private fun NavGraphBuilder.artistDetailScreen(
    route: String,
    onBackButtonClicked: () -> Unit,
    onPlayTrack: (SearchResult.TrackSearchResult) -> Unit,
    onAlbumClicked: (SearchResult.AlbumSearchResult) -> Unit,
    arguments: List<NamedNavArgument> = emptyList()
) {
    composable(route, arguments) { backStackEntry ->
        val viewModel = hiltViewModel<ArtistDetailViewModel>(backStackEntry)
        val arguments = backStackEntry.arguments!!
        val artistName =
            arguments.getString(SoundMatchNavigationDestinations.ArtistDetailScreen.NAV_ARG_ARTIST_NAME)!!
        val artistImageUrlString =
            arguments.getString(SoundMatchNavigationDestinations.ArtistDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING)
                ?.run { URLDecoder.decode(this, StandardCharsets.UTF_8.toString()) }
        val releases = viewModel.albumsOfArtistFlow.collectAsLazyPagingItems()
        val uiState by viewModel.uiState
        val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)
        ArtistDetailScreen(
            artistName = artistName,
            artistImageUrlString = artistImageUrlString,
            popularTracks = viewModel.popularTracks.value,
            releases = releases,
            currentlyPlayingTrack = currentlyPlayingTrack,
            onBackButtonClicked = onBackButtonClicked,
            onPlayButtonClicked = {},
            onTrackClicked = onPlayTrack,
            onAlbumClicked = onAlbumClicked,
            isLoading = uiState is ArtistDetailScreenUiState.Loading,
            fallbackImageRes = com.example.soundmatch.R.drawable.ic_outline_account_circle_24,
            isErrorMessageVisible = uiState is ArtistDetailScreenUiState.Error
        )
    }
}

private fun NavGraphBuilder.albumDetailScreen(
    route: String,
    onBackButtonClicked: () -> Unit,
    onPlayTrack: (SearchResult.TrackSearchResult) -> Unit
) {
    composable(route) { backStackEntry ->
        val arguments = backStackEntry.arguments!!
        val viewModel = hiltViewModel<AlbumDetailViewModel>()
        val albumArtUrl =
            arguments.getString(SoundMatchNavigationDestinations.AlbumDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING)!!
        val albumName =
            arguments.getString(SoundMatchNavigationDestinations.AlbumDetailScreen.NAV_ARG_ALBUM_NAME)!!
        val artists =
            arguments.getString(SoundMatchNavigationDestinations.AlbumDetailScreen.NAV_ARG_ARTISTS_STRING)!!
        val yearOfRelease =
            arguments.getString(SoundMatchNavigationDestinations.AlbumDetailScreen.NAV_ARG_YEAR_OF_RELEASE_STRING)!!
        val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)
        AlbumDetailScreen(
            albumName = albumName,
            artistsString = artists,
            yearOfRelease = yearOfRelease,
            albumArtUrlString = albumArtUrl,
            trackList = viewModel.tracks.value,
            onTrackItemClick = onPlayTrack,
            onBackButtonClicked = onBackButtonClicked,
            isLoading = viewModel.uiState.value is AlbumDetailUiState.Loading,
            isErrorMessageVisible = viewModel.uiState.value is AlbumDetailUiState.Error,
            currentlyPlayingTrack = currentlyPlayingTrack
        )
    }
}

private fun NavGraphBuilder.playlistDetailScreen(
    route: String,
    onBackButtonClicked: () -> Unit,
    onPlayTrack: (SearchResult.TrackSearchResult) -> Unit,
    navigationArguments: List<NamedNavArgument> = emptyList()
) {
    composable(route = route, arguments = navigationArguments) {
        val arguments = it.arguments!!
        val viewModel = hiltViewModel<PlaylistDetailViewModel>()
        val tracks = viewModel.tracks.collectAsLazyPagingItems()
        val playlistName =
            arguments.getString(SoundMatchNavigationDestinations.PlaylistDetailScreen.NAV_ARG_PLAYLIST_NAME)!!
        val imageUrlString =
            arguments.getString(SoundMatchNavigationDestinations.PlaylistDetailScreen.NAV_ARG_ENCODED_IMAGE_URL_STRING)!!
        val ownerName =
            arguments.getString(SoundMatchNavigationDestinations.PlaylistDetailScreen.NAV_ARG_OWNER_NAME)!!
        val totalNumberOfTracks =
            arguments.getString(SoundMatchNavigationDestinations.PlaylistDetailScreen.NAV_ARG_NUMBER_OF_TRACKS)!!
        val isErrorMessageVisible by remember {
            derivedStateOf {
                tracks.loadState.refresh is LoadState.Error ||
                        tracks.loadState.append is LoadState.Error ||
                        tracks.loadState.prepend is LoadState.Error

            }
        }
        val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)
        val isPlaybackLoading by viewModel.playbackLoadingStateStream.collectAsState(initial = false)
        PlaylistDetailScreen(
            playlistName = playlistName,
            playlistImageUrlString = imageUrlString,
            nameOfPlaylistOwner = ownerName,
            totalNumberOfTracks = totalNumberOfTracks,
            imageResToUseWhenImageUrlStringIsNull = com.example.soundmatch.R.drawable.ic_outline_account_circle_24,
            tracks = tracks,
            currentlyPlayingTrack = currentlyPlayingTrack,
            onBackButtonClicked = onBackButtonClicked,
            onTrackClicked = onPlayTrack,
            isLoading = tracks.loadState.refresh is LoadState.Loading || isPlaybackLoading,
            isErrorMessageVisible = isErrorMessageVisible
        )
    }
}

/**
 * A class that acts a controller that is used to navigate within
 * destinations defined in [NavGraphBuilder.navGraphWithDetailScreens].
 *
 * Navigation component doesn't work deterministically when the same
 * nested graph is used more than once in the same graph. Since the
 * same destinations defined in [NavGraphBuilder.navGraphWithDetailScreens] are
 * reused (with the same routes) multiple times within the same graph,
 * navigation component chooses the destination that appears in the first call
 * to [NavGraphBuilder.navGraphWithDetailScreens] when ever the client
 * chooses to navigate to one of the screens defined in
 * [NavGraphBuilder.navGraphWithDetailScreens].
 * Eg:
 * Let's assume that NavGraphBuilder.navGraphWithDetailScreens has an artist
 * and album detail screen.
 * ```
 * NavHost(...){
 *
 *      // (1) contains detail screens
 *      navGraphWithDetailScreens(){
 *         /* Other composable destinations */
 *      }
 *
 *      // (2) contains the same detail screens as (1)
 *      navGraphWithDetailScreens(){
 *         /* Other composable destinations */
 *      }
 * }
 *```
 * When the client wants to navigate to a detail screen (lets take album detail
 * screen for example), then, the navigation component will navigate to the
 * album detail screen defined in (1) and not the detail screen defined in (2)
 * even if the client is navigating from one of the composable destinations defined
 * in the second call since the route strings for the detail screens are the same in
 * both graphs ((1) and (2)). This results in navigating to a destination that has an
 * unexpected parent navGraph. In order to avoid this, the destinations defined
 * in [NavGraphBuilder.navGraphWithDetailScreens] are prefixed with the route
 * of the said navGraph using [prefixedWithRouteOfNavGraphRoute]. The
 * [NavGraphWithDetailScreensNestedController.navigateToDetailScreen]
 * prefixes [associatedNavGraphRoute] before navigating in-order to accommodate
 * for this.
 */
class NavGraphWithDetailScreensNestedController(
    private val navController: NavHostController,
    private val associatedNavGraphRoute: String,
    private val playTrack: (SearchResult.TrackSearchResult) -> Unit
) {
    fun navigateToDetailScreen(podcastEpisode: PodcastEpisode) {
        val route = SoundMatchNavigationDestinations
            .PodcastShowDetailScreen
            .buildRoute(podcastEpisode.podcastShowInfo.id)
        navController.navigate(associatedNavGraphRoute + route) { launchSingleTop = true }
    }

    fun navigateToDetailScreen(searchResult: SearchResult) {
        val route = when (searchResult) {
            is SearchResult.AlbumSearchResult -> SoundMatchNavigationDestinations
                .AlbumDetailScreen
                .buildRoute(searchResult)

            is SearchResult.ArtistSearchResult -> SoundMatchNavigationDestinations
                .ArtistDetailScreen
                .buildRoute(searchResult)

            is SearchResult.PlaylistSearchResult -> SoundMatchNavigationDestinations
                .PlaylistDetailScreen
                .buildRoute(searchResult)

            is SearchResult.TrackSearchResult -> {
                playTrack(searchResult)
                return
            }
            is SearchResult.PodcastSearchResult -> {
                SoundMatchNavigationDestinations.PodcastShowDetailScreen.buildRoute(searchResult.id)
            }
            is SearchResult.EpisodeSearchResult -> {
                SoundMatchNavigationDestinations.PodcastEpisodeDetailScreen.buildRoute(searchResult.id)
            }
        }
        navController.navigate(associatedNavGraphRoute + route)
    }
}

private fun NavGraphBuilder.podcastEpisodeDetailScreen(
    route: String,
    onPlayButtonClicked: (PodcastEpisode) -> Unit,
    onPauseButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    navigateToPodcastShowDetailScreen: (PodcastEpisode) -> Unit
) {
    composable(route = route) {
        val viewModel = hiltViewModel<PodcastEpisodeDetailViewModel>()

        val uiState = viewModel.uiState
        val isEpisodeCurrentlyPlaying = viewModel.isEpisodeCurrentlyPlaying
        if (viewModel.podcastEpisode == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState == PodcastEpisodeDetailViewModel.UiSate.LOADING) {
                    DefaultSoundMatchLoadingAnimation(
                        modifier = Modifier.align(Alignment.Center),
                        isVisible = true
                    )
                }
                if (uiState == PodcastEpisodeDetailViewModel.UiSate.ERROR) {
                    DefaultSoundMatchErrorMessage(
                        modifier = Modifier.align(Alignment.Center),
                        title = "Oops! Something doesn't look right",
                        subtitle = "Please check the internet connection",
                        onRetryButtonClicked = viewModel::retryFetchingEpisode
                    )
                }
            }
        } else {
            PodcastEpisodeDetailScreen(
                podcastEpisode = viewModel.podcastEpisode!!,
                isEpisodeCurrentlyPlaying = isEpisodeCurrentlyPlaying,
                isPlaybackLoading = uiState == PodcastEpisodeDetailViewModel.UiSate.PLAYBACK_LOADING,
                onPlayButtonClicked = {
                    onPlayButtonClicked(viewModel.podcastEpisode!!)
                },
                onPauseButtonClicked = { onPauseButtonClicked() },
                onShareButtonClicked = {},
                onAddButtonClicked = {},
                onDownloadButtonClicked = {},
                onBackButtonClicked = onBackButtonClicked,
                navigateToPodcastDetailScreen = {
                    viewModel.podcastEpisode?.let { navigateToPodcastShowDetailScreen(it) }
                }
            )
        }
    }
}


private fun NavGraphBuilder.podcastShowDetailScreen(
    route: String,
    onEpisodePlayButtonClicked: (PodcastEpisode) -> Unit,
    onEpisodePauseButtonClicked: (PodcastEpisode) -> Unit,
    onEpisodeClicked: (PodcastEpisode) -> Unit,
    onBackButtonClicked: () -> Unit
) {
    composable(route = route) {
        val viewModel = hiltViewModel<PodcastShowDetailViewModel>()
        val uiState = viewModel.uiState
        val episodesForShow = viewModel.episodesForShowStream.collectAsLazyPagingItems()
        if (viewModel.podcastShow == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState == PodcastShowDetailViewModel.UiState.LOADING) {
                    DefaultSoundMatchLoadingAnimation(
                        modifier = Modifier.align(Alignment.Center),
                        isVisible = true
                    )
                }
                if (uiState == PodcastShowDetailViewModel.UiState.ERROR) {
                    DefaultSoundMatchErrorMessage(
                        modifier = Modifier.align(Alignment.Center),
                        title = "Oops! Something doesn't look right",
                        subtitle = "Please check the internet connection",
                        onRetryButtonClicked = viewModel::retryFetchingShow
                    )
                }
            }
        } else {
            PodcastShowDetailScreen(
                podcastShow = viewModel.podcastShow!!,
                onBackButtonClicked = onBackButtonClicked,
                onEpisodePlayButtonClicked = onEpisodePlayButtonClicked,
                onEpisodePauseButtonClicked = onEpisodePauseButtonClicked,
                currentlyPlayingEpisode = viewModel.currentlyPlayingEpisode,
                isCurrentlyPlayingEpisodePaused = viewModel.isCurrentlyPlayingEpisodePaused,
                isPlaybackLoading = uiState == PodcastShowDetailViewModel.UiState.PLAYBACK_LOADING,
                onEpisodeClicked = onEpisodeClicked,
                episodes = episodesForShow
            )
        }
    }
}

/**
 * A utility function that appends the [routeOfNavGraph] to [MusifyNavigationDestinations.route]
 * as prefix. See docs of [NavGraphWithDetailScreensNestedController] for more information.
 */
private fun SoundMatchNavigationDestinations.prefixedWithRouteOfNavGraphRoute(routeOfNavGraph: String) =
    routeOfNavGraph + this.route