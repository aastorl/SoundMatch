package com.example.soundmatch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * A card meant to be used in the home feed.
 * Note: The width of the composable is fixed at 170dp.
 *
 * @param imageUrlString the url of the image.
 * @param caption the text to be displayed at the bottom of the image.
 * @param onClick the lambda to execute when the item is clicked
 * @param modifier the modifier to be applied to the composable. The
 * width is fixed at 170dp.
 */

@Composable
fun HomeFeedCard(
    imageUrlString: String,
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .widthIn(min = 160.dp, max = 160.dp)
            .height(IntrinsicSize.Min)
            .then(modifier)
            .clickable(onClick = onClick), //
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // M3 Colors
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // M3 Elevation
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                model = imageUrlString,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                text = caption,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f) // Similar a ContentAlpha.medium
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis // Large Text
            )
        }
    }
}