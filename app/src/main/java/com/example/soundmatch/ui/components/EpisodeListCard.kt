package com.example.soundmatch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.soundmatch.domain.SearchResult
import com.example.soundmatch.domain.getFormattedDateAndDurationString

/**
 * A compact card that is used to display the information of a specific
 * [SearchResult.EpisodeSearchResult], arranged in a row-wise manner.
 * This composable, as the name implies, is meant to be mainly used in lists.
 * It is also important to note that **this composable also has a fixed height.**
 *
 * @param episodeSearchResult the instance of [SearchResult.EpisodeSearchResult]
 * that will be used by the composable.
 * @param onClick the lambda to execute when this card is clicked.
 * @param modifier the modifier to be applied to this composable. The composable
 * has a fixed height which cannot be changed.
 * @param backgroundColor the background [Color] to be applied to this composable.
 */

@Composable
fun EpisodeListCard(
    episodeSearchResult: SearchResult.EpisodeSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
) {
    val context = LocalContext.current
    val dateAndDurationString = remember(episodeSearchResult) {
        episodeSearchResult.getFormattedDateAndDurationString(context)
    }
    var isLoadingPlaceholderVisible by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .height(114.dp)
            .then(modifier)
        .clickable(onClick = onClick), // Use Modifier.clickable to handle clicks, due to Material 3 migration
    colors = CardDefaults.cardColors(
        containerColor = backgroundColor
    )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImageWithPlaceholder(
                modifier = Modifier
                    .size(98.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = episodeSearchResult.episodeContentInfo.imageUrlString,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                onImageLoading = {
                    if (!isLoadingPlaceholderVisible) isLoadingPlaceholderVisible = true
                },
                onImageLoadingFinished = { isLoadingPlaceholderVisible = false },
                isLoadingPlaceholderVisible = isLoadingPlaceholderVisible
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = episodeSearchResult.episodeContentInfo.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = episodeSearchResult.episodeContentInfo.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f)
                    ),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = dateAndDurationString,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f)
                    ),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}