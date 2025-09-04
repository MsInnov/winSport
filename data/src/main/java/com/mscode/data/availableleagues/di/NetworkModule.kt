package com.mscode.data.availableleagues.di

import com.mscode.data.availableleagues.api.AvailableLeaguesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAvailableLeaguesApi(retrofit: Retrofit): AvailableLeaguesApi =
        retrofit.create(AvailableLeaguesApi::class.java)
}