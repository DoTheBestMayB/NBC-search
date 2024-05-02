package com.dothebestmayb.nbc_search.presentation.model

import java.util.Date

sealed interface SearchListItem {

    val siteName: String
    val thumbnail: String
    val date: Date
    val bookmarked: Boolean

    data class ImageItem(
        override val siteName: String,
        override val thumbnail: String,
        override val date: Date,
        override val bookmarked: Boolean = false,
    ) : SearchListItem
}