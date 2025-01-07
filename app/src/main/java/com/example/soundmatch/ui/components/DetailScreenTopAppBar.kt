package com.example.soundmatch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.soundmatch.ui.dynamicTheme.dynamicbackgroundmodifier.DynamicBackgroundResource
import com.example.soundmatch.ui.theme.SoundMatchTheme

/**
 * An appbar that is meant to be used in a detail screen. It is mainly
 * used to display the [title] with a back button. This overload
 * uses the [Modifier.dynamicBackground] modifier.
 *
 * @param title the title to be displayed.
 * @param onBackButtonClicked the lambda to execute with the user clicks
 * on the back button.
 * @param modifier the modifier to be applied to the app bar.
 * @param onClick the lambda to execute when the app bar clicked. This is
 * usually used to scroll a list to the first item.
 * @param dynamicBackgroundResource the resource from which the background
 * color would be extracted from. By default, it is set to [DynamicBackgroundResource.Empty].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenTopAppBar(
    title: String,
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    dynamicBackgroundResource: DynamicBackgroundResource = DynamicBackgroundResource.Empty
) {
    val backgroundColor = Color.Transparent // Adjust this if you have a dynamic color for the background.

    // TopAppBar from Material 3
    TopAppBar(
        modifier = modifier
            .clickable(onClick = onClick),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = backgroundColor
        ),
        actions = {
            // Add any action icons if needed.
        },
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_chevron_left_24),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Preview
@Composable
fun DetailScreenTopAppBarPreview() {
    SoundMatchTheme {
        DetailScreenTopAppBar(
            title = "Title",
            onBackButtonClicked = {},
            dynamicBackgroundResource = DynamicBackgroundResource.Empty
        )
    }
}