package com.example.soundmatch.data.repositories.genresrepository

import com.example.soundmatch.domain.Genre

/**
 * A repository that contains all methods related to genres.
 */
interface GenresRepository {
    fun fetchAvailableGenres(): List<Genre>
}