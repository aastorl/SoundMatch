package com.example.soundmatch.ui.screens.searchscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.soundmatch.domain.Genre

/**
 * A [LazyVerticalGrid] that displays the [availableGenres].
 * @param availableGenres the genres that are to be displayed.
 * @param onGenreItemClick the lambda to execute when a [Genre] item
 * is clicked. This lambda also provides an instance of [Genre] that
 * indicates the item that was clicked.
 * @param modifier the modifier to be applied to the grid.
 */

@Composable
fun GenresGrid(
    availableGenres: List<Genre>,
    onGenreItemClick: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(170.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item(span = { GridItemSpan(this.maxCurrentLineSpan) }) {
            Text(
                text = "Genres",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
        }
        items(items = availableGenres) {
            GenreCard(
                genre = it,
                modifier = Modifier.height(120.dp),
                onClick = { onGenreItemClick(it) },
                imageResourceId = it.genreType.getAssociatedImageResource(),
                backgroundColor = it.genreType.getAssociatedBackgroundColor()
            )
        }
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            Spacer(
                modifier = Modifier
                    .height(SoundMatchBottomNavigationConstants.navigationHeight + SoundMatchMiniPlayerConstants.miniPlayerHeight)
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
            )
        }
    }
}