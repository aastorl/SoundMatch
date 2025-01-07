package com.example.soundmatch.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

/**
 * A safer extension function for [LazyListScope] to display a list of items
 * from [LazyPagingItems]. This ensures null values are not passed to the
 * [itemContent] lambda, avoiding potential crashes or unnecessary null checks
 * inside the composable.
 *
 * Note: This function handles nullable items gracefully by checking if the
 * item at a given index is non-null before invoking the [itemContent].
 * If the item is null, it will simply skip rendering that item.
 *
 * Usage:
 * ```
 * itemsSafe(myPagingItems) { item ->
 *     MyComposable(item)
 * }
 * ```
 *
 * @param items The [LazyPagingItems] containing the data to be displayed in the list.
 * @param itemContent The composable to render each non-null item in the list.
 */

fun <T : Any> LazyListScope.itemsSafe(
    items: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T) -> Unit
) {
    items(items.itemCount) { index ->
        items[index]?.let { itemContent(it) }
    }
}

fun <T : Any> LazyListScope.itemsSafe(
    items: List<T>,
    itemContent: @Composable LazyItemScope.(value: T) -> Unit
) {
    itemsIndexed(items) { index, item ->
        itemContent(item)
    }
}


