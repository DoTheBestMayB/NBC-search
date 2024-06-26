package com.dothebestmayb.nbc_search.data.model

import com.dothebestmayb.nbc_search.presentation.model.SearchListItem
import com.squareup.moshi.Json
import java.util.Date

data class ImageItemResponse(
    @Json(name = "thumbnail_url") val thumbnailUrl: String,
    @Json(name = "display_sitename") val displaySiteName: String,
    @Json(name = "doc_url") val docUrl: String,
    val datetime: Date,
) {
    fun toEntity(): SearchListItem.ImageItem {
        return SearchListItem.ImageItem(
            siteName = displaySiteName,
            thumbnail = thumbnailUrl,
            date = datetime,
        )
    }
}
