package com.example.soundmatch.ui.navigation

sealed class SoundMatchBottomNavigationDestinations(
    val route: String,
    val label: String,
    val outlinedIconVariantResourceId: Int,
    val filledIconVariantResourceId: Int
) {
    object Home : SoundMatchBottomNavigationDestinations(
        route = "com.example.soundmatch.ui.navigation.bottom.home",
        label = "Home",
        outlinedIconVariantResourceId = R.drawable.ic_outline_home_24,
        filledIconVariantResourceId = R.drawable.ic_filled_home_24
    )

    object Search : SoundMatchBottomNavigationDestinations(
        route = "com.example.soundmatch.ui.navigation.bottom.search",
        label = "Search",
        outlinedIconVariantResourceId = R.drawable.ic_outline_search_24,
        filledIconVariantResourceId = R.drawable.ic_outline_search_24
    )

    object Premium : SoundMatchBottomNavigationDestinations(
        route = "com.example.soundmatch.ui.navigation.bottom.premium",
        label = "Premium",
        outlinedIconVariantResourceId = R.drawable.ic_spotify_premium,
        filledIconVariantResourceId = R.drawable.ic_spotify_premium
    )
}
