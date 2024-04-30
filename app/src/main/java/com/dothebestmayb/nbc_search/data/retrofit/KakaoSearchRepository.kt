package com.dothebestmayb.nbc_search.data.retrofit

import com.dothebestmayb.nbc_search.presentation.model.SearchListItem

interface KakaoSearchRepository {

    suspend fun getImage(query: String): List<SearchListItem.ImageItem>

}