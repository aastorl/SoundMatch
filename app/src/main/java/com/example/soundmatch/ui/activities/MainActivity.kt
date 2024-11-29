package com.example.soundmatch.ui.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.soundmatch.ui.navigation.SoundMatchBottomNavigationConnectedWithBackStack
import com.example.soundmatch.ui.navigation.SoundMatchBottomNavigationDestinations
import com.example.soundmatch.ui.navigation.SoundMatchNavigation
import com.example.soundmatch.ui.screens.homescreen.ExpandableMiniPlayerWithSnackbar
import com.example.soundmatch.ui.theme.SoundMatchTheme
import com.example.soundmatch.viewmodels.PlaybackViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            SoundMatchTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    content = { SoundMatchApp() })
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun SoundMatchApp() {
    val playbackViewModel = hiltViewModel<PlaybackViewModel>()
    val playbackState by playbackViewModel.playbackState
    val snackbarHostState = remember { SnackbarHostState() }
    val playbackEvent: PlaybackViewModel.Event? by playbackViewModel.playbackEventsFlow.collectAsState(
        initial = null
    )
    val miniPlayerStreamable = remember(playbackState) {
        playbackState.currentlyPlayingStreamable ?: playbackState.previouslyPlayingStreamable
    }
    var isNowPlayingScreenVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = playbackEvent) {
        if (playbackEvent !is PlaybackViewModel.Event.PlaybackError) return@LaunchedEffect
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = (playbackEvent as PlaybackViewModel.Event.PlaybackError).errorMessage,
        )
    }
    val isPlaybackPaused = remember(playbackState) {
        playbackState is PlaybackViewModel.PlaybackState.Paused || playbackState is PlaybackViewModel.PlaybackState.PlaybackEnded
    }

    BackHandler(isNowPlayingScreenVisible) {
        isNowPlayingScreenVisible = false
    }
    val bottomNavigationItems = remember {
        listOf(
            SoundMatchBottomNavigationDestinations.Home,
            SoundMatchBottomNavigationDestinations.Search,
            SoundMatchBottomNavigationDestinations.Premium
        )
    }
    val navController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        // the playbackState.currentlyPlayingTrack will automatically be set
        // to null when the playback is stopped
        SoundMatchNavigation(
            navController = navController,
            playStreamable = playbackViewModel::playStreamable,
            isFullScreenNowPlayingOverlayScreenVisible = isNowPlayingScreenVisible,
            onPausePlayback = playbackViewModel::pauseCurrentlyPlayingTrack
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            AnimatedContent(
                modifier = Modifier.fillMaxWidth(),
                targetState = miniPlayerStreamable
            ) { state ->
                if (state == null) {
                    SnackbarHost(hostState = snackbarHostState)
                } else {
                    ExpandableMiniPlayerWithSnackbar(
                        modifier = Modifier
                            .animateEnterExit(
                                enter = fadeIn() + slideInVertically { it },
                                exit = fadeOut() + slideOutVertically { -it }
                            ),
                        streamable = miniPlayerStreamable!!,
                        onPauseButtonClicked = playbackViewModel::pauseCurrentlyPlayingTrack,
                        onPlayButtonClicked = playbackViewModel::resumeIfPausedOrPlay,
                        isPlaybackPaused = isPlaybackPaused,
                        timeElapsedStringFlow = playbackViewModel.flowOfProgressTextOfCurrentTrack.value,
                        playbackProgressFlow = playbackViewModel.flowOfProgressOfCurrentTrack.value,
                        totalDurationOfCurrentTrackText = playbackViewModel.totalDurationOfCurrentTrackTimeText.value,
                        snackbarHostState = snackbarHostState
                    )
                }
            }

            SoundMatchBottomNavigationConnectedWithBackStack(
                navController = navController,
                modifier = Modifier.navigationBarsPadding(),
                navigationItems = bottomNavigationItems,
            )
        }
    }
}
