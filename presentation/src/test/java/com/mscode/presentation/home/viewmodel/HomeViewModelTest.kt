package com.mscode.presentation.home.viewmodel

import app.cash.turbine.test
import com.mscode.domain.availableleagues.model.League
import com.mscode.domain.common.WrapperResults.*
import com.mscode.domain.availableleagues.usecase.GetAvailableLeaguesUseCase
import com.mscode.presentation.home.mapper.LeaguesUiMapper
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiState
import com.mscode.presentation.home.model.UiLeague
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getAvailableLeaguesUseCase: GetAvailableLeaguesUseCase = mockk()
    private val leaguesUiMapper: LeaguesUiMapper = mockk()
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Leagues UiState when use case returns Success`() = runTest {
        // Given
        val domainLeagues = listOf(
            League(1, "league1", "foot")
        )
        val uiLeagues = listOf(
            UiLeague("league1", "foot")
        )

        coEvery { getAvailableLeaguesUseCase() } returns Success(domainLeagues)
        every { leaguesUiMapper.toUiLeague(any()) } returns uiLeagues
        //When
        viewModel = HomeViewModel(getAvailableLeaguesUseCase, leaguesUiMapper)

        //Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.AvailableLeaguesState(uiLeagues), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Error UiState when use case returns Error`() = runTest {
        coEvery { getAvailableLeaguesUseCase() } returns Error(Exception("Something went wrong"))
        viewModel = HomeViewModel(getAvailableLeaguesUseCase, leaguesUiMapper)

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(UiState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `on query changed filters leagues`() = runTest {
        // Given
        val leagues = listOf(
            UiLeague("English Premier League", "Foot"),
            UiLeague("German Bundesliga", "Foot"),
            UiLeague("French Ligue 1", "Foot")
        )
        val domainLeagues = listOf(
            League(1, "English Premier League", "Foot"),
            League(2, "German Bundesliga", "Foot"),
            League(3, "French Ligue 1", "Foot")
        )
        val availableLeaguesState = UiState.AvailableLeaguesState(leagues)
        coEvery { getAvailableLeaguesUseCase() } returns Success(domainLeagues)
        coEvery { leaguesUiMapper.toUiLeague(domainLeagues) } returns leagues

        //When
        viewModel = HomeViewModel(getAvailableLeaguesUseCase, leaguesUiMapper)

        //Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(availableLeaguesState, awaitItem())
            viewModel.onEvent(UiEvent.QueryChanged("english"))
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(
                availableLeaguesState.copy(
                    query = "english",
                    suggestions = listOf(UiLeague("English Premier League", "Foot"))
                ),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
