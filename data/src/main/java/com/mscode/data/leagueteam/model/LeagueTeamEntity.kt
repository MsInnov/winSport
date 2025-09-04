package com.mscode.data.leagueteam.model

data class LeagueTeamsEntity(
    val teams: List<LeagueTeamEntity>
)

data class LeagueTeamEntity(
    val idTeam: String,
    val strTeam: String,
    val strLogo: String?,
    val strBadge: String?,
)