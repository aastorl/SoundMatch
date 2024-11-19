package com.example.soundmatch.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A filter chip composable.
 * @param text the text that is displayed within chip.
 * @param onClick callback that will be executed when the user clicks
 * the chip.
 * @param modifier Modifier to be applied to the button
 * @param isSelected indicates whether the filter chip is selected or not.
 * Based on the selected state of the composable, different styles will be
 * applied.
 */

@Composable
fun SoundMatchFilterChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = onClick,
        border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            containerColor = MaterialTheme.colorScheme.surface
        ),
        label = {
            Text(text = text, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface)
        }
    )
}