package com.example.soundmatch.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.soundmatch.ui.components.SoundMatchBottomNavigation

/**
 * A composable that takes care of setting the active icon of the bottom
 * navigation composable based on the currentBackStackEntry of the [navController].
 * This composable acts as a recomposition scope. Therefore, reading the
 * state value of [NavHostController.currentBackStackEntryAsState()] will result
 * in the recomposition of only this composable.
 */
@Composable
fun SoundMatchBottomNavigationConnectedWithBackStack(
    navController: NavHostController,
    navigationItems: List<SoundMatchBottomNavigationDestinations>,
    modifier: Modifier = Modifier
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    var previousValidBottomNavigationDestination by remember {
        mutableStateOf<SoundMatchBottomNavigationDestinations>(SoundMatchBottomNavigationDestinations.Home)
    }
    val currentlySelectedItem by remember {
        derivedStateOf {
            // if the most recent backstack entry is null or it's route
            // doesn't match with any of the routes defined in
            // SoundMatchBottomNavigationDestinations sealed classes, then
            // use the previously found valid bottom navigation destination
            // as the currently selected item.
            // If it does match one of the routes defined by one of the classes that
            // is a child of SoundMatchBottomNavigationDestinations sealed class,
            // then store it in a variable and update the state.
            // Essentially, it is picking the most recent valid route that
            // matches the route of a class that is a child of SoundMatchBottomNavigationDestinations
            // sealed class.
            currentBackStackEntry?.let {
                val route = if (it.isInNestedNavGraph) it.destination.parent?.route
                else it.destination.route
                previousValidBottomNavigationDestination = when (route) {
                    SoundMatchBottomNavigationDestinations.Home.route -> SoundMatchBottomNavigationDestinations.Home
                    SoundMatchBottomNavigationDestinations.Search.route -> SoundMatchBottomNavigationDestinations.Search
                    SoundMatchBottomNavigationDestinations.Premium.route -> SoundMatchBottomNavigationDestinations.Premium
                    else -> previousValidBottomNavigationDestination
                }
                previousValidBottomNavigationDestination
            } ?: previousValidBottomNavigationDestination
        }
    }
    SoundMatchBottomNavigation(
        modifier = modifier,
        navigationItems = navigationItems,
        currentlySelectedItem = currentlySelectedItem,
        onItemClick = { bottomNavigationDestination ->
            if (
                bottomNavigationDestination == currentlySelectedItem &&
                navController.currentDestinationRoute !=
                navController.parentOfCurrentDestination?.startDestinationRoute
            ) {
                // pop the backstack if the user has clicked on the same destination
                // and the currently visible destination is not the first destination
                // of the nested navigation graph it belongs.
                // This will allow the user to tap a bottom navigation icon more than
                // once to navigate out of a detail screen.
                navController.popBackStack()
            } else {
                navController.navigate(bottomNavigationDestination.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        // Save backstack state. This will ensure restoration of
                        // nested navigation screen when the user comes back to
                        // the destination.
                        saveState = true
                    }
                    // prevent duplicate destinations when the navigation is
                    // clicked multiple times
                    launchSingleTop = true
                    // restore state if previously saved
                    restoreState = true
                }
            }
        }
    )
}

/**
 * A [Boolean] that indicates whether the current backstack entry is part
 * of a nested NavGraph.
 */
private val NavBackStackEntry.isInNestedNavGraph get() = this.destination.parent?.parent != null

/**
 * A [String] that returns the route of the [NavHostController.currentDestination].
 */
private val NavHostController.currentDestinationRoute: String? get() = this.currentDestination?.route

/**
 * A [String] that returns the parent navigation graph of the [NavHostController.currentDestination].
 */
private val NavHostController.parentOfCurrentDestination: NavGraph? get() = this.currentDestination?.parent