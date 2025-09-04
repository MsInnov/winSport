package com.mscode.data.leagueteam.repository

import com.mscode.data.leagueteam.api.LeagueTeamApi
import com.mscode.data.leagueteam.datasource.LeagueTeamLocalDataSource
import com.mscode.data.leagueteam.datasource.LeagueTeamRemoteDataSource
import com.mscode.data.leagueteam.mapper.LeagueTeamMapper
import com.mscode.data.leagueteam.model.LeagueTeamEntity
import com.mscode.data.leagueteam.model.LeagueTeamsEntity
import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.Path
import com.mscode.data.remoteconfig.model.Url
import com.mscode.data.remoteconfig.model.path_search_all_teams
import com.mscode.data.remoteconfig.model.url_the_sports_db
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.leagueteam.model.LeagueTeam
import com.mscode.domain.leagueteam.model.LeagueTeams
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LeagueTeamRepositoryImplTest {

    private lateinit var localConfigDataSource: LocalConfigDataSource
    private lateinit var retrofitFactory: RetrofitFactory
    private lateinit var leagueTeamMapper: LeagueTeamMapper
    private lateinit var leagueTeamLocalDataSource: LeagueTeamLocalDataSource
    private lateinit var repository: LeagueTeamRepositoryImpl

    private val testUrl = Url("123", "https://example.com")
    private val testPath = Path(path_search_all_teams, "path")

    @BeforeEach
    fun setUp() {
        localConfigDataSource = mockk()
        retrofitFactory = mockk()
        leagueTeamMapper = mockk()
        leagueTeamLocalDataSource = mockk(relaxed = true)

        repository = LeagueTeamRepositoryImpl(
            localConfigDataSource,
            retrofitFactory,
            leagueTeamMapper,
            leagueTeamLocalDataSource
        )
    }

    @Test
    fun `getLeagueTeam returns Success when everything works`() = runTest {
        // Given
        val api: LeagueTeamApi = mockk()
        val apiKey = "123"
        val path = "path"
        val league = "PSG"
        val remoteDataSource = mockk<LeagueTeamRemoteDataSource>()
        val leagueTeamEntity = LeagueTeamEntity(
            idTeam = "2",
            strTeam = league,
            strBadge = null,
            strLogo = null
        )
        val leagueTeamsEntity = LeagueTeamsEntity(
            teams = listOf(
                leagueTeamEntity
            )
        )
        val leagueTeam = LeagueTeam(
            idTeam = "2",
            strTeam = league,
            strBadge = null,
            strLogo = null
        )
        val leagueTeams = LeagueTeams(
            teams = listOf(leagueTeam)
        )

        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()
        every { localConfigDataSource.paths } returns listOf(testPath).toMutableList()
        every { leagueTeamLocalDataSource.getLeagueTeams(league) } returns null
        every { retrofitFactory.create(testUrl.value, LeagueTeamApi::class.java) } returns api
        every { leagueTeamMapper.toLeagueTeams(leagueTeamsEntity) } returns leagueTeams
        coEvery { remoteDataSource.getLeagueTeam(apiKey, path, league) } returns WrapperResults.Success(leagueTeamsEntity)
        mockkConstructor(LeagueTeamRemoteDataSource::class)
        coEvery { anyConstructed<LeagueTeamRemoteDataSource>().getLeagueTeam(apiKey, path, league) } returns WrapperResults.Success(leagueTeamsEntity)

        // When
        val result = repository.getLeagueTeam(league)

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(leagueTeams.teams, result.data.teams)
        verify { leagueTeamLocalDataSource.saveLeagueTeam(leagueTeams, league) }
    }

    @Test
    fun `getLeagueTeam returns Error when league teams URL is missing`() = runTest {
        val league = "PSG"
        every { localConfigDataSource.urls } returns emptyList<Url>().toMutableList()
        every { leagueTeamLocalDataSource.getLeagueTeams(league) } returns null

        val result = repository.getLeagueTeam(league)

        assertTrue(result is WrapperResults.Error)
        assertEquals("Available Leagues URL missing", result.exception.message)
    }

    @Test
    fun `getLeagueTeam returns Error when remote call fails`() = runTest {
        val api: LeagueTeamApi = mockk()
        val exception = RuntimeException("Network error")
        val apiKey = "123"
        val path = "path"
        val league = "PSG"

        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()
        every { localConfigDataSource.paths } returns listOf(testPath).toMutableList()
        every { retrofitFactory.create(testUrl.value, LeagueTeamApi::class.java) } returns api
        every { leagueTeamLocalDataSource.getLeagueTeams(league) } returns null
        mockkConstructor(LeagueTeamRemoteDataSource::class)
        coEvery { anyConstructed<LeagueTeamRemoteDataSource>().getLeagueTeam(apiKey, path, league) } returns WrapperResults.Error(exception)

        val result = repository.getLeagueTeam(league)

        assertTrue(result is WrapperResults.Error)
    }
}