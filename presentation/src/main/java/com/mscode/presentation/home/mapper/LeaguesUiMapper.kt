package com.mscode.presentation.home.mapper

import com.mscode.domain.availableleagues.model.League
import com.mscode.presentation.home.model.UiLeague

class LeaguesUiMapper {

    fun toUiLeague(
        leagues: List<League>,
    ) = leagues.map { league ->
        UiLeague(
            league = league.league,
            sport = league.sport
        )
    }

}