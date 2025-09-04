package com.mscode.data.availableleagues.di

import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.availableleagues.datasource.AvailableLeaguesLocalDataSource
import com.mscode.data.availableleagues.mapper.LeagueMapper
import com.mscode.data.availableleagues.repository.AvailableLeaguesRepositoryImpl
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.domain.availableleagues.repository.AvailableLeaguesRepository
import com.mscode.domain.availableleagues.usecase.GetAvailableLeaguesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideLeagueMapper(): LeagueMapper = LeagueMapper()

    @Provides
    @Singleton
    fun provideAvailableLeaguesRepository(
        localConfigDataSource: LocalConfigDataSource,
        retrofit: RetrofitFactory,
        mapper: LeagueMapper,
        availableLeaguesLocalDataSource: AvailableLeaguesLocalDataSource
    ): AvailableLeaguesRepository = AvailableLeaguesRepositoryImpl(localConfigDataSource, retrofit, mapper, availableLeaguesLocalDataSource)

    @Provides
    @Singleton
    fun provideAvailableLeaguesLocalDataSource(): AvailableLeaguesLocalDataSource = AvailableLeaguesLocalDataSource()

    @Provides
    @Singleton
    fun provideGetAvailableLeaguesUseCase(repo: AvailableLeaguesRepository): GetAvailableLeaguesUseCase =
        GetAvailableLeaguesUseCase(repo)

}