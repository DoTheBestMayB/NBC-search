package com.dothebestmayb.nbc_search.presentation.di

import com.dothebestmayb.nbc_search.data.datasource.KakaoRemoteDataSource
import com.dothebestmayb.nbc_search.data.datasource.KakaoRemoteDataSourceImpl
import com.dothebestmayb.nbc_search.data.network.KakaoService
import com.dothebestmayb.nbc_search.data.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return RetrofitClient.retrofitClient
    }

    @Provides
    fun provideKakaoService(retrofit: Retrofit): KakaoService {
        return retrofit.create(KakaoService::class.java)
    }

    @Provides
    fun provideKakaoRemoteDatsSource(kakaoService: KakaoService): KakaoRemoteDataSource {
        return KakaoRemoteDataSourceImpl(kakaoService)
    }
}