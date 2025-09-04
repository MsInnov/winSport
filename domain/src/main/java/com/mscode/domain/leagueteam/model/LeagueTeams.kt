package com.mscode.domain.leagueteam.model

data class LeagueTeams(
    val teams: List<LeagueTeam>
)

data class LeagueTeam(
    val idTeam: String,
    val strTeam: String,
    val strLogo: String?,
    val strBadge: String?,
)
