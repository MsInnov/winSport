package com.mscode.domain.leagueteam.usecase

import com.mscode.domain.leagueteam.model.LeagueTeam

class GetEveryOtherLeagueTeamUseCase {

    operator fun invoke(teams: List<LeagueTeam>) = teams.filterIndexed { index, _ -> index % 2 == 0 }

}