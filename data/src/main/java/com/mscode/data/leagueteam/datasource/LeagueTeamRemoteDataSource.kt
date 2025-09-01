package com.mscode.data.leagueteam.datasource

import com.mscode.data.leagueteam.api.LeagueTeamApi
import com.mscode.data.leagueteam.model.LeagueTeamsEntity
import com.mscode.domain.common.WrapperResults

class LeagueTeamRemoteDataSource(
    private val api: LeagueTeamApi
) {

    suspend fun getLeagueTeam(
        apiKey: String,
        path: String,
        leagueName: String
    ): WrapperResults<LeagueTeamsEntity> = try {
        api.getLeagueTeam(apiKey, path, leagueName).body()?.let {
            WrapperResults.Success(it)
        } ?: run {
            WrapperResults.Error(Exception())
        }
    } catch (e: Exception) {
        WrapperResults.Error(e)
    }

}