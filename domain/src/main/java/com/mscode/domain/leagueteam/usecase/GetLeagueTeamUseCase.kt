package com.mscode.domain.leagueteam.usecase

import com.mscode.domain.leagueteam.repository.LeagueTeamRepository

class GetLeagueTeamUseCase(
    private val repository: LeagueTeamRepository
) {

    suspend operator fun invoke(league: String) = repository.getLeagueTeam(league)

}