package com.example.soundmatch.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.soundmatch.ui.navigation.SoundMatchBottomNavigationDestinations

/**
 * An object that contains constants related to the [SoundMatchBottomNavigation]
 * composable.
 */
object SoundMatchBottomNavigationConstants {
    val navigationHeight = 60.dp
}

/**
 * A bottom navigation bar composable with a background gradient.
 * Note: The bottom navigation bar has a fixed height of 80dp.
 *
 * @param navigationItems the [SoundMatchBottomNavigationDestinations] to
 * display in the navigation bar.
 * @param currentlySelectedItem the currently selected [SoundMatchBottomNavigationDestinations].
 * The currently selected item will be highlighted and will also use the
 * [SoundMatchBottomNavigationDestinations.filledIconVariantResourceId] for the image resource.
 * @param onItemClick the lambda to execute when an item is clicked. A reference to
 * an instance of [SoundMatchBottomNavigationDestinations] that was clicked will be provided
 * as a parameter to the lambda.
 * @param modifier the modifier to be applied to the navigation bar. The height of the
 * composable is fixed at 80dp.
 */
@Composable
fun SoundMatchBottomNavigation(
    navigationItems: List<SoundMatchBottomNavigationDestinations>,
    currentlySelectedItem: SoundMatchBottomNavigationDestinations,
    onItemClick: (SoundMatchBottomNavigationDestinations) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gradientBrush = remember {
        Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to Color.Black,
                0.3f to Color.Black.copy(alpha = 0.9f),
                0.5f to Color.Black.copy(alpha = 0.8f),
                0.7f to Color.Black.copy(alpha = 0.6f),
                0.9f to Color.Black.copy(alpha = 0.2f),
                1f to Color.Transparent
            ),
            startY = Float.POSITIVE_INFINITY,
            endY = 0.0f
        )
    }

    // Surface with gradient background and elevation 0.dp
    Surface(
        modifier = modifier
            .background(gradientBrush),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(SoundMatchBottomNavigationConstants.navigationHeight)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Loop over navigationItems and render NavigationBarItem for each
            navigationItems.forEach {
                NavigationBarItem(
                    selected = it == currentlySelectedItem,
                    onClick = { onItemClick(it) },
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = if (it == currentlySelectedItem) it.filledIconVariantResourceId
                                else it.outlinedIconVariantResourceId
                            ),
                            contentDescription = null
                        )
                    },
                    label = { Text(text = it.label) }
                )
            }
        }
    }
}
