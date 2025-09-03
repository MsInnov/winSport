package com.mscode.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.availableleagues.usecase.GetAvailableLeaguesUseCase
import com.mscode.domain.common.WrapperResults
import com.mscode.domain.leagueteam.model.LeagueTeams
import com.mscode.domain.leagueteam.usecase.GetEveryOtherLeagueTeamUseCase
import com.mscode.domain.leagueteam.usecase.GetLeagueTeamUseCase
import com.mscode.domain.leagueteam.usecase.SortLeagueTeamsByNameDescendingUseCase
import com.mscode.presentation.home.mapper.LeaguesUiMapper
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiLeague
import com.mscode.presentation.home.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAvailableLeaguesUseCase: GetAvailableLeaguesUseCase,
    private val getLeagueTeamUseCase: GetLeagueTeamUseCase,
    private val sortLeagueTeamsByNameDescendingUseCase: SortLeagueTeamsByNameDescendingUseCase,
    private val getEveryOtherLeagueTeamUseCase: GetEveryOtherLeagueTeamUseCase,
    private val mapper: LeaguesUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchLeagues()
    }

    fun onEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            when(uiEvent) {
                is UiEvent.QueryChanged -> onQueryChanged(uiEvent.query)
                is UiEvent.LeagueSelected -> onLeagueSelected(uiEvent.league)
                is UiEvent.GetLeagueTeams -> getLeagueTeamUseCase(uiEvent.league).apply {
                    onLogoTeamsDisplayed(this)
                }
            }
        }
    }

    private fun fetchLeagues() {
        viewModelScope.launch {
            when (val result = getAvailableLeaguesUseCase()) {
                is Success -> {
                    _uiState.value = UiState.AvailableLeaguesState(leagues = mapper.toUiLeague(result.data))
                }
                is Error -> _uiState.value = UiState.Error
            }
        }
    }

    private fun onQueryChanged(query: String) {
        val currentState = _uiState.value
        if (currentState is UiState.AvailableLeaguesState) {
            val filtered = currentState.leagues.filter {
                it.league.contains(query, ignoreCase = true)
            }
            _uiState.value = currentState.copy(
                query = query,
                suggestions = filtered
            )
        }
    }

    private fun onLeagueSelected(league: UiLeague) {
        val currentState = _uiState.value
        if (currentState is UiState.AvailableLeaguesState) {
            _uiState.value = currentState.copy(
                query = league.league,
                suggestions = emptyList()
            )
        }
    }

    private fun onLogoTeamsDisplayed(leagueTeams: WrapperResults<LeagueTeams>) {
        when(leagueTeams) {
            is Success -> {
                val teamsSorted = sortLeagueTeamsByNameDescendingUseCase(leagueTeams.data.teams)
                val teamsOrganized = getEveryOtherLeagueTeamUseCase(teamsSorted)
                val uiTeam = teamsOrganized.map { mapper.toUiTeam(it) }
                val currentState = _uiState.value
                if (currentState is UiState.AvailableLeaguesState) {
                    _uiState.value = currentState.copy(teams = uiTeam)
                }
            }
            is Error -> _uiState.value = UiState.Error
        }
    }
}