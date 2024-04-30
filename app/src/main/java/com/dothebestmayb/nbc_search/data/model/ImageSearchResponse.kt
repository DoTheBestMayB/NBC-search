package com.dothebestmayb.nbc_search.data.model

import com.squareup.moshi.Json

data class ImageSearchResponse (
    @Json(name = "meta") val metaResponse: MetaResponse,
    @Json(name = "documents") val items: List<ImageItemResponse>
)
