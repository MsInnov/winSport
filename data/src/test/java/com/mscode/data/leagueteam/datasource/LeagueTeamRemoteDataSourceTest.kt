package com.mscode.data.leagueteam.datasource

import com.mscode.data.leagueteam.api.LeagueTeamApi
import com.mscode.data.leagueteam.model.LeagueTeamEntity
import com.mscode.data.leagueteam.model.LeagueTeamsEntity
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class LeagueTeamRemoteDataSourceTest {

    private lateinit var api: LeagueTeamApi
    private lateinit var dataSource: LeagueTeamRemoteDataSource

    @BeforeEach
    fun setup() {
        api = mockk()
        dataSource = LeagueTeamRemoteDataSource(api)
    }

    @Test
    fun `getAllLeagues should return Success when API call is successful`() = runTest {
        // Given
        val apiKey = "123"
        val path = "path"
        val team = "PSG"
        val expectedLeagues = LeagueTeamsEntity(
            teams = listOf(
                LeagueTeamEntity(
                    idTeam = "1",
                    strTeam = team,
                    strBadge = null,
                    strLogo = null
                )
            )
        )

        coEvery { api.getLeagueTeam(apiKey, path, team) } returns Response.success(expectedLeagues)

        // When
        val result = dataSource.getLeagueTeam(apiKey, path, team)

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(expectedLeagues, (result as WrapperResults.Success).data)
    }

    @Test
    fun `getLeagueTeam should return Error when API throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        val apiKey = "123"
        val path = "path"
        val team = "PSG"
        coEvery { api.getLeagueTeam(apiKey, path, team) } throws exception

        // When
        val result = dataSource.getLeagueTeam(apiKey, path, team)

        // Then
        assertTrue(result is WrapperResults.Error)
        assertEquals(exception, (result as WrapperResults.Error).exception)
    }

    @Test
    fun `getLeagueTeam should return Error when API call return null`() = runTest {
        // Given
        val apiKey = "123"
        val path = "path"
        val team = "PSG"
        coEvery { api.getLeagueTeam(apiKey, path, team) } returns Response.success(null)

        // When
        val result = dataSource.getLeagueTeam(apiKey, path, team)

        // Then
        assertTrue(result is WrapperResults.Error)
    }
}