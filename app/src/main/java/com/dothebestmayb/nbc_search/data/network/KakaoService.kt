package com.dothebestmayb.nbc_search.data.network

import com.dothebestmayb.nbc_search.data.model.ImageSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoService {

    @GET("v2/search/image")
    suspend fun getImage(
        @Query("query") query: String,
        @Query("sort") sort: String = "accuracy",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 30,
    ): ImageSearchResponse
}