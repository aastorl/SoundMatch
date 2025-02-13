package com.example.soundmatch.viewmodels.homefeedviewmodel

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundmatch.SoundMatchApplication
import com.example.soundmatch.data.repositories.homefeedrepository.HomeFeedRepository
import com.example.soundmatch.data.repositories.homefeedrepository.ISO6391LanguageCode
import com.example.soundmatch.data.utils.FetchedResource
import com.example.soundmatch.domain.*
import com.example.soundmatch.viewmodels.homefeedviewmodel.HomeFeedError.NetworkError
import com.example.soundmatch.viewmodels.homefeedviewmodel.HomeFeedError.ParsingError
import com.example.soundmatch.viewmodels.homefeedviewmodel.greetingphrasegenerator.GreetingPhraseGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

sealed class HomeFeedError {
    object NetworkError : HomeFeedError()
    object ParsingError : HomeFeedError()
    object UnknownError : HomeFeedError()
}

@HiltViewModel
class HomeFeedViewModel @Inject constructor(
    application: Application,
    greetingPhraseGenerator: GreetingPhraseGenerator,
    private val homeFeedRepository: HomeFeedRepository,
) : AndroidViewModel(application) {

    private val _homeFeedCarousels = mutableStateOf<List<HomeFeedCarousel>>(emptyList())
    private val _uiState = mutableStateOf<HomeFeedUiState>(HomeFeedUiState.IDLE)
    val uiState = _uiState as State<HomeFeedUiState>
    val homeFeedCarousels = _homeFeedCarousels as State<List<HomeFeedCarousel>>
    val greetingPhrase = greetingPhraseGenerator.generatePhrase()

    init {
        Log.d("HomeFeedViewModel", "ViewModel initialized")
        fetchAndAssignHomeFeedCarousels()
    }

    private fun fetchAndAssignHomeFeedCarousels() {
        Log.d("HomeFeedViewModel", "fetchAndAssignHomeFeedCarousels called")
        viewModelScope.launch {
            _uiState.value = HomeFeedUiState.LOADING
            Log.d("HomeFeedViewModel", "_uiState set to LOADING")

            val carousels = mutableListOf<HomeFeedCarousel>()
            val languageCode = getApplication<SoundMatchApplication>().resources.configuration.locales[0].language.let(::ISO6391LanguageCode)
            val countryCode = Locale.getDefault().country

            Log.d("HomeFeedViewModel", "Fetching data for country: $countryCode and language: $languageCode")

            val newAlbums = async { homeFeedRepository.fetchNewlyReleasedAlbums(countryCode) }
            val featuredPlaylists = async {
                homeFeedRepository.fetchFeaturedPlaylistsForCurrentTimeStamp(
                    timestampMillis = System.currentTimeMillis(),
                    countryCode = countryCode,
                    languageCode = languageCode
                )
            }
            val categoricalPlaylists = async {
                homeFeedRepository.fetchPlaylistsBasedOnCategoriesAvailableForCountry(
                    countryCode = countryCode, languageCode = languageCode
                )
            }

            featuredPlaylists.awaitFetchedResourceUpdatingUiState {
                it.playlists.map(::toHomeFeedCarouselCardInfo).let { playlistCards ->
                    carousels.add(HomeFeedCarousel("Featured Playlists", "Featured Playlists", playlistCards))
                }
            }

            newAlbums.awaitFetchedResourceUpdatingUiState {
                it.map(::toHomeFeedCarouselCardInfo).let { albumCards ->
                    carousels.add(HomeFeedCarousel("Newly Released Albums", "Newly Released Albums", albumCards))
                }
            }

            categoricalPlaylists.awaitFetchedResourceUpdatingUiState {
                it.map { category -> category.toHomeFeedCarousel() }.forEach(carousels::add)
            }

            _homeFeedCarousels.value = carousels
            _uiState.value = HomeFeedUiState.IDLE
        }
    }

    fun refreshFeed() {
        if (_uiState.value == HomeFeedUiState.LOADING) return
        viewModelScope.launch { fetchAndAssignHomeFeedCarousels() }
    }

    private fun logCountryCodes() {
        val countryCode = Locale.getDefault().country
        Log.d("CountryCode", "Country from Locale: $countryCode")

        val simCountry = getSimCountryCode(getApplication<Application>())
        Log.d("CountryCode", "Country from SIM: $simCountry")

        val systemCountry = getSystemCountryCode(getApplication<Application>())
        Log.d("CountryCode", "Country from System Settings: $systemCountry")
    }

    private fun getSimCountryCode(context: Context): String? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.simCountryIso
    }

    private fun getSystemCountryCode(context: Context): String {
        return Locale.getDefault().country
    }

    private suspend fun <FetchedResourceType> Deferred<FetchedResource<FetchedResourceType, SoundMatchErrorType>>.awaitFetchedResourceUpdatingUiState(
        onSuccess: (FetchedResourceType) -> Unit
    ) {
        awaitFetchedResource(onError = {
            Log.e("HomeFeedViewModel", "Error fetching data: $it")
            val homeFeedError = when (it) {
                is NetworkError -> HomeFeedError.NetworkError
                is ParsingError -> HomeFeedError.ParsingError
                else -> HomeFeedError.UnknownError
            }
            if (_uiState.value == HomeFeedUiState.ERROR) return@awaitFetchedResource
            _uiState.value = HomeFeedUiState.ERROR
        }, onSuccess = {
            onSuccess(it)
            if (_uiState.value == HomeFeedUiState.IDLE) return@awaitFetchedResource
            _uiState.value = HomeFeedUiState.IDLE
        })
    }

    private suspend fun <FetchedResourceType> Deferred<FetchedResource<FetchedResourceType, SoundMatchErrorType>>.awaitFetchedResource(
        onError: (SoundMatchErrorType) -> Unit, onSuccess: (FetchedResourceType) -> Unit
    ) {
        val fetchedResourceResult = this.await()
        if (fetchedResourceResult !is FetchedResource.Success) {
            onError((fetchedResourceResult as FetchedResource.Failure).cause)
            return
        }
        onSuccess(fetchedResourceResult.data)
    }

    private fun toHomeFeedCarouselCardInfo(searchResult: SearchResult): HomeFeedCarouselCardInfo =
        when (searchResult) {
            is SearchResult.AlbumSearchResult -> HomeFeedCarouselCardInfo(
                id = searchResult.id,
                imageUrlString = searchResult.albumArtUrlString,
                caption = searchResult.name,
                associatedSearchResult = searchResult
            )
            is SearchResult.PlaylistSearchResult -> HomeFeedCarouselCardInfo(
                id = searchResult.id,
                imageUrlString = searchResult.imageUrlString ?: "",
                caption = searchResult.name,
                associatedSearchResult = searchResult
            )
            else -> throw IllegalArgumentException("Unsupported SearchResult type: ${searchResult::class.java}")
        }

    enum class HomeFeedUiState { IDLE, LOADING, ERROR }
}


