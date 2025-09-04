package com.mscode.presentation.home.model

sealed class UiEvent {

    data class QueryChanged(val query: String): UiEvent()
    data class GetLeagueTeams(val league: String): UiEvent()
    data class LeagueSelected(val league: UiLeague) : UiEvent()

}