package com.mscode.data.leagueteam.di

import com.mscode.data.leagueteam.datasource.LeagueTeamLocalDataSource
import com.mscode.data.leagueteam.mapper.LeagueTeamMapper
import com.mscode.data.leagueteam.repository.LeagueTeamRepositoryImpl
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.domain.leagueteam.repository.LeagueTeamRepository
import com.mscode.domain.leagueteam.usecase.GetEveryOtherLeagueTeamUseCase
import com.mscode.domain.leagueteam.usecase.GetLeagueTeamUseCase
import com.mscode.domain.leagueteam.usecase.SortLeagueTeamsByNameDescendingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideLeagueTeamMapper(): LeagueTeamMapper = LeagueTeamMapper()

    @Provides
    @Singleton
    fun provideLeagueTeamRepository(
        localConfigDataSource: LocalConfigDataSource,
        retrofit: RetrofitFactory,
        mapper: LeagueTeamMapper,
        leagueTeamLocalDataSource: LeagueTeamLocalDataSource
    ): LeagueTeamRepository = LeagueTeamRepositoryImpl(localConfigDataSource, retrofit, mapper, leagueTeamLocalDataSource)

    @Provides
    @Singleton
    fun provideLeagueTeamLocalDataSource(): LeagueTeamLocalDataSource = LeagueTeamLocalDataSource()

    @Provides
    @Singleton
    fun provideGetLeagueTeamUseCase(repo: LeagueTeamRepository): GetLeagueTeamUseCase =
        GetLeagueTeamUseCase(repo)

    @Provides
    @Singleton
    fun provideSortLeagueTeamsByNameDescendingUseCase(): SortLeagueTeamsByNameDescendingUseCase =
        SortLeagueTeamsByNameDescendingUseCase()

    @Provides
    @Singleton
    fun provideGetEveryOtherLeagueTeamUseCase(): GetEveryOtherLeagueTeamUseCase =
        GetEveryOtherLeagueTeamUseCase()

}