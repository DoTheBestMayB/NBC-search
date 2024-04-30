package com.dothebestmayb.nbc_search.data.datasource

import com.dothebestmayb.nbc_search.data.model.ImageItemResponse

interface KakaoRemoteDataSource {
    suspend fun getItem(query: String): List<ImageItemResponse>
}