package com.mscode.presentation.home.model

sealed class UiState {

    data object Loading : UiState()
    data object Error : UiState()
    data class AvailableLeaguesState(
        val leagues: List<UiLeague> = emptyList(),
        val query: String = "",
        val suggestions: List<UiLeague> = emptyList(),
        val teams: List<UiTeam> = emptyList()
    ) : UiState()

}