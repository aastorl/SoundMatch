package com.example.soundmatch.data.repositories.genresrepository

import com.example.soundmatch.data.remote.musicservice.SupportedSpotifyGenres
import com.example.soundmatch.data.remote.musicservice.toGenre
import com.example.soundmatch.domain.Genre
import javax.inject.Inject

class SoundMatchGenresRepository @Inject constructor() : GenresRepository {
    override fun fetchAvailableGenres(): List<Genre> = SupportedSpotifyGenres.entries.map {
        it.toGenre()
    }
}