package com.mscode.data.leagueteam.datasource

import com.mscode.domain.leagueteam.model.LeagueTeams

class LeagueTeamLocalDataSource {

    private var _leagueTeams: MutableMap<String, LeagueTeams> = mutableMapOf()

    fun getLeagueTeams(league: String): LeagueTeams? = _leagueTeams.get(league)

    fun saveLeagueTeam(leagueTeams: LeagueTeams, league: String) {
        _leagueTeams[league] = leagueTeams
    }

}