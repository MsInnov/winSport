package com.mscode.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mscode.domain.common.WrapperResults.Error
import com.mscode.domain.common.WrapperResults.Success
import com.mscode.domain.availableleagues.usecase.GetAvailableLeaguesUseCase
import com.mscode.presentation.home.mapper.LeaguesUiMapper
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAvailableLeaguesUseCase: GetAvailableLeaguesUseCase,
    private val mapper: LeaguesUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchLeagues()
    }

    fun onEvent(uiEvent: UiEvent){
        when(uiEvent) {
            is UiEvent.QueryChanged -> onQueryChanged(uiEvent.query)
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
}