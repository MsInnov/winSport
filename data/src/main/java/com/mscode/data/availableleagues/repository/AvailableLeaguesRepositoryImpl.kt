package com.mscode.data.availableleagues.repository

import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.availableleagues.api.AvailableLeaguesApi
import com.mscode.data.availableleagues.datasource.AvailableLeaguesLocalDataSource
import com.mscode.data.availableleagues.datasource.AvailableLeaguesRemoteDataSource
import com.mscode.data.availableleagues.mapper.LeagueMapper
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.path_all_leagues
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.availableleagues.model.League
import com.mscode.domain.availableleagues.repository.AvailableLeaguesRepository

class AvailableLeaguesRepositoryImpl(
    private val localConfigDataSource: LocalConfigDataSource,
    private val retrofitFactory: RetrofitFactory,
    private val leagueMapper: LeagueMapper,
    private val availableLeaguesLocalDataSource: AvailableLeaguesLocalDataSource
) : AvailableLeaguesRepository {

    override suspend fun getAvailableLeagues(): WrapperResults<List<League>> {
        if(availableLeaguesLocalDataSource.availableLeagues.isNotEmpty())
            return WrapperResults.Success(availableLeaguesLocalDataSource.availableLeagues)

        val baseUrl = localConfigDataSource.urls.firstOrNull()
            ?: return WrapperResults.Error(Exception("Available Leagues URL missing"))

        val api = retrofitFactory.create(baseUrl.value, AvailableLeaguesApi::class.java)
        val remoteDataSource = AvailableLeaguesRemoteDataSource(api)
        val path = localConfigDataSource.paths.firstOrNull{ it.name == path_all_leagues}?.value
            ?: return WrapperResults.Error(Exception("Available Leagues URL missing"))

        return when (val result = remoteDataSource.getAvailableLeagues("123", path)) {
            is WrapperResults.Success -> {
                val availableLeagues = result.data.leagues.map { availableLeaguesEntity ->
                    leagueMapper.toLeague(
                        availableLeaguesEntity
                    )
                }
                availableLeaguesLocalDataSource.saveAvailableLeagues(availableLeagues)
                WrapperResults.Success(availableLeagues)
            }

            is WrapperResults.Error -> result
        }
    }
}