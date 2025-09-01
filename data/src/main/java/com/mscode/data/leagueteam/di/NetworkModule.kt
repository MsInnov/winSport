package com.mscode.data.leagueteam.di

import com.mscode.data.leagueteam.api.LeagueTeamApi
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
    fun provideLeagueTeamApi(retrofit: Retrofit): LeagueTeamApi =
        retrofit.create(LeagueTeamApi::class.java)
}