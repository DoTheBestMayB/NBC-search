package com.dothebestmayb.nbc_search.presentation.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dothebestmayb.nbc_search.data.datasource.KakaoRemoteDataSource
import com.dothebestmayb.nbc_search.data.retrofit.KakaoSearchRepository
import com.dothebestmayb.nbc_search.data.retrofit.KakaoSearchRepositoryImpl
import com.dothebestmayb.nbc_search.data.retrofit.SearchQueryRepository
import com.dothebestmayb.nbc_search.data.retrofit.SearchQueryRepositoryImpl
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

    @Provides
    @ViewModelScoped
    fun provideSearchQueryRepository(dataStore: DataStore<Preferences>): SearchQueryRepository {
        return SearchQueryRepositoryImpl(dataStore)
    }
}