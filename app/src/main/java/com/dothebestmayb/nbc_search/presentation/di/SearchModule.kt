package com.dothebestmayb.nbc_search.presentation.di

import com.dothebestmayb.nbc_search.data.datasource.KakaoRemoteDataSource
import com.dothebestmayb.nbc_search.data.retrofit.KakaoSearchRepository
import com.dothebestmayb.nbc_search.data.retrofit.KakaoSearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SearchModule {

    @Provides
    @ViewModelScoped
    fun provideKakaoSearchRepository(kakaoRemoteDataSource: KakaoRemoteDataSource): KakaoSearchRepository {
        return KakaoSearchRepositoryImpl(kakaoRemoteDataSource)
    }
}