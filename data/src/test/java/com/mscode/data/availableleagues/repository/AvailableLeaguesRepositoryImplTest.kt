package com.mscode.data.availableleagues.repository

import com.mscode.data.network.factory.RetrofitFactory
import com.mscode.data.availableleagues.api.AvailableLeaguesApi
import com.mscode.data.availableleagues.datasource.AvailableLeaguesLocalDataSource
import com.mscode.data.availableleagues.datasource.AvailableLeaguesRemoteDataSource
import com.mscode.data.availableleagues.mapper.LeagueMapper
import com.mscode.data.availableleagues.model.LeagueEntity
import com.mscode.data.availableleagues.model.LeaguesEntity
import com.mscode.data.remoteconfig.datasource.LocalConfigDataSource
import com.mscode.data.remoteconfig.model.Path
import com.mscode.data.remoteconfig.model.Url
import com.mscode.data.remoteconfig.model.key_api
import com.mscode.data.remoteconfig.model.path_all_leagues
import com.mscode.data.remoteconfig.model.url_the_sports_db
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.availableleagues.model.League
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class AvailableLeaguesRepositoryImplTest {

    private lateinit var localConfigDataSource: LocalConfigDataSource
    private lateinit var retrofitFactory: RetrofitFactory
    private lateinit var leagueMapper: LeagueMapper
    private lateinit var availableLeaguesLocalDataSource: AvailableLeaguesLocalDataSource
    private lateinit var repository: AvailableLeaguesRepositoryImpl

    private val testUrl = Url("123", "https://example.com")
    private val testPath = Path(path_all_leagues, "path")

    @BeforeEach
    fun setUp() {
        localConfigDataSource = mockk()
        retrofitFactory = mockk()
        leagueMapper = mockk()
        availableLeaguesLocalDataSource = mockk(relaxed = true)

        repository = AvailableLeaguesRepositoryImpl(
            localConfigDataSource,
            retrofitFactory,
            leagueMapper,
            availableLeaguesLocalDataSource
        )
    }

    @Test
    fun `getAvailableLeagues returns Success when everything works`() = runTest {
        // Given
        val leagueEntity = LeagueEntity("1", "league1", "foot")
        val leaguesEntity = LeaguesEntity(listOf(leagueEntity))
        val league = League(1, "league1", "foot")
        val api: AvailableLeaguesApi = mockk()
        val apiKey = "123"
        val path = "path"
        val remoteDataSource = mockk<AvailableLeaguesRemoteDataSource>()

        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()
        every { localConfigDataSource.paths } returns listOf(testPath).toMutableList()
        every { retrofitFactory.create(testUrl.value, AvailableLeaguesApi::class.java) } returns api
        every { leagueMapper.toLeague(leagueEntity) } returns league
        coEvery { remoteDataSource.getAvailableLeagues(apiKey, path) } returns WrapperResults.Success(leaguesEntity)
        mockkConstructor(AvailableLeaguesRemoteDataSource::class)
        coEvery { anyConstructed<AvailableLeaguesRemoteDataSource>().getAvailableLeagues(apiKey, path) } returns WrapperResults.Success(leaguesEntity)

        // When
        val result = repository.getAvailableLeagues()

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(listOf(league), result.data)
        verify { availableLeaguesLocalDataSource.saveAvailableLeagues(listOf(league)) }
    }

    @Test
    fun `getAvailableLeagues returns Error when available Leagues URL is missing`() = runTest {
        every { localConfigDataSource.urls } returns emptyList<Url>().toMutableList()

        val result = repository.getAvailableLeagues()

        assertTrue(result is WrapperResults.Error)
        assertEquals("Available Leagues URL missing", result.exception.message)
    }

    @Test
    fun `getAvailableLeagues returns Error when remote call fails`() = runTest {
        val api: AvailableLeaguesApi = mockk()
        val exception = RuntimeException("Network error")
        val apiKey = "123"
        val path = "path"

        every { localConfigDataSource.urls } returns listOf(testUrl).toMutableList()
        every { localConfigDataSource.paths } returns listOf(testPath).toMutableList()
        every { retrofitFactory.create(testUrl.value, AvailableLeaguesApi::class.java) } returns api
        mockkConstructor(AvailableLeaguesRemoteDataSource::class)
        coEvery { anyConstructed<AvailableLeaguesRemoteDataSource>().getAvailableLeagues(apiKey, path) } returns WrapperResults.Error(exception)

        val result = repository.getAvailableLeagues()

        assertTrue(result is WrapperResults.Error)
    }
}
