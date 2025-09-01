package com.mscode.data.leagueteam.datasource

import com.mscode.domain.availableleagues.model.League
import com.mscode.domain.leagueteam.model.LeagueTeam
import com.mscode.domain.leagueteam.model.LeagueTeams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LeagueTeamLocalDataSourceTest {

    private lateinit var dataSource: LeagueTeamLocalDataSource

    @BeforeEach
    fun setUp() {
        dataSource = LeagueTeamLocalDataSource()
    }

    @Test
    fun `getLeagueTeams should be null initially`() {
        assertTrue(dataSource.getLeagueTeams("") == null)
    }

    @Test
    fun `saveLeagueTeam should update leagueTeams`() {
        // Given
        val team = "PSG"
        val leagueTeam = LeagueTeam(
            idTeam = "1",
            strTeam = "PSG",
            strBadge = null,
            strLogo = null
        )

        val leagueTeams = LeagueTeams(
            teams = listOf(leagueTeam)
        )

        // When
        dataSource.saveLeagueTeam(leagueTeams, team)

        // Then
        val savedLeagueTeams = dataSource.getLeagueTeams("PSG")
        assertEquals(1, savedLeagueTeams?.teams?.size)
        assertEquals(team, savedLeagueTeams?.teams?.get(0)?.strTeam)
    }
}