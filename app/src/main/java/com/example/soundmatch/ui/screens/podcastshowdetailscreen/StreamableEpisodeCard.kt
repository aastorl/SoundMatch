package com.example.soundmatch.ui.screens.podcastshowdetailscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.soundmatch.domain.PodcastEpisode
import com.example.soundmatch.domain.getFormattedDateAndDurationString
import com.example.soundmatch.ui.components.AsyncImageWithPlaceholder
import com.example.soundmatch.R

@Composable
fun StreamableEpisodeCard(
    episode: PodcastEpisode,
    isEpisodePlaying: Boolean,
    isCardHighlighted: Boolean,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    StreamableEpisodeCard(
        isEpisodePlaying = isEpisodePlaying,
        isCardHighlighted = isCardHighlighted,
        onPlayButtonClicked = onPlayButtonClicked,
        onPauseButtonClicked = onPauseButtonClicked,
        onClicked = onClicked,
        thumbnailImageUrlString = episode.episodeImageUrl,
        title = episode.title,
        description = episode.description,
        dateAndDurationString = episode.getFormattedDateAndDurationString(context),
        modifier = modifier
    )
}

@Composable
fun StreamableEpisodeCard(
    isEpisodePlaying: Boolean,
    isCardHighlighted: Boolean,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onClicked: () -> Unit,
    thumbnailImageUrlString: String,
    title: String,
    description: String,
    dateAndDurationString: String,
    modifier: Modifier = Modifier
) {
    var isThumbnailLoading by remember { mutableStateOf(true) }
    val innerColumnVerticalArrangementSpacing = remember { 8.dp }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RectangleShape,
        onClick = onClicked
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(innerColumnVerticalArrangementSpacing)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImageWithPlaceholder(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    model = thumbnailImageUrlString,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onImageLoadingFinished = {
                        if (isThumbnailLoading) isThumbnailLoading = false
                    },
                    isLoadingPlaceholderVisible = isThumbnailLoading,
                    onImageLoading = { isThumbnailLoading = true })
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isCardHighlighted) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dateAndDurationString,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                ),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            // Footer composables are wrapped in a column to avoid being affected by Arrangement.spacedBy().
            Column {
                ActionsRow(
                    isEpisodePlaying = isEpisodePlaying,
                    onPlayButtonClicked = onPlayButtonClicked,
                    onPauseButtonClicked = onPauseButtonClicked,
                    onAddToLibraryButtonClicked = {},
                    onDownloadButtonClicked = {},
                    onShareButtonClick = {},
                    onMoreInfoButtonClick = {}
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun ActionsRow(
    isEpisodePlaying: Boolean,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit,
    onAddToLibraryButtonClicked: () -> Unit,
    onDownloadButtonClicked: () -> Unit,
    onShareButtonClick: () -> Unit,
    onMoreInfoButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.offset(x = (-16).dp) // to accommodate for touch target sizing
        ) {
            IconButton(onClick = onAddToLibraryButtonClicked) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = onDownloadButtonClicked) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_outline_download_for_offline_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = onShareButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = onMoreInfoButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        IconButton(
            onClick = {
                if (isEpisodePlaying) onPauseButtonClicked()
                else onPlayButtonClicked()
            }
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onBackground,
                imageVector = ImageVector
                    .vectorResource(
                        if (isEpisodePlaying) R.drawable.ic_pause_circle_filled_24
                        else R.drawable.ic_play_circle_filled_24
                    ),
                contentDescription = null
            )
        }
    }
}
