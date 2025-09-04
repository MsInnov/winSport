package com.mscode.domain.leagueteam.usecase

import com.mscode.domain.common.WrapperResults
import com.mscode.domain.leagueteam.model.LeagueTeam
import com.mscode.domain.leagueteam.model.LeagueTeams
import com.mscode.domain.leagueteam.repository.LeagueTeamRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLeagueTeamUseCaseTest {

    private lateinit var repository: LeagueTeamRepository
    private lateinit var useCase: GetLeagueTeamUseCase


    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetLeagueTeamUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Given
        val league = "PSG"
        val leagueTeam = LeagueTeam(
            idTeam = "2",
            strTeam = league,
            strBadge = null,
            strLogo = null
        )
        val leagueTeams = LeagueTeams(
            teams = listOf(leagueTeam)
        )
        coEvery { repository.getLeagueTeam(league) } returns WrapperResults.Success(leagueTeams)

        // When
        val result = useCase(league)

        // Then
        assertTrue(result is WrapperResults.Success)
        coVerify(exactly = 1) { repository.getLeagueTeam(league) }
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val league = "PSG"
        coEvery { repository.getLeagueTeam(league) } returns WrapperResults.Error(Exception("Something went wrong"))

        // When
        val result = useCase(league)

        // Then
        assertTrue(result is WrapperResults.Error)
        coVerify(exactly = 1) { repository.getLeagueTeam(league) }
    }
}