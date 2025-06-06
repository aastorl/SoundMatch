package com.example.soundmatch.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.soundmatch.data.repositories.podcastsrepository.PodcastsRepository
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.PodcastEpisode
import com.example.soundmatch.domain.PodcastShow
import com.example.soundmatch.ui.navigation.SoundMatchNavigationDestinations
import com.example.soundmatch.usecases.getCurrentlyPlayingEpisodePlaybackStateUseCase.GetCurrentlyPlayingEpisodePlaybackStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PodcastShowDetailViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    getCurrentlyPlayingEpisodePlaybackStateUseCase: GetCurrentlyPlayingEpisodePlaybackStateUseCase,
    private val podcastsRepository: PodcastsRepository
) : AndroidViewModel(application) {

    enum class UiState { IDLE, LOADING, PLAYBACK_LOADING, ERROR }

    private val showId =
        savedStateHandle.get<String>(SoundMatchNavigationDestinations.PodcastShowDetailScreen.NAV_ARG_PODCAST_SHOW_ID)!!
    val episodesForShowStream = podcastsRepository.getPodcastEpisodesStreamForPodcastShow(
        showId = showId,
        countryCode = getCountryCode()
    )

    var currentlyPlayingEpisode by mutableStateOf<PodcastEpisode?>(null)
        private set

    var uiState by mutableStateOf(UiState.IDLE)
        private set

    var podcastShow by mutableStateOf<PodcastShow?>(null)
        private set

    var isCurrentlyPlayingEpisodePaused by mutableStateOf<Boolean?>(null)
        private set

    init {
        fetchShowUpdatingUiState()
        getCurrentlyPlayingEpisodePlaybackStateUseCase
            .currentlyPlayingEpisodePlaybackStateStream
            .onEach { playbackState ->
                when (playbackState) {
                    is GetCurrentlyPlayingEpisodePlaybackStateUseCase.PlaybackState.Ended -> {
                        isCurrentlyPlayingEpisodePaused = null
                        currentlyPlayingEpisode = null
                    }
                    is GetCurrentlyPlayingEpisodePlaybackStateUseCase.PlaybackState.Loading -> {
                        uiState = UiState.PLAYBACK_LOADING
                    }
                    is GetCurrentlyPlayingEpisodePlaybackStateUseCase.PlaybackState.Paused -> {
                        currentlyPlayingEpisode = playbackState.pausedEpisode
                        isCurrentlyPlayingEpisodePaused = true
                    }
                    is GetCurrentlyPlayingEpisodePlaybackStateUseCase.PlaybackState.Playing -> {
                        if (uiState != UiState.IDLE) uiState = UiState.IDLE
                        if (isCurrentlyPlayingEpisodePaused == null || isCurrentlyPlayingEpisodePaused == true) {
                            isCurrentlyPlayingEpisodePaused = false
                        }
                        currentlyPlayingEpisode = playbackState.playingEpisode
                    }
                    else -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    fun retryFetchingShow() {
        fetchShowUpdatingUiState()
    }

    private fun fetchShowUpdatingUiState() {
        viewModelScope.launch {
            uiState = UiState.LOADING
            val result = podcastsRepository.fetchPodcastShow(
                showId = showId,
                countryCode = getCountryCode()
            )
            if (result is FetchedResource.Success) {
                uiState = UiState.IDLE
                podcastShow = result.data
            } else {
                uiState = UiState.ERROR
            }
        }
    }
}