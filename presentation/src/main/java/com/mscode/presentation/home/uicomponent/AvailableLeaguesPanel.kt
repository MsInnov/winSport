package com.mscode.presentation.home.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mscode.presentation.home.model.UiEvent
import com.mscode.presentation.home.model.UiState
import com.mscode.presentation.home.viewmodel.HomeViewModel

@Composable
fun AvailableLeaguesPanel(state: UiState.AvailableLeaguesState, homeViewModel: HomeViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        SearchField(
            query = state.query,
            onQueryChange = { homeViewModel.onEvent(UiEvent.QueryChanged(it)) },
            onClear = { homeViewModel.onEvent(UiEvent.QueryChanged("")) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SuggestionsList(
            suggestions = state.suggestions,
            onSuggestionClick = { suggestion ->
                homeViewModel.onEvent(UiEvent.LeagueSelected(suggestion))
                homeViewModel.onEvent(UiEvent.GetLeagueTeams(suggestion.league))
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (state.teams.isEmpty()) {
            PlaceholderInfo()
        }

        Spacer(modifier = Modifier.height(12.dp))

        TeamsGrid(teams = state.teams)
    }
}