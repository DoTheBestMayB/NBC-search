package com.dothebestmayb.nbc_search.data.network

import com.dothebestmayb.nbc_search.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://dapi.kakao.com/"

    private val moshi = Moshi.Builder()
        .add(DateAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val apiInterceptor = Interceptor {
        val originalRequest = it.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "KakaoAK %s".format(BuildConfig.KAKAO_API_KEY))
            .build()
        it.proceed(newRequest)
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(apiInterceptor)
        .build()

    val retrofitClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}