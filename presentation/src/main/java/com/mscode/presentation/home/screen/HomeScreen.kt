package com.mscode.presentation.home.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mscode.presentation.common.ErrorScreen
import com.mscode.presentation.home.model.UiState
import com.mscode.presentation.home.uicomponent.AvailableLeaguesPanel
import com.mscode.presentation.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val uiState = homeViewModel.uiState.collectAsState()
    when (val state = uiState.value) {
        is UiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        is UiState.Error -> ErrorScreen()
        is UiState.AvailableLeaguesState -> AvailableLeaguesPanel(state, homeViewModel)
    }
}