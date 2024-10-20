package com.example.soundmatch.domain


data class Genre(
    val id: Int,
    val label: String,
    val genreType: GenreType
) {
    enum class GenreType {
        AMBIENT,
        CHILL,
        CLASSICAL,
        DANCE,
        ELECTRONIC,
        METAL,
        RAINY_DAY,
        ROCK,
        PIANO,
        POP,
        SLEEP
    }
}
/**
 * A mapper function used to map an enum of type [Genre.GenreType] to
 * the corresponding enum of type [SupportedSpotifyGenres].
 */
fun Genre.GenreType.toSupportedSpotifyGenreType() = when(this) {
    Genre.GenreType.AMBIENT -> SupportedSpotifyGenres.AMBIENT
    Genre.GenreType.CHILL -> SupportedSpotifyGenres.CHILL
    Genre.GenreType.CLASSICAL -> SupportedSpotifyGenres.CLASSICAL
    Genre.GenreType.DANCE -> SupportedSpotifyGenres.DANCE
    Genre.GenreType.ELECTRONIC -> SupportedSpotifyGenres.ELECTRONIC
    Genre.GenreType.METAL -> SupportedSpotifyGenres.METAL
    Genre.GenreType.RAINY_DAY -> SupportedSpotifyGenres.RAINY_DAY
    Genre.GenreType.ROCK -> SupportedSpotifyGenres.ROCK
    Genre.GenreType.PIANO -> SupportedSpotifyGenres.PIANO
    Genre.GenreType.POP -> SupportedSpotifyGenres.POP
    Genre.GenreType.SLEEP -> SupportedSpotifyGenres.SLEEP
}