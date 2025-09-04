package com.mscode.domain.leagueteam.usecase

import com.mscode.domain.leagueteam.model.LeagueTeam
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SortLeagueTeamsByNameDescendingUseCaseTest {

    private lateinit var useCase: SortLeagueTeamsByNameDescendingUseCase

    @BeforeEach
    fun setUp() {
        useCase = SortLeagueTeamsByNameDescendingUseCase()
    }

    @Test
    fun `invoke should return team sorted by name descending list`() = runTest {
        // Given
        val leagueTeam3 = LeagueTeam(
            idTeam = "3",
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
        val leagueTeam1 = LeagueTeam(
            idTeam = "1",
            strTeam = "Auxerre",
            strBadge = null,
            strLogo = null
        )
        val expected = listOf(leagueTeam3, leagueTeam2, leagueTeam1)

        // When
        val result = useCase(listOf(leagueTeam1, leagueTeam2, leagueTeam3))

        // Then
        assertEquals(expected,result)
    }

}