package com.mscode.data.availableleagues.model

data class LeaguesEntity(
    val leagues: List<LeagueEntity>
)

data class LeagueEntity(
    val idLeague: String,
    val strLeague: String,
    val strSport: String
)