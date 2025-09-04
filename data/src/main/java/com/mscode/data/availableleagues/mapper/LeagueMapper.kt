package com.mscode.data.availableleagues.mapper

import com.mscode.data.availableleagues.model.LeagueEntity
import com.mscode.domain.availableleagues.model.League

class LeagueMapper {

    fun toLeague(
        leagueEntity: LeagueEntity
    ) = League(
        id = leagueEntity.idLeague.toInt(),
        league = leagueEntity.strLeague,
        sport = leagueEntity.strSport
    )
}