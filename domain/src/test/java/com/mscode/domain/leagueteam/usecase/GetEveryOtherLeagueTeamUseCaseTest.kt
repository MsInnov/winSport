package com.mscode.domain.leagueteam.usecase

import com.mscode.domain.leagueteam.model.LeagueTeam
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetEveryOtherLeagueTeamUseCaseTest {

    private lateinit var useCase: GetEveryOtherLeagueTeamUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetEveryOtherLeagueTeamUseCase()
    }

    @Test
    fun `invoke should return every other league team list`() = runTest {
        // Given
        val leagueTeam1 = LeagueTeam(
            idTeam = "1",
            strTeam = "PSG",
            strBadge = null,
            strLogo = null
        )
        val leagueTeam2 = LeagueTeam(
            idTeam = "2",
            strTeam = "Marseille",
            strBadge = null,
            strLogo = null
        )
        val leagueTeam3 = LeagueTeam(
            idTeam = "3",
            strTeam = "Auxerre",
            strBadge = null,
            strLogo = null
        )
        val expected = listOf(leagueTeam1, leagueTeam3)

        // When
        val result = useCase(listOf(leagueTeam1, leagueTeam2, leagueTeam3))

        // Then
        assertEquals(expected,result)
    }

}