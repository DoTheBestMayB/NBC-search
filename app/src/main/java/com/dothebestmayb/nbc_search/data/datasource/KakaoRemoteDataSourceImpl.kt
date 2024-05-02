package com.dothebestmayb.nbc_search.data.datasource

import com.dothebestmayb.nbc_search.data.model.ImageItemResponse
import com.dothebestmayb.nbc_search.data.network.KakaoService

class KakaoRemoteDataSourceImpl(
    private val kakaoService: KakaoService,
) : KakaoRemoteDataSource {

    override suspend fun getItem(query: String): List<ImageItemResponse> {
        return kakaoService.getImage(query).items
    }
}