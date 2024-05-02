package com.dothebestmayb.nbc_search.presentation.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.dothebestmayb.nbc_search.data.datasource.KakaoRemoteDataSource
import com.dothebestmayb.nbc_search.data.datasource.KakaoRemoteDataSourceImpl
import com.dothebestmayb.nbc_search.data.network.KakaoService
import com.dothebestmayb.nbc_search.data.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import javax.inject.Singleton

private const val SEARCH_QUERY_PREFERENCES_NAME = "search_query_preferences"

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

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, SEARCH_QUERY_PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(SEARCH_QUERY_PREFERENCES_NAME) }
        )
    }

}