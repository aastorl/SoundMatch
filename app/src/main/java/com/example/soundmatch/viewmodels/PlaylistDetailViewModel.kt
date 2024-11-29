package com.example.soundmatch.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.soundmatch.data.repositories.tracksrepository.TracksRepository
import com.example.soundmatch.ui.navigation.SoundMatchNavigationDestinations
import com.example.soundmatch.usecases.getCurrentlyPlayingTrackUseCase.GetCurrentlyPlayingTrackUseCase
import com.example.soundmatch.usecases.getPlaybackLoadingStatusUseCase.GetPlaybackLoadingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
    tracksRepository: TracksRepository,
    getCurrentlyPlayingTrackUseCase: GetCurrentlyPlayingTrackUseCase,
    getPlaybackLoadingStatusUseCase: GetPlaybackLoadingStatusUseCase,
) : AndroidViewModel(application) {
    private val playlistId =
        savedStateHandle.get<String>(SoundMatchNavigationDestinations.PlaylistDetailScreen.NAV_ARG_PLAYLIST_ID)!!
    val playbackLoadingStateStream = getPlaybackLoadingStatusUseCase.loadingStatusStream
    val currentlyPlayingTrackStream = getCurrentlyPlayingTrackUseCase.currentlyPlayingTrackStream
    val tracks = tracksRepository.getPaginatedStreamForPlaylistTracks(
        playlistId = playlistId,
        countryCode = getCountryCode()
    ).cachedIn(viewModelScope)
}