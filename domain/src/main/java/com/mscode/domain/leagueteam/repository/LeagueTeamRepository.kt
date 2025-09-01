package com.mscode.domain.leagueteam.repository

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.leagueteam.model.LeagueTeams

interface LeagueTeamRepository {

    suspend fun getLeagueTeam(league: String): WrapperResults<LeagueTeams>

}