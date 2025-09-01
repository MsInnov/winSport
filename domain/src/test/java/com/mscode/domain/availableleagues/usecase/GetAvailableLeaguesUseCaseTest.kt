package com.mscode.domain.availableleagues.usecase

import com.mscode.domain.availableleagues.model.League
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.availableleagues.repository.AvailableLeaguesRepository

class GetAvailableLeaguesUseCaseTest {

    private val repository: AvailableLeaguesRepository = mockk()
    private val useCase = GetAvailableLeaguesUseCase(repository)

    @Test
    fun `invoke should return Success result from repository`() = runTest {
        // Given
        val leagues = listOf(
            League(
                id = 1,
                league = "league1",
                sport = "foot"
            )
        )
        val expectedResult = WrapperResults.Success(leagues)
        coEvery { repository.getAvailableLeagues() } returns expectedResult

        // When
        val result = useCase()

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should return Error result from repository`() = runTest {
        // Given
        val expectedResult = WrapperResults.Error(Exception("Network error"))
        coEvery { repository.getAvailableLeagues() } returns expectedResult

        // When
        val result = useCase()

        // Then
        assertEquals(expectedResult, result)
    }
}