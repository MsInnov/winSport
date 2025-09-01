package com.mscode.presentation.home.mapper

import com.mscode.domain.availableleagues.model.League
import com.mscode.domain.leagueteam.model.LeagueTeam
import com.mscode.presentation.home.model.UiLeague
import com.mscode.presentation.home.model.UiTeam

class LeaguesUiMapper {

    fun toUiLeague(
        leagues: List<League>,
    ) = leagues.map { league ->
        UiLeague(
            league = league.league,
            sport = league.sport
        )
    }

    fun toUiTeam(
        league: LeagueTeam,
    ) = UiTeam(
        name = league.strTeam,
        logo = league.strLogo,
        badge = league.strBadge
    )
}