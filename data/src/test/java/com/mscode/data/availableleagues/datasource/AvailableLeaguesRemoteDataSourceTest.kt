package com.mscode.data.availableleagues.datasource

import com.mscode.data.availableleagues.api.AvailableLeaguesApi
import com.mscode.data.availableleagues.model.LeagueEntity
import com.mscode.data.availableleagues.model.LeaguesEntity
import com.mscode.domain.common.WrapperResults
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

class AvailableLeaguesRemoteDataSourceTest {

    private lateinit var api: AvailableLeaguesApi
    private lateinit var dataSource: AvailableLeaguesRemoteDataSource

    @BeforeEach
    fun setup() {
        api = mockk()
        dataSource = AvailableLeaguesRemoteDataSource(api)
    }

    @Test
    fun `getAllLeagues should return Success when API call is successful`() = runTest {
        // Given
        val expectedLeagues = LeaguesEntity(
            leagues = listOf(
                LeagueEntity(
                    "1",
                    "foot",
                    "league1"
                ),LeagueEntity(
                    "2",
                    "foot",
                    "league2"
                )
            )
        )
        val apiKey = "123"
        val path = "path"

        coEvery { api.getAllLeagues(apiKey, path) } returns Response.success(expectedLeagues)

        // When
        val result = dataSource.getAvailableLeagues(apiKey, path)

        // Then
        assertTrue(result is WrapperResults.Success)
        assertEquals(expectedLeagues, (result as WrapperResults.Success).data)
    }

    @Test
    fun `getAllLeagues should return Error when API throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        val apiKey = "123"
        val path = "path"
        coEvery { api.getAllLeagues(apiKey, path) } throws exception

        // When
        val result = dataSource.getAvailableLeagues(apiKey, path)

        // Then
        assertTrue(result is WrapperResults.Error)
        assertEquals(exception, (result as WrapperResults.Error).exception)
    }

    @Test
    fun `getAllLeagues should return Error when API call return null`() = runTest {
        // Given
        val apiKey = "123"
        val path = "path"
        coEvery { api.getAllLeagues(apiKey, path) } returns Response.success(null)

        // When
        val result = dataSource.getAvailableLeagues(apiKey, path)

        // Then
        assertTrue(result is WrapperResults.Error)
    }
}