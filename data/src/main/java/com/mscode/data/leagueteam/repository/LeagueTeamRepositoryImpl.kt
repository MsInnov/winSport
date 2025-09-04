package com.mscode.data.leagueteam.repository

import com.mscode.data.leagueteam.api.LeagueTeamApi
import com.mscode.data.leagueteam.datasource.LeagueTeamLocalDataSource
import com.mscode.data.leagueteam.datasource.LeagueTeamRemoteDataSource
import com.mscode.data.leagueteam.mapper.LeagueTeamMapper
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.path_all_leagues
import com.mscode.data.remoteconfig.model.path_search_all_teams
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.leagueteam.model.LeagueTeams
import com.mscode.domain.leagueteam.repository.LeagueTeamRepository

class LeagueTeamRepositoryImpl(
    private val localConfigDataSource: LocalConfigDataSource,
    private val retrofitFactory: RetrofitFactory,
    private val leagueTeamMapper: LeagueTeamMapper,
    private val leagueTeamLocalDataSource: LeagueTeamLocalDataSource
): LeagueTeamRepository {

    override suspend fun getLeagueTeam(league: String) : WrapperResults<LeagueTeams> {
        leagueTeamLocalDataSource.getLeagueTeams(league)?.let {
            return WrapperResults.Success(it)
        }
        val baseUrl = localConfigDataSource.urls.firstOrNull()
            ?: return WrapperResults.Error(Exception("Available Leagues URL missing"))
        val api = retrofitFactory.create(baseUrl.value, LeagueTeamApi::class.java)
        val remoteDataSource = LeagueTeamRemoteDataSource(api)
        val path = localConfigDataSource.paths.firstOrNull{ it.name == path_search_all_teams }?.value
            ?: return WrapperResults.Error(Exception("Available Leagues URL missing"))
        return when (val result = remoteDataSource.getLeagueTeam(baseUrl.keyApi, path, league)) {
            is WrapperResults.Success -> {
                val leagueTeams = leagueTeamMapper.toLeagueTeams(
                    result.data
                )
                leagueTeamLocalDataSource.saveLeagueTeam(leagueTeams, league)
                WrapperResults.Success(leagueTeams)
            }

            is WrapperResults.Error -> result
        }
    }

}