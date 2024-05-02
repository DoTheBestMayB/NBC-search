package com.dothebestmayb.nbc_search.data.retrofit

import com.dothebestmayb.nbc_search.data.datasource.KakaoRemoteDataSource
import com.dothebestmayb.nbc_search.presentation.model.SearchListItem

class KakaoSearchRepositoryImpl(
    private val kakaoRemoteDataSource: KakaoRemoteDataSource
) : KakaoSearchRepository {

    override suspend fun getImage(query: String): List<SearchListItem.ImageItem> {
        return kakaoRemoteDataSource.getItem(query).map {
            it.toEntity()
        }
    }
}