package com.dothebestmayb.nbc_search.data.retrofit

interface SearchQueryRepository {

    suspend fun read(key: String): String?

    suspend fun save(key: String, query: String)
}