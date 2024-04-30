package com.dothebestmayb.nbc_search.presentation.model

import java.util.Date

sealed interface SearchListItem {

    val id: String
    val siteName: String
    val thumbnail: String
    val date: Date
    val bookmarked: Boolean

    data class ImageItem(
        override val id: String,
        override val siteName: String,
        override val thumbnail: String,
        override val date: Date,
        override val bookmarked: Boolean = false,
    ) : SearchListItem
}