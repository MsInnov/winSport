package com.mscode.data.leagueteam.mapper

import com.mscode.data.leagueteam.model.LeagueTeamsEntity
import com.mscode.domain.leagueteam.model.LeagueTeam
import com.mscode.domain.leagueteam.model.LeagueTeams

class LeagueTeamMapper {

    fun toLeagueTeams(
        leagueTeamsEntity: LeagueTeamsEntity
    ) = LeagueTeams(
        leagueTeamsEntity.teams.map {
            LeagueTeam(
                idTeam = it.idTeam,
                strTeam = it.strTeam,
                strBadge = it.strBadge,
                strLogo = it.strLogo
            )
        }
    )
}