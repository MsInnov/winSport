package com.mscode.domain.leagueteam.usecase

import com.mscode.domain.leagueteam.model.LeagueTeam

class SortLeagueTeamsByNameDescendingUseCase {

    operator fun invoke(teams: List<LeagueTeam>) = teams.sortedByDescending{it.strTeam}

}